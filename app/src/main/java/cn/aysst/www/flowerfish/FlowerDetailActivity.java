package cn.aysst.www.flowerfish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class FlowerDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private Switch mSwitch;
    private LinearLayout mLinearLayout;
    private Button btnWater;
    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_detail);

        getSupportActionBar().setTitle("详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwitch = (Switch)findViewById(R.id.switch_on_flower_content);
        mLinearLayout = (LinearLayout)findViewById(R.id.setting_on_flower_content);
        btnWater = (Button) findViewById(R.id.btn_confirm_flower_water);


        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (! isChecked){
                    mLinearLayout.setVisibility(View.VISIBLE);
                }else {
                    mLinearLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnWater.setOnClickListener(this);
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
            case R.id.btn_confirm_flower_water:
                Toast.makeText(FlowerDetailActivity.this, "浇水成功", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
