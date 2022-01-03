package kr.co.dmart;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import kr.co.dmart.AndroidBridge;


import android.content.ActivityNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.Manifest;

@SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "Override", "NewApi"})
public class MainActivity extends Activity {

	public static WebView mWebView;

	public static String regId = "";
	public static String fcm_id = "";
	public static String static_web_dv_id = "";
	public static String static_web_shop_seq = "";
	public static String static_web_goods_seq = "";
	public static String static_now_etc_kind = "";
	public static String static_now_etc_detail_kind = "";
	public static String static_member_order_seq = "";
	public static String static_member_order_page = "";
	public static String static_member_order_flag = "";
	public static String static_cook_seq = "";
	public static String static_cook_page = "";
	public static String static_img_src = "";
	public static String static_zoom_img_src = "";
	public static String is_register = "NO";
	public static String is_load = "N";
	public static Context main_context;
	public static Activity main_activity;
	public static boolean is_live = false;
	public static String app_register_url = "https://dnmart.co.kr/daemon/app_start.html";
	public static String fcm_register_url = "https://dnmart.co.kr/daemon/fcm_id_register.html";
	public static String and_agree_url = "https://dnmart.co.kr/daemon/and_agree_register.html";
	public static String app_ready_url = "https://dnmart.co.kr/app/app_ready.html";
	public static String app_phone_not_allow_url = "https://dnmart.co.kr/daemon/app_phone_not_allow.html";
	public static String app_start_url_22 = "https://dnmart.co.kr/app2/app_dir_index.html";
	public static String check_url = "https://dnmart.co.kr/daemon/app_check_status.html";
	public static String device_response_url = "https://dnmart.co.kr/daemon/daemon_device_response.html";
	public static String check_result = "";
	public static String server_status_check_code = "";
	public static String server_status_check_msg = "";
	public static String dv_phone_num = "";
	public static String db_phone_num = "";
	public static String and_id = "";
	public static String static_p_shop_seq = null;

	public static String push_get_msg_kind = "";
	public static String push_get_shop_seq = "";
	public static String location_call_from = "";

	public static int versionNumber = 0;

	private SendMassgeHandler mMainHandler = null;
	public LocationManager mLM;
	private long press_time_1 = 0;
	private long press_time_2 = 0;
	private final long FINSH_INTERVAL_TIME = 1000;
	private Toast toast;
	private int dialog_agree = 0;
	public static boolean TFLAG = false;
	public static String CHANNL_ID = "fcm_noti_channel";
	public static int NOTI_ID = 001;

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

//		Log.d("GCMIntentService", "...MainActivity onCreate...");
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				restart_webview();
			}
		}, 10000);  // 2000은 2초를 의미합니다.
		main_activity = MainActivity.this;
		and_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		Log.d("GCMIntentService", "and_id=" + and_id);

		// 파라메터 - 해당하는 마트 기본설정 2020-11-11
		if (getIntent().getScheme() != null && getIntent().getScheme().equals("romarket")) {
			this.static_p_shop_seq = getIntent().getData().getQueryParameter("shop_seq");
		}
		Log.d("GCMIntentService", "this.static_p_shop_seq : " + this.static_p_shop_seq);

		//String token = FirebaseInstanceId.getInstance().getToken();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			//Log.e( "GCMIntentService", "...Build.VERSION_CODES.O..." );
			/*
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        NotificationChannel notificationChannel = new NotificationChannel("dnmartchannel", "로마켓알림", NotificationManager.IMPORTANCE_DEFAULT);
	        notificationChannel.setDescription("로마켓 알림 안내입니다.");
	        notificationChannel.enableLights(true);
	        notificationChannel.setLightColor(Color.GREEN);
	        notificationChannel.enableVibration(true);
	        notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
	        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
	        notificationManager.createNotificationChannel(notificationChannel);
	        */

			//TFLAG = true;
			//Intent backStartIntent = new Intent(MainActivity.this, GoBackgroundService.class);
			//backStartIntent.setAction("Action1");
			//startForegroundService(backStartIntent);
		}


		super.onCreate(savedInstanceState);

		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(6055);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ready);
		startActivity(new Intent(this, SplashActivity.class));
		mMainHandler = new SendMassgeHandler();
		findViewById(R.id.id_btn_exit).setOnClickListener(mClickListener);
		//check_server_state();
		String token = "";

		try {
			PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionNumber = pinfo.versionCode;
			//String versionName = pinfo.versionName;
		} catch (NameNotFoundException e) {
			Log.d("GCMIntentService", "NameNotFoundException..." + e.toString());
		}

		FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
			@Override
			public void onSuccess(InstanceIdResult instanceIdResult) {
				fcm_id = instanceIdResult.getToken();
				if ("N".equals(kr.co.dmart.MainActivity.is_load)) {
					// start_webview();
					new CheckServerStatus().execute(null, null, null);
				}
			}
		});

		Log.e("fcm gcm", "fcm[" + fcm_id + "],gcm[" + regId + "]");

	}

	Button.OnClickListener mClickListener = new View.OnClickListener() {
		public void onClick(View v) {
//			Log.d("GCMIntentService", "...onClick...");
			switch (v.getId()) {
				case R.id.id_btn_exit:
					main_activity.finish();
					is_live = false;
					break;
			}
		}
	};


	private class CheckServerStatus extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... arg) {
			//Log.d("GCMIntentService", "...CheckServerStatus...");
			try {

				StringBuffer strbuf = new StringBuffer();
				//Context context = getApplicationContext();
				//TelephonyManager telManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE); 
				//dv_phone = telManager.getLine1Number();
				strbuf.append(kr.co.dmart.MainActivity.check_url).append("?dv_id=").append(kr.co.dmart.MainActivity.regId).append("&dv_kind=android");
				String url = strbuf.toString();
				Log.d("GCMIntentService", "in CheckServerStatus check_url=" + url);

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

				check_result = builder.toString();

				//check_result = result.toString();
				Log.d("GCMIntentService", "check_result=" + check_result);
				//결과값 파싱
				String[] arr_str;
				arr_str = check_result.split("\\|");
				if (arr_str.length > 1) {
					server_status_check_code = arr_str[0];
					server_status_check_msg = arr_str[1];
					db_phone_num = arr_str[2];
				}

			} catch (Exception e) {
				//Log.d("GCMIntentService", "...Exception in CheckServerStatus...");
				//Log.d("GCMIntentService", "check_result="+e.toString());
				server_status_check_code = "NO";
				server_status_check_msg = "서버접속 오류입니다. 잠시후 다시 접속해 주십시요.";
				//show_err_msg("서버접속 오류입니다.\n잠시후 다시 접속해 주십시요.");
				//TextView myTextView = (TextView)findViewById(R.id.id_ready_ment);
				//Log.d("GCMIntentService", "...myTextView...");
				//myTextView.setText(R.string.conn_timeout_ment);
				//Log.d("GCMIntentService", "...setText...");
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			if (server_status_check_code.equals("OK")) {
				//check_regId();
				//if("".equals(fcm_id)) {
				//	app_start_url_22 = "https://dnmart.co.kr/app/app_ready.html";
				//;
				start_webview();
			}
			if (server_status_check_code.equals("NO")) {
				show_err_msg(server_status_check_msg);
			}

		}

	}

	private class send_phone_num extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... arg) {
			try {

				StringBuffer strbuf = new StringBuffer();
				String dv_phone = kr.co.dmart.MainActivity.dv_phone_num;
				//Context context = getApplicationContext();
				//TelephonyManager telManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE); 
				//dv_phone = telManager.getLine1Number();
				strbuf.append(kr.co.dmart.MainActivity.and_agree_url).append("?group_id=&dv_id=").append(kr.co.dmart.MainActivity.regId).append("&dv_phone=").append(dv_phone);
				strbuf.append("&dv_kind=android").append("&and_id=").append(kr.co.dmart.MainActivity.and_id).append("&fcm_id=").append(kr.co.dmart.MainActivity.fcm_id);

				String url = strbuf.toString();

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

				Log.d("GCMIntentService", "...전화번호 전송완료...");

			} catch (Exception e) {
			}
			return null;
		}

		protected void onPostExecute(Void result) {
		}

	}

	class SendMassgeHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
//            super.handleMessage(msg);

//            Log.d("GCMIntentService", "SendMassgeHandler in...");
			switch (msg.what) {
				case 1:
					show_err_msg("서버연결시 오류가 발생하였습니다.\n잠시후 다시 접속해 주십시요.1");
					break;

				case 2:
					show_err_msg("서버연결시 오류가 발생하였습니다.\n잠시후 다시 접속해 주십시요.2");
					break;

				case 3:
					show_err_msg("서버연결시 오류가 발생하였습니다.\n잠시후 다시 접속해 주십시요.3");
					break;

				case 4:
					show_err_msg("서버연결시 오류가 발생하였습니다.\n잠시후 다시 접속해 주십시요.4");
					break;

				default:
					break;
			}
		}

	}

	public void show_err_msg(String msg) {
		if (msg == null || msg.equals("")) {
		} else {
			toast = Toast.makeText(getApplicationContext(), "서버접속 오류입니다. 네트웍 상태를 확인하시거나 네트웍은 문제가 없으실 경우 서버작업중일수 있으니 잠시후에 다시 접속해 주십시요.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			//TextView myTextView = (TextView)findViewById(R.id.id_ready_ment);
			//myTextView.setText(msg);
		}
	}

	public void try_get_user_phone_num() {
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 3);
	}

	private class phone_access_not_allow extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... arg) {
			Log.d("GCMIntentService", "...in phone_access_not_allow...");
			try {

				StringBuffer strbuf = new StringBuffer();
				Log.d("GCMIntentService", "...conn url=" + kr.co.dmart.MainActivity.app_phone_not_allow_url);
				strbuf.append(kr.co.dmart.MainActivity.app_phone_not_allow_url).append("?group_id=&dv_id=").append(kr.co.dmart.MainActivity.regId).append("&dv_kind=android");
				String url = strbuf.toString();
				Log.d("GCMIntentService", "...conn url=" + strbuf.toString());

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
				Log.d("GCMIntentService", "...send phone_access_not_allow...");

			} catch (Exception e) {
				Log.d("GCMIntentService", "...err phone_access_not_allow..." + e.toString());
			}
			return null;
		}
	}

	public void check_phone_allow() {

	}

	public void start_webview() {
		setContentView(R.layout.activity_main);
		main_context = this;

		setLayout();

		// 웹뷰에서 자바스크립트실행가능
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setBuiltInZoomControls(false);
		mWebView.getSettings().setSupportZoom(false);
		mWebView.addJavascriptInterface(new AndroidBridge(), "android");
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//window.open허용
		mWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND); // 플러그인을 사용할 수 있도록 설정
		// 임의의 WebChromeClient 객체를 생성하여 WebView에 등록.
		// 여기의 Chrome은 브라우저 창 주의를 정리하는 모든 작업을 일컬음. 즉, 브라우저 처리을 할 클라이언트를 등록
		//여기부터 kcp 결제를 위해 추가한 부분
		/*
        mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        */
		//여기까지
		//mWebView.loadUrl(conn_url);
		mWebView.setWebChromeClient(new CustomWebChromeClient());
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
				if (!request.getUrl().toString().startsWith("http://") && !request.getUrl().toString().startsWith("https://") && !request.getUrl().toString().startsWith("javascript:")) {
					Intent intent = null;
					try {
						intent = Intent.parseUri(request.getUrl().toString(), Intent.URI_INTENT_SCHEME);
						Uri uri = Uri.parse(intent.getDataString());
						view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));

						return true;
					} catch (URISyntaxException e) {
						return false;
					} catch (ActivityNotFoundException e) {
						if (intent == null) {
							return false;
						}

						String packageName = intent.getPackage();
						if (packageName != null) {
							view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
							return true;
						}
						return false;
					}
				}
				return super.shouldOverrideUrlLoading(view, request);
			}
		});
		//String conn_url = kr.co.dmart.MainActivity.app_start_url_22;
		//conn_url += "?group_id=&dv_id="+kr.co.dmart.MainActivity.regId+"&and_id="+kr.co.dmart.MainActivity.and_id;
		//conn_url += "&dv_kind=android&fcm_id"+kr.co.dmart.MainActivity.fcm_id+"&dv_ver="+kr.co.dmart.MainActivity.versionNumber;
		if ("".equals(fcm_id) && "".equals(regId)) {
			mWebView.loadUrl("https://dnmart.co.kr/app/app_ready.html");
			Log.d("GCMIntentService", "...app_ready.html...");
			kr.co.dmart.MainActivity.is_load = "N";
		} else {

			String conn_url = kr.co.dmart.MainActivity.app_start_url_22;
			// conn_url = "http://192.168.0.14:8080/display/main";
			conn_url += "?dv_kind=android&group_id=";
			conn_url += "&dv_id=" + kr.co.dmart.MainActivity.regId;
			conn_url += "&and_id=" + kr.co.dmart.MainActivity.and_id;
			conn_url += "&fcm_id=" + kr.co.dmart.MainActivity.fcm_id;
			conn_url += "&dv_ver=" + kr.co.dmart.MainActivity.versionNumber;

			// 파라메터 - 해당하는 마트 기본설정 2020-11-11
			if (this.static_p_shop_seq != null && !this.static_p_shop_seq.isEmpty()) {
				conn_url += "&p_shop_seq=" + this.static_p_shop_seq;
			}
			Log.d("GCMIntentService", "conn_url : " + conn_url);

			mWebView.loadUrl(conn_url);
			Log.d("GCMIntentService", "...app_dir_index.html...");
			Log.d("GCMIntentService", kr.co.dmart.MainActivity.regId + "===" + kr.co.dmart.MainActivity.fcm_id);
			kr.co.dmart.MainActivity.is_load = "Y";
		}

		is_live = true;
	}

	public void restart_webview() {
		Log.d("GCMIntentService", "...restart_webview..." + is_load + "...fcm_id=" + fcm_id);
		if ("N".equals(kr.co.dmart.MainActivity.is_load)) {
			if ("".equals(fcm_id)) {
				Log.d("GCMIntentService", "...restart_webview 인데 아직도 fcm이 안 구해졌음..." + is_load + "...fcm_id=");
			} else {
				mWebView.loadUrl("https://dnmart.co.kr/app2/app_dir_index.html?dv_kind=android&group_id=&dv_id=" + kr.co.dmart.MainActivity.regId + "&and_id=" + kr.co.dmart.MainActivity.and_id + "&fcm_id=" + kr.co.dmart.MainActivity.fcm_id + "&dv_ver=" + kr.co.dmart.MainActivity.versionNumber);
				// mWebView.loadUrl("http://192.168.0.14:8080/diaply/main?dv_kind=android&group_id=&dv_id=" + kr.co.dmart.MainActivity.regId + "&and_id=" + kr.co.dmart.MainActivity.and_id + "&fcm_id=" + kr.co.dmart.MainActivity.fcm_id + "&dv_ver=" + kr.co.dmart.MainActivity.versionNumber);
				kr.co.dmart.MainActivity.is_load = "Y";
			}
		}
	}

	@Override
	public void onNewIntent(Intent newIntent) {
		//super.onNewIntent(newIntent);
	}

	public void set_webview_url(String url) {
//		mWebView.loadUrl(url);
		mWebView.loadUrl("javascript:from_app_to_web('Hello from Android')");

	}

	public void response_dv_id(String call_fromm) {
		//Log.d("GCMIntentService", "call response_dv_id function...");
		if ("".equals(regId)) {
			mWebView.loadUrl("javascript:app_response_dv_id('" + this.fcm_id + "','" + call_fromm + "')");
		} else {
			mWebView.loadUrl("javascript:app_response_dv_id('" + this.regId + "','" + call_fromm + "')");
		}
		//Log.d("GCMIntentService", "app_response_dv_id javascript called!!");
	}

	public void response_and_id(String call_fromm) {
		//Log.d("GCMIntentService", "call response_dv_id function...");
		mWebView.loadUrl("javascript:app_response_and_id('" + this.and_id + "','" + call_fromm + "')");
		//Log.d("GCMIntentService", "app_response_dv_id javascript called!!");
	}

	public void response_ver(String call_fromm) {
		//Log.d("GCMIntentService", "call response_ver = "+versionNumber);
		mWebView.loadUrl("javascript:app_response_ver('" + versionNumber + "','" + call_fromm + "')");
	}

	public void exit_app() {
		is_live = false;
		main_activity.finish();
		this.finish();
	}

	public void and_url_window_open(String str_url) {
		Log.d("GCMIntentService", "call and_url_window_open = " + str_url);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(str_url));
		startActivity(intent);
	}

	public void app_slide_go_back() {
		Log.d("GCMIntentService", "call app_slide_go_back = ");
		mWebView.goBack();
		Log.d("GCMIntentService", "after call app_slide_go_back = ");
	}

	public void get_now_location(String call_fromm) {
		location_call_from = call_fromm;
		/*
		if (ContextCompat.checkSelfPermission(main_activity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(main_activity,Manifest.permission.ACCESS_FINE_LOCATION)) {
				location_call_from = call_fromm;
				//LocationManager mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Log.d("GCMIntentService", "call get_now_location");
				mLM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
				mLM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
			} else {
				Log.d("GCMIntentService", "...requestPermissions...");
		        ActivityCompat.requestPermissions(main_activity,
		                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
		                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

				//toast = Toast.makeText(getApplicationContext(),"위치 정보를 허용해 주셔야 가까운 주변마트가 검색됩니다", Toast.LENGTH_LONG);
				//toast.setGravity(Gravity.CENTER, 0, 0);
				//toast.show();
		    }
		} else {
			Log.d("GCMIntentService", "...111 requestPermissions 111...");
	        ActivityCompat.requestPermissions(main_activity,
	                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
	                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
			
		}
		*/
		if (Build.VERSION.SDK_INT >= 23) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
		} else {
			mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Log.d("GCMIntentService", "call get_now_location");
			mLM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
			mLM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
		}
		/*
		location_call_from = call_fromm;
		//LocationManager mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Log.d("GCMIntentService", "call get_now_location");
		mLM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
		mLM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
		//mLM.removeUpdates(mLocationListener);
		 */

	}

	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//LocationManager mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					Log.d("GCMIntentService", "call get_now_location");
					mLM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
					mLM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
				} else {
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					toast = Toast.makeText(getApplicationContext(), "위치 정보를 허용해 주셔야 가까운 주변마트가 검색됩니다", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				return;
			}

			case 2: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					//startActivityForResult(intent, 0);
					IntentIntegrator integrator = new IntentIntegrator(this);
					integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
					integrator.setPrompt("붉은선에 바코드를 맞춰주세요.");
					integrator.setCameraId(0);
					integrator.setBeepEnabled(true);
					integrator.setBarcodeImageEnabled(false);
					integrator.initiateScan();
				} else {
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					toast = Toast.makeText(getApplicationContext(), "카메라를 허용해 주세요", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				return;
			}

			case 3: {
	        	return;
	        }
	        
	            // other 'case' lines to check for other
	            // permissions this app might request
	    }
	}
	
	public void response_show_cart_activity(String web_dv_id, String web_shop_seq)
	{
		this.static_web_dv_id = web_dv_id;
		this.static_web_shop_seq = web_shop_seq;
		Intent intent = new Intent(
                getApplicationContext(), // 현재 화면의 제어권자
                cart_activity.class); // 다음 넘어갈 클래스 지정
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
	}
	
	//response_show_zoom_img
	public void response_show_zoom_img(String zoom_img_src)
	{
		this.static_zoom_img_src = zoom_img_src;
		Intent intent = new Intent(
                getApplicationContext(), // 현재 화면의 제어권자
                ZoomImg_activity.class); // 다음 넘어갈 클래스 지정
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
	}
	
	public void response_show_goods_activity(String web_dv_id, String web_shop_seq, String web_goods_seq)
	{
		//Log.d("GCMIntentService", "response_show_goods_activity..."+web_dv_id+":"+web_shop_seq+":"+web_goods_seq);
		this.static_web_dv_id = web_dv_id;
		this.static_web_shop_seq = web_shop_seq;
		this.static_web_goods_seq = web_goods_seq;
		//Log.d("GCMIntentService", "11response_show_goods_activity..."+this.static_web_dv_id+":"+this.static_web_shop_seq+":"+this.static_web_goods_seq);
		Intent intent = new Intent(
                getApplicationContext(), // 현재 화면의 제어권자
                goods_activity.class); // 다음 넘어갈 클래스 지정
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
	}

	public void response_show_mypage_activity(String web_dv_id, String web_shop_seq)
	{
		this.static_web_dv_id = web_dv_id;
		this.static_web_shop_seq = web_shop_seq;
		Intent intent = new Intent(
                getApplicationContext(), // 현재 화면의 제어권자
                Mypage_activity.class); // 다음 넘어갈 클래스 지정
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
	}
	
	public void response_reload_app_index_from_mypage(String web_dv_id, String web_shop_seq)
	{
		mWebView.loadUrl("javascript:reload_app_index()");
	}

	public void show_msg_noti()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "sale info notification";
			String desc = "할인,할인,주문정보 알람";
			int impo = NotificationManager.IMPORTANCE_DEFAULT;

			NotificationChannel noti_channel = new NotificationChannel(CHANNL_ID,name,impo);
			noti_channel.setDescription(desc);

			NotificationManager noti_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			noti_manager.createNotificationChannel(noti_channel);
		}
		dispay_noti();
	}

	public void dispay_noti()
	{
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNL_ID);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle("로마켓 알람입니다");
		builder.setContentText("다시는 못볼 오늘의 할인 상품 \n 깜놀깜놀 \n 완전 대박");
		builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

		NotificationManagerCompat noti_manager = NotificationManagerCompat.from(this);
		noti_manager.notify(NOTI_ID,builder.build());

	}
	
	public void android_call_show_footer() {
		//Log.d("GCMIntentService", "call response_dv_id function...");
		//mWebView.loadUrl("javascript:show_footer_info()");
		mWebView.loadUrl("javascript:update_bottom_cnt()");
		//Log.d("GCMIntentService", "app_response_dv_id javascript called!!");
	}
	
	public void response_update_cart_cnt(String web_shop_seq)
	{
		mWebView.loadUrl("javascript:update_cart_cnt('"+web_shop_seq+"')");
	}
	
	public void response_login_comp()
	{
		mWebView.loadUrl("javascript:login_comp()");
	}
	
	public void get_new_msg_cnt_to_bottom(String cnt)
	{
		mWebView.loadUrl("javascript:update_new_msg_cnt_to_bottom('"+cnt+"')");
	}
	
	public void response_show_sale_msg(String web_dv_id, String web_shop_seq, String now_etc_kind) 
	{
		this.static_web_dv_id = web_dv_id;
		this.static_web_shop_seq = web_shop_seq;
		this.static_now_etc_kind = now_etc_kind;
		Intent intent = new Intent(
                getApplicationContext(), // 현재 화면의 제어권자
                etc_activity.class); // 다음 넘어갈 클래스 지정
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
	}
	
	public void response_show_sale_image(String img_src)
	{
		this.static_img_src = img_src;
		Intent intent = new Intent(
                getApplicationContext(), // 현재 화면의 제어권자
                etc_activity.class); // 다음 넘어갈 클래스 지정
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
	}
	
	public void response_phone_call(String phone_num)
	{
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
		callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	callIntent.setData(Uri.parse("tel:"+phone_num));
    	main_context.startActivity(callIntent);
	}
	
    public void direct_response_show_mypage_sub(String web_dv_id, String web_shop_seq, String show_kind)
    {
  		 Intent intent = new Intent(getApplicationContext(), etc_activity.class);
  		 startActivity(intent);
  		 overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    public void request_vibrate(String vib_time) 
	{
		
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		if (vibrator.hasVibrator()) {
			int i_vib_time = Integer.parseInt(vib_time);
			vibrator.vibrate(i_vib_time); 
		}

	}
    
    public void resonse_move_back () {
    	moveTaskToBack(true); 
    }
	
	public void response_show_recipe_detail(String web_dv_id, String web_shop_seq, String cook_seq, String page)
	{
    	kr.co.dmart.MainActivity.static_web_dv_id = web_dv_id;
    	kr.co.dmart.MainActivity.static_cook_seq = cook_seq;
    	kr.co.dmart.MainActivity.static_cook_page = page;
    	kr.co.dmart.MainActivity.static_web_shop_seq = web_shop_seq;
    	kr.co.dmart.MainActivity.static_now_etc_detail_kind = "recipe_detail";
		//Log.d("GCMIntentService", "response_show_goods_activity..."+web_dv_id+":"+web_shop_seq+":"+web_goods_seq);
		//Log.d("GCMIntentService", "11response_show_goods_activity..."+this.static_web_dv_id+":"+this.static_web_shop_seq+":"+this.static_web_goods_seq);
 		 Intent intent = new Intent(getApplicationContext(), etc_sub_activity.class);
   		 startActivity(intent);
   		 overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
	}
	
	public void request_send_share(String web_dv_id, String web_shop_seq, String share_kind, String share_msg) 
	{
		this.static_web_dv_id = web_dv_id;
		this.static_web_shop_seq = web_shop_seq;
		
		if (share_kind.equals("SMS")) { //정상적으로 접속되지 않은 상태에서는 goBack 시 오류가 발생함.
			try{
	            Uri smsUri = Uri.parse("sms:");
	            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
	            share_msg = share_msg.replace("@","\n");
	            sendIntent.putExtra("sms_body", share_msg);
	            startActivity(sendIntent);
	 
//	        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//	        sendIntent.putExtra("address", number);
//	        sendIntent.putExtra("sms_body", editBody.getText().toString());
//	        sendIntent.setType("vnd.android-dir/mms-sms");
//	        startActivity(sendIntent);
	        }catch (Exception e){
	            e.printStackTrace();
	        }
		} else if (share_kind.equals("EMAIL")) {
			try{
				share_msg = share_msg.replace("@","\r\n");
				final Intent shareIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "무거운 장바구니 이제그만~ 마트가기 귀찮을땐~!");
				shareIntent.putExtra(Intent.EXTRA_TEXT, share_msg);
				startActivity(shareIntent);
	        }catch (Exception e){
	        	toast.makeText(getApplicationContext(), "...이메일 어플이 존재하지 않습니다...", Toast.LENGTH_SHORT).show();
	            e.printStackTrace();
	        }
		} else if (share_kind.equals("FACEBOOK")) {
			String urlToShare = share_msg;
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
			intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

			boolean facebookAppFound = false;
			List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
			for (ResolveInfo info : matches) {
			    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
			        intent.setPackage(info.activityInfo.packageName);
			        facebookAppFound = true;
			        break;
			    }
			}

			// As fallback, launch sharer.php in a browser
			if (facebookAppFound == false) {
//				toast.makeText(getApplicationContext(), "...페이스북 어플을 설치해 주세요...", Toast.LENGTH_SHORT).show();
			    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
			    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
			    startActivity(intent);
			} else {
			    startActivity(intent);
			}

			
		} else if (share_kind.equals("TWITTER")) {
			share_msg = share_msg.replace("@","\r\n");
			String strLink = null;
	        try {
	            strLink = String.format("http://twitter.com/intent/tweet?text=%s", 
	                    URLEncoder.encode(share_msg, "utf-8"));
	        } catch (UnsupportedEncodingException e1) {
	            e1.printStackTrace();
	        }

	        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strLink));
	        startActivity(intent);
		} else if (share_kind.equals("KAKAO")) {
			share_msg = share_msg.replace("@","\r\n");
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");

			boolean kakaokAppFound = false;
			List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
			for (ResolveInfo info : matches) {
			    if (info.activityInfo.packageName.toLowerCase().startsWith("com.kakao.talk")) {
			        intent.setPackage(info.activityInfo.packageName);
			        kakaokAppFound = true;
			        break;
			    }
			}

			// As fallback, launch sharer.php in a browser
			if (kakaokAppFound == false) {
				toast.makeText(getApplicationContext(), "...카카오톡 어플을 설치해 주세요...", Toast.LENGTH_SHORT).show();
			} else {
				 intent.putExtra(Intent.EXTRA_TEXT, share_msg); 
				 startActivity(intent);
			}
		}

	}

	private final LocationListener mLocationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			double longitude;    //경도
			double latitude;         //위도
			float accuracy;        //신뢰도
		    if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
				//Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
				longitude = location.getLongitude();    //경도
				latitude = location.getLatitude();         //위도
				accuracy = location.getAccuracy();        //신뢰도
				Log.d("GCMIntentService", "gps기반 위도="+latitude+", 경도="+longitude);
				mWebView.loadUrl("javascript:response_my_location('"+longitude+"','"+latitude+"','"+accuracy+"','GPS','"+location_call_from+"')");
		    } else {
				//Network 위치제공자에 의한 위치변화
				//Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
				longitude = location.getLongitude();    //경도
				latitude = location.getLatitude();         //위도
				accuracy = location.getAccuracy();        //신뢰도
				Log.d("GCMIntentService", "wifi기반 위도="+latitude+", 경도="+longitude);
				mWebView.loadUrl("javascript:response_my_location('"+longitude+"','"+latitude+"','"+accuracy+"','WIFI','"+location_call_from+"')");
		    }
//		    Log.d("GCMIntentService", "NETWORK_PROVIDER="+LocationManager.NETWORK_PROVIDER);
		    mLM.removeUpdates(mLocationListener);
		    //Log.d("GCMIntentService", "...mLM is removeUpdates...=");
		    
		}
		public void onProviderDisabled(String provider) {
		}
		
		public void onProviderEnabled(String provider) {
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	
	public void get_new_msg_cnt(String cnt) {
		//Log.d("GCMIntentService", "call response_ver = "+versionNumber);
		//mWebView.loadUrl("javascript:app_response_ver('"+versionNumber+"','"+call_fromm+"')");
		int iCnt = Integer.parseInt(cnt);
    	Intent badge_intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
    	badge_intent.putExtra("badge_count", iCnt);
    	// 메인 메뉴에 나타나는 어플의  패키지 명
    	badge_intent.putExtra("badge_count_package_name","kr.co.dmart");
    	// 메인메뉴에 나타나는 어플의 클래스 명
    	badge_intent.putExtra("badge_count_class_name","kr.co.dmart.MainActivity");
    	sendBroadcast(badge_intent);
	}
	
	public void run_scan() {
		if (Build.VERSION.SDK_INT>=23) {
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 2);
		} else {
			//intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

			/*
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			startActivityForResult(intent, 0);
			*/


			IntentIntegrator integrator = new IntentIntegrator(this);
			integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
			integrator.setPrompt("붉은선에 바코드를 맞춰주세요.");
			integrator.setCameraId(0);
			integrator.setBeepEnabled(true);
			integrator.setBarcodeImageEnabled(false);
			integrator.initiateScan();
		}
		/*
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		//intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(intent, 0);
		*/
	}
	
	public void response_scan_barcode_value(String str_bar_code) {
		mWebView.loadUrl("javascript:response_bar_code('"+str_bar_code+"')");
	}
	
	@Override  
	protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
		// handle scan result
		Log.d("GCMIntentService", "...requestCode="+requestCode+",resultCode="+resultCode);
		if (resultCode != 0) {
			String contents = intent.getStringExtra("SCAN_RESULT");
			Log.d("GCMIntentService", "...contents="+contents);
			String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
			if (contents != null) {
				Log.d("GCMIntentService", "...contents="+contents);
				Log.d("GCMIntentService", "...format="+format);
				//QR코드 말고 바코드만 처리...EAN_13
				if (format.equals("EAN_13")) {
					response_scan_barcode_value(contents);
				}
	//			Log.d("GCMIntentService", "...handle scan result success!!!...");
	//			Log.d("GCMIntentService", "requestCode="+scanResult.toString());
			} else {
				Log.d("GCMIntentService", "...contents is null...");
			}
		}
	}
	
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	boolean call_goback = true;
    	if (server_status_check_code.equals("OK")) { //정상적으로 접속되지 않은 상태에서는 goBack 시 오류가 발생함.
	    	if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) { 
	            //mWebView.goBack();
	        	long temp_time        = System.currentTimeMillis();
	        	if (press_time_1 == 0) {
	        		//최초 백키 클릭
	        		press_time_1 = temp_time; 
	        	} else {
	        		if (press_time_2 == 0) {
	        			//백키 두번 처음
	        			if (press_time_1 > (temp_time-FINSH_INTERVAL_TIME)) {
	        				//Toast.makeText(getApplicationContext(),"'뒤로'버튼을 빠르게 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
	        				toast = Toast.makeText(getApplicationContext(),"'뒤로'버튼을 빠르게 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
	        				toast.setGravity(Gravity.CENTER, 0, 0);
	        				toast.show();
	        				call_goback = false;
	        			}
	        			press_time_2 = temp_time;
	        		} else {
	        			if (press_time_2 > (temp_time-FINSH_INTERVAL_TIME)) {
	        				main_activity.finish();
	        				this.finish();
//	        				toast.cancel();
	        			} else {
	        				press_time_1 = temp_time;
		        			press_time_2 = 0;
		        			//Log.d("GCMIntentService", "빠르게 누르지 않아 리셋");
	        			}
	        		}
	        	}
	    		
	        	if (call_goback == true) {
	        		mWebView.loadUrl("javascript:android_not_sup_goback()");
	        	}
	            return true; 
	        }
    	}
        return super.onKeyDown(keyCode, event);
    }
     
    private class WebViewClientClass extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            //view.loadUrl(url);
        	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true; 
        } 
    }
     
    /*
     * Layout
     */
    private void setLayout(){
        mWebView = (WebView) findViewById(R.id.webview);
    }

	public void appendLog(String text)
	{
		/*
		File logFile = new File("/storage/sdcard0/Download/dnmart.txt");
		if (!logFile.exists())
		{
			try
			{
				logFile.createNewFile();
			}
			catch (IOException e)
			{
				// 적절한 예외처리를 해주면됩니다.
				e.printStackTrace();
			}
		}
		try
		{
			//퍼포먼스를 위해 BufferedWriter를 썼고 FileWriter의 true는 파일을 이어쓸 수 있게 하기 위해서 해줬습니다.
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
			buf.append(text);
			buf.newLine();
			buf.close();
		}
		catch (IOException e)
		{
			// 적절한 예외처리를 해주면됩니다.
			e.printStackTrace();
		}
		*/
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public static Context getMainAppContext() {
        return main_context;
    }

	public static void call_finish() {
		is_live = false;
		main_activity.finish();
	}
	
	public class CustomWebChromeClient extends WebChromeClient {
		@Override
		   public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
		      AlertDialog dialog = new AlertDialog.Builder(view.getContext()).
		    		  setTitle("로마켓").
		              setMessage(message).
		              setPositiveButton("OK", new DialogInterface.OnClickListener() {
		                 @Override
		                 public void onClick(DialogInterface dialog, int which) {
		                    //do nothing
		                 }
		              }).create();
		      dialog.show();
		      result.confirm();
		      return true;
		  }
		@Override
	    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
	        new AlertDialog.Builder(view.getContext())
	        .setTitle("로마켓")
	        .setMessage(message)
	        .setPositiveButton(android.R.string.ok,
	                new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int which)
	            {
	                result.confirm();
	            }
	        })
	        .setNegativeButton(android.R.string.cancel,
	                new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface dialog, int which)
	            {
	                result.cancel();
	            }
	        })
	        .create()
	        .show();

	        return true;
	    }
	}
	
}
