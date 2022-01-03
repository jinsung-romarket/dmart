package kr.co.dmart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class etc_activity extends Activity {
	
	public static Activity etc_activity;
	public static WebView etc_activity_webview;
	public static Context etc_context;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
		 Log.d("GCMIntentService", "etc_activity...");
		 etc_activity = etc_activity.this;
		 etc_context = this;
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_etc);  // layout xml 과 자바파일을 연결
		 String show_kind = kr.co.dmart.MainActivity.static_now_etc_kind;
		 TextView textView1 =(TextView)findViewById(R.id.etc_left_title);
		 textView1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		 if (show_kind.equals("member_edit")) {
			 //회원정보수정
			 textView1.setBackgroundColor(Color.parseColor("#53DDDD"));
			 textView1.setText("회원정보수정");
		 } else if (show_kind.equals("member_register")) {
			 textView1.setBackgroundColor(Color.parseColor("#53DDDD"));
			 textView1.setText("회원가입");
		 } else if (show_kind.equals("order_list")) {
			 textView1.setBackgroundColor(Color.parseColor("#23262E"));
			 textView1.setText("주문내역");
		 } else if (show_kind.equals("msg_list")) {
			 textView1.setBackgroundColor(Color.parseColor("#FFA726"));
			 textView1.setText("메세지함");
		 } else if (show_kind.equals("point_list")) {
			 textView1.setBackgroundColor(Color.parseColor("#FFA726"));
			 textView1.setText("포인트");
		 } else if (show_kind.equals("coupon_list")) {
			 textView1.setBackgroundColor(Color.parseColor("#23262E"));
			 textView1.setText("쿠폰함");
		 } else if (show_kind.equals("save_card")) {
			 textView1.setBackgroundColor(Color.parseColor("#FFA726"));
			 textView1.setText("마트적립카드");
		 } else if (show_kind.equals("question_list")) {
			 textView1.setBackgroundColor(Color.parseColor("#1A8CE7"));
			 textView1.setText("고객센터");
		 } else if (show_kind.equals("member_out")) {
			 textView1.setBackgroundColor(Color.parseColor("#53DDDD"));
			 textView1.setText("회원탈퇴");
		 } else if (show_kind.equals("member_out")) {
			 textView1.setBackgroundColor(Color.parseColor("#53DDDD"));
			 textView1.setText("회원탈퇴");
		 } else if (show_kind.equals("sale_msg")) {
			 textView1.setBackgroundColor(Color.parseColor("#53DDDD"));
			 textView1.setText("세일메세지");
		 } else if (show_kind.equals("mart_info")) {
			 textView1.setBackgroundColor(Color.parseColor("#53DDDD"));
			 textView1.setText("마트정보");
		 } else if (show_kind.equals("com_info")) {
			 textView1.setBackgroundColor(Color.parseColor("#53DDDD"));
			 textView1.setText("회사소개");
		 } else if (show_kind.equals("rull_info")) {
			 textView1.setBackgroundColor(Color.parseColor("#53DDDD"));
			 textView1.setText("이용약관");
		 } else if (show_kind.equals("private_rull")) {
			 textView1.setBackgroundColor(Color.parseColor("#53DDDD"));
			 textView1.setText("개인정보취급방침");
		 } else if (show_kind.equals("faq")) {
			 textView1.setBackgroundColor(Color.parseColor("#00A776"));
			 textView1.setText("FAQ");
		 } else if (show_kind.equals("recipe")) {
			 textView1.setBackgroundColor(Color.parseColor("#FF5B7E"));
			 textView1.setText("레시피");
		 } else if (show_kind.equals("recipe_detail")) {
			 textView1.setBackgroundColor(Color.parseColor("#FF5B7E"));
			 textView1.setText("레시피");
		 } else if (show_kind.equals("sale_img")) {
			 textView1.setBackgroundColor(Color.parseColor("#53DDDD"));
			 textView1.setText("전단보기");
		 } else if (show_kind.equals("order_reserve")) {
			 textView1.setBackgroundColor(Color.parseColor("#FFA726"));
			 textView1.setText("예약주문");
		 }
		 start_etc_webview(show_kind);
	        
	        //start_goods_webview();
	        
	    }

     public void back_click(View v) {
         switch(v.getId()) {
         case R.id.etc_left_title:
        	 //Toast.makeText(getApplicationContext(), "돌아가기.", 1000).show();
             finish();
             overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
             break;
         }
     }
     
     public void start_etc_webview(String show_kind) {
    	 
    	 //Log.d("GCMIntentService", "22response_show_goods_activity..."+kr.co.dmart.MainActivity.static_web_dv_id+":"+kr.co.dmart.MainActivity.static_web_shop_seq+":"+kr.co.dmart.MainActivity.static_web_goods_seq);
    	 String conn_url = "";
    	 if (show_kind.equals("sale_img")) {
    		 conn_url = "https://dnmart.co.kr/app2/app_2_i_img_zoom.html?img_src="+kr.co.dmart.MainActivity.static_img_src;
    	 } else {
    		 conn_url = "https://dnmart.co.kr/app2/app_22_base_etc_main.html?show_kind="+show_kind+"&dv_id="+kr.co.dmart.MainActivity.static_web_dv_id+"&shop_seq="+kr.co.dmart.MainActivity.static_web_shop_seq+"&goods_seq="+kr.co.dmart.MainActivity.static_web_goods_seq;
    	 }
 		
    	 setLayout();
 	        
         // 웹뷰에서 자바스크립트실행가능
    	 etc_activity_webview.getSettings().setJavaScriptEnabled(true);
    	 etc_activity_webview.getSettings().setBuiltInZoomControls(false);
    	 etc_activity_webview.getSettings().setSupportZoom(false);
    	 etc_activity_webview.addJavascriptInterface(new AndroidBridge(), "android");
    	 etc_activity_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    	 etc_activity_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically (true);//window.open허용
    	 etc_activity_webview.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND); // 플러그인을 사용할 수 있도록 설정
         // 임의의 WebChromeClient 객체를 생성하여 WebView에 등록.
         // 여기의 Chrome은 브라우저 창 주의를 정리하는 모든 작업을 일컬음. 즉, 브라우저 처리을 할 클라이언트를 등록
 	
    	 //etc_activity_webview.loadUrl(conn_url);
    	 etc_activity_webview.setWebChromeClient(new CustomWebChromeClient());
    	 etc_activity_webview.setWebViewClient(new WebViewClient() {
         });
    	 etc_activity_webview.loadUrl(conn_url);
 	}

     private void setLayout(){
    	 etc_activity_webview = (WebView) findViewById(R.id.etc_webview);
     }
     
     public void response_show_order_detail(String web_dv_id, String web_shop_seq, String member_order_seq, String page, String order_flag)
     {
  		 Intent intent = new Intent(getApplicationContext(), etc_sub_activity.class);
   		 startActivity(intent);
   		 overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
     }
     
     public void response_show_recipe_detail(String web_dv_id, String web_shop_seq, String cook_seq, String page)
     {
    	 Log.d("GCMIntentService", "...response_show_recipe_detail...start...");
  		 Intent intent = new Intent(getApplicationContext(), etc_sub_activity.class);
   		 startActivity(intent);
   		 overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
   		Log.d("GCMIntentService", "...response_show_recipe_detail...end...");
     }

     public void finish_etc_activity(String kind) 
     {
    	 if (kind.equals("register")) {
    		 Toast.makeText(getApplicationContext(), "해당아이디로 재접속합니다.", 2000).show();
             finish();
    	 } else if (kind.equals("member_out")) {
    		 Toast.makeText(getApplicationContext(), "정상적으로 회원탈퇴 처리가 되었습니다. 그동안 로마켓 이용해 주셔서 감사합니다. 회원탈퇴 상태로 재접속 합니다.", 8000).show();
             finish();
    	 } else {
             finish();
    		 overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    	 }
     }
     
     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
     	boolean call_goback = true;
 	    	if (keyCode == KeyEvent.KEYCODE_BACK) { 
 	             finish();
 	            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
 	            return true; 
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
