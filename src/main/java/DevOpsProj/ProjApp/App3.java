package DevOpsProj.ProjApp;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import service.LibraryService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


public class App3 {

    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "password";
    private static LibraryService libraryService = new LibraryService();

    public static final void main(final String[] args) {
        App3 app = new App3();
        app.createServer();
    }

    public boolean createServer() {
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/login", new LoginHandler());       
            server.createContext("/menu", new MenuHandler()); 
            server.createContext("/books", new BooksHandler()); 
            server.createContext("/books/add", new AddBookHandler());
            server.createContext("/books/delete", new DeleteBookHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Library Manager v3.0 is running on: http://localhost:8080/login");
            return true;
        } catch (final IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            
            if ("GET".equalsIgnoreCase(method)) {
                String response = "<html><body>"
                        + "<h2>Login to Library System</h2>"
                        + "<form action='/login' method='POST'>"
                        + "Username: <input type='text' name='username' /><br/>"
                        + "Password: <input type='password' name='password' /><br/>"
                        + "<input type='submit' value='Login' />"
                        + "</form>"
                        + "</body></html>";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if ("POST".equalsIgnoreCase(method)) {
                // Read form data from post request
                Map<String, String> params = parseFormParams(exchange);
                String username = params.get("username");
                String password = params.get("password");

                if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
                    String redirectUrl = "/menu";
                    exchange.getResponseHeaders().set("Location", redirectUrl);
                    exchange.sendResponseHeaders(302, -1);
                } else {
                    String response = "<html><body>"
                            + "<h2>Invalid Credentials. Please try again.</h2>"
                            + "<a href='/login'>Go back to login page</a>"
                            + "</body></html>";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        }
    }

    private static class MenuHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<html><body>"
                    + "<h1>Welcome to Library v3.0</h1>";
                        response += "<h3>Current Books in Library</h3><ul>";
            List<Book> books = libraryService.getBooks();
            if (books.isEmpty()) {
                response += "<li>No books in the library.</li>";
            } else {
                for (Book book : books) {
                    response += "<li>" + book.getTitle() + " by " + book.getAuthor() + " (ID: " + book.getId() + ")</li>";
                }
            }
            response += "</ul>";
            
            response += "<br/><a href='/books/add'>Add Book</a><br/>"
                    + "<a href='/books/delete'>Delete Book</a><br/>"
                    + "</body></html>";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static class AddBookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<html><body>"
                    + "<h2>Add a new Book</h2>"
                    + "<form action='/books' method='POST'>"
                    + "Book ID: <input type='text' name='id' /><br/>"
                    + "Title: <input type='text' name='title' /><br/>"
                    + "Author: <input type='text' name='author' /><br/>"
                    + "<input type='submit' value='Add Book' />"
                    + "</form>"
                    + "<br/><a href='/menu'>Back to Menu</a>"
                    + "</body></html>";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static class DeleteBookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<html><body>"
                    + "<h2>Delete a Book</h2>"
                    + "<form action='/books' method='POST'>"
                    + "Book ID: <input type='text' name='id' /><br/>"
                    + "<input type='submit' value='Delete Book' />"
                    + "</form>"
                    + "<br/><a href='/menu'>Back to Menu</a>"
                    + "</body></html>";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static class BooksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";

            if ("POST".equalsIgnoreCase(method)) {
                Map<String, String> params = parseFormParams(exchange);
                String id = params.get("id");
                String title = params.get("title");
                String author = params.get("author");

                if (id != null && title != null && author != null) {
                    if (title != null) title = URLDecoder.decode(title, StandardCharsets.UTF_8);
                    if (author != null) author = URLDecoder.decode(author, StandardCharsets.UTF_8);
                    libraryService.addBook(new Book(id, title, author));
                    response = "<html><body><h2>Book added successfully!</h2><a href='/menu'>Back to Menu</a></body></html>";
                } else if (id != null) {
                    libraryService.deleteBook(id);
                    exchange.getResponseHeaders().set("Location", "/menu");
                    exchange.sendResponseHeaders(302, -1); 
                    return;
                } else {
                  response = "<html><body><h2>Invalid book details.</h2><a href='/books/add'>Try again</a></body></html>";
                }
            }
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // Helper method to parse the POST form parameters
    private static Map<String, String> parseFormParams(HttpExchange exchange) throws IOException {
        Map<String, String> params = new HashMap<>();
        String query = new String(exchange.getRequestBody().readAllBytes());
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }
}
