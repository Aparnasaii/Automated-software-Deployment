package com.deploymentpipeline.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Selenium End-to-End Tests")
@Disabled("Run manually - requires Chrome browser and running application")
class SeleniumE2ETest {

    private WebDriver driver;
    private static final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Swagger UI should be accessible")
    void swaggerUiShouldBeAccessible() {
        driver.get(BASE_URL + "/swagger-ui.html");

        String pageTitle = driver.getTitle();
        assertNotNull(pageTitle);

        WebElement swaggerContainer = driver.findElement(By.id("swagger-ui"));
        assertTrue(swaggerContainer.isDisplayed());
    }

    @Test
    @DisplayName("H2 Console should be accessible in dev mode")
    void h2ConsoleShouldBeAccessible() {
        driver.get(BASE_URL + "/h2-console");

        String pageSource = driver.getPageSource();
        assertNotNull(pageSource);
        assertTrue(pageSource.contains("H2 Console") || pageSource.contains("Login"));
    }

    @Test
    @DisplayName("API docs endpoint should return JSON")
    void apiDocsShouldBeAccessible() {
        driver.get(BASE_URL + "/api-docs");

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("openapi") || pageSource.contains("paths"));
    }
}
