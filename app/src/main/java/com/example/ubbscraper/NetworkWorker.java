package com.example.ubbscraper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ubbscraper.models.Subject;
import com.example.ubbscraper.repository.SubjectDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class NetworkWorker extends Worker {
    public NetworkWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int id = getInputData().getInt("Id", 0);
        String url = getInputData().getString("Url");
        String subj = getInputData().getString("Subject");
        String prof = getInputData().getString("Prof name");
        int color = getInputData().getInt("Color", R.color.colorPrimary);

        if(getInputData().getBoolean("isUrlChanged", true)) {
            try {
                ArrayList<String> hash = new ArrayList<>();
                Document el = Jsoup.connect(url).get();
                Elements in = el.body().getAllElements();
                for (Element x : in) {
                    if (x.childrenSize() < 1)
                        hash.add(x.text());
                }
                SubjectDatabase.newInstance(getApplicationContext()).getDao().delete(SubjectDatabase.newInstance(getApplicationContext()).getDao().get(id));
                SubjectDatabase.newInstance(getApplicationContext()).getDao().insert(new Subject(subj, prof, url, hash, color));


                return Result.success();

            } catch (Exception e) {
                return Result.failure();
            }
        }
        else{
            ArrayList<String> aux = SubjectDatabase.newInstance(getApplicationContext()).getDao().get(id).getHtml_texts();
            SubjectDatabase.newInstance(getApplicationContext()).getDao().delete(SubjectDatabase.newInstance(getApplicationContext()).getDao().get(id));
            SubjectDatabase.newInstance(getApplicationContext()).getDao().insert(new Subject(subj, prof, url, aux, color));
            return Result.success();
        }
    }
}
