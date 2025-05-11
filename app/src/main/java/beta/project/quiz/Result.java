package beta.project.quiz;

import static beta.project.quiz.MainActivity.CHANNEL_ID;
import static beta.project.quiz.MainActivity.NOTIFICATION_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


public class Result extends AppCompatActivity {
    //private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 100;
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
                String namech=(namec.length()>16)?namec.substring(0,16):namec;
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(namech +" passed "+data.getStringExtra("quiz_name"))
                        .setContentText(namec + " has scored " +result+" in "+data.getStringExtra("quiz_name") + " with a time of :"+data.getIntExtra("time",0)+"s")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(NOTIFICATION_ID, builder.build());
                Log.d("Donny", "onCreate: notification sent");
            }
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