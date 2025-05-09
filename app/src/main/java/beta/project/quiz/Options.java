package beta.project.quiz;

import static beta.project.quiz.MainActivity.CHANNEL_ID;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Options extends AppCompatActivity {
    void get_back(boolean b){
        Intent i = new Intent(Options.this,MainActivity.class);
        if(b)i.putExtra("options",ops);
        startActivity(i);
        finish();
    }

    //private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission is needed to notify the parent at the end of every quiz.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CharSequence name = "Evaluation";
                String description = "Channel for showing score results at every quiz passed";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
            }
        }
    }
    boolean[] ops;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Intent data = getIntent();
        ops = data.getBooleanArrayExtra("options");
        Button cbtn = findViewById(R.id.button);
        Switch ckbtn = findViewById(R.id.ck1);
        ckbtn.setChecked(ops[0]);
        Switch ckbtn2 = findViewById(R.id.ck2);
        ckbtn2.setChecked(ops[1]);
        ckbtn2.setOnClickListener(v -> {
            if(ckbtn2.isChecked())requestNotificationPermission();
        });
        cbtn.setOnClickListener(v -> {
            ops[0]=ckbtn.isChecked();ops[1]=ckbtn2.isChecked();
            get_back(true);
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        get_back(false);
    }
}