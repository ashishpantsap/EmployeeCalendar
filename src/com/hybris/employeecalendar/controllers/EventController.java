/**
 *
 */
package com.hybris.employeecalendar.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hybris.employeecalendar.data.EventDto;
import com.hybris.employeecalendar.data.SAPEmployeeDto;
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
		return "submiteventpage";
	}

	@RequestMapping(value = "/deleteeventpage")
	public String deleteEvent()
	{
		return "deleteevent";
	}

	@RequestMapping(value = "/sendevent", method = RequestMethod.POST)
	public String sendEvent(final Model model, @RequestParam(value = "inumber") final String pk,
			@RequestParam(value = "date") final String date)
	{

		final DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
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
			model.addAttribute("message", "Error with the date submitted");
			return "submiteventpage";
		}


		if (pk == null || pk.equals(""))
		{
			model.addAttribute("message", "No inumber submitted");
			return "submiteventpage";
		}

		final SapEmployeeModel employee = sapEmployeeService.getSapEmployeeByPK(pk);

		final EventDto event = new EventDto();

		event.setDate(dat);

		calendarEventService.saveEventOnCalendar(event, employee);


		return "redirect:" + "submiteventpage";
	}
}
