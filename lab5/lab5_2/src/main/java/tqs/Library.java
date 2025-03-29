package tqs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private final List<Book> store = new ArrayList<>();

    public void addBook(final Book book) {
        store.add(book);
    }

    public List<Book> findBooksByAuthor(final String author) {
        return store.stream()
                    .filter(book -> book.getAuthor().equals(author))
                    .collect(Collectors.toList());
    }

    public List<Book> findBooks(final Date from, final Date to) {
        return store.stream()
                    .filter(book -> book.getPublished().after(from) && book.getPublished().before(to))
                    .sorted((book1, book2) -> book2.getPublished().compareTo(book1.getPublished()))
                    .collect(Collectors.toList());
    }
}
