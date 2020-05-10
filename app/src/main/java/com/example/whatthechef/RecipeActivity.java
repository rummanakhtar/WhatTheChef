package com.example.whatthechef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;


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


public class RecipeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MyAdapter adapter;

    private static String JSON_URL="https://rummanakhtar.github.io/dataforwtc/fooddata.json";
    List<ListItem> items;

    @SuppressLint("Assert")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recyclerView = findViewById(R.id.recyclerView);
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
                        listItem.setItemImage(jsonObject.getString("image"));
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
