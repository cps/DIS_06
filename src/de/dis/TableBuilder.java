package de.dis;

import de.dis.data.DbConnectionManager;
import de.dis.data.QueryBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableBuilder {

    HashMap geoMap = new HashMap();
    HashMap timeMap = new HashMap();

    public TableBuilder(){

        geoMap.put("country", "Country");
        geoMap.put("region", "Region");
        geoMap.put("city", "City");
        geoMap.put("shop", "Shop");

        timeMap.put("year", "Year");
        timeMap.put("quarter", "Quarter");
        timeMap.put("month", "Month");
        timeMap.put("day", "Day");
        timeMap.put("date", "Date");
    }

    public void buildTable(HashMap parameter){

        try {

            Connection con = DbConnectionManager.getInstance().getConnection();
            
            QueryBuilder queryBuilder = new QueryBuilder();
            String headerQuery = queryBuilder.buildHeaderQuery(parameter);
            PreparedStatement headerPreparedStatement = con.prepareStatement(headerQuery);
            ResultSet headerResult = headerPreparedStatement.executeQuery();

            ArrayList<ArrayList> table = new ArrayList();
            ArrayList header = new ArrayList();
            header.add(geoMap.get(parameter.get("geo")));
            header.add(timeMap.get(parameter.get("time")));
            while(headerResult.next()){
                header.add(headerResult.getString(1));
            }
            headerPreparedStatement.close();

            header.add("Total");
            table.add(header);
            int headerCount = header.size()-3;

            String contentQuery = queryBuilder.buildContentQuery(parameter, headerCount);
            PreparedStatement contentPreparedStatement = con.prepareStatement(contentQuery);
            ResultSet contentResult = contentPreparedStatement.executeQuery();
            ResultSetMetaData rsmt = contentResult.getMetaData();
            int columnCount = rsmt.getColumnCount();

            while(contentResult.next()){
                ArrayList row = new ArrayList();

                String rsFirstColumn = contentResult.getString(1);
                String[] rsFirstColumnArray = rsFirstColumn.split(",");
                String firstColumn = rsFirstColumnArray[0].replace("{", "").replace("\"", "");
                row.add(firstColumn.equals("NULL")? "All" : firstColumn );
                String secondColumn = rsFirstColumnArray[1].replace("}", "");
                row.add(secondColumn.equals("NULL")? "All" : secondColumn );

                int total = 0;
                for(int i = 2; i <= columnCount; i++){
                    int content = contentResult.getInt(i);
                    row.add(content);
                    total += content;
                }
                row.add(total);
                table.add(row);
            }
            contentPreparedStatement.close();

            List<String> limits = new ArrayList<>();
            int maxCharCounter = 0;
            for(int i = 0; i < table.size();i++){
                List row = table.get(i);
                int length = row.get(0).toString().length();
                if( length > maxCharCounter){
                    maxCharCounter = length;
                }
            }
            limits.add("| %-" + maxCharCounter + "s ");
            limits.add("| %-10s ");

            for(int j = 2; j < table.get(0).size()-1;j++){
                int length = table.get(0).get(j).toString().length();
                if(length < 7){
                    length = 7;
                }
                limits.add("| %-" + length + "s ");
            }
            limits.add("| %-8s |%n");

            for(int i = 0; i < table.size(); i++){
                ArrayList row = table.get(i);
                for(int j = 0; j < row.size(); j++){
                    System.out.format(limits.get(j), row.get(j));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
