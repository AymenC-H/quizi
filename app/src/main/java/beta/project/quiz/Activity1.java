package beta.project.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Activity1 extends AppCompatActivity {
    static TextView questT,pg,field1,field2,field3;static CheckBox r1,r2,r3;String[] quests,answs1,answs2,answs3,imgs;
    static ImageView imgq;Intent data;
    static LinearLayout timell;
    boolean b;
    int index,endtime;long ctime;double[] sums;boolean[][]  Eanswers,Tanswers;int[] sum;

    void toScore(){
        Intent intent=new Intent(Activity1.this,Result.class);
        /*int sum = 0;
        for (int i = 0; i < sums.length; i++)sum += sums[i];
        intent.putExtra("score",sum);*/
        intent.putExtra("scores",sums);
        intent.putExtra("idqu",data.getIntExtra("idqu",0));
        intent.putExtra("quiz_name",data.getStringExtra("quiz_name"));
        intent.putExtra("idc",data.getStringExtra("name"));
        intent.putExtra("time",(int)ctime);
        startActivity(intent);
        finish();
    }
    private void fill_activity(String[] data, boolean[] checks){
        questT.setText(data[0]);
        questT.setSelected(true);
        field1.setText(data[1]);
        field1.setSelected(true);
        field2.setText(data[2]);
        field2.setSelected(true);
        field3.setText(data[3]);
        field3.setSelected(true);
        r1.setChecked(checks[0]);
        r2.setChecked(checks[1]);
        r3.setChecked(checks[2]);
        if(!data[4].isEmpty())Glide.with(this).load(data[4]).into(imgq);
        else Glide.with(this).load(R.drawable.no_image).into(imgq);
    }

    boolean calcul(int index) {
        Eanswers[index][0] = r1.isChecked();
        Eanswers[index][1] = r2.isChecked();
        Eanswers[index][2] = r3.isChecked();
        if (!Eanswers[index][0] && !Eanswers[index][1] && !Eanswers[index][2]) {
            Toast.makeText(Activity1.this, "Il faut Choisir une réponse", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            boolean logic = true;
            for (int j = 0; j < 3; j++) {
                if (Eanswers[index][j] && !Tanswers[index][j]) {
                    logic = false;
                    Log.d("Donny", "calcul: " + j);
                    break;
                }
            }
            if (logic) {
                sums[index] = 0;
                for (int j = 0; j < 3; j++) {
                    sums[index] += (Eanswers[index][j] == Tanswers[index][j] && Tanswers[index][j]) ? (1.0 / sum[index]) : 0;
                    Log.d("Donny", "calcul: " + sums[index] +", "+(1.0 / sum[index]));
                }
            }
            Log.d("Donny", sum[index] +"calcul: " + sums[index]);
        }
        return true;
    }
    TextView timer,timerm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
        questT=findViewById(R.id.question);
        pg=findViewById(R.id.pages);
        r1 =findViewById(R.id.ans1);
        r2 =findViewById(R.id.ans2);
        r3 =findViewById(R.id.ans3);
        field1 =findViewById(R.id.textView1);
        field2 =findViewById(R.id.textView2);
        field3 =findViewById(R.id.textView3);
        imgq =findViewById(R.id.imageView);
        timell=findViewById(R.id.timeLL);
        timer = findViewById(R.id.timer);
        timerm = findViewById(R.id.maxtimer);

        data=getIntent();
        quests=data.getStringArrayExtra("questions");
        answs1=data.getStringArrayExtra("answers1");
        answs2=data.getStringArrayExtra("answers2");
        answs3=data.getStringArrayExtra("answers3");
        imgs=data.getStringArrayExtra("images");
        pg.setText(index+1+"/"+quests.length);
        Tanswers= new boolean[quests.length][3];
        for(int i=0;i<quests.length;i++){
            Tanswers[i]=new boolean[3];
            Tanswers[i][0]=answs1[i].charAt(0)=='1';
            Tanswers[i][1]=answs2[i].charAt(0)=='1';
            Tanswers[i][2]=answs3[i].charAt(0)=='1';
        }
        Eanswers=new boolean[quests.length][3];
        sum=new int[quests.length];//sum est le nombre des bonne réponses
        for(int i=0;i<quests.length;i++){
            Eanswers[i]=new boolean[3];
            sum[i]=0;
            for(int j=0;j<3;j++) {
                Eanswers[i][j] = false;
                sum[i]+=Tanswers[i][j]?1:0;
            }
        }
        fill_activity(new String[]{quests[index],answs1[index].substring(1),answs2[index].substring(1),answs3[index].substring(1),imgs[index]},Eanswers[index]);
        Button rbtn=findViewById(R.id.right);
        Button lbtn=findViewById(R.id.left);
        if(quests.length==1){
            rbtn.setText("Finish");
        }
        lbtn.setText("Finish");
        sums=new double[quests.length];//sums est le score de chaque question
        index=0;
        rbtn.setOnClickListener(v -> {
            if (calcul(index)){
            if(index==quests.length-1)toScore();
            else {
                fill_activity(new String[]{quests[++index], answs1[index].substring(1), answs2[index].substring(1), answs3[index].substring(1), imgs[index]}, Eanswers[index]);
                r1.setChecked(Eanswers[index][0]);
                r2.setChecked(Eanswers[index][1]);
                r3.setChecked(Eanswers[index][2]);
                pg.setText((index + 1) + "/" + quests.length);
                if (index == quests.length - 1) {
                    rbtn.setText("Finish");
                    lbtn.setText("Previous");
                }
            }
        }
            });
        lbtn.setOnClickListener(v -> {
            if(calcul(index)) {
                if (index == 0) toScore();
                else {
                    Log.d("donno", "said false!!");
                    Eanswers[index][0] = r1.isChecked();
                    Eanswers[index][1] = r2.isChecked();
                    Eanswers[index][2] = r3.isChecked();
                    r1.setChecked(Eanswers[index][0]);
                    r2.setChecked(Eanswers[index][1]);
                    r3.setChecked(Eanswers[index][2]);
                    rbtn.setText("Next");
                    fill_activity(new String[]{quests[--index], answs1[index].substring(1), answs2[index].substring(1), answs3[index].substring(1), imgs[index]}, Eanswers[index]);
                    pg.setText((index + 1) + "/" + quests.length);
                    if (index == 0) {
                        lbtn.setText("Finish");
                    }
                }
            }
        });
        b=true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        /*sec = 10;
        handler = new Handler();Runnable run=new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                timer.setText(sec + "s");
            }};
            while ((sec--) > 0) {
            handler.post(run);
            }*/
        endtime=data.getIntExtra("time",0);
        if(endtime==0) timell.setVisibility(LinearLayout.GONE);
        else timerm.setText(Integer.toString(endtime));
        if(b)startCountdown(endtime*1000,1000);
        }
    private CountDownTimer countdownTimer;
    private void startCountdown(int totalTimeInMillis,int countDownInterval) {b=false;
        countdownTimer = new CountDownTimer(totalTimeInMillis, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                ctime = Math.abs(millisUntilFinished / 1000 - endtime);
                if(endtime!=0)timer.setText(Integer.toString((int) ctime));
            }
            @Override
            public void onFinish() {
                toScore();
            }
        };
        countdownTimer.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the countdown when the activity is destroyed
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toScore();
    }
}