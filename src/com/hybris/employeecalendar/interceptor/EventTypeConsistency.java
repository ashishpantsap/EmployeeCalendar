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
import com.hybris.employeecalendar.enums.OOOType;
import com.hybris.employeecalendar.model.SapEventModel;


/**
 * @author I310388
 *
 */
public class EventTypeConsistency implements ValidateInterceptor<SapEventModel>
{
	private static final Logger LOG = Logger.getLogger(EventTypeConsistency.class.getName());

	private CalendarEventDao sapEventDao;


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


	private void checkConsistency(final SapEventModel eventOnDb, final SapEventModel model) throws InterceptorException
	{
		if (eventOnDb.getType().equals(EventType.ON_CALL))
		{
			throw new InterceptorException("No events can be added in this day. ON_CALL event already saved in the calendar");
		}
		else if (eventOnDb.getType().equals(EventType.AFTERNOON_SHIFT))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.OUT_OF_THE_OFFICE)
					|| model.getType().equals(EventType.AFTERNOON_SHIFT))
			{
				throw new InterceptorException(
						"No events can be added in this day. AFTERNOON_SHIFT event already saved it is not possible to add MORNING_SHIFT |  OUT_OF_THE_OFFICE | ON_CALL | SICK_LEAVE | AFTERNOON_SHIFT");
			}
		}
		else if (eventOnDb.getType().equals(EventType.OUT_OF_THE_OFFICE) && eventOnDb.getOooType().equals(OOOType.FULL_DAY))
		{
			throw new InterceptorException(
					"No events can be added in this day. OUT_OF_THE_OFFICE event already saved for this date");
		}
		else if (eventOnDb.getType().equals(EventType.OUT_OF_THE_OFFICE) && eventOnDb.getOooType().equals(OOOType.HALF_DAY))
		{
			if (model.getType().equals(EventType.ON_CALL))
			{
				throw new InterceptorException(
						"No events ON_CALL can be added in this day. OUT_OF_THE_OFFICE half_day already saved on DB");
			}
		}
		else if (eventOnDb.getType().equals(EventType.QUEUE_MANAGER))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.OUT_OF_THE_OFFICE)
					|| model.getType().equals(EventType.QUEUE_MANAGER))
			{
				throw new InterceptorException(
						"No events can be added in this day. QUEUE_MANAGER event already saved it is not possible to add ON_CALL |  OUT_OF_THE_OFFICE | SICK_LEAVE | QUEUE_MANAGER");
			}
		}
		else if (eventOnDb.getType().equals(EventType.OTHERS))
		{
			if (model.getType().equals(EventType.ON_CALL) || model.getType().equals(EventType.OUT_OF_THE_OFFICE)
					|| model.getType().equals(EventType.QUEUE_MANAGER) || model.getType().equals(EventType.OTHERS))
			{
				throw new InterceptorException(
						"No events can be added in this day. TRAINING event already saved it is not possible to add ON_CALL |  OUT_OF_THE_OFFICE | QUEUE_MANAGER | WORK_BANK_HOLIDAY | SICK_LEAVE | TRAINING");
			}
		}
	}
}
