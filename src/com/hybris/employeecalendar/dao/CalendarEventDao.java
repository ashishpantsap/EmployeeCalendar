/**
 *
 */
package com.hybris.employeecalendar.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.hybris.employeecalendar.enums.EventType;
import com.hybris.employeecalendar.model.SapEventModel;


/**
 * @author I310388
 *
 */
public interface CalendarEventDao
{
	public void saveEventOnCalendar(SapEventModel event);

	public void saveEventsOnCalendar(List<SapEventModel> events);

	public void deleteEventOnCalendar(SapEventModel event);

	public List<Date> getMonthlyEventByInumber(String iNumber);

	public List<SapEventModel> getSapEventByInumberAndDate(final String iNumber, final Date date) throws ParseException;

	public List<SapEventModel> getMonthlyScheduleFromDateToDate(Date from, Date to);

	public List<SapEventModel> getMonthlyScheduleOnCallAndQM(Date from, Date to);

	public SapEventModel getTypeEventFromDate(Date date, EventType eventType) throws ParseException;

	public List<SapEventModel> getReport(final Date date, final EventType event, final String PK) throws ParseException;

	public void deleteEventsInTheDay(Date date, String PK) throws ParseException;
}
