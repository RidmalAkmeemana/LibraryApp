package com.example.LibraryApp.Screens.Home;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.LibraryApp.Connection.RetroClient;
import LibraryApp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    //DEBUG TAG
    private String TAG = "HomeFragment";

    View rootView;
    RecyclerView recyclerView;
    TextView noResultsTextView;
    List<LectureModel> lectureModelList;

    //session for get values
    SharedPreferences sharedpreferences;
    String UserName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.home_fragment, container, false);

        //declare a session
        sharedpreferences = getContext().getSharedPreferences("user_details", MODE_PRIVATE);
        UserName = sharedpreferences.getString("st_username", "");

        //Init
        recyclerView = rootView.findViewById(R.id.recyclerView);
        noResultsTextView = rootView.findViewById(R.id.noResultsTextView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            loadLectures();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        loadLectures();
    }

    private void loadLectures() {

        lectureModelList = new ArrayList<>();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userName", UserName);

        Call<List<LectureModel>> getAllLectures = RetroClient.getInstance().getApi().getLectures(parameters);
        //Get All Available lectures in today
        getAllLectures.enqueue(new Callback<List<LectureModel>>() {
            @Override
            public void onResponse(Call<List<LectureModel>> call, Response<List<LectureModel>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                lectureModelList = response.body();

                if (lectureModelList.isEmpty()) {
                    noResultsTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noResultsTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    LectureListAdapter lectureListAdapter = new LectureListAdapter(lectureModelList, getContext());
                    recyclerView.setAdapter(lectureListAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onFailure(Call<List<LectureModel>> call, Throwable t) {
                Log.e(TAG, "onFailure :" + t.getMessage() + "");
            }
        });

    }
}