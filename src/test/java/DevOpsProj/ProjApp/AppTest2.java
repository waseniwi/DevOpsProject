package DevOpsProj.ProjApp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AppTest2 {

    private App2 app;

    @BeforeEach
    void setUp() {
        app = new App2();
    }

    @Test
    void testServerCreation() {
        assertTrue(app.createServer(), "Server should be created successfully");
    }

    @Test
    void testLoginPageGETRequest() throws Exception {
        URL url = URI.create("http://localhost:8080/login").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "GET request to /login should return 200");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        assertTrue(response.toString().contains("Login to Library System"), "Login page should contain the form");
    }

    @Test
    void testLoginPagePOSTRequestInvalidCredentials() throws Exception {
        URL url = URI.create("http://localhost:8080/login").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        String data = "username=wrong&password=incorrect";
        connection.getOutputStream().write(data.getBytes());

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "POST request with invalid credentials should return 200");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        assertTrue(response.toString().contains("Invalid Credentials"), "Response should contain 'Invalid Credentials'");
    }
}
