package com.example.LibraryApp.Screens;

import static android.content.Context.MODE_PRIVATE;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.LibraryApp.Connection.RetroClient;
import LibraryApp.R;
import com.example.LibraryApp.Screens.Home.LectureModel;
import com.example.LibraryApp.Screens.Profile.ServerResponse;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubmitFragment extends Fragment  implements LocationListener {
    //Debug TAG Const
    private  static final String TAG= "SubmitFragment";
    public static TextView scan_code;

    View rootView;

    //Attributes
    TextView m_code;
    TextView lec_code;
    TextView m_des;
    TextView instructor_name;
    TextView m_time;
    Button submit_Button;
    Button scan_Button;


    //To get live location
    private double latitude=0;
    private double longitude=0;
    LocationManager locationManager;
    //Request Code
    private static  final  int REQEST_CODE= 123;

    //session for get values
    SharedPreferences sharedpreferences;
    String UserName;

    LectureModel lectureModel;

    public SubmitFragment(LectureModel lectureModel) {

        this.lectureModel=lectureModel;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.submit_fragment, container, false);

        //declare a session
        sharedpreferences= getContext().getSharedPreferences("user_details",MODE_PRIVATE);
        UserName=sharedpreferences.getString("st_username","");

        initiationView();

        liveLocationCal();

        LoadData();

        // Find the "Scan QR code" button by its ID
        Button scanButton = rootView.findViewById(R.id.scan_Button);

        // Set a click listener for the button
        scan_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),ScannerFragment.class));
            }
        });

        submit_Button.setOnClickListener(view -> {

            TextView lecCode = rootView.findViewById(R.id.lec_code);
            TextView scanCode = rootView.findViewById(R.id.scan_code);

            String lecCodeValue = lecCode.getText().toString();
            String scanCodeValue = scanCode.getText().toString();

            if (latitude == 0 || longitude == 0 ){
                dialogBox("Check You Are Allow The Permission & Wait 5s Until Tracking.....  ","Location Error",R.drawable.location_error);
                return;
            }

            else
            {
                if(lecCodeValue.equals(scanCodeValue))
                {
                    //
                    Location center = new Location("NSBM");//NSBM Location
                    center.setLatitude(6.823414701323104);//your coords of course
                    center.setLongitude(80.04173438872066);

                    Location current_location = new Location("");//provider name is unnecessary
                    current_location.setLatitude(latitude);//your coords of course
                    current_location.setLongitude(longitude);


                    float distanceInMeters = center.distanceTo(current_location);
                    boolean isWith500m = distanceInMeters < 500;

                    Log.d(TAG, isWith500m+"");

                    // callAPI();
                    Map<String,String> parameters= new HashMap<>();

                    parameters.put("userName",UserName);
                    parameters.put("lec_id",lectureModel.getLec_id()+"");
                    parameters.put("lat",latitude+"");
                    parameters.put("lng",longitude+"");
                    parameters.put("ispresent",isWith500m+"");

                    Call<ServerResponse> markAttendance = RetroClient.getInstance().getApi().markAttendance(parameters);

                    markAttendance.enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                            if(!response.isSuccessful()) return;

                            Log.e(TAG,"onResponse :"+response.body().getSuccess()+"");

                            if(response.body().getSuccess().equals("true")) {

                                openFragment(new ConfirmFragment());


                            }else{
                                dialogBox("You Are Already Submitted !! ","Attendance",R.drawable.waning);
                            }


                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            Log.e(TAG,"onFailure :"+t.getMessage()+"");
                        }
                    });
                }
                else
                {
                    dialogBox("Please Scan Valid QR Code !! ","Attendance",R.drawable.waning);
                }

            }

        });

        return rootView;
    }

    private void openFragment(Fragment fragment) {

        FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }
    private void LoadData() {

        lec_code.setText(""+lectureModel.getLec_id());
        m_code.setText(""+lectureModel.getM_code()+" | "+lectureModel.getM_name());
        m_des.setText(""+lectureModel.getM_description());
        instructor_name.setText(""+lectureModel.getInstructor_name());
        m_time.setText("Start :"+lectureModel.getTime_start()+" - "+lectureModel.getTime_end());

    }



    private void initiationView() {
        lec_code = rootView.findViewById(R.id.lec_code);
        m_code= rootView.findViewById(R.id.m_code);
        m_des= rootView.findViewById(R.id.m_des);
        instructor_name=rootView.findViewById(R.id.instructor_name);
        m_time= rootView.findViewById(R.id.m_time);
        submit_Button= rootView.findViewById(R.id.submit_Button);
        scan_Button= rootView.findViewById(R.id.scan_Button);
        scan_code= rootView.findViewById(R.id.scan_code);
    }


    private void liveLocationCal() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQEST_CODE );

        } else {
            detectCurrentLocation();
        }

    }

    private void detectCurrentLocation() {
        Toast.makeText(getContext(), "Getting Your Current Location", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.d(TAG, latitude +" :" +longitude);

        // m_des.setText(""+latitude);
        // findAddress();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(getContext(), "Please Turn On Location.", Toast.LENGTH_SHORT).show();
    }


    //Check RequestPermissionsResults
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                detectCurrentLocation();
            } else {
                Toast.makeText(getContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Alert Box
    public void dialogBox(String message, String title, int resID) {


        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(resID);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
        });

        alertDialog.show();


    }
}