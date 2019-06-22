package cn.aysst.www.flowerfish;

import android.app.Activity;
import android.content.Intent;
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


public class ForgotInfoActivity extends Activity implements View.OnClickListener {
    private EditText editEmail;
    private Button btnVerCode; //获取验证码按钮
    private EditText editVerCode;
    private TextView btnConfirmVerCode;
    private String verCode;
    final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotinfo);
        init();
    }

    private void init() {
        editEmail = (EditText)findViewById(R.id.edit_email_forgetPassword);
        btnVerCode = (Button)findViewById(R.id.btn_getEmailVerificationCode);
        editVerCode = (EditText)findViewById(R.id.edit_emailVerificationCode);
        btnConfirmVerCode = (TextView)findViewById(R.id.btn_confirm_VerCode);

        btnVerCode.setOnClickListener(this);
        btnConfirmVerCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getEmailVerificationCode:
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

            case R.id.btn_confirm_VerCode:
                String edit_code = editVerCode.getText().toString().trim();
                if (edit_code.equals(verCode)){
                    Toast.makeText(this, "验证码正确", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotInfoActivity.this, ChangePwdOutActivity.class);
                    intent.putExtra("email", editEmail.getText().toString().trim()) ;
                    startActivity(intent);
                }else {
                    Toast.makeText(this, "验证码输入有误", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void sendEmail(String email) {
        new SendEmailAsyncTask().execute(email);
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
                verCode = result;
            } else {
                Toast.makeText(ForgotInfoActivity.this, "服务器繁忙,请稍后重试!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String httpPostSendEmailReq(String email) {
        String result = "fail";
        FormBody body = new FormBody.Builder()
                .add("msg", "sendForgetPasswordEmailAction")
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
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
