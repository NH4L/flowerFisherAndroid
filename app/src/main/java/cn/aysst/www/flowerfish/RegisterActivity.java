package cn.aysst.www.flowerfish;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.aysst.www.flowerfish.utils.Http;
import cn.aysst.www.flowerfish.utils.NetworkJudge;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.util.concurrent.TimeUnit;


public class RegisterActivity extends Activity implements View.OnClickListener {
    private int ResultCode = 2;
    private final static int REGISTER_JUDGE = 2;
    public EditText edit_username, edit_setpassword, edit_resetpassword, edit_email;
    private TextView text_to_login, text_click_yes;
    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }


    protected void init() {
        edit_email = (EditText)findViewById(R.id.edit_email);
        edit_email.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        for (int i=start; i<end; i++) {
                            if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.toString(source.charAt(i)).equals("@") && !Character.toString(source.charAt(i)).equals(".")) {
                                Toast.makeText(RegisterActivity.this, "请输入正确的邮箱格式!", Toast.LENGTH_SHORT).show();
                                return "";
                            }
                        }
                        return null;
                    }
                }
        });

        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_username.setFilters(new InputFilter[]{                 //在编辑的时候就进行判断
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.toString(source.charAt(i)).equals("_")) {
                                Toast.makeText(RegisterActivity.this, "只能使用'_'、字母、数字、汉字注册！", Toast.LENGTH_SHORT).show();
                                return "";
                            }
                        }
                        return null;
                    }}

        });

        edit_username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_username.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_username.getWindowToken(), 0);
                }
                return false;
            }
        });

        edit_setpassword = (EditText) findViewById(R.id.edit_setpassword);
        edit_setpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {//当输入完密码点击软键盘上完成后进行判断
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String s = v.getText().toString().trim();
                if (s.length() >= 6 && s.length() <= 16) {
                    System.out.println("密码长度为:" + s.length());
                } else {
                    Toast.makeText(RegisterActivity.this, "密码长度为6-16位!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });


        edit_resetpassword = (EditText) findViewById(R.id.edit_resetpassword);

        text_click_yes = (TextView) findViewById(R.id.text_confirm_register);
        text_click_yes.setOnClickListener(this);

        text_to_login = (TextView)findViewById(R.id.text_log_in);
        text_to_login.setOnClickListener(this);
        //btn_cancel = (Button) findViewById(R.id.btn_cancle);
        //btn_cancel.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_confirm_register:
                if (edit_email.getText().toString().trim().length() == 0){
                    Toast.makeText(this, "请填写邮箱!", Toast.LENGTH_SHORT).show();
                } else {
                    if (edit_username.getText().toString().trim().length() == 0) {
                        Toast.makeText(this, "请填写注册用户名!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (edit_setpassword.getText().toString().trim().length() < 6){
                            Toast.makeText(this, "密码长度必须大于等于6!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!NetworkJudge.isNetworkConnected(this)) {
                                Toast.makeText(this, "请检查网络连接!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (edit_setpassword.getText().toString().trim().equals(edit_resetpassword.getText().toString())) {
                                    String username = edit_username.getText().toString();
                                    String email = edit_setpassword.getText().toString();
                                    String password = edit_email.getText().toString();
                                    checkRegister(username, email, password);
                                } else {
                                    Toast.makeText(this, "两次输入密码不同，请重新输入！", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }
                }

                break;
            case R.id.text_log_in:
                Intent login_intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login_intent);
                break;
            default:
                break;
        }
    }


    private void checkRegister(String username, String email, String password) {
        new RegisterAsyncTask().execute(username, email, password);
    }

    class RegisterAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return httpPostInsTaskReq(params[0], params[1], params[2]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(RegisterActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
                Intent register_intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(register_intent);
            } else if (result.equals("fail")) {
                Toast.makeText(RegisterActivity.this, "注册失败,用户名或邮箱已被注册,请重新注册!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String httpPostInsTaskReq(String username, String email, String password) {
        String result = "fail";
        FormBody body = new FormBody.Builder()
                .add("username", username)
                .add("email", email)
                .add("password", password)
                .build();

        final Request request = new Request.Builder()
                .url(Http.BASE_URL + "/RegisterServlet")
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String postData = response.body().string();
                return postData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}

