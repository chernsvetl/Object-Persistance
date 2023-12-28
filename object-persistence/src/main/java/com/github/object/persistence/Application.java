package org.example;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;


public class Application {
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost:5432/op_test";

    static final String USER = "postgres";
    static final String PASS = "postgres";


    public static void main(String[] args) throws Exception {

        for (Field field : Table.class.getDeclaredFields()) {

            List<String> field_name = List.of(field.getName());
            List<String> field_class = List.of((field.getType().getSimpleName()));

            field_name.forEach(System.out::println);
            field_class.forEach(System.out::println);

        }

        Connection conn = null;
        Statement stmt = null;

        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        System.out.println("Deleting database...");
        stmt = conn.createStatement();

        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        stmt = conn.createStatement();

        String sql = "CREATE TABLE Person"
                + "(id INTEGER not NULL, "
                + " age INTEGER, "
                + " PRIMARY KEY ( id ))";

        stmt.executeUpdate(sql);
        System.out.println("Created table in given database...");
        stmt.close();
        conn.close();
    }
}