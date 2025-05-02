package beta.project.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class Options extends AppCompatActivity {
    void get_back(){
        Intent i = new Intent(Options.this,MainActivity.class);
        finish();
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Intent data = getIntent();
        boolean[] ops = data.getBooleanArrayExtra("options");
        Button cbtn = findViewById(R.id.button);
        Switch ckbtn = findViewById(R.id.ck1);
        ckbtn.setChecked(ops[0]);
        Switch ckbtn2 = findViewById(R.id.ck2);
        ckbtn2.setChecked(ops[1]);
        cbtn.setOnClickListener(v -> {
            ops[0]=ckbtn.isChecked();ops[1]=ckbtn2.isChecked();
            Intent i = new Intent(Options.this,MainActivity.class);
            i.putExtra("options",ops);
            startActivity(i);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        get_back();
    }
}