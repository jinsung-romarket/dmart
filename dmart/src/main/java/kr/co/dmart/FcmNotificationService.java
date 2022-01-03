package kr.co.dmart;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;
import java.util.concurrent.Executor;

public class FcmNotificationService extends JobIntentService {

    private static final String TAG = "FcmNotificationService";

    static final int JOB_ID = 1000;
    public  static String CHANNL_ID = "fcm_noti_channel";
    public  static int NOTI_ID = 001;
    public static String local_fcm_id = "";
    public static int local_ver = 0;

    void enqueueWork(Context context, Intent work) {
        enqueueWork(context, FcmNotificationService.class, JOB_ID, work);
    }


    @Override
    protected void onHandleWork(Intent intent) {
        Log.e(TAG, "메세지 왔음...팝업실행준비");
        //Log.d(TAG, "fcm msg=" + kr.co.dmart.FCMService.fcm_msg);
        //Log.d(TAG, "fcm msg_kind=" + kr.co.dmart.FCMService.fcm_msg_kind);
        //Log.d(TAG, "fcm conn_url=" + kr.co.dmart.FCMService.fcm_conn_url);
        //Log.d(TAG, "fcm shop_seq=" + kr.co.dmart.FCMService.fcm_shop_seq);
        //Log.d(TAG, "fcm shop_name=" + kr.co.dmart.FCMService.fcm_shop_name);

        String msg = FCMService.fcm_msg;
        String conn_url = FCMService.fcm_conn_url;
        String msg_kind = FCMService.fcm_msg_kind;
        String shop_seq = FCMService.fcm_shop_seq;
        String shop_name = FCMService.fcm_shop_name;
        String sale_contents 		= "";
        String result_sound = "";
        String block_popup 	= "";

        OutputStreamWriter wr = null;
        BufferedReader rd = null;

        String result = "";
        local_fcm_id = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "local_fcm_id=" + local_fcm_id);
        String version_num = "";
        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            local_ver = pinfo.versionCode;
            version_num = String.valueOf(local_ver);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("GCMIntentService", "NameNotFoundException..."+e.toString());
        }
        Log.e(TAG, "local_ver=" + local_ver);

        if (msg_kind.equals("HB")) {
            //살아있는지 확인
            //Log.d("GCMIntentService", "msg_kind=HB");
            /*
            String device_response_url 			= "https://dnmart.co.kr/daemon/daemon_device_response.html";
            StringBuffer strbuf = new StringBuffer();
            strbuf.append(device_response_url).append("?group_id=&dev_kind=andriod&fcm_id=").append(local_fcm_id).append("&ver=").append(local_ver);
            String url = strbuf.toString();

            try {
                URL Url = new URL(url); // URL화 한다.
                HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
                conn.setRequestMethod("GET"); // get방식 통신
                conn.setDoOutput(true); // 쓰기모드 지정
                conn.setDoInput(true); // 읽기모드 지정
                conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
                conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

                InputStream is = conn.getInputStream(); //input스트림 개방

                StringBuilder builder = new StringBuilder(); //문자열을 담기 위한 객체
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); //문자열 셋 세팅
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                //Log.d("GCMIntentService", "hb 연결종료");
            } catch (Exception e) {
            }
            */
        } else {

            int new_msg = 0;
            Random rnd = new Random();
            int rnd_val = rnd.nextInt(5);
            String get_new_msg_cnt_url = "";
            get_new_msg_cnt_url = "https://dnmart.co.kr/daemon/daemon_get_new_msg_cnt_sound.html";
            try {
                // Construct data
                String data = URLEncoder.encode("dv_id", "UTF-8") + "=" + URLEncoder.encode(local_fcm_id, "UTF-8");
                data += "&" + URLEncoder.encode("ver", "UTF-8") + "=" + local_ver;
                data += "&" + URLEncoder.encode("fcm_id", "UTF-8") + "=" + URLEncoder.encode(local_fcm_id, "UTF-8");
                data += "&" + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");

                // Send data
                URL url = new URL(get_new_msg_cnt_url);

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                // Get the response
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
//	    			Log.d(tag, "result = "+result);
                String[] arr_str;
                arr_str = result.split("\\|");
                String result_code 	= "";
                String result_msg 	= "";


                if (arr_str.length == 4) {
                    result_code 		= arr_str[0];
                    result_msg 			= arr_str[1];
                    result_sound 		= arr_str[2];
                    block_popup 		= arr_str[3];
                } else {
                    result_code 		= arr_str[0];
                    result_msg 			= arr_str[1];
                    result_sound 		= arr_str[2];
                    block_popup 		= "N";
                }

                if (result_code.equals("OK")) {
                    new_msg = Integer.parseInt(result_msg);
                } else {
                }
                //System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.toString());
            } finally {
                try {
                    if (wr != null) {
                        wr.close();
                    }
                    if (rd != null) {
                        rd.close();
                    }
                } catch (IOException ioe) {
                    System.out.println(ioe.toString());
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

            if (msg_kind.equals("spot")) {
                //반짝세일의 경우 메세지를 별도로 가져온다.
                Log.d(TAG, "반짝세일의 경우 메세지를 별도로 가져온다.");
                try {
                    // Construct data
                    String data = URLEncoder.encode("dv_id", "UTF-8") + "=" + URLEncoder.encode(local_fcm_id, "UTF-8");

                    // Send data
                    URL url = new URL(conn_url);

                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    // Get the response
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line;
                    result = "";
                    while ((line = rd.readLine()) != null) {
                        result += line;
                    }
                    Log.d(TAG, "result = "+result);
                    String[] arr_str;
                    arr_str = result.split("\\|");
                    String sale_result_code 	= arr_str[0];

                    if (sale_result_code.equals("OK")) {
                        msg 		= arr_str[1];
                    } else {
                    }
                    //System.out.println(result);
                } catch (Exception e) {
                    System.out.println(e.toString());
                } finally {
                    try {
                        if (wr != null) {
                            wr.close();
                        }
                        if (rd != null) {
                            rd.close();
                        }
                    } catch (IOException ioe) {
                        System.out.println(ioe.toString());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }

            String content_title = "로마켓";
            String content_text = "메세지가 도착했습니다. 마이페이지 -> 메세지에서 확인하실 수 있습니다.";
            String str_ticket = "로마켓에서 메세지가 도착하였습니다.";
            if (msg_kind.equals("IMGPOPUP")) {
                content_title = shop_name;
                content_text = "할인정보가 도착했습니다. 로마켓에 접속하셔서 확인하세요.";
                str_ticket = shop_name.concat("에서 메세지가 도착하였습니다.");
            } else {

            }
            //Log.d(LOG_TAG, "content_title=" + content_title);
            //Log.d(LOG_TAG, "content_text=" + content_text);
            //Log.d(LOG_TAG, "str_ticket=" + str_ticket);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String[] arr_msg = msg.split("º");
                String tmp_msg = "";
                for (int i = 0; i < arr_msg.length; i++) {
                    tmp_msg += arr_msg[i] + "\r\n";
                    //Log.d("GCMIntentService", "arr_msg[i]="+arr_msg[i]);
                }

                CharSequence name = "sale info notification";
                String desc = "할인,주문정보 알람";
                int impo = NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel noti_channel = new NotificationChannel(CHANNL_ID, name, impo);
                noti_channel.setDescription(desc);

                NotificationManager noti_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                noti_manager.createNotificationChannel(noti_channel);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNL_ID);
                builder.setSmallIcon(R.drawable.ic_launcher);
                builder.setContentTitle(content_title);
                builder.setContentText(tmp_msg);
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(tmp_msg));

                Random noti_rnd = new Random();
                int noti_rnd_val = noti_rnd.nextInt(100);

                NotificationManagerCompat noti_manager_q = NotificationManagerCompat.from(this);
                Intent intent_noti = new Intent(getApplicationContext(), MainActivity.class);
                intent_noti.addCategory(Intent.CATEGORY_LAUNCHER);
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext()
                        , 0
                        , intent_noti
                        , Intent.FILL_IN_ACTION);
                if (result_sound.equals("Y")) {
                    builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                } else {
                    builder.setDefaults(Notification.DEFAULT_VIBRATE);
                }
                builder.setContentIntent(pIntent);
                noti_manager_q.notify(noti_rnd_val, builder.build());

            } else {
                String show_popup = "Y";
                //테스트를 위해 임시로 값 세팅
                //block_popup = "Y";
                if (block_popup.equals("Y")) {
                    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    boolean isScreenOn = pm.isScreenOn();
                    if (isScreenOn) {
                        //화면져졌음
                        Log.d("GCMIntentService", "...화면 켜 졌음...");
                        show_popup = "N";
                    } else {
                        Log.d("GCMIntentService", "...화면 꺼 졌음...");
                    }
                }

                if (show_popup.equals("N")) {
                    String[] arr_msg = msg.split("º");
                    String tmp_msg = "";
                    for (int i = 0; i < arr_msg.length; i++) {
                        tmp_msg += arr_msg[i] + "\r\n";
                        //Log.d("GCMIntentService", "arr_msg[i]="+arr_msg[i]);
                    }

                    Log.d("GCMIntentService", "팝업차단으로 설정했는데 마침 현재 화면이 켜져있어서 팝업대신 상단에 알림만");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = "sale info notification";
                        String desc = "할인,할인,주문정보 알람";
                        int impo = NotificationManager.IMPORTANCE_DEFAULT;

                        NotificationChannel noti_channel = new NotificationChannel(CHANNL_ID, name, impo);
                        noti_channel.setDescription(desc);

                        NotificationManager noti_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        noti_manager.createNotificationChannel(noti_channel);
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNL_ID);
                    builder.setSmallIcon(R.drawable.ic_launcher);
                    builder.setContentTitle(content_title);
                    builder.setContentText(tmp_msg);
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(tmp_msg));

                    Random noti_rnd = new Random();
                    int noti_rnd_val = noti_rnd.nextInt(100);

                    NotificationManagerCompat noti_manager = NotificationManagerCompat.from(this);
                    noti_manager.notify(noti_rnd_val, builder.build());

                } else {

                    if (msg_kind.equals("IMGPOPUP")) {
                        Intent i = new Intent("kr.co.dmart.ImgPopup");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        i.putExtra("msg", msg);
                        i.putExtra("conn_url", conn_url);
                        i.putExtra("msg_kind", msg_kind);
                        i.putExtra("shop_seq", shop_seq);
                        i.putExtra("shop_name", shop_name);
                        i.putExtra("is_sound", result_sound);
                        //context.startActivity(i);
                        startActivity(i);
                    } else {
                        Intent i = new Intent("kr.co.dmart.pushpopup");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                                | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.putExtra("msg", msg);
                        i.putExtra("conn_url", conn_url);
                        i.putExtra("msg_kind", msg_kind);
                        i.putExtra("shop_seq", shop_seq);
                        i.putExtra("shop_name", shop_name);
                        i.putExtra("is_sound", result_sound);
                        //context.startActivity(i);
                        startActivity(i);
                    }
                }
            }

            int badgeCount = new_msg;
            //Log.d("GCMIntentService", "badgeCount=2");
            Intent badge_intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            badge_intent.putExtra("badge_count", badgeCount);
            // 메인 메뉴에 나타나는 어플의  패키지 명
            badge_intent.putExtra("badge_count_package_name","kr.co.dmart");
            // 메인메뉴에 나타나는 어플의 클래스 명
            badge_intent.putExtra("badge_count_class_name","kr.co.dmart.MainActivity");
            sendBroadcast(badge_intent);
        }
    }

}