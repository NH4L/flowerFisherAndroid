package cn.aysst.www.flowerfish;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.aysst.www.flowerfish.utils.DataGenerator;
import cn.aysst.www.flowerfish.utils.Http;
import cn.aysst.www.flowerfish.utils.MyBitmapUtils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity{
    private TabLayout mTabLayout;
    private Fragment[] mFragmensts;
    private String name, email, phone, signature, sex, portraitAddr;
    private String user_json = "";
    private FragmentTransaction transaction;
    private FragmentManager manager;
    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build();

    private static final int REQUEST_WE_STORAGE_PER = 100;
    private static final int REQUEST_RE_STORAGE_PER = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("智家");
        initView();

    }


    private void initView() {

        mFragmensts = DataGenerator.getFragments();
        manager = getSupportFragmentManager();

        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());
                for (int i=0; i<mTabLayout.getTabCount(); i++){
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    ImageView icon = (ImageView) view.findViewById(R.id.tab_content_image);
                    TextView text = (TextView) view.findViewById(R.id.tab_content_text);
                    if(i == tab.getPosition()){ // 选中状态
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    }else{// 未选中状态
                        icon.setImageResource(DataGenerator.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for(int i=0; i<3; i++){
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(DataGenerator.getTabView(this, i)));
        }



//        Intent intent = getIntent();
//        String account = intent.getStringExtra("account");
//        account = "lcy";
//        user_json = httpPostUserinfoReq(account);
        user_json = "{\"name\":\"lcy\",\"email\":\"lcy@qq.com\",\"money\":5129.5,\"sex\":\"女\",\"phone\":\"15995028879\",\"signature\":\"今天很开心\",\"portraitAddr\":\"http://www.aysst.cn/files/images/lcy/2/6/BA60VROIYP62P17UWLQAUPZKMU5KFWC1.jpg\"}";
        Log.d("user", user_json);
        try {
            JSONObject jsonObject = new JSONObject(user_json);
            name = (String) jsonObject.getString("name");
            email = (String) jsonObject.getString("email");
            sex = (String)jsonObject.getString("sex");
            phone = (String)jsonObject.getString("phone");
            signature = (String)jsonObject.getString("signature");
            portraitAddr = (String)jsonObject.getString("portraitAddr");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);//存储用户名
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("sex", sex);
        editor.putString("phone", phone);
        editor.putString("signature", signature);
        editor.putString("portraitAddr", portraitAddr);

        editor.commit();//提交修改

//        if (portraitAddr.equals("无")) {
//            portraitImage.setImageResource(R.drawable.people_fill);
//        } else {
//            new MyBitmapUtils().disPlay(portraitImage, portraitAddr);
//        }

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    private void onTabItemSelected(int position){
        transaction = manager.beginTransaction();
        hideAllFragemnt(manager);
        switch (position){
            case 0:
                showFragment("fragment_flower", mFragmensts[0],position);
                break;
            case 1:
                showFragment("fragment_fish", mFragmensts[1],position);
                break;
            case 2:
                showFragment("fragment_person", mFragmensts[2],position);
                break;
        }
    }

    private void hideAllFragemnt(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();
        for (Fragment fragment:fragments){
            transaction.hide(fragment);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_WE_STORAGE_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this,"权限已开放",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_RE_STORAGE_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this,"权限已开放",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showFragment(String tag, Fragment fragment, int position){
        if (manager.findFragmentByTag(tag) != null){
            transaction.show(fragment);
        }else{
            transaction.add(R.id.home_container, fragment,tag);
            transaction.show(fragment);
        }
        transaction.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
