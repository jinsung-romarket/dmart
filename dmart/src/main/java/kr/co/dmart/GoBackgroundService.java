package kr.co.dmart;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
 
public class GoBackgroundService extends Service {
    private int a = 0;
 
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
 
    @Override
    public void onCreate() {
        super.onCreate();
 
    }
 
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	
    	if (intent.getAction() != null) {
	        if (intent.getAction().equals("Action1")) {
	        //if ("Action1".equals(intent.getAction())) {
	            Log.i("onStartCommand-Action1", "---서비스 스타트--- ");
	            
	            Intent notificationIntent = new Intent(this, MainActivity.class);
	            notificationIntent.setAction("Action2");
	            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
	                    notificationIntent, 0);
	 
	            Bitmap icon = BitmapFactory.decodeResource(getResources(),
	            		R.drawable.ic_launcher);
	 
	            Notification notification = new NotificationCompat.Builder(this)
	                    .setContentTitle("로마켓 알림")
	                    .setTicker("로마켓알림")
	                    .setContentText("로마켓알림")
	                    .setSmallIcon(R.drawable.ic_launcher)
	                    .setContentIntent(pendingIntent)
	                    .setOngoing(true).build();
	 
	            startForeground(111, notification);
	            /*
	            Thread bt = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    while(true){
	                        try {
	                            Thread.sleep(2000);
	                        }catch(InterruptedException e){
	                            e.printStackTrace();
	                        }
	                        if(!MainActivity.TFLAG){
	                            Log.i("background-counter","---백스레드 중지---");
	                            break;
	                        }
	 
	                        Log.i("background-counter",String.valueOf(++a));
	                    }
	                }
	            });
	            bt.setName("백그라운드스레드");
	            bt.start();
	            */
	 
	        }else if(intent.getAction().equals("Action2")){
	        }
 
    	}
 
        return START_REDELIVER_INTENT;
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
 
        MainActivity.TFLAG = false;
        Log.i("SERVICE TAG ", "==== 서비스 destroyed ===");
 
    }
}
