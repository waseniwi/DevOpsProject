package DevOpsProj.ProjApp;

import java.util.List;
import java.util.Scanner;

import model.Book;
import service.LibraryService;

/**
 * Hello world!
 */
public class App {
	 private static LibraryService libraryService = new LibraryService();

	    public static void main(String[] args) {
	    	System.out.println("\nCurrent Books in Library:");
	    	List<Book> books = libraryService.getBooks();
	    	books.forEach(System.out::println);
	        Scanner scanner = new Scanner(System.in);
	        int option;

	        do {
	            System.out.println("Library Management System");
	            System.out.println("1. Add Book");
	            System.out.println("2. Delete Book");
	            System.out.println("0. Exit");
	            System.out.print("Select an option: ");
	            option = scanner.nextInt();
	            scanner.nextLine();

	            switch (option) {
	                case 1:
	                    System.out.print("Enter book ID: ");
	                    String id = scanner.nextLine();
	                    System.out.print("Enter book title: ");
	                    String title = scanner.nextLine();
	                    System.out.print("Enter book author: ");
	                    String author = scanner.nextLine();
	                    libraryService.addBook(new Book(id, title, author));
	                    System.out.println("Book added successfully!");
	                    break;
	                case 2:
	                    System.out.print("Enter book ID to delete: ");
	                    String deleteId = scanner.nextLine();
	                    libraryService.deleteBook(deleteId);
	                    System.out.println("Book deleted successfully!");
	                    break;
	                case 0:
	                    System.out.println("Exiting...");
	                    break;
	                default:
	                    System.out.println("Invalid option. Please try again.");
	            }

	            
	            System.out.println("\nCurrent Books in Library:");
	            if (books.isEmpty()) {
	                System.out.println("No books in the library.");
	            } else {
	                books.forEach(System.out::println);
	            }
	            System.out.println();

	        } while (option != 0);

	        scanner.close();
	    }
}
