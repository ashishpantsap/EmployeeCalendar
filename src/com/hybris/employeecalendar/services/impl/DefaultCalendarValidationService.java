/**
 *
 */
package com.hybris.employeecalendar.services.impl;

import de.hybris.platform.validation.services.impl.DefaultValidationService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.employeecalendar.dao.CalendarEventDao;
import com.hybris.employeecalendar.data.MessageDto;
import com.hybris.employeecalendar.data.enums.Alerts;
import com.hybris.employeecalendar.enums.EventType;
import com.hybris.employeecalendar.enums.OOOType;
import com.hybris.employeecalendar.model.SapEmployeeModel;
import com.hybris.employeecalendar.model.SapEventModel;
import com.hybris.employeecalendar.services.CalendarValidationService;
import com.hybris.employeecalendar.services.SAPEmployeeService;
import com.hybris.employeecalendar.util.HelperUtil;



/**
 * @author I310388
 *
 */
public class DefaultCalendarValidationService implements CalendarValidationService
{
	private CalendarEventDao sapEventDao;
	private SAPEmployeeService sapEmployeeService;
	private static final Logger LOG = Logger.getLogger(DefaultValidationService.class.getName());

	@Override
	public MessageDto validateInputData(final String pk, final List<Date> validDates, final String typeevent)
	{
		MessageDto msave = null;
		if (validDates == null)
		{
			return HelperUtil.createMessage("Choose dates for submitting an event", Alerts.DANGER);
		}
		final SapEmployeeModel employee = sapEmployeeService.getSapEmployeeByPK(pk);
		if (employee == null)
		{
			return HelperUtil.createMessage("No Employee found", Alerts.DANGER);
		}
		msave = canCreateEvents(employee.getInumber(), validDates, typeevent);
		return msave;
	}

	@Override
	public MessageDto canCreateEvents(final String iNumber, final List<Date> dates, final String typeOfEvent)
	{
		List<SapEventModel> sapEvents = null;
		SapEventModel QM = null;
		final StringBuilder message = new StringBuilder();
		MessageDto msave = null;
		final DateFormat simpleDateFormat = new SimpleDateFormat("MM-dd", Locale.ENGLISH);
		for (final Date date : dates)
		{
			try
			{
				sapEvents = sapEventDao.getSapEventByInumberAndDate(iNumber, date);
				QM = sapEventDao.getTypeEventFromDate(date, EventType.QUEUE_MANAGER);
				for (final SapEventModel eventOnDb : sapEvents)
				{
					final String validate = checkConsistency(QM, eventOnDb, typeOfEvent);
					if (validate != null)
					{
						message.append(simpleDateFormat.format(date) + ": " + validate + ",");
					}
				}
			}
			catch (final Exception e)
			{
				message.append(simpleDateFormat.format(date) + ": " + "Error validating the input for this date" + ",");
				LOG.error(e.getMessage() + "Error during validation for " + iNumber + " to " + typeOfEvent + " and for date " + date);
			}
		}
		if (!message.toString().isEmpty())
		{
			msave = HelperUtil.createMessage(message.toString(), Alerts.DANGER);
		}
		return msave;
	}

	private String checkConsistency(final SapEventModel QM, final SapEventModel eventOnDb, final String typeOfEvent)
	{
		String msave = null;
		if (QM != null && QM.equals(EventType.QUEUE_MANAGER))
		{
			msave = "There is already QM registered for this date";
		}
		else if (eventOnDb.getType().equals(EventType.ON_CALL))
		{
			msave = "No events can be added in this day. ON_CALL event already saved in the calendar";
		}
		else if (eventOnDb.getType().equals(EventType.AFTERNOON_SHIFT))
		{
			if (typeOfEvent.equals(EventType.ON_CALL.getCode()) || typeOfEvent.equals(EventType.OUT_OF_THE_OFFICE.getCode())
					|| typeOfEvent.equals(EventType.AFTERNOON_SHIFT.getCode()))
			{
				msave = "No events can be added in this day. AFTERNOON_SHIFT event already saved it is not possible to add MORNING_SHIFT |  OUT_OF_THE_OFFICE | ON_CALL | SICK_LEAVE | AFTERNOON_SHIFT";
			}
		}
		else if (eventOnDb.getType().equals(EventType.OUT_OF_THE_OFFICE) && eventOnDb.getOooType().equals(OOOType.FULL_DAY))
		{
			msave = "No events can be added in this day. OUT_OF_THE_OFFICE event already saved for this date";
		}
		else if (eventOnDb.getType().equals(EventType.OUT_OF_THE_OFFICE) && eventOnDb.getOooType().equals(OOOType.HALF_DAY))
		{
			if (typeOfEvent.equals(EventType.ON_CALL.getCode()))
			{
				msave = "No events ON_CALL can be added in this day. OUT_OF_THE_OFFICE half_day already saved on DB";
			}
		}
		else if (eventOnDb.getType().equals(EventType.QUEUE_MANAGER))
		{
			if (typeOfEvent.equals(EventType.ON_CALL.getCode()) || typeOfEvent.equals(EventType.OUT_OF_THE_OFFICE.getCode())
					|| typeOfEvent.equals(EventType.QUEUE_MANAGER.getCode()))
			{
				msave = "No events can be added in this day. QUEUE_MANAGER event already saved it is not possible to add ON_CALL |  OUT_OF_THE_OFFICE | SICK_LEAVE | QUEUE_MANAGER";
			}
		}
		else if (eventOnDb.getType().equals(EventType.OTHERS))
		{
			if (typeOfEvent.equals(EventType.ON_CALL.getCode()) || typeOfEvent.equals(EventType.OUT_OF_THE_OFFICE.getCode())
					|| typeOfEvent.equals(EventType.QUEUE_MANAGER.getCode()) || typeOfEvent.equals(EventType.OTHERS.getCode()))
			{
				msave = "No events can be added in this day. TRAINING event already saved it is not possible to add ON_CALL |  OUT_OF_THE_OFFICE | QUEUE_MANAGER | WORK_BANK_HOLIDAY | SICK_LEAVE | TRAINING";
			}
		}
		return msave;
	}

	public void setSapEventDao(final CalendarEventDao sapEventDao)
	{
		this.sapEventDao = sapEventDao;
	}

	@Autowired
	public void setSapEmployeeService(final SAPEmployeeService sapEmployeeService)
	{
		this.sapEmployeeService = sapEmployeeService;
	}
}
