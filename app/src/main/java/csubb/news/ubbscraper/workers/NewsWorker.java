package csubb.news.ubbscraper.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import csubb.news.ubbscraper.utils.Utils;
import csubb.news.ubbscraper.models.NewsObject;
import csubb.news.ubbscraper.models.Subject;
import csubb.news.ubbscraper.repository.NewsDatabase;
import csubb.news.ubbscraper.repository.SubjectDatabase;

import java.util.ArrayList;
import java.util.List;

public class NewsWorker extends AbstractWorker {

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

    @NonNull
    @Override
    public Result doWork() {
        List<Subject> all = SubjectDatabase.newInstance(getApplicationContext()).getDao().getAll();
        messaged = false;
        for(Subject obj: all){
            try {
                ArrayList<String> hash = retrieveNewTexts(obj.getUrl());
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
            throwToast(getApplicationContext(), "Niciun anunt nou");
        }
        return Result.success();
    }
}
