package com.example.api_coctails.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {


    private Button buttonFavorite;

    private RecyclerView recyclerView;
    private CocktailAdapter cocktailAdapter;
    private ArrayList<Cocktail> cocktails;
    private RequestQueue requestQueue;

    Context cContext;
    DBCocktails cDBConnector;

    public void clickFavoriteButton(View view) {
    }

    private class QueryTask extends AsyncTask<String, Void, JsonObjectRequest> {


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

                                }
                                cocktailAdapter = new CocktailAdapter(MainActivity.this, cocktails);
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
        setContentView(R.layout.activity_main);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cocktails = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        cContext = this;
        cDBConnector = new DBCocktails(this);

        getCocktails();


        buttonFavorite = findViewById(R.id.button_favorite);
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteCocktailsActivity.class);
                startActivity(intent);
            }
        });



    }

    private void getCocktails() {
        //String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=a";
        String url = "https://www.thecocktaildb.com/api/json/v1/1/random.php";

        new MainActivity.QueryTask().execute(url);  // запускаем поток
    }

    public void clickLetterButton(View view) {
        Button b = (Button)view;
        String buttonText = b.getText().toString().toLowerCase();
//        Toast  - всплывающая подсказка
        Toast.makeText(this, "Нажата " + buttonText.toUpperCase(), Toast.LENGTH_SHORT).show();
        openFirstLetterActivity(buttonText);
      // Log.d("1", buttonText);  // Это можно использовать для просмотра в панели Logcat
    }

    private void openFirstLetterActivity(String letter) {
        Intent intent = new Intent(MainActivity.this, FirstLetterActivity.class);
        intent.putExtra("letter", letter);
        startActivity(intent);
        finish();
    }

}
