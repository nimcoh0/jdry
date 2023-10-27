package org.softauto.analyzer.core.dto;

import java.util.LinkedList;
import java.util.function.Function;

public class Converter<T, D> {

    private final Function<LinkedList<T>, LinkedList<D>> fromDto;

    public Converter(final Function<LinkedList<T>, LinkedList<D>> fromDto) {
        this.fromDto = fromDto;
    }

    public final LinkedList<D> convertFromDto(final LinkedList<T> dto) {
        return fromDto.apply(dto);
    }


}
