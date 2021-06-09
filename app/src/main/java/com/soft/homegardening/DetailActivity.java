package com.soft.homegardening;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.soft.homegardening.activities.DashBoardActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class DetailActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.two, R.drawable.one, R.drawable.three};
    private ArrayList<ModelGif> gifArrayList = new ArrayList<>();
    Button startedbtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        startedbtn = findViewById(R.id.getstartbtn);
        startedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, LoginActivity.class));
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void init() {


        mPager = (ViewPager) findViewById(R.id.viewPagerMain);


        mPager.setAdapter(new SlidingImage_Adapter(populateGifData(),
                DetailActivity.this));


        CircleIndicator indicator =
                (CircleIndicator) findViewById(R.id.indicator);

        indicator.setViewPager(mPager);


//        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
//        indicator.setRadius(5 * density);

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 4000,
                4000);

    /*     Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });*/

    }

    private ArrayList<ModelGif> populateGifData() {
        gifArrayList.clear();
        for (int i = 0; i < IMAGES.length; i++) {
            if (i == 0) {
                gifArrayList.add(new ModelGif(IMAGES[0], "Flower will not grow, if the stem doesn't allow "));
            } else if (i == 1) {
                gifArrayList.add(new ModelGif(IMAGES[1], "Use plants to bring life"));
            } else if (i == 2) {
                gifArrayList.add(new ModelGif(IMAGES[2], "Garden is the sign of life "));
            }

        }


        return gifArrayList;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(this, DashBoardActivity.class));
            finish();
        }
    }
}