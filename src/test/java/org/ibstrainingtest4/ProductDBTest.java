package org.ibstrainingtest4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author Vikor_Mikhaylov
 * Класс для прогонки теста проверки корректности добавления товара в базу данных (БД)
 */
public class ProductDBTest {

    @Test
    void addProductDBTest() throws SQLException {

//        Создание соединения
        Connection connection = DriverManager.getConnection(
                "jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user",
                "pass");

        Statement statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

//        Получение данных из таблицы
        String queryAllFromFood = "SELECT * FROM food";
        ResultSet resultSet = statement.executeQuery(queryAllFromFood);

//        Создание переменной для определения количества строк в таблице
        resultSet.last();
        int countOfRows = resultSet.getRow();

//        Создание и инициализация переменных для команды INSERT
        int id = countOfRows + 1;
        String productName = "картофель";
        String type = "VEGETABLE";
        int isExotic = 0;

//        Создание коллекции с информацией о добавляемом товаре
//        с целью последующей проверки корректности его добавления в БД
        ArrayList<Object> productToBeAddList = new ArrayList<>();
        productToBeAddList.add(id);
        productToBeAddList.add(productName);
        productToBeAddList.add(type);
        productToBeAddList.add(isExotic);

//        Выполнение запроса добавления продукта в БД
        String insert = "INSERT INTO food VALUES (" + id + ",'" + productName + "','" + type + "'," + isExotic + ")";
        statement.executeUpdate(insert);

//        Повторный запрос всех данных таблицы
        resultSet = statement.executeQuery(queryAllFromFood);

//        Получение информации из БД о добавленном товаре
        resultSet.last();
        int idDB = resultSet.getInt("FOOD_ID");
        String fruitNameDB = resultSet.getString("FOOD_NAME");
        String typeDB = resultSet.getString("FOOD_TYPE");
        int isExoticDB = resultSet.getInt("FOOD_EXOTIC");

//        Создание коллекции с информацией о добавленном товаре, полученной из БД
        ArrayList<Object> productAddedList = new ArrayList<>();
        productAddedList.add(idDB);
        productAddedList.add(fruitNameDB);
        productAddedList.add(typeDB);
        productAddedList.add(isExoticDB);

//        Проверка корректности добавления товара
        Assertions.assertEquals(productToBeAddList,productAddedList,
                "Товар не был добавлен в БД или добавлен некорректно");

//        Удаление добавленной сторки, закрытие соединения
        resultSet.deleteRow();
        connection.close();
    }
}
