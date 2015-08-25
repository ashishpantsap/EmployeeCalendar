/**
 *
 */
package com.hybris.employeecalendar.services;

import java.util.Date;
import java.util.List;

import com.hybris.employeecalendar.data.EventDto;
import com.hybris.employeecalendar.model.SapEmployeeModel;




/**
 * @author I310388
 *
 */
public interface CalendarEventService
{

	public void saveEventOnCalendar(EventDto event);

	public void saveEventOnCalendar(final EventDto event, SapEmployeeModel employee);

	public void saveEventsOnCalendar(List<EventDto> events);

	public void deleteEventOnCalendar(EventDto event);

	public List<Date> getMonthlyEventByInumber(String iNumber);

	public List<EventDto> getMonthlySchedule(String month);

}
