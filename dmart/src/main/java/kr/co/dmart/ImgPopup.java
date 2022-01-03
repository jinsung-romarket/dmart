package kr.co.dmart;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import uk.co.senab.photoview.PhotoViewAttacher;
import kr.co.dmart.MainActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class ImgPopup  extends Activity{
	
	private static Context imppop_context = null;//applicationContext
	public static String tmp_shop_seq=null;
	public static String tmp_title_msg=null;
	public static String tmp_subject_msg=null;
	public static float static_maxTextureSize = 4000;
	public static Activity zoom_img_activity;
	public static Context zoom_img_context;
	PhotoViewAttacher mAttacher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		int[] maxTextureSize = new int[1];
    	GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
    	Log.d("imgpopuptest", "maxTextureSize[0]="+maxTextureSize[0]);
    	if (maxTextureSize[0] == 0) {
    		Log.d("imgpopuptest", "인식못함, 재시도");
            GLES10.glGetIntegerv(GL11.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
        }
    	if (maxTextureSize[0] == 0) maxTextureSize[0] = 4000;
    	this.static_maxTextureSize = maxTextureSize[0];
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON 
	            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED 
	            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //투명배경
        
        //창이 나타나고 바닥은 검정색 반투명으로
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);

        imppop_context = this;
    	Bundle extras = getIntent().getExtras();
    	String msg = extras.getString("msg");
    	String conn_url = extras.getString("conn_url");
    	String shop_seq = extras.getString("shop_seq");
    	tmp_shop_seq = shop_seq; 
    	String shop_name = extras.getString("shop_name");
    	String msg_kind = extras.getString("msg_kind");
    	String is_sound = extras.getString("is_sound");
    	if (shop_name == null || shop_name.equals("")) {
    		tmp_title_msg = "[로마켓]";
    	} else {
    		tmp_title_msg = shop_name;
    	}
    	tmp_subject_msg = "[정보]".concat(msg);
    	Log.d("imgpopuptest", "conn_url="+conn_url);
    	Log.d("imgpopuptest", "msg="+msg);
    	Log.d("imgpopuptest", "shop_seq="+shop_seq);
    	Log.d("imgpopuptest", "shop_name="+shop_name);
    	Log.d("imgpopuptest", "tmp_subject_msg="+tmp_subject_msg);
    	Log.d("imgpopuptest", "tmp_title_msg="+tmp_title_msg);
    	Log.d("imgpopuptest", "is_sound="+is_sound);
    	
        //final float playbackSpeed = 1.0f;
    	// 두번 더 반복 (총 3번)
    	//final int loopCount = 0;                    
    	//final SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
    	// 이 load함수는 음원이 raw 폴더에 있을 경우에 사용한다. 
    	// sd카드에 있을경우에는 path를 지정하여 다른 load 함수를 이용하면 된다.
    	//final int soundId = soundPool.load(getApplicationContext(), R.raw.beep46, 1);      
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
	           		final float streamVolume = streamCurrent / streamMax;
	           		//final float volume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	           		Log.d("GCMIntentService", "streamCurrent="+streamCurrent+",streamMax="+streamMax+",streamVolume="+streamVolume);
	           		soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
	           		@Override
	           	    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
	           			//soundPool.play(soundId, streamVolume, streamVolume, 1, loopCount, playbackSpeed);
	           	    	//soundPool.play(soundId, 1, 1, 1, loopCount, playbackSpeed);
	       				Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(),RingtoneManager.TYPE_NOTIFICATION);
	       				Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
	       				ringtone.play();
	           	    }
	           	});
	            break;
	        }
    	}
    	
		//new DownloadImageTask((ImageView) findViewById(R.id.your_image)).execute(conn_url);
		//setContentView(R.layout.activity_imgpopup);
    	setContentView(R.layout.activity_zoom_img);  // layout xml 과 자바파일을 연결
		Log.d("GCMIntentService", "zoom_img..."+kr.co.dmart.MainActivity.static_zoom_img_src);
		new DownloadImageTask((ImageView) findViewById(R.id.zoom_image)).execute(conn_url);
		setContentView(R.layout.activity_zoom_img);
		//new MainActivity.response_devcie_send().execute(null,null,null);
		
	}
	
	public void btn_click (View v) {
		
//		switch(v.getId()){
//        case R.id.button_confirm:
		if (v.getId() == R.id.button_confirm || v.getId() == R.id.your_image) {
			boolean is_live_1 = kr.co.dmart.MainActivity.is_live;
        	Intent i = new Intent(imppop_context,MainActivity.class);
        	
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        			| Intent.FLAG_ACTIVITY_CLEAR_TOP
        			| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			i.putExtra("conn_url", "imgpopup"); //
			i.putExtra("noti_from", "imgpopup");
			i.putExtra("shop_seq", tmp_shop_seq);
			startActivity(i);
			
			this.finish();
			
		} else if (v.getId() == R.id.button_cancel) {
        	this.finish();
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	    	if (result == null) {
	    		finish();
	    	} else {
		    	float img_width = result.getWidth();
		    	float img_height = result.getHeight();
	//	    	Log.d("imgpopuptest", "img_width="+img_width);
	//	    	Log.d("imgpopuptest", "img_height="+img_height);
		    	float new_img_width = 0;
		    	float new_img_height = 0;
		    	float resize_base = 0;
		    	
		    	
		    	float base_size = ZoomImg_activity.static_maxTextureSize;
		    	if (base_size<4000) base_size = 4000;
	
		    	if (img_width > base_size || img_height > base_size){
		    		//고행사도 이미지는 Bitmap too large to be uploaded into a texture 오류발생...
		    		base_size = base_size -10;
		    		Log.d("imgpopuptest", "고해상도 이미지라 리사이즈="+base_size);
		    		if (img_width>img_height) {
		    			//가로가 큰 경우 가로를 기준으로 리사이즈
		    			resize_base = base_size/img_width;
		    			new_img_width = base_size;
		    			new_img_height = img_height*resize_base;
		    		} else {
		    			//세로가 큰 경우
		    			resize_base = base_size/img_height;
		    			new_img_height = base_size;
		    			new_img_width = img_width*resize_base;
		    		}
	//	    		Log.d("imgpopuptest", "resize_base="+resize_base);
	//		    	Log.d("imgpopuptest", "new_img_width="+new_img_width);
	//		    	Log.d("imgpopuptest", "new_img_height="+new_img_height);
		    		Bitmap resized = null;
		    		resized = Bitmap.createScaledBitmap(result, (int)new_img_width , (int)new_img_height, false);
	//	    		Log.d("imgpopuptest", "result="+result);
	//	    		Log.d("imgpopuptest", "resized="+resized);
		    		
			    	ImageView zoom_img = (ImageView)findViewById(R.id.zoom_image);
			    	zoom_img.setBackgroundColor(Color.TRANSPARENT);
			    	zoom_img.setImageBitmap(resized);
		    		mAttacher = new PhotoViewAttacher(zoom_img);
		            // 3.화면에 꽉차는 옵션 (선택사항)
		            mAttacher.setScaleType(ScaleType.FIT_CENTER);
		    	} else {
			    	ImageView zoom_img = (ImageView)findViewById(R.id.zoom_image);
			    	zoom_img.setBackgroundColor(Color.TRANSPARENT);
			    	zoom_img.setImageBitmap(result);
		    		mAttacher = new PhotoViewAttacher(zoom_img);
		            // 3.화면에 꽉차는 옵션 (선택사항)
		            mAttacher.setScaleType(ScaleType.FIT_CENTER);
		    	}
	    	}
	    }
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	boolean call_goback = true;
	    	if (keyCode == KeyEvent.KEYCODE_BACK) { 
	    		finish();
	            return true; 
	        }
	    	return super.onKeyDown(keyCode, event);
    }
}
