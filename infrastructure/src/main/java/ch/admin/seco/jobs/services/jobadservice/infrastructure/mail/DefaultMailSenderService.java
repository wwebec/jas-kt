package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.util.IDNEmailAddressConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import org.springframework.context.MessageSource;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderData;
import ch.admin.seco.jobs.services.jobadservice.application.MailSenderService;

class DefaultMailSenderService implements MailSenderService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultMailSenderService.class);

    private final MailSendingTaskRepository mailSendingTaskRepository;

    private final SpringTemplateEngine templateEngine;

    private final MailSenderProperties mailSenderProperties;

    private final MessageSource messageSource;

    private final IDNEmailAddressConverter idnEmailAddressConverter;

    DefaultMailSenderService(MailSendingTaskRepository mailSendingTaskRepository, SpringTemplateEngine templateEngine, MailSenderProperties mailSenderProperties, MessageSource messageSource, IDNEmailAddressConverter idnEmailAddressConverter) {
        this.mailSendingTaskRepository = mailSendingTaskRepository;
        this.templateEngine = templateEngine;
        this.mailSenderProperties = mailSenderProperties;
        this.messageSource = messageSource;
        this.idnEmailAddressConverter = idnEmailAddressConverter;
    }

    @Override
    public void send(MailSenderData mailSenderData) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Save email with MailSenderData={}", mailSenderData);
        }

        mailSendingTaskRepository.save(new MailSendingTask(toMailData(mailSenderData)));
    }

    private MailSendingTask.MailSendingTaskData toMailData(MailSenderData mailSenderData) {
        String subject = messageSource.getMessage(mailSenderData.getSubject(), null, mailSenderData.getSubject(), mailSenderData.getLocale());
        String content = createContent(mailSenderData);
        String from = mailSenderData.getFrom().orElse(mailSenderProperties.getFromAddress());
        String[] bcc = mailSenderData.getBcc().orElse(mailSenderProperties.getBccAddress());
        return MailSendingTask.builder()
                .setBcc(bcc)
                .setCc(encodeEmailAddresses(mailSenderData.getCc()))
                .setContent(content)
                .setFrom(from)
                .setSubject(subject)
                .setTo(encodeEmailAddresses(mailSenderData.getTo()))
                .build();
    }

    private String createContent(MailSenderData mailSenderData) {
        return StringUtils.strip(templateEngine.process(mailSenderData.getTemplateName(), createTemplateContext(mailSenderData)));
    }

    private Context createTemplateContext(MailSenderData mailSenderData) {
        Context context = new Context();
        context.setVariable("baseUrl", mailSenderProperties.getBaseUrl());
        context.setVariable("linkToJobAdDetailPage", mailSenderProperties.getLinkToJobAdDetailPage());
        context.setVariable("user", null);
        context.setVariables(mailSenderData.getTemplateVariables());
        context.setLocale(mailSenderData.getLocale());
        return context;
    }

    private String[] encodeEmailAddresses(String[] addresses) {
        return addresses != null
                ? Stream.of(addresses)
                .map(idnEmailAddressConverter::toASCII)
                .toArray(String[]::new)
                : null;
    }
}
