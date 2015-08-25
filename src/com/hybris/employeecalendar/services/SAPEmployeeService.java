/**
 *
 */
package com.hybris.employeecalendar.services;

import java.util.List;

import com.hybris.employeecalendar.data.SAPEmployeeDto;
import com.hybris.employeecalendar.model.SapEmployeeModel;



/**
 * @author I310388
 *
 */
public interface SAPEmployeeService
{

	public SAPEmployeeDto getSapEmployeeByInumber(String iNumber);

	public SapEmployeeModel getSapEmployeeByPK(String pk);

	public List<SAPEmployeeDto> getSapEmployees();

}
