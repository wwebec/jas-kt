package ch.admin.seco.jobs.services.jobadservice.core.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MappingBuilderTest {

    static final String LEFT_1 = "A";
    static final String LEFT_2 = "B";
    static final String LEFT_3 = "C";
    static final String LEFT_4 = "D";
    static final Integer RIGHT_1 = 1;
    static final Integer RIGHT_2 = 2;
    static final Integer RIGHT_3 = 3;
    static final Integer RIGHT_4 = 4;

    @Test
    public void testPut() {
        MappingBuilder<String, Integer> map = new MappingBuilder<String, Integer>()
                .put(LEFT_1, RIGHT_1)
                .put(LEFT_2, RIGHT_2)
                .put(LEFT_3, RIGHT_3);
        assertThat(map.size()).isEqualTo(3);
    }

    @Test
    public void testToImmutable() {
        MappingBuilder<String, Integer> map = new MappingBuilder<String, Integer>()
                .put(LEFT_1, RIGHT_1)
                .put(LEFT_2, RIGHT_2)
                .put(LEFT_3, RIGHT_3)
                .toImmutable();
        assertThatThrownBy(() -> map.put(LEFT_4, RIGHT_4)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testGetRight() {
        MappingBuilder<String, Integer> map = new MappingBuilder<String, Integer>()
                .put(LEFT_1, RIGHT_1)
                .put(LEFT_2, RIGHT_2)
                .put(LEFT_3, RIGHT_3);
        assertThat(map.getRight(LEFT_1)).isEqualTo(RIGHT_1);
        assertThat(map.getRight(LEFT_2)).isEqualTo(RIGHT_2);
        assertThat(map.getRight(LEFT_3)).isEqualTo(RIGHT_3);
    }

    @Test
    public void testGetLeft() {
        MappingBuilder<String, Integer> map = new MappingBuilder<String, Integer>()
                .put(LEFT_1, RIGHT_1)
                .put(LEFT_2, RIGHT_2)
                .put(LEFT_3, RIGHT_3);
        assertThat(map.getLeft(RIGHT_1)).isEqualTo(LEFT_1);
        assertThat(map.getLeft(RIGHT_2)).isEqualTo(LEFT_2);
        assertThat(map.getLeft(RIGHT_3)).isEqualTo(LEFT_3);
    }
}