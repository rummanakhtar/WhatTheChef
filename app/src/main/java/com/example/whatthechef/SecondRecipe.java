package com.example.whatthechef;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SecondRecipe extends AppCompatActivity {
    RecyclerView recyclerView;
    MyAdapter adapter;
    String JSON_URL;

    List<ListItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_recipe);

        recyclerView = findViewById(R.id.recyclerView);
        try{
            if(Objects.requireNonNull(getIntent().getExtras()).getString("jsonURL")==null){
                JSON_URL="https://rummanakhtar.github.io/dataforwtc/arabiandishes.json";
            }
            JSON_URL= Objects.requireNonNull(getIntent().getExtras()).getString("jsonURL");
        }catch (Exception e){
            e.printStackTrace();
        }

        items=new ArrayList<>();
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
                        ListItem listItem = new ListItem();
                        listItem.setItemName(jsonObject.getString("title"));
                        listItem.setItemDescription(jsonObject.getString("description"));
                        listItem.setCardFlag(jsonObject.getString("cardflag"));
                        listItem.setItemImage(jsonObject.getString("image"));
                        listItem.setColorStrip(jsonObject.getString("colorstrip"));
                        items.add(listItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter=new MyAdapter(getApplicationContext(),items);
                recyclerView.setAdapter(adapter);
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
