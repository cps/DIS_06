package de.dis;

import de.dis.data.DbConnectionManager;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Arrays;

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
    PreparedStatement pstmt;
    {
        try {
            pstmt = conn.prepareStatement(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Parses CSV file, transforms date and calls function to insert sale into the database
     */
    void performETL(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date;
        try {
            String line;
            BufferedReader br = Files.newBufferedReader(Paths.get(file), StandardCharsets.ISO_8859_1);
            // Skip first line that contains row-titles
            br.readLine();
            // Read line by line
            while ((line = br.readLine()) != null) {
                String[] sale = line.split(";");

                // Parse date
                date = LocalDate.parse(sale[0], format);

                // Ignore sale, if it is malformed (= not containing five elements)
                if (sale.length == 5) {
                    writeSaleToDB(date.getDayOfMonth(), date.getMonthValue(), date.getYear(), date.get(IsoFields.QUARTER_OF_YEAR), Integer.parseInt(sale[3]), Double.parseDouble(sale[4].replace(',', '.')), sale[1], sale[2]);
                    System.out.println("Inserted record: " + Arrays.toString(sale));
                } else {
                    System.out.println("Record not inserted due to error:" + Arrays.toString(sale));
                }
            } } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void writeSaleToDB(int day, int month, int year, int quarter, int sold, double revenue, String store, String product) {
        try {
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
