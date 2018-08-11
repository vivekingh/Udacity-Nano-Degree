package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        String mainName="";
        List<String> alsoKnownAs = new LinkedList<>();
        String placeOfOrigin="";
        String description="";
        String image="";
        List<String> ingredients = new LinkedList<>();

        try {
            JSONObject mainObject = new JSONObject(json);

            JSONObject uniObject = mainObject.getJSONObject("name");
            mainName = uniObject.getString("mainName");
            JSONArray jsonArray = uniObject.getJSONArray("alsoKnownAs");
            for(int i=0;i<jsonArray.length();i++){
                alsoKnownAs.add(jsonArray.getString(i));
            }

            placeOfOrigin = mainObject.getString("placeOfOrigin");
            description = mainObject.getString("description");
            image = mainObject.getString("image");
            jsonArray = mainObject.getJSONArray("ingredients");
            for(int i=0;i<jsonArray.length();i++){
                ingredients.add(jsonArray.getString(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Sandwich sandwich = new Sandwich(mainName,alsoKnownAs,placeOfOrigin,description,image,ingredients);

        return sandwich;
    }
}
