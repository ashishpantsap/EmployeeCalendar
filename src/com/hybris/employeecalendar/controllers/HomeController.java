/**
 *
 */
package com.hybris.employeecalendar.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hybris.employeecalendar.enums.TrainingType;


/**
 * @author I310388
 *
 */
@Controller
public class HomeController
{

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
}
