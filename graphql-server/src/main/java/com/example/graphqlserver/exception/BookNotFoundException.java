package com.example.graphqlserver.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookNotFoundException extends RuntimeException implements GraphQLError {
    // GraphQLError provides a field called extensions to pass additional data to the error object sent to the client.
    // In this case, we'll use it to pass the invalid book ID.
    public Map<String, Object> extensions = new HashMap<>();

    public BookNotFoundException(String message, Long invalidBookId) {
        super(message);
        extensions.put("invalidBookId", invalidBookId);
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.DataFetchingException;
    }
}
