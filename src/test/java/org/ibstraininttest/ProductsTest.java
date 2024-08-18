package org.ibstraininttest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Vikor_Mikhaylov
 * Класс для прогонки параметризированного теста для проверки корректности добавления товара
 */
public class ProductsTest extends BaseTest {

    @ParameterizedTest
    @CsvSource(value = {
            "Кабачок, Овощ, false",
            "Свекла, Овощ, true",
            "Банан, Фрукт, false",
            "Гро-мишель, Фрукт, true"
    })
     void addProductTest(String fruitName, String type, Boolean isExotic) {

        WebDriver driver = BaseTest.getDriver();
        WebDriverWait explicitWait =  BaseTest.getExplicitWait();

//        Открытие выпадающего списка "Песочница"
        WebElement btnSendBox = driver.findElement(By.id("navbarDropdown"));
        btnSendBox.click();
        Assertions.assertEquals(
                "navbarDropdown", btnSendBox.getAttribute("id"),
                "Не открылся выпадающий список Песочницы");

        waitToSee();

//        Клик по полю "Товары"
        WebElement itemProducts = driver.findElement(By.xpath("//a[text() ='Товары']"));
        itemProducts.click();

//        Проверка отображения списка товаров
        WebElement titleTable = driver.findElement(By.xpath("//h5[text()='Список товаров']"));
        Assertions.assertEquals(
                "Список товаров",
                titleTable.getText(),
                "Не открылась таблица со списком товаров"
        );

//        Нажатие на кнопку "Добавить"
        WebElement btnAddProduct = driver.findElement(By.xpath("//button[text() = 'Добавить']"));
        btnAddProduct.click();

        waitToSee();

//        Проверка открытия окна добавления товара
        WebElement titleAddProduct = driver.findElement(By.id("editModalLabel"));
        Assertions.assertEquals(
                "editModalLabel",
                titleAddProduct.getAttribute("id"),
                "Не открылось окно добавления товара");

//       Заполнение формы добавления товара
        WebElement fieldFruitName = driver.findElement(By.xpath("//input[@id='name']"));
        WebElement selectType = driver.findElement(By.xpath("//select[@id='type']"));
        WebElement checkBoxExotic = driver.findElement(By.xpath("//input[@type='checkbox']"));

        fieldFruitName.sendKeys(fruitName);
        selectType.sendKeys(type);
        if (isExotic) {checkBoxExotic.click();}

//        Создание массива для последующей проверки корректности добавления товара
        String[] productInputArray = new String[3];
        productInputArray[0] = fruitName;
        productInputArray[1] = type;
        productInputArray[2] = isExotic.toString();
        int arrayLength = productInputArray.length;

        waitToSee();

//       Нажатие кнопки "сохранить"
        WebElement btnSave = driver.findElement(By.id("save"));
        btnSave.click();

//        Ожидание того, что таблица станет видимой
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody")));

//        Получение информации о количестве строк в таблице
        WebElement tableProducts = driver.findElement(By.xpath("//tbody"));
        int countOfProducts = Integer.parseInt(tableProducts.getAttribute("childElementCount"));

//        Создание массива из фактически добавленных в таблицу значений
        String[] productValuesArray = new String[arrayLength];
        for (int i = 1; i <= arrayLength; i++) {
            WebElement value = driver.findElement(By.xpath(
                    "//tr[" + countOfProducts + "]/td[" + i + "]"));
            productValuesArray[i - 1] = value.getText();
        }

//        Проверка корректности добавление в таблицу товара
        Assertions.assertArrayEquals(productInputArray, productValuesArray,
                "Товар не был добавлен или добавлен некорректно");
        System.out.println("Товар успешно доваблен");

        waitToSee();
    }

    /**
     * Метод приостановки потока с целью того, чтобы увидеть как отрабатывает тест.
     * Для сокращения времени прогонки автотеста можно время приостановки потока установить - 0, либо удалить метод.
     */
    private void waitToSee() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
