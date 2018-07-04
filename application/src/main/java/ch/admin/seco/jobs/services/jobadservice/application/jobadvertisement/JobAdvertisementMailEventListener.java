package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.JobCenterService;
import ch.admin.seco.jobs.services.jobadservice.application.MailSenderData;
import ch.admin.seco.jobs.services.jobadservice.application.MailSenderService;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCancelledEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCreatedEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementRefinedEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementRejectedEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Component
public class JobAdvertisementMailEventListener {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	private static final String LANGUAGE_DE = new Locale("de").getLanguage();
	private static final String LANGUAGE_FR = new Locale("fr").getLanguage();
	private static final String LANGUAGE_IT = new Locale("it").getLanguage();
	private static final String LANGUAGE_EN = new Locale("en").getLanguage();

	private static Logger LOG = LoggerFactory.getLogger(JobAdvertisementMailEventListener.class);

	private static final String EMAIL_DELIMITER = "\\s*;\\s*";

	private static final String JOB_ADVERTISEMENT_CREATED_SUBJECT = "mail.jobAd.created.subject";
	private static final String JOB_ADVERTISEMENT_CREATED_TEMPLATE = "JobAdCreatedMail.html";
	private static final String JOB_ADVERTISEMENT_REFINED_SUBJECT = "mail.jobAd.refined.subject";
	private static final String JOB_ADVERTISEMENT_REFINED_TEMPLATE = "JobAdRefinedMail.html";
	private static final String JOB_ADVERTISEMENT_REFINED_MULTILINGUAL_SUBJECT = "mail.jobAd.refined.subject_multilingual";
	private static final String JOB_ADVERTISEMENT_REFINED_MULTILINGUAL_TEMPLATE = "JobAdRefinedMail_multilingual.html";
	private static final String JOB_ADVERTISEMENT_REJECTED_SUBJECT = "mail.jobAd.rejected.subject";
	private static final String JOB_ADVERTISEMENT_REJECTED_TEMPLATE = "JobAdRejectedMail.html";
	private static final String JOB_ADVERTISEMENT_CANCELLED_SUBJECT = "mail.jobAd.cancelled.subject";
	private static final String JOB_ADVERTISEMENT_CANCELLED_TEMPLATE = "JobAdCancelledMail.html";
	private static final String JOB_ADVERTISEMENT_CANCELLED_MULTILINGUAL_SUBJECT = "mail.jobAd.cancelled.subject_multilingual";
	private static final String JOB_ADVERTISEMENT_CANCELLED_MULTILINGUAL_TEMPLATE = "JobAdCancelledMail_multilingual.html";
	private static final String DEFAULT_LANGUAGE = "";

	private final JobAdvertisementRepository jobAdvertisementRepository;
	private final MailSenderService mailSenderService;
	private final MessageSource messageSource;
	private final JobCenterService jobCenterService;

	@Autowired
	public JobAdvertisementMailEventListener(JobAdvertisementRepository jobAdvertisementRepository, MailSenderService mailSenderService, MessageSource messageSource, JobCenterService jobCenterService) {
		this.jobAdvertisementRepository = jobAdvertisementRepository;
		this.mailSenderService = mailSenderService;
		this.messageSource = messageSource;
		this.jobCenterService = jobCenterService;
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	void onCreated(JobAdvertisementCreatedEvent event) {
		final JobAdvertisement jobAdvertisement = getJobAdvertisement(event.getAggregateId());
		if (jobAdvertisement.getSourceSystem().equals(SourceSystem.API)) {
			return;
		}
		if (hasNoContactEmail(jobAdvertisement.getContact())) {
			return;
		}
		LOG.debug("EVENT catched for mail: JOB_ADVERTISEMENT_CREATED for JobAdvertisementId: '{}'", event.getAggregateId().getValue());
		Locale contactLocale = new Locale(DEFAULT_LANGUAGE);
		if (jobAdvertisement.getContact() != null) {
			contactLocale = jobAdvertisement.getContact().getLanguage();
		}
		final JobCenter jobCenter = jobCenterService.findJobCenterByCode(jobAdvertisement.getJobCenterCode());
		final String stellennummer = extractStellennummer(jobAdvertisement);
		Map<String, Object> variables = new HashMap<>();
		variables.put("stellennummer", stellennummer);
		variables.put("reportingObligation", jobAdvertisement.isReportingObligation());
		variables.put("jobAdvertisementId", jobAdvertisement.getId().getValue());
		variables.put("accessToken", jobAdvertisement.getOwner().getAccessToken());
		variables.put("jobCenter", jobCenter);
		mailSenderService.send(
				new MailSenderData.Builder()
						.setTo(parseMultipleAddresses(jobAdvertisement.getContact().getEmail()))
						.setSubject(messageSource.getMessage(JOB_ADVERTISEMENT_CREATED_SUBJECT,
								new Object[] {jobAdvertisement.getJobContent().getJobDescriptions().get(0).getTitle(), stellennummer}, contactLocale))
						.setTemplateName(JOB_ADVERTISEMENT_CREATED_TEMPLATE)
						.setTemplateVariables(variables)
						.setLocale(contactLocale)
						.build()
		);
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	void onRefined(JobAdvertisementRefinedEvent event) {
		final JobAdvertisement jobAdvertisement = getJobAdvertisement(event.getAggregateId());
		if (jobAdvertisement.getSourceSystem().equals(SourceSystem.API) && (jobAdvertisement.getStellennummerAvam() == null)) {
			return;
		}
		if (hasNoContactEmail(jobAdvertisement.getContact())) {
			return;
		}
		LOG.debug("EVENT catched for mail: JOB_ADVERTISEMENT_REFINED for JobAdvertisementId: '{}'", event.getAggregateId().getValue());
		final JobCenter jobCenter = jobCenterService.findJobCenterByCode(jobAdvertisement.getJobCenterCode());
		final String stellennummer = extractStellennummer(jobAdvertisement);
		Map<String, Object> variables = new HashMap<>();
		variables.put("stellennummer", stellennummer);
		variables.put("reportingObligation", jobAdvertisement.isReportingObligation());
		variables.put("reportingObligationEndDate", nullSafeFormatLocalDate(jobAdvertisement.getReportingObligationEndDate()));
		variables.put("jobAdvertisementId", jobAdvertisement.getId().getValue());
		variables.put("accessToken", jobAdvertisement.getOwner().getAccessToken());
		variables.put("jobCenter", jobCenter);
		Locale contactLocale = new Locale(DEFAULT_LANGUAGE);
		String subject = JOB_ADVERTISEMENT_REFINED_MULTILINGUAL_SUBJECT;
		String template = JOB_ADVERTISEMENT_REFINED_MULTILINGUAL_TEMPLATE;
		if (hasContactLanguage(jobAdvertisement)) {
			contactLocale = jobAdvertisement.getContact().getLanguage();
			subject = JOB_ADVERTISEMENT_REFINED_SUBJECT;
			template = JOB_ADVERTISEMENT_REFINED_TEMPLATE;
		}
		mailSenderService.send(
				new MailSenderData.Builder()
						.setTo(parseMultipleAddresses(jobAdvertisement.getContact().getEmail()))
						.setSubject(messageSource.getMessage(subject,
								new Object[] {jobAdvertisement.getJobContent().getJobDescriptions().get(0).getTitle(), stellennummer}, contactLocale))
						.setTemplateName(template)
						.setTemplateVariables(variables)
						.setLocale(contactLocale)
						.build()
		);
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	void onRejected(JobAdvertisementRejectedEvent event) {
		final JobAdvertisement jobAdvertisement = getJobAdvertisement(event.getAggregateId());
		if (jobAdvertisement.getSourceSystem().equals(SourceSystem.API) && (jobAdvertisement.getStellennummerAvam() == null)) {
			return;
		}
		if (hasNoContactEmail(jobAdvertisement.getContact())) {
			return;
		}
		LOG.debug("EVENT catched for mail: JOB_ADVERTISEMENT_REJECTED for JobAdvertisementId: '{}'", event.getAggregateId().getValue());
		Locale contactLocale = new Locale(DEFAULT_LANGUAGE);
		if (jobAdvertisement.getContact() != null) {
			contactLocale = jobAdvertisement.getContact().getLanguage();
		}
		final JobCenter jobCenter = jobCenterService.findJobCenterByCode(jobAdvertisement.getJobCenterCode());
		final String stellennummer = extractStellennummer(jobAdvertisement);
		Map<String, Object> variables = new HashMap<>();
		variables.put("stellennummer", stellennummer);
		variables.put("rejectionReason", jobAdvertisement.getRejectionReason());
		variables.put("jobCenter", jobCenter);
		mailSenderService.send(
				new MailSenderData.Builder()
						.setTo(parseMultipleAddresses(jobAdvertisement.getContact().getEmail()))
						.setSubject(messageSource.getMessage(JOB_ADVERTISEMENT_REJECTED_SUBJECT,
								new Object[] {jobAdvertisement.getJobContent().getJobDescriptions().get(0).getTitle(), stellennummer}, contactLocale))
						.setTemplateName(JOB_ADVERTISEMENT_REJECTED_TEMPLATE)
						.setTemplateVariables(variables)
						.setLocale(contactLocale)
						.build()
		);
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	void onCancelled(JobAdvertisementCancelledEvent event) {
		final JobAdvertisement jobAdvertisement = getJobAdvertisement(event.getAggregateId());
		if (jobAdvertisement.getSourceSystem().equals(SourceSystem.API) && (jobAdvertisement.getStellennummerAvam() == null)) {
			return;
		}
		if (hasNoContactEmail(jobAdvertisement.getContact())) {
			return;
		}
		LOG.debug("EVENT catched for mail: JOB_ADVERTISEMENT_CANCELLED for JobAdvertisementId: '{}'", event.getAggregateId().getValue());
		final JobCenter jobCenter = jobCenterService.findJobCenterByCode(jobAdvertisement.getJobCenterCode());
		final String stellennummer = extractStellennummer(jobAdvertisement);
		Map<String, Object> variables = new HashMap<>();
		variables.put("stellennummer", stellennummer);
		variables.put("jobCenter", jobCenter);
		Locale contactLocale = new Locale(DEFAULT_LANGUAGE);
		String subject = JOB_ADVERTISEMENT_CANCELLED_MULTILINGUAL_SUBJECT;
		String template = JOB_ADVERTISEMENT_CANCELLED_MULTILINGUAL_TEMPLATE;
		if (hasContactLanguage(jobAdvertisement)) {
			contactLocale = jobAdvertisement.getContact().getLanguage();
			subject = JOB_ADVERTISEMENT_CANCELLED_SUBJECT;
			template = JOB_ADVERTISEMENT_CANCELLED_TEMPLATE;
		}
		mailSenderService.send(
				new MailSenderData.Builder()
						.setTo(parseMultipleAddresses(jobAdvertisement.getContact().getEmail()))
						.setSubject(messageSource.getMessage(subject,
								new Object[] {jobAdvertisement.getJobContent().getJobDescriptions().get(0).getTitle(), stellennummer}, contactLocale))
						.setTemplateName(template)
						.setTemplateVariables(variables)
						.setLocale(contactLocale)
						.build()
		);
	}

	private String extractStellennummer(JobAdvertisement jobAdvertisement) {
		if(jobAdvertisement.getStellennummerEgov() != null) {
			return jobAdvertisement.getStellennummerEgov();
		}
		if(jobAdvertisement.getStellennummerAvam() != null) {
			return jobAdvertisement.getStellennummerAvam();
		}
		return null;
	}

	private boolean hasContactLanguage(JobAdvertisement jobAdvertisement) {
		if (jobAdvertisement.getContact() == null) {
			return false;
		}
		Locale locale = jobAdvertisement.getContact().getLanguage();
		if (locale.getLanguage().equals(LANGUAGE_DE)) {
			return true;
		}
		if (locale.getLanguage().equals(LANGUAGE_EN)) {
			return true;
		}
		if (locale.getLanguage().equals(LANGUAGE_FR)) {
			return true;
		}
		if (locale.getLanguage().equals(LANGUAGE_IT)) {
			return true;
		}
		return false;
	}

	private boolean hasNoContactEmail(Contact contact) {
		return ((contact == null) || (contact.getEmail() == null));
	}

	private static String[] parseMultipleAddresses(String emailAddress) {
		return (emailAddress == null) ? null : emailAddress.split(EMAIL_DELIMITER);
	}

	private JobAdvertisement getJobAdvertisement(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
		Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId);
		return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, jobAdvertisementId.getValue()));
	}

	private String nullSafeFormatLocalDate(LocalDate date) {
		return (date != null) ? date.format(DATE_FORMATTER) : null;
	}

}
