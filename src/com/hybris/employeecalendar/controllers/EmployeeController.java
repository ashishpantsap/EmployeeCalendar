/**
 *
 */
package com.hybris.employeecalendar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hybris.employeecalendar.data.SAPEmployeeDto;
import com.hybris.employeecalendar.services.SAPEmployeeService;


@Controller
public class EmployeeController
{
	private SAPEmployeeService sapEmployeeService;

	@Autowired
	public void setSapEmployeeService(final SAPEmployeeService sapEmployeeService)
	{
		this.sapEmployeeService = sapEmployeeService;
	}

	@RequestMapping(value = "/sapemployees", method = RequestMethod.GET)
	@ResponseBody
	public List<SAPEmployeeDto> sapEmployees()
	{
		final List<SAPEmployeeDto> sapEmployees = sapEmployeeService.getSapEmployees();

		return sapEmployees;
	}

}
