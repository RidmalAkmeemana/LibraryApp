package com.example.LibraryApp.Screens;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import LibraryApp.R;
import com.example.LibraryApp.Screens.Home.HomeFragment;


public class ConfirmFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.confirm_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find your "Back To Home" button by its ID
        Button backToHomeButton = view.findViewById(R.id.backToHomeButton);

        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use FragmentManager to clear the back stack
                requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                // Replace with the name of your HomeFragment class
                HomeFragment homeFragment = new HomeFragment();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
            }
        });
    }
}