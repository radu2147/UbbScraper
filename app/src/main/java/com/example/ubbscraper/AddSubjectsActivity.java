package com.example.ubbscraper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ubbscraper.databinding.ActivityAddSubjectsBinding;
import java.net.URL;
public class AddSubjectsActivity extends AppCompatActivity {

    int[] colors;

    private boolean isUrlOk(String url){
        try{
            new URL(url).toURI();
            return false;
        }
        catch (Exception e){
            return true;
        }
    }

    private void setColors(){
        colors = new int[5];
        colors[0] = Color.RED;
        colors[1] = R.color.colorPrimary;
        colors[2] = Color.MAGENTA;
        colors[3] = Color.GREEN;
        colors[4] = Color.GRAY;
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subjects);
        setColors();

        final ActivityAddSubjectsBinding obj = DataBindingUtil.setContentView(this, R.layout.activity_add_subjects);

        if(getIntent().hasExtra("editable")){
            obj.addButton.setText("Salveaza");
            obj.prof.setText(getIntent().getStringExtra("Profesor"));
            obj.subject.setText(getIntent().getStringExtra("Subject"));
            obj.editTextTextPersonName3.setText(getIntent().getStringExtra("Url"));
            obj.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isUrlOk(obj.editTextTextPersonName3.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Url invalid", Toast.LENGTH_SHORT).show();
                    else {
                        Constraints con = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                                .build();
                        Data data = new Data.Builder().putInt("Id", getIntent().getIntExtra("Id", 0))
                                .putString("Url", obj.editTextTextPersonName3.getText().toString())
                                .putString("Subject", obj.subject.getText().toString())
                                .putString("Prof name", obj.prof.getText().toString())
                                .putInt("Color", colors[(int) (System.currentTimeMillis() % 5)])
                                .build();
                        OneTimeWorkRequest.Builder one = new OneTimeWorkRequest.Builder(NetworkWorker.class)
                                .setInputData(data);
                        if(!obj.editTextTextPersonName3.getText().toString().equals(getIntent().getStringExtra("Url")))
                            one.setConstraints(con);

                        WorkManager man = WorkManager.getInstance(getApplicationContext());
                        man.enqueue(one.build());

                        Intent trans = new Intent(AddSubjectsActivity.this, MainActivity.class);
                        startActivity(trans);
                    }
                }
            });
        }
        else {
            obj.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isUrlOk(obj.editTextTextPersonName3.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Url invalid", Toast.LENGTH_SHORT).show();
                    else {
                        Constraints con = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                                .build();
                        Data data = new Data.Builder()
                                .putString("Url", obj.editTextTextPersonName3.getText().toString())
                                .putString("Subject", obj.subject.getText().toString())
                                .putString("Prof name", obj.prof.getText().toString())
                                .putInt("Color", colors[(int) (System.currentTimeMillis() % 5)])
                                .build();
                        OneTimeWorkRequest.Builder one = new OneTimeWorkRequest.Builder(NetworkAddWokrer.class)
                                .setInputData(data)
                                .setConstraints(con);

                        WorkManager man = WorkManager.getInstance(getApplicationContext());
                        man.enqueue(one.build());

                        Intent trans = new Intent(AddSubjectsActivity.this, MainActivity.class);
                        startActivity(trans);
                    }
                }
            });
        }

    }
}