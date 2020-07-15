package iss.workshop.flipMe;

import android.graphics.Bitmap;

public class ImageDAO {
    private Bitmap bitmap;
    private String url;
    private int pos;

    public ImageDAO(Bitmap bitmap, String url) {
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
