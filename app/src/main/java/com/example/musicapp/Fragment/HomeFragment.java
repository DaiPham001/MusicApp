package com.example.musicapp.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.musicapp.Activity.AlbumActivity;
import com.example.musicapp.Activity.ListMusicActivity;
import com.example.musicapp.Activity.MusicPlayerActivity;
import com.example.musicapp.Activity.PlayListActivity;
import com.example.musicapp.Activity.TopicActivity;
import com.example.musicapp.Adapter.Adapter_Album;
import com.example.musicapp.Adapter.Adapter_Category_Topic;
import com.example.musicapp.Adapter.Adapter_Playlist;
import com.example.musicapp.Adapter.Adapter_Popular;
import com.example.musicapp.Adapter.Adapter_pager;
import com.example.musicapp.Model.Advertisement;
import com.example.musicapp.Model.Album;
import com.example.musicapp.Model.Category;
import com.example.musicapp.Model.Item_CaTop;
import com.example.musicapp.Model.Music;
import com.example.musicapp.Model.Playlist;
import com.example.musicapp.Model.Topic;
import com.example.musicapp.R;
import com.example.musicapp.ViewModel.Advertisement_ViewModel;
import com.example.musicapp.ViewModel.Album_ViewModel;
import com.example.musicapp.ViewModel.CategoryTopic_ViewModel;
import com.example.musicapp.ViewModel.Music_ViewModel;
import com.example.musicapp.ViewModel.Playlist_ViewModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment implements Adapter_pager.Onclick, Adapter_Playlist.Onclick, Adapter_Category_Topic.OnClick, Adapter_Album.OnClick, Adapter_Popular.OnClick_Luotthich {

    private Fragment fragment;
    private ViewPager viewPager2;
    private CircleIndicator circleIndicator;
    private Adapter_pager adapter_pager;
    private Adapter_Playlist adapter_playlist;
    private Adapter_Album adapter_album;
    private ArrayList<Advertisement> listpager;
    private Timer timer;
    private RecyclerView rcv_playlist,rcv_catetop,rcv_album,rcv_popular;
    private Playlist_ViewModel playlist_viewModel;
    private Album_ViewModel album_viewModel;
    private Music_ViewModel music_viewModel;
    private TextView tv_playlist_home,tv_ca_top,tv_album_home;
    private RelativeLayout overlay_no_network;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        addControls(view);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Home");
        addEvents();
        return view;
    }

    // anh xạ view
    private void addControls(View view) {
        viewPager2 = view.findViewById(R.id.viewpager2);
        circleIndicator = view.findViewById(R.id.circle);
        rcv_playlist = view.findViewById(R.id.rcv_playlist);
        rcv_catetop = view.findViewById(R.id.rcv_catop);
        rcv_album = view.findViewById(R.id.rcv_album);
        rcv_popular = view.findViewById(R.id.rcv_popular);
        tv_playlist_home = view.findViewById(R.id.tv_playlist_home);
        tv_ca_top = view.findViewById(R.id.tv_ca_top);
        tv_album_home = view.findViewById(R.id.tv_album_home);
        overlay_no_network = view.findViewById(R.id.overlay_no_network);
        // Initialize the list
        listpager = new ArrayList<>();
        adapter_pager = new Adapter_pager(getContext(), listpager,this);
        viewPager2.setAdapter(adapter_pager);

        circleIndicator.setViewPager(viewPager2);
        adapter_pager.registerDataSetObserver(circleIndicator.getDataSetObserver());

        // khởi tạo các viewmodel
        music_viewModel = new ViewModelProvider(getActivity()).get(Music_ViewModel.class);
        album_viewModel = new ViewModelProvider(getActivity()).get(Album_ViewModel.class);
        playlist_viewModel = new ViewModelProvider(getActivity()).get(Playlist_ViewModel.class);

        loadDataPage();
        loadPlaylist();
        loadCategoryTopic();
        loadAlbum();
        loadPopular();
    }

    private void loadPopular() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv_popular.setLayoutManager(linearLayoutManager);
        // dòng kẻ của rcv
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv_popular.addItemDecoration(itemDecoration);
        //
        music_viewModel.getpopular().observe(getActivity(), music_model -> {
            if (music_model.isSuccess()){
                Adapter_Popular adapter_popular = new Adapter_Popular(getContext(),music_model.getResult(),this);
                rcv_popular.setAdapter(adapter_popular);
            }else {
                Log.e("loadPopular","null");
            }
        });
    }

    private void loadAlbum() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        rcv_album.setLayoutManager(linearLayoutManager);

        album_viewModel.getalbum().observe(getActivity(), album_model -> {
            if (album_model.isSuccess()){
                 adapter_album = new Adapter_Album(getContext(), album_model.getResult(),this);
                rcv_album.setAdapter(adapter_album);
            }else {
                Log.e("loadalbum", "null");
            }
        });
    }

    // hiển thị danh sách chủ đề và thể loại
    private void loadCategoryTopic() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcv_catetop.setLayoutManager(linearLayoutManager);

        CategoryTopic_ViewModel categoryTopic_viewModel = new ViewModelProvider(getActivity()).get(CategoryTopic_ViewModel.class);
        categoryTopic_viewModel.getCategoryTopic().observe(getActivity(), categoryendTopic_model -> {
            if (categoryendTopic_model.isSuccess()) {
                // Combine theloai and chude into a single list of Item objects
                ArrayList<Item_CaTop> items = new ArrayList<>();
                for (Category category : categoryendTopic_model.getTheloai()) {
                    items.add(new Item_CaTop(category, null));
                }
                for (Topic topic : categoryendTopic_model.getChude()) {
                    items.add(new Item_CaTop(null, topic));
                }

                // Create the adapter with the combined list
                Adapter_Category_Topic adapter_category_topic = new Adapter_Category_Topic(getContext(), items,this);
                rcv_catetop.setAdapter(adapter_category_topic);
            } else {
                Log.e("nullcatetop", "null");
            }
        });
    }



    // hien thi danh sach playlist
    private void loadPlaylist() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv_playlist.setLayoutManager(linearLayoutManager);
        // dòng kẻ của rcv
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv_playlist.addItemDecoration(itemDecoration);
        //
        playlist_viewModel.getplaylist().observe(getActivity(), playlist_model -> {
            if (playlist_model.isSuccess()){
                adapter_playlist = new Adapter_Playlist(getContext(),playlist_model.getResult(),this);
                rcv_playlist.setAdapter(adapter_playlist);
            }else {
                Log.e("nullplaylist","null");
            }
        });

    }

    private void addEvents() {
        // Add any event listeners here if needed
        tv_playlist_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), PlayListActivity.class);
                startActivity(intent);
            }
        });

        tv_ca_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), TopicActivity.class);
                startActivity(intent);
            }
        });

        tv_album_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), AlbumActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadDataPage() {
        Advertisement_ViewModel advertisement_viewModel = new ViewModelProvider(getActivity()).get(Advertisement_ViewModel.class);
        advertisement_viewModel.getquangcao().observe(getActivity(), advertisement_model -> {
            if (advertisement_model.isSuccess()) {
                listpager.clear();
                listpager.addAll(advertisement_model.getResult());
                adapter_pager.notifyDataSetChanged();
                AutoSlideImages();  // Start auto-slide after data is loaded
            } else {
                Log.e("HomeFragment", "Failed to load advertisements");
            }
        });
    }

    private void AutoSlideImages() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (listpager == null || listpager.isEmpty() || viewPager2 == null) {
            return;
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    int currentItem = viewPager2.getCurrentItem();
                    int totalItem = listpager.size() - 1;
                    if (currentItem < totalItem) {
                        currentItem++;
                        viewPager2.setCurrentItem(currentItem);
                    } else {
                        viewPager2.setCurrentItem(0);
                    }
                });
            }
        }, 5000, 3000);
    }

    @Override
    public void onDestroyView() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroyView();
    }


    // quang cao
    @Override
    public void onClick(Advertisement advertisement) {
        intent = new Intent(getActivity(), ListMusicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("idqc",advertisement.getIdqc());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // playlist
    @Override
    public void onClick(Playlist playlist) {
        intent = new Intent(getActivity(), ListMusicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("playlist",playlist);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // item cap top
    @Override
    public void onClick(Item_CaTop item_caTop) {
        intent = new Intent(getActivity(), ListMusicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("idtheloai",item_caTop.getCategory().getIdtheloai());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // item album
    @Override
    public void onClick(Album album) {
        intent = new Intent(getActivity(),ListMusicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("idalbum",album.getIdalbum());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // cap nhat luot thich
    @Override
    public void onClick_Luotthich(Music music) {
        Log.e("luotthich",String.valueOf(music.getLuotthich()));
        Log.e("color","true");
        music_viewModel.update_luotthich(music.getIdbaihat()).observe(this, music_model -> {
            if (music_model.isSuccess()){
                Log.e("luotthich_update",String.valueOf(music.getLuotthich()));
                loadPopular();
            }else {
                Log.e("onClick_Luotthich","null");
            }
        });
    }

    @Override
    public void onClickItem(Music music) {
        intent = new Intent(getActivity(), MusicPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("musichome",music);
        intent.putExtras(bundle);
        startActivity(intent);
    }
//    public void getFragment() {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.viewpager, fragment)
//                .addToBackStack(null)
//                .commit();
//    }

    // Method to check network availability
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            return networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }

    private BroadcastReceiver musicPlayerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                if (isNetworkAvailable(context)){
                    Log.e("mang","có");
                    overlay_no_network.setVisibility(View.GONE);
                }else {
                    Log.e("mang","ko có");
                    overlay_no_network.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(musicPlayerReceiver,intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(musicPlayerReceiver);
    }
}
