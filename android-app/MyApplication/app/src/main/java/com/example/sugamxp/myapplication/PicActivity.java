package com.example.sugamxp.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import spencerstudios.com.bungeelib.Bungee;

public class PicActivity extends AppCompatActivity implements IPickResult {
    private static final String TAG = "PicActivity";
    private Button btn_choose_image, btn_upload;
    private EditText et_height;
    private ImageView imageView;
    View view;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    String fname;
    Location loc;
    private LocationManager locationManager;
    Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.pb);
        btn_choose_image = findViewById(R.id.btn_choose_image);
        btn_upload = findViewById(R.id.btn_upload);
        et_height = findViewById(R.id.editText);
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();


        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = v;
                PickImageDialog.build(new PickSetup().setPickTypes(EPickType.CAMERA,EPickType.GALLERY)).show(PicActivity.this);
                Log.d(TAG, "onClick: btn_choose_image");
            }
        });


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_upload.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                Date date = new Date();
                //This method returns the time in millis
                String timeMilli = String.valueOf(date.getTime());
                fname = timeMilli;
                final StorageReference filepath = mStorageRef.child("Photos").child(timeMilli);
                String key = databaseReference.push().getKey();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String strDate = dateFormat.format(date);
                databaseReference.child("uploadDetails").child(auth.getUid()).child(key).setValue(new UploadInfo(timeMilli, key,strDate));

                filepath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toasty.success(PicActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        btn_upload.setVisibility(View.VISIBLE);
                        final OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(30, TimeUnit.SECONDS)
                                .writeTimeout(30, TimeUnit.SECONDS)
                                .readTimeout(30, TimeUnit.SECONDS)
                                .build();

                        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://192.168.43.49:5000/getjson/" + fname).newBuilder();
                        urlBuilder.addQueryParameter("uid",auth.getUid());
                        urlBuilder.addQueryParameter("height", et_height.getText().toString());
                        urlBuilder.addQueryParameter("name", auth.getCurrentUser().getDisplayName());
                        urlBuilder.addQueryParameter("email", auth.getCurrentUser().getEmail());

                        et_height.setText("");
                        String url = urlBuilder.build().toString();
                        final Request request = new Request.Builder()
                                .url(url)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }
                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    final String myres = response.body().string();
                                    PicActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Do something'
                                            Toasty.success(PicActivity.this, myres,Toasty.LENGTH_LONG).show();
                                        }
                                    });
                                }else{
                                    Toasty.error(PicActivity.this,"Server Offline", Toasty.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PicActivity.this, "Upload Failed!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            fileUri = r.getUri();
//            Toasty.info(PicActivity.this, fileUri.toString(), Toasty.LENGTH_LONG).show();
            imageView.setImageURI(fileUri);
            Log.d("URI", "onPickResult: " + r.getUri().toString());
        } else {
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.zoom(this);

    }
}
