package DevOpsProj.ProjApp;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class App2 {

	private static final String VALID_USERNAME = "user";
	private static final String VALID_PASSWORD = "password";
	
    public static final void main(final  String[] args )
    {
    	App2 app = new App2();
    	app.createServer();
    }
    
    public boolean createServer() {
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/login", new LoginHandler());
            server.createContext("/menu", new MenuHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
            System.out.println("Library Manager v2.0 is running on: http://localhost:8080/login");
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
                    + "<h1>Welcome to Library v2.0</h1>"
                    + "<p>More features coming soon...</p>"
                    + "</body></html>";
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
