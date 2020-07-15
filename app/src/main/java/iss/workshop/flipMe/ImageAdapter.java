package iss.workshop.flipMe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ImageAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<ImageDTO> images;
    private ArrayList<ImageDTO> selectedImages;



    public ImageAdapter(Context mContext, ArrayList<ImageDTO> images) {
        this.mContext = mContext;
        this.images = images;
        selectedImages = new ArrayList<>();
        int openCount=1;
        int currentId=0;
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
        final ImageDTO image = images.get(pos);
        image.setPos(pos);
        if (view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.linearlayout_images, null);
        }
        final ImageView imageView =
                (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageBitmap(image.getBitmap());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof WebViewActivity){
                    //store 2 instance of each selected image
                    System.out.println(image.getBitmap());
                    addSelectedImages(image);
                    if(selectedImages.size()==12){
                        startGameActivity();
                    }
                }

                if(mContext instanceof GameActivity){
                    System.out.println(image.getId());
                    System.out.println(image.getBitmap());
                }
                System.out.println(image.getPos());

                //send selected images to game activity
            }
        });

        return view;
    }

    public void addSelectedImages(ImageDTO image){
        image.setId(image.getPos());
        selectedImages.add(image);
        selectedImages.add(image);
    }

    public boolean match(int opened, int current){
        return (opened==current);
    }

    public void startGameActivity(){
        Collections.shuffle(selectedImages);
        Intent intent  = new Intent(mContext,GameActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("selected",(Serializable) selectedImages);
        intent.putExtra("BUNDLE",args);
        mContext.startActivity(intent);
    }

}
