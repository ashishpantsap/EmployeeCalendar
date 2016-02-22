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
import com.hybris.employeecalendar.enums.OOOType;
import com.hybris.employeecalendar.model.SapEmployeeModel;
import com.hybris.employeecalendar.model.SapEventModel;
import com.hybris.employeecalendar.services.CalendarEventService;
import com.hybris.employeecalendar.services.SAPEmployeeService;
import com.hybris.employeecalendar.util.HelperUtil;


/**
 * @author I310388
 *
 */
public class DefaultCalendarEventService implements CalendarEventService
{


	private CalendarEventDao calendarEventDao;
	private SAPEmployeeService sapEmployeeService;



	/**
	 * @param calendarEventDao
	 *           the calendarEventDao to set
	 */
	@Autowired
	public void setCalendarEventDao(final CalendarEventDao calendarEventDao)
	{
		this.calendarEventDao = calendarEventDao;
	}

	@Autowired
	public void setSAPEmployeeService(final SAPEmployeeService sapEmployeeService)
	{
		this.sapEmployeeService = sapEmployeeService;
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
	public void saveEventsOnCalendar(final List<EventDto> events, final SapEmployeeModel employee) throws ParseException
	{
		if (events == null || events.isEmpty() || employee == null)
		{
			return;
		}

		EventDto event = null;

		final List<SapEventModel> eventsModel = new ArrayList<>();
		for (final EventDto eventDto : events)
		{

			final SapEventModel eventM = new SapEventModel();
			final List<SapEventModel> list = calendarEventDao.getEventFromEmployeeAndDate(eventDto.getFromDate(),
					employee.getPk().toString());
			if (list != null && !list.isEmpty())
			{
				eventsModel.addAll(list);
			}
			//fixing date with time
			event = HelperUtil.getDateRangeFromEventType(eventDto);

			eventM.setDescription(event.getDescription());
			eventM.setEmployee(employee);
			eventM.setFromDate(event.getFromDate());
			eventM.setToDate(event.getToDate());
			eventM.setType(EventType.valueOf(event.getType()));
			eventM.setOooType(OOOType.valueOf(event.getOooType() != null ? event.getOooType() : OOOType.FULL_DAY.getCode()));
			eventsModel.add(eventM);
		}
		calendarEventDao.saveEventsOnCalendar(eventsModel);
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
		sapEvent.setType(event.getType() != null ? EventType.valueOf(event.getType()) : EventType.OUT_OF_THE_OFFICE);

		calendarEventDao.saveEventOnCalendar(sapEvent);
	}



	@Override
	public List<EventDto> getMonthlyScheduleFromDateToDate(final Date from, final Date to)
	{
		final List<SapEventModel> eventsModel = calendarEventDao.getMonthlyScheduleFromDateToDate(from, to);

		return populateDtos(eventsModel);
	}


	@Override
	public List<EventDto> getReport(final Date date, final EventType event, final String PK) throws ParseException
	{
		final List<SapEventModel> eventsModel = calendarEventDao.getReport(date, event, PK);

		return populateDtos(eventsModel);
	}

	//TODO Change Generic Exception
	@Override
	public void deleteEventOnCalendar(final EventDto event) throws Exception
	{
		//
	}

	//TODO Change Generic Exception
	private SapEventModel convertEventDtoToModel(final EventDto eventDto) throws Exception
	{
		if (eventDto == null)
		{
			throw new Exception("Could not convert Dto to Model: No event present.");
		}
		final SapEventModel sapEventModel = new SapEventModel();
		final SAPEmployeeDto sapEmployeeDto = sapEmployeeService.getSapEmployeeByInumber(eventDto.getEmployee().getInumber());
		sapEventModel.setEmployee(convertEmployeeDtoToModel(sapEmployeeDto));
		sapEventModel.setFromDate(eventDto.getFromDate());
		sapEventModel.setType(EventType.valueOf(eventDto.getType()));
		return sapEventModel;

		//		final SapEmployeeModel sapEmployeeModel = convertEmployeeDtoToModel(sapEmployeeDto);
		//		final Collection<SapEventModel> events = sapEmployeeModel.getEvents();
		//		final Iterator i = events.iterator();
		//		while (i.hasNext())
		//		{
		//			final SapEventModel sapEventModel = (SapEventModel) i;
		//			if (sapEventModel.getFromDate().equals(eventDto.getFromDate()) && sapEventModel.getType().equals(eventDto.getType()))
		//			{
		//				return sapEventModel;
		//			}
		//		}
		//		return null;



		//		model.setEmployee(value);
		//			model.setEmployee(eventDto.getEmployee());
		//			final EventDto eventDto = new EventDto();
		//			eventDto.setFromDate(model.getFromDate());
		//			eventDto.setToDate(model.getToDate());
		//			eventDto.setDescription(model.getDescription());
		//			eventDto.setType(model.getType().getCode());
		//
		//			final SapEmployeeModel employee = model.getEmployee();
		//			final SAPEmployeeDto employeeDto = new SAPEmployeeDto();
		//			employeeDto.setInumber(employee.getInumber());
		//			employeeDto.setName(employee.getName());
		//			employeeDto.setSurname(employee.getSurname());
		//
		//			// employeeDto should set the PK as id dto shuould be modified
		//			eventDto.setEmployee(employeeDto);
		//
		//			events.add(eventDto);
	}

	private SapEmployeeModel convertEmployeeDtoToModel(final SAPEmployeeDto sapEmployeeDto) throws Exception
	{
		final SapEmployeeModel sapEmployeeModel = new SapEmployeeModel();
		sapEmployeeModel.setInumber(sapEmployeeDto.getInumber());
		return sapEmployeeModel;
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
			eventDto.setPK(model.getPk().getLongValueAsString());
			eventDto.setFromDate(model.getFromDate());
			eventDto.setToDate(model.getToDate());
			eventDto.setDescription(model.getDescription());
			eventDto.setType(model.getType().getCode());
			eventDto.setOooType(model.getOooType().getCode());

			final SapEmployeeModel employee = model.getEmployee();
			final SAPEmployeeDto employeeDto = new SAPEmployeeDto();
			employeeDto.setInumber(employee.getInumber());
			employeeDto.setName(employee.getName());
			employeeDto.setSurname(employee.getSurname());
			employeeDto.setEmail(employee.getEmail());

			// employeeDto should set the PK as id dto shuould be modified
			eventDto.setEmployee(employeeDto);

			events.add(eventDto);
		}
		return events;
	}


	@Override
	public void deleteEventFromPk(final String pk)
	{
		calendarEventDao.deleteEventFromPk(pk);
	}


	@Override
	public List<EventDto> getMonthlyScheduleOnCallAndQM(final Date from, final Date to)
	{
		final List<SapEventModel> monthlyScheduleOnCallAndQM = calendarEventDao.getMonthlyScheduleOnCallAndQM(from, to);
		return populateDtos(monthlyScheduleOnCallAndQM);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.employeecalendar.services.CalendarEventService#getAllEventsInTheDay(java.util.Date)
	 */
	@Override
	public List<SapEventModel> getAllEventsInTheDay(final Date date, final String name, final String eventType)
			throws ParseException
	{
		return calendarEventDao.getAllEventsInTheDay(date, name, eventType);


	}

	@Override
	public List<EventDto> getAllEventsForDay(final Date date) throws ParseException
	{
		final List<SapEventModel> daysEvents = calendarEventDao.getAllEventsInTheDay(date, null, null);
		return populateDtos(daysEvents);

	}


	@Override
	public void deleteEventsInTheDay(final Date date, final String PK) throws ParseException
	{
		calendarEventDao.deleteEventsInTheDay(date, PK);
	}
	@Override
	public List<EventDto> getSapEventByInumberAndDate(final String iNumber, final String fromDate) throws ParseException
	{
		final List<SapEventModel> daysEvents = calendarEventDao.getSapEventByInumberAndDate(iNumber, fromDate);
		return populateDtos(daysEvents);
	}
}
