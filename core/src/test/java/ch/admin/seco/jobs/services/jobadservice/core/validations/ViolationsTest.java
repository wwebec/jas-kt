package ch.admin.seco.jobs.services.jobadservice.core.validations;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ViolationsTest {

    @Test
    public void shouldAddIfTrue() {
        Violations violations = new Violations();

        violations.addIfTrue(true, "message %s %s", "argument1", "argument2");

        assertThat(violations.isEmpty()).isFalse();
        List<String> messages = violations.getMessages();
        assertThat(messages.size()).isEqualTo(1);
        assertThat(messages.get(0)).isEqualTo("message argument1 argument2");
    }

    @Test
    public void shouldAddIfFalse() {
        Violations violations = new Violations();

        violations.addIfFalse(false, "message %s", "argument1");

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.getCount()).isEqualTo(1);
        assertThat(violations.getMessages().get(0)).isEqualTo("message argument1");
    }

    @Test
    public void shouldAddIfNull() {
        Violations violations = new Violations();

        violations.addIfNull(null, "message");

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.getCount()).isEqualTo(1);
        assertThat(violations.getMessages().get(0)).isEqualTo("message");
    }

    @Test
    public void shouldAddIfNotNull() {
        Violations violations = new Violations();

        violations.addIfNotNull(new Object(), "message");

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.getCount()).isEqualTo(1);
        assertThat(violations.getMessages().get(0)).isEqualTo("message");
    }

    @Test
    public void shouldAddIfEmptyString() {
        Violations violations = new Violations();

        violations.addIfEmpty((String) null, "message");
        violations.addIfEmpty("", "message");
        violations.addIfEmpty("  ", "message");

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.getCount()).isEqualTo(2);
    }

    @Test
    public void shouldAddIfEmptyCollection() {
        Violations violations = new Violations();

        violations.addIfEmpty(new HashSet(), "message");
        violations.addIfEmpty(new ArrayList(), "message");
        violations.addIfEmpty(Arrays.asList("a", "b", "c"), "message");

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.getCount()).isEqualTo(2);
    }

    @Test
    public void shouldAddIfBlank() {
        Violations violations = new Violations();

        violations.addIfBlank(null, "message");
        violations.addIfBlank("", "message");
        violations.addIfBlank("  ", "message");
        violations.addIfBlank("   a   ", "message");

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.getCount()).isEqualTo(3);
    }

    @Test
    public void shouldAddNothing() {
        Violations violations = new Violations();

        violations.addIfTrue(false, "message");
        violations.addIfFalse(true, "message");
        violations.addIfNull(new Object(), "message");
        violations.addIfNotNull(null, "message");
        violations.addIfEmpty("abc", "message");
        violations.addIfEmpty(Arrays.asList("a", "b", "c"), "message");
        violations.addIfBlank("abc", "message");

        assertThat(violations.isEmpty()).isTrue();
    }

}