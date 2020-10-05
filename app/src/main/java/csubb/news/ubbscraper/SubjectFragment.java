package csubb.news.ubbscraper;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import csubb.news.ubbscraper.adapters.SubjectRecyclerViewAdapter;
import csubb.news.ubbscraper.models.Subject;
import csubb.news.ubbscraper.repository.SubjectDatabase;
import csubb.news.ubbscraper.repository.SubjectViewModel;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectFragment extends Fragment {


    private SubjectRecyclerViewAdapter adapter;
    private TextView message;

    public SubjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment SubjectFragment.
     */
    public static SubjectFragment newInstance() {
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /*
    Sets the observer onChanged method for the database LiveData field
     */
    public void retrieveSubjectList(Context context){
        SubjectViewModel vm = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(SubjectViewModel.class);
        vm.get_live_data(context).observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
            @Override
            public void onChanged(List<Subject> subjects) {
                adapter.setData(subjects);
                setMessageVisibility(adapter);
            }
        });
    }

    /*
    Sets the recycler view empty case message visible if empty and invisible if there are elements
     */
    private void setMessageVisibility(SubjectRecyclerViewAdapter adapter){
        if(adapter.getItemCount() > 0){
            message.setVisibility(View.INVISIBLE);
        }
        else{
            message.setVisibility(View.VISIBLE);
        }
    }

    /*
        Sets the recycler view adapter and layout manager
     */
    private RecyclerView setRecyclerViewSettings(View view){
        adapter = new SubjectRecyclerViewAdapter();
        RecyclerView recycle = view.findViewById(R.id.recyclerView);
        recycle.setAdapter(adapter);
        recycle.setLayoutManager(new LinearLayoutManager(getContext()));

        return recycle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject, container, false);

        message = view.findViewById(R.id.no_subject_message);
        RecyclerView recycle = setRecyclerViewSettings(view);
        retrieveSubjectList(view.getContext());
        setMessageVisibility((SubjectRecyclerViewAdapter) recycle.getAdapter());

        return view;
    }
}