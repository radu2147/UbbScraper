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

    private boolean messaged;

    public NewsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /*
    Updates the subjectdatabase with the new texts from the web page and insert a news object in the db
     */
    private void updateDatabase(Subject obj, ArrayList<String> hash, ArrayList<String> cop){
        messaged = true;
        SubjectDatabase.newInstance(getApplicationContext()).getDao().delete(SubjectDatabase.newInstance(getApplicationContext()).getDao().get(obj.getId()));
        SubjectDatabase.newInstance(getApplicationContext()).getDao().insert(new Subject(obj.getSubj_name(), obj.getProf_name(), obj.getUrl(), hash, obj.getColor()));
        NewsDatabase.getInstance(getApplicationContext()).getDao().insert(new NewsObject(obj.getProf_name(), obj.getSubj_name(), Utils.getDateToString(), cop, obj.getColor(), obj.getUrl()));

    }


    /*
    Crawls the web page to get the new updated texts
     */
    private ArrayList<String> getUpdatedTexts(Elements in){
        ArrayList<String> hash = new ArrayList<>();
        for (Element x : in) {
            // if this happens we know the text will not repeat
            if (x.childrenSize() < 1)
                hash.add(x.text());
        }
        return hash;
    }



    @NonNull
    @Override
    public Result doWork() {
        List<Subject> all = SubjectDatabase.newInstance(getApplicationContext()).getDao().getAll();
        messaged = false;
        for(Subject obj: all){
            try {
                Document el = Jsoup.connect(obj.getUrl()).get();

                ArrayList<String> hash = getUpdatedTexts(el.body().getAllElements());
                ArrayList<String> cop = (ArrayList<String>)hash.clone();
                cop.removeAll(obj.getHtml_texts());

                if(!cop.isEmpty()){
                    updateDatabase(obj, hash, cop);
                }
            }
            catch(Exception e){
                return Result.failure();
            }
        }
        if(!messaged){
            Utils.throwToast(getApplicationContext(), "Niciun anunt nou");
        }
        return Result.success();
    }
}
