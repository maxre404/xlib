package com.max.uiframe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.max.uiframe.R;
import com.max.uiframe.adapter.SampleAdapter;

import java.util.ArrayList;
import java.util.List;

public class SampleFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<String> data = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            data.add("Item " + i);
        }

        adapter = new SampleAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
