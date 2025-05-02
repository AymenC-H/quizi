package beta.project.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class quiz extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Intent intent;
    RecyclerView recc;
    Quizz_adapter adapter;Quizz res;
    private void list_quizz() {
        dbHelper = new DatabaseHelper(this);
        List<Quizz> table = dbHelper.getQuiz();
        adapter = new Quizz_adapter(table);
        recc.setAdapter(adapter);
        recc.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        LinearLayout child=findViewById(R.id.child_side);
        Button abtn=findViewById(R.id.add_but);
        recc=findViewById(R.id.recycler_view8);
        Button ubtn=findViewById(R.id.update_button);
        ImageButton dbtn=findViewById(R.id.del_but);
        LinearLayout ll=findViewById(R.id.linearLayout3);
        ScrollView sv=findViewById(R.id.scrollView4);
        //list_quizz();
        try{
        Intent verify = getIntent();
        boolean admin=verify.getBooleanExtra("admin",false);
        if (admin){//admin interface
            ViewGroup.LayoutParams params = sv.getLayoutParams();
            params.height =(int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    580,
                    getResources().getDisplayMetrics()
            );
            sv.setLayoutParams(params);
            child.setVisibility(View.GONE);
            child.setEnabled(false);
            abtn.setOnClickListener(v -> {
                Intent intent = new Intent(quiz.this, question_list.class);
                intent.putExtra("listi",true);
                startActivity(intent);
                finish();
            });
            /*dbHelper = new DatabaseHelper(this);//tentative pour selectionner les questions associé au quiz sans du hard code avec un handler
            List<Quest> table = dbHelper.getQuestions();
            Question_adapter adapterq = new Question_adapter(table);*/
            ubtn.setOnClickListener(v -> {
                try {
                    /*Log.d("Done", "notpassed!!"+adapterq.chks);
                    boolean[] tocheck = new boolean[adapterq.chks.size()];
                    Log.d("Done", "passed!!"+adapter.cheks.size());
                    for (int i = 0; i < adapter.cheks.size(); i++) {
                        if (!adapter.cheks.get(i).ckb.isChecked()) continue;
                        Intent intent = new Intent(quiz.this, question_list.class);
                        res = dbHelper.getQuiz(adapter.cheks.get(i).idn);
                        for (int j = 0; j < res.ids_questions.length; j++) {
                            for (int k = 0; k < question_list.adapter.chks.size(); k++) {
                                if (question_list.adapter.questionList.get(i).id == res.ids_questions[i])
                                    tocheck[k] = true;
                            }
                        }*/
                    List<Intent> I = new ArrayList<>();
                    for (int i = 0; i < adapter.cheks.size(); i++) {
                        intent = new Intent(quiz.this, question_list.class);
                        res = dbHelper.getQuiz(adapter.cheks.get(i).idn);
                        if (i == 0) intent.putExtra("isLast", true);
                        intent.putExtra("listi", true);
                        intent.putExtra("listiU", true);
                        intent.putExtra("name", res.nameq);
                        //intent.putExtra("tocheck", tocheck);
                        //intent.putExtra("ids",res.ids_questions);
                        I.add(intent);
                    }
                    if (!I.isEmpty()) {
                        Intent[] intents = new Intent[I.size()];
                        for (int i = 0; i < I.size(); i++) intents[i] = I.get(i);
                        finish();
                        startActivities(intents);
                    } else Toast.makeText(this, "No quiz selected!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Log.e("Done", "in7!!"+e.getMessage());
                }
            });
            dbtn.setOnClickListener(v -> {List<String> idqus=new ArrayList<>();
                for (int i=0;i<adapter.cheks.size();i++){
                    if (adapter.cheks.get(i).ckb.isChecked())idqus.add(adapter.cheks.get(i).idn);
                }
                Log.d("Done", "in5!!"+idqus.size());
                if (!idqus.isEmpty()) {
                    dbHelper.removeQuizz(idqus);
                    Toast.makeText(this, "Quiz deleted!", Toast.LENGTH_SHORT).show();
                    list_quizz();
                }
                else Toast.makeText(this, "No quiz selected!", Toast.LENGTH_SHORT).show();
            });
            if (question_list.outline==null){
                question_list.outline= ContextCompat.getDrawable(this,R.drawable.trash_bin_outline);
                question_list.sharp=ContextCompat.getDrawable(this,R.drawable.trash_bin_sharp);
            }
            dbtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Change the image when touched
                            //dbtn.setImageDrawable(getDrawable(R.drawable.trash_bin_sharp));
                            question_list.changeImageWithFade(dbtn, question_list.outline,question_list.sharp);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            // Restore the original image when touch is released or canceled
                            //dbtn.setImageDrawable(getDrawable(R.drawable.trash_bin_outline));
                            question_list.changeImageWithFade(dbtn, question_list.sharp, question_list.outline);
                            break;
                    }
                    return false; // Consume the touch event
                }
            });
        }
        else {//child part
            ViewGroup.LayoutParams params = ll.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            ll.setLayoutParams(params);
            EditText name = findViewById(R.id.name);
            ubtn.setVisibility(View.GONE);
            ubtn.setEnabled(false);
            dbtn.setVisibility(View.GONE);
            dbtn.setEnabled(false);
            abtn.setText("START");
            abtn.setOnClickListener(v -> {
                //try{
                if (name.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter your name above!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int index=adapter.cheks.size();
                for (int i=0;i<adapter.cheks.size();i++){
                    if (adapter.cheks.get(i).ckb.isChecked()) {
                        if (index != adapter.cheks.size()) {
                            Toast.makeText(this, "Select only one quiz to take!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        index = i;
                    }
                }
                if (index==adapter.cheks.size()){
                    Toast.makeText(this, "Select a quiz to take!", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbHelper = new DatabaseHelper(this);
                boolean b= dbHelper.existChild(name.getText().toString());
                if (b) {
                    dbHelper.addChild(name.getText().toString());
                    Toast.makeText(this, "New child added!", Toast.LENGTH_SHORT).show();
                }
                int[] idqus;String[] answs1,answs2,answs3,nameqs,urls;
                res=dbHelper.getQuiz(adapter.cheks.get(index).idn);
                idqus=res.ids_questions;
                answs1=new String[idqus.length];
                answs2=new String[idqus.length];
                answs3=new String[idqus.length];
                nameqs=new String[idqus.length];
                urls=new String[idqus.length];
                for (int i=0;i<idqus.length;i++){
                    Quest question=dbHelper.getQuestions(idqus[i]);
                    nameqs[i]=question.question;
                    answs1[i]=question.ans1;
                    answs2[i]=question.ans2;
                    answs3[i]=question.ans3;
                    urls[i]=question.url;
                }
                Intent intent = new Intent(quiz.this, Activity1.class);
                intent.putExtra("name",(name.getText().toString().trim()));
                intent.putExtra("idqu",res.nameq);
                intent.putExtra("questions",nameqs);
                intent.putExtra("answers1",answs1);
                intent.putExtra("answers2",answs2);
                intent.putExtra("answers3",answs3);
                intent.putExtra("images",urls);
                intent.putExtra("time",res.mxtime);
                Toast.makeText(this, "Quiz started!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            });
        }}
        catch (Exception e) {
            Log.e("Done", "in6!!"+e.getMessage());
        }
        //list_questions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.list_quizz();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(quiz.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
