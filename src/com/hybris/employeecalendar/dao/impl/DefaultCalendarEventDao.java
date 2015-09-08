/**
 *
 */
package com.hybris.employeecalendar.dao.impl;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.employeecalendar.dao.CalendarEventDao;
import com.hybris.employeecalendar.data.DateRangeDto;
import com.hybris.employeecalendar.enums.EventType;
import com.hybris.employeecalendar.model.SapEventModel;
import com.hybris.employeecalendar.util.HelperUtil;


/**
 * @author I310388
 *
 */
public class DefaultCalendarEventDao implements CalendarEventDao
{

	private FlexibleSearchService flexibleSearchService;
	private ModelService modelService;


	@Autowired
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

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

	@Override
	public void saveEventsOnCalendar(final List<SapEventModel> events)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void deleteEventOnCalendar(final SapEventModel event)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public List<Date> getMonthlyEventByInumber(final String iNumber)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SapEventModel> getSapEventByInumberAndDate(final String iNumber, final Date fromDate) throws ParseException
	{
		final String queryString = //
		"SELECT {e:PK } " //
				+ "FROM { SapEmployee AS p JOIN SapEvent AS e " + "ON {p:PK} = {e:employee} } "//
				+ "WHERE {p:INUMBER}=?inumber "//
				+ "AND {e:FROMDATE} >= ?fromDate " //;
				+ "AND {e:TODATE} <= ?toDate";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("inumber", iNumber);
		final DateRangeDto dateRange = HelperUtil.getDateRangeOfTheDay(fromDate);

		query.addQueryParameter("fromDate", dateRange.getFromDate());
		query.addQueryParameter("toDate", dateRange.getToDate());
		final List<SapEventModel> result = flexibleSearchService.<SapEventModel> search(query).getResult();

		return result;
	}

	@Override
	public List<SapEventModel> getMonthlyScheduleFromDateToDate(final Date from, final Date to)
	{
		final String queryString = //
		"SELECT {e:PK }" //
				+ "FROM { SapEmployee AS p JOIN SapEvent AS e " + "ON {p:PK} = {e:employee} } "//
				+ "WHERE {e:FROMDATE} >= ?from "//
				+ "AND {e:TODATE} <= ?to "//
				+ "ORDER BY {e:FROMDATE} ASC";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("from", from);
		query.addQueryParameter("to", to);
		final List<SapEventModel> result = flexibleSearchService.<SapEventModel> search(query).getResult();

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.employeecalendar.dao.CalendarEventDao#getQMfromDate(java.util.Date)
	 */
	@Override
	public SapEventModel getTypeEventFromDate(final Date date, final EventType eventType) throws ParseException
	{
		if (date == null || eventType == null)
		{
			return null;
		}

		final DateRangeDto dateRange = HelperUtil.getDateRangeOfTheDay(date);

		final String queryString = //
		"SELECT {e:PK } " //
				+ "FROM { SapEvent AS e } "//
				+ "WHERE  {e:TYPE} = ?eventType "//
				+ "AND {e:FROMDATE} >= ?from "//
				+ "AND {e:TODATE} <= ?to ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("from", dateRange.getFromDate());
		query.addQueryParameter("to", dateRange.getToDate());
		query.addQueryParameter("eventType", eventType);
		final List<SapEventModel> result = flexibleSearchService.<SapEventModel> search(query).getResult();

		if (result == null || result.size() == 0)
		{
			return null;
		}

		return result.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.employeecalendar.dao.CalendarEventDao#getReport(java.util.Date,
	 * com.hybris.employeecalendar.enums.EventType, java.lang.String)
	 */
	@Override
	public List<SapEventModel> getReport(final Date date, final EventType event, final String PK) throws ParseException
	{
		if (date == null)
		{
			return null;
		}

		final DateRangeDto dateRange = HelperUtil.getDateFirstToLastofTheMonth(date);

		final String queryString = //
		"SELECT {e:PK } " //
				+ "FROM { SapEvent AS e JOIN SapEmployee AS em ON {e:employee} = {em:PK}} "//
				+ "WHERE {e:FROMDATE} >= ?from "//
				+ "AND {e:TODATE} <= ?to ";


		final StringBuilder buildString = new StringBuilder();
		buildString.append(queryString);
		if (event != null)
		{
			buildString.append(" AND {e:TYPE}=?eventType");
		}
		if (PK != null)
		{
			buildString.append(" AND {em:PK}=?pk");
		}
		buildString.append(" ORDER BY {e:FROMDATE} ASC");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(buildString);
		query.addQueryParameter("from", dateRange.getFromDate());
		query.addQueryParameter("to", dateRange.getToDate());
		if (event != null)
		{
			query.addQueryParameter("eventType", event);
		}
		if (PK != null)
		{
			query.addQueryParameter("pk", PK);
		}

		final List<SapEventModel> result = flexibleSearchService.<SapEventModel> search(query).getResult();

		if (result == null || result.size() == 0)
		{
			return null;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.employeecalendar.dao.CalendarEventDao#deleteEventsInTheDay(java.util.Date, java.lang.String)
	 */
	@Override
	public void deleteEventsInTheDay(final Date date, final String PK) throws ParseException
	{
		if (date == null || PK == null)
		{
			return;
		}

		final DateRangeDto dateRange = HelperUtil.getDateRangeOfTheDay(date);

		final String queryString = //
		"SELECT {e:PK } " //
				+ "FROM { SapEvent AS e JOIN SapEmployee AS em ON {e:employee} = {em:PK}} "//
				+ "WHERE {e:FROMDATE} >= ?from "//
				+ "AND {e:TODATE} <= ?to "//
				+ "AND {em:PK}=?pk";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("from", dateRange.getFromDate());
		query.addQueryParameter("to", dateRange.getToDate());
		query.addQueryParameter("pk", PK);
		final List<SapEventModel> result = flexibleSearchService.<SapEventModel> search(query).getResult();

		if (result == null || result.size() == 0)
		{
			return;
		}

		modelService.removeAll(result);

	}
}
