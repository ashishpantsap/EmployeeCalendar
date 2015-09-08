package com.hybris.employeecalendar.controllers;

import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.hybris.employeecalendar.data.MessageDto;
import com.hybris.employeecalendar.data.SAPEmployeeDto;
import com.hybris.employeecalendar.data.enums.Alerts;
import com.hybris.employeecalendar.services.CalendarEventService;
import com.hybris.employeecalendar.services.SAPEmployeeService;
import com.hybris.employeecalendar.util.HelperUtil;


@Controller
public class DeleteEventController
{
	private static final Logger LOG = Logger.getLogger(DeleteEventController.class.getName());

	private CalendarEventService calendarEventService;

	private SAPEmployeeService sapEmployeeService;

	@Autowired
	public void setSapEmployeeService(final SAPEmployeeService sapEmployeeService)
	{
		this.sapEmployeeService = sapEmployeeService;
	}

	@Autowired
	public void setCalendarEventService(final CalendarEventService calendarEventService)
	{
		this.calendarEventService = calendarEventService;
	}

	@RequestMapping(value = "/deleteeventpage")
	public String deleteEvent(final Model model)
	{
		final List<SAPEmployeeDto> sapEmployees = sapEmployeeService.getSapEmployees();
		model.addAttribute("employees", sapEmployees);

		return "deleteeventpage";
	}


	@ResponseBody
	@RequestMapping(value = "/deleteevents", method = RequestMethod.POST, headers = "Accept=application/json")
	public MessageDto deleteEvent(//
			@RequestParam(value = "date") final String date, //
			@RequestParam(value = "pk") final String pk //
	)
	{
		MessageDto message = null;

		if (date == null || pk == null)
		{
			message = HelperUtil.createMessage("error with parameter : date/pk is null", Alerts.WARNING);
			return message;
		}

		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			final Date parsedDate = format.parse(date);

			calendarEventService.deleteEventsInTheDay(parsedDate, pk);
		}
		catch (final ParseException pe)
		{
			message = HelperUtil.createMessage("error parsing date", Alerts.WARNING);
		}
		catch (final ModelRemovalException mre)
		{
			message = HelperUtil.createMessage("error removing events", Alerts.DANGER);
		}
		catch (final Exception e)
		{
			message = HelperUtil.createMessage(e.getMessage(), Alerts.DANGER);
		}

		if (message == null)
		{
			message = HelperUtil.createMessage("events have been deleted from database", Alerts.SUCCESS);
		}

		return message;

	}



}
