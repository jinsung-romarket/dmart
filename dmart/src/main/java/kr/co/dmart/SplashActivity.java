package kr.co.dmart;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
            }
        };

        handler.sendEmptyMessageDelayed(0, 4000);
    }
}

