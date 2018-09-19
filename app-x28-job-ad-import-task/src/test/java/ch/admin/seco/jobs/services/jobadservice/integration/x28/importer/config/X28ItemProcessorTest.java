package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadimport.Oste;

@SpringBootTest
@RunWith(SpringRunner.class)
public class X28ItemProcessorTest {

    @Autowired
    private X28ItemProcessor x28ItemProcessor;

    @Test
    public void testProcessWithValidation() {
        // given
        Oste oste = new Oste();

        //when
        CreateJobAdvertisementFromX28Dto result = this.x28ItemProcessor.process(oste);

        //then
        assertThat(result).isNull();
    }
}