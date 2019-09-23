package com.example.lenovo.jsonserves;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button button;
    TextView textView, textView1;
    String apiURL = "http://mobileappdatabase.in/demo/smartnews/app_dashboard/jsonUrl/single-article.php?article-id=71";
    String title, image, category;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tb);
        textView1 = (TextView) findViewById(R.id.tv);
        imageView = (ImageView) findViewById(R.id.im);
        button = (Button) findViewById(R.id.b);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();
            }
        });

    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading dude");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String str = "";

            try {
                URL url;
                HttpsURLConnection httpsURLConnection = null;
                try {
                    url = new URL(apiURL);
                    httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    InputStream inputStream = httpsURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    int data = inputStreamReader.read();
                    while (data != -1) {
                        str =str+ (char) data;
                        data = inputStreamReader.read();
                        System.out.println(str);
                    }
                    return str;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (httpsURLConnection != null) {
                        httpsURLConnection.disconnect();
                    }
                }
            } catch (Exception e){
                    e.printStackTrace();
                    return "Exception is "+e.getMessage();
                }
                return str;
        }

            @Override
            protected void onPostExecute (String s){
                Log.d("data ",s);
                progressDialog.dismiss();

                try{
                    JSONArray jsonArray=new JSONArray(s);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);

                    //pulling data from array
                    title=jsonObject.getString("title");
                    image=jsonObject.getString("image");
                    category=jsonObject.getString("category");

                    //showing in UI
                    textView.setText("TITLE :"+title);
                    textView1.setText("Category "+category);

                    //showing image of url
                    Picasso.get().load(image).into(imageView);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

