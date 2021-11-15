package com.example.mobileauthentication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileauthentication.databinding.ActivityMainBinding;
import com.example.mobileauthentication.databinding.ActivityProfileBinding;
import com.example.mobileauthentication.utils.Constants;
import com.example.mobileauthentication.utils.Util;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class ProfileActivity extends AppCompatActivity {

    String str_name = "", str_email = "", str_mobNo = "";

    String shar_name = "", shar_name_email = "", shar_name_mobNo = "";
    // use upload gallery image :
    Uri FilePathUri;
    // use upload camera image :
    byte[] camera_data;
    Bitmap bm = null;

    SharedPreferences prefs;
    ActivityProfileBinding profileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_profile);

        profileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        prefs = getApplicationContext().getSharedPreferences(Constants.USER_PREF,
                Context.MODE_PRIVATE);

        // getting our root layout in our view.
        View view = profileBinding.getRoot();

        // below line is to set
        // Content view for our layout.
        setContentView(view);


        profileBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_user();
            }
        });

        profileBinding.btnUploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean result = Util.checkPermission(ProfileActivity.this);

                if (result)
                    selectImage();
            }
        });

    }

    private void validate_user() {

        str_name = profileBinding.edtName.getText().toString().trim();
        str_email = profileBinding.edtEmail.getText().toString().trim();
        str_mobNo = profileBinding.edtMobNo.getText().toString().trim();


        if (str_name.equals("")) {
            Util.showToastMessage(this, "Enter Full Name");
        } else if (str_email.equals("")) {
            Util.showToastMessage(this, "Enter Email id");
        } else if (str_mobNo.equals("")) {
            Util.showToastMessage(this, "Enter Mobile Number");
        } else if (!Util.isValidEmailId(str_email)) {
            Toast.makeText(getApplicationContext(), "InValid Email Address", Toast.LENGTH_SHORT).show();
        } else if (!Util.isValidMobile(str_mobNo)) {
            Toast.makeText(getApplicationContext(), "InValid Mobile number", Toast.LENGTH_SHORT).show();
        } else if (profileBinding.imgProfilePic.getDrawable() == null) {
            Util.showToastMessage(this, "Upload profile pic");
        } else {

            saveData();

        }

    }

    private void saveData() {

        Util.showToastMessage(this, "Save data Successfully");

        profileBinding.edtName.setText("");
        profileBinding.edtEmail.setText("");
        profileBinding.edtMobNo.setText("");
        profileBinding.imgProfilePic.setImageResource(0);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.NAME, str_name);
        if (camera_data != null) {
            editor.putString(Constants.PRO_IMAGE, camera_data.toString());
        }else
        {
            editor.putString(Constants.PRO_IMAGE, bm.toString());
        }
        editor.putString(Constants.PHONE, str_mobNo);
        editor.putString(Constants.EMAIL, str_email);
        editor.apply();

        startActivity(new Intent(this,ProductsActivity.class));
        finish();

       // getData();

    }

    private void getData() {
        shar_name = prefs.getString(Constants.NAME, "");
        prefs.getString(Constants.PRO_IMAGE, "");
        shar_name_mobNo = prefs.getString(Constants.PHONE, "");
        shar_name_email = prefs.getString(Constants.EMAIL, "");

        Util.showToastMessage(this, shar_name+" "+shar_name_mobNo+" "+shar_name_email);
    }

    // image upload :
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                onCaptureImageResult(data);
            } else if (requestCode == 2) {

                FilePathUri = data.getData();

                onSelectFromGalleryResult(data);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), FilePathUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        profileBinding.imgProfilePic.setImageBitmap(bm);

    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
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

        // upload on firebase camera image :
        camera_data = bytes.toByteArray();

        profileBinding.imgProfilePic.setImageBitmap(thumbnail);

    }
}
