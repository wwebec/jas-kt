package ch.admin.seco.jobs.services.jobadservice.core.utils;

import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CompareUtilsTest {

    @Test
    public void testNotChangedContentComparingNullWithNull() {
        assertThat(CompareUtils.hasChangedContent(null, null)).isFalse();
    }

    @Test
    public void testChangedContentComparingListWithNull() {
        List<String> content = Arrays.asList("1", "2", "3");
        assertThat(CompareUtils.hasChangedContent(content, null)).isTrue();
        assertThat(CompareUtils.hasChangedContent(null, content)).isTrue();
    }

    @Test
    public void testEmptyAndSingletonList() throws Exception {
        assertThat(CompareUtils.hasChangedContent(Collections.emptyList(), Collections.emptyList())).isFalse();
        assertThat(CompareUtils.hasChangedContent(Collections.singletonList("test"), Collections.emptyList())).isTrue();
        assertThat(CompareUtils.hasChangedContent(Collections.emptyList(), Collections.singletonList("test"))).isTrue();
    }

    @Test
    public void testNotChangedContentWithDifferentOrderForList() throws Exception {
        List<String> content1 = Arrays.asList("1", "2", "3");
        List<String> content2 = Arrays.asList("3", "2", "1");
        assertThat(CompareUtils.hasChangedContent(content1, content2)).isFalse();
    }

    @Test
    public void testChangedContentWithDifferentOrderForList() throws Exception {
        List<String> content1 = Arrays.asList("1", "2", "3");
        List<String> content2 = Arrays.asList("1", "2", "1");
        assertThat(CompareUtils.hasChangedContent(content1, content2)).isTrue();
    }

    @Test
    public void testChangedContentWithNullElementInList() throws Exception {
        List<String> content1 = Arrays.asList("1", "2", "3");
        List<String> content2 = Arrays.asList("1", null, "1");
        assertThat(CompareUtils.hasChangedContent(content1, content2)).isTrue();
    }

    @Test
    public void testNotChangedContentForEmptyLists() throws Exception {
        assertThat(CompareUtils.hasChangedContent(Collections.emptyList(), new ArrayList<>())).isFalse();
    }

    @Test
    public void testNotChangedContentWithDifferentOrderForSet() throws Exception {
        Set<String> content1 = newHashSet("1", "2", "3");
        Set<String> content2 = newHashSet("3", "2", "1");
        assertThat(CompareUtils.hasChangedContent(content1, content2)).isFalse();
    }

    @Test
    public void testChangedContentWithDifferentOrderForSet() throws Exception {
        Set<String> content1 = newHashSet("1", "2", "3");
        Set<String> content2 = newHashSet("1", "2", "4");
        assertThat(CompareUtils.hasChangedContent(content1, content2)).isTrue();
    }

    @Test
    public void testNotChangedContentForEmptySets() throws Exception {
        assertThat(CompareUtils.hasChangedContent(Collections.emptySet(), new HashSet<>())).isFalse();
    }

    @Test
    public void testNotChangedComparingNullWithNull() {
        assertThat(CompareUtils.hasChanged(null, null)).isFalse();
    }

    @Test
    public void testChangedComparingObjectWithNull() {
        String content = "1";
        assertThat(CompareUtils.hasChanged(content, null)).isTrue();
        assertThat(CompareUtils.hasChanged(null, content)).isTrue();
    }

    @Test
    public void testNotChangedComparingSameObjectValue() {
        String content1 = "1";
        String content2 = "1";
        assertThat(CompareUtils.hasChanged(content1, content2)).isFalse();
    }

    @Test
    public void testChangedComparingDifferentObjectValue() {
        String content1 = "1";
        String content2 = "2";
        assertThat(CompareUtils.hasChanged(content1, content2)).isTrue();
    }

    private static <T> Set<T> newHashSet(T... elements) {
        Set<T> set = new HashSet<>();
        for (T e : elements) {
            set.add(e);
        }
        return set;
    }
}