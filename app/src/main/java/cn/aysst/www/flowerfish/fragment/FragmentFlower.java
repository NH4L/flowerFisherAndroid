package cn.aysst.www.flowerfish.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import cn.aysst.www.flowerfish.FlowerDetailActivity;
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
import org.json.JSONArray;
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
    private FloatingActionButton floatingActionButton;
    private CardView cardViewLuhui;
	public static FragmentFlower newInstance() {
		FragmentFlower fragment = new FragmentFlower();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_fragment_flower,null);

        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.float_btn_add_flower);
		cardViewLuhui = (CardView) view.findViewById(R.id.card_luhui);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFlower();
            }
        });
		cardViewLuhui.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addFlower();
			}
		});
        return view;
	}

    private void addFlower() {
	    Intent intent = new Intent(getActivity(), FlowerDetailActivity.class);
	    startActivity(intent);
    }


//	private void sendInstruction(String instruction) {
//		new SendInstructionAsyncTask().execute(instruction);
//	}

	class SendInstructionAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			return connSocketToStm(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getContext(), "连接成功", Toast.LENGTH_SHORT).show();
		}
	}

	private String connSocketToStm(String instruction) {
		String result = "fail";
		try {

			Socket socket = new Socket(Http.BASE_IP, Http.BASE_PORT);
			//获取输出流，向服务器端发送信息
			OutputStream os = socket.getOutputStream();//字节输出流
			PrintWriter out = new PrintWriter(os);//将输出流包装为打印流
			out.write(instruction);
			Log.d(Http.TAG, instruction);
			out.flush();
			socket.shutdownOutput();//关闭输出流

			InputStream is = socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			result = in.readLine();

//            while((info=in.readLine())!=null){
//                //System.out.println("我是客户端，Python服务器说："+info);
//                Log.d("MAIN","我是客户端，Python服务器说："+info);
//                Message msg = new Message();
//                Bundle data = new Bundle();
//                data.putString("value","我是客户端，Python服务器说："+info);
//                msg.setData(data);
//            }
			is.close();
			in.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}


}
