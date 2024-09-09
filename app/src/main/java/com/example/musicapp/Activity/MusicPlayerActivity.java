package com.example.musicapp.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;;

import com.example.musicapp.Adapter.ViewPager_Playmusic_Adapter;
import com.example.musicapp.R;

public class MusicPlayerActivity extends AppCompatActivity {

    private ViewPager viewpager_playmusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        addcontroll();
    }

    private void addcontroll() {
        viewpager_playmusic = findViewById(R.id.viewpager_playmusic);
        ViewPager_Playmusic_Adapter viewPager_playmusic_adapter = new ViewPager_Playmusic_Adapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        viewpager_playmusic.setAdapter(viewPager_playmusic_adapter);
        viewpager_playmusic.setOffscreenPageLimit(2);
    }
}