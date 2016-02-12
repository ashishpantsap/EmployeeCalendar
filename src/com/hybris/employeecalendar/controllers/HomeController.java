/**
 *
 */
package com.hybris.employeecalendar.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hybris.employeecalendar.enums.TrainingType;



@Controller
public class HomeController
{

	@RequestMapping(value = "/home")
	public String home(final Model model)
	{
		final List<String> listOfTrainings = new ArrayList<>();
		for (final TrainingType training : TrainingType.values())
		{
			listOfTrainings.add(training.getCode());
		}
		model.addAttribute("trainings", listOfTrainings);
		return "home";
	}
}
