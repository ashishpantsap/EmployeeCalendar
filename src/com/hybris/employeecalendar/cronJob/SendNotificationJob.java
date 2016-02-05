/**
 *
 */
package com.hybris.employeecalendar.cronJob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.employeecalendar.services.MailService;


public class SendNotificationJob extends AbstractJobPerformable<CronJobModel>
{


	private MailService mailService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{

		mailService.sendReminder();
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Autowired
	public void setMailService(final MailService trailMailService)
	{
		this.mailService = trailMailService;
	}
}
