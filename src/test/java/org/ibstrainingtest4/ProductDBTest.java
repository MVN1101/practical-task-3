package org.ibstrainingtest4;

import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author Vikor_Mikhaylov
 * ����� ��� �������� ����� �������� ������������ ���������� ������ � ���� ������ (��)
 */
public class ProductDBTest {

    @Test
    void addProductDBTest() throws SQLException {

//        �������� ����������
        Connection connection = DriverManager.getConnection(
                "jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user",
                "pass");

        Statement statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

//        ��������� ������ �� �������
        String queryAllFromFood = "SELECT * FROM food";
        ResultSet resultSetSelectAll = statement.executeQuery(queryAllFromFood);

//        �������� ���������� ��� ����������� ���������� ����� � �������
        resultSetSelectAll.last();
        int countOfRows = resultSetSelectAll.getRow();

//        �������� � ������������� ���������� ��� ������� INSERT
        int id = countOfRows + 1;
        String productName = "���������";
        String type = "VEGETABLE";
        int isExotic = 0;

//        �������� ��������� � ����������� � ����������� ������
//        � ����� ����������� �������� ������������ ��� ���������� � ��
        ArrayList<Object> productToBeAddList = new ArrayList<>();
        productToBeAddList.add(id);
        productToBeAddList.add(productName);
        productToBeAddList.add(type);
        productToBeAddList.add(isExotic);

//        ���������� ������� ���������� �������� � ��
        String insert = "INSERT INTO food VALUES (" + id + ",'" + productName + "','" + type + "'," + isExotic + ")";
        statement.executeUpdate(insert);

//        ��������� ������ ���� ������ �������
        resultSetSelectAll = statement.executeQuery(queryAllFromFood);

//        ��������� ���������� �� �� � ����������� ������
        resultSetSelectAll.last();
        int idBD = resultSetSelectAll.getInt("FOOD_ID");
        String fruitNameBD = resultSetSelectAll.getString("FOOD_NAME");
        String typeBD = resultSetSelectAll.getString("FOOD_TYPE");
        int isExoticBD = resultSetSelectAll.getInt("FOOD_EXOTIC");

//        �������� ��������� � ����������� � ����������� ������, ���������� �� ��
        ArrayList<Object> productAddedList = new ArrayList<>();
        productAddedList.add(idBD);
        productAddedList.add(fruitNameBD);
        productAddedList.add(typeBD);
        productAddedList.add(isExoticBD);

//        �������� ������������ ���������� ������
        if (productToBeAddList.equals(productAddedList)) {
            System.out.printf(
                    "����� (id: %d, ������������: %s, ���: %s, ������������: %d) ��� ������� �������� � ��",
                    idBD, fruitNameBD, typeBD, isExoticBD);
        } else {
            throw new SQLException("����� �� ��� �������� � �� ��� �������� �����������");
        }

//        �������� ����������� ������, �������� ����������
        resultSetSelectAll.deleteRow();
        connection.close();
    }
}
