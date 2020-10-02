package csubb.news.ubbscraper.adapters;

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

import csubb.news.ubbscraper.R;
import csubb.news.ubbscraper.Utils;
import csubb.news.ubbscraper.models.NewsObject;
import csubb.news.ubbscraper.repository.NewsDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    public void onBindViewHolder(@NonNull final NewsObjectViewHolder holder, final int position) {
        holder.professor.setText(listObjects.get(getItemCount() - 1 - position).getProf());
        holder.message.setText(Utils.createNewsMessage(listObjects.get(getItemCount() - 1 - position).getMessage()));
        holder.date.setText(Utils.dateFormat(listObjects.get(getItemCount() - 1 - position).getDate()));
        holder.subject.setText(listObjects.get(getItemCount() - 1 - position).getSubj_name());
        holder.card.setCardBackgroundColor(listObjects.get(getItemCount() - 1 - position).getColor());
        holder.element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent cv = new Intent(Intent.ACTION_VIEW, Uri.parse(listObjects.get(getItemCount() - 1 - position).getUrl()));
                if(cv.resolveActivity(view.getContext().getPackageManager()) != null){
                    view.getContext().startActivity(cv);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            NewsDatabase.getInstance(view.getContext()).getDao().delete(listObjects.get(getItemCount() - 1 - position));
                        }
                    });
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
