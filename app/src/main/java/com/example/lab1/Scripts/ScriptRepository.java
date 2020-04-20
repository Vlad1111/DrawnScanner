package com.example.lab1.Scripts;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ScriptRepository {
    private static ScriptRepository instance = null;
    private static String JSON_STRING_NAME = "SCRIPT_REPO";

    private ArrayList<Script> repo = new ArrayList<>();

    public static ScriptRepository getInstance(AppCompatActivity app){
        if(instance == null)
            instance = new ScriptRepository(app);
        return instance;
    }

    private ScriptRepository(AppCompatActivity app) {
        Load(app);
    }

    public void addScript(Script script, AppCompatActivity app){
        for(int i=0;i<repo.size();i++){
            Script sc =repo.get(i);
            if(sc.getName().equals(script.getName())){
                repo.set(i, script);
                Save(app);
                return;
            }
        }
        repo.add(script);
        Save(app);
    }

    public ArrayList<Script> getAll(){
        return repo;
    }

    public void deleteScript(Script script, AppCompatActivity app) {
        for (int i = 0; i < repo.size(); i++) {
            Script sc = repo.get(i);
            if (sc.getName().equals(script.getName())) {
                repo.remove(i);
                Save(app);
                return;
            }
        }
    }

    public Script getScript(String scriptName){
        for (int i = 0; i < repo.size(); i++) {
            Script sc = repo.get(i);
            if (sc.getName().equals(scriptName)) {
                return sc;
            }
        }
        return  null;
    }

    public void Save(AppCompatActivity app){
        SharedPreferences sharedPreferences = app.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(repo);
        editor.putString(JSON_STRING_NAME, json);
        editor.apply();
    }

    public  void Load(AppCompatActivity app){
        SharedPreferences sharedPreferences = app.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json  =sharedPreferences.getString(JSON_STRING_NAME, null);
        Type type = new TypeToken<ArrayList<Script>>() {}.getType();
        repo = gson.fromJson(json, type);

        if(repo == null)
            repo = new ArrayList<>();
    }
}
