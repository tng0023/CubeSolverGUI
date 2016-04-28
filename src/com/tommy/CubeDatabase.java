package com.tommy;

import java.sql.*;
import java.util.*;

public class CubeDatabase {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";        //Configure the driver needed
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/cubes";     //Connection string – where's the database?
    static final String USER = "root";   //TODO replace with your username
    static final String PASSWORD = "Tng621180!";   //TODO replace with your password

    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rs = null;

    public final static String Cube_Table_Name = "Cube_Solvers";
    public final static String PK_COLUMN = "id";
    public final static String Name_COLUMN = "Cube_Solver";
    public final static String Time_COLUMN = "Best_Time";

    private static CubeSolverDataModel cubeDataModel;


    public static void main(String[] args) {

        if (!setup()) {
            System.exit(-1);
        }

        if (!loadAllNames()) {
            System.exit(-1);
        }

        cubeManager gui = new cubeManager(cubeDataModel);
    }

    public static boolean loadAllNames() {

        try {

            if (rs != null) {
                rs.close();
            }

            String getAllData = "SELECT * FROM " + Cube_Table_Name;
            rs = statement.executeQuery(getAllData);

            if (cubeDataModel == null) {
                cubeDataModel = new CubeSolverDataModel(rs);
            } else {
                cubeDataModel.updateResultSet(rs);
            }
            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading cube solvers");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean setup() {

        try {

            try {
                String Driver = "com.mysql.jdbc.Driver";
                Class.forName(Driver);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("No database drivers found. Quitting");
                return false;
            }

            conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);

            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);


            if (!cubeTableExists()) {

                //Create a table in the database with 3 columns: Movie title, year and rating
                String createTableSQL = "CREATE TABLE " + Cube_Table_Name + " (" + PK_COLUMN + " INT NOT NULL AUTO_INCREMENT, " + Name_COLUMN + " VARCHAR(50), " + Time_COLUMN + " FLOAT, PRIMARY KEY(" + PK_COLUMN + "))";
                System.out.println(createTableSQL);
                statement.executeUpdate(createTableSQL);

                System.out.println("Created Cube_Solvers table");

                String addDataSQL = "INSERT INTO " + Cube_Table_Name + "( " + Name_COLUMN + ", " + Time_COLUMN + ")" + " VALUES ('Tommy Ng', 2.55)";
                statement.executeUpdate(addDataSQL);
            }
            return true;

        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

    private static boolean cubeTableExists() throws SQLException {

        String checkTablePresenQuery = "SHOW TABLES LIKE '" + Cube_Table_Name + "'";
        ResultSet tablesRS = statement.executeQuery(checkTablePresenQuery);
        if(tablesRS.next()) {
            return true;
        }
        return false;
    }

    public static void shutdown(){
        try{
            if(rs != null) {
                rs.close();
                System.out.println("Result set closed");
            }
        }catch (SQLException se){
            se.printStackTrace();
        }

        try {
            if(statement != null) {
                statement.close();
                System.out.println("Statement closed");
            }
        }catch (SQLException se){
            se.printStackTrace();
        }

        try{
            if(conn != null){
                conn.close();
                System.out.println("Database connection closed");
            }
        }
        catch (SQLException se){
            se.printStackTrace();
        }
    }


//        try {
//
//            Class.forName(JDBC_DRIVER);
//
//        } catch (ClassNotFoundException cnfe) {
//            System.out.println("Can't instantiate driver class; check you have drivers and classpath configured correctly?");
//            cnfe.printStackTrace();
//            System.exit(-1);  //No driver? Need to fix before anything else will work. So quit the program
//        }
//
//        Statement statement = null;
//        Connection conn = null;
//
//        Scanner name = new Scanner(System.in);
//
//        try {
//            //You should have already created a database via terminal/command prompt OR MySQL Workbench
//
//            conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
//            statement = conn.createStatement();
//
//            //Create a table in the database, if it does not exist already
//            String createTableSQL = "CREATE TABLE IF NOT EXISTS cubes (CubeSolver varchar(100), BestTime Float)";
//            statement.executeUpdate(createTableSQL);
//            System.out.println("Created cubes table");
//
//            //Add some data
//            String addDataSQL = "INSERT INTO cubes VALUES ('Cubestormer II robot', 5.27)";
//            statement.executeUpdate(addDataSQL);
//
//            addDataSQL = "INSERT INTO cubes VALUES ('Fakhri Raihaan(using his feet)', 27.93)";
//            statement.executeUpdate(addDataSQL);
//
//            addDataSQL = "INSERT INTO cubes VALUES ('Ruxin Liu(age 3)', 99.33)";
//            statement.executeUpdate(addDataSQL);
//
//            addDataSQL = "INSERT INTO cubes VALUES ('Mats Valk(human record holder)', 6.27)";
//            statement.executeUpdate(addDataSQL);
//
//            System.out.println("Added four rows of data");
//
//        while (true){
//            System.out.println("Would you like to enter a new name and time?(Y/N)");
//            String scanner = name.next();
//            if(scanner.equalsIgnoreCase("Y")){
//                System.out.println("Enter name: ");
//                String name2 = name.next();
//                System.out.println("Enter time: ");
//                Float time2 = name.nextFloat();
//                addDataSQL = "INSERT INTO cubes VALUES ('" + name2 + "'," + time2 + ")";
//                statement.executeUpdate(addDataSQL);}
//            else if(scanner.equalsIgnoreCase("N")){
//                break;
//
//            }
//        }
//
//            //Updates a new time for Mats Valk
//            String updateData = "UPDATE cubes SET BestTime = 5.55 WHERE CubeSolver = 'Mats Valk(human record holder)'";
//            statement.executeUpdate(updateData);
//            System.out.println("Updated Data");
//
//
//
//        } catch (SQLException se) {
//            se.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            //A finally block runs whether an exception is thrown or not. Close resources and tidy up whether this code worked or not.
//            try {
//                if (statement != null) {
//                    statement.close();
//                    System.out.println("Statement closed");
//                }
//            } catch (SQLException se) {
//                //Closing the connection could throw an exception too
//                se.printStackTrace();
//            }
//            try {
//                if (conn != null) {
//                    conn.close();  //Close connection to database
//                    System.out.println("Database connection closed");
//                }
//            } catch (SQLException se) {
//                se.printStackTrace();
//            }
//        }
//        System.out.println("End of program");
//
//    }

}

