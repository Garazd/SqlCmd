package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to SqlCmd");

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgresSQL JDBC Driver registered");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgresSQL JDBC Driver? "
                            + "Include in your library path!");
            e.printStackTrace();
            return;
        }

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                "jdbs:postgresql://localhost:5432/sqlcmd", "postgres", "postgres");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        //insert
        try (Statement statement = connection.createStatement())
        {
            statement.executeUpdate("INSERT INTO public.user (login, password)" +
            "VALUES ('Vitaliy', 'pass')");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //select
        try (Statement statement = connection.createStatement())
        {
             ResultSet resultSet = statement.executeQuery("SELECT * FROM public.user WHERE id > 10");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // update
        try (PreparedStatement preparedStatement = connection.prepareStatement(
            "UPDATE public.user SET password = ? WHERE id > 3"))
        {
                String pass = "password_" + new Random().nextInt();
                preparedStatement.setString(1, pass);
                preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // delete
        try (Statement statement = connection.createStatement())
        {
            statement.executeUpdate("DELETE FROM public.user " +
                "WHERE id > 10 AND id < 100");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}