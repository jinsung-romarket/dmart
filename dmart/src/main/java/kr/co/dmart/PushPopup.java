package kr.co.dmart;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.dmart.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PushPopup extends Activity {
	
	public static String tmp_conn_url=null;
	public static String msg_kind=null;
	public static String shop_seq=null;
	public static String shop_name=null;
	private static Context pop_context = null;//applicationContext
	boolean customTitleSupported;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("GCMIntentService", "start PushPopup class");
		/*
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        */
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON 
	            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED 
	            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //투명배경
        
      //창이 나타나고 바닥은 검정색 반투명으로
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);

		//        Log.d("GCMIntentService", "addFlags end");

		//requestWindowFeature(android.view.Window.FEATURE_CUSTOM_TITLE);
//        customTitleSupported = requestWindowFeature(android.view.Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_popup);
		pop_context = this;
		//getWindow().setFeatureInt(android.view.Window.FEATURE_CUSTOM_TITLE, R.string.title_ment);
//		Log.d("GCMIntentService", "requestWindowFeature end");
		
        customTitleBar(getText(R.string.app_name).toString(), getText(R.string.title_ment).toString());
        
		Bundle extras = getIntent().getExtras();
        String msg = extras.getString("msg");
        //conn_url = extras.getString("conn_url");
        msg_kind = extras.getString("msg_kind");
        shop_seq = extras.getString("shop_seq");
        shop_name = extras.getString("shop_name");
        String is_sound = extras.getString("is_sound");
        
        if (is_sound.equals("N")) {
    	} else {
	        AudioManager clsAudioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
	        
	        switch( clsAudioManager.getRingerMode( ) )
	        {
	           case AudioManager.RINGER_MODE_VIBRATE:
	            // 진동 모드
	        	   Vibrator mVibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	        	   mVibe.vibrate(1000);
	        	   break;
	           case AudioManager.RINGER_MODE_NORMAL:
				   Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(),RingtoneManager.TYPE_NOTIFICATION);
				   Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
				   ringtone.play();
	           	/*
	        	   // 소리 모드
	        	   final float playbackSpeed = 1.0f;
	        	   // 두번 더 반복 (총 3번)
	           		final int loopCount = 0;                    
	           		final SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	           		//이 load함수는 음원이 raw 폴더에 있을 경우에 사용한다. 
	           		//sd카드에 있을경우에는 path를 지정하여 다른 load 함수를 이용하면 된다.
	           		final int soundId = soundPool.load(getApplicationContext(), R.raw.beep46, 1);      
	           	 
	           		AudioManager mgr = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
	           		final float streamCurrent = mgr.getStreamVolume(AudioManager.STREAM_RING);
	           		float streamMax = mgr.getStreamMaxVolume(AudioManager.STREAM_RING);
	           		//final float volume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	           		final float streamVolume = streamCurrent / 10;
	           		//final float streamVolume = 0.7f;
	           		Log.d("GCMIntentService", "streamCurrent="+streamCurrent+",streamMax="+streamMax+",streamVolume="+streamVolume);
	           		soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
	           		@Override
	           	    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
	       				//soundPool.play(soundId, 0.03f, 0.03f, 1, loopCount, playbackSpeed);
	           	    	//soundPool.play(soundId, 1, 1, 1, loopCount, playbackSpeed);
	       				Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(),RingtoneManager.TYPE_NOTIFICATION);
	       				Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
	       				ringtone.play();
	           	    }
	           	});
	           	*/
	            break;
	        }
    	}
        
        if (shop_name == null || shop_name.equals("")) shop_name = "로마켓";
        String[] arr_msg = msg.split("º");
        String new_msg = "";
        for (int i = 0; i<arr_msg.length; i++) {
        	new_msg += arr_msg[i]+"\r\n";
        	//Log.d("GCMIntentService", "arr_msg[i]="+arr_msg[i]);
        }
        
        if(new_msg != null && new_msg.length() > 200) {
    		DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
    		int width = dm.widthPixels;
    		int height = dm.heightPixels;
    		
    		Log.d("GCMIntentService", "width="+width);
    		Log.d("GCMIntentService", "height="+height);
    		
    		int parent_layout_width = width - 50;
    		int parent_layout_height = height - 100;
    		
    		Log.d("GCMIntentService", "2 parent_layout_width="+parent_layout_width);
    		Log.d("GCMIntentService", "2 parent_layout_height="+parent_layout_height);
    		Log.d("GCMIntentService", "parent_layout_width="+parent_layout_width);
    		Log.d("GCMIntentService", "parent_layout_height="+parent_layout_height);

    		RelativeLayout rl = (RelativeLayout) findViewById(R.id.parent_r_layout);
    		//if (parent_layout_width>910) parent_layout_width = 910;
    		rl.getLayoutParams().width = parent_layout_width;
    		rl.getLayoutParams().height = parent_layout_height;
        }

		Log.d("GCMIntentService", "new_msg="+new_msg);
	    //tmp_conn_url = conn_url; 
        String tmp_subject_msg = "[정보]".concat(shop_name);
		TextView tvText = (TextView) findViewById(R.id.textView1);
		//RelativeLayout MyLinear=(RelativeLayout)findViewById(R.id.RelativeLayout01);
		tvText.setMaxLines(100);
		tvText.setVerticalScrollBarEnabled(true);
		tvText.setMovementMethod(new ScrollingMovementMethod());
		tvText.setText(new_msg);
		TextView subject_bar=(TextView)findViewById(R.id.text_subject_bar);
        subject_bar.setText(tmp_subject_msg);
        //tvText.setPaintFlags(tvText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		//TextView tvText_title = (TextView) findViewById(R.id.text_title_bar);
		//tvText_title.setText(shop_name);
		Log.d("GCMIntentService", "set msg to TextView");
		//new MainActivity.response_devcie_send().execute(null,null,null);
		//MyLinear.setBackgroundColor(Color.TRANSPARENT);
		//text_title_bar.setBackgroundColor(Color.TRANSPARENT);
		
		/*
		TimerTask myTask = new TimerTask() {
		    public void run() {
		        //PushWakeLock pw1 = new PushWakeLock ();
		        //pw1.releaseCpuLock();
		        finish();
		    }
		};
		Timer timer = new Timer();
		timer.schedule(myTask, 10000);  // 5초후 실행하고 종료
		*/

	}
	
	public void onNewIntent(Intent newIntent) {
//		Log.d("GCMIntentService", "설마...onNewIntent...");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void btn_click (View v) {
		Log.d("GCMIntentService", "kr.co.dmart.MainActivity.is_live..."+kr.co.dmart.MainActivity.is_live);
		if (v.getId() == R.id.button_confirm) {	
			boolean is_live_1 = kr.co.dmart.MainActivity.is_live;
			if (is_live_1 == true) {
        		Log.d("GCMIntentService", "로마켓 실행중...");
				if (Build.VERSION.SDK_INT>=23) {
					Intent i = new Intent(pop_context, MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_SINGLE_TOP);
					i.putExtra("msg_kind", msg_kind);
					i.putExtra("shop_seq", shop_seq);
					startActivity(i);
				}
        	} else {
        		Log.d("GCMIntentService", "로마켓 비 실행중...");
        		Intent i = new Intent(pop_context,MainActivity.class);
        		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
        			| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        		i.putExtra("msg_kind", msg_kind);
        		i.putExtra("shop_seq", shop_seq);
        		startActivity(i);
        		//Toast.makeText(getApplicationContext(), "로마켓 비 실행중...", Toast.LENGTH_SHORT).show();
        	}
			this.finish();
		} else if (v.getId() == R.id.button_cancel) {	
			this.finish();
		}
	}
	
	public void customTitleBar(String left, String right) {
        if (right.length() > 20) right = right.substring(0, 20);
        // set up custom title
        if (customTitleSupported) {
                getWindow().setFeatureInt(android.view.Window.FEATURE_CUSTOM_TITLE,
                                R.layout.title_layout);
                TextView titleTvLeft = (TextView) findViewById(R.id.titleTvLeft);
                titleTvLeft.setText(right);
 
        }
	}
	
	public static Context getPushpopAppContext() {
        return pop_context;
    }
	
	@Override
	protected void onPause() {
		super.onPause();
//		Log.d("GCMIntentService", "...onPause...");
	}

	protected void onDestroy() {
		super.onDestroy();
//		Log.d("GCMIntentService", "...onDestroy...");
	}
	
}

