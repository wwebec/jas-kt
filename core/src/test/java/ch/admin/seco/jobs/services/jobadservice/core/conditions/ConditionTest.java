package ch.admin.seco.jobs.services.jobadservice.core.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ConditionTest {

	@Test
	public void shouldReturnNotNullValue() {
		// WHEN
		Integer result = Condition.notNull(1);

		// THEN
		assertThat(result).isEqualTo(1);
	}

	@Test(expected = ConditionException.class)
	public void shouldThrowExceptionForNull() {
		// WHEN
		Condition.notNull(null);
	}

	@Test
	public void shouldReturnNotEmptyValue() {
		// WHEN
		String value = Condition.notEmpty("value");

		// THEN
		assertThat(value).isEqualTo("value");
	}

	@Test(expected = ConditionException.class)
	public void shouldThrowExceptionForEmptyValue() {
		// WHEN
		Condition.notEmpty("");
	}

	@Test
	public void shouldReturnNotEmptyCollection() {
		// WHEN
		List<Integer> value = Condition.notEmpty(Arrays.asList(1, 2));

		// THEN
		assertThat(value).containsExactly(1, 2);
	}

	@Test(expected = ConditionException.class)
	public void shouldThrowExceptionForEmptyCollection() {
		// WHEN
		Condition.notEmpty(new ArrayList<>());
	}

	@Test
	public void shouldReturnNotBlankValue() {
		// WHEN
		String value = Condition.notBlank(" value");

		// THEN
		assertThat(value).isEqualTo(" value");
	}

	@Test(expected = ConditionException.class)
	public void shouldThrowExceptionForBlankValue() {
		// WHEN
		Condition.notBlank("   ");
	}

	@Test
	public void shouldReturnTrue() {
		// WHEN
		boolean value = Condition.isTrue(true);

		// THEN
		assertThat(value).isTrue();
	}

	@Test(expected = ConditionException.class)
	public void shouldThrowExceptionForFalse() {
		// WHEN
		Condition.isTrue(false);
	}

	@Test
	public void shouldReturnFalse() {
		// WHEN
		boolean value = Condition.isFalse(false);

		// THEN
		assertThat(value).isTrue();
	}

	@Test(expected = ConditionException.class)
	public void shouldThrowExceptionForTrue() {
		// WHEN
		Condition.isFalse(true);
	}

	@Test
	public void shouldReturnTrueWhenContains() {
		// WHEN
		boolean value = Condition.contains(Arrays.asList(1, 2), 2);

		// THEN
		assertThat(value).isTrue();
	}

	@Test(expected = ConditionException.class)
	public void shouldThrowExceptionWhenNotContains() {
		// WHEN
		Condition.contains(Arrays.asList(1, 2), 555);
	}

	@Test
	public void shouldReturnTrueWhenNotContains() {
		// WHEN
		boolean value = Condition.notContains(Arrays.asList(1, 2), 5);

		// THEN
		assertThat(value).isTrue();
	}

	@Test(expected = ConditionException.class)
	public void shouldThrowExceptionWhenContains() {
		// WHEN
		Condition.notContains(Arrays.asList(1, 2), 2);
	}
}