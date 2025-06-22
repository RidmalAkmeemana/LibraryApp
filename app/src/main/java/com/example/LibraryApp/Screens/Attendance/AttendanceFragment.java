package com.example.LibraryApp.Screens.Attendance;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.LibraryApp.Connection.RetroClient;
import LibraryApp.R;
import com.example.LibraryApp.Screens.Home.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceFragment extends Fragment {

    //DEBUG TAG
    private String TAG = "AttendanceFragment";

    View rootView;
    RecyclerView recyclerView;
    TextView noResultsTextView;
    List<AttendanceModel> attendanceModelList;

    //session for get values
    SharedPreferences sharedpreferences;
    String UserName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.attendance_fragment, container, false);

        //declare a session
        sharedpreferences = getContext().getSharedPreferences("user_details", MODE_PRIVATE);
        UserName = sharedpreferences.getString("st_username", "");

        //Init
        recyclerView = rootView.findViewById(R.id.recyclerView);
        noResultsTextView = rootView.findViewById(R.id.noResultsTextView);

        // Handle back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, new HomeFragment())
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

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

        attendanceModelList = new ArrayList<>();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userName", UserName);

        Call<List<AttendanceModel>> getAllAttendnace = RetroClient.getInstance().getApi().getAttendance(parameters);
        //Get All Available lectures in today
        getAllAttendnace.enqueue(new Callback<List<AttendanceModel>>() {
            @Override
            public void onResponse(Call<List<AttendanceModel>> call, Response<List<AttendanceModel>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                attendanceModelList = response.body();

                if (attendanceModelList.isEmpty()) {
                    noResultsTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noResultsTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    AttendanceListAdapter attendanceListAdapter = new AttendanceListAdapter(attendanceModelList, getContext());
                    recyclerView.setAdapter(attendanceListAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceModel>> call, Throwable t) {
                Log.e(TAG, "onFailure :" + t.getMessage() + "");
            }
        });

    }
}