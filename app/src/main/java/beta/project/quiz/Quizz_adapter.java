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

public class Quizz_adapter extends RecyclerView.Adapter<Quizz_adapter.QuizzViewHolder> {
    static class combo{
        String idn;
        CheckBox ckb;
        public combo(String idn,CheckBox ckb){
            this.idn=idn;
            this.ckb=ckb;
        }}
    private final List<Quizz> quizzList;
    List<combo> cheks= new ArrayList<>();

    public Quizz_adapter(List<Quizz> quizzList) {
        this.quizzList = quizzList;
        this.cheks.clear();
    }

    @Override
    public QuizzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quizz_line, parent, false);
        return new QuizzViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizzViewHolder holder, int position) {
        Quizz line = quizzList.get(position);
        cheks.add(new combo(line.nameq,holder.ckb));
        Log.i("quizz: ", "onBindViewHolder: "+cheks.size());
        holder.quizn.setText(line.nameq);
        holder.nbq.setText(line.ids_questions.length+"");
        holder.nbp.setText(line.nb_passes+"");
    }

    @Override
    public int getItemCount() {
        return quizzList.size();
    }

    static class QuizzViewHolder extends RecyclerView.ViewHolder {
        TextView quizn,nbq,nbp;
        CheckBox ckb;
        public QuizzViewHolder(View itemView) {
            super(itemView);
            this.quizn = itemView.findViewById(R.id.question);
            this.nbq = itemView.findViewById(R.id.nbquests);
            this.nbp = itemView.findViewById(R.id.nbpass);
            this.ckb = itemView.findViewById(R.id.checkBox3);
        }
    }
}
