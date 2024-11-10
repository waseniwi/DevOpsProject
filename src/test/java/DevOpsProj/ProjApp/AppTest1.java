package DevOpsProj.ProjApp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LibraryService;

public class AppTest1 {

	private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        libraryService = new LibraryService();
    }

    @Test
    void testAddBook() {
        Book book = new Book("1", "The Great Gatsby", "F. Scott Fitzgerald");
        libraryService.addBook(book);
        List<Book> books = libraryService.getBooks();
        assertTrue(books.contains(book), "The book should be added to the library");
    }

    @Test
    void testDeleteBook() {
        Book book = new Book("2", "1984", "George Orwell");
        libraryService.addBook(book);
        libraryService.deleteBook("2");
        List<Book> books = libraryService.getBooks();
        assertFalse(books.contains(book), "The book should be removed from the library");
    }

    @Test
    void testInitialOutput() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        String simulatedInput = "0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        App.main(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("Library v.1.0"), "Initial output should include version info");
        assertTrue(output.contains("Library Management System"), "Initial output should include the main menu");

        System.setOut(System.out);
        System.setIn(System.in);
    }
}
