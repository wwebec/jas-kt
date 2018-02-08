package ch.admin.seco.jobs.services.jobadservice.core.domain;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import java.util.UUID;

public class IdGenerator {

    private static final EthernetAddress ETHERNET_ADDRESS = EthernetAddress.fromInterface();

    private static final TimeBasedGenerator TIME_BASED_GENERATOR = Generators.timeBasedGenerator(ETHERNET_ADDRESS);

    private IdGenerator() {
    }

    public static UUID timeBasedUUID() {
        return TIME_BASED_GENERATOR.generate();
    }

    public static UUID randomUUID() {
        return UUID.randomUUID();
    }
}
