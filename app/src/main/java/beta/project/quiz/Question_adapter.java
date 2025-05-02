package beta.project.quiz;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Question_adapter extends RecyclerView.Adapter<Question_adapter.QuestionViewHolder> {
    static class combo{
        Integer id;
        CheckBox ckb;
        public combo(Integer id,CheckBox ckb){
            this.id=id;
            this.ckb=ckb;
    }}
    List<Quest> questionList;
    List<combo> chks;
    public Question_adapter(List<Quest> questionList) {
        this.questionList = questionList;
        chks=null;
        //chks=new ArrayList<>();
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_line, parent, false);
        return new QuestionViewHolder(view);
    }

    private int nbr,nbf;
    private static final String TAG = "question_adapter";
    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        Quest line = questionList.get(position);
        if (this.chks==null)this.chks = new ArrayList<>();
        this.chks.add(new combo(line.id,holder.ckb));
        Log.d("holders","added combo"+chks.get(chks.size()-1).id);
        holder.question.setText(line.question);
        nbr=0;nbf=0;
        if (line.ans1.charAt(0)=='0')nbf++;else nbr++;
        if (line.ans2.charAt(0)=='0')nbf++;else nbr++;
        if (line.ans3.charAt(0)=='0')nbf++;else nbr++;
        holder.answers.setText(nbr+"|"+nbf);
    }
    List<combo> getChks() {
        Log.d(TAG, "getChks: "+chks.size());
        return chks;
    }
    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        TextView answers;
        CheckBox ckb;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            this.question = itemView.findViewById(R.id.question);
            this.question.setSelected(true);
            this.answers = itemView.findViewById(R.id.r_or_f);
            this.ckb = itemView.findViewById(R.id.cbox);
        }
    }
}
