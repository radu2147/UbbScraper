package csubb.news.ubbscraper.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import csubb.news.ubbscraper.AddSubjectsActivity;
import csubb.news.ubbscraper.R;
import csubb.news.ubbscraper.models.Subject;

import java.util.List;

public class SubjectRecyclerViewAdapter extends RecyclerView.Adapter<SubjectRecyclerViewAdapter.SubjectViewHolder> {

    List<Subject> list;
    int[] colorList;

    public SubjectRecyclerViewAdapter() {
        colorList = new int[20];
        colorList[0] = Color.RED;
        colorList[1] = R.color.colorPrimary;
        colorList[2] = Color.GREEN;
        colorList[3] = Color.MAGENTA;
    }

    public void setData(List<Subject> obj){
        list = obj;
        notifyDataSetChanged();
    }

    public List<Subject> get_all(){
        return list;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubjectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_fragment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, final int position) {
        holder.professor.setText(list.get(position).getProf_name());
        holder.subject.setText(list.get(position).getSubj_name());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri html = Uri.parse(list.get(position).getUrl());
                Intent webpage = new Intent(Intent.ACTION_VIEW, html);
                if(webpage.resolveActivity(view.getContext().getPackageManager()) != null){
                    view.getContext().startActivity(webpage);
                }
            }
        });
        holder.card.setCardBackgroundColor(list.get(position).getColor());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder{

        TextView professor;
        TextView subject;
        Button button;
        CardView card;

        public SubjectViewHolder(@NonNull final View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subject_name);
            professor = itemView.findViewById(R.id.professor);
            button = itemView.findViewById(R.id.web_intent);
            card = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent x = new Intent(itemView.getContext(), AddSubjectsActivity.class);
                    x.putExtra("editable", true);
                    x.putExtra("Profesor", professor.getText().toString());
                    x.putExtra("Subject", subject.getText().toString());
                    x.putExtra("Url",list.get(getAdapterPosition()).getUrl());
                    x.putExtra("Id",list.get(getAdapterPosition()).getId());
                    itemView.getContext().startActivity(x);
                }
            });
        }
    }
}
