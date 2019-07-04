package cn.aysst.www.flowerfish.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import cn.aysst.www.flowerfish.FishDetailActivity;
import cn.aysst.www.flowerfish.FlowerDetailActivity;
import cn.aysst.www.flowerfish.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.aysst.www.flowerfish.utils.Http;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

public class FragmentFish extends Fragment {

	private FloatingActionButton floatingActionButton;

	final OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(5000, TimeUnit.MILLISECONDS)
			.readTimeout(5000, TimeUnit.MILLISECONDS)
			.build();
	private CardView cardViewJinyu;
    public static FragmentFish newInstance() {
		FragmentFish fragment = new FragmentFish();                ;
        return fragment;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_fragment_fish, null);

		floatingActionButton = (FloatingActionButton)view.findViewById(R.id.float_btn_add_fish);
		cardViewJinyu = (CardView) view.findViewById(R.id.card_jinyu);


		floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addFish();
			}
		});

		cardViewJinyu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addFish();
			}
		});

		return view;
	}

	private void addFish() {
		Intent intent = new Intent(getActivity(), FishDetailActivity.class);
		startActivity(intent);
	}

}
