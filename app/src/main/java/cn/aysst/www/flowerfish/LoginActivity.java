package cn.aysst.www.flowerfish;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.aysst.www.flowerfish.utils.Http;
import cn.aysst.www.flowerfish.utils.NetworkJudge;
import cn.aysst.www.flowerfish.utils.PermissionsUtils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText edit_account, edit_password;
    private TextView text_msg;
    private TextView text_login, text_login_turn_to_register_act;
    private ImageButton openpwd;
    private boolean flag = false;
    private TransitionView mAnimView;
    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
//            Toast.makeText(LoginActivity.this, "权限通过!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void forbitPermissons() {
//            finish();
            Toast.makeText(LoginActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void init() {
        //一个数据读写权限
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        PermissionsUtils.showSystemSetting = false;//是否支持显示系统设置权限设置窗口跳转
        //这里的this不是上下文，是Activity对象！
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);
        edit_account = (EditText) findViewById(R.id.edit_account);
        edit_account.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_account.clearFocus();
                }
                return false;
            }
        });

        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_password.clearFocus();
                }
                return false;
            }
        });

        text_msg = (TextView) findViewById(R.id.forget_password);              //忘记密码
        text_login = (TextView) findViewById(R.id.text_confirm_login);              //登录按钮
        text_login_turn_to_register_act = (TextView) findViewById(R.id.text_login_turn_to_sign_in);        //注册按钮
        openpwd = (ImageButton) findViewById(R.id.btn_openpwd);         //密码可视化按钮

        mAnimView = (TransitionView) findViewById(R.id.ani_view);       //动画
        mAnimView.setOnAnimationEndListener(new TransitionView.OnAnimationEndListener()
        {
            @Override
            public void onEnd()
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("account", edit_account.getText().toString());

                startActivity(intent);
                finish();
            }
        });

        text_msg.setOnClickListener(this);
        text_login.setOnClickListener(this);
        text_login_turn_to_register_act.setOnClickListener(this);
        openpwd.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }
//    private long exitTime = 0;
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
//            if((System.currentTimeMillis()-exitTime) > 2000){
//                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                finish();
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_confirm_login:
                if (edit_account.getText().toString().trim().equals("") | edit_password.getText().
                        toString().trim().equals("")) {
                    Toast.makeText(this, "请输入账号或者注册账号!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!NetworkJudge.isNetworkConnected(this)){
                        Toast.makeText(this, "请检查网络连接!", Toast.LENGTH_SHORT).show();
                    }else {
                        checkLogin(edit_account.getText().toString().trim(), edit_password.getText().toString().trim());
                    }
                }
                break;
            case R.id.text_login_turn_to_sign_in:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_openpwd:
                if (flag == true) {//不可见
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    openpwd.setBackgroundResource(R.drawable.invisible);
                } else {
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    openpwd.setBackgroundResource(R.drawable.visible);
                }
                break;
            case R.id.forget_password:
                Intent i = new Intent(LoginActivity.this, ForgotInfoActivity.class);
                startActivity(i);
                break;
        }
    }


    public void checkLogin(String account, String password) {
        new LogInAsyncTask().execute(account, password);
    }

    class LogInAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return httpPostLoginReq(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                mAnimView.startAnimation();
            } else if (result.equals("fail")) {
                Toast.makeText(LoginActivity.this, "账户不存在或密码错误，请重新输入!!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String httpPostLoginReq(String account, String password) {
        String result = "fail";
        FormBody body = new FormBody.Builder()
                .add("account", account)
                .add("password", password)
                .build();

        final Request request = new Request.Builder()
                .url(Http.BASE_URL + "/LoginServlet")
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
