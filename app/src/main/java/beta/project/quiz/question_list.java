package beta.project.quiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class question_list extends AppCompatActivity {

    DatabaseHelper dbHelper;
    static RecyclerView recc;
    Question_adapter adapter;
    static Drawable outline,sharp;boolean[] checks;
    boolean fromUpdate,fromAdd,selectedOne;String nameq;

    private static final String TAG = "question_list";
    private void list_questions() {
        try {dbHelper = new DatabaseHelper(this);
            List<Quest> table = dbHelper.getQuestions();
            adapter = new Question_adapter(table);String toast;
            switch (table.size()){
                case 0:toast="Create your 1st question here!";break;
                case 1:toast="there is 1 question";break;
                default:toast="there are "+table.size()+" questions";break;
            }
            Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
            recc.setAdapter(adapter);
            recc.setLayoutManager(new LinearLayoutManager(question_list.this));
        } catch (Exception e) {
            Log.d(TAG, "list_questions: ", e);
            Toast.makeText(this, "out"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    static void changeImageWithFade(ImageButton ib, Drawable old_image, Drawable new_image) {

        // Create fade-out animation
        ObjectAnimator fadeOut = ObjectAnimator.ofInt(old_image, "alpha", 255, 0);
        fadeOut.setDuration(150);
        fadeOut.setInterpolator(new AccelerateDecelerateInterpolator());

        // Create fade-in animation
        ObjectAnimator fadeIn = ObjectAnimator.ofInt(new_image, "alpha", 0, 255);
        fadeIn.setDuration(200);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());

        // Create AnimatorSet to play animations together
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeOut, fadeIn);

        // Add a listener to change the image after fade-out
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // Change the image after fade-out
                ib.setImageDrawable(new_image);
                // Reset the alpha of the old drawable
                new_image.setAlpha(255);
            }
        });

        animatorSet.start();
    }
    static EditText etTime;int maxtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);
        EditText et=findViewById(R.id.editText);
        etTime=findViewById(R.id.editTextTime);
        Button abtn=findViewById(R.id.add_but);
        Button ubtn=findViewById(R.id.update_button);
        ImageButton dbtn=findViewById(R.id.del_but);
        LinearLayout ll = findViewById(R.id.linearLayout3);//to-do change width on listi
        LinearLayout ll2 = findViewById(R.id.linearLayout2);
        recc=findViewById(R.id.recycler_view1);
        /*Handler  handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list_questions();
            }
        });*/

        //list_questions();
        Intent data = getIntent();
        fromUpdate=data.getBooleanExtra("listiU",false);//to update the quiz
        fromAdd=data.getBooleanExtra("listi",false);//to add question
        checks=data.getBooleanArrayExtra("tocheck");
        nameq=data.getStringExtra("name");
        /*et.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
            abtn.setVisibility(View.GONE);
            else
            abtn.setVisibility(View.VISIBLE);});*/
        if (fromAdd){//from quiz
            ViewGroup.LayoutParams params = ll.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            ll.setLayoutParams(params);
            ubtn.setVisibility(View.GONE);
            ubtn.setEnabled(false);
            dbtn.setVisibility(View.GONE);
            dbtn.setEnabled(false);
            try {
                    if (fromUpdate) {
                        abtn.setText("Update");
                        et.setText(nameq);
                    }
                    list_questions();
                    /*if (b) {
                        for (int i = 0; i < adapter.chks.size(); i++) {
                            if (checks[i])
                                adapter.chks.get(i).ckb.setChecked(true);
                        }
                    }*/

                    abtn.setOnClickListener(v -> {
                        if (et.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Enter a name!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        selectedOne = true;
                        List<Integer> idqus = new ArrayList<>();
                        for (int i = 0; i < adapter.chks.size(); i++) {
                            if (adapter.chks.get(i).ckb.isChecked()) {
                                idqus.add(adapter.chks.get(i).id);
                                selectedOne = false;
                            }
                        }
                        if (selectedOne) {
                            Toast.makeText(this, "Check at least 1 question!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (fromUpdate) {
                            dbHelper.updateQuiz(et.getText().toString(), idqus,maxtime);
                            Toast.makeText(this, "Quiz updated!", Toast.LENGTH_SHORT).show();
                            //last update
                            if (data.getBooleanExtra("isLast", false)) {
                                Intent intent = new Intent(question_list.this, quiz.class);
                                intent.putExtra("admin", true);
                                startActivity(intent);
                                finish();
                            }
                            finishAffinity();
                        }
                        else {//addQuiz for normal listing
                            try{
                                try {
                                    if (!etTime.getText().toString().isEmpty())maxtime=Integer.parseInt(etTime.getText().toString());
                                    else if (maxtime<10)
                                        Toast.makeText(this, "Duration must be at least 10 sec!", Toast.LENGTH_SHORT).show();
                                    else if (maxtime>7200)
                                        Toast.makeText(this, "Duration shouldn't exceed 2 Hours!", Toast.LENGTH_SHORT).show();
                                    else maxtime=0;
                                } catch (NumberFormatException e) {
                                    Toast.makeText(this, "Invalid time format!", Toast.LENGTH_SHORT).show();
                                }
                                if (dbHelper.addQuiz(et.getText().toString().trim(),idqus,maxtime))
                                    Toast.makeText(this, "Quiz name exists!", Toast.LENGTH_SHORT).show();
                                else {Intent intent = new Intent(question_list.this, quiz.class);
                                    intent.putExtra("admin",true);
                                    Toast.makeText(this, "Quiz added!", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            catch (Exception e){
                                Log.d("not added", "quizzi!!"+e.getMessage());
                            }
                        }
                    });
                    }
                    catch (Exception e){
                        Log.d("Done", "in!!"+e.getMessage());
                    }
        }
        else{//question side
            ll2.setVisibility(View.GONE);
            ll2.setEnabled(false);

        abtn.setOnClickListener(v -> {
            Intent intent = new Intent(question_list.this, Add_question.class);
            startActivity(intent);
        });
        ubtn.setOnClickListener(v -> {List<Intent> I= new ArrayList<>();
            for (int i=0;i<adapter.chks.size();) {
                if (!adapter.chks.get(i).ckb.isChecked()) {i++;continue;}
                Intent intent = new Intent(question_list.this, Add_question.class);
                Quest res=dbHelper.getQuestions(adapter.chks.get(i).id);
                intent.putExtra("u_a",true);
                intent.putExtra("id",res.id);
                intent.putExtra("question",res.question);
                intent.putExtra("ans1",res.ans1);
                intent.putExtra("ans2",res.ans2);
                intent.putExtra("ans3",res.ans3);
                intent.putExtra("img",res.url);
                intent.putExtra("isLast",i++==0);
                I.add(intent);
            }
            Intent[] intents=new Intent[I.size()];
            if(!I.isEmpty()) {
                for (int i = 0; i < I.size(); i++) intents[i] = I.get(i);
                startActivities(intents);
            }
            else Toast.makeText(this, "No question selected!", Toast.LENGTH_SHORT).show();
        });
            dbtn.setOnClickListener(v -> {boolean b,bb;b=true;bb=false;
                try{List<Integer> idqs = new ArrayList<>();
                for (int i=0;i<adapter.chks.size();i++){
                    idqs.add(adapter.chks.get(i).ckb.isChecked()?adapter.chks.get(i).id:-1);
                    if (idqs.get(i)!=-1){
                        if(!b){bb=true;continue;}
                        b=false;
                    }
                }
                dbHelper.removeQuestion(idqs);
                if (!b)Toast.makeText(this, "Question"+(bb?"":"s")+" deleted!", Toast.LENGTH_SHORT).show();
                list_questions();}
                catch (Exception e){
                    Log.d("Done", "in 3!!"+e.getMessage());
                }
            });
        if (outline==null){
            outline = ContextCompat.getDrawable(this, R.drawable.trash_bin_outline);
            sharp = ContextCompat.getDrawable(this, R.drawable.trash_bin_sharp);
        }

        dbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Change the image when touched
                        //dbtn.setImageDrawable(getDrawable(R.drawable.trash_bin_sharp));
                        changeImageWithFade(dbtn, outline,sharp);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Restore the original image when touch is released or canceled
                        //dbtn.setImageDrawable(getDrawable(R.drawable.trash_bin_outline));
                        changeImageWithFade(dbtn, sharp, outline);
                        break;
                }
                return false; // Consume the touch event
            }
        });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        list_questions();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fromUpdate) {
            Intent intent = new Intent(this, quiz.class);
            intent.putExtra("admin", true);
            startActivity(intent);
            finish();
        }
        if (!fromAdd) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }
}