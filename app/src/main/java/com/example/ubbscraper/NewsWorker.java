package com.example.ubbscraper;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ubbscraper.models.NewsObject;
import com.example.ubbscraper.models.Subject;
import com.example.ubbscraper.repository.NewsDatabase;
import com.example.ubbscraper.repository.SubjectDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class NewsWorker extends Worker {
    public NewsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        List<Subject> all = SubjectDatabase.newInstance(getApplicationContext()).getDao().getAll();
        for(Subject obj: all){
            try {
                ArrayList<String> hash = new ArrayList<>();
                Document el = Jsoup.connect(obj.getUrl()).get();
                Elements in = el.body().getAllElements();
                for (Element x : in) {
                    if (x.childrenSize() < 1)
                        hash.add(x.text());
                }
                ArrayList<String> cop;
                cop = (ArrayList<String>)hash.clone();
                cop.removeAll(obj.getHtml_texts());
                if(!cop.isEmpty()){
                    SubjectDatabase.newInstance(getApplicationContext()).getDao().delete(SubjectDatabase.newInstance(getApplicationContext()).getDao().get(obj.getId()));
                    SubjectDatabase.newInstance(getApplicationContext()).getDao().insert(new Subject(obj.getSubj_name(), obj.getProf_name(), obj.getUrl(), hash, obj.getColor()));
                    NewsDatabase.getInstance(getApplicationContext()).getDao().insert(new NewsObject(obj.getProf_name(), obj.getSubj_name(), Utils.getDateToString(), cop, obj.getColor(), obj.getUrl()));
                }
            }
            catch(Exception e){
                Log.d("ERROR", e.toString());
                return Result.failure();
            }
        }

        return Result.success();
    }
}
