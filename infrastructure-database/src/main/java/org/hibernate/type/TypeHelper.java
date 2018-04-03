//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.hibernate.type;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.bytecode.enhance.spi.LazyPropertyInitializer;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.property.access.internal.PropertyAccessStrategyBackRefImpl;
import org.hibernate.tuple.NonIdentifierAttribute;

public class TypeHelper {

    private TypeHelper() {
    }

    public static void deepCopy(Object[] values, Type[] types, boolean[] copy, Object[] target, SharedSessionContractImplementor session) {
        for (int i = 0; i < types.length; ++i) {
            if (copy[i]) {
                if (values[i] != LazyPropertyInitializer.UNFETCHED_PROPERTY && values[i] != PropertyAccessStrategyBackRefImpl.UNKNOWN) {
                    target[i] = types[i].deepCopy(values[i], session.getFactory());
                } else {
                    target[i] = values[i];
                }
            }
        }

    }

    public static void beforeAssemble(Serializable[] row, Type[] types, SharedSessionContractImplementor session) {
        for (int i = 0; i < types.length; ++i) {
            if (row[i] != LazyPropertyInitializer.UNFETCHED_PROPERTY && row[i] != PropertyAccessStrategyBackRefImpl.UNKNOWN) {
                types[i].beforeAssemble(row[i], session);
            }
        }

    }

    public static Object[] assemble(Serializable[] row, Type[] types, SharedSessionContractImplementor session, Object owner) {
        Object[] assembled = new Object[row.length];

        for (int i = 0; i < types.length; ++i) {
            if (row[i] != LazyPropertyInitializer.UNFETCHED_PROPERTY && row[i] != PropertyAccessStrategyBackRefImpl.UNKNOWN) {
                assembled[i] = types[i].assemble(row[i], session, owner);
            } else {
                assembled[i] = row[i];
            }
        }

        return assembled;
    }

    public static Serializable[] disassemble(Object[] row, Type[] types, boolean[] nonCacheable, SharedSessionContractImplementor session, Object owner) {
        Serializable[] disassembled = new Serializable[row.length];

        for (int i = 0; i < row.length; ++i) {
            if (nonCacheable != null && nonCacheable[i]) {
                disassembled[i] = LazyPropertyInitializer.UNFETCHED_PROPERTY;
            } else if (row[i] != LazyPropertyInitializer.UNFETCHED_PROPERTY && row[i] != PropertyAccessStrategyBackRefImpl.UNKNOWN) {
                disassembled[i] = types[i].disassemble(row[i], session, owner);
            } else {
                disassembled[i] = (Serializable) row[i];
            }
        }

        return disassembled;
    }

    public static Object[] replace(Object[] original, Object[] target, Type[] types, SharedSessionContractImplementor session, Object owner, Map copyCache) {
        Object[] copied = new Object[original.length];

        for (int i = 0; i < types.length; ++i) {
            if (original[i] != LazyPropertyInitializer.UNFETCHED_PROPERTY && original[i] != PropertyAccessStrategyBackRefImpl.UNKNOWN) {
                if (target[i] == LazyPropertyInitializer.UNFETCHED_PROPERTY) {
                    if (types[i].isMutable()) {
                        copied[i] = types[i].deepCopy(original[i], session.getFactory());
                    } else {
                        copied[i] = original[i];
                    }
                } else {
                    copied[i] = types[i].replace(original[i], target[i], session, owner, copyCache);
                }
            } else {
                copied[i] = target[i];
            }
        }

        return copied;
    }

    public static Object[] replace(Object[] original, Object[] target, Type[] types, SharedSessionContractImplementor session, Object owner, Map copyCache, ForeignKeyDirection foreignKeyDirection) {
        Object[] copied = new Object[original.length];

        for (int i = 0; i < types.length; ++i) {
            if (original[i] != LazyPropertyInitializer.UNFETCHED_PROPERTY && original[i] != PropertyAccessStrategyBackRefImpl.UNKNOWN) {
                copied[i] = types[i].replace(original[i], target[i], session, owner, copyCache, foreignKeyDirection);
            } else {
                copied[i] = target[i];
            }
        }

        return copied;
    }

    public static Object[] replaceAssociations(Object[] original, Object[] target, Type[] types, SharedSessionContractImplementor session, Object owner, Map copyCache, ForeignKeyDirection foreignKeyDirection) {
        Object[] copied = new Object[original.length];

        for (int i = 0; i < types.length; ++i) {
            if (original[i] != LazyPropertyInitializer.UNFETCHED_PROPERTY && original[i] != PropertyAccessStrategyBackRefImpl.UNKNOWN) {
                if (types[i].isComponentType()) {
                    CompositeType componentType = (CompositeType) types[i];
                    Type[] subtypes = componentType.getSubtypes();
                    Object[] origComponentValues = original[i] == null ? new Object[subtypes.length] : componentType.getPropertyValues(original[i], session);
                    Object[] targetComponentValues = target[i] == null ? new Object[subtypes.length] : componentType.getPropertyValues(target[i], session);

                    // quickfix to support ElementCollection in nested Embedded objects
                    // see https://hibernate.atlassian.net/browse/OGM-1261
                    Object[] values = replaceAssociations(origComponentValues, targetComponentValues, subtypes, session, null, copyCache, foreignKeyDirection);
                    if (target[i] != null) {
                        componentType.setPropertyValues(target[i], values, null);
                    }
                    copied[i] = target[i];
                } else if (!types[i].isAssociationType()) {
                    copied[i] = target[i];
                } else {
                    copied[i] = types[i].replace(original[i], target[i], session, owner, copyCache, foreignKeyDirection);
                }
            } else {
                copied[i] = target[i];
            }
        }

        return copied;
    }

    /** @deprecated */
    @Deprecated
    public static int[] findDirty(NonIdentifierAttribute[] properties, Object[] currentState, Object[] previousState, boolean[][] includeColumns, boolean anyUninitializedProperties, SharedSessionContractImplementor session) {
        return findDirty(properties, currentState, previousState, includeColumns, session);
    }

    public static int[] findDirty(NonIdentifierAttribute[] properties, Object[] currentState, Object[] previousState, boolean[][] includeColumns, SharedSessionContractImplementor session) {
        int[] results = null;
        int count = 0;
        int span = properties.length;

        for (int i = 0; i < span; ++i) {
            boolean dirty = currentState[i] != LazyPropertyInitializer.UNFETCHED_PROPERTY && properties[i].isDirtyCheckable() && properties[i].getType().isDirty(previousState[i], currentState[i], includeColumns[i], session);
            if (dirty) {
                if (results == null) {
                    results = new int[span];
                }

                results[count++] = i;
            }
        }

        if (count == 0) {
            return null;
        } else {
            int[] trimmed = new int[count];
            System.arraycopy(results, 0, trimmed, 0, count);
            return trimmed;
        }
    }

    /** @deprecated */
    @Deprecated
    public static int[] findModified(NonIdentifierAttribute[] properties, Object[] currentState, Object[] previousState, boolean[][] includeColumns, boolean[] includeProperties, boolean anyUninitializedProperties, SharedSessionContractImplementor session) {
        return findModified(properties, currentState, previousState, includeColumns, includeProperties, session);
    }

    public static int[] findModified(NonIdentifierAttribute[] properties, Object[] currentState, Object[] previousState, boolean[][] includeColumns, boolean[] includeProperties, SharedSessionContractImplementor session) {
        int[] results = null;
        int count = 0;
        int span = properties.length;

        for (int i = 0; i < span; ++i) {
            boolean modified = currentState[i] != LazyPropertyInitializer.UNFETCHED_PROPERTY && includeProperties[i] && properties[i].isDirtyCheckable() && properties[i].getType().isModified(previousState[i], currentState[i], includeColumns[i], session);
            if (modified) {
                if (results == null) {
                    results = new int[span];
                }

                results[count++] = i;
            }
        }

        if (count == 0) {
            return null;
        } else {
            int[] trimmed = new int[count];
            System.arraycopy(results, 0, trimmed, 0, count);
            return trimmed;
        }
    }

    public static String toLoggableString(Object[] state, Type[] types, SessionFactoryImplementor factory) {
        StringBuilder buff = new StringBuilder();

        for (int i = 0; i < state.length; ++i) {
            if (i > 0) {
                buff.append(", ");
            }

            if (state[i] != LazyPropertyInitializer.UNFETCHED_PROPERTY && Hibernate.isInitialized(state[i])) {
                buff.append(types[i].toLoggableString(state[i], factory));
            } else {
                buff.append("<uninitialized>");
            }
        }

        return buff.toString();
    }
}
