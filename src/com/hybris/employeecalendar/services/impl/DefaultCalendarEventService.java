/**
 *
 */
package com.hybris.employeecalendar.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.employeecalendar.dao.CalendarEventDao;
import com.hybris.employeecalendar.data.EventDto;
import com.hybris.employeecalendar.enums.EventType;
import com.hybris.employeecalendar.model.SapEmployeeModel;
import com.hybris.employeecalendar.model.SapEventModel;
import com.hybris.employeecalendar.services.CalendarEventService;


/**
 * @author I310388
 *
 */
public class DefaultCalendarEventService implements CalendarEventService
{


	private CalendarEventDao calendarEventDao;

	/**
	 * @param calendarEventDao
	 *           the calendarEventDao to set
	 */
	@Autowired
	public void setCalendarEventDao(final CalendarEventDao calendarEventDao)
	{
		this.calendarEventDao = calendarEventDao;
	}


	@Override
	public void saveEventOnCalendar(final EventDto event)
	{
		if (event == null)
		{
			return;
		}

		final SapEventModel sapEvent = new SapEventModel();
		final SapEmployeeModel employee = new SapEmployeeModel();
		employee.setInumber(event.getEmployee().getInumber());
		employee.setName(event.getEmployee().getName());
		employee.setSurname(event.getEmployee().getSurname());

		sapEvent.setEmployee(employee);
		sapEvent.setDate(event.getDate());

		calendarEventDao.saveEventOnCalendar(sapEvent);
	}


	@Override
	public List<Date> getMonthlyEventByInumber(final String iNumber)
	{
		// YTODO Auto-generated method stub
		return null;
	}


	@Override
	public void saveEventsOnCalendar(final List<EventDto> events)
	{
		// YTODO Auto-generated method stub

	}


	@Override
	public void deleteEventOnCalendar(final EventDto event)
	{
		// YTODO Auto-generated method stub

	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.employeecalendar.services.CalendarEventService#getMonthlySchedule(java.lang.String)
	 */
	@Override
	public List<EventDto> getMonthlySchedule(final String month)
	{

		return null;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.employeecalendar.services.CalendarEventService#saveEventOnCalendar(com.hybris.employeecalendar.data
	 * .EventDto, com.hybris.employeecalendar.model.SapEmployeeModel)
	 */
	@Override
	public void saveEventOnCalendar(final EventDto event, final SapEmployeeModel employee)
	{
		if (event == null || employee == null)
		{
			return;
		}

		final SapEventModel sapEvent = new SapEventModel();
		sapEvent.setEmployee(employee);
		sapEvent.setDate(event.getDate());
		sapEvent.setDescription(event.getDescription());
		sapEvent.setType(event.getType() != null ? EventType.valueOf(event.getType()) : EventType.MORNING_SHIFT);

		calendarEventDao.saveEventOnCalendar(sapEvent);
	}


}
