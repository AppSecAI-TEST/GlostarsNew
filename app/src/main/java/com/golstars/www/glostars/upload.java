package com.golstars.www.glostars;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.github.clans.fab.FloatingActionMenu;
import com.loopj.android.http.*;
import org.json.*;

public class upload extends AppCompatActivity {


    //===========================FABS=========================================

    Animation fab_hide;
    Animation fab_show;
    Animation rotate_clockwise;
    Animation rotate_anticlockwise;

    boolean isOpen = false;


    com.github.clans.fab.FloatingActionButton cameraFAB;
    com.github.clans.fab.FloatingActionButton competitionFAB;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton notificationFAB;
    com.github.clans.fab.FloatingActionButton homeFAB;

    FloatingActionMenu menuDown;


    TextView homebadge;
    TextView notificationbadge;
    TextView profilebadge;
    TextView camerabadge;
    TextView mainbadge;
    TextView competitionbadge;
    //===================================================================

    ImageView image;
    RadioButton publicpost;
    RadioButton competition;
    RadioButton followerspost;
    EditText description;
    ImageView fbshare;
    ImageView vkshare;
    ImageView twshare;
    Button cancel;
    Button submit;
    File file;
    private Bitmap bm;
    private MyUser mUser;

    private static final MediaType JSONType = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    String baseURL = "http://www.glostars.com/";

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, SELECT_FILE_KITKAT = 1;
    private String userChoosenTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);
        menuDown.setClosedOnTouchOutside(true);


        image = (ImageView)findViewById(R.id.imageUpload);
        publicpost = (RadioButton)findViewById(R.id.radiopublic);
        competition = (RadioButton)findViewById(R.id.radiocompetition);
        followerspost = (RadioButton)findViewById(R.id.radiofollowers);
        description = (EditText)findViewById(R.id.description);
        fbshare = (ImageView)findViewById(R.id.fbshare);
        vkshare = (ImageView)findViewById(R.id.vkshare);
        twshare = (ImageView)findViewById(R.id.twshare);
        cancel = (Button)findViewById(R.id.cancelbutton);
        submit = (Button)findViewById(R.id.submitbutton);


        submit.setTransformationMethod(null);
        cancel.setTransformationMethod(null);


        cameraFAB =(com.github.clans.fab.FloatingActionButton)findViewById(R.id.cameraFAB);
        competitionFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.competitionFAB);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        notificationFAB = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.notificationFAB);
        homeFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.homeFAB);

        //=============Notification Badges===============================================
//        homebadge = (TextView)findViewById(R.id.homebadge);
//        notificationbadge = (TextView)findViewById(R.id.notificationbadge);
//        profilebadge = (TextView)findViewById(R.id.profilebadge);
//        camerabadge = (TextView)findViewById(R.id.uploadbadge);
//        mainbadge = (TextView)findViewById(R.id.mainbadge);
//        competitionbadge = (TextView)findViewById(R.id.competitionbadge);




        fab_show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_show);
        fab_hide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_hide);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-Light.ttf");
        submit.setTypeface(type);
        cancel.setTypeface(type);
        description.setTypeface(type);
        competition.setTypeface(type);
        publicpost.setTypeface(type);
        followerspost.setTypeface(type);


        notificationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(upload.this, notification.class));
            }
        });

        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(upload.this, user_profile.class));
            }
        });


        cameraFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(upload.this, upload.class));
            }
        });


        competitionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(upload.this, competitionAll.class));
            }
        });

        //========================= HANDLING PICTURE ===============================================




        //file = null;
        image.setVisibility(View.INVISIBLE);
        selectImage();

        /*
        Bundle filePath = null;
        Bundle bundle = this.getIntent().getExtras();
        if(filePath != null){
            bm = bundle.getParcelable("PREVIEW_PICTURE");
            file = (File)getIntent().getExtras().get("FILEPATH");
            //bm = BitmapFactory.decodeFile(file.getPath());
            image.setImageBitmap(bm);
            System.out.println("file is:");
            System.out.println(file);

        } */

        mUser = MyUser.getmUser(getApplicationContext());
        mUser.setContext(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String privacy = "";
                if(publicpost.isChecked()){
                    privacy = "public";
                } else if(competition.isChecked()){
                    privacy = "competition";
                } else if(followerspost.isChecked()){
                    privacy = "followers";
                }

                if((bm != null) && (privacy != "")){
                    uploadPhoto(description.getText().toString(), privacy, competition.isChecked(), file, bm);
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    public static String getBase64ForBitmap(File file) {

        int size = (int) file.length();

        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private void requestDevicePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }
    }

    private void prepareUpload(String descrip, String privacy, Boolean isCompeting, String token, Bitmap bm) {

        String base64 = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            base64 = "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT);


            JSONObject msg = new JSONObject();
            msg.put("Description", descrip);
            msg.put("IsCompeting", isCompeting.toString());
            msg.put("Privacy", privacy);
            msg.put("ImageDataUri", base64);

            description.setText(msg.getString("ImageDataUri"));

            //System.out.println(msg.getString("ImageDataUri"));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void uploadPhoto(String descrip, String privacy, Boolean isCompeting, File file, Bitmap bm){


        String url = "http://www.glostars.com/home/upload";
        AsyncHttpClient client = new AsyncHttpClient();
        String base64 = null;
        String imageData = null;
        try{
            //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            //byte[] bytes = byteArrayOutputStream.toByteArray();
            //imageData = Base64.encodeToString(bytes, Base64.DEFAULT);


            //KeyStore trustore = KeyStore.getInstance(KeyStore.getDefaultType());
            //trustore.load(null, null);
            //MySSLSocketFactory sf = new MySSLSocketFactory(trustore);
            //sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            //client.setSSLSocketFactory(sf);

        } catch (Exception e) {}
        client.addHeader("Authorization", "Bearer " + mUser.getToken());
        Log.d("UPLOAD", "token: " + mUser.getToken());
        RequestParams requestParams = new RequestParams();
        requestParams.put("Description", descrip);
        requestParams.put("IsCompeting", isCompeting);
        requestParams.put("Privacy", privacy);
        //requestParams.put("ImageDataUri", "data:image/jpeg;base64,"+imageData);
        try {
            System.out.println("file to load is: " +  file);
            requestParams.put("file", file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(getApplicationContext(), url, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println(errorResponse);
            }
        });

    }


    //-------------CAMERA AND GALLERY CALLERS------------------------------------------
    /**
     *  In this method we'll create a dialog box with three options
     *  for either camera, gallery or cancelling actions
     */

    private void selectImage(){
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(upload.this);
        builder.setTitle("Select Source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                boolean result = Utility.checkPermission(upload.this);

                if(items[item].equals("Take Photo")){
                    userChoosenTask = "Take Photo";
                    if(result)
                        cameraIntent();
                } else if(items[item].equals("Choose from Library")){
                    userChoosenTask = "Choose from Library";
                    if(result)
                        galleryIntent();
                } else if(items[item].equals("Cancel")){
                    dialogInterface.dismiss();
                    finish();
                }
            }
        });
        builder.show();
    }


    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,  REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        /*
        if (Build.VERSION.SDK_INT <19){
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");
            startActivityForResult(intent, SELECT_FILE_KITKAT);
        }*/

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
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
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void onSelectFromGalleryResult(Intent data) {
        File imageFile = null;
        if (data != null) {
            try {




                Uri selectedImageUri = data.getData();

                //OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                //MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                //DEBUG PURPOSE - you can delete this if you want
                if(selectedImagePath!=null){
                    System.out.println(selectedImagePath);
                    imageFile = new File(selectedImagePath);
                    file = imageFile;
                }
                else System.out.println("selectedImagePath is null");
                if(filemanagerstring!=null){
                    System.out.println(filemanagerstring);
                    imageFile = new File(filemanagerstring);
                    file = imageFile;
                    System.out.println(imageFile);
                }

                else System.out.println("filemanagerstring is null");

                //imageFile = new File(imagePath);
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                image.setImageBitmap(bm);
                image.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        /*
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        Bundle filePath = new Bundle();

        bundle.putParcelable("PREVIEW_PICTURE", bm);
        filePath.putSerializable("FILEPATH", imageFile);
        intent.putExtras(bundle);
        intent.putExtras(filePath);
        intent.setClass(this, upload.class);
        startActivity(intent);
        */
        //ivImage.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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


        bm = thumbnail;
        image.setImageBitmap(bm);
        image.setVisibility(View.VISIBLE);
        file = destination;
        /*
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        Bundle filePath = new Bundle();

        bundle.putParcelable("PREVIEW_PICTURE", thumbnail);
        filePath.putSerializable("FILEPATH", destination);
        intent.putExtras(bundle);
        intent.putExtras(filePath);
        intent.setClass(this, upload.class);
        startActivity(intent);
        */
        //ivImage.setImageBitmap(thumbnail);
    }


    //UPDATED!
    public String getPath(Uri uri) {

        /*
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
        */

        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null , null , null, null);

        try{
            if(cursor != null && cursor.moveToFirst()){

                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                return displayName;
            }
        } finally {
            cursor.close();
        }

        return  null;

    }



    //-------------/CAMERA AND GALLERY CALLERS<end>------------------------------------------


}
