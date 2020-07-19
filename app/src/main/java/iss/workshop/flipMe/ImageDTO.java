package iss.workshop.flipMe;

import android.graphics.Bitmap;

public class ImageDTO{
    private Bitmap bitmap;
    private int pos;
    private int id;


    public ImageDTO(int id) {
        this.id = id;
    }

    public ImageDTO(int id, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.id = id;
    }

    public ImageDTO(Bitmap bitmap, String url) {
        this.bitmap = bitmap;
    }

   
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
