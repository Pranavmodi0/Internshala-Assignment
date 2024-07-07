package com.only.notesapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.only.notesapp.Adapters.NotesAdapter;
import com.only.notesapp.Models.Notes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements DetailNoteFragment.OnNoteActionListener {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private List<Notes> noteList;
    private LinearLayout addNote;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = getActivity().getSharedPreferences("notes", MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.notes_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        noteList = loadNotes();
        adapter = new NotesAdapter(getActivity(), noteList);
        recyclerView.setAdapter(adapter);

        addNote = view.findViewById(R.id.add_note_btn);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddNoteFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private List<Notes> loadNotes() {
        String json = sharedPreferences.getString("notes_map", "{}");

        Gson gson = new Gson();
        Map<String, Notes> notesMap = gson.fromJson(json, new TypeToken<HashMap<String, Notes>>() {}.getType());

        if (notesMap != null) {
            return new ArrayList<>(notesMap.values());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void onNoteDeleted(String title) {
        Map<String, Notes> notesMap = loadNotesFromSharedPreferences();
        if (notesMap != null) {
            notesMap.remove(title);
            saveNotesToSharedPreferences(notesMap);
            refreshNotesList();
        }
    }

    @Override
    public void onNoteUpdated(String oldTitle, String newTitle, String newDescription) {
        Map<String, Notes> notesMap = loadNotesFromSharedPreferences();
        if (notesMap != null) {
            Notes note = notesMap.remove(oldTitle);
            if (note != null) {
                note.setTitle(newTitle);
                note.setDescription(newDescription);
                notesMap.put(newTitle, note);
                saveNotesToSharedPreferences(notesMap);
                refreshNotesList();
            }
        }
    }

    private Map<String, Notes> loadNotesFromSharedPreferences() {
        String json = sharedPreferences.getString("notes_map", "{}");
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<HashMap<String, Notes>>() {}.getType());
    }

    private void saveNotesToSharedPreferences(Map<String, Notes> notesMap) {
        Gson gson = new Gson();
        String json = gson.toJson(notesMap);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("notes_map", json);
        editor.apply();
    }

    private void refreshNotesList() {
        noteList.clear();
        noteList.addAll(loadNotes());
        adapter.notifyDataSetChanged();
    }
}