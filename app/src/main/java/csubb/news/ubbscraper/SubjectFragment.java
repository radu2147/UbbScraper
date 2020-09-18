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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
    private ProgressBar bar;

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


    public void retrieveSubjectList(Context context){
        SubjectViewModel vm = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(SubjectViewModel.class);
        vm.get_live_data(context).observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
            @Override
            public void onChanged(List<Subject> subjects) {

                bar.setVisibility(View.VISIBLE);
                adapter.setData(subjects);
                bar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject, container, false);



        adapter = new SubjectRecyclerViewAdapter();
        RecyclerView recycle = view.findViewById(R.id.recyclerView);
        recycle.setAdapter(adapter);
        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        bar = view.findViewById(R.id.progressBar2);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        SubjectDatabase.newInstance(getContext()).getDao().delete(adapter.get_all().get(viewHolder.getAdapterPosition()));
                    }
                });

            }
        }).attachToRecyclerView(recycle);


        retrieveSubjectList(view.getContext());
        return view;
    }
}