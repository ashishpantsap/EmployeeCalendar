/**
 *
 */
package com.hybris.employeecalendar.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.employeecalendar.dao.SAPEmployeeDao;
import com.hybris.employeecalendar.data.SAPEmployeeDto;
import com.hybris.employeecalendar.model.SapEmployeeModel;
import com.hybris.employeecalendar.services.SAPEmployeeService;


/**
 * @author I310388
 *
 */
public class DefaultSAPEmployeeService implements SAPEmployeeService
{

	private SAPEmployeeDao sapEmployeeDao;


	@Autowired
	public void setSapEmployeeDao(final SAPEmployeeDao sapEmployeeDao)
	{
		this.sapEmployeeDao = sapEmployeeDao;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.employeecalendar.services.SAPEmployeeService#getSapEmployeeByInumber(java.lang.String)
	 */
	@Override
	public SAPEmployeeDto getSapEmployeeByInumber(final String iNumber)
	{
		final SapEmployeeModel sapEmployeeModel = sapEmployeeDao.getSapEmployeeByInumber(iNumber);

		final SAPEmployeeDto sapEmployeeDto = new SAPEmployeeDto();
		sapEmployeeDto.setInumber(sapEmployeeModel.getInumber());
		sapEmployeeDto.setName(sapEmployeeModel.getName());
		sapEmployeeDto.setSurname(sapEmployeeModel.getSurname());

		return sapEmployeeDto;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.employeecalendar.services.SAPEmployeeService#getSapEmployees()
	 */
	@Override
	public List<SAPEmployeeDto> getSapEmployees()
	{
		final List<SapEmployeeModel> sapEmployees = sapEmployeeDao.getSapEmployees();

		if (sapEmployees == null || sapEmployees.size() == 0)
		{
			return Collections.EMPTY_LIST;
		}
		final List<SAPEmployeeDto> employees = new ArrayList<SAPEmployeeDto>();
		for (final SapEmployeeModel model : sapEmployees)
		{
			final SAPEmployeeDto employeeDto = new SAPEmployeeDto();
			employeeDto.setPK(model.getPk().toString());
			employeeDto.setName(model.getName());
			employeeDto.setSurname(model.getSurname());
			employeeDto.setInumber(model.getInumber());

			employees.add(employeeDto);
		}

		return employees;
	}


	@Override
	public SapEmployeeModel getSapEmployeeByPK(final String pk)
	{
		final SapEmployeeModel sapEmployeeModel = sapEmployeeDao.getSapEmployeeByPK(pk);

		return sapEmployeeModel;
	}


	@Override
	public void saveEmployee(final SapEmployeeModel model)
	{
		sapEmployeeDao.saveEmployeee(model);
	}
}
