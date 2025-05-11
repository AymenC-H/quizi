package beta.project.quiz;

import static beta.project.quiz.question_list.changeImageWithFade;
import static beta.project.quiz.question_list.outline;
import static beta.project.quiz.question_list.sharp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class activity_score extends AppCompatActivity {

    DatabaseHelper dbHelper;
    static RecyclerView recc;
    Scorr_adapter adapter;
    private void list_score() {
    try {dbHelper = new DatabaseHelper(this);
        List<Scorr> table = dbHelper.getScore();
        adapter = new Scorr_adapter(table);
        recc.setAdapter(adapter);
        recc.setLayoutManager(new LinearLayoutManager(activity_score.this));
    } catch (Exception e) {
        Log.d("Childrenn", "list_children: ", e);
        Toast.makeText(this, "out"+e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
    static ImageButton clear_btn;

    void toMain(){
        Intent intent = new Intent(activity_score.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        clear_btn=findViewById(R.id.clear_btn);
        recc = findViewById(R.id.recycler_view1);
        list_score();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        clear_btn.setOnClickListener(v -> {
            builder.setTitle("Clear Score Table?");
            builder.setMessage("Are you sure you want to clear the score table?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dbHelper.clearScore();
                Toast.makeText(this, "Score Table cleared", Toast.LENGTH_LONG).show();
                toMain();
            });
            builder.setNegativeButton("No", (dialog, which) -> {});
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        if (outline==null){
            outline = ContextCompat.getDrawable(this, R.drawable.trash_bin_outline);
            sharp = ContextCompat.getDrawable(this, R.drawable.trash_bin_sharp);
        }
        clear_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Change the image when touched
                        //dbtn.setImageDrawable(getDrawable(R.drawable.trash_bin_sharp));
                        changeImageWithFade(clear_btn, outline,sharp);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Restore the original image when touch is released or canceled
                        //dbtn.setImageDrawable(getDrawable(R.drawable.trash_bin_outline));
                        changeImageWithFade(clear_btn, sharp, outline);
                        break;
                }
                return false; // Consume the touch event
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toMain();
    }
}