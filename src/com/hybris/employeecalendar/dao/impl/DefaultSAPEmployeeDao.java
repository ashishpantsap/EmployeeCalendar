/**
 *
 */
package com.hybris.employeecalendar.dao.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Date;
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
		"SELECT {p:PK}" //
				+ "FROM { SapEmployee  AS p} "//
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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.employeecalendar.dao.SAPEmployeeDao#getSapEmployeeByInumberAndDate(java.lang.String,
	 * java.util.Date)
	 */
	@Override
	public SapEmployeeModel getSapEmployeeByInumberAndDate(final String iNumber, final Date date)
	{
		final String queryString = //
		"SELECT {p:PK }" //
				+ "FROM { SapEmployee AS p JOIN SapEvent AS e " + "ON {p:PK} = {e:employee} } "//
				+ "WHERE {p:INUMBER}=?inumber "//
				+ "AND {e:DATE}=?date";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("inumber", iNumber);
		query.addQueryParameter("date", date);
		final List<SapEmployeeModel> result = flexibleSearchService.<SapEmployeeModel> search(query).getResult();
		if (result != null && result.size() > 0)
		{
			return result.get(0);
		}
		else
		{
			return null;
		}
	}


	@Override
	public void saveEmployeee(final SapEmployeeModel model)
	{
		modelService.save(model);
	}

}
