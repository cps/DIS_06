package de.dis.data;

import java.util.HashMap;

public class QueryBuilder {

    HashMap geoMap = new HashMap();
    HashMap timeMap = new HashMap();
    HashMap productMap = new HashMap();

    public QueryBuilder(){

        geoMap.put("country", "co.name");
        geoMap.put("region", "r.name");
        geoMap.put("city", "c.name");
        geoMap.put("shop", "sh.name");

        timeMap.put("year", "s.year");
        timeMap.put("quarter", "s.quarter");
        timeMap.put("month", "CASE WHEN (s.month < 10) THEN CONCAT(0,s.month) ELSE CONCAT(s.month) END");
        timeMap.put("day", "CASE WHEN (s.day < 10) THEN CONCAT(0,s.day) ELSE CONCAT(s.day) END");
        timeMap.put("date", "CONCAT(s.year, ''.'', CASE WHEN (s.month < 10) THEN CONCAT(0,s.month) ELSE CONCAT(s.month) END, ''.'', CASE WHEN (s.day < 10) THEN CONCAT(0,s.day) ELSE CONCAT(s.day) END)");

        productMap.put("productcategory", "pc.productcategoryid");
        productMap.put("productfamily", "pf.productfamilyid");
        productMap.put("productgroup", "pg.productgroupid");
        productMap.put("article", "a.articleid");
    }

    public String buildContentQuery(HashMap parameter, int columns){

        String geo = (String) geoMap.get(parameter.get("geo"));
        String time = (String) timeMap.get(parameter.get("time"));
        String product = (String) productMap.get(parameter.get("product"));

        String query = "SELECT * FROM crosstab('SELECT ARRAY[" + geo + ", "+ time +"]::text[], "+ product +", SUM(s.sold)"
                    +" FROM sales s";

        query += " INNER JOIN shop sh ON sh.shopid = s.shopid";
        query += geo.equals("c.name")||geo.equals("r.name")||geo.equals("co.name") ? " INNER JOIN city c ON c.cityid = sh.cityid" : "";
        query += geo.equals("r.name")||geo.equals("co.name") ? " INNER JOIN region r ON r.regionid= c.regionid" : "";
        query += geo.equals("co.name") ? " INNER JOIN country co ON co.countryid = r.countryid" : "";

        query += " INNER JOIN article a ON a.articleid = s.articleid";
        query += product.equals("pg.productgroupid")||product.equals("pf.productfamilyid")||product.equals("pc.productcategoryid") ? " INNER JOIN productgroup pg ON pg.productgroupid = a.productgroupid" : "";
        query += product.equals("pf.productfamilyid")||product.equals("pc.productcategoryid") ? " INNER JOIN productfamily pf ON pf.productfamilyid = pg.productfamilyid" : "";
        query += product.equals("pc.productcategoryid") ? " INNER JOIN productcategory pc ON pc.productcategoryid = pf.productcategoryid" : "";

        query += " GROUP BY CUBE("+ geo + ", "+ time +", "+ product +") ORDER BY 1'";
        query += " , $$VALUES ";
        for(int i = 1; i <= columns; i++){
            query += "(" + i + ")";
            if(i != columns){
                query += ", ";
            }
        }
        query += " $$ ) AS ct (\"ROW\" text,";
        for(int i = 1; i <= columns; i++){
            query += " \""+ i +"\" int";
            if(i != columns){
                query += ", ";
            }
        }
        query += ");";

        return query;
    }

    public String buildHeaderQuery(HashMap parameter){

        return "SELECT name FROM " + parameter.get("product");
    }
}
