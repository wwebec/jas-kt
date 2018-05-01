package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import ch.admin.seco.jobs.services.jobadservice.core.utils.MappingBuilder;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;

public enum LegacyTitleEnum {
    MISTER,
    MADAM;

    public static final MappingBuilder<LegacyTitleEnum, Salutation> MAPPING_TITLE = new MappingBuilder<LegacyTitleEnum, Salutation>()
            .put(LegacyTitleEnum.MISTER, Salutation.MR)
            .put(LegacyTitleEnum.MADAM, Salutation.MS)
            .toImmutable();

}
