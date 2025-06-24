package com.example.LibraryApp.Screens.Home;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import LibraryApp.R;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment, container, false);

        RecyclerView managementRecyclerView = rootView.findViewById(R.id.managementRecyclerView);

        // Grid Items
        List<String> managementList = Arrays.asList(
                "Books",
                "Publishers",
                "Branches",
                "Members",
                "Authors",
                "Book Copies",
                "Book Loans"
        );

        ManagementAdapter managementAdapter = new ManagementAdapter(getContext(), managementList);
        managementRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2x2 Grid
        managementRecyclerView.setAdapter(managementAdapter);

        return rootView;
    }
}
