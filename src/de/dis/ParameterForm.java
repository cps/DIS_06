package de.dis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ParameterForm {

    private static List<String> geoList = Arrays.asList(new String[]{"country", "region", "city", "shop"});
    private static List<String> timeList = Arrays.asList(new String[]{"year", "quarter", "month", "day", "date"});
    private static List<String> productList = Arrays.asList(new String[]{"productcategory", "productfamily", "productgroup", "article"});

    public ParameterForm(){}

    public static HashMap getParameter(){

        HashMap parameter = new HashMap<String, String>();

        boolean finished = false;

        String geo = "";
        while(!finished){
            System.out.println("GEO (country, region, city, shop):");
            geo = readString("Eingabe:");
            if(geoList.contains(geo.toLowerCase())){
                finished = true;
            }else{
                System.out.println("Falsche Eingabe!");
            }
        }
        parameter.put("geo", geo.toLowerCase());

        finished = false;
        String time = "";
        while(!finished){
            System.out.println("Time (year, quarter, month, day, date):");
            time = readString("Eingabe:");
            if(timeList.contains(time.toLowerCase())){
                finished = true;
            }else{
                System.out.println("Falsche Eingabe!");
            }
        }
        parameter.put("time", time.toLowerCase());

        finished= false;
        String product = "";
        while(!finished){
            System.out.println("Product (productcategory, productfamily, productgroup, article):");
            product = readString("Eingabe:");
            if(productList.contains(product.toLowerCase())){
                finished = true;
            }else{
                System.out.println("Falsche Eingabe!");
            }
        }
        parameter.put("product", product.toLowerCase());

        return parameter;
    }

    public static String readString(String label) {
        String ret = null;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print(label+": ");
            ret = stdin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
