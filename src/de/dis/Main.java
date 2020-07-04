package de.dis;

public class Main {

    SchemaManager schemaManager = new SchemaManager();
    DataManager dataManager = new DataManager();

    public static void main(String[] args) {
        // Configuration flags to set-up necessary actions
        // Delete Database tables
        boolean clearDB = false;
        // Create Dimension and Record tables
        boolean createSchema = true;
        // Load Data from CSV file
        boolean performETL = false;

        SchemaManager schemaManager = new SchemaManager();
        DataManager dataManager = new DataManager();

        if (clearDB) {
            System.out.println("Starting to clear DB………");
            schemaManager.clearDB();
            System.out.println("Finished to clear DB………");
        }

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

        while (true){
            //@TODO Set-up input loop for querying data (see second task)
            return;
        }


    }

}