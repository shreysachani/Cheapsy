package com.example.demo;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

public class Ram 
{
	public static void main(String[] args) {
       
		
		validate_Date("18/04/2024");
    }
	static Pattern PATTERN_FOR_DATE = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
	// Pattern PATTERN_FOR_DATE = Pattern.compile("^(?:(?:0[1-9]|1\\d|2[0-8])\\s(0[1-9]|1[0-2])|(29|30)\\s(0[13-9]|1[0-2])|31\\s(0[13578]|1[02]))\\s\\d{4}$");

	public static boolean isValidDate(String dateStr)
	{
        String[] parts = dateStr.split("/");

        if (parts.length != 3) {
            return false;
        }

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        if (month < 1 || month > 12) {
            return false;
        }

        if (day < 1 || day > 31) {
            return false;
        }

        if (month == 2 && day > 29) {
            return false;
        }

        if (!isLeapYear(year) && month == 2 && day == 29) {
            return false;
        }

        if ((day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) || day > 30) {
            return false;
        }

        return true;
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    public static boolean is_Previous_Date(String input_Date) {
    	
    	 String[] rt=input_Date.toString().split("/");

    	 LocalDate date1 = LocalDate.of(Integer.parseInt(rt[2]), Integer.parseInt(rt[1]), Integer.parseInt(rt[0]));
    	
    	   Date date = new Date();
			

		    
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        String formattedDate = sdf.format(date);
	        String[] rtp=formattedDate.toString().split("/");

	        LocalDate date2 = LocalDate.of(Integer.parseInt(rtp[2]), Integer.parseInt(rtp[1]), Integer.parseInt(rtp[0]));
	    	
         // Compare the dates
         int comparison = date1.compareTo(date2);
         if(comparison<0)

             return true;
         else
         {
        	 return false;
         }
          
           
       } 
	public static boolean validate_Date(String input_Date)
    {
    	boolean checkk_ptrn=false;
    	checkk_ptrn = PATTERN_FOR_DATE.matcher(input_Date).matches();
        
        if (!checkk_ptrn)
         {
                  System.out.println("date format is invalid. Please use the format dd/mm/yy. Kindly try again.");
              
         }
       String[] rt=input_Date.toString().split("/");
       
       if(!isValidDate(input_Date))
       {
    	   System.out.println("The date is a not valid");
           checkk_ptrn =false;
    	   
       }
    	
        if (is_Previous_Date(input_Date)) {
            System.out.println("The date is a previous date.");
            checkk_ptrn =false;
        } 
        
        System.out.println(checkk_ptrn);
             return checkk_ptrn;
        
      
    }
}