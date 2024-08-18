package org.ibstraininttest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    private static WebDriver driver;
    private static WebDriverWait explicitWait;

    public static WebDriver getDriver() {
        return driver;
    }

    public static WebDriverWait getExplicitWait() {
        return explicitWait;
    }

    @BeforeAll
    public static void setProperty() {
        System.setProperty("webdriver.chromedriver.driver", "/src/test/resources/chromedriver.exe");
    }

    @BeforeEach
    public void createDriver() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        explicitWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get("http://localhost:8080");
    }

    @AfterEach
    public void closeDriver() {
        getDriver().quit();
    }
}
