package cn.aysst.www.flowerfish.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import cn.aysst.www.flowerfish.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.aysst.www.flowerfish.beans.Voice;
import cn.aysst.www.flowerfish.utils.Http;
import cn.aysst.www.flowerfish.utils.NetworkJudge;
import cn.aysst.www.flowerfish.utils.PermissionsUtils;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FragmentFlower extends Fragment  {
	final OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(20000, TimeUnit.MILLISECONDS)
			.readTimeout(20000, TimeUnit.MILLISECONDS)
			.build();

	public static FragmentFlower newInstance() {
		FragmentFlower fragment = new FragmentFlower();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_fragment_flower,null);

        return view;
	}

}
