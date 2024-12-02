package com.example.musicapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.musicapp.Adapter.Adapter_ListMusicfm;
import com.example.musicapp.Model.Music;
import com.example.musicapp.R;
import com.example.musicapp.Sevice.MusicPlayerService;
import com.example.musicapp.ViewModel.Search_ViewModel;


public class SearchFragment extends Fragment implements Adapter_ListMusicfm.OnClick {

    private SearchView searchView;
    private TextView tv_null_search;
    private RecyclerView rcv_search;
    private Adapter_ListMusicfm adapter_listMusicfm;
    private Search_ViewModel search_viewModel;
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv_search.setLayoutManager(linearLayoutManager);
    }

    private void addevenst() {
        // lắng nghe sự kiện nhấp dữ liệu trên SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // khi người dùng nhấn nut "tìm kiếm" nếu vẵn n=bản không rỗng thì gọi api tìm kiếm
                if (!query.trim().isEmpty()){
                    getDataSearch(query.trim());// hàm lấy dữ liệu tìm kiếm
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // xử lý mỗi khi văn bản trong ô tìm kiếm thay đổi
                if (newText.length() == 0){
                    Log.e("newtest","null");
                    clearSearchResults(); // Xóa kết quả nếu ô tìm kiếm trống
                }else {
                    Log.e("newtest","!null");
                    getDataSearch(newText.toString());// Tìm kiếm nếu ô không rỗng
                }
                return false;
            }
        });
    }


    // hien san pham khi nguoi dung tim kiem
    private void getDataSearch(String s) {
        search_viewModel = new ViewModelProvider(SearchFragment.this).get(Search_ViewModel.class);
        search_viewModel.search(s).observe(SearchFragment.this,music_model -> {
            if (music_model.isSuccess()){
                adapter_listMusicfm = new Adapter_ListMusicfm(getContext(),music_model.getResult(),this);
                rcv_search.setAdapter(adapter_listMusicfm);
            }else {
                Log.e("loi","loi");
                clearSearchResults();// xóa dữ liệu trên giao diện
                tv_null_search.setVisibility(View.VISIBLE);

            }
        });
    }

    // hàm xóa kết quả tìm kiếm trên rcv
    private void clearSearchResults() {
        if (adapter_listMusicfm != null){
            adapter_listMusicfm.clear();
            rcv_search.setAdapter(null);// đặt lại rcv về trạng thái mặc định
        }
    }

    @Override
    public void onClick(Music music) {
        // Gửi dữ liệu bài hát đã chọn đến MusicPlayerService để phát nhạc
        Intent intent = new Intent(getContext(), MusicPlayerService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected_music", music);
        intent.putExtras(bundle);
        getActivity().startService(intent);
    }
}