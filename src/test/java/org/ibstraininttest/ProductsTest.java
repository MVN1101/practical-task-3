package org.ibstraininttest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductsTest {

    private WebDriver driver;
    private WebDriverWait explicitWait;

    @BeforeAll
    public static void setProperty(){
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

    @ParameterizedTest
    @CsvSource(value = {
            "�������, ����, false",
            "������, ����, true",
            "�����, �����, false",
            "���-������, �����, true"
    })
     void addProductTest(String fruitName, String type, Boolean isExotic) {


//        �������� ����������� ������ "���������"
        WebElement btnSendBox = driver.findElement(By.id("navbarDropdown"));
        btnSendBox.click();
        Assertions.assertEquals(
                "navbarDropdown", btnSendBox.getAttribute("id"),
                "�� �������� ���������� ������ ���������");

        waitingToSee();

//        ���� �� ���� "������"
        WebElement itemProducts = driver.findElement(By.xpath("//a[text() ='������']"));
        itemProducts.click();

//        �������� ����������� ������ �������
        WebElement titleTable = driver.findElement(By.xpath("//h5[text()='������ �������']"));
        Assertions.assertEquals(
                "������ �������",
                titleTable.getText(),
                "�� ��������� ������� �� ������� �������"
        );

//        ������� �� ������ "��������"
        WebElement btnAddProduct = driver.findElement(By.xpath("//button[text() = '��������']"));
        btnAddProduct.click();

        waitingToSee();

//        �������� �������� ���� ���������� ������
        WebElement titleAddProduct = driver.findElement(By.id("editModalLabel"));
        Assertions.assertEquals(
                "editModalLabel",
                titleAddProduct.getAttribute("id"),
                "�� ��������� ���� ���������� ������");

//       ���������� ����� ���������� ������
        WebElement fieldFruitName = driver.findElement(By.xpath("//input[@id='name']"));
        WebElement selectType = driver.findElement(By.xpath("//select[@id='type']"));
        WebElement checkBoxExotic = driver.findElement(By.xpath("//input[@type='checkbox']"));

        fieldFruitName.sendKeys(fruitName);
        selectType.sendKeys(type);
        if (isExotic) {
            checkBoxExotic.click();
        }

//        �������� ������� ��� ����������� �������� ������������ ���������� ������
        String[] productInputArray = new String[3];
        productInputArray[0] = fruitName;
        productInputArray[1] = type;
        productInputArray[2] = isExotic.toString();
        int arrayLength = productInputArray.length;

        waitingToSee();

//       ������� ������ "���������"
        WebElement btnSave = driver.findElement(By.id("save"));
        btnSave.click();

//        �������� ����, ��� ������� ������ �������
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody")));

//        ��������� ���������� � ���������� ����� � �������
        WebElement table = driver.findElement(By.xpath("//tbody"));
        int countOfProducts = Integer.parseInt(table.getAttribute("childElementCount"));

//        �������� ������� �� ���������� ����������� � ������� ��������
        String[] productValuesArray = new String[arrayLength];
        for (int i = 1; i <= arrayLength; i++) {
            WebElement value = driver.findElement(By.xpath(
                    "//tr[" + countOfProducts + "]/td[" + i + "]"));
            productValuesArray[i - 1] = value.getText();
        }

//        �������� ������������ ���������� � ������� ������
        Assertions.assertArrayEquals(productInputArray, productValuesArray,
                "����� �� ��� �������� ��� �������� �����������");
        System.out.println("����� ������� ��������");

        waitingToSee();
    }

    @AfterEach
    public void closeDriver(){
        driver.quit();
    }


    /**
     * ����� ��������� ������ � ����� ����, ����� ������� ��� ������������ ����.
     * ��� ���������� ������� �������� ��������� ����� ����� ��������� ������ ������� - 0, ���� �������.
     */
    private void waitingToSee() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
