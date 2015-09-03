/**
 *
 */
package com.hybris.employeecalendar.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

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


/**
 * @author I310388
 *
 */
@Controller
public class EventController
{

	private CalendarEventService calendarEventService;

	private SAPEmployeeService sapEmployeeService;

	private Map<String, String> eventTypeMapping;

	/**
	 * @param eventTypeMapping
	 *           the eventTypeMapping to set
	 */
	@Resource(name = "eventTypeMapping")
	public void setEventTypeMapping(final Map<String, String> eventTypeMapping)
	{
		this.eventTypeMapping = eventTypeMapping;
	}

	/**
	 * @param calendarEventService
	 *           the calendarEventService to set
	 */
	@Autowired
	public void setCalendarEventService(final CalendarEventService calendarEventService)
	{
		this.calendarEventService = calendarEventService;
	}

	/**
	 * @param sAPEmployeeService
	 *           the sAPEmployeeService to set
	 */
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




	@RequestMapping(value = "/deleteeventpage")
	public String deleteEvent()
	{
		return "deleteevent";
	}



	@ResponseBody
	@RequestMapping(value = "/sendevent", method = RequestMethod.POST, headers = "Accept=application/json")
	public MessageDto sendEvent(final Model model, @RequestParam(value = "inumber") final String pk,
			@RequestParam(value = "date") final String date, @RequestParam(value = "description") final String description,
			@RequestParam(value = "typeevent") final String typeevent)
	{

		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date dat = null;
		try
		{
			dat = format.parse(date);
		}
		catch (final ParseException e)
		{
			e.printStackTrace();
		}
		if (dat == null)
		{
			final MessageDto messageDto = createMessage("Error with the date submitted", Alerts.WARNING);
			model.addAttribute("message", messageDto);
			return messageDto;
		}


		if (pk == null || pk.equals(""))
		{
			final MessageDto messageDto = createMessage("No inumber submitted", Alerts.DANGER);
			model.addAttribute("message", messageDto);
			return messageDto;
		}

		final SapEmployeeModel employee = sapEmployeeService.getSapEmployeeByPK(pk);

		final EventDto event = new EventDto();

		event.setDate(dat);
		event.setDescription(description);
		event.setType(typeevent);

		MessageDto msave = null;
		try
		{
			calendarEventService.saveEventOnCalendar(event, employee);
		}
		catch (final Exception mse)
		{
			msave = createMessage(mse.getMessage(), Alerts.DANGER);
		}


		if (msave == null)
		{
			msave = createMessage("Event saved successfully", Alerts.SUCCESS);
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
			dateFrom = convertLongMillisecondsToDate(millisecondsfrom);
			dateTo = convertLongMillisecondsToDate(millisecondsto);
		}
		catch (final ParseException e)
		{
			e.printStackTrace();
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
			feedCalendar.setTitle(event.getType() + " " + event.getEmployee().getInumber() + " | " + event.getEmployee().getName()
					+ "  " + event.getDescription());
			feedCalendar.setUrl("#");
			feedCalendar.setStart(String.valueOf(event.getDate().getTime()));
			feedCalendar.setEnd(String.valueOf(event.getDate().getTime()));
			feedCalendar.setId(test);

			events.add(feedCalendar);
		}

		return events;

	}



	private MessageDto createMessage(final String description, final Alerts alert)
	{
		final MessageDto messageDto = new MessageDto();
		messageDto.setAlert(alert);
		messageDto.setDescription(description);
		return messageDto;
	}

	private Date convertLongMillisecondsToDate(final long milliseconds) throws ParseException
	{
		final Calendar calendar = Calendar.getInstance();
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		calendar.setTimeInMillis(milliseconds);
		final String dateInFormat = format.format(calendar.getTime());
		return format.parse(dateInFormat);
	}


}
