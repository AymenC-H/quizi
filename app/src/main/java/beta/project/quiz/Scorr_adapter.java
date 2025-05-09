package beta.project.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
public class Scorr_adapter extends RecyclerView.Adapter<Scorr_adapter.ScorrViewHolder> {

    private List<Scorr> scorrList;

    public Scorr_adapter(List<Scorr> scorrList) {
        this.scorrList = scorrList;
    }

    @Override
    public ScorrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_line, parent, false);
        return new ScorrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScorrViewHolder holder, int position) {
        Scorr line = scorrList.get(position);
        holder.name.setText(line.namec);
        holder.nbp.setText(Integer.toString(line.nb_passes));
        holder.avg.setText(Math.round(line.avg_value*100)+"%");
    }

    @Override
    public int getItemCount() {
        return scorrList.size();
    }

    static class ScorrViewHolder extends RecyclerView.ViewHolder {
        TextView name,nbp,avg;
        public ScorrViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name_field);
            this.nbp = itemView.findViewById(R.id.passes_field);
            this.avg = itemView.findViewById(R.id.values_field);
        }
    }
}

