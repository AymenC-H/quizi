package beta.project.quiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;


public class Help extends AppCompatActivity {
    void get_back(){
        Intent i = new Intent(Help.this,MainActivity.class);
        finish();
        startActivity(i);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        TextView par=findViewById(R.id.textView3);
        TextView email=findViewById(R.id.emaili);
        TextView number=findViewById(R.id.dial);
        LinearLayout nb=findViewById(R.id.number);
        //LinearLayout em=findViewById(R.id.email);
        Button ret=findViewById(R.id.button3);
        par.setText(HtmlCompat.fromHtml("<b>Description</b>: <br>" +
                "QUIZI is a quiz game where you can make a quiz for your children. <br>" +
                "First you create a local account," +
                "then you set the quiz for your child in 3 easy steps: <br>" +
                "1. Click on the create button <br>" +
                "2. Set a question, image (optional) and rules <br>" +
                "3. Create and fill a quiz with the wanted questions <br>" +
                "After this, you can hand the quiz for your child without needing a sign in <br><br>" +
                "<b style=\"color=red;\">SAVE YOUR PASSWORD!!</b>, forgetting it means losing all the data<br>" +
                "you can clear the app data to restart over <br>" +
                "for any issues or question, you can contact the developer <br>", HtmlCompat.FROM_HTML_MODE_LEGACY));

        ret.setOnClickListener(v -> {
            //A.init_audio();
            get_back();
        });

        nb.setOnClickListener(v -> {
            String data = "tel:"+number.getText().toString();
            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(data));
            startActivity(i);
        });

        email.setOnClickListener(v -> {
            String data = "mailto:";
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setDataAndType(Uri.parse(data),"text/plain");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
            i.putExtra(Intent.EXTRA_SUBJECT, "How to use QUIZI... ?");
            try {
                startActivity(Intent.createChooser(i,"send email..."));
            } catch (Exception e) {
                Toast.makeText(this, "oops!!"+e.getMessage(), Toast.LENGTH_LONG).show();
                //throw new RuntimeException(e);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //A.end_audio();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        get_back();
    }
}