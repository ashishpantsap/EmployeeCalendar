/**
 *
 */
package com.hybris.employeecalendar.services.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.employeecalendar.dao.CalendarEventDao;
import com.hybris.employeecalendar.data.EventDto;
import com.hybris.employeecalendar.data.SAPEmployeeDto;
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
		sapEvent.setFromDate(event.getFromDate());
		sapEvent.setToDate(event.getToDate());
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


	@Override
	public List<EventDto> getMonthlySchedule(final Date today)
	{

		return null;
	}


	@Override
	public void saveEventOnCalendar(final EventDto event, final SapEmployeeModel employee)
	{
		if (event == null || employee == null)
		{
			return;
		}

		final SapEventModel sapEvent = new SapEventModel();
		sapEvent.setEmployee(employee);
		sapEvent.setFromDate(event.getFromDate());
		sapEvent.setToDate(event.getToDate());
		sapEvent.setDescription(event.getDescription());
		sapEvent.setType(event.getType() != null ? EventType.valueOf(event.getType()) : EventType.MORNING_SHIFT);

		calendarEventDao.saveEventOnCalendar(sapEvent);
	}



	@Override
	public List<EventDto> getMonthlyScheduleFromDateToDate(final Date from, final Date to)
	{
		final List<SapEventModel> eventsModel = calendarEventDao.getMonthlyScheduleFromDateToDate(from, to);

		return populateDtos(eventsModel);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.employeecalendar.services.CalendarEventService#getReport(java.util.Date,
	 * com.hybris.employeecalendar.enums.EventType, java.lang.String)
	 */
	@Override
	public List<EventDto> getReport(final Date date, final EventType event, final String PK) throws ParseException
	{
		final List<SapEventModel> eventsModel = calendarEventDao.getReport(date, event, PK);

		return populateDtos(eventsModel);
	}


	private List<EventDto> populateDtos(final List<SapEventModel> eventsModel)
	{
		if (eventsModel == null || eventsModel.size() == 0)
		{
			return Collections.EMPTY_LIST;
		}

		final ArrayList<EventDto> events = new ArrayList<EventDto>();
		for (final SapEventModel model : eventsModel)
		{
			final EventDto eventDto = new EventDto();
			eventDto.setFromDate(model.getFromDate());
			eventDto.setToDate(model.getToDate());
			eventDto.setDescription(model.getDescription());
			eventDto.setType(model.getType().getCode());

			final SapEmployeeModel employee = model.getEmployee();
			final SAPEmployeeDto employeeDto = new SAPEmployeeDto();
			employeeDto.setInumber(employee.getInumber());
			employeeDto.setName(employee.getName());
			employeeDto.setSurname(employee.getSurname());

			// employeeDto should set the PK as id dto shuould be modified
			eventDto.setEmployee(employeeDto);

			events.add(eventDto);
		}
		return events;
	}


}
