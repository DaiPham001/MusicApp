package com.example.musicapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.musicapp.Adapter.ViewPagerAdapter;
import com.example.musicapp.Fragment.HomeFragment;
import com.example.musicapp.Model.FilesMP3;
import com.example.musicapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView naView;
    private Fragment fragment = null;
    private BottomNavigationView bottom;
    private ViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addcontroll();
        addevenst();
        checkPemission();
    }

    private void addcontroll() {
        drawerLayout = findViewById(R.id.drawerlayout);
        toolbar = findViewById(R.id.toolbar);
        naView = findViewById(R.id.naview);
        bottom = findViewById(R.id.botom);
        mViewPager = findViewById(R.id.viewpager);

        //hiển thị toobar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menutobar);

        //khai báo viewadapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        mViewPager.setAdapter(viewPagerAdapter);
        // load 2 tab
        mViewPager.setOffscreenPageLimit(2);
    }

    private void addevenst() {
        Navigation();
        addViewpager();
        addbottom();
    }

    private void Navigation() {
        // set fragment mặc định
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.viewpager, new HomeFragment())
                .commit();


        naView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homepage:
                    mViewPager.setCurrentItem(0);
                    getFragment();
                    break;

                case R.id.lt:
                    //fragment = new LaptopFragment();

                    getFragment();
                    break;
                case R.id.cart:
                    //fragment = new CartFragment();
                    getFragment();
                    break;
                case R.id.order:
                    //fragment = new ViewOrderFragment();
                    getFragment();
                    break;

                case R.id.manage:
                    //fragment = new ManageFragment();
                    getFragment();
                    break;

                case R.id.logout:
                    //Logout();
                    break;

                case R.id.sub_pass:
                    //fragment = new ChangeFragment();
                    getFragment();
                    break;

                default:

                    break;
            }

            // đổi title fragment
            getSupportActionBar().setTitle(item.getTitle());

            drawerLayout.closeDrawers();
            return false;
        });
    }

    // hàm sử lý hoạt động toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.e("đc", "00");
            drawerLayout.openDrawer(GravityCompat.START);// mở navigation bên trái
        }
        return super.onOptionsItemSelected(item);
    }

    // method khởi tạo fragment
    public void getFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.viewpager, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void addbottom() {
        // tạo hoạt động cho bottom
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        mViewPager.setCurrentItem(0);//sử lý click botom
                        // load lại dữ liệu khi click
                        HomeFragment homeFragment = (HomeFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
                        //HomeFragment.reloaddata();
                        break;
                    case R.id.tab2:
                        mViewPager.setCurrentItem(1);//sử lý click botom
                        break;
                    case R.id.tab3:
                        mViewPager.setCurrentItem(2);//sử lý click botom
                        break;
//                    case R.id.tab4:
//                        mViewPager.setCurrentItem(3);//sử lý click botom
//                        break;
                }
                return true;
            }
        });
    }

    private void addViewpager() {
        // tạo hoạt động cho viewpager
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // sử lý vuốt màn hình
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottom.getMenu().findItem(R.id.tab1).setChecked(true);
                        break;
                    case 1:
                        bottom.getMenu().findItem(R.id.tab2).setChecked(true);
                        break;
                    case 2:
                        bottom.getMenu().findItem(R.id.tab3).setChecked(true);
                        break;
//                    case 3:
//                        bottom.getMenu().findItem(R.id.tab4).setChecked(true);
//                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void checkPemission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else {
            // Permission is granted
            getAllAudio(this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getAllAudio(this);
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<FilesMP3> getAllAudio(Context context) {
        ArrayList<FilesMP3> tempAudiolist = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                FilesMP3 musicFiles = new FilesMP3(path, title, artist, album, duration);
                //Log.e("path", "album" + album);
               // Log.e("title", "title" + title);
                tempAudiolist.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudiolist;
    }
}