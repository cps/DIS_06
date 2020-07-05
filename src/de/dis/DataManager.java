package de.dis;

import de.dis.data.DbConnectionManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataManager {
    private Connection conn = DbConnectionManager.getInstance().getConnection();
    private String file = "./data/sales.csv";
    private String sql = """
                 INSERT INTO sales (day, month, year, quarter, shopid, articleid, sold, revenue)
                                    SELECT
                                    	?,
                                    	?,
                                    	?,
                                    	?,
                                    s.shopid,
                                    a.articleid,
                                    ?,
                                    ?
                                     FROM
                                         dis.shop s,
                                         dis.article a
                                    WHERE
                                    	s.name = ? AND
                                    	a.name = ?;
                            """;





    void performETL(){
        //TODO Parse sales.csv and perform necessary transformations to insert data into sales table
        try {
            FileReader fr = new FileReader(file);
            String line;
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] sale = line.split(";");
                writeSaleToDB(1,1,1,1,Integer.parseInt(sale[3]), Double.parseDouble(sale[4].replace(',','.')), sale[1], sale[2]);
                System.out.println(sale);

            } } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }




    private void writeSaleToDB(int day, int month, int year, int quarter, int sold, double revenue, String store, String product) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, day);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            pstmt.setInt(4, quarter);
            pstmt.setInt(5, sold);
            pstmt.setDouble(6, revenue);
            pstmt.setString(7, store);
            pstmt.setString(8, product);
            int result = pstmt.executeUpdate();
            System.out.println("Inserted number of records: " + result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
