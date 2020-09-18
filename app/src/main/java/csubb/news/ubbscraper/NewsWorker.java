package csubb.news.ubbscraper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import csubb.news.ubbscraper.models.NewsObject;
import csubb.news.ubbscraper.models.Subject;
import csubb.news.ubbscraper.repository.NewsDatabase;
import csubb.news.ubbscraper.repository.SubjectDatabase;

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
        boolean messaged = false;
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
                    messaged = true;
                    SubjectDatabase.newInstance(getApplicationContext()).getDao().delete(SubjectDatabase.newInstance(getApplicationContext()).getDao().get(obj.getId()));
                    SubjectDatabase.newInstance(getApplicationContext()).getDao().insert(new Subject(obj.getSubj_name(), obj.getProf_name(), obj.getUrl(), hash, obj.getColor()));
                    NewsDatabase.getInstance(getApplicationContext()).getDao().insert(new NewsObject(obj.getProf_name(), obj.getSubj_name(), Utils.getDateToString(), cop, obj.getColor(), obj.getUrl()));
                }
            }
            catch(Exception e){
                return Result.failure();
            }
        }
        if(!messaged){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Niciun anunt nou", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return Result.success();
    }
}
