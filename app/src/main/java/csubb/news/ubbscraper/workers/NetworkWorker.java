package csubb.news.ubbscraper.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import csubb.news.ubbscraper.R;
import csubb.news.ubbscraper.models.Subject;
import csubb.news.ubbscraper.repository.SubjectDatabase;

import java.util.ArrayList;

public class NetworkWorker extends AbstractWorker {
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
                throwToast(getApplicationContext(), "Conexiune esuata. Linkul poate fi incorect");
                return Result.failure();
            }
        }
        else{
            updateWithoutUrl(id, subj, prof, url, color);
            return Result.success();
        }
    }
}
