package com.just.agentwebx5_sample;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;

import org.json.JSONObject;

/**
 * Created by cenxiaozhong on 2017/5/26.
 */

public class JsAgentWebFragment extends AgentWebX5Fragment {

    public static final JsAgentWebFragment getInstance(Bundle bundle) {

        JsAgentWebFragment mJsAgentWebFragment = new JsAgentWebFragment();
        if (bundle != null)
            mJsAgentWebFragment.setArguments(bundle);

        return mJsAgentWebFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        LinearLayout mLinearLayout= (LinearLayout) view;
        LayoutInflater.from(getContext()).inflate(R.layout.fragment_js,mLinearLayout,true);
        super.onViewCreated(view, savedInstanceState);


        Log.i("Info","add android:"+ mAgentWebX5);
        if(mAgentWebX5 !=null){
            mAgentWebX5.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface(mAgentWebX5,this.getActivity()));

        }
//        String p="\"hello\""+","+"\" js\"";
//        mAgentWebX5.getWebCreator().get().loadUrl("javascript:callByAndroid("+"\"hello\""+","+"\" js\""+")");

        view.findViewById(R.id.one).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.two).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.three).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.four).setOnClickListener(mOnClickListener);


    }

    private View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {


            switch (v.getId()){

                case R.id.one:
                    mAgentWebX5.getJsEntraceAccess().quickCallJs("callByAndroid");
                    break;

                case R.id.two:
                    mAgentWebX5.getJsEntraceAccess().quickCallJs("callByAndroidParam","Hello ! Agentweb");
                    break;

                case R.id.three:
                    mAgentWebX5.getJsEntraceAccess().quickCallJs("callByAndroidMoreParams", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.i("Info","value:"+value);
                        }
                    },getJson(),"say:", " Hello! Agentweb");
                    break;
                case R.id.four:
                    mAgentWebX5.getJsEntraceAccess().quickCallJs("callByAndroidInteraction","你好Js");
                    break;
            }

        }
    };

    private String getJson(){

        String result="";
        try {

            JSONObject mJSONObject=new JSONObject();
            mJSONObject.put("id",1);
            mJSONObject.put("name","Agentweb");
            mJSONObject.put("age",18);
            result= mJSONObject.toString();
        }catch (Exception e){

        }

        return result;
    }


}
