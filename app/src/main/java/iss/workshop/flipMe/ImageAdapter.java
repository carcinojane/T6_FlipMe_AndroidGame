package iss.workshop.flipMe;

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
    private  ArrayList<Integer> imageIds;
    private ArrayList<Integer>imagePos;


    public ImageAdapter(Context mContext, ArrayList<ImageDTO> images) {
        this.mContext = mContext;
        this.images = images;
    }

    //return no. of cells to render
    @Override
    public int getCount() {
        return(images.size()>0)? images.size():0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int pos) {
        return null;
    }

    //create cell for gridview
    public View getView(int pos, View view, ViewGroup parent) {

        if (view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.linearlayout_images, null);
        }
        final ImageView imageView =
                (ImageView) view.findViewById(R.id.imageview);

        final ImageDTO image = images.get(pos);
        image.setPos(pos);


        if(mContext instanceof WebViewActivity) {
            imageView.setImageBitmap(image.getBitmap());
            imageIds = new ArrayList<>();
            imagePos=new ArrayList<>();
        }


        if(mContext instanceof GameActivity) {
            imageView.setImageResource(R.drawable.dummy);
            //imageView.setImageBitmap(image.getBitmap());
        }

        return view;

    }

}
