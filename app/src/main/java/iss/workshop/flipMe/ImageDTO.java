package iss.workshop.flipMe;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ImageDTO {
    private Bitmap bitmap;
    private String url;
    private int pos;
    private ArrayList<ImageDTO> selectedImages;

    public ImageDTO(Bitmap bitmap, String url) {
        this.bitmap = bitmap;
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
