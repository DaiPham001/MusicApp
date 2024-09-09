package com.example.musicapp.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.musicapp.R;


public class SearchFragment extends Fragment {

    private SearchView searchView;
    private TextView tv_null_search;
    private RecyclerView rcv_search;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search,container,false);
       addcontroll(view);
       addevenst();
       return view;
    }

    private void addcontroll(View view) {
        searchView = view.findViewById(R.id.searchview);
        tv_null_search = view.findViewById(R.id.tv_null_search);
        rcv_search = view.findViewById(R.id.rcv_search);
    }

    private void addevenst() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0){
                    Log.e("newtest","null");
                }else {
                    Log.e("newtest","!null");
                }
                return false;
            }
        });
    }
}