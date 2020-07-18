package iss.workshop.flipMe;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class ImageDTO
        implements Serializable, Parcelable {
    private Bitmap bitmap;
    private String url;
    private int pos;
    private int id;

    public ImageDTO() {
    }

    public ImageDTO(int id) {
        this.id = id;
    }

    public ImageDTO(int id, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.id = id;
    }

    public ImageDTO(Bitmap bitmap, String url) {
        this.bitmap = bitmap;
        this.url = url;
    }

    protected ImageDTO(Parcel in) {
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        url = in.readString();
        pos = in.readInt();
        id = in.readInt();
    }

    public static final Creator<ImageDTO> CREATOR = new Creator<ImageDTO>() {
        @Override
        public ImageDTO createFromParcel(Parcel in) {
            return new ImageDTO(in);
        }

        @Override
        public ImageDTO[] newArray(int size) {
            return new ImageDTO[size];
        }
    };

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

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(url);
        parcel.writeInt(pos);
        parcel.writeParcelable(bitmap,i);

    }
}
