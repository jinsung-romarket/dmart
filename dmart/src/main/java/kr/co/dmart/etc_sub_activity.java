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

public class etc_sub_activity extends Activity {
	
	public static Activity etc_sub_activity;
	public static WebView etc_sub_activity_webview;
	public static Context etc_sub_context;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
		 etc_sub_activity = etc_sub_activity.this;
		 etc_sub_context = this;
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_etc_sub);  // layout xml 과 자바파일을 연결
		 String show_kind = kr.co.dmart.MainActivity.static_now_etc_detail_kind;
		 TextView textView1 =(TextView)findViewById(R.id.etc_sub_left_title);
		 textView1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		 if (show_kind.equals("order_detail")) {
			 //주문상세
			 textView1.setBackgroundColor(Color.parseColor("#23262E"));
			 textView1.setText("주문상세");
		 } else if (show_kind.equals("recipe_detail")) {
			 textView1.setBackgroundColor(Color.parseColor("#FF5B7E"));
			 textView1.setText("레시피상세");
		 }
		 start_etc_sub_webview(show_kind);
	        
	        //start_goods_webview();
	        
	    }

     public void back_click(View v) {
         switch(v.getId()) {
         case R.id.etc_sub_left_title:
        	 //Toast.makeText(getApplicationContext(), "돌아가기.", 1000).show();
             finish();
             overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
             if (kr.co.dmart.MainActivity.static_now_etc_detail_kind.equals("recipe_detail")) {
            	 ((MainActivity)MainActivity.main_context).android_call_show_footer();
             }
             break;
         }
     }
     
     public void start_etc_sub_webview(String show_kind) {
    	 //Log.d("GCMIntentService", "show_kind..."+show_kind);
    	 //Log.d("GCMIntentService", "22response_show_goods_activity..."+kr.co.dmart.MainActivity.static_web_dv_id+":"+kr.co.dmart.MainActivity.static_web_shop_seq+":"+kr.co.dmart.MainActivity.static_web_goods_seq);
    	 String conn_url = "https://dnmart.co.kr/app2/app_22_base_etc_sub.html?show_kind="+show_kind+
    			 			"&dv_id="+kr.co.dmart.MainActivity.static_web_dv_id+
    			 			"&shop_seq="+kr.co.dmart.MainActivity.static_web_shop_seq+
    			 			"&show_kind="+show_kind+
    			 			"&member_order_seq="+kr.co.dmart.MainActivity.static_member_order_seq+
    			 			"&order_page="+kr.co.dmart.MainActivity.static_member_order_page+
    			 			"&order_flag="+kr.co.dmart.MainActivity.static_member_order_flag+
    			 			"&cook_seq="+kr.co.dmart.MainActivity.static_cook_seq+
    			 			"&cook_page="+kr.co.dmart.MainActivity.static_cook_page
    			 			;
 		
    	 setLayout();
 	        
         // 웹뷰에서 자바스크립트실행가능
    	 etc_sub_activity_webview.getSettings().setJavaScriptEnabled(true);
    	 etc_sub_activity_webview.getSettings().setBuiltInZoomControls(false);
    	 etc_sub_activity_webview.getSettings().setSupportZoom(false);
    	 etc_sub_activity_webview.addJavascriptInterface(new AndroidBridge(), "android");
    	 etc_sub_activity_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    	 etc_sub_activity_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically (true);//window.open허용
    	 etc_sub_activity_webview.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND); // 플러그인을 사용할 수 있도록 설정
         // 임의의 WebChromeClient 객체를 생성하여 WebView에 등록.
         // 여기의 Chrome은 브라우저 창 주의를 정리하는 모든 작업을 일컬음. 즉, 브라우저 처리을 할 클라이언트를 등록
 	
    	 //etc_activity_webview.loadUrl(conn_url);
    	 etc_sub_activity_webview.setWebChromeClient(new CustomWebChromeClient());
    	 etc_sub_activity_webview.setWebViewClient(new WebViewClient() {
         });
    	 etc_sub_activity_webview.loadUrl(conn_url);
 	}

     private void setLayout(){
    	 etc_sub_activity_webview = (WebView) findViewById(R.id.etc_sub_webview);
     }
     
     public void finish_etc_sub_activity() 
     {
         finish();
         overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
     }

     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
     	boolean call_goback = true;
 	    	if (keyCode == KeyEvent.KEYCODE_BACK) { 
 	    		finish();
 	            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
 	           if (kr.co.dmart.MainActivity.static_now_etc_detail_kind.equals("recipe_detail")) {
 	        	   ((MainActivity)MainActivity.main_context).android_call_show_footer();
 	           }
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
