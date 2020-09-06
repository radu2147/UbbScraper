package com.example.ubbscraper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {
    public static String createNewsMessage(ArrayList<String> ex){
        String message = "";
        for(String el: ex){
            if(message.length() + 2 + el.length() < 40){
                message += el;
                message += ". ";
            }
            else{
                int contor = 0;
                while(message.length() < 37){
                    message += el.charAt(contor);
                    contor ++;
                }
                message += "...";
                break;
            }
        }
        return message;
    }

    public static String getDateToString(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
    private static String getMeYesterday(){
        Date yesterday = new Date(System.currentTimeMillis()-24*60*60*1000);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(yesterday);

    }

    public static String dateFormat(String date){
         String x = date.equals(getDateToString()) ? "Astazi" : date;
         return x.equals(getMeYesterday()) ? "Ieri" : x;
    }
}
