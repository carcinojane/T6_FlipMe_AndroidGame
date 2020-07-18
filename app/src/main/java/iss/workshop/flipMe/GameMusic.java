package iss.workshop.flipMe;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class GameMusic {
    private AudioAttributes audioAttributes;
    final int soundStreamMax=3;

    private static SoundPool soundPool;
    private static int correct; // if two matches
    private static int wrong; // if mismatch
    private static int win;
    private static int flip;

    public GameMusic(Context context){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            audioAttributes=new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            //Can Support Multiple Music playback simultaneously
            soundPool=new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(soundStreamMax)
                    .build();
        }
        else{
            //soundStreamMax:Set the maximum number of concurrent flows for soundpool objects
            //streamType:Stream, usually a STREAM_MUSIC
            //srcQuality:Sample rate conversion quality, currently no effect, use 0 as the default value
            soundPool=new SoundPool(soundStreamMax, AudioManager.STREAM_MUSIC,0);
        }
        //int load(Context context,int resId,int priority)
        //Loading audio with a resource ID
        //The API indicates that the priority parameter is currently not effective and suggests setting it to 1
        correct =soundPool.load(context,R.raw.correct,1);
        wrong=soundPool.load(context,R.raw.wrong,1);
        win=soundPool.load(context,R.raw.win,1);
        flip=soundPool.load(context,R.raw.flip,1);
    }
    public void playCorrectSound(){
        soundPool.play(correct,1.0f,1.0f,1,0,1.0f);
    }
    public void playWrongSound(){
        soundPool.play(wrong,1.0f,1.0f,1,0,1.0f);
    }
    public void playWinSound(){ soundPool.play(win,1.0f,1.0f,1,0,1.0f);}
    public void playClickSound(){ soundPool.play(flip,1.0f,1.0f,1,0,1.0f);}
}
