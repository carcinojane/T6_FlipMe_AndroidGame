package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    TextView team6, tagLine;

    //animation variables
    Animation team6Anim, tagLineAnim;

    //time-interval for the welcome screen
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //animations
        team6Anim = AnimationUtils.loadAnimation(this, R.anim.team6_animation);
        tagLineAnim = AnimationUtils.loadAnimation(this, R.anim.tagline_animation);

        //animation hooks
        team6 = findViewById(R.id.team6);
        tagLine = findViewById(R.id.tagline);

        team6.setAnimation(team6Anim);
        tagLine.setAnimation(tagLineAnim);

        //splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}