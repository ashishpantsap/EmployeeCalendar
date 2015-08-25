/**
 *
 */
package com.hybris.employeecalendar.dao;

import java.util.List;

import com.hybris.employeecalendar.model.SapEmployeeModel;


/**
 * @author I310388
 *
 */
public interface SAPEmployeeDao
{
	public SapEmployeeModel getSapEmployeeByInumber(String iNumber);

	public SapEmployeeModel getSapEmployeeByPK(String pk);

	public List<SapEmployeeModel> getSapEmployees();
}
