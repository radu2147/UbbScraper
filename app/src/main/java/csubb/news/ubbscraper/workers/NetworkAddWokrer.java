package csubb.news.ubbscraper.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import csubb.news.ubbscraper.R;
import csubb.news.ubbscraper.models.Subject;
import csubb.news.ubbscraper.repository.SubjectDatabase;

import java.util.ArrayList;

public class NetworkAddWokrer extends AbstractWorker {
    public NetworkAddWokrer(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String url = getInputData().getString("Url");
        String subj = getInputData().getString("Subject");
        String prof = getInputData().getString("Prof name");
        int color = getInputData().getInt("Color", R.color.colorPrimary);

        try {
            ArrayList<String> hash = retrieveNewTexts(url);
            SubjectDatabase.newInstance(getApplicationContext()).getDao().insert(new Subject(subj, prof, url, hash, color));

            return Result.success();

        }
        catch(Exception e){
            throwToast(getApplicationContext(), "Conexiune esuata. Linkul poate fi incorect");
            return Result.failure();
        }
    }
}
