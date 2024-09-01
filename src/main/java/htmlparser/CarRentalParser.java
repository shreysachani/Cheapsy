package htmlparser;


import model.Car_info;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import org.jsoup.select.*;

import java.io.*;

import java.util.*;
import java.util.regex.*;

// parseer for carrental website
public class CarRentalParser {

	// fetch data
    public static List<Car_info> fetchAll_CarRental_Deals(){
        String website1_Path = "CarRentalFiles/orbitz_deals.html";
        File folderrr = new File(website1_Path);
        // parseing the website
        return parse_CarRental_Website(website1_Path);
    }
   
    
    //calling parsing website methd

    private static List<Car_info> parse_CarRental_Website(String filee_Ppath) {
        List<Car_info> car_Innfo_Llist = new ArrayList<>();

        try {
       
            File inpt = new File(filee_Ppath);
            Document doct = Jsoup.parse(inpt, "UTF-8");

        
            Elements car_Eles = doct.select("li.offer-card-desktop");

            // for loop for all elements
            for (Element car_Element : car_Eles) {


                String car_Name = car_Element.select("div.uitk-text.uitk-type-300.uitk-text-default-theme.uitk-spacing.uitk-spacing-margin-blockend-one").text().split(" or ")[0]
                        .trim();
                if (car_Name.contains("Economy Special") || car_Name.contains("Special")) {
                    continue;
                }
//               // car price
                double car_Price = Double.parseDouble(car_Element.select("span.total-price").first().text().replaceAll("[^\\d.]", ""));
//                
                int passenger_Capacity = Integer.parseInt(fet(car_Element.select("span.uitk-spacing.text-attribute.uitk-spacing-padding-inlinestart-two.uitk-spacing-padding-inlineend-three").first().text()));
                String car_Group = car_Element.select("h3.uitk-heading.uitk-heading-5.uitk-spacing.uitk-spacing-padding-inline-three.uitk-layout-grid-item").text();
//           
                String transmission_Type = car_Element.select("span.uitk-spacing.text-attribute.uitk-spacing-padding-inlinestart-two.uitk-spacing-padding-inlineend-three").text().split(" ")[1];

               // give value
                int large_Bag = new Random().nextInt(6) + 1;
                int small_Bag = new Random().nextInt(6) + 1;


          
                Car_info carInfo = new Car_info(car_Name, car_Price, passenger_Capacity, car_Group, transmission_Type, large_Bag, small_Bag, "CarRental");
                // add record
                car_Innfo_Llist.add(carInfo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return car_Innfo_Llist;
    }
// pattern matter based on string
    public static String fet(String st) {
        Pattern pt = Pattern.compile("\\d+");
        Matcher mt = pt.matcher(st);
        if (mt.find()) {
            return mt.group();
        }
        return st;
    }

}
