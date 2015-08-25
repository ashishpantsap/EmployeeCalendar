/**
 *
 */
package com.hybris.employeecalendar.dao.impl;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.employeecalendar.dao.CalendarEventDao;
import com.hybris.employeecalendar.model.SapEventModel;


/**
 * @author I310388
 *
 */
public class DefaultCalendarEventDao implements CalendarEventDao
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


	@Override
	public void saveEventOnCalendar(final SapEventModel event)
	{
		modelService.save(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.employeecalendar.dao.CalendarEventDao#saveEventsOnCalendar(java.util.List)
	 */
	@Override
	public void saveEventsOnCalendar(final List<SapEventModel> events)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.employeecalendar.dao.CalendarEventDao#deleteEventOnCalendar(com.hybris.employeecalendar.model.SapEventModel
	 * )
	 */
	@Override
	public void deleteEventOnCalendar(final SapEventModel event)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.employeecalendar.dao.CalendarEventDao#getMonthlyEventByInumber(java.lang.String)
	 */
	@Override
	public List<Date> getMonthlyEventByInumber(final String iNumber)
	{
		// YTODO Auto-generated method stub
		return null;
	}


}
