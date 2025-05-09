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
                "QUIZI is a quiz maker app where you can make custom quiz for your children.<br>" +
                "First you create a local account," +
                "then you set the quiz for your child in 3 easy steps: <br>" +
                "1. Click on the \"manage\" button <br>" +
                "2. Set a question, image (optional) and the answers <br>" +
                "3. Create a quiz with the wanted questions <br>" +
                "After this, your children can pass a quiz via the \"play\" button without needing to sign in<br>" +
                "In the options menu, you can change the sound and notification settings.<br>" +
                "If you ticked the notification option, you get a notification when your child passes a quiz.<br>" +
                "Then you would manage the children list and their scores by selecting which one to view.<br><br>" +
                "<b style=\"color=red;\">SAVE YOUR PASSWORD!!</b>,<br>" +
                "forgetting it means losing the ability to manage the database<br>" +
                "In this case, you would clear the app data to start from scratch. <br>" +
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