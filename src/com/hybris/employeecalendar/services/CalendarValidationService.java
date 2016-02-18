/**
 *
 */
package com.hybris.employeecalendar.services;

import java.util.Date;
import java.util.List;

import com.hybris.employeecalendar.data.MessageDto;



/**
 * @author I310388
 *
 */
public interface CalendarValidationService
{

	public MessageDto validateInputData(String pk, List<Date> validDates, String typeevent);

	public MessageDto canCreateEvents(String iNumber, List<Date> dates, String typeOfEvent);

}
