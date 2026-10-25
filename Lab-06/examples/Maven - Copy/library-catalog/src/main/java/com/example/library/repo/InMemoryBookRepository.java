package com.example.library.repo;

import com.example.library.domain.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBookRepository implements BookRepository {
    private final Map<String, Book> byIsbn = new ConcurrentHashMap<>();

    private static final Comparator<Book> BY_TITLE = new Comparator<Book>() {
        @Override public int compare(Book a, Book b) {
            return a.getTitle().compareTo(b.getTitle());
        }
    };

    private static final Comparator<Book> BY_YEAR = new Comparator<Book>() {
        @Override public int compare(Book a, Book b) {
            return Integer.compare(a.getYear(), b.getYear());
        }
    };

    @Override public void save(Book book) {
        byIsbn.put(book.getIsbn(), book);
    }

    @Override public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(byIsbn.get(isbn));
    }

    @Override public List<Book> findByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book b : byIsbn.values()) {
            if (b.getAuthor().equalsIgnoreCase(author)) {
                result.add(b);
            }
        }
        result.sort(BY_YEAR);
        return result;
    }

    @Override public List<Book> findAll() {
        List<Book> result = new ArrayList<>(byIsbn.values());
        result.sort(BY_TITLE);
        return Collections.unmodifiableList(result);
    }

    @Override public boolean deleteByIsbn(String isbn) {
        return byIsbn.remove(isbn) != null;
    }
}
