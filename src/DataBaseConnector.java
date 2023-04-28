import java.sql.*;

/*
* Umożliwia nawiązanie połączenia z bazą danych.*/
public class DataBaseConnector {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_NAME = "expense_manager_database";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/" + DB_NAME +"?";
    private static final String USER = "databaseUser";
    private static final String PASSWORD = "!HuTh%4DtYfTraA";

    /*
    * Nawiązuje połączenie z bazą danych i je zwraca
    * @return Connection conn obiek przechowujący połączenie z bazą danych*/
    public Connection startConnection(){
        Connection conn = null;
        try{
            Class.forName(DB_DRIVER);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            conn = DriverManager.getConnection( URL, USER, PASSWORD);

        } catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return  conn;
    }
}

