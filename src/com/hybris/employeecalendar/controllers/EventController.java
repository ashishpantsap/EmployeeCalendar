/**
 *
 */
package com.hybris.employeecalendar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author I310388
 *
 */
@Controller
public class EventController
{

	@RequestMapping(value = "/submitevent", method = RequestMethod.GET)
	public String submitEvent()
	{
		return "submitevent";
	}

	@RequestMapping(value = "/deleteevent")
	public String deleteEvent()
	{
		return "deleteevent";
	}

	@RequestMapping(value = "/sendevent", method = RequestMethod.POST)
	public String sendEvent(final Model model)
	{
		//System.out.println(event.getInumber() + " " + event.getDate());
		return "submitevent";
	}
}
