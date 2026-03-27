package com.example.DuAnMau_PH63816.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.DuAnMau_PH63816.R;

public class SimpleMessageFragment extends Fragment {
    private static final String ARG_TITLE = "arg_title";

    public static SimpleMessageFragment newInstance(String title) {
        SimpleMessageFragment fragment = new SimpleMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_placeholder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtPlaceholder = view.findViewById(R.id.txtPlaceholder);
        String title = getArguments() != null ? getArguments().getString(ARG_TITLE) : getString(R.string.placeholder_coming_soon);
        txtPlaceholder.setText(title);
    }
}
