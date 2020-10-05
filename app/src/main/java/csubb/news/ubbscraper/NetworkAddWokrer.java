package csubb.news.ubbscraper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import csubb.news.ubbscraper.models.Subject;
import csubb.news.ubbscraper.repository.SubjectDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class NetworkAddWokrer extends Worker {
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
            ArrayList<String> hash = new ArrayList<>();
            Document el = Jsoup.connect(url).get();
            Elements in = el.body().getAllElements();
            for (Element x : in) {
                if (x.childrenSize() < 1)
                    hash.add(x.text());
            }
            SubjectDatabase.newInstance(getApplicationContext()).getDao().insert(new Subject(subj, prof, url, hash, color));

            return Result.success();

        }
        catch(Exception e){
            Utils.throwToast(getApplicationContext(), "Conexiune esuata. Linkul poate fi incorect");
            return Result.failure();
        }
    }
}
