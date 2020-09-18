package csubb.news.ubbscraper.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "Subjects")
public class Subject {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String prof_name;
    private String subj_name;
    private String url;
    private ArrayList<String> html_texts;
    private int color;

    public Subject(int id, String subj_name, String prof_name, String url, int color){

        this.id = id;
        this.subj_name = subj_name;
        this.prof_name = prof_name;
        this.url = url;
        this.color = color;
        this.html_texts = new ArrayList<>();
    }

    @Ignore
    public Subject(String subj_name, String prof_name, String url){

        this.subj_name = subj_name;
        this.prof_name = prof_name;
        this.url = url;
        this.html_texts = new ArrayList<>();
    }
    @Ignore
    public Subject(String subj_name, String prof_name, String url, ArrayList<String> html_texts, int color){

        this.subj_name = subj_name;
        this.prof_name = prof_name;
        this.url = url;
        this.html_texts = html_texts;
        this.color = color;
    }



    public String getProf_name() {
        return prof_name;
    }

    public String getSubj_name() {
        return subj_name;
    }

    public String getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getHtml_texts() {
        return html_texts;
    }

    public void setHtml_texts(ArrayList<String> obj){
        html_texts = obj;
    }

    public int getColor() {
        return color;
    }
}
