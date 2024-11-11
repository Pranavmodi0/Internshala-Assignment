package com.only.notesapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.only.notesapp.Models.Notes;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class AddNoteFragment extends Fragment {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Map<String, Notes> noteMap;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        editTextTitle = view.findViewById(R.id.editText1);
        editTextDescription = view.findViewById(R.id.editText2);
        Button buttonSave = view.findViewById(R.id.done_btn);
        ImageButton back_Btn = view.findViewById(R.id.add_back);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (!title.isEmpty() && !description.isEmpty()) {
            Notes note = new Notes();
            note.setTitle(title);
            note.setDescription(description);

            saveNoteToSharedPreferences(note);

            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(getContext(), "Please enter both title and description", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNoteToSharedPreferences(Notes note) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("notes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Map<String, Notes> notesMap = loadNotesFromSharedPreferences();
        if (notesMap == null) {
            notesMap = new HashMap<>();
        }
        String noteId = String.valueOf(System.currentTimeMillis()); // Example: Use timestamp as key
        notesMap.put(noteId, note);

        Gson gson = new Gson();
        String json = gson.toJson(notesMap);
        editor.putString("notes_map", json);
        editor.apply();
    }

    private Map<String, Notes> loadNotesFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("notes", MODE_PRIVATE);
        String json = sharedPreferences.getString("notes_map", null);
        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, HashMap.class);
        }
        return null;
    }
}