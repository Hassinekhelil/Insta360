package com.esprit.insta360.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.esprit.insta360.Fragments.AddCapturedPhotoFragment;
import com.esprit.insta360.Fragments.AddPhotoFromGalleryFragment;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.ViewPagerAdapter;

public class ShareActivity extends AppCompatActivity {
    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;
    Toolbar toolbar;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        toolbar = (Toolbar) findViewById(R.id.secondary_toolbar);
        setSupportActionBar(toolbar);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragments(new AddPhotoFromGalleryFragment());
        mViewPagerAdapter.addFragments(new AddCapturedPhotoFragment());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.postContainer);
        mViewPager.setAdapter(mViewPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.postTabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_gallery_picker);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_capture_something);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            if (position==0){
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_gallery_picker_on);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_capture_something);
            }
                if (position==1){
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_capture_something_on);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_gallery_picker);

                }

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

}
