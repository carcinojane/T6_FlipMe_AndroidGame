package iss.workshop.flipMe;

import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class OnBoardActivity extends AppCompatActivity {

    private LinearLayout mDotLayout;
    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;

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
           mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
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
}