package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import features.DataValidation;
import features.FrequencyCount;
import features.PageRanking;
import features.SearchFrequency;
import features.SpellChecking;
import features.WordCompletion;
import htmlparser.AvisParser;
import htmlparser.BudgetParser;
import htmlparser.CarRentalParser;
import model.Car_info;
import webcrawling.AvisCanadaCrawl;
import webcrawling.BudgetCanadaCrawl;
import webcrawling.CarRentalWebCrawl;


public class Entry {

	// intailize gloabal scanner
   static  Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {


        

        while (true) 
        {
            System.out.println("Select an option:");
            System.out.println("1. Perform Crawling");
            System.out.println("2. Perform Parsing");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    performCrawling(); // to perfrom webcrawl from 3 websites
                    break;

                case 2:
                    if (checkHtmlFiles()) 
                    {
                        List<Car_info> carInfoList = performParsing();
                        filter_Car_Deals(carInfoList);
                    } else {
                        System.out.println("No HTML files available for parsing.");
                    }
                    break;

                case 3:
                    System.out.println("Exiting program. Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please select again.");
            }
        }
    }
// checking file in avis folder
    private static boolean files_Avis() {
        File avis_Fr = new File("AvisFiles");

        if (avis_Fr.exists() && avis_Fr.isDirectory()) {
            File[] fss = avis_Fr.listFiles();

            if (fss != null) {
                for (File fie : fss) {
                    if (fie.isFile() && fie.getName().endsWith(".html")) {
                        return true; 
                    }
                }
            }
        }

        return false; // No HTML files found in the "AvisFiles" folder
    }
 // checking file in budget folder
    private static boolean files_InBudget() {
        File budget_Fr = new File("BudgetFiles");

        if (budget_Fr.exists() && budget_Fr.isDirectory()) {
            File[] fss = budget_Fr.listFiles();

            if (fss != null) {
                for (File fie : fss) {
                    if (fie.isFile() && fie.getName().endsWith(".html")) {
                        return true; // Found at least one HTML file
                    }
                }
            }
        }

        return false;
    }
 // checking file in carrental folder
    private static boolean files_CarRental() {
        File carrentalFr = new File("CarRentalFiles");

        if (carrentalFr.exists() && carrentalFr.isDirectory()) {
            File[] fiss = carrentalFr.listFiles();

            if (fiss != null) {
                for (File fie : fiss) {
                    if (fie.isFile() && fie.getName().endsWith(".html")) {
                        return true; // Found at least one HTML file
                    }
                }
            }
        }

        return false; // No HTML files found in the "AvisFiles" folder
    }

    private static boolean checkHtmlFiles() {
       // checking files for pasring
       
       // if files not present then webcrawl has errors
        if (files_Avis() || files_InBudget() || files_CarRental()) {
            return true;
        } else {
            return false;
        }
       
    }

// printing data
    private static void filter_Car_Deals(List<Car_info> car_Info_List) {
       

        System.out.println("**************************************************");
        System.out.println("*                  PARSING                      *");
        System.out.println("*            CAR DEALS FILTER MENU                *");
        System.out.println("**************************************************");

        String refine_Selection;
        do {
            System.out.println("Do you want to filter the car deals? (y/n): ");
            refine_Selection = scanner.next().toLowerCase();
        } 
        while (!DataValidation.validate_User_Response(refine_Selection));

        List<Car_info> process_Filter = new ArrayList<>();
        while (refine_Selection.equals("y")) {
//            processFilter.addAll(carInfoList);
            process_Filter = car_Info_List;
            process_Filter.sort(Comparator.comparingDouble(Car_info::getPrice));
            String option = "1";

            boolean validInput = false;

            while (!validInput) {
                try {
                    do {
                        System.out.println("\nSelect option to filter the deals:\n1. Display deals by price (LOW - HIGH)\n2. Sort by Car Name\n3. Car Price\n4. Sort by Transmission Type\n5. Sort by Passenger Capacity (HIGH - LOW)\n6. Sort by Luggage Capacity (HIGH - LOW)\n7. Show Car Count Analysis\n8. Exit");
                        option = scanner.next();
                    } while (!DataValidation.validate_Integer(Integer.parseInt(option)));
                    validInput = true;
                }catch (NumberFormatException ex){
                    System.out.println("Invalid input. Please enter a valid response.");
                }
            }
            switch (Integer.parseInt(option)) {
                case 1:
//                  it will print all cars available from 3 websites on that date  u given
                    display_Car_List(process_Filter);
                    break;
                case 2:
                	// get car types u want
                    System.out.println("The available Car Companies:");
                    Set<String> car_List = car_Info_List.stream()
                            .map(ele -> {
                                return ele.getName().split(" ")[0];
                              
                            })
                            .collect(Collectors.toSet());


                    System.out.println(car_List);
//
//                    List<String> most_Searched_Cars = SearchFrequency.display_MostSearched_Cars(car_List);
//
//                    if (!most_Searched_Cars.isEmpty()){
//                        System.out.println("Most Searched Cars:");
//                        for (String car : most_Searched_Cars) {
//                            System.out.println(car);
//                        }
//                    }

                    try {
                    	// for spell checking
                        SpellChecking.initialize_Dictionary("JsonData/filtered_car_deals.json");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    boolean check;
                    String preferred_Car_Name;
                    do {
                       
                            System.out.println("Enter your preferred Car Company from above given list: ");
                            preferred_Car_Name = scanner.next().toLowerCase();
                            
                        

                        check = SpellChecking.check_Spelling(preferred_Car_Name);
                        if (!check) {
                            System.out.println("No such Car exists. Please try again any other from the given list...");
                        }
                    } while (!check);

                    SearchFrequency.increment_Search_Frequency(preferred_Car_Name); // increasing search frequncy by 1 for searched car
                    
                    List<String> most_Searched_Cars = SearchFrequency.display_MostSearched_Cars(car_List);

                    if (!most_Searched_Cars.isEmpty()){
                        System.out.println("Most Searched Cars:");
                        for (String car : most_Searched_Cars) {
                            System.out.println(car);
                        }
                    }
                    process_Filter = filterBy_CarName(car_Info_List, preferred_Car_Name);
                    display_Car_List(process_Filter); // prints list of cars

                    String s;
                    do {
                        System.out.println("Do you want to see Page Rank of websites for the given Car Model (Y/N): ");
                        s = scanner.next();
                    } while (!DataValidation.validate_User_Response(s));

                    if (s.equalsIgnoreCase("y")) {
                        try {
                            PageRanking.show_Ranking(preferred_Car_Name);
                            // in pageranking we use invertedindexing such that we can find words more quickly
//                            
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 3:
                    String preferred_Price_Range;

                    do {
                        System.out.println("Enter preferred price range type (50-200): ");
                        preferred_Price_Range = scanner.next().toLowerCase();
                    } while (!DataValidation.validate_Range_Input(preferred_Price_Range));

                    process_Filter = filterByPriceRange(car_Info_List, preferred_Price_Range);
                    display_Car_List(process_Filter);
                    break;
                case 4:

                    String preferredTransmission;
                    do {
                        System.out.println("Enter preferred transmission type (A or M): ");
                        preferredTransmission = scanner.next().toUpperCase();
                    } while (!DataValidation.validate_TTypes(preferredTransmission));

                    process_Filter = switch (preferredTransmission.toUpperCase()) {
                        case "A" -> filterBy_Transmission(car_Info_List, "Automatic");
                        case "M" -> filterBy_Transmission(car_Info_List, "Manual");
                        default -> process_Filter;
                    };
//                    processFilter = filterByTransmission(carInfoList, preferredTransmission);
                    display_Car_List(process_Filter);

                    break;
                case 5:
                    int preferredPassengerCapacity;
                    do {
                        System.out.println("Enter preferred passenger capacity: ");
                        preferredPassengerCapacity = scanner.nextInt();
                        
                    } while (!DataValidation.validate_Integer(preferredPassengerCapacity));
                    process_Filter = filterBy_Passenger_Capacity(car_Info_List, preferredPassengerCapacity);
                    display_Car_List(process_Filter);

                    break;
                case 6:
                    int preferredLuggageCapacity;
                    do {
                        System.out.println("Enter preferred luggage capacity: ");
                        preferredLuggageCapacity = scanner.nextInt();
                    } 
                    while (!DataValidation.validate_Integer(preferredLuggageCapacity));
                    process_Filter = filterBy_Luggage_Capacity(car_Info_List, preferredLuggageCapacity);
                    display_Car_List(process_Filter);
                    break;
                case 7:
                    fetch_Car_Analysis(car_Info_List); // car count analysis

                    break;
                case 8:
                    refine_Selection = "no";
                    break;
                default:
                    System.out.println("Invalid option. Please enter a valid option.");
            }
        }
    }

    private static void fetch_Car_Analysis(List<Car_info> car_Info_List) {
        Map<String, Integer> frequencyMap = FrequencyCount.get_Frequency_Count("JsonData/filtered_car_deals.json");

        // Print the frequency count
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            System.out.println("Total Available \"" + entry.getKey() + "\" cars: \"" + entry.getValue() + "\"");
        }
    }

    private static List<Car_info> filterByPriceRange(List<Car_info> car_Info_List, String preferred_Price_Range) {
        String[] priceRange = preferred_Price_Range.split("-");

        if (priceRange.length != 2) {
            // Handle invalid price range input
            throw new IllegalArgumentException("Invalid price range format");
        }

        int minPrice = Math.min(Integer.parseInt(priceRange[0].trim()),Integer.parseInt(priceRange[1].trim()));
        int maxPrice = Math.max(Integer.parseInt(priceRange[0].trim()),Integer.parseInt(priceRange[1].trim()));

        return car_Info_List.stream()
                .filter(car -> {
                    try {
                        double carPrice = car.getPrice();
                        return carPrice >= minPrice && carPrice <= maxPrice;
                    } catch (NumberFormatException e) {
                        // Handle invalid price format for a car
                        return false;
                    }
                })
                .sorted(Comparator.comparingDouble(Car_info::getPrice))
                .collect(Collectors.toList());
    }

    private static int get_User_Selection(int maxOption) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select an option (or Enter 0 to select all): ");
        while (!scanner.hasNextInt()) {
            System.out.println("\nInvalid input. Please enter a valid option.");
            scanner.next();
        }
        int selectedOption = scanner.nextInt();
        return selectedOption;
    }

    private static List<Car_info> filterBy_CarName(List<Car_info> car_Info_List, String preferred_CarName) {
        try {
            SpellChecking.initialize_Dictionary("JsonData/filtered_car_deals.json");
            WordCompletion.initialize_Dictionary_From_JsonFile("JsonData/filtered_car_deals.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> suggestions = WordCompletion.get_Suggestions(preferred_CarName.toLowerCase());

        if (!suggestions.isEmpty()) {
            System.out.println("Suggestions:");

            for (int i = 0; i < suggestions.size(); i++) {

                System.out.println((i + 1) + ". " + suggestions.get(i));
            }

         
            int selectedOption = get_User_Selection(suggestions.size());


      
            if (selectedOption >= 1 && selectedOption <= suggestions.size()) {
                preferred_CarName = suggestions.get(selectedOption - 1);
            } else if (selectedOption == 0) {
              
            } else {
                System.out.println("Invalid selection. Using original input.");
            }
        }

       

        String finalPreferredCarName = preferred_CarName;
        return car_Info_List.stream()
                .filter(car -> car.getName().equalsIgnoreCase(finalPreferredCarName) || car.getName().toLowerCase().contains(finalPreferredCarName))
                .sorted(Comparator.comparingDouble(Car_info::getPrice))
                .toList();
    }

    private static List<Car_info> filterBy_Transmission(List<Car_info> car_Info_List, String preferred_Transmission) {
        return car_Info_List.stream()
                .filter(car -> car.getTransmissionType().equalsIgnoreCase(preferred_Transmission))
                .sorted(Comparator.comparingDouble(Car_info::getPrice))
                .toList();
    }

    private static List<Car_info> filterBy_Passenger_Capacity(List<Car_info> car_Info_List, int preferred_Passenger_Capacity)
    {

        Optional<Car_info> maxPassengerCapacity = car_Info_List.stream()
                .max(Comparator.comparingInt(Car_info::getPassengerCapacity));

        if (preferred_Passenger_Capacity > maxPassengerCapacity.get().getPassengerCapacity()) {
            preferred_Passenger_Capacity = maxPassengerCapacity.get().getPassengerCapacity();
        }
        int finalPreferredPassengerCapacity = preferred_Passenger_Capacity;

        return car_Info_List.stream()
                .filter(car -> car.getPassengerCapacity() >= finalPreferredPassengerCapacity)
                .sorted(Comparator.comparingDouble(Car_info::getPrice))
                .toList();
    }

    private static List<Car_info> filterBy_Luggage_Capacity(List<Car_info> car_Info_List, int preferred_Luggage_Capacity) 
    {


        Optional<Car_info> max_Total_Car = car_Info_List.stream()
                .max(Comparator.comparingInt(car -> car.getLargeBag() + car.getSmallBag()));

        if (preferred_Luggage_Capacity > max_Total_Car.get().getLargeBag() + max_Total_Car.get().getSmallBag()) {
            preferred_Luggage_Capacity = max_Total_Car.get().getLargeBag() + max_Total_Car.get().getSmallBag();
        }
        int finalPreferredLuggageCapacity = preferred_Luggage_Capacity;
        return car_Info_List.stream()
                .filter(car -> car.getLargeBag() + car.getSmallBag() >= finalPreferredLuggageCapacity)
                .sorted(Comparator.comparingDouble(Car_info::getPrice))
                .toList();
    }

    private static void display_Car_List(List<Car_info> car_Info_List) {
      
        System.out.println("+-------------------------+----------------------------------------+-------------------+------------------------+------------------------+--------------------------+--------------------+");
        System.out.println("|      Car Group          |          Car Model                     |    Rent Price     |   Passenger Capacity   |    Luggage Capacity    |     Transmission Type    |    Rental Company  |");
        System.out.println("+-------------------------+----------------------------------------+-------------------+------------------------+------------------------+--------------------------+--------------------+");

     
        for (Car_info car_Info : car_Info_List) {
            System.out.printf("| %-23s | %-38s | $%-16.2f | %-22s | %-22s | %-24s | %-18s |\n",
                    car_Info.getCarGroup(), car_Info.getName(), car_Info.getPrice(),
                    car_Info.getPassengerCapacity(), car_Info.getLargeBag() + car_Info.getSmallBag(),
                    car_Info.getTransmissionType(), car_Info.getRentalCompany());
        }


      
        System.out.println("+-------------------------+----------------------------------------+-------------------+------------------------+------------------------+--------------------------+--------------------+");
    }

    private static void save_CarInfo_To_Json(List<Car_info> car_Infoo_Llist, String file_name) {
        ObjectMapper obj_Mapper = new ObjectMapper();  
        String dir_Path = "JsonData/";

        try {
            File diry = new File(dir_Path);
            
// making dir if not present
            if (!diry.exists()) {
                diry.mkdirs();
            }

           
            File filee = new File(diry, file_name + ".json");

//           writing to json format
            try {
                obj_Mapper.writeValue(filee, car_Infoo_Llist);
               
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Car_info> performParsing() {
        List<Car_info> listt = new ArrayList<>();
        
        listt.addAll(CarRentalParser.fetchAll_CarRental_Deals());
        
        listt.addAll(AvisParser.parse_Files());
        listt.addAll(BudgetParser.parse_Files());
       
        
        save_CarInfo_To_Json(listt, "filtered_car_deals");

        return listt;
    }

    @SuppressWarnings("deprecation")
	private static void performCrawling() {
    	// getting connections with 3 sites
        AvisCanadaCrawl.init_Driver();
        System.out.println("avis");
        BudgetCanadaCrawl.init_Driver();
        System.out.println("budget");
        CarRentalWebCrawl.initDriver();
        System.out.println("carrental");

        Scanner scanner = new Scanner(System.in);
        String response;
        do {
          // confirming taking and return loc is same
            String same_Location_Response;
            do {
                System.out.print("Are pickup and drop-off locations the same? (y/n): ");
                same_Location_Response = scanner.nextLine().toLowerCase();
            } while (!DataValidation.validate_User_Response(same_Location_Response));

            // loc for taking car
            String pickup_Location;
            do {
                System.out.print("Enter pickup location: ");
                pickup_Location = scanner.nextLine();
            } while (!DataValidation.validate_City_Name(pickup_Location));

            String final_Selected_PickupLoc = AvisCanadaCrawl.resolve_Location(pickup_Location, "PicLoc_value", "PicLoc_dropdown");
            BudgetCanadaCrawl.resolve_Location(final_Selected_PickupLoc.split(",")[0], "PicLoc_value", "PicLoc_dropdown");
            CarRentalWebCrawl.handle_PickUp_Location(final_Selected_PickupLoc);
            CarRentalWebCrawl.handle_PickUp_Location(final_Selected_PickupLoc.split(",")[0]);
            BudgetCanadaCrawl.resolve_Location(pickup_Location,"PicLoc_value", "PicLoc_dropdown");

//            if locations are different
            String dropOff_Location;
            do {
                dropOff_Location = same_Location_Response.equals("n") ? get_DropOff_Location(scanner) : pickup_Location;
            } while (!DataValidation.validate_City_Name(dropOff_Location));

            if (same_Location_Response.equals("n")){
                String finalSelectedDropOffLoc = AvisCanadaCrawl.resolve_Location(dropOff_Location, "DropLoc_value", "DropLoc_dropdown");
                BudgetCanadaCrawl.resolve_Location(finalSelectedDropOffLoc, "DropLoc_value", "DropLoc_dropdown");
                CarRentalWebCrawl.handle_DropOff_Location(finalSelectedDropOffLoc);
            }


            // Get pickup date
            String pickup_Date;
            do {
                System.out.print("Enter pickup date (DD/MM/YYYY): ");
                pickup_Date = scanner.nextLine();
            } while (!DataValidation.validate_Date(pickup_Date));

            // Get drop-off date
            String return_Date;
            do {
                System.out.print("Enter return date (DD/MM/YYYY): ");
                return_Date = scanner.nextLine();
              
            
            } while (!DataValidation.validate_returnDate(pickup_Date,return_Date));

            CarRentalWebCrawl.resolve_Date(pickup_Date, return_Date);

            pickup_Date = convert_Date_Format(pickup_Date);
            return_Date = convert_Date_Format(return_Date);

            AvisCanadaCrawl.resolve_Date(pickup_Date, return_Date);
            BudgetCanadaCrawl.resolve_Date(pickup_Date, return_Date);

//            System.out.println("Do you have a specific time in mind to pick and return the car: ");

            String pickup_Time;
            do {
            
              
				
				
				  System.out.print("Enter pickup time (HH:MM AM/PM): ");
				   pickup_Time = scanner.nextLine();
			        String[] split_Time = pickup_Time.split(":");
			        int hourr = Integer.parseInt(split_Time[0]);
			        int minutee = Integer.parseInt(split_Time[1].substring(0, 2));
			        boolean isPM = split_Time[1].substring(2).trim().equalsIgnoreCase("PM");
			        
			      
			        //int time_InMinutes = hourr * 60 + minutee;
			        if(isPM)
			        {
			        	if(hourr!=12)
			        	     hourr +=12;
			        }
			        LocalTime Time = LocalTime.of(hourr,minutee,00);

			        
			        LocalTime currentTime = LocalTime.now();
			      
			       Date date1 = new Date(pickup_Date);
					Date date2 = new Date();
					

			    
			        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			        // Format the current date using the defined format
			        String datep = sdf.format(date1);
			        String formattedDate = sdf.format(date2);

			       
			        String[] rt=datep.toString().split("/");
			        String[] rtp=formattedDate.toString().split("/");
			        
			        
			        if(rtp[0].compareTo(rt[0]) == 0)
			        {
			        	if(rtp[1].compareTo(rt[1]) == 0)
			    		{
			    			System.out.println("enter");
			           
			            if (Time.compareTo(currentTime)<0) {
			            	System.out.println("Enter TIME GRETER THAN CURRENT TIME as u give present date ");
			            	 System.out.print("Enter pickup time (HH:MM AM/PM): ");
			            	 pickup_Time = scanner.nextLine();
			            	 
			                
			              }
			    		}
			        	
			        }
					
            }
				while (!DataValidation.validate_Time(pickup_Time));

            // enter  return  date
            String return_Time;
            do {
                System.out.print("Enter return time (HH:MM AM/PM): ");
                return_Time = scanner.nextLine();

                String[] split_Time = return_Time.split(":");
                int hourr = Integer.parseInt(split_Time[0]);
                int minutee = Integer.parseInt(split_Time[1].substring(0, 2));
                boolean isPM = split_Time[1].substring(2).trim().equalsIgnoreCase("PM");
                
              
                int time_InMinutes = hourr * 60 + minutee;
                if(isPM)
                {
                	if(hourr!=12)
                	     time_InMinutes = hourr * 60 + minutee+720;
                }
                String[] split_Time1 = pickup_Time.split(":");
                int hourr1 = Integer.parseInt(split_Time1[0]);
                int minutee1 = Integer.parseInt(split_Time1[1].substring(0, 2));
                boolean isPM1 = split_Time1[1].substring(2).trim().equalsIgnoreCase("PM");
                
              
                int time_InMinutes1 = hourr1 * 60 + minutee1;
                if(isPM1)
                {
                	if(hourr1!=12)
                	    time_InMinutes1 = hourr1 * 60 + minutee1+720;
                }
                
                
		        String[] rt=pickup_Date.toString().split("/");
		        String[] rtp=return_Date.toString().split("/");
				
                if(rtp[0].compareTo(rt[0]) == 0)
		        {
		        	if(rtp[1].compareTo(rt[1]) == 0)
		    		{
                if (time_InMinutes<time_InMinutes1) {
                	System.out.println("give difference of 3 hours after the pick time  and should be greater than pick up time ");
                	 System.out.print("Enter return time (HH:MM AM/PM): ");
                     return_Time = scanner.nextLine();
                  }
		    		}
          
				}
              
            } while (!DataValidation.validate_Time(return_Time));
             
            
            try {
                AvisCanadaCrawl.resolve_Time(pickup_Time, return_Time);
                BudgetCanadaCrawl.resolve_Time(pickup_Time, return_Time);
                CarRentalWebCrawl.resolve_Time(pickup_Time, return_Time);
                AvisCanadaCrawl.fetch_Car_Deals();
                

               // CarRentalWebCrawl.fetch_Car_Deals();
                BudgetCanadaCrawl.fetch_Car_Deals();
            }catch (Exception ex){
            	System.out.println(" exceptionncatched no cars availble in that location try again!"); 

            }

             

            // Ask if the user wants to continue
            System.out.print("Do you want to continue? (yes/no): ");
            response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                AvisCanadaCrawl.reset_Driver();
                BudgetCanadaCrawl.reset_Driver();
                CarRentalWebCrawl.reset_Driver();
            }
        } while (response.equalsIgnoreCase("yes"));

        AvisCanadaCrawl.close_Driver();
        BudgetCanadaCrawl.close_Driver();
        CarRentalWebCrawl.close_Driver();
    }

    public static String convert_Date_Format(String inputtDdate) {
        SimpleDateFormat original_Format = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat target_Format = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date datee = original_Format.parse(inputtDdate);
            return target_Format.format(datee);
        } catch (ParseException e) {
            e.printStackTrace(); 
            return null; 
        }
    }

    private static String get_DropOff_Location(Scanner scanner) {
        System.out.print("Enter drop-off location: ");
        return scanner.nextLine();
    }
}