package iss.workshop.flipMe;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageDTO
        implements Serializable {
    private Bitmap bitmap;
    private String url;
    private int pos;
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
