package com.proyectofinal.proyectofinal.utils;

import org.modelmapper.ModelMapper;

import java.util.List;

public class EntityMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        configure();
    }

    private static void configure() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setImplicitMappingEnabled(true)
                .setSkipNullEnabled(true);
    }

    public static <D, T> D map(T source, Class<D> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    public static <D, T> List<D> mapList(List<T> sourceList, Class<D> targetClass) {
        return sourceList.stream()
                .map(element -> map(element, targetClass))
                .toList();
    }
}