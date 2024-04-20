package com.example.api_coctails.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.BaseAdapter;
import android.widget.SimpleCursorAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.api_coctails.R;
import com.example.api_coctails.data.CocktailAdapter;
import com.example.api_coctails.model.Cocktail;

import java.util.ArrayList;

public class FavoriteCocktailsActivity extends Activity {

    DBCocktails cDBConnector;
    Context cContext;

    private Button buttonDelete;
    private RecyclerView cRecyclerView;
    private CocktailAdapter cocktailAdapter;
    private ArrayList<Cocktail> cocktails;
    private RequestQueue requestQueue;

    private myListAdapter myAdapter;

    int UPDATE_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_cocktails);

        //  Работа с БД
        cContext = this;
        cDBConnector = new DBCocktails(this);

        cRecyclerView = findViewById(R.id.recyclerFavoriteView);
        cRecyclerView.hasFixedSize();
        cRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cocktails = cDBConnector.selectAll();  // хотим получить данные из БД
        cocktailAdapter = new CocktailAdapter(FavoriteCocktailsActivity.this, cocktails);
        cRecyclerView.setAdapter(cocktailAdapter);


        requestQueue = Volley.newRequestQueue(this);

        myAdapter=new myListAdapter(cContext,cDBConnector.selectAll());
        registerForContextMenu(cRecyclerView);

        buttonDelete = findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cDBConnector.deleteAll();
                finish();
                //startActivity(getIntent());
            }
        });
    }
    private void updateList () {
        myAdapter.setArrayMyData(cDBConnector.selectAll());
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            Cocktail cd = (Cocktail) data.getExtras().getSerializable("Cocktail");
            if (requestCode == UPDATE_ACTIVITY)
                cDBConnector.update(cd);
            else
                cDBConnector.insert(cd.getTitle(), cd.getPictureUrl(), cd.getCategory(), cd.getInstruction());
            updateList();
        }
    }

    public void clickDeleteButton(View view) {
    }

    class myListAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;
        private ArrayList<Cocktail> arrayMyCocktails;

        public myListAdapter (Context ctx, ArrayList<Cocktail> arr) {
            mLayoutInflater = LayoutInflater.from(ctx);
            setArrayMyData(arr);
        }

        public ArrayList<Cocktail> getArrayMyData() {
            return arrayMyCocktails;
        }

        public void setArrayMyData(ArrayList<Cocktail> arrayMyData) {
            this.arrayMyCocktails = arrayMyData;
        }

        public int getCount () {
            return arrayMyCocktails.size();
        }

        public Object getItem (int position) {

            return position;
        }

        public long getItemId (int position) {
            Cocktail cd = arrayMyCocktails.get(position);
            if (cd != null) {
                return cd.getId();
            }
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return convertView;
        }
    } // end myAdapter

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FavoriteCocktailsActivity.this, MainActivity.class));
    }
}