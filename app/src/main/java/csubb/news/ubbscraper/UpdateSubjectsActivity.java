package csubb.news.ubbscraper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.Executors;

import csubb.news.ubbscraper.databinding.ActivityUpdateSubjectsBinding;
import csubb.news.ubbscraper.models.Subject;
import csubb.news.ubbscraper.repository.SubjectDatabase;

public class UpdateSubjectsActivity extends AppCompatActivity {

    private int position;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.update){
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    Log.d("OBJECT", String.valueOf(position));
                    Subject obj = SubjectDatabase.newInstance(getApplicationContext()).getDao().get(position);
                    Log.d("OBJECT", String.valueOf(obj));
                    SubjectDatabase.newInstance(getApplicationContext()).getDao().delete(obj);
                }
            });
            Intent trans = new Intent(UpdateSubjectsActivity.this, MainActivity.class);
            startActivity(trans);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_subjects);

        if(getIntent().hasExtra("Id")){
            position = getIntent().getIntExtra("Id", 0);
        }

        final ActivityUpdateSubjectsBinding obj = DataBindingUtil.setContentView(this, R.layout.activity_update_subjects);

        obj.textView.setText(R.string.update_text);
        obj.addButton.setText("Salveaza");
        obj.messageForUser.setText(R.string.update_operation);
        obj.prof.setText(getIntent().getStringExtra("Profesor"));
        obj.subject.setText(getIntent().getStringExtra("Subject"));
        obj.editTextTextPersonName3.setText(getIntent().getStringExtra("Url"));
        obj.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isUrlOk(obj.editTextTextPersonName3.getText().toString()))
                    Toast.makeText(getApplicationContext(), "Url invalid", Toast.LENGTH_SHORT).show();
                else {
                    Constraints con = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                            .build();
                    Data.Builder data = new Data.Builder().putInt("Id", getIntent().getIntExtra("Id", 0))
                            .putString("Url", obj.editTextTextPersonName3.getText().toString())
                            .putString("Subject", obj.subject.getText().toString())
                            .putString("Prof name", obj.prof.getText().toString())
                            .putInt("Color", Utils.getColors()[Math.abs(obj.editTextTextPersonName3.getText().toString().hashCode()) % Utils.getColors().length]);
                    OneTimeWorkRequest.Builder one = new OneTimeWorkRequest.Builder(NetworkWorker.class);
                    if(!obj.editTextTextPersonName3.getText().toString().equals(getIntent().getStringExtra("Url"))) {
                        one.setConstraints(con);
                        data.putBoolean("isUrlChanged", true);
                    }
                    else{
                        data.putBoolean("isUrlChanged", false);
                    }

                    one.setInputData(data.build());
                    WorkManager man = WorkManager.getInstance(getApplicationContext());
                    man.enqueue(one.build());

                    Intent trans = new Intent(UpdateSubjectsActivity.this, MainActivity.class);
                    startActivity(trans);
                }
            }
        });
    }
}