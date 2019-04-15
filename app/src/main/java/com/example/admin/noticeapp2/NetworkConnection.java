package com.example.admin.noticeapp2;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class NetworkConnection extends AppCompatActivity {
    Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_layout);

        Glide.with(NetworkConnection.this)
                .load(R.drawable.network)
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) findViewById(R.id.imageView4));

//        final String url = "https://api.dandelion.eu/datatxt/sim/v1";
//        final String token="9b89f0c372c94723895b26ac3652153b";



        findViewById(R.id.button2)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConnectivityManager connec =
                                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
                        if(( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) )
                        {

//                            String t2 = null,t1=null;
//                            try {
//                                t2 = URLEncoder.encode( "Germany and France are to seek talks with the US to settle a row over spying, as espionage claims continue to overshadow an EU summit in Brussels.","utf-8");
//
//                                t1 = URLEncoder.encode("Reports that the NSA eavesdropped on world leaders have \"severely shaken\" relations between Europe and the U.S., German Chancellor Angela Merkel said.","utf-8");
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//
//                           String URL =  url+"?text1="+ t1 +"&text2="+ t2 +"&token="+token;
//
//                            RequestQueue queue = Volley.newRequestQueue(NetworkConnection.this);
//
//                            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
//                                @Override
//                                public void onResponse(JSONObject response) {
//                                    Log.e("RESPONSE:",response.toString());
//                                    Gson gson = new Gson();
//                                    TextSimilarity result =  gson.fromJson(String.valueOf(response),TextSimilarity.class);
//                                    Log.e("RESPONSE:",result.getSimilarity().toString());
//
//                                }
//                            }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Log.e("RESPONSE_ERR:",error.toString());
//                                }
//                            });
//                            queue.add(jsObjRequest);
                            Intent intent = new Intent(getApplicationContext(),Login_Window.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(NetworkConnection.this,"No Connection",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
