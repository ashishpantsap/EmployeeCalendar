/**
 *
 */
package com.hybris.employeecalendar.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import com.hybris.employeecalendar.dao.CalendarEventDao;
import com.hybris.employeecalendar.enums.EventType;
import com.hybris.employeecalendar.model.SapEventModel;


/**
 * @author I310388
 *
 */
public class EventTypeConsistency implements ValidateInterceptor<SapEventModel>
{
	private static final Logger LOG = Logger.getLogger(EventTypeConsistency.class.getName());

	private CalendarEventDao sapEventDao;

	/**
	 * @param sapEventDao
	 *           the sapEventDao to set
	 */
	public void setSapEventDao(final CalendarEventDao sapEventDao)
	{
		this.sapEventDao = sapEventDao;
	}

	@Override
	public void onValidate(final SapEventModel model, final InterceptorContext ctx) throws InterceptorException
	{

		List<SapEventModel> sapEvents = null;
		SapEventModel QM = null;
		try
		{
			sapEvents = sapEventDao.getSapEventByInumberAndDate(model.getEmployee().getInumber(), model.getFromDate());
			QM = sapEventDao.getTypeEventFromDate(model.getFromDate(), EventType.QUEUE_MANAGER);
		}
		catch (final ParseException pe)
		{
			LOG.error("error parsing the date");
		}

		if (QM != null && model.getType().equals(EventType.QUEUE_MANAGER))
		{
			throw new InterceptorException("There is already a QUEUE_MANAGER for this date");
		}


		if (sapEvents == null || sapEvents.size() == 0)
		{
			return;
		}

		for (final SapEventModel eventOnDb : sapEvents)
		{
			checkConsistency(eventOnDb, model);
		}

	}


	//1 ON_CALL cannot have other events in the day
	//2 AFTERNOON_SHIFT cannot have ON_CALL,MORNING_SHIFT,OUT_OF_THE_OFFICE
	//3 MORNING_SHIFT cannot have ON_CALL,AFTERNOON_SHIFT,OUT_OF_THE_OFFICE
	//4 OUT_OF_THE_OFFICE cannot have other events in the day
	//5 WORKING_FROM_HOME cannot have ON_CALL,OUT_OF_THE_OFFICE
	//6 QUEUE_MANAGER cannot have ON_CALL,OUT_OF_THE_OFFICE
	//7 WORKING_BANK_HOLIDAY cannot have ON_CALL,OUT_OF_THE_OFFICE
	//8 TRAINING cannot have ON_CALL, WORKING_FROM_HOME, OUT_OF_THE_OFFICE, QUEUE_MANAGER, WORKING_BANK_HOLIDAY
	private void checkConsistency(final SapEventModel eventOnDb, final SapEventModel model) throws InterceptorException
	{
		if (eventOnDb.getType().equals(EventType.ON_CALL))
		{
			throw new InterceptorException("No events can be added in this day. ON_CALL event already saved in the calendar");
		}
		else if (eventOnDb.getType().equals(EventType.AFTERNOON_SHIFT))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.MORNING_SHIFT)
					|| model.getType().equals(EventType.OUT_OF_THE_OFFICE) || model.getType().equals(EventType.SICK_LEAVE)
					|| model.getType().equals(EventType.AFTERNOON_SHIFT))
			{
				throw new InterceptorException(
						"No events can be added in this day. AFTERNOON_SHIFT event already saved it is not possible to add MORNING_SHIFT |  OUT_OF_THE_OFFICE | ON_CALL | SICK_LEAVE | AFTERNOON_SHIFT");
			}
		}
		else if (eventOnDb.getType().equals(EventType.MORNING_SHIFT))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.AFTERNOON_SHIFT)
					|| model.getType().equals(EventType.OUT_OF_THE_OFFICE) || model.getType().equals(EventType.SICK_LEAVE)
					|| model.getType().equals(EventType.MORNING_SHIFT))
			{
				throw new InterceptorException(
						"No events can be added in this day. MORNING_SHIFT event already saved it is not possible to add AFTERNOON_SHIFT |  OUT_OF_THE_OFFICE | ON_CALL | SICK_LEAVE | MORNING_SHIFT");
			}
		}
		else if (eventOnDb.getType().equals(EventType.OUT_OF_THE_OFFICE))
		{
			throw new InterceptorException("No events can be added in this day. OUT_OF_THE_OFFICE event already saved for this date");
		}
		else if (eventOnDb.getType().equals(EventType.WORKING_FROM_HOME))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.OUT_OF_THE_OFFICE)
					|| model.getType().equals(EventType.SICK_LEAVE) || model.getType().equals(EventType.WORKING_FROM_HOME))
			{
				throw new InterceptorException(
						"No events can be added in this day. WORKING_FROM_HOME event already saved it is not possible to add ON_CALL |  OUT_OF_THE_OFFICE | SICK_LEAVE | WORKING_FROM_HOME");
			}
		}
		else if (eventOnDb.getType().equals(EventType.QUEUE_MANAGER))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.OUT_OF_THE_OFFICE)
					|| model.getType().equals(EventType.SICK_LEAVE) || model.getType().equals(EventType.QUEUE_MANAGER))
			{
				throw new InterceptorException(
						"No events can be added in this day. QUEUE_MANAGER event already saved it is not possible to add ON_CALL |  OUT_OF_THE_OFFICE | SICK_LEAVE | QUEUE_MANAGER");
			}
		}
		else if (eventOnDb.getType().equals(EventType.WORKING_BANK_HOLIDAY))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.OUT_OF_THE_OFFICE)
					|| model.getType().equals(EventType.SICK_LEAVE) || model.getType().equals(EventType.WORKING_BANK_HOLIDAY))
			{
				throw new InterceptorException(
						"No events can be added in this day. WORKING_BANK_HOLIDAY event already saved it is not possible to add ON_CALL |  OUT_OF_THE_OFFICE  | SICK_LEAVE | WORKING_BANK_HOLIDAY");
			}
		}
		else if (eventOnDb.getType().equals(EventType.TRAINING))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.OUT_OF_THE_OFFICE)
					|| model.getType().equals(EventType.QUEUE_MANAGER) || model.getType().equals(EventType.WORKING_BANK_HOLIDAY)
					|| model.getType().equals(EventType.SICK_LEAVE) || model.getType().equals(EventType.TRAINING))
			{
				throw new InterceptorException(
						"No events can be added in this day. TRAINING event already saved it is not possible to add ON_CALL |  OUT_OF_THE_OFFICE | QUEUE_MANAGER | WORK_BANK_HOLIDAY | SICK_LEAVE | TRAINING");
			}
		}
		else if (eventOnDb.getType().equals(EventType.SICK_LEAVE))
		{
			throw new InterceptorException(
					"No events can be added in this day. SICK_LEAVE event already saved it is not possible to add other events");
		}


	}
}
