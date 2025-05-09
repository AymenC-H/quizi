package beta.project.quiz;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Childd_adapter extends RecyclerView.Adapter<Childd_adapter.ChilddViewHolder> {
    static class combo{
        String idn;
        CheckBox ckb;
        public combo(String idn,CheckBox ckb){
            this.idn=idn;
            this.ckb=ckb;
        }
    }
    private List<Childd> childdList;

    public Childd_adapter(List<Childd> childdList) {
        this.childdList = childdList;
    }
    List<combo> ccheks=new ArrayList<>();

    @Override
    public ChilddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_line, parent, false);
        return new ChilddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChilddViewHolder holder, int position) {
        Childd line = childdList.get(position);
        ccheks.add(new combo(line.namec,holder.ckb));
        Log.i("childd: ", "onBindViewHolder: "+ccheks.size());
        holder.name.setText(line.namec);
        Date date = new Date(line.date);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = newFormat.format(date);
        holder.date.setText(formatedDate);
    }

    @Override
    public int getItemCount() {
        return childdList.size();
    }

    static class ChilddViewHolder extends RecyclerView.ViewHolder {
        TextView name,date;
        CheckBox ckb;
        public ChilddViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name_field);
            this.date = itemView.findViewById(R.id.date_field);
            this.ckb = itemView.findViewById(R.id.cbox);
        }
    }
}
