package com.github.ullaspprabhakar.fjord.framework.common.service.impl;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import org.modelmapper.AbstractConverter;

public class StringToNullConverter extends AbstractConverter<String, String> {
    @Override
    protected String convert(String source) {
        return (source == null || source.trim().isEmpty()) ? null : source.trim();
    }
}
