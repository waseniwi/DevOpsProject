package service;

import java.util.ArrayList;
import java.util.List;

import model.Book;

public class LibraryService {
	private List<Book> books;

    public LibraryService() {
        this.books = new ArrayList<>();
        initializeDefaultBooks(); 
    }

    private void initializeDefaultBooks() {
        books.add(new Book("1", "The Catcher in the Rye", "J.D. Salinger"));
        books.add(new Book("2", "To Kill a Mockingbird", "Harper Lee"));
        books.add(new Book("3", "1984", "George Orwell"));
        books.add(new Book("4", "The Great Gatsby", "F. Scott Fitzgerald"));
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void deleteBook(String id) {
        books.removeIf(book -> book.getId().equals(id));
    }

    public List<Book> getBooks() {
        return books;
    }
}
