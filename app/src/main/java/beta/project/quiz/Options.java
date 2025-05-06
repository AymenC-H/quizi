package beta.project.quiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Options extends AppCompatActivity {
    void get_back(boolean b){
        Intent i = new Intent(Options.this,MainActivity.class);
        if(b)i.putExtra("options",ops);
        startActivity(i);
        finish();
    }
    void requestNotificationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
            Toast.makeText(this, "Notification permission is needed to notify the parent at the end of every quiz.", Toast.LENGTH_SHORT).show();
        }
    }
    //private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
                // ... proceed with storage access ...
            } else {
                // Permission denied
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
                // ... handle the denied permission ...
            }
        }*/
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
                // ... proceed with notification sending ...
            } else {
                // Permission denied
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
                // ... handle the denied permission ...
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