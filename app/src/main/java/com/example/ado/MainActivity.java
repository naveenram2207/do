package com.example.ado;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    final String prodBase = "https://reqres.in";
    final String URL = prodBase + "/api/users";

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responseText = findViewById(R.id.response_text);
        signin();


    }
    private void signin(){
//        JSONObject data = new JSONObject();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "onResponse: Success");

                String value = "";

                try {
                    JSONArray dataArray = response.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject childObject = dataArray.getJSONObject(i);

                        value += childObject.getString("email") + "\n";
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: ",e);
                }


                responseText.setText(value);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onResponse: Error "+error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "getParams: coming here");
                HashMap<String, String> data = new HashMap<>();
                data.put("pages", "2");
                return data;
            }
        };
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        Volley.newRequestQueue(this).add(jsonObjectRequest.setRetryPolicy(retryPolicy));
    }
}
