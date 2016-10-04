package com.example.aman.crusader2;

/**
 * Created by aman on 4/10/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;


public class uploadvid {

    public static final String UPLOAD_URL= "http://ec2-52-66-4-99.ap-south-1.compute.amazonaws.com/upload.php";

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private int serverResponseCode;

    public String uploadVideo(String file) {

        String fileName = file;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        File sourceFile = new File(file);
        if (!sourceFile.isFile()) {
            Log.e("Sorry", "Source File Does not exist");
            return null;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(UPLOAD_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("myFile", fileName);
            //conn.setRequestProperty("x", "Value of string");
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

//            Context pref=null;
//            SharedPreferences prefs = pref.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//
//            // got the tag and description using shared prefrence.
//            String user_id1 = prefs.getString("user_id", " ");
//            String tag= prefs.getString("tag", " ");
//            String description = prefs.getString("description", " ");
//            String title = prefs.getString("title", " ");
//
//            String data="title="+title+"&tag="+tag+"&description="+description;
//
//            dos.writeBytes(data);

            bytesAvailable = fileInputStream.available();
            Log.i("yep", "Initial .available : " + bytesAvailable);

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = conn.getResponseCode();



            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (serverResponseCode == 200) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
            } catch (IOException ignored) {
            }
            return sb.toString();
        }else {
            return "Could not upload";
        }
    }

//    private SharedPreferences getSharedPreferences(String myPrefsName, int modePrivate) {
//        return ;
//    }

}

