package beta.project.quiz;

import static beta.project.quiz.question_list.changeImageWithFade;
import static beta.project.quiz.question_list.outline;
import static beta.project.quiz.question_list.sharp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class activity_child extends AppCompatActivity {
    DatabaseHelper dbHelper;
    RecyclerView recc;
    Childd_adapter adapter;
    Button add_btn;
    ImageButton dbtn;

    private void list_children() {
        try {dbHelper = new DatabaseHelper(this);
            List<Childd> table = dbHelper.getChildren();
            adapter = new Childd_adapter(table);
            recc.setAdapter(adapter);
            recc.setLayoutManager(new LinearLayoutManager(activity_child.this));
        } catch (Exception e) {
            Log.d("Childrenn", "list_children: ", e);
            Toast.makeText(this, "out"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        add_btn = findViewById(R.id.add_but);
        dbtn = findViewById(R.id.del_but);
        recc = findViewById(R.id.recycler_view1);
        list_children();
        add_btn.setOnClickListener(v -> {
            Intent i = new Intent(activity_child.this, quiz.class);
            startActivity(i);
        });
        dbtn.setOnClickListener(v -> {boolean b,bb;b=false;bb=false;
            try{List<String> idqs = new ArrayList<>();
                for (int i=0;i<adapter.ccheks.size();i++){
                    if (adapter.ccheks.get(i).ckb.isChecked()) {
                        idqs.add(adapter.ccheks.get(i).idn);
                        if(bb)continue;
                        if(b)bb=true;
                        b=true;
                    }
                }
                dbHelper.removeChild(idqs.toArray(new String[idqs.size()]));
                if (!b)Toast.makeText(this, (bb?"Child name":"Children names")+" removed!", Toast.LENGTH_SHORT).show();
                list_children();}
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}