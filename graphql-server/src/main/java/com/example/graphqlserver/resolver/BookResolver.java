package com.example.graphqlserver.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.example.graphqlserver.model.Author;
import com.example.graphqlserver.model.Book;
import com.example.graphqlserver.repository.AuthorRepository;

import java.util.Optional;

public class BookResolver implements GraphQLResolver<Book> {
    private AuthorRepository authorRepository;

    public BookResolver(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Optional<Author> getAuthor(Book book) {
        return authorRepository.findById(book.getAuthor().getId());
    }
}
