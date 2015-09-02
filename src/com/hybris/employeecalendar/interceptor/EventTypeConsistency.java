/**
 *
 */
package com.hybris.employeecalendar.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.List;

import com.hybris.employeecalendar.dao.CalendarEventDao;
import com.hybris.employeecalendar.enums.EventType;
import com.hybris.employeecalendar.model.SapEventModel;


/**
 * @author I310388
 *
 */
public class EventTypeConsistency implements ValidateInterceptor<SapEventModel>
{
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
		final List<SapEventModel> sapEvents = sapEventDao.getSapEventByInumberAndDate(model.getEmployee().getInumber(),
				model.getDate());

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
	private void checkConsistency(final SapEventModel eventOnDb, final SapEventModel model) throws InterceptorException
	{
		if (eventOnDb.getType().equals(EventType.ON_CALL))
		{
			throw new InterceptorException("No events can be added in this day. ON_CALL event already saved in the calendar");
		}
		else if (eventOnDb.getType().equals(EventType.AFTERNOON_SHIFT))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.MORNING_SHIFT)
					|| model.getType().equals(EventType.OUT_OF_THE_OFFICE))
			{
				throw new InterceptorException(
						"No events can be added in this day. AFTERNOON_SHIFT event already saved it is not possible to add MORNING_SHIFT |  OUT_OF_THE_OFFICE | ON_CALL");
			}
		}
		else if (eventOnDb.getType().equals(EventType.MORNING_SHIFT))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.AFTERNOON_SHIFT)
					|| model.getType().equals(EventType.OUT_OF_THE_OFFICE))
			{
				throw new InterceptorException(
						"No events can be added in this day. MORNING_SHIFT event already saved it is not possible to add AFTERNOON_SHIFT |  OUT_OF_THE_OFFICE | ON_CALL");
			}
		}
		else if (eventOnDb.getType().equals(EventType.OUT_OF_THE_OFFICE))
		{
			throw new InterceptorException("No events can be added in this day. OUT_OF_THE_OFFICE event already saved for this date");
		}
		else if (eventOnDb.getType().equals(EventType.WORKING_FROM_HOME))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.OUT_OF_THE_OFFICE))
			{
				throw new InterceptorException(
						"No events can be added in this day. WORKING_FROM_HOME event already saved it is not possible to add ON_CALL |  OUT_OF_THE_OFFICE ");
			}
		}
		else if (eventOnDb.getType().equals(EventType.QUEUE_MANAGER))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.OUT_OF_THE_OFFICE))
			{
				throw new InterceptorException(
						"No events can be added in this day. QUEUE_MANAGER event already saved it is not possible to add ON_CALL |  OUT_OF_THE_OFFICE ");
			}
		}
		else if (eventOnDb.getType().equals(EventType.WORKING_BANK_HOLIDAY))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.OUT_OF_THE_OFFICE))
			{
				throw new InterceptorException(
						"No events can be added in this day. QUEUE_MANAGER event already saved it is not possible to add ON_CALL |  OUT_OF_THE_OFFICE ");
			}
		}

	}
}
