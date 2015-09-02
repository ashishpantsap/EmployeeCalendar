/**
 *
 */
package com.hybris.employeecalendar.dao;

import java.util.Date;
import java.util.List;

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

	public List<SapEventModel> getSapEventByInumberAndDate(final String iNumber, final Date date);
}
