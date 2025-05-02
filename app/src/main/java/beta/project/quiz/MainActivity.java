package beta.project.quiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import android.widget.RadioGroup;

import android.widget.Toast;
//import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private void animateButtonDown(Button hbtn,Intent i) {
        // Animate elevation change
        ObjectAnimator elevationDown = ObjectAnimator.ofFloat(hbtn, "elevation", 0f);
        elevationDown.setDuration(200);
        elevationDown.setInterpolator(new AccelerateDecelerateInterpolator());
        elevationDown.start();

        // Animate scale change
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(hbtn, "scaleX", 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(hbtn, "scaleY", 1f);
        scaleDownX.setDuration(200);
        scaleDownY.setDuration(200);
        scaleDownX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleDownY.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleDownX.start();
        scaleDownY.start();
        if (i!=null){
        scaleDownY.addListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        startActivity(i);
                        animateButtonUp(hbtn);
                    }
                });
        }
    }

    private void animateButtonUp(Button hbtn) {
        // Animate elevation change
        ObjectAnimator elevationUp = ObjectAnimator.ofFloat(hbtn, "elevation", 25f);

        // Animate scale change
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(hbtn, "scaleX", 1.1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(hbtn, "scaleY", 1.1f);
        scaleUpX.setDuration(200);
        scaleUpY.setDuration(200);
        elevationUp.setDuration(200);
        elevationUp.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleUpX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleUpY.setInterpolator(new AccelerateDecelerateInterpolator());
        elevationUp.start();
        scaleUpX.start();
        scaleUpY.start();
    }
audio_player A;static SharedPreferences sp;
    boolean verif_psw(String pass){
        if (pass.length()<8) return false;
        int pw=0;
        for (int i=0;i<pass.length();i++){
            if (Character.isDigit(pass.charAt(i))) pw++;
            if (Character.toUpperCase(pass.charAt(i))==pass.charAt(i))pw++;
            if (pw>=2) return true;
        }
        return false;
    }
    int encrypt(String password,int key) {
        int len=password.length()-1;
        int nb=password.charAt(len-1);
        int sum=0;int i;
        for (i = 0; i <= len; i++) sum+=password.charAt(i);sum*=sum;
        for (i = 0; i < len; i++) nb+=(int)(key*(Math.sqrt(password.charAt(i+1)))-sum);
        return Math.abs(nb);
    }
    public void init_audio(){
        sp = getSharedPreferences("myOptions", Context.MODE_PRIVATE);
        if (sp.getBoolean("soundOn", false)){
        try {
            final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.button_push);
            mediaPlayer.seekTo(30);
            mediaPlayer.start();
        }
        catch (Exception e) {
            Log.i("audio", "audio "+ e.getMessage());
        }}
    }
    Button sbtn;Button pbtn;Button hbtn;Button obtn;EditText nm;EditText ps;EditText psc;RadioGroup cg;RadioButton c1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);

        /*AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/
        setContentView(R.layout.activity_main);

        sbtn=findViewById(R.id.start_btn);
        pbtn=findViewById(R.id.play_btn);
        hbtn=findViewById(R.id.help_btn);
        obtn=findViewById(R.id.option_btn);
        //Button expbtn=findViewById(R.id.exp_btns);
        nm=findViewById(R.id.name);
        ps=findViewById(R.id.pass);
        psc=findViewById(R.id.passc);
        cg=findViewById(R.id.rgid);
        c1=findViewById(R.id.choice1);c1.setChecked(true);
        A= new audio_player();
        //String[] login={"",""};
        //boolean[] B1={false,false,false};
        //boolean[] B2={false,false,false};
        SharedPreferences log = getSharedPreferences("login_data", MODE_PRIVATE);
        obtn.setOnClickListener(v -> {
            sp = getSharedPreferences("myOptions", Context.MODE_PRIVATE);
            boolean[] op = {sp.getBoolean("soundOn", false), sp.getBoolean("notifOn", false), false};
            Intent i = new Intent(MainActivity.this, Options.class);
            i.putExtra("options", op);
            animateButtonDown(obtn,i);
            init_audio();
        });
        obtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        animateButtonDown(obtn,null);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        animateButtonUp(obtn);
                        break;
                }
                return false;
            }
        });
        hbtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, Help.class);
            init_audio();
            animateButtonDown(hbtn,i);
        });
        hbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //hbtn.setTranslationZ(0f);
                        animateButtonDown(hbtn,null);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        //hbtn.setTranslationZ(10f);
                        animateButtonUp(hbtn);
                        break;
                }
                return false;
            }
        });
        if(!(log.getBoolean("inscrit", false))){
            pbtn.setVisibility(View.GONE);
            pbtn.setEnabled(false);
            cg.setVisibility(View.GONE);
            cg.setEnabled(false);
            sbtn.setText("sign up");
            sbtn.setOnClickListener(v -> {
                sp = getSharedPreferences("myOptions", Context.MODE_PRIVATE);
                A.init_audio();
            if (nm.length()<2) {
                Toast.makeText(MainActivity.this,"Type a valid name",Toast.LENGTH_SHORT).show();
            }
            else if (!ps.getText().toString().equals(psc.getText().toString())){
                Toast.makeText(MainActivity.this,"Retype the password correctly",Toast.LENGTH_SHORT).show();
            }
            else if (!verif_psw(ps.getText().toString())) {
                Toast.makeText(MainActivity.this,"Le mot de passe est faible",Toast.LENGTH_SHORT).show();
            }
            else {
                SharedPreferences prefs = getSharedPreferences("login_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("inscrit", true);
                editor.putString("name", nm.getText().toString());
                //editor.putString("pwd", ps.getText().toString());
                Random rand = new Random();int nbr=rand.nextInt(999);
                editor.putInt("pwd", encrypt(ps.getText().toString(),nbr));
                editor.putInt("key", nbr);
                editor.commit();
                Toast.makeText(this, "LOGIN stored!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,MainActivity.class);
                Log.i("sd","reached");
                finish();
                startActivity(i);
            }
        });
        }
        else {
            //int[] Ts={0,0};
            psc.setVisibility(View.GONE);
            psc.setEnabled(false);
            //RelativeLayout.LayoutParams margins= (RelativeLayout.LayoutParams) sbtn.getLayoutParams();
            //sbtn.setMargins(margins.leftMargin,margins.topMargin*5,margins.rightMargin,margins.bottomMargin);
            //sbtn.setLayoutParams(margins);
            pbtn.setOnClickListener(v -> {
                //A.init_audio();
                Intent i = new Intent(MainActivity.this, quiz.class);
                startActivity(i);
            });
            sbtn.setOnClickListener(v -> {
                sp = getSharedPreferences("myOptions", Context.MODE_PRIVATE);
                A.init_audio();
                if (log.getBoolean("inscrit", false)) {
                    if (nm.length() == 0 || ps.length() == 0)
                        Toast.makeText(MainActivity.this, "Type your email/password above!", Toast.LENGTH_SHORT).show();
                    else if (nm.getText().toString().equals(log.getString("name", "")) && encrypt(ps.getText().toString(), log.getInt("key", 0)) == (log.getInt("pwd", 0))) {
                        //if (nm.getText().toString().equals(log.getString("name","")) && ps.getText().toString().equals(log.getString("pwd",""))){
                        if (c1.isChecked()) {
                            Intent i = new Intent(MainActivity.this, question_list.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, quiz.class);
                            i.putExtra("admin", true);

                            startActivity(i);
                        }
                    } else
                        Toast.makeText(MainActivity.this, "Incorrect LOGIN!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "oko!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            /*expbtn.setOnClickListener(v -> {
                A.init_audio();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    exportTable();
                } else {
                    checkStoragePermission();
                }
            });*/
        }
    }

    /*private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            exportTable();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }*/

    @Override
    protected void onResume() {
        super.onResume();
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        sbtn.startAnimation(fadeIn);
        pbtn.startAnimation(fadeIn);
        obtn.setVisibility(View.GONE);
        hbtn.setVisibility(View.GONE);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            obtn.startAnimation(fadeIn);
            hbtn.startAnimation(fadeIn);
            obtn.setVisibility(View.VISIBLE);
            hbtn.setVisibility(View.VISIBLE);
        }, 500);
        cg.startAnimation(fadeIn);
        nm.startAnimation(fadeIn);
        ps.startAnimation(fadeIn);
        psc.startAnimation(fadeIn);
            Intent data = getIntent();
            boolean[] ops = data.getBooleanArrayExtra("options");
            if (ops!=null){
            SharedPreferences prefs = getSharedPreferences("myOptions", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("soundOn", ops[0]);
            editor.putBoolean("notifOn", ops[1]);
            editor.commit();
            Toast.makeText(this, "Preferences saved!", Toast.LENGTH_SHORT).show();
        }
    }


    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        A.end_audio();
    }*/

}