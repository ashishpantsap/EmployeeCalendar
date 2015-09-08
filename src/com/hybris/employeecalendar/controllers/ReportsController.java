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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hybris.employeecalendar.data.EventDto;
import com.hybris.employeecalendar.data.SAPEmployeeDto;
import com.hybris.employeecalendar.enums.EventType;
import com.hybris.employeecalendar.services.CalendarEventService;
import com.hybris.employeecalendar.services.SAPEmployeeService;


/**
 * @author I310388
 *
 */
@Controller
public class ReportsController
{
	private static final Logger LOG = Logger.getLogger(ReportsController.class.getName());

	private CalendarEventService calendarEventService;

	private SAPEmployeeService sapEmployeeService;

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


	@RequestMapping(value = "/reportspage", method = RequestMethod.GET)
	public String reportsPage(final Model model)
	{
		final List<SAPEmployeeDto> sapEmployees = sapEmployeeService.getSapEmployees();
		final SAPEmployeeDto allForQuery = new SAPEmployeeDto();
		allForQuery.setPK("ALL");
		allForQuery.setInumber("");
		allForQuery.setName("ALL");
		allForQuery.setSurname("");
		sapEmployees.add(allForQuery);
		model.addAttribute("employees", sapEmployees);

		final List<String> events = new ArrayList<String>();
		for (final EventType event : EventType.values())
		{
			events.add(event.getCode());
		}
		events.add("ALL");// added to specify the query ALL
		model.addAttribute("eventsType", events);
		return "reportspage";
	}

	@ResponseBody
	@RequestMapping(value = "/getresults", method = RequestMethod.POST, headers = "Accept=application/json")
	public List<EventDto> getReports(@RequestParam(value = "pk") String pkEmployee,
			@RequestParam(value = "typeevent") String typeevent, @RequestParam(value = "date") String date)
	{

		List<EventDto> events = null;
		if (date == null)
		{
			return Collections.EMPTY_LIST;
		}
		date += "-01 01";
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
		try
		{
			final Date parsedDate = format.parse(date);
			if (pkEmployee != null && pkEmployee.equals("ALL"))
			{
				pkEmployee = null;
			}
			if (typeevent != null && typeevent.equals("ALL"))
			{
				typeevent = null;
			}
			events = calendarEventService.getReport(parsedDate, typeevent != null ? EventType.valueOf(typeevent) : null, pkEmployee);
		}
		catch (final ParseException e)
		{
			LOG.error("error parsing date");
			return Collections.EMPTY_LIST;
		}
		return events;
	}



}
