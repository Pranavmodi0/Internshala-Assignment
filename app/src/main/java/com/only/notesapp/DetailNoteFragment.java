package com.only.notesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

public class DetailNoteFragment extends Fragment {

    private EditText textViewDetailTitle;
    private EditText textViewDetailDescription;
    private Button deleteBtn, updateBtn;
    private OnNoteActionListener listener;
    private ImageButton back_Btn;

    public interface OnNoteActionListener {
        void onNoteDeleted(String title);
        void onNoteUpdated(String oldTitle, String newTitle, String newDescription);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteActionListener) {
            listener = (OnNoteActionListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnNoteActionListener");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_note, container, false);

        textViewDetailTitle = view.findViewById(R.id.textViewDetailTitle);
        textViewDetailDescription = view.findViewById(R.id.textViewDetailDescription);
        deleteBtn = view.findViewById(R.id.delete_btn);
        updateBtn = view.findViewById(R.id.update_btn);
        back_Btn = view.findViewById(R.id.detail_back);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString("title", "");
            String description = bundle.getString("description", "");
            textViewDetailTitle.setText(title);
            textViewDetailDescription.setText(description);
        }

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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String title = textViewDetailTitle.getText().toString();
                    listener.onNoteDeleted(title);
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String oldTitle = bundle.getString("title", "");
                    String newTitle = textViewDetailTitle.getText().toString();
                    String newDescription = textViewDetailDescription.getText().toString();
                    listener.onNoteUpdated(oldTitle, newTitle, newDescription);
                }
            }
        });

        return view;
    }
}