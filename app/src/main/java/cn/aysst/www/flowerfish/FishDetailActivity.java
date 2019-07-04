package cn.aysst.www.flowerfish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class FishDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch mSwith01;
    private Switch mSwith02;
    private LinearLayout mLinearLayout01;
    private LinearLayout mLinearLayout02;
    private Button btnWaterExchange;
    private Button btnFeedFish;
    private

    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_detail);

        getSupportActionBar().setTitle("详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnWaterExchange = (Button) findViewById(R.id.btn_water_exchange);
        btnFeedFish = (Button) findViewById(R.id.btn_feed_fish);

        mSwith01 = (Switch)findViewById(R.id.switch_01_on_fishing_content);
        mSwith02 = (Switch)findViewById(R.id.switch_02_on_fishing_content);

        mLinearLayout01 = (LinearLayout)findViewById(R.id.setting_01_on_fishing_content);
        mLinearLayout02 = (LinearLayout)findViewById(R.id.setting_02_on_fishing_content);

        mSwith01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    mLinearLayout01.setVisibility(View.VISIBLE);
                }else {
                    mLinearLayout01.setVisibility(View.INVISIBLE);
                }
            }
        });
        mSwith02.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (! isChecked){
                    mLinearLayout02.setVisibility(View.VISIBLE);
                }else {
                    mLinearLayout02.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnWaterExchange.setOnClickListener(this);
        btnFeedFish.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_water_exchange:
                Toast.makeText(FishDetailActivity.this, "换水成功", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_feed_fish:
                Toast.makeText(FishDetailActivity.this, "喂食成功", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
