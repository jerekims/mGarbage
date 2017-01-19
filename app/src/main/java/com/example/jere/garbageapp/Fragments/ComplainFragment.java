package com.example.jere.garbageapp.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jere.garbageapp.R;
import com.example.jere.garbageapp.libraries.Utility;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComplainFragment extends BaseFragment implements View.OnClickListener {
    private EditText et_description, et_wtype, et_mlocation;
    private AppCompatButton btncomplain,btnImage;
    private ImageView imageView;
    private ProgressBar progress;
    private SharedPreferences pref;
    private MaterialBetterSpinner et_wastetype;
    private String userChoosenTask;

    Bitmap thumbnail;

    public ComplainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_complain, container, false);
        setinit(view);
        // Inflate the layout for this fragment
        return view;
    }

    public void setinit(View view) {
        et_description=(EditText)view.findViewById(R.id.fragment_complain_description);
        et_description.setHint("Complain description");
        et_wastetype=(MaterialBetterSpinner)view.findViewById(R.id.fragment_complain_wastetype);
        et_description.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                if(hasFocus)
                    et_description.setHint("");
                else
                    et_description.setHint("Complain description");
            }
        });
        imageView=(ImageView)view.findViewById(R.id.fragment_complain_ivImage);
        btnImage=(AppCompatButton)view.findViewById(R.id.fragment_complain_image);
        btncomplain=(AppCompatButton)view.findViewById(R.id.fragment_complain_btn);
        btnImage.setOnClickListener(this);
        btncomplain.setOnClickListener(this);

        List<String> mywaste = new ArrayList<>();
        mywaste.add("Biodegradable");
        mywaste.add("Plastics");
        mywaste.add("Metaallics");

        ArrayAdapter<String> wasteadapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mywaste);
        wasteadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_wastetype.setAdapter(wasteadapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_complain_image:
                selectImage();
                break;
            case R.id.fragment_complain_btn:
                reportComplain();
                break;
        }
    }

    private void selectImage(){
        final CharSequence[] items = { "Take Photo", "Choose from gallery",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getContext());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from gallery")) {
                    userChoosenTask="Choose from gallery";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    protected void cameraIntent()
    {
       // Log.d("Taking an Image","Image showing");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }

    protected void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from gallery"));
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1)
                onSelectFromGalleryResult(data);
            else if (requestCode == 0)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        thumbnail=null;
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Log.d("Image from gallery","Image showing");
        imageView.setImageBitmap(thumbnail);
    }

    public void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Image from gallery","Image from gallery");
        imageView.setImageBitmap(thumbnail);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


       public void reportComplain(){
        if (!validate()) {
            reportSubmsisionFailed();
            return;
        }
        else if (!isNetworkConnected()){
            Snackbar.make(getView(),"No Network Connection.Turn on Your Wifi", Snackbar.LENGTH_LONG).show();
        }else {

                String desc= et_description.getText().toString();
                String wtype= et_wastetype.getText().toString();
                String image=getStringImage(thumbnail);
                String function = "complain";
                String phone="0702179556";

                if(image.length()< 1){
                    Toast.makeText(getActivity(),"An Image is Missing",Toast.LENGTH_LONG).show();
                    // Snackbar.make(getActivity(),"An Image is Missing",Snackbar.LENGTH_LONG).show();
                }
                else {
                        btncomplain.setEnabled(false);
                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Submitting Complain...");
                        progressDialog.show();

                        BackgroundTasks backgroundTasks =new BackgroundTasks(getContext());
                        backgroundTasks.execute(function,phone,desc,wtype,image);
                        Log.d("Complain","Submitted complain");

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        reportSubmissionSucess();
                                        progressDialog.dismiss();
                                    }
                                }, 3000);
                }
            }

    }

    public void reportSubmsisionFailed() {
        Snackbar.make(getView(), "Complain Submission failed!", Snackbar.LENGTH_LONG).show();
    }

    public void reportSubmissionSucess() {
        btncomplain.setEnabled(true);
        et_description.setText("");
        imageView.setImageDrawable(null);
        Snackbar.make(getView(),"Complain Submission successful!",Snackbar.LENGTH_SHORT).show();
        toHome();

    }

    public boolean validate(){
        boolean valid =true;
        String description= et_description.getText().toString();
        String wtype=et_wastetype.getText().toString();
        if (description.isEmpty() || description.length() < 3) {
            et_description.setError("at least 3 characters");
            valid = false;
        } else {
            et_description.setError(null);
        }
        if(wtype.isEmpty()){
            et_wastetype.setError("Select Waste Type");
            valid=false;
        }else {
            et_wastetype.setError("");
        }

        return valid;
    }
}