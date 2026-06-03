package com.polarbookshop.catalogservice.persistence;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final Map<String, Book> booksByIsbn = new ConcurrentHashMap<>();

    @Override
    public Iterable<Book> findAll() {
        return booksByIsbn.values();
    }

    @Override
    public Optional<Book> findByIsbn( String isbn ) {
        return Optional.ofNullable( booksByIsbn.get( isbn ) );
    }

    @Override
    public boolean existsByIsbn( String isbn ) {
        return booksByIsbn.get( isbn ) != null;
    }

    @Override
    public Book save( Book book ) {
        booksByIsbn.put( book.isbn(), book );
        return book;
    }

    @Override
    public void deleteByIsbn( String isbn ) {
        booksByIsbn.remove( isbn );
    }
}
