package ch.admin.seco.jobs.services.jobadservice.core.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MappingBuilder<L, R> {

    private final Map<L, R> mapLeft;
    private final Map<R, L> mapRight;

    public MappingBuilder() {
        this(new HashMap<>(), new HashMap<>());
    }

    private MappingBuilder(Map<L, R> mapLeft, Map<R, L> mapRight) {
        this.mapLeft = mapLeft;
        this.mapRight = mapRight;
    }

    public MappingBuilder<L, R> put(L left, R right) {
        this.mapLeft.put(left, right);
        this.mapRight.put(right, left);
        return this;
    }

    public MappingBuilder<L, R> toImmutable() {
        return new MappingBuilder<>(Collections.unmodifiableMap(mapLeft), Collections.unmodifiableMap(mapRight));
    }

    public R getRight(L left) {
        return mapLeft.get(left);
    }

    public L getLeft(R right) {
        return mapRight.get(right);
    }

}
