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
    private final ArrayList<ImageDTO> selectedImages;



    public ImageAdapter(Context mContext, ArrayList<ImageDTO> images) {
        this.mContext = mContext;
        this.images = images;
        selectedImages = new ArrayList<>();
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

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mContext instanceof WebViewActivity) {
                        //store 2 instance of each selected image
                        System.out.println(image.getBitmap());
                        addSelectedImages(image);
                        System.out.println(image.getPos());
                        System.out.println(image.getId());
                        System.out.println(image.getBitmap());
                        if (selectedImages.size() == 12) {
                            startGameActivity();
                        }
                    }



                    //send selected images to game activity
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
        //image.setId(image.getPos());
        selectedImages.add(image);
        selectedImages.add(image);
    }

    public ImageDTO getSelectedImage(Integer id){
        for(ImageDTO selectedImage:selectedImages){
            if(selectedImage.getId()==id) return selectedImage;
        }
        return null;
    }

    public boolean match(ImageDTO image1, ImageDTO image2){
        int opened=image1.getId();
        int current=image2.getId();
        return (opened==current);
    }

    public void flip(){
        //use position to set card to dummy
    }

    public void startGameActivity(){
        Collections.shuffle(selectedImages);
        Intent intent  = new Intent(mContext,GameActivity.class);



        //ArrayList<ImageDTO> imageIds= new ArrayList<>();
        ArrayList<Integer> imageIds = new ArrayList<>();
        for(ImageDTO selectedImage:selectedImages){
            imageIds.add(selectedImage.getId());
        }
        intent.putIntegerArrayListExtra("BUNDLE",imageIds);
        mContext.startActivity(intent);
    }

}
