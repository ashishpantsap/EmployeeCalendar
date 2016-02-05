/**
 *
 */
package com.hybris.employeecalendar.util;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.hybris.employeecalendar.data.DateRangeDto;
import com.hybris.employeecalendar.data.EventDto;
import com.hybris.employeecalendar.data.MessageDto;
import com.hybris.employeecalendar.data.enums.Alerts;
import com.hybris.employeecalendar.enums.EventType;


/**
 * @author I310388
 *
 */
public class HelperUtil
{
	public static MessageDto createMessage(final String description, final Alerts alert)
	{
		final MessageDto messageDto = new MessageDto();
		messageDto.setAlert(alert);
		messageDto.setDescription(description);
		return messageDto;
	}

	public static Date convertLongMillisecondsToDate(final long milliseconds) throws ParseException
	{
		final Calendar calendar = Calendar.getInstance();
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH", Locale.ENGLISH);
		calendar.setTimeInMillis(milliseconds);
		final String dateInFormat = format.format(calendar.getTime());
		return format.parse(dateInFormat);
	}

	public static DateRangeDto getDateRangeOfTheDay(final Date date) throws ParseException
	{
		if (date == null)
		{
			return null;
		}
		final DateRangeDto dateRangeDto = new DateRangeDto();

		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		final String dateToString = format.format(date);
		final String from = dateToString + " 00:00:00";
		final String to = dateToString + " 23:59:59";

		final DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		dateRangeDto.setFromDate(format2.parse(from));
		dateRangeDto.setToDate(format2.parse(to));

		return dateRangeDto;
	}

	public static DateRangeDto getDateFirstToLastofTheMonth(final Date date) throws ParseException
	{
		if (date == null)
		{
			return null;
		}
		final DateRangeDto dateRangeDto = new DateRangeDto();

		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		final DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		final String dateToString = format.format(date);
		final String from = dateToString + " 00:00:00";
		final String to = dateToString + " 23:59:59";
		//get the last day of the month
		final Calendar cal = Calendar.getInstance();
		cal.setTime(format2.parse(to));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		dateRangeDto.setFromDate(format2.parse(from));
		dateRangeDto.setToDate(cal.getTime());

		return dateRangeDto;
	}

	public static EventDto getDateRangeFromEventType(final EventDto event) throws ParseException
	{

		final Date date = event.getFromDate();
		if (date == null)
		{
			return null;
		}
		//final DateRangeDto dateRangeDto = new DateRangeDto();
		String from = "";
		String to = "";
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		final String dateToString = format.format(date);

		if (EventType.AFTERNOON_SHIFT.getCode().equalsIgnoreCase(event.getType()))
		{
			from = dateToString + " 12:00:00";
			to = dateToString + " 20:00:00";
		}
		else if (EventType.ON_CALL.getCode().equalsIgnoreCase(event.getType()))
		{
			from = dateToString + " 08:00:00";
			to = dateToString + " 16:00:00";
		}
		else if (EventType.QUEUE_MANAGER.getCode().equalsIgnoreCase(event.getType()))
		{
			from = dateToString + " 10:00:00";
			to = dateToString + " 18:00:00";
		}
		else if (EventType.OUT_OF_THE_OFFICE.getCode().equalsIgnoreCase(event.getType()))
		{
			from = dateToString + " 10:00:00";
			to = dateToString + " 18:00:00";
		} //TRAINING TO BE DECIDED WITH NEW VALUE ENUMTYPE
		else if (EventType.TRAINING.getCode().equalsIgnoreCase(event.getType()))
		{
			if (event.getTrainingTime() != null)
			{
				if ("MORNING".equals(event.getTrainingTime()))
				{
					from = dateToString + " 09:00:00";
					to = dateToString + " 12:00:00";
				}
				else if (("AFTERNOON".equals(event.getTrainingTime())))
				{
					from = dateToString + " 14:00:00";
					to = dateToString + " 18:00:00";
				}
				else if (("ALL_DAY".equals(event.getTrainingTime())))
				{
					from = dateToString + " 09:00:00";
					to = dateToString + " 18:00:00";
				}
			}
			else
			{
				from = dateToString + " 09:00:00";
				to = dateToString + " 18:00:00";
			}
		}
		final DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		event.setFromDate(format2.parse(from));
		event.setToDate(format2.parse(to));

		return event;
	}
}
