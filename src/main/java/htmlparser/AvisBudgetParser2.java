package htmlparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Car_info;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import org.jsoup.select.*;

import java.io.*;

import java.util.*;
import java.util.regex.*;

// html parser for both avis and budget parser
public class AvisBudgetParser2 {
	// entry point

    public static void main(String[] args) {

        List<Car_info> combined_CarInfo_List = new ArrayList<>();

        String[] folder_Paths = {"AvisFiles/", "BudgetFiles/", "CarRentalFiles/"};

        for (String folder_Path : folder_Paths) {
         
            File folderr = new File(folder_Path);

          
            if (folderr.isDirectory()) {
          
                File[] filess = folderr.listFiles();

                if (filess != null) {
                    for (File filee : filess) {
//              
                        combined_CarInfo_List.addAll(parse_CarRental_Website(filee.getAbsolutePath()));
                    }
                } else {
                    System.out.println("The folder is empty.");
                }
            } else {
                System.out.println("The specified path is not a directory.");
            }
        }
        // saving info
        save_CarInfo_To_Json(combined_CarInfo_List, "filtered_car_deals");

    }
// calling parsefiles
    public static List<Car_info> parse_Files() {
    	// creating a list
        List<Car_info> combined_CarInfo_List = new ArrayList<>();
 // creating folders
        String[] folder_Paths = {"AvisFiles/", "BudgetFiles/"};

        for (String folder_Path : folder_Paths) {
       
            File folderr = new File(folder_Path);

       
            if (folderr.isDirectory()) {
             
                File[] filess = folderr.listFiles();

                if (filess != null) {
                    for (File filee : filess) {
//                 
                    	// saving all data to file
                        combined_CarInfo_List.addAll(parse_CarRental_Website(filee.getAbsolutePath()));
//                        combinedCarInfoList.addAll(CarRentalParser.fetchAllCarRentalDeals());
                    }
                } else {
                    System.out.println("The folder is empty.");
                }
            } else {
                System.out.println("The specified path is not a directory.");
            }
        }
//        saveCarInfoToJson(combinedCarInfoList, "filtered_car_deals");
        return combined_CarInfo_List;
    }
   // parsing car renatl website
    private static List<Car_info> parse_CarRental_Website(String file_Path) {
    	// creating arraylist
        List<Car_info> car_Info_List = new ArrayList<>();

        String rental_Company = "";
        // Parse local HTML file
        if (file_Path.toLowerCase().contains("avis")) {
            rental_Company = "avis";
        } else if (file_Path.toLowerCase().contains("budget")) {
            rental_Company = "budget";
        }
        try {
            // Parse local HTML file
            File inputt = new File(file_Path);
            // parsing the website
            Document documentt = Jsoup.parse(inputt, "UTF-8");

            // Example: Extract car name, price, passenger capacity, etc.
            Elements car_Elements = documentt.select(".step2dtl-avilablecar-section");


            for (Element car_Element : car_Elements) {
            	

            	System.out.println(car_Element);
                String car_Name = car_Element.select("p.featurecartxt.similar-car").text().split(" or")[0];
                
                if (car_Name.contains("Mystery Car")) {
                    continue;
                }

                double car_Price = Double.parseDouble(car_Element.select("p.payamntp").text().replaceAll("[^0-9.]", ""));
//         
                int passenger_Capacity = Integer.parseInt(fetch_Int(car_Element.select("span.four-seats-feat").first().text()));
                String car_Group = car_Element.select("h3[ng-bind='car.carGroup']").text();
                String transmission_Type = car_Element.select("span.four-automatic-feat").text();
                int large_Bag = Integer.parseInt(fetch_Int(car_Element.select("span.four-bags-feat").first().text()));
                int small_Bag = Integer.parseInt(fetch_Int(car_Element.select("span.four-bags-feat-small").first().text()));


                // create car info 
                Car_info car_Info = new Car_info(car_Name, car_Price, passenger_Capacity, car_Group, transmission_Type, large_Bag, small_Bag, rental_Company);
                // add data
                car_Info_List.add(car_Info);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return car_Info_List;
    }

    public static String fetch_Int(String stringg) {
        Pattern patternn = Pattern.compile("\\d+");
        Matcher matcherr = patternn.matcher(stringg);
        if (matcherr.find()) {
            return matcherr.group();
        }
        return stringg;
    }


    private static void save_CarInfo_To_Json(List<Car_info> car_Info_List, String file_name) {
        ObjectMapper object_Mapper = new ObjectMapper();
        String directory_Path = "JsonData/";

        try {
            File directoryy = new File(directory_Path);

            // Create the directory if it doesn't exist
            if (!directoryy.exists()) {
                directoryy.mkdirs();
            }

            // Create the file in the specified directory with the provided filename
            File filee = new File(directoryy, file_name + ".json");

            System.out.println(car_Info_List);
            // Write carInfoList to JSON file
            try {
                object_Mapper.writeValue(filee, car_Info_List);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//            System.out.println("Filtered car deals saved to: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}