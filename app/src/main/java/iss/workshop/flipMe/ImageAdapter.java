package iss.workshop.flipMe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<ImageDTO> images;


    public ImageAdapter(Context mContext, ArrayList<ImageDTO> images) {
        this.mContext = mContext;
        this.images = images;
    }

    //return no. of cells to render
    @Override
    public int getCount() {
        return Math.max(images.size(), 0);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int pos) {
        return null;
    }


    @SuppressLint("InflateParams")
    public View getView(int pos, View view, ViewGroup parent) {

        if (view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.linearlayout_images, null);
        }
        final ImageView imageView =
                view.findViewById(R.id.imageview);

        final ImageDTO image = images.get(pos);
        image.setPos(pos);


        if(mContext instanceof WebViewActivity) {
            imageView.setImageBitmap(image.getBitmap());
        }


        if(mContext instanceof GameActivity) {
            imageView.setImageResource(R.drawable.dummy);
        }

        return view;

    }

}
