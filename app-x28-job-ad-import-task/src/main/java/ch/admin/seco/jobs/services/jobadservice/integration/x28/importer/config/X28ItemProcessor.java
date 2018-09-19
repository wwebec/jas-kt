package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadimport.Oste;

public class X28ItemProcessor implements ItemProcessor<Oste, CreateJobAdvertisementFromX28Dto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(X28ItemProcessor.class);

    private final JobAdvertisementDtoAssembler jobAdvertisementDtoAssembler;

    private final Validator validator;

    X28ItemProcessor(Validator validator) {
        this.validator = validator;
        this.jobAdvertisementDtoAssembler = new JobAdvertisementDtoAssembler();
    }

    @Override
    public CreateJobAdvertisementFromX28Dto process(Oste item) {
        CreateJobAdvertisementFromX28Dto createItem = jobAdvertisementDtoAssembler.createJobAdvertisementFromX28Dto(item);
        Set<ConstraintViolation<CreateJobAdvertisementFromX28Dto>> violations = validator.validate(createItem);
        if (violations.isEmpty()) {
            return createItem;
        }
        LOGGER.warn("Item with Fingerprint: {} has constraint violations: {}", item.getFingerprint(), violations);
        return null;
    }

}
