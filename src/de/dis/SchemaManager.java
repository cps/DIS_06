package de.dis;

import de.dis.data.DbConnectionManager;
import de.dis.data.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;


public class SchemaManager {
    DbConnectionManager theManager = DbConnectionManager.getInstance();
    ScriptRunner runner = new ScriptRunner(theManager.getConnection(), false, false);

    /**
     * Creating the database Schemas based on SQL files.
     * This uses the library ScriptRunner (https://github.com/BenoitDuffez/ScriptRunner) not developed by us.
     */
     void createSchema(){
        System.out.println("Creating dimension tables");
        String stores_file= "./data/stores-and-products.sql";
        try {
            runner.runScript(new BufferedReader(new FileReader(stores_file)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            System.err.println("Error while creating dimension tables.");
            throwables.printStackTrace();
        }
         System.out.println("Created dimension tables");
         System.out.println("Creating sales and date tables");
         String sales_file= "./data/sales.sql";
         try {
             runner.runScript(new BufferedReader(new FileReader(sales_file)));
         } catch (IOException e) {
             e.printStackTrace();
         } catch (SQLException throwables) {
             System.err.println("Error while creating dimension tables.");
             throwables.printStackTrace();
         }
    }
}
