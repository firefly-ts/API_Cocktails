package com.example.api_coctails.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.api_coctails.R;
import com.example.api_coctails.data.CocktailAdapter;
import com.example.api_coctails.model.Cocktail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FirstLetterActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CocktailAdapter cocktailAdapter;
    private ArrayList<Cocktail> cocktails;
    private RequestQueue requestQueue;

    String letter;

    Context cContext;
    DBCocktails cDBConnector;
    ImageButton img;

    private class QueryTask extends AsyncTask<String, Void, JsonObjectRequest>{


        @Override
        protected JsonObjectRequest doInBackground(String... strings) {

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, strings[0], null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("drinks");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String title = jsonObject.getString("strDrink");
                                String pictureUrl = jsonObject.getString("strDrinkThumb");
                                String category = jsonObject.getString("strCategory");
                                String instruction = jsonObject.getString("strInstructions");

                                Cocktail cocktail = new Cocktail();
                                cocktail.setTitle(title);
                                cocktail.setPictureUrl(pictureUrl);
                                cocktail.setCategory(category);
                                cocktail.setInstruction(instruction);

                                cocktails.add(cocktail);
                                Log.i("<<< First Activity >>>", "Add DataBase");
                            }
                            // определяем слушателя нажатия элемента в списке
                            CocktailAdapter.OnStateClickListener onClickListener = new CocktailAdapter.OnStateClickListener() {
                                @Override
                                public void onStateClick(Cocktail cocktail, int position) {
                                    cDBConnector.insert(cocktail);  // Добавляем в базу
                                    
                                }
                            };

                            cocktailAdapter = new CocktailAdapter(FirstLetterActivity.this, cocktails, onClickListener);
                            recyclerView.setAdapter(cocktailAdapter);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

            return request;
        }

        @Override
        protected void onPostExecute(JsonObjectRequest jsonObjectRequest) {
            requestQueue.add(jsonObjectRequest);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_letter);

        Intent intent = getIntent();
        letter = intent.getStringExtra("letter");  // получаем по ключу


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cocktails = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        cContext = this;
        cDBConnector = new DBCocktails(this);

        getCocktails();

    }


    private void getCocktails() {
        String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=" + letter;
//        String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=a";
        //String url = "https://www.thecocktaildb.com/api/json/v1/1/random.php";

        new QueryTask().execute(url);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FirstLetterActivity.this, MainActivity.class));
    }

}