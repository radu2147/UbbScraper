package csubb.news.ubbscraper.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "News")
public class NewsObject {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String prof;
    private String subj_name;
    private String date;
    private ArrayList<String> message;
    private String url;
    private int color;

    @Ignore
    public NewsObject(String prof, String subj_name, String date, ArrayList<String> message, int color, String url) {
        this.prof = prof;
        this.subj_name = subj_name;
        this.date = date;
        this.message = message;
        this.color = color;
        this.url = url;
    }

    public NewsObject(int id, String prof, String subj_name, String date, ArrayList<String> message, int color, String url){
        this.id = id;
        this.prof = prof;
        this.subj_name = subj_name;
        this.date = date;
        this.message = message;
        this.color = color;
        this.url = url;
    }


    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public String getProf() {
        return prof;
    }

    public String getSubj_name() {
        return subj_name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }
}
