/**
 *
 */
package com.hybris.employeecalendar.controllers;

import de.hybris.platform.servicelayer.model.ModelService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hybris.employeecalendar.data.EventDto;
import com.hybris.employeecalendar.data.FeedCalendarDto;
import com.hybris.employeecalendar.data.MessageDto;
import com.hybris.employeecalendar.data.SAPEmployeeDto;
import com.hybris.employeecalendar.data.enums.Alerts;
import com.hybris.employeecalendar.enums.EventType;
import com.hybris.employeecalendar.model.SapEmployeeModel;
import com.hybris.employeecalendar.model.SapEventModel;
import com.hybris.employeecalendar.services.CalendarEventService;
import com.hybris.employeecalendar.services.SAPEmployeeService;
import com.hybris.employeecalendar.util.HelperUtil;


@Controller
public class EventController
{

	private static final Logger LOG = Logger.getLogger(EventController.class.getName());

	private CalendarEventService calendarEventService;

	private SAPEmployeeService sapEmployeeService;

	private ModelService modelService;

	private Map<String, String> eventTypeMapping;

	private Map<String, String> eventTypeShortName;

	@Resource(name = "eventTypeMapping")
	public void setEventTypeMapping(final Map<String, String> eventTypeMapping)
	{
		this.eventTypeMapping = eventTypeMapping;
	}

	@Resource(name = "eventTypeShortName")
	public void setEventTypeShortName(final Map<String, String> eventTypeShortName)
	{
		this.eventTypeShortName = eventTypeShortName;
	}


	@Autowired
	public void setCalendarEventService(final CalendarEventService calendarEventService)
	{
		this.calendarEventService = calendarEventService;
	}


	@Autowired
	public void setSapEmployeeService(final SAPEmployeeService sapEmployeeService)
	{
		this.sapEmployeeService = sapEmployeeService;
	}

	@Autowired
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@RequestMapping(value = "/submiteventpage", method = RequestMethod.GET)
	public String submitEvent(final Model model)
	{
		final List<SAPEmployeeDto> sapEmployees = sapEmployeeService.getSapEmployees();
		model.addAttribute("employees", sapEmployees);

		final List<String> events = new ArrayList<String>();
		for (final EventType event : EventType.values())
		{
			events.add(event.getCode());
		}

		model.addAttribute("events", events);
		return "submiteventpage";
	}


	@ResponseBody
	@RequestMapping(value = "/sendevent", method = RequestMethod.POST, headers = "Accept=application/json")
	public MessageDto sendEvent(final Model model, //
			@RequestParam(value = "pk") final String pk, //
			@RequestParam(value = "fromDate") final String fromDate, //
			@RequestParam(value = "toDate", required = false) final String toDate, //
			@RequestParam(value = "description", required = false) final String description, //
			@RequestParam(value = "training-time", required = false) final String trainingTime, //
			@RequestParam(value = "typeevent") final String typeevent)
	{
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date datef = null;
		Date datet = null;
		try
		{
			datef = format.parse(fromDate);
			datet = format.parse(toDate);
		}
		catch (final ParseException e)
		{
			LOG.debug("error parsing date");
		}

		if (datef == null || datef.after(datet))
		{
			final MessageDto messageDto = HelperUtil.createMessage("Error with the date submitted", Alerts.WARNING);
			model.addAttribute("message", messageDto);
			return messageDto;
		}
		if (pk == null || pk.equals(""))
		{
			final MessageDto messageDto = HelperUtil.createMessage("No inumber submitted", Alerts.DANGER);
			model.addAttribute("message", messageDto);
			return messageDto;
		}

		final SapEmployeeModel employee = sapEmployeeService.getSapEmployeeByPK(pk);

		EventDto event = new EventDto();

		event.setFromDate(datef);
		event.setToDate(datet);
		event.setDescription(description == null ? "" : description);
		event.setType(typeevent);
		event.setTrainingTime(trainingTime);
		MessageDto msave = null;
		try
		{
			event = HelperUtil.getDateRangeFromEventType(event);
			calendarEventService.saveEventOnCalendar(event, employee);
		}
		catch (final Exception mse)
		{
			msave = HelperUtil.createMessage(mse.getMessage(), Alerts.DANGER);
		}


		if (msave == null)
		{
			msave = HelperUtil.createMessage("Event saved successfully", Alerts.SUCCESS);
		}

		model.addAttribute("message", msave);

		return msave;
	}


	@ResponseBody
	@RequestMapping(value = "/feedCalendar", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<FeedCalendarDto> feedCalendarAll( //
			@RequestParam(value = "month") final String month, //
			@RequestParam(value = "from") final String from, //
			@RequestParam(value = "to") final String to)
	{
		final long millisecondsfrom = Long.parseLong(from);
		final long millisecondsto = Long.parseLong(to);

		Date dateFrom = null;
		Date dateTo = null;
		try
		{
			dateFrom = HelperUtil.convertLongMillisecondsToDate(millisecondsfrom);
			dateTo = HelperUtil.convertLongMillisecondsToDate(millisecondsto);
		}
		catch (final ParseException e)
		{
			LOG.error(e.getMessage());
			return Collections.EMPTY_LIST;
		}
		final List<EventDto> eventsDto = calendarEventService.getMonthlyScheduleOnCallAndQM(dateFrom, dateTo);
		if (eventsDto.size() == 0)
		{
			return Collections.EMPTY_LIST;
		}

		final List<FeedCalendarDto> events = new ArrayList<FeedCalendarDto>();
		int i = 0;
		String test = "test";
		for (final EventDto event : eventsDto)
		{
			final String employee = event.getEmployee().getName() + " ";
			test = test + "" + i++;
			final FeedCalendarDto feedCalendar = new FeedCalendarDto();
			feedCalendar.setClassevent(eventTypeMapping.get(event.getType()) != null ? eventTypeMapping.get(event.getType())
					: "event-success");
			feedCalendar.setTitle(eventTypeShortName.get(event.getType()) != null ? eventTypeShortName.get(event.getType()) + "  "
					+ employee : event.getType() + " " + employee);
			feedCalendar.setUrl(event.getType());
			feedCalendar.setStart(String.valueOf(event.getFromDate().getTime()));
			feedCalendar.setEnd(String.valueOf(event.getToDate().getTime()));
			feedCalendar.setId(test);

			events.add(feedCalendar);
		}

		return events;

	}

	@RequestMapping(value = "/sapemployees", method = RequestMethod.GET)
	@ResponseBody
	public List<SAPEmployeeDto> sapEmployees()
	{
		final List<SAPEmployeeDto> sapEmployees = sapEmployeeService.getSapEmployees();

		return sapEmployees;
	}

	@RequestMapping(value = "/sapevents", method = RequestMethod.GET)
	@ResponseBody
	public List<String> sapEvents()
	{
		final List<String> events = new ArrayList<String>();
		for (final EventType event : EventType.values())
		{
			events.add(event.getCode());
		}
		return events;
	}

	@RequestMapping(value = "/daysevents", method = RequestMethod.GET)
	@ResponseBody
	public List<EventDto> daysEvents(@RequestParam(value = "date") final Date date)
	{
		List<EventDto> events = new ArrayList<EventDto>();
		try
		{
			events = calendarEventService.getAllEventsForDay(date);
		}
		catch (final ParseException e)
		{
			LOG.info("Could Not Retrieve Events For Today");
		}

		return events;
	}

	//Warning: The deletion doesn't take into account hh:mm
	@RequestMapping(value = "/deleteevent", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public void deleteevent(@RequestParam(value = "name") final String name, @RequestParam(value = "event") final String event,
			@RequestParam(value = "date") final Date date) throws Exception
	{
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		final List<SapEventModel> events = calendarEventService
				.getAllEventsInTheDay(format.parse(format.format(date)), name, event); //NEED TO FORMAT DATE
		final Iterator i = events.iterator();
		SapEventModel sapEventModel;
		while (i.hasNext())
		{
			sapEventModel = (SapEventModel) i.next();
			final String dtoDate = format.format(sapEventModel.getFromDate());
			final String newEvent = sapEventModel.getType().toString();
			final String newDate = format.format(date);
			final String newName = sapEventModel.getEmployee().getName() + "," + sapEventModel.getEmployee().getSurname();
			if (newName.equals(name) && dtoDate.equals(newDate) && newEvent.equals(event))
			{
				modelService.remove(sapEventModel);
			}
		}
	}
}
