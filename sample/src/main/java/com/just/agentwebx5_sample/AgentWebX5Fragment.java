package com.just.agentwebx5_sample;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentwebX5.AgentWebX5;
import com.just.agentwebX5.ChromeClientCallbackManager;
import com.just.agentwebX5.DefaultWebClient;
import com.just.agentwebX5.DownLoadResultListener;
import com.just.agentwebX5.LogUtils;
import com.just.agentwebX5.MiddleWareWebChromeBase;
import com.just.agentwebX5.MiddleWareWebClientBase;
import com.just.agentwebX5.PermissionInterceptor;
import com.just.agentwebX5.WebDefaultSettingsManager;
import com.just.agentwebX5.WebSettings;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import static com.just.agentwebx5_sample.R.id.iv_back;


/**
 * Created by cenxiaozhong on 2017/5/15.
 */

public class AgentWebX5Fragment extends Fragment implements FragmentKeyDown {


    private ImageView mBackImageView;
    private View mLineView;
    private ImageView mFinishImageView;
    private TextView mTitleTextView;
    protected AgentWebX5 mAgentWebX5;
    public static final String URL_KEY = "url_key";
    public static final String TAG = AgentWebX5Fragment.class.getSimpleName();
    private MiddleWareWebChromeBase mMiddleWareWebChrome;
    private MiddleWareWebClientBase mMiddleWareWebClient;


    public static AgentWebX5Fragment getInstance(Bundle bundle) {

        AgentWebX5Fragment mAgentWebX5Fragment = new AgentWebX5Fragment();
        if (bundle != null)
            mAgentWebX5Fragment.setArguments(bundle);

        return mAgentWebX5Fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agentweb, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAgentWebX5 = AgentWebX5.with(this)//
                .setAgentWebParent((LinearLayout) view, new LinearLayout.LayoutParams(-1,-1))//
                .setIndicatorColorWithHeight(-1, 2)//
                .setWebSettings(getSettings())//
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setReceivedTitleCallback(mCallback)
                .setPermissionInterceptor(mPermissionInterceptor)
                .setNotifyIcon(R.mipmap.download)
                .useMiddleWareWebChrome(getMiddleWareWebChrome())
                .useMiddleWareWebClient(getMiddleWareWebClient())
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .interceptUnkownScheme()
                .openParallelDownload()
                .setSecurityType(AgentWebX5.SecurityType.strict)
                .addDownLoadResultListener(mDownLoadResultListener)
                .createAgentWeb()//
                .ready()//
                .go(getUrl());


        initView(view);

//        mAgentWebX5.getWebCreator().getGroup().setVisibility(View.GONE);
//        X5WebView mX5WebView=new X5WebView(getActivity());
//        LinearLayout mLinearLayout= (LinearLayout) view;
//
//        mLinearLayout.addView(mX5WebView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mX5WebView.loadUrl(getUrl());


    }


    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        //AgentWeb 在触发某些敏感的 Action 时候会回调该方法， 比如定位触发 。
        //例如 https//:www.baidu.com 该 Url 需要定位权限， 返回false ，如果版本大于等于23 ， agentWeb 会动态申请权限 ，true 该Url对应页面请求定位失败。
        //该方法是每次都会优先触发的 ， 开发者可以做一些敏感权限拦截 。
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            Log.i(TAG, "url:" + url + "  permission:" + permissions + " action:" + action);
            return false;
        }
    };
    protected DownLoadResultListener mDownLoadResultListener=new DownLoadResultListener() {
        @Override
        public void success(String path) {

            Log.i("Info","path:"+path);
            /*File mFile=new File(path);
            mFile.delete();*/
        }

        @Override
        public void error(String path, String resUrl, String cause, Throwable e) {

            Log.i("Info","path:"+path+"  url:"+resUrl+"  couse:"+cause+"  Throwable:"+e);
        }
    };

    public WebSettings getSettings() {
        return WebDefaultSettingsManager.getInstance();
    }

    public String getUrl() {
        String target = "";

        if (TextUtils.isEmpty(target = this.getArguments().getString(URL_KEY))) {
            target = "http://www.jd.com";
        }
        return target;
    }

    protected ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (mTitleTextView != null && !TextUtils.isEmpty(title))
                if (title.length() > 10)
                    title = title.substring(0, 10) + "...";
            mTitleTextView.setText(title);

        }
    };
    protected WebChromeClient mWebChromeClient=new WebChromeClient(){

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    };
    protected com.tencent.smtt.sdk.WebViewClient mWebViewClient = new com.tencent.smtt.sdk.WebViewClient() {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }




        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            LogUtils.i("Info", "mWebViewClient shouldOverrideUrlLoading:" + url);
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?vid=XODEzMjU1MTI4&refer=&tuid=&ua=Mozilla%2F5.0%20(Linux%3B%20Android%207.0%3B%20SM-G9300%20Build%2FNRD90M%3B%20wv)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Version%2F4.0%20Chrome%2F58.0.3029.83%20Mobile%20Safari%2F537.36&source=exclusive-pageload&cookieid=14971464739049EJXvh|Z6i1re#Intent;scheme=youku;package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            if (url.startsWith("intent://"))
                return true;
            else if(url.startsWith("youku"))
                return true;
//            else if(isAlipay(view,url))  //不需要，defaultWebClient内部会自动处理
//                return true;



            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            Log.i("Info", "url:" + url + " onPageStarted  target:" + getUrl());
            if (url.equals(getUrl())) {
                pageNavigator(View.GONE);
            } else {
                pageNavigator(View.VISIBLE);
            }

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("Info","onActivityResult -- >callback:"+requestCode+"   0x254:"+0x254);
//        Log.i("Info","onActivityResult result");
        mAgentWebX5.uploadFileResult(requestCode, resultCode, data);
    }

    protected void initView(View view) {
        mBackImageView = (ImageView) view.findViewById(iv_back);
        mLineView = view.findViewById(R.id.view_line);

        mFinishImageView = (ImageView) view.findViewById(R.id.iv_finish);
        mTitleTextView = (TextView) view.findViewById(R.id.toolbar_title);

        mBackImageView.setOnClickListener(mOnClickListener);
        mFinishImageView.setOnClickListener(mOnClickListener);

        pageNavigator(View.GONE);
    }



    private void pageNavigator(int tag) {

       // Log.i("Info", "TAG:" + tag);
        mBackImageView.setVisibility(tag);
        mLineView.setVisibility(tag);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            switch (v.getId()) {

                case iv_back:

                    if (!mAgentWebX5.back())
                        AgentWebX5Fragment.this.getActivity().finish();

                    break;
                case R.id.iv_finish:
                    AgentWebX5Fragment.this.getActivity().finish();
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        mAgentWebX5.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mAgentWebX5.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
        return mAgentWebX5.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        mAgentWebX5.getWebLifeCycle().onDestroy();
        super.onDestroyView();
        //  mAgentWebX5.destroy();
    }

    public MiddleWareWebChromeBase getMiddleWareWebChrome() {
        return mMiddleWareWebChrome;
    }

    public MiddleWareWebClientBase getMiddleWareWebClient() {
        return mMiddleWareWebClient;
    }
}
