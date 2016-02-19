/**
 *
 */
package com.hybris.employeecalendar.controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import com.hybris.employeecalendar.enums.TrainingType;
import com.hybris.employeecalendar.services.CalendarEventService;
import com.hybris.employeecalendar.util.HelperUtil;


@Controller
public class HomeController
{
	private CalendarEventService calendarEventService;
	private Map<String, String> eventTypeMapping;
	private Map<String, String> eventTypeShortName;
	private static final Logger LOG = Logger.getLogger(HomeController.class.getName());

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

	@RequestMapping(value = "/home")
	public String home(final Model model, @RequestParam(value = "eventsMutated", required = false) final String eventsMutated)
	{

		final List<String> listOfTrainings = new ArrayList<>();
		for (final TrainingType training : TrainingType.values())
		{
			listOfTrainings.add(training.getCode());
		}
		model.addAttribute("trainings", listOfTrainings);
		if (eventsMutated != null)
		{
			model.addAttribute("eventsMutated", eventsMutated);
		}
		return "home";
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
			feedCalendar.setClassevent(
					eventTypeMapping.get(event.getType()) != null ? eventTypeMapping.get(event.getType()) : "event-success");
			feedCalendar.setTitle(eventTypeShortName.get(event.getType()) != null
					? eventTypeShortName.get(event.getType()) + "  " + employee : event.getType() + " " + employee);
			feedCalendar.setUrl(event.getType());
			feedCalendar.setStart(String.valueOf(event.getFromDate().getTime()));
			feedCalendar.setEnd(String.valueOf(event.getToDate().getTime()));
			feedCalendar.setId(test);

			events.add(feedCalendar);
		}

		return events;
	}

	//	@RequestMapping(value = "/home", method = RequestMethod.POST, headers = "Accept=application/json")
	//	public String showHome(final Model model, //
	//			@RequestParam(value = "message", required = false) final String message, //
	//			@RequestParam(value = "alert", required = false) final String alert)
	//	{
	//		if (message != null && alert != null)
	//		{
	//			final MessageDto messageDto = HelperUtil.createMessage(message, Enum.valueOf(Alerts.class, alert.toUpperCase()));
	//			model.addAttribute("messageDto", messageDto);
	//		}
	//		return home(model, null);
	//	}

}
