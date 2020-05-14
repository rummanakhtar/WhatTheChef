package com.example.whatthechef;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class FinalRecipe extends AppCompatActivity {
    private TextView ingredients;
    private TextView procedure;
    private  TextView recipeName;
    String JSON_URL;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_recipe);
        ingredients=findViewById(R.id.ingredients);
        procedure=findViewById(R.id.procedure);
        recipeName=findViewById(R.id.recipename);


        try{
            if(Objects.requireNonNull(getIntent().getExtras()).getString("jsonURL")==null){
                JSON_URL="https://rummanakhtar.github.io/dataforwtc/veg.json";
            }
            JSON_URL= Objects.requireNonNull(getIntent().getExtras()).getString("jsonURL");
        }catch (Exception e){
            e.printStackTrace();
        }

        extractData();
    }
    private void extractData() {
        RequestQueue queue= Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        recipeName.setText(jsonObject.getString("recipename"));
                        ingredients.setText(jsonObject.getString("ingredients"));
                        procedure.setText(jsonObject.getString("procedure"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG","Error: "+error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }
}
