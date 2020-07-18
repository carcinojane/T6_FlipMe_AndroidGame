package iss.workshop.flipMe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){

        this.context=context;
    }

    public int[] slide_images={
            R.drawable.choose_level,
            R.drawable.select_image,
            R.drawable.match_image
    };

    public String[] slide_headings={
            "Choose a Level",
            "Select your images",
            "Find matching images"
    };

    public String[] slide_descs={
      "Choose a difficulty level; I can flip is a level for beginners with 6 matching pairs while" +
              "I can flipping code is a level for the brave souls.",
            "Firstly, input a URL and select fetch to retrieve your favourite images for the game." +
                    " Next, select the images of your choice.",
            "Finally, let the game begin, tap on the cards to reveal images behind them, find all matching pairs" +
                    "within the time limit to score points."
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slidelayout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slideImageView);
        TextView slideHeading =(TextView)view.findViewById(R.id.slide_heading);
        TextView slideDesc = (TextView)view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDesc.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((RelativeLayout)object);
    }
}
