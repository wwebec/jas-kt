package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderData;
import ch.admin.seco.jobs.services.jobadservice.application.MailSenderService;

public class DefaultMailSenderService implements MailSenderService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultMailSenderService.class);

    private final MailSendingTaskRepository mailSendingTaskRepository;

    DefaultMailSenderService(MailSendingTaskRepository mailSendingTaskRepository) {
        this.mailSendingTaskRepository = mailSendingTaskRepository;
    }

    @Override
    public void send(MailSenderData mailSenderData) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Save email with MailSenderData={}", mailSenderData);
        }
        mailSendingTaskRepository.save(new MailSendingTask(mailSenderData));
    }
}
