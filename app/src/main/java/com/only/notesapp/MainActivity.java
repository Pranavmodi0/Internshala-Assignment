package com.only.notesapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.only.notesapp.Models.Notes;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DetailNoteFragment.OnNoteActionListener {

    private SharedPreferences sharedPreferences;
    private static final String TAG = "MainActivity";

    private LoginFragment loginFragment = new LoginFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, loginFragment).commitNow();

        sharedPreferences = getSharedPreferences("notes", MODE_PRIVATE);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

                if (currentFragment instanceof HomeFragment || currentFragment instanceof LoginFragment) {
                    finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

    }

    @Override
    public void onNoteDeleted(String title) {
        Map<String, Notes> notesMap = loadNotesFromSharedPreferences();
        Iterator<Map.Entry<String, Notes>> iterator = notesMap.entrySet().iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Map.Entry<String, Notes> entry = iterator.next();
            if (entry.getValue().getTitle().equals(title)) {
                iterator.remove();
                found = true;
                break;
            }
        }
        if (found) {
            saveNotesToSharedPreferences(notesMap);
            refreshHomeFragment();
        }
    }

    @Override
    public void onNoteUpdated(String oldTitle, String newTitle, String newDescription) {
        Map<String, Notes> notesMap = loadNotesFromSharedPreferences();
        for (Map.Entry<String, Notes> entry : notesMap.entrySet()) {
            if (entry.getValue().getTitle().equals(oldTitle)) {
                Notes note = entry.getValue();
                note.setTitle(newTitle);
                note.setDescription(newDescription);
                notesMap.put(entry.getKey(), note);
                saveNotesToSharedPreferences(notesMap);
                refreshHomeFragment();
                return;
            }
        }
    }

    private Map<String, Notes> loadNotesFromSharedPreferences() {
        String json = sharedPreferences.getString("notes_map", "{}");
        Gson gson = new Gson();
        Map<String, Notes> notesMap = gson.fromJson(json, new TypeToken<HashMap<String, Notes>>() {}.getType());
        return notesMap != null ? notesMap : new HashMap<>();
    }

    private void saveNotesToSharedPreferences(Map<String, Notes> notesMap) {
        Gson gson = new Gson();
        String json = gson.toJson(notesMap);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("notes_map", json);
        editor.apply();
    }

    private void refreshHomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.frame_layout, homeFragment);
        fragmentTransaction.commit();
    }
}