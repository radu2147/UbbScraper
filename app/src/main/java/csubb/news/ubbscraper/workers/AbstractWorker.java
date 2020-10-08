package csubb.news.ubbscraper.workers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public abstract class AbstractWorker extends Worker {

    public AbstractWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /*
    retrieves the new html texts from the new url
     */
    protected ArrayList<String> retrieveNewTexts(String url) throws Exception{
        Document el = Jsoup.connect(url).get();
        Elements in = el.body().getAllElements();
        ArrayList<String> hash = new ArrayList<>();
        for (Element x : in) {
            if (x.childrenSize() < 1)
                hash.add(x.text());
        }
        return hash;
    }


    protected void throwToast(final Context context, final String message){
        new Handler(Looper. getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
