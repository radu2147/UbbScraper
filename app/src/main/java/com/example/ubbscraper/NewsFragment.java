package com.example.ubbscraper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ubbscraper.adapters.NewsObjectRecyclerViewAdapter;
import com.example.ubbscraper.models.NewsObject;
import com.example.ubbscraper.repository.NewsDatabase;
import com.example.ubbscraper.repository.NewsObjectViewModel;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    private NewsObjectRecyclerViewAdapter adapter;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsFragment.
     */

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.about){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view =  inflater.inflate(R.layout.fragment_news, container, false);

        setHasOptionsMenu(true);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
        adapter = new NewsObjectRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final SwipeRefreshLayout lay = view.findViewById(R.id.swipe_to_refresh);
        lay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Constraints con = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
                OneTimeWorkRequest one = new OneTimeWorkRequest.Builder(NewsWorker.class).setConstraints(con).build();
                WorkManager man = WorkManager.getInstance(Objects.requireNonNull(getContext()));
                man.enqueue(one);
                lay.setRefreshing(false);
            }
        });

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
                        NewsDatabase.getInstance(getContext()).getDao().delete(adapter.get_all().get(adapter.getItemCount() - 1 - viewHolder.getAdapterPosition()));
                    }
                });

            }
        }).attachToRecyclerView(recyclerView);

        retrieveObjects();
        return view;
    }

    private void retrieveObjects() {
        NewsObjectViewModel viewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(NewsObjectViewModel.class);
        viewModel.getInstance(getContext()).observe(getActivity(), new Observer<List<NewsObject>>() {
            @Override
            public void onChanged(List<NewsObject> newsObjects) {
                adapter.setData(newsObjects);
            }
        });
    }
}