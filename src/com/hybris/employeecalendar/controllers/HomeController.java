/**
 *
 */
package com.hybris.employeecalendar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author I310388
 *
 */
@Controller
public class HomeController
{
	@RequestMapping(value = "/viewcalendarpage")
	public String viewcalendar()
	{
		return "calendar";
	}

	@RequestMapping(value = "/home")
	public String home()
	{
		return "home";
	}
}
