/**
 *
 */
package com.hybris.employeecalendar.services.impl;

import de.hybris.platform.util.Config;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.employeecalendar.dao.CalendarEventDao;
import com.hybris.employeecalendar.enums.EventType;
import com.hybris.employeecalendar.model.SapEmployeeModel;
import com.hybris.employeecalendar.model.SapEventModel;
import com.hybris.employeecalendar.services.MailService;


/**
 * @author I317496
 *
 */

public class DefaultMailService implements MailService, InitializingBean
{
	private String fromAddress;
	private String smtpPort;
	private String smtpHost;
	private String managersEmail;

	private CalendarEventDao sapEventDao;

	@Override
	public void afterPropertiesSet()
	{
		fromAddress = Config.getParameter("mail.from");
		smtpHost = Config.getParameter("mail.smtp.server");
		managersEmail = Config.getParameter("mail.manager");
		smtpPort = Config.getParameter("mail.smtp.port");
	}

	@Override
	public void sendReminder()
	{
		try
		{
			final SapEventModel sapQMEvent = sapEventDao.getTypeEventFromDate(new Date(), EventType.QUEUE_MANAGER);
			final List<SapEventModel> sapOOEvent = sapEventDao.getTypeEventsFromDate(new Date(), EventType.OUT_OF_THE_OFFICE);
			final List<SapEventModel> sapTrainingEvent = sapEventDao.getTypeEventsFromDate(new Date(), EventType.OTHERS);
			final Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			final boolean monday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
			final boolean friday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
			String qmorOncall = "QM";
			SapEventModel previousEventForPhone = null;
			SapEventModel nextDayQM = null;
			if (monday)
			{
				cal.add(Calendar.DATE, +1);
				nextDayQM = sapEventDao.getTypeEventFromDate(cal.getTime(), EventType.QUEUE_MANAGER);
				cal.add(Calendar.DATE, -2);
				previousEventForPhone = sapEventDao.getTypeEventFromDate(cal.getTime(), EventType.ON_CALL);
				if (previousEventForPhone == null)
				{
					cal.add(Calendar.DATE, -1);
					previousEventForPhone = sapEventDao.getTypeEventFromDate(cal.getTime(), EventType.ON_CALL);
				}
				if (previousEventForPhone == null)
				{
					cal.add(Calendar.DATE, -1);
					previousEventForPhone = sapEventDao.getTypeEventFromDate(cal.getTime(), EventType.QUEUE_MANAGER);
				}

			}
			else if (friday)
			{
				cal.add(Calendar.DATE, -1);
				previousEventForPhone = sapEventDao.getTypeEventFromDate(cal.getTime(), EventType.QUEUE_MANAGER);
				cal.add(Calendar.DATE, +2);
				nextDayQM = sapEventDao.getTypeEventFromDate(cal.getTime(), EventType.ON_CALL);
				if (nextDayQM == null)
				{
					cal.add(Calendar.DATE, +1);
					nextDayQM = sapEventDao.getTypeEventFromDate(cal.getTime(), EventType.ON_CALL);
				}
				if (nextDayQM != null)
				{
					qmorOncall = "ONCALL person";
				}
				if (nextDayQM == null)
				{
					cal.add(Calendar.DATE, +1);
					nextDayQM = sapEventDao.getTypeEventFromDate(cal.getTime(), EventType.QUEUE_MANAGER);
				}

			}
			else
			{
				cal.add(Calendar.DATE, -1);
				previousEventForPhone = sapEventDao.getTypeEventFromDate(cal.getTime(), EventType.QUEUE_MANAGER);
				cal.add(Calendar.DATE, +2);
				nextDayQM = sapEventDao.getTypeEventFromDate(cal.getTime(), EventType.QUEUE_MANAGER);
			}
			if (sapQMEvent != null)
			{
				final SapEmployeeModel qm = sapQMEvent.getEmployee();
				final StringBuilder emponOO = new StringBuilder();
				final StringBuilder emponTraining = new StringBuilder();
				final String toAddress = qm.getEmail();
				final String toAddressName = qm.getName() + " " + qm.getSurname();
				if (sapOOEvent != null && !sapOOEvent.isEmpty())
				{
					for (final SapEventModel eventOO : sapOOEvent)
					{
						if (sapOOEvent.size() > 1)
						{
							emponOO.append(" " + eventOO.getEmployee().getName() + " " + eventOO.getEmployee().getSurname() + ",");
						}
						else
						{
							emponOO.append(" " + eventOO.getEmployee().getName() + " " + eventOO.getEmployee().getSurname());
						}
					}
				}
				if (sapTrainingEvent != null && !sapTrainingEvent.isEmpty())
				{
					for (final SapEventModel eventTraining : sapTrainingEvent)
					{
						if (sapTrainingEvent.size() > 1)
						{
							emponTraining.append(
									" " + eventTraining.getEmployee().getName() + " " + eventTraining.getEmployee().getSurname() + ",");
						}
						else
						{
							emponTraining
									.append(" " + eventTraining.getEmployee().getName() + " " + eventTraining.getEmployee().getSurname());
						}
					}
				}
				sendEmail(toAddressName, toAddress, emponOO, emponTraining, previousEventForPhone, nextDayQM, qmorOncall);
			}
			else
			{
				sendEmail(null, null, null, null, null, null, null);
			}
		}
		catch (final Exception e1)
		{
			e1.printStackTrace();
		}
	}

	private void sendEmail(final String toAddressName, final String toAddress, final StringBuilder emponOO,
			final StringBuilder emponTraining, final SapEventModel previousEventForPhone, final SapEventModel nextDayQM,
			final String qmorOncall)
	{
		final Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);
		String empwithPhone = null;
		String empOnActivities = null;
		String nextDayHandOver = null;

		final Session session = Session.getInstance(props);
		try
		{
			final Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			if (toAddressName != null)
			{
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(managersEmail));
				message.setSubject("Reminder for QM");
				if (previousEventForPhone != null)
				{
					empwithPhone = " </p><p>Request you to get the phone from " + " " + "<b>"
							+ previousEventForPhone.getEmployee().getName() + " " + previousEventForPhone.getEmployee().getSurname()
							+ "</b>";
				}
				else
				{
					empwithPhone = " </p><p>There is no data available regarding the previous day QM. Please check with your colleagues to get the phone. ";
				}
				if (nextDayQM != null)
				{
					nextDayHandOver = " </p><p> The next day " + qmorOncall + " is " + " " + "<b>" + nextDayQM.getEmployee().getName()
							+ " " + nextDayQM.getEmployee().getSurname() + "</b>";
				}
				else
				{
					nextDayHandOver = " </p><p>There is no data available regarding the next day QM/ONCALL. ";
				}
				if (!emponTraining.toString().equals("") || !emponOO.toString().equals(""))
				{
					empOnActivities = "Below are the list of Out of Office and Employees on Other activities for whom you may help to look after their tickets in case of any urgent action is required.<br><br><p>"
							+ "<b> Out of Office</b>:" + emponOO.toString() + "<br>" + "<b>Others  </b>   :" + emponTraining.toString();
				}
				else
				{
					empOnActivities = "There are no colleagues on Out of Office or on Other activities.<p>";
				}

				message.setContent(
						"<html><body><h4>Dear" + " " + toAddressName
								+ ",</h4><p>This to remind you that you are the Queue Manager for today. Please do keep on eye on P1 tickets.<br><br>"
								+ empOnActivities + empwithPhone + nextDayHandOver
								+ "<br><br>Please be advised that the above details are sent considering the employee calendar is up to date.<br><br>Have a good day.<br><br>Thanks & Regards,<br>Dublin Product Support Ireland<br></p></body></html>",
						"text/html; charset=utf-8");
			}
			else
			{
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(managersEmail));
				message.setSubject("Reminder to Update QM");
				message.setContent(
						"<html><body><h4>Dear Nitin Garg"
								+ ",</h4><p>This to remind you that Queue Manager for today has not been updated. Please do update and let the person knows the same since the email reminder service is already been processed for today<br><br>"
								+ "Have a good day.<br><br>Thanks & Regards,<br>Dublin Product Support Ireland<br></p></body></html>",
						"text/html; charset=utf-8");
			}
			Transport.send(message);
		}
		catch (final MessagingException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Autowired
	public void setSapEventDao(final CalendarEventDao sapEventDao)
	{
		this.sapEventDao = sapEventDao;
	}
}
