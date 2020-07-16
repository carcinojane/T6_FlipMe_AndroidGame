package iss.workshop.flipMe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class ImageAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<ImageDTO> images;
    private  ArrayList<Integer> imageIds;



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
        imageView.setImageBitmap(image.getBitmap());

        if(mContext instanceof WebViewActivity) {
            imageIds = new ArrayList<>();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //store 2 instance of each selected image
                    addSelectedImages(image);
                    if (imageIds.size() == 12) {
                        startGameActivity();
                    }
                }
            });
        }

        if(mContext instanceof GameActivity){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(image.getId());
                    System.out.println(image.getBitmap());
                }
            });

        }

        return view;

    }

    public void addSelectedImages(ImageDTO image){
        imageIds.add(image.getId());
        imageIds.add(image.getId());
    }


    public void startGameActivity(){
        Collections.shuffle(imageIds);
        Intent intent  = new Intent(mContext,GameActivity.class);
        intent.putIntegerArrayListExtra("ImageIds",imageIds);
        mContext.startActivity(intent);
    }

}
