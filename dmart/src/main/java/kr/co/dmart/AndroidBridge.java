package kr.co.dmart;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;

final class AndroidBridge {
	private final Handler handler = new Handler();
	
	@JavascriptInterface
    public void callAndroid(final String str) {
    	handler.post(new Runnable() {
            public void run() {
                //Log.d("HybridApp", "javascript_get("+str+")");
//                mTextView.setText(arg);
                //MainActivity.mWebView.loadUrl("javascript:callJS('Hello from Android')");
                //((MainActivity) appContext).set_webview_url(str);
                ((MainActivity)MainActivity.main_context).set_webview_url(str);
                //Log.d("HybridApp", "main activity send_webview_url call...");
            }


        });
 
    }
	
	@JavascriptInterface
    public void request_ver(final String call_from) {
    	//Log.d("GCMIntentService", "call request_ver function...");
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
//            	Log.d("GCMIntentService", "222 call request_ver function...");
                ((MainActivity)MainActivity.main_context).response_ver(call_from);
            }
        });
    	
    }
	
	@JavascriptInterface
    public void check_phone_num() {
    	Log.d("GCMIntentService", "call check_phone_num function...");
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
//            	Log.d("GCMIntentService", "222 call request_ver function...");
                ((MainActivity)MainActivity.main_context).check_phone_allow();
            }
        });

    }
	
	@JavascriptInterface
    public void request_dv_id(final String call_from) {
    	//MainActivity.this.response_gcmid(); 
    	handler.post(new Runnable() {
            public void run() {
            	//Log.d("GCMIntentService", "call request_dv_id function11...");
//                mTextView.setText(arg);
                //MainActivity.mWebView.loadUrl("javascript:callJS('Hello from Android')");
                //((MainActivity) appContext).set_webview_url(str);
                ((MainActivity)MainActivity.main_context).response_dv_id(call_from);
            }
        });
    	
    }
	
	@JavascriptInterface
    public void request_and_id(final String call_from) {
    	//MainActivity.this.response_gcmid(); 
    	handler.post(new Runnable() {
            public void run() {
            	//Log.d("GCMIntentService", "call request_dv_id function11...");
//                mTextView.setText(arg);
                //MainActivity.mWebView.loadUrl("javascript:callJS('Hello from Android')");
                //((MainActivity) appContext).set_webview_url(str);
                ((MainActivity)MainActivity.main_context).response_and_id(call_from);
            }
        });
    	
    }

	@JavascriptInterface
    public void exit_prog() {
    	//Log.d("GCMIntentService", "call request_ver function...");
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
//            	Log.d("GCMIntentService", "222 call request_dv_id function...");
                ((MainActivity)MainActivity.main_context).exit_app();
            }
        });
    	
    }
	
	@JavascriptInterface
    public void send_new_msg_cnt(final String cnt) {
    	//Log.d("GCMIntentService", "call send_new_msg_cnt function...");
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
//            	Log.d("GCMIntentService", "222 call request_dv_id function...");
                ((MainActivity)MainActivity.main_context).get_new_msg_cnt(cnt);
            }
        });
    	
    }

	@JavascriptInterface
    public void send_new_msg_cnt_to_bottom(final String cnt) {
    	//Log.d("GCMIntentService", "call send_new_msg_cnt function...");
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
//            	Log.d("GCMIntentService", "222 call request_dv_id function...");
                ((MainActivity)MainActivity.main_context).get_new_msg_cnt_to_bottom(cnt);
            }
        });
    	
    }

	@JavascriptInterface
    public void url_window_open(final String str_url) {
    	Log.d("GCMIntentService", "call url_window_open function...");
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
            	Log.d("GCMIntentService", "url="+str_url);
                ((MainActivity)MainActivity.main_context).and_url_window_open(str_url);
            }
        });
    	
    }

	@JavascriptInterface
    public void req_now_location(final String call_from) {
    	Log.d("GCMIntentService", "req_now_location function...");
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
                ((MainActivity)MainActivity.main_context).get_now_location(call_from);
            }
        });
    	
    }

	@JavascriptInterface
    public void slide_go_back() {
    	Log.d("GCMIntentService", "slide_go_back...");
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
                ((MainActivity)MainActivity.main_context).app_slide_go_back();
            }
        });
    }

	@JavascriptInterface
    public void request_run_scan(final String call_from) {
    	Log.d("GCMIntentService", "request_run_scan function...");
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
                ((MainActivity)MainActivity.main_context).run_scan();
            }
        });
    }
	
	@JavascriptInterface
    public void request_show_cart_activity(final String web_dv_id, final String web_shop_seq) {
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
                ((MainActivity)MainActivity.main_context).response_show_cart_activity(web_dv_id,web_shop_seq);
            }
        });
    }

	@JavascriptInterface
    public void request_show_goods_activity(final String web_dv_id, final String web_shop_seq, final String web_goods_seq) {
		//Log.d("GCMIntentService", "request_show_goods_activity..."+web_dv_id+":"+web_shop_seq+":"+web_goods_seq);
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
                ((MainActivity)MainActivity.main_context).response_show_goods_activity(web_dv_id, web_shop_seq, web_goods_seq);
            }
        });
    }

	@JavascriptInterface
    public void request_show_mypage_activity(final String web_dv_id, final String web_shop_seq) {
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
                ((MainActivity)MainActivity.main_context).response_show_mypage_activity(web_dv_id, web_shop_seq);
            }
        });
    }

	@JavascriptInterface
    public void request_update_cart_cnt(final String shop_seq) {
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
                ((MainActivity)MainActivity.main_context).response_update_cart_cnt(shop_seq);
            }
        });
    }

	@JavascriptInterface
    public void request_login_comp() {
    	handler.post(new Runnable() {
            public void run() {
                ((MainActivity)MainActivity.main_context).response_login_comp();
            }
        });
    }

	@JavascriptInterface
    public void request_show_footer_info() {
    	handler.post(new Runnable() {
            public void run() {
                ((MainActivity)MainActivity.main_context).android_call_show_footer();
            }
        });
    }

	@JavascriptInterface
    public void request_reload_app_index_from_mypage(final String web_dv_id, final String web_shop_seq) {
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
                ((MainActivity)MainActivity.main_context).response_reload_app_index_from_mypage(web_dv_id, web_shop_seq);
                ((Mypage_activity)Mypage_activity.mypage_context).finish_mypage_activity();
            }
        });
    }

	@JavascriptInterface
    public void request_reload_app_index_from_register(final String web_dv_id, final String web_shop_seq) {
    	//MainActivity.this.response_gcmid();
		//Log.d("GCMIntentService", "request_reload_app_index_from_register..."+web_dv_id+":"+web_shop_seq);
    	handler.post(new Runnable() {
            public void run() {
                ((etc_activity)etc_activity.etc_context).finish_etc_activity("register");
                ((Mypage_activity)Mypage_activity.mypage_context).finish_mypage_activity();
                ((MainActivity)MainActivity.main_context).response_reload_app_index_from_mypage(web_dv_id, web_shop_seq);
            }
        });
    }

	@JavascriptInterface
    public void request_reload_app_index_from_member_out(final String web_dv_id, final String web_shop_seq) {
    	//MainActivity.this.response_gcmid();
		//Log.d("GCMIntentService", "request_reload_app_index_from_register..."+web_dv_id+":"+web_shop_seq);
    	handler.post(new Runnable() {
            public void run() {
                ((etc_activity)etc_activity.etc_context).finish_etc_activity("member_out");
                ((Mypage_activity)Mypage_activity.mypage_context).finish_mypage_activity();
                ((MainActivity)MainActivity.main_context).response_reload_app_index_from_mypage(web_dv_id, web_shop_seq);
            }
        });
    }

	@JavascriptInterface
    public void request_show_mypage_sub(final String web_dv_id, final String web_shop_seq, final String show_kind) {
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
            	kr.co.dmart.MainActivity.static_now_etc_kind = show_kind;
            	((MainActivity)MainActivity.main_context).direct_response_show_mypage_sub(web_dv_id, web_shop_seq, show_kind);
            }
        });
    }

	@JavascriptInterface
    public void request_show_order_detail(final String web_dv_id, final String web_shop_seq, final String member_order_seq, final String page, final String order_flag) {
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
            	kr.co.dmart.MainActivity.static_member_order_seq = member_order_seq;
            	kr.co.dmart.MainActivity.static_member_order_page = page;
            	kr.co.dmart.MainActivity.static_member_order_flag = order_flag;
            	kr.co.dmart.MainActivity.static_now_etc_detail_kind = "order_detail";
                ((etc_activity)etc_activity.etc_context).response_show_order_detail(web_dv_id, web_shop_seq, member_order_seq, page, order_flag);
            }
        });
    }

	@JavascriptInterface
    public void request_show_recipe_detail(final String web_dv_id, final String web_shop_seq, final String cook_seq, final String page) {
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
                //((etc_sub_activity)etc_sub_activity.etc_sub_context).response_show_recipe_detail(web_dv_id, web_shop_seq, cook_seq, page);
            	((MainActivity)MainActivity.main_context).response_show_recipe_detail(web_dv_id, web_shop_seq, cook_seq, page);
            }
        });
    }

	@JavascriptInterface
    public void request_etc_activity_finish() {
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
                ((etc_activity)etc_activity.etc_context).finish_etc_activity("");
            }
        });
    }

	@JavascriptInterface
    public void request_etc_sub_activity_finish() {
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
                ((etc_sub_activity)etc_sub_activity.etc_sub_context).finish_etc_sub_activity();
            }
        });
    }

	@JavascriptInterface
    public void request_send_new_msg_cnt(final String web_dv_id, final String web_shop_seq) {
    	//MainActivity.this.response_gcmid();
		//Log.d("GCMIntentService", "request_send_new_msg_cnt..."+web_dv_id+":"+web_shop_seq);
    	handler.post(new Runnable() {
            public void run() {
                ((Mypage_activity)Mypage_activity.mypage_context).response_send_new_msg_cnt(web_dv_id, web_shop_seq);
            }
        });
    }

	@JavascriptInterface
    public void request_show_etc_activity(final String web_dv_id, final String web_shop_seq, final String show_kind) {
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
            	Log.d("GCMIntentService", "request_show_etc_activity..."+web_dv_id+":"+web_shop_seq+":"+show_kind);
            	((MainActivity)MainActivity.main_context).response_show_sale_msg(web_dv_id, web_shop_seq, show_kind);
            }
        });
    }

	@JavascriptInterface
    public void request_show_sale_img(final String img_src) {
    	//MainActivity.this.response_gcmid();
    	handler.post(new Runnable() {
            public void run() {
            	kr.co.dmart.MainActivity.static_now_etc_kind = "sale_img";
            	((MainActivity)MainActivity.main_context).response_show_sale_image(img_src);
            }
        });
    }

	@JavascriptInterface
    public void zoom_img(final String zoom_img_src) {
    	//MainActivity.this.response_gcmid();
		//Log.d("GCMIntentService", "zoom_img..."+zoom_img_src);
    	handler.post(new Runnable() {
            public void run() {
            	//((ZoomImg_activity)ZoomImg_activity.zoom_img_context).show_zoom_img(zoom_img_src);
            	((MainActivity)MainActivity.main_context).response_show_zoom_img(zoom_img_src);
            }
        });
    }
	
	@JavascriptInterface
    public void request_phone_call(final String phone_num) {
    	//MainActivity.this.response_gcmid();
		//Log.d("GCMIntentService", "zoom_img..."+zoom_img_src);
    	handler.post(new Runnable() {
            public void run() {
            	//((ZoomImg_activity)ZoomImg_activity.zoom_img_context).show_zoom_img(zoom_img_src);
            	((MainActivity)MainActivity.main_context).response_phone_call(phone_num);
            }
        });
    }

	@JavascriptInterface
    public void request_vibrate(final String vib_time) {
    	//MainActivity.this.response_gcmid();
		//Log.d("GCMIntentService", "zoom_img..."+zoom_img_src);
    	handler.post(new Runnable() {
            public void run() {
            	//((ZoomImg_activity)ZoomImg_activity.zoom_img_context).show_zoom_img(zoom_img_src);
            	((MainActivity)MainActivity.main_context).request_vibrate(vib_time);
            }
        });
    }

	@JavascriptInterface
    public void request_send_share(final String web_dv_id, final String web_shop_seq, final String share_kind, final String share_msg) {
    	//MainActivity.this.response_gcmid();
		//Log.d("GCMIntentService", "zoom_img..."+zoom_img_src);
    	handler.post(new Runnable() {
            public void run() {
            	//((ZoomImg_activity)ZoomImg_activity.zoom_img_context).show_zoom_img(zoom_img_src);
            	((MainActivity)MainActivity.main_context).request_send_share(web_dv_id,web_shop_seq,share_kind,share_msg);
            }
        });
    }

    @JavascriptInterface
    public void show_msg_noti() {
        //MainActivity.this.response_gcmid();
        Log.d("GCMIntentService", "...show_msg_noti...");
        handler.post(new Runnable() {
            public void run() {
                //((ZoomImg_activity)ZoomImg_activity.zoom_img_context).show_zoom_img(zoom_img_src);
                ((MainActivity)MainActivity.main_context).show_msg_noti();
            }
        });
    }

	@JavascriptInterface
    public void request_move_back() {
    	//MainActivity.this.response_gcmid();
		//Log.d("GCMIntentService", "zoom_img..."+zoom_img_src);
    	handler.post(new Runnable() {
            public void run() {
            	//((ZoomImg_activity)ZoomImg_activity.zoom_img_context).show_zoom_img(zoom_img_src);
            	((MainActivity)MainActivity.main_context).resonse_move_back();
            }
        });
    }

}
