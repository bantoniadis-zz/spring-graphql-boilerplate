package com.example.graphqlserver.exception;

import graphql.ErrorType;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;
import java.util.Map;

/**
 * This adapter class is used to hide the exception (which can be wrapped in an ExceptionWhileDataFetching class).
 * You then need to redefine GraphQL's default error handler in @Configuration class or in @SpringBootApplication.
 */
public class GraphQLErrorAdapter implements GraphQLError {
    private GraphQLError error;

    public GraphQLErrorAdapter(GraphQLError error) {
        this.error = error;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return error.getExtensions();
    }

    @Override
    public List<SourceLocation> getLocations() {
        return error.getLocations();
    }

    @Override
    public ErrorType getErrorType() {
        return error.getErrorType();
    }

    @Override
    public Map<String, Object> toSpecification() {
        return error.toSpecification();
    }

    @Override
    public String getMessage() {
        return (error instanceof ExceptionWhileDataFetching) ?
                ((ExceptionWhileDataFetching) error).getException().getMessage() : error.getMessage();
    }
}
