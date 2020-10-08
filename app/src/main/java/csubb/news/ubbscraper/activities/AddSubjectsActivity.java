package csubb.news.ubbscraper.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import csubb.news.ubbscraper.R;
import csubb.news.ubbscraper.utils.Utils;
import csubb.news.ubbscraper.databinding.ActivityAddSubjectsBinding;
import csubb.news.ubbscraper.workers.NetworkAddWokrer;

public class AddSubjectsActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.about:
                Intent x = new Intent(AddSubjectsActivity.this, AboutActivity.class);
                startActivity(x);
                return true;
        }

        return super.onOptionsItemSelected(item); // important line
    }

    /*
    Creates a worker that adds a new subject to the database and crawls the internet for the html texts in the webpage
     */
    private void createWorker(final ActivityAddSubjectsBinding obj){
        Constraints con = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        Data data = new Data.Builder()
                .putString("Url", obj.editTextTextPersonName3.getText().toString())
                .putString("Subject", obj.subject.getText().toString())
                .putString("Prof name", obj.prof.getText().toString())
                .putInt("Color", Utils.getColors()[Math.abs(obj.editTextTextPersonName3.getText().toString().hashCode()) % Utils.getColors().length])
                .build();
        OneTimeWorkRequest.Builder one = new OneTimeWorkRequest.Builder(NetworkAddWokrer.class)
                .setInputData(data)
                .setConstraints(con);

        WorkManager man = WorkManager.getInstance(getApplicationContext());
        man.enqueue(one.build());
    }

    /*
    Restarts the MainActivity
     */
    private void startMain(){
        Intent trans = new Intent(AddSubjectsActivity.this, MainActivity.class);
        startActivity(trans);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subjects);
        Utils.setColors();

        final ActivityAddSubjectsBinding obj = DataBindingUtil.setContentView(this, R.layout.activity_add_subjects);

            obj.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utils.isUrlOk(obj.editTextTextPersonName3.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Url invalid", Toast.LENGTH_SHORT).show();
                    else {
                        createWorker(obj);
                        startMain();
                    }
                }
            });
        }

}