package csubb.news.ubbscraper;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {


    public static int[] colors;

    public static int[] getColors(){
        return colors;
    }

    public static void setColors(){
        colors = new int[10];
        colors[0] = Color.RED;
        colors[1] = R.color.colorPrimary;
        colors[2] = Color.MAGENTA;
        colors[3] = Color.GREEN;
        colors[4] = Color.GRAY;
        colors[5] = R.color.white_backgorund_color1;
        colors[6] = R.color.white_backgorund_color2;
        colors[7] = R.color.white_backgorund_color3;
        colors[8] = R.color.white_backgorund_color4;
        colors[9] = R.color.white_backgorund_color5;
    }

    public static String createNewsMessage(ArrayList<String> ex){
        String message = "";
        for(String el: ex){
            if(message.length() + 2 + el.length() < 31){
                message += el;
                message += ". ";
            }
            else{
                int contor = 0;
                while(message.length() < 28){
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

    public static boolean isUrlOk(String url){
        try{
            new URL(url).toURI();
            return false;
        }
        catch (Exception e){
            return true;
        }
    }

    public static void throwToast(final Context context, final String message){
        new Handler(Looper. getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
