package beta.project.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import java.util.Arrays;


public class Result extends AppCompatActivity {
    //private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 100;
    private static final String CHANNEL_ID = "my_channel_id";
    private static final int NOTIFICATION_ID = 1;
    //private ActivityResultLauncher<String> requestPermissionLauncher;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView res=findViewById(R.id.textView2);
        TextView texts=findViewById(R.id.textView4);
        Button rbtn=findViewById(R.id.sub_btns);

        try{
            Intent data = getIntent();
            double[] sc = data.getDoubleArrayExtra("scores");
            //Log.d("Donny", "onCreate: "+sc[0]+", "+sc[1]);
            double score = Arrays.stream(sc).sum() / sc.length;//Arrays.stream(sc).sum();
            String namec = data.getStringExtra("idc");
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.addScore(namec, data.getIntExtra("idqu",0), score, data.getIntExtra("time", 0));
            String result=Math.round(score * 100) + "%";
            texts.setText(result);
            SharedPreferences sp = getSharedPreferences("myOptions", Context.MODE_PRIVATE);
            if(sp.getBoolean("notifOn", false) & ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                Log.d("Donny", "onCreate: notified");
                String name=(namec.length()>16)?namec.substring(0,16):namec;
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(name +" passed "+data.getStringExtra("quiz_name"))
                        .setContentText(namec + " has scored " +result+" in "+data.getStringExtra("quiz_name") + "with a time of :")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                Log.d("Donny", "onCreate: notified1");
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    notificationManager.notify(NOTIFICATION_ID, builder.build());
                }
                Log.d("Donny", "onCreate: notified2");
            }
            /*requestPermissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            // Permission is granted. Continue the action or workflow in your app.
                            showNotification();
                        } else {
                            // Explain to the user that the feature is unavailable because the
                            // feature requires a permission that the user has denied. At the
                            // same time, respect the user's decision. Don't link to system
                            // settings in an effort to convince the user to change their
                            // decision.
                            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
                        }
                    });*/
            String ch;
            Log.d("Donny", score + "onCreate: " + sc[0]);
            if (score > .4 && score < .6) ch = namec + " at AVERAGE score";
            else if (score == 0) ch = ":(";
            else if (score <= .4) ch = "GL next time " + namec + "!!";
            else if (score == 1) ch = "PERFECT";
            else if (score >= .8) ch = "WELL DONE " + namec;
            else if (score >= .6) ch = "GOOD " + namec;
            else ch = "ERROR";
            res.setText(ch);
        }
        catch (Exception e){
            Log.e("Donny", "onCreate: "+e.getMessage());
        }
        rbtn.setOnClickListener(v -> {
            //A.init_audio();
            Intent i = new Intent(Result.this,quiz.class);
            startActivity(i);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Result.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}