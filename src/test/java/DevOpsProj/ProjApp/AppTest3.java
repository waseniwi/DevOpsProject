package DevOpsProj.ProjApp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AppTest3 {

	private App3 app;

    @BeforeEach
    void setUp() {
    	app = new App3();
    }

    @Test
    void testServerCreation() {
        assertTrue(app.createServer(), "Server should be created successfully");
    }
}
