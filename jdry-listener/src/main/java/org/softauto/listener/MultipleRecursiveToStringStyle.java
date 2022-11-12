package org.softauto.listener;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

public class MultipleRecursiveToStringStyle extends ToStringStyle {

    private static final int    INFINITE_DEPTH  = -1;

    private int                 maxDepth;

    private int                 depth;

    public MultipleRecursiveToStringStyle() {
        this(INFINITE_DEPTH);
    }

    public MultipleRecursiveToStringStyle(int maxDepth) {
        setUseShortClassName(true);
        setUseIdentityHashCode(false);

        this.maxDepth = maxDepth;
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        if (value.getClass().getName().startsWith("java.lang.")
                || (maxDepth != INFINITE_DEPTH && depth >= maxDepth)) {
            buffer.append(value);
        } else {
            depth++;
            buffer.append(ReflectionToStringBuilder.toString(value, this));
            depth--;
        }
    }




    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Collection<?> coll) {
        Collections.sort((List<Comparable>) coll);
        for(Object value: coll){
            if (value.getClass().getName().startsWith("java.lang.")
                    || (maxDepth != INFINITE_DEPTH && depth >= maxDepth)) {
                buffer.append(value);
            } else {
                depth++;
                buffer.append(ReflectionToStringBuilder.toString(value, this));
                depth--;
            }
        }
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Map<?, ?> map) {
        TreeMap<?, ?> sortedMap = new TreeMap<Object, Object>(map);
        for (Map.Entry<?, ?> kvEntry : sortedMap.entrySet()) {
            Object value = kvEntry.getKey();
            if (value.getClass().getName().startsWith("java.lang.")
                    || (maxDepth != INFINITE_DEPTH && depth >= maxDepth)) {
                buffer.append(value);
            } else {
                depth++;
                buffer.append(ReflectionToStringBuilder.toString(value, this));
                depth--;
            }
            value = kvEntry.getValue();
            if (value.getClass().getName().startsWith("java.lang.")
                    || (maxDepth != INFINITE_DEPTH && depth >= maxDepth)) {
                buffer.append(value);
            } else {
                depth++;
                buffer.append(ReflectionToStringBuilder.toString(value, this));
                depth--;
            }
        }
    }
}
