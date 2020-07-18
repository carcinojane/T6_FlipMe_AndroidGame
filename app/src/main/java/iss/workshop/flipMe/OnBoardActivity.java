package iss.workshop.flipMe;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class OnBoardActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private LinearLayout mDotLayout;
    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);

        mSlideViewPager=(ViewPager)findViewById(R.id.slideViewPager);
        mDotLayout=(LinearLayout)findViewById(R.id.dots);

        sliderAdapter= new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        //initialize gesture detector
        this.gestureDetector = new GestureDetector(OnBoardActivity.this, this);
    }

    public  void addDotsIndicator(int position){
       mDots=new TextView[sliderAdapter.getCount()];
        mDotLayout.removeAllViews();;
       for(int i=0; i<mDots.length;i++){
           mDots[i]= new TextView(this);
           mDots[i].setText(Html.fromHtml("&#8226;"));
           mDots[i].setTextSize(35);
           mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

           mDotLayout.addView(mDots[i]);
       }
       if(mDots.length>0){
           mDots[position].setTextColor(getResources().getColor(R.color.White));
       }
    }

    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    //override on touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            //starting to swipe time gesture
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            //ending time swipe gesture
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                //getting value for horizontal swipe
                float valueX = x2 - x1;

                //getting value for vertical swipe
                float valueY = y2 - y1;

                if(Math.abs(valueY)>MIN_DISTANCE){
                    //detect left to right swipe
                    if (y1>y2){
                        super.finish();
                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_in_bottom);
                        Log.d(TAG, "Top Swipe");
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}