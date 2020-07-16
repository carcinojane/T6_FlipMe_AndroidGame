package iss.workshop.flipMe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<ImageDTO> images;
    private  ArrayList<Integer> imageIds;
    private ArrayList<Integer>imagePos;
    private ImageDTO selectid1;
    private ImageDTO selectid2;




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
            //imageView.setImageResource(R.drawable.dummy);
            imageIds = new ArrayList<>();
            imagePos=new ArrayList<>();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //store 2 instance of each selected image
                    if(imagePos.contains(image.getPos()))
                    {
                        System.out.println("already clicked"+image.getPos());
                        imagePos.remove(new Integer(image.getPos()));
                        imageIds.remove(new Integer(image.getPos()));
                        imageIds.remove(new Integer(image.getPos()));
                        imageView.clearColorFilter();
                    }
                    else{
                        imagePos.add(image.getPos());
                        System.out.println("Positions "+imagePos);
                        addSelectedImages(image);
                        System.out.println("Ids " +imageIds);
                        imageView.setColorFilter(R.color.MintCream);
                        System.out.println(image.getPos());
                        if (imageIds.size() == 12) {
                            startGameActivity();
                        }}
                }
            });
        }


        if(mContext instanceof GameActivity){

            //imageView.setImageBitmap(image.getBitmap());
            TextView matches = view.findViewById(R.id.matchesTxt);
            imageView.setImageResource(R.drawable.dummy);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView.setImageBitmap(image.getBitmap());
                    System.out.println("id "+image.getId());
                    System.out.println(image.getPos());
                    System.out.println(image.getBitmap());
                }
            });

        }

        return view;

    }

    public boolean checkMatch(int id1, int id2){
        return (id1==id2);
    }

    public void selectId(int id){
        if(selectid1!=null && selectid2!=null){
            if(selectid1.getBitmap()==selectid2.getBitmap()){

            }
        }
        if(selectid1==null){


        }

    }

    public void addSelectedImages(ImageDTO image){
        imageIds.add(image.getId());
        imageIds.add(image.getId());
    }


    public void startGameActivity(){
        Intent intent  = new Intent(mContext,GameActivity.class);
        intent.putIntegerArrayListExtra("ImageIds",imageIds);
        mContext.startActivity(intent);
    }



}
