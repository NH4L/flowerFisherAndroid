package cn.aysst.www.flowerfish;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.aysst.www.flowerfish.utils.Http;
import cn.aysst.www.flowerfish.utils.NetworkJudge;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ChangeEmailActivity extends Activity implements View.OnClickListener{
    private EditText editEmail;
    private Button btnVerCode; //获取验证码按钮
    private EditText editVerCode;
    private TextView btnConfirmVerCode;
    private String verCode;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email_on_homepage);
        init();
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        finish();
    }
    private void init() {
        editEmail = (EditText)findViewById(R.id.edit_new_email_on_homepage);
        btnVerCode = (Button)findViewById(R.id.btn_getEmailVerificationCode_on_homepage);
        editVerCode = (EditText)findViewById(R.id.edit_emailVerificationCode_on_homepage);
        btnConfirmVerCode = (TextView)findViewById(R.id.btn_confirm_VerCode_on_homepage);

        btnVerCode.setOnClickListener(this);
        btnConfirmVerCode.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getEmailVerificationCode_on_homepage:
                if (editEmail.getText().toString().trim().equals("")){
                    Toast.makeText(this, "请输入验证邮箱!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!NetworkJudge.isNetworkConnected(this)) {
                        Toast.makeText(this, "请连接网络后重试!", Toast.LENGTH_SHORT).show();
                    } else {
                        String email = editEmail.getText().toString().trim();
                        sendEmail(email);
                    }
                }
                break;

            case R.id.btn_confirm_VerCode_on_homepage:
                String edit_code = editVerCode.getText().toString().trim();
                if (edit_code.equals(verCode)){
                    SharedPreferences preferences = getSharedPreferences("userInfo", ChangeEmailActivity.MODE_PRIVATE);
                    String name = preferences.getString("name", "");
                    String email = editEmail.getText().toString().trim();
                    changeEmailDB(email, name);
                }else {
                    Toast.makeText(this, "验证码输入有误", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void sendEmail(String email) {
        new SendEmailAsyncTask().execute(email);
    }

    /**
     * 更改邮箱，启动异步任务
     * @param email 当前用户邮箱
     * @param name 待修改密码
     */
    private void changeEmailDB(String email, String name) {
        new changeEmailDBAsyncTask().execute(email, name);
    }

    /**
     * 更改密码的异步任务
     */
    class changeEmailDBAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return httpPostChangePwdReq(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(ChangeEmailActivity.this, "修改邮箱成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangeEmailActivity.this, HomepageActivity.class);
                //intent.putExtra("email", editEmail.getText().toString().trim()) ;
                startActivity(intent);
            }
        }
    }
    class SendEmailAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return httpPostSendEmailReq(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("fail")) {
                Toast.makeText(ChangeEmailActivity.this, "验证码发送成功!", Toast.LENGTH_SHORT).show();
                verCode = result;
            } else {
                Toast.makeText(ChangeEmailActivity.this, "输入的邮箱相同!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String httpPostSendEmailReq(String email) {
        SharedPreferences preferences = getSharedPreferences("userInfo", ChangeEmailActivity.MODE_PRIVATE);
        String name = preferences.getString("name", "");
        String result = "fail";
        FormBody body = new FormBody.Builder()
                .add("msg", "changeEmailAction")
                .add("name", name)
                .add("email", email)
                .build();

        final Request request = new Request.Builder()
                .url(Http.BASE_URL + "/ExecuteUserInfoServlet")
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String postResult = response.body().string();
                return postResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *  访问服务器修改密码
     * @param email 邮箱
     * @param name 密码
     * @return 返回success或fail
     */
    private String httpPostChangePwdReq(String email, String name) {
        String result = "fail";
        FormBody body = new FormBody.Builder()
                .add("msg", "changeEmailDBAction")
                .add("email", email)
                .add("name", name)
                .build();

        final Request request = new Request.Builder()
                .url(Http.BASE_URL + "/ExecuteUserInfoServlet")
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
