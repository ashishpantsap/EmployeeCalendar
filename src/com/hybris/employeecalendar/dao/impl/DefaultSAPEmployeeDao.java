/**
 *
 */
package com.hybris.employeecalendar.dao.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.employeecalendar.dao.SAPEmployeeDao;
import com.hybris.employeecalendar.model.SapEmployeeModel;


/**
 * @author I310388
 *
 */
public class DefaultSAPEmployeeDao implements SAPEmployeeDao
{
	private FlexibleSearchService flexibleSearchService;
	private ModelService modelService;

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	@Autowired
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Autowired
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.employeecalendar.dao.SAPEmployeeDao#getSapEmployeeByInumber(java.lang.String)
	 */
	@Override
	public SapEmployeeModel getSapEmployeeByInumber(final String iNumber)
	{
		final String queryString = //
		"SELECT {p:" + SapEmployeeModel.PK + "}" //
				+ "FROM {" + SapEmployeeModel._TYPECODE + " AS p} "//
				+ "WHERE " + "{p:" + SapEmployeeModel.INUMBER + "}=?inumber ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("inumber", iNumber);

		return flexibleSearchService.<SapEmployeeModel> search(query).getResult().get(0);
	}


	@Override
	public List<SapEmployeeModel> getSapEmployees()
	{
		final String queryString = //
		"SELECT {p:" + SapEmployeeModel.PK + "}" //
				+ "FROM {" + SapEmployeeModel._TYPECODE + " AS p} ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		return flexibleSearchService.<SapEmployeeModel> search(query).getResult();
	}


	@Override
	public SapEmployeeModel getSapEmployeeByPK(final String pk)
	{
		return modelService.get(PK.parse(pk));
	}

}
