package com.example.LibraryApp.Screens.Profile;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.LibraryApp.Connection.RetroClient;
import LibraryApp.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    //DEBUG TAG
    private ProgressDialog progressDialog;
    private  String  TAG="ProfileFragment";

    View rootView;
    ImageView userImage;
    ImageView edit_icon;


    private static final int PReqCode = 2 ;
    private static final int REQ_CODE = 1000;

    Uri imageUri;

    //session for get values
    SharedPreferences sharedpreferences;
    String UserName;

    EditText studentID;
    EditText studentName;
    EditText studentDegree;
    EditText studentBatch;

    Bitmap imageBitmap;
    ProgressDialog pd;

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        initiationView();

        //declare a session
        sharedpreferences= getContext().getSharedPreferences("user_details",MODE_PRIVATE);
        UserName=sharedpreferences.getString("st_username","");
        edit_icon.setOnClickListener(v -> { checkAndRequestForPermission(); });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadUserData();
    }

    @Override
    public void onPause() {
        super.onPause();
        LoadUserData();
    }

    private void LoadUserData() {

        Map<String,String> parameters= new HashMap<>();
        parameters.put("userName",UserName);

        Call<UserModel> getProfileDetails= RetroClient.getInstance().getApi().getProfileDetails(parameters);


        getProfileDetails.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                if (!response.isSuccessful()) {
                    hideProgressDialog();
                    return;
                }

                studentID.setText(response.body().getSt_no()+"");
                studentName.setText(response.body().getSt_name()+"");
                studentDegree.setText(response.body().getDegree()+"");
                studentBatch.setText(response.body().getBatch()+"");

                // Load the userImage with Picasso and use a callback
                Picasso.get()
                        .load("" + response.body().getImgPath() + "")
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(userImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                // Image loaded successfully, hide the progress dialog
                                hideProgressDialog();
                            }

                            @Override
                            public void onError(Exception e) {
                                // Handle errors here if needed
                                hideProgressDialog();
                            }
                        });

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

                Log.e(TAG,"onFailure :"+t.getMessage()+"");

            }
        });
    }

    private void initiationView() {
        pd = new ProgressDialog(getContext());
        userImage=rootView.findViewById(R.id.userImage);
        edit_icon=rootView.findViewById(R.id.edit_icon);
        studentBatch=rootView.findViewById(R.id.studentBatch);
        studentDegree=rootView.findViewById(R.id.studentDegree);
        studentName=rootView.findViewById(R.id.studentName);
        studentID=rootView.findViewById(R.id.studentID);


    }


    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(getContext(),"Please accept for required permission", Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }


            return;
        }else{
            Intent OpenGalleryIntent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(OpenGalleryIntent,REQ_CODE);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {


                Uri path = data.getData();
                try {
                    imageBitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                    userImage.setImageBitmap(imageBitmap);
                    Log.e("Image path",imageBitmap+"");

                    UpLoadingImage();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    private void UpLoadingImage() {

        pd.setMessage("Uploading loading");
        pd.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,75, byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        String encodedImage =  Base64.encodeToString(imageInByte,Base64.DEFAULT);

        Map<String,String> parameters= new HashMap<>();

        parameters.put("userName",UserName);
        parameters.put("image",encodedImage);

        Call<ServerResponse> profile_image= RetroClient.getInstance().getApi().updateProfile(parameters);

        profile_image.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if(response.body().getSuccess().equals("true")){
                    Toast.makeText(getContext(),"Image Success   !",Toast.LENGTH_SHORT).show();

                    LoadUserData();

                    pd.dismiss();

                }else{
                    pd.dismiss();
                    Toast.makeText(getContext(),"Image Failed  !",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(TAG,"onFailure :"+t.getMessage()+"");
            }
        });
    }
}