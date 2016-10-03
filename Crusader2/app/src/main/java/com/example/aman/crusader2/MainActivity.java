package com.example.aman.crusader2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.HttpAuthHandler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    Button b1, b2;
    EditText et1, et2;
    String user, pass;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        et1 = (EditText) findViewById(R.id.editText1);
        et2 = (EditText) findViewById(R.id.editText2);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        tv = (TextView) findViewById(R.id.textView);


        b2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent i=new Intent(MainActivity.this,Signup.class);
                startActivity(i);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

//            new JSONTask().execute("http://ec2-52-66-4-99.ap-south-1.compute.amazonaws.com/first.php");


                //String st=getJson("http://ec2-52-66-4-99.ap-south-1.compute.amazonaws.com/first.php" , "usernameValue=shubam&passValue=shubam");

                //tv.setText(st);

                PostClass obj=new PostClass(et1.getText().toString(), et2.getText().toString());
                obj.execute();

            }

        });

    }


    private class PostClass extends AsyncTask<String, Void, Void> {


        String pass,user;

        public PostClass(String st1,String st2)  {

            user=st1;
            pass=st2;

        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL("http://ec2-52-66-4-99.ap-south-1.compute.amazonaws.com/first.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters = String.format("usernameValue=%s&passValue=%s",user,pass );
                connection.setRequestMethod("POST");
               // connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                //connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                final StringBuilder output = new StringBuilder("");
                //output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                //output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                //output.append(responseOutput.toString());

                JSONObject jObject = new JSONObject(responseOutput.toString());
                final String aJsonString = jObject.getString("code");



                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if(aJsonString.equals("0")) {
                            tv.setText("Credentials are wrong. Try Again !");
                            et1.setText("");
                            et2.setText("");
                        }
//
//                        if(aJsonString.equals("1")) {
//                            Intent i=new Intent(MainActivity.this,Signup.class);
//                        }
                    }
                });


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        public class JSONTask extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... params) {

                //  Toast.makeText(MainActivity.this , "*********  " , Toast.LENGTH_LONG).show();

                BufferedReader reader = null;
                HttpURLConnection connection = null;

                try {

                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream stream = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    String line = "";
                    StringBuffer buffer = new StringBuffer();

                    while ((line = reader.readLine()) != null) {

                        buffer.append(line);

                    }

                   return buffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    if (connection != null)
                        connection.disconnect();
                    try {
                        if (reader != null)
                            reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                return null;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null)
                    tv.setText(s);
                //if(s=="1")
                //Toast.makeText(MainActivity.this , "********* s ********* " , Toast.LENGTH_LONG).show();
            }


        }


        //@Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        //@Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            // Handle action bar item clicks here. The action bar will
//            // automatically handle clicks on the Home/Up button, so long
//            // as you specify a parent activity in AndroidManifest.xml.
//            int id = item.getItemId();
//
//            //noinspection SimplifiableIfStatement
//            if (id == R.id.action_settings) {
//                return true;
//            }
//
//            return super.onOptionsItemSelected(item);
//        }
    }
}