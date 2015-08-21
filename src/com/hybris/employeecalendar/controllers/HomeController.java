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
@RequestMapping("employeecalendar")
public class HomeController
{
	@RequestMapping(value = "/viewcalendar")
	public String viewcalendar()
	{
		return "calendar";
	}
}
