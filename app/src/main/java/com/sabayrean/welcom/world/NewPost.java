package com.sabayrean.welcom.world;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sabayrean.welcom.R;
import com.sabayrean.welcom.app.AppConfig;
import com.sabayrean.welcom.app.AppController;
import com.sabayrean.welcom.helper.SessionManager;
import com.sabayrean.welcom.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by LAYLeangsros on 16/07/2015.
 */

public class NewPost extends Activity implements View.OnClickListener{

    ProgressDialog prgDialog;
    String encodedString;
    String imgPath, fileName;
    Bitmap bitmap;
    Button btnPost, btnAddImage;
    protected final int RESULT_LOAD_IMG=1;

    protected Double latitude,longitude;
    private TextView tvCity, tvCoutry;
    private EditText inputStatus;
    private CheckBox saveLocation;
    protected String imageBinary, imageFileName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post);

        btnAddImage = (Button) findViewById(R.id.btnAddImage);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnAddImage.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        prgDialog = new ProgressDialog(this);
        // Set Cancelable as False
        prgDialog.setCancelable(true);

        inputStatus = (EditText) findViewById(R.id.inputStatus);
        tvCity = (TextView) findViewById(R.id.city);
        tvCoutry = (TextView) findViewById(R.id.country);
        saveLocation = (CheckBox) findViewById(R.id.saveLocation);
        saveLocation.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddImage:
                loadImagefromGallery();
                break;
            case R.id.btnPost:
                //Post status
                postStatusNow();


                //Log.d("Binary", "" + imageBinary);
                break;
            case R.id.saveLocation:
                initLocation();
                break;
        }

    }

    private void postStatusNow() {
        prgDialog.show();
        String tag_string_req = "req_register";
        Log.d("Posting", "Wait");
        final SessionManager pref = new SessionManager(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("This", "Login Response: " + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if(!object.getBoolean("error")) {
                        //Go back to home page
                        prgDialog.hide();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("EEE", "Eroror while uploading"+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {



                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "post_status");
                params.put("saying", inputStatus.getText().toString());
                params.put("user_id", "" + pref.getId());
                if(imageFileName != null) {
                    params.put("image", imageBinary);
                    params.put("filename", imageFileName);
                }
                params.put("latitude", ""+latitude);
                params.put("longitude", ""+longitude);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void initLocation() {



        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location location;

        if(network_enabled){
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null && saveLocation.isChecked()){
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                //String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                //String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                tvCity.setText(city);
                tvCoutry.setText(country);
            }else{
                latitude=null;
                longitude=null;
            }
        }
    }
    public  void encondeImage(){
        InputStream inputStream = null;//You can get an inputStream using any IO API
        try {
            inputStream = new FileInputStream(imgPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        imageBinary = Base64.encodeToString(bytes, Base64.DEFAULT);


    }

    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {


            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                imageBinary = encodedString;                // Trigger Image upload
            }
        }.execute(null, null, null);
    }


    public void uploadImage() {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            prgDialog.setMessage("Converting Image to Binary Data");
            // Convert image to String using Base64
            encondeImage();
            ///encodeImagetoString();

            /*Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    encodeImagetoString();
                }
            });
            thread.start();*/

            // When Image is not selected from Gallery
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                Log.d("donee", "donee");
                Uri selectedImage = data.getData();
                Log.d("doneea", "doneea");
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
                ImageView imageView = (ImageView) findViewById(R.id.imgView);
                imageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));

                // Get the Image's file name
                Log.d("ImagePath:1" + imageFileName, "path:1" + imgPath);
                String fileNameSegments[] = imgPath.split("/");
                imageFileName = fileNameSegments[fileNameSegments.length - 1];
                Log.d("ImagePath:" + imageFileName, "path:" + imgPath);
                // Put file name in Async Http Post Param which will used in Php web app
                uploadImage();

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }
}
