package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.Iterator;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mOrigin,mAlsoKnown,mDescription,mIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        mOrigin = findViewById(R.id.origin_tv);
        mAlsoKnown = findViewById(R.id.also_known_tv);
        mDescription = findViewById(R.id.description_tv);
        mIngredient = findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(ingredientsIv,new Callback() {
                    @Override
                    public void onSuccess() {
                        findViewById(R.id.homeprogress).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        /*Log.i("POC","POC = "+sandwich.getPlaceOfOrigin());
        Log.i("DEC","POC = "+sandwich.getDescription());*/
        if(!sandwich.getPlaceOfOrigin().equals("")) {
            mOrigin.setText(sandwich.getPlaceOfOrigin());

        }
        else{
            mOrigin.setText("No Data Available");
        }
        if(!sandwich.getDescription().equals("")) {
            mDescription.setText(sandwich.getDescription());
        }
        else{
            mDescription.setText("No Data Available");
        }
        String aka = "";
        Iterator<String> iterator = sandwich.getAlsoKnownAs().iterator();
        while(iterator.hasNext()){
            if(aka!=""){
                aka = aka+", "+iterator.next();
            }
            else{
                aka = iterator.next();
            }
        }
        if(!aka.equals("")) {
            mAlsoKnown.setText(aka);
        }
        else{
            mAlsoKnown.setText("No Data Available");
        }
        iterator = sandwich.getIngredients().iterator();
        aka = "";
        while(iterator.hasNext()){
            if(aka!=""){
                aka = aka+", "+iterator.next();
            }
            else{
                aka = iterator.next();
            }
        }
        if(!aka.equals("")) {
            mIngredient.setText(aka);
        }
        else{
            mIngredient.setText("No Data Available");
        }
    }
}
