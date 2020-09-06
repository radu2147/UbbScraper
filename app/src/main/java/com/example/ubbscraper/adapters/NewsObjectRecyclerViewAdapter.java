package com.example.ubbscraper.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ubbscraper.R;
import com.example.ubbscraper.Utils;
import com.example.ubbscraper.models.NewsObject;

import java.util.List;

public class NewsObjectRecyclerViewAdapter extends RecyclerView.Adapter<NewsObjectRecyclerViewAdapter.NewsObjectViewHolder> {

    private List<NewsObject> listObjects;


    public List<NewsObject> get_all(){
        return listObjects;
    }

    @NonNull
    @Override
    public NewsObjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        return new NewsObjectViewHolder(inf.inflate(R.layout.fragment_news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsObjectViewHolder holder, final int position) {
        holder.professor.setText(listObjects.get(getItemCount() - 1 - position).getProf());
        holder.message.setText(Utils.createNewsMessage(listObjects.get(getItemCount() - 1 - position).getMessage()));
        holder.date.setText(Utils.dateFormat(listObjects.get(getItemCount() - 1 - position).getDate()));
        holder.subject.setText(listObjects.get(getItemCount() - 1 - position).getSubj_name());
        holder.card.setCardBackgroundColor(listObjects.get(getItemCount() - 1 - position).getColor());
        holder.element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cv = new Intent(Intent.ACTION_VIEW, Uri.parse(listObjects.get(position).getUrl()));
                if(cv.resolveActivity(view.getContext().getPackageManager()) != null){
                    view.getContext().startActivity(cv);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listObjects == null ? 0 : listObjects.size();
    }

    public void setData(List<NewsObject> lst){
        listObjects = lst;
        notifyDataSetChanged();
    }

    public static class NewsObjectViewHolder extends RecyclerView.ViewHolder{

        TextView subject;
        TextView professor;
        TextView message;
        TextView date;
        CardView card;
        ConstraintLayout element;

        public NewsObjectViewHolder(@NonNull final View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subject_name);
            professor = itemView.findViewById(R.id.professor_text);
            message = itemView.findViewById(R.id.announcement_id);
            date = itemView.findViewById(R.id.date);
            card = itemView.findViewById(R.id.materie);
            element = itemView.findViewById(R.id.element);

        }
    }
}
