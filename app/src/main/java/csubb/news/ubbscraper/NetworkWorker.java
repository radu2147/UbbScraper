package csubb.news.ubbscraper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import csubb.news.ubbscraper.models.Subject;
import csubb.news.ubbscraper.repository.SubjectDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class NetworkWorker extends Worker {
    public NetworkWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /*
    Updates the database with new data without changing the existing html text
     */
    private void updateWithoutUrl(int id, String subj, String prof, String url, int color){
        ArrayList<String> aux = SubjectDatabase.newInstance(getApplicationContext()).getDao().get(id).getHtml_texts();
        SubjectDatabase.newInstance(getApplicationContext()).getDao().delete(SubjectDatabase.newInstance(getApplicationContext()).getDao().get(id));
        SubjectDatabase.newInstance(getApplicationContext()).getDao().insert(new Subject(subj, prof, url, aux, color));
    }

    /*
    retrieves the new html texts from the new url
     */
    private ArrayList<String> retrieveNewTexts(String url) throws Exception{
        Document el = Jsoup.connect(url).get();
        Elements in = el.body().getAllElements();
        ArrayList<String> hash = new ArrayList<>();
        for (Element x : in) {
            if (x.childrenSize() < 1)
                hash.add(x.text());
        }
        return hash;
    }

    /*
    Updates the new db with the new data from web crawling
     */
    private void updateDatabaseWithUrl(int id, String subj, String prof, String url, int color, ArrayList<String> hash){
        SubjectDatabase.newInstance(getApplicationContext()).getDao().delete(SubjectDatabase.newInstance(getApplicationContext()).getDao().get(id));
        SubjectDatabase.newInstance(getApplicationContext()).getDao().insert(new Subject(subj, prof, url, hash, color));
    }


    @NonNull
    @Override
    public Result doWork() {
        //initialisation from inputdata
        int id = getInputData().getInt("Id", 0);
        String url = getInputData().getString("Url");
        String subj = getInputData().getString("Subject");
        String prof = getInputData().getString("Prof name");
        int color = getInputData().getInt("Color", R.color.colorPrimary);

        if(getInputData().getBoolean("isUrlChanged", true)) {
            try {

                ArrayList<String> hash = retrieveNewTexts(url);
                updateDatabaseWithUrl(id, subj, prof, url, color, hash);

                return Result.success();

            } catch (Exception e) {
                Utils.throwToast(getApplicationContext(), "Conexiune esuata. Linkul poate fi incorect");
                return Result.failure();
            }
        }
        else{
            updateWithoutUrl(id, subj, prof, url, color);
            return Result.success();
        }
    }
}
