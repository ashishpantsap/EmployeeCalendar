/**
 *
 */
package com.hybris.employeecalendar.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.hybris.employeecalendar.services.CalendarEventService;
import com.hybris.employeecalendar.services.SAPEmployeeService;
import com.hybris.employeecalendar.util.HelperUtil;


/**
 * @author I310388
 *
 */
@Controller
public class EventController
{

	private static final Logger LOG = Logger.getLogger(EventController.class.getName());

	private CalendarEventService calendarEventService;

	private SAPEmployeeService sapEmployeeService;

	private Map<String, String> eventTypeMapping;


	@Resource(name = "eventTypeMapping")
	public void setEventTypeMapping(final Map<String, String> eventTypeMapping)
	{
		this.eventTypeMapping = eventTypeMapping;
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
	public MessageDto sendEvent(final Model model,//
			@RequestParam(value = "pk") final String pk,//
			@RequestParam(value = "fromDate") final String fromDate, //
			@RequestParam(value = "toDate", required = false) final String toDate, //
			@RequestParam(value = "description", required = false) final String description,//
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

		//		if (datet == null)
		//		{
		//			final Calendar cal = Calendar.getInstance();
		//			cal.setTime(datef);
		//			cal.add(Calendar.HOUR_OF_DAY, 1);
		//			datet = cal.getTime();
		//		}


		if (pk == null || pk.equals(""))
		{
			final MessageDto messageDto = HelperUtil.createMessage("No inumber submitted", Alerts.DANGER);
			model.addAttribute("message", messageDto);
			return messageDto;
		}

		final SapEmployeeModel employee = sapEmployeeService.getSapEmployeeByPK(pk);

		final EventDto event = new EventDto();

		event.setFromDate(datef);
		event.setToDate(datet);
		if (typeevent.equals("TRAINING"))
		{
			event.setDescription(description);
		}
		event.setType(typeevent);

		MessageDto msave = null;
		try
		{
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
	public List<FeedCalendarDto> feedCalendarAll(@RequestParam(value = "month") final String month,
			@RequestParam(value = "from") final String from, @RequestParam(value = "to") final String to)
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

		final List<EventDto> eventsDto = calendarEventService.getMonthlyScheduleFromDateToDate(dateFrom, dateTo);
		if (eventsDto.size() == 0)
		{
			return Collections.EMPTY_LIST;
		}

		final List<FeedCalendarDto> events = new ArrayList<FeedCalendarDto>();
		int i = 0;
		String test = "test";
		for (final EventDto event : eventsDto)
		{
			test = test + "" + i++;
			final FeedCalendarDto feedCalendar = new FeedCalendarDto();
			feedCalendar.setClassevent(eventTypeMapping.get(event.getType()) != null ? eventTypeMapping.get(event.getType())
					: "event-success");
			feedCalendar.setTitle(event.getType() + " - " + event.getEmployee().getName() + "  " + event.getDescription());
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
}
