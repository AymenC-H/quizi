package beta.project.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Add_question extends AppCompatActivity {
    boolean distinct(EditText[] ed){
        for(int i=0;i<ed.length;i++) {
            for (int j = i + 1; j < ed.length; j++) {
                if (ed[i].getText().toString().equals(ed[j].getText().toString()))
                    return true;
            }
        }
        return false;
    }

    boolean ua, islast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        Button abtn = findViewById(R.id.button6);
        EditText question = findViewById(R.id.editText4);
        EditText ans1 = findViewById(R.id.editText5);
        EditText ans2 = findViewById(R.id.editText6);
        EditText ans3 = findViewById(R.id.editText7);
        EditText img = findViewById(R.id.imageField);
        CheckBox cb1 = findViewById(R.id.checkBox3);
        CheckBox cb2 = findViewById(R.id.checkBox4);
        CheckBox cb3 = findViewById(R.id.checkBox5);
        Intent data = getIntent();
        ua = data.getBooleanExtra("u_a", false);
        //to-do: update
        if (ua) {
            abtn.setText("Update");
            img.setText(data.getStringExtra("img"));
            question.setText(data.getStringExtra("question"));
            ans1.setText((data.getStringExtra("ans1")).substring(1));
            ans2.setText((data.getStringExtra("ans2")).substring(1));
            ans3.setText((data.getStringExtra("ans3")).substring(1));
            if (data.getStringExtra("ans1").charAt(0) == '1') cb1.setChecked(true);
            if (data.getStringExtra("ans2").charAt(0) == '1') cb2.setChecked(true);
            if (data.getStringExtra("ans3").charAt(0) == '1') cb3.setChecked(true);
        }

        abtn.setOnClickListener(v -> {
            if (String.valueOf(question.getText()).isEmpty() || String.valueOf(ans1.getText()).isEmpty() || String.valueOf(ans2.getText()).isEmpty() || String.valueOf(ans3.getText()).isEmpty())
                Toast.makeText(this, "Answers and question fields can't be empty!", Toast.LENGTH_LONG).show();
            //return;
            if (distinct(new EditText[]{ans1, ans2, ans3}))
                Toast.makeText(this, "Answers should be distinct!", Toast.LENGTH_LONG).show();
            else if (!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked())
                Toast.makeText(this, "Choose at least one answer to be true!", Toast.LENGTH_LONG).show();
            else{
            String a1=(cb1.isChecked()?"1":"0")+ans1.getText().toString();
            String a2=(cb2.isChecked()?"1":"0")+ans2.getText().toString();
            String a3=(cb3.isChecked()?"1":"0")+ans3.getText().toString();
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            if (ua) {
                dbHelper.updateQuestion(question.getText().toString(), a1, a2, a3, data.getIntExtra("id", -1), img.getText().toString());
                Toast.makeText(this, "Question updated!", Toast.LENGTH_SHORT).show();
            }
            else {
                dbHelper.addQuestion(question.getText().toString(), a1, a2, a3, img.getText().toString());
                Toast.makeText(this, "Question added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Add_question.this, question_list.class);
                    intent.putExtra("u_a",ua);
                    startActivity(intent);
                    finish();
            }
                islast = data.getBooleanExtra("isLast", false);
                if (islast) {
                    Intent intent = new Intent(Add_question.this, question_list.class);
                    startActivity(intent);
                    finishAffinity();
                }
                else finish();
            }

        });}
        @Override
        public void onBackPressed() {
            super.onBackPressed();
                Intent intent = new Intent(this, question_list.class);
                startActivity(intent);
                finishAffinity();
        }
    }