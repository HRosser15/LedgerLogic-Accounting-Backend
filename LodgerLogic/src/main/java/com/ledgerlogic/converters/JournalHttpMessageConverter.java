package com.ledgerlogic.converters;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledgerlogic.models.Journal;
import com.ledgerlogic.models.JournalEntry;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.List;

public class JournalHttpMessageConverter extends AbstractHttpMessageConverter<Journal> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JournalHttpMessageConverter() {
        super(MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Journal.class.isAssignableFrom(clazz);
    }

    @Override
    protected Journal readInternal(Class<? extends Journal> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, JournalEntry.class);
        List<JournalEntry> journalEntries = objectMapper.readValue(inputMessage.getBody(), javaType);
        return new Journal(null, null, null, null, journalEntries);
    }

    @Override
    protected void writeInternal(Journal journal, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // No need to implement this method
    }
}