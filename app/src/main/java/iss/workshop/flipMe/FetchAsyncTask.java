package iss.workshop.flipMe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


public class FetchAsyncTask extends AsyncTask<String, ImageDTO, Void> {
    ICallback callback;

    public FetchAsyncTask(ICallback callback){
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(String... urls) {
        String htmlText = getHTMLText(urls[0]);
        if(htmlText == null) {
            if(this.callback != null) callback.makeToast("No images found. Please try another webpage.");
            return null;
        }
        ArrayList<String> imgURLs = getImgURLs(htmlText);
        if(imgURLs.size() < WebViewActivity.NO_OF_IMAGES){
            if(this.callback != null) callback.makeToast("Insufficient images. Please try another search term.");
            return null;
        }
        getBitmaps(imgURLs);
        return null;
    }

    String getHTMLText(String urlString){
        StringBuilder stringBuffer = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            while(scanner.hasNext()){
                stringBuffer.append(scanner.nextLine());
            }
            connection.disconnect();
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return stringBuffer.toString();
    }

    ArrayList<String> getImgURLs(String htmlText){
        ArrayList<String> imgURLs = new ArrayList<>();
        int lastIndex = 0;
        while(true){
            int index1 = htmlText.indexOf("<img", lastIndex);
            int index2 = htmlText.indexOf(">", index1);
            if(index1 == -1 || index2 == -1) break;
            String imgURL = htmlText.substring(index1, index2 + 1);
            if(imgURL.contains("http")&&!imgURLs.contains(imgURL)) {
                imgURLs.add(imgURL);
            }
            lastIndex = index2;
        }

        HashSet<String> uniqueURLs= new HashSet(imgURLs);
        return new ArrayList<>(uniqueURLs);
    }

    void getBitmaps(ArrayList<String> imgURLs) {
        for (int i = 0; i < WebViewActivity.NO_OF_IMAGES; i++) {
            String imgURL = imgURLs.get(i);
            int index1 = imgURL.indexOf("http");
            int index2 = imgURL.indexOf("\"", index1);
            try {
                final URL url = new URL(imgURL.substring(index1, index2));
                // Download images in separate threads
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            downloadImage(url);
                        }
                        catch(IOException e){e.printStackTrace();}
                    }
                }).start();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    void downloadImage(URL url) throws IOException{
        if (isCancelled()) return;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        String cookie = connection.getHeaderField( "Set-Cookie");
        connection.disconnect();
        connection = (HttpURLConnection) url.openConnection();
        cookie = cookie != null? cookie.split(";")[0] : null;
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
        connection.setRequestProperty("Cookie", cookie);
        connection.connect();
        if(connection.getResponseCode() == 200) {
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            publishProgress(new ImageDTO(bitmap, url.toString()));
        }
        connection.disconnect();
    }

    @Override
    protected void onProgressUpdate(ImageDTO... images){
        if(this.callback != null){
            this.callback.AddImages(images[0]);
        }
    }

    @Override
    protected void onPostExecute(Void v){
    }

    public interface ICallback{
        void AddImages(ImageDTO image);
        void makeToast(String message);
    }
}

