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
import com.hybris.employeecalendar.data.MessageDto;
import com.hybris.employeecalendar.data.enums.Alerts;


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
}
