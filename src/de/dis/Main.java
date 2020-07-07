package de.dis;

import de.dis.data.QueryBuilder;

import java.util.HashMap;

public class Main {

    SchemaManager schemaManager = new SchemaManager();
    DataManager dataManager = new DataManager();

    public static void main(String[] args) {
        // Configuration flags to set-up necessary actions
        // Create Dimension and Record tables
        boolean createSchema = false;
        // Load Data from CSV file
        boolean performETL = false;

        SchemaManager schemaManager = new SchemaManager();
        DataManager dataManager = new DataManager();

        if (createSchema) {
            System.out.println("Starting to create DB………");
            schemaManager.createSchema();
            System.out.println("Finished to create DB………");
        }

        if (performETL){
            System.out.println("Starting to perform ETL………");
            dataManager.performETL();
            System.out.println("Finished to perform ETL………");
        }

        boolean finished = false;

        while (!finished){

            HashMap parameter = ParameterForm.getParameter();

            TableBuilder tableBuilder = new TableBuilder();
            tableBuilder.buildTable(parameter);

            if(ParameterForm.readString("New Query? (y/n)").equals("n")){finished = true;}

        }


    }

}
