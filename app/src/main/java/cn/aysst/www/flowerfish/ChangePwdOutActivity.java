package cn.aysst.www.flowerfish;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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


public class ChangePwdOutActivity extends Activity implements View.OnClickListener {
    private EditText editPwd;
    private EditText editConfirmPwd;
    private TextView btnConfirmPwd;
    private String email;
    final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd_out);
        init();
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        finish();
    }
    private void init() {
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        editPwd = (EditText)findViewById(R.id.edit_pwd_on_forget_account);
        editConfirmPwd = (EditText)findViewById(R.id.edit_confirm_pwd_on_forget_account);
        btnConfirmPwd = (TextView) findViewById(R.id.btn_confirm_pwd_out);

        btnConfirmPwd.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_pwd_out:
                if (editPwd.getText().toString().trim().equals("")){
                    Toast.makeText(this, "请输入密码!", Toast.LENGTH_SHORT).show();
                } else {
                    if (editConfirmPwd.getText().toString().trim().equals("")) {
                        Toast.makeText(this, "请输入重复密码!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!editPwd.getText().toString().trim().equals(editConfirmPwd.getText().toString().trim())) {
                            Toast.makeText(this, "两次输入的密码不一致!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!NetworkJudge.isNetworkConnected(this)) {
                                Toast.makeText(this, "请连接网络后重试!", Toast.LENGTH_SHORT).show();
                            } else {
                                String password = editPwd.getText().toString().trim();
                                changePwd(email, password);
                            }
                        }
                    }
                }
                break;

        }
    }

    /**
     * 更改密码，启动异步任务
     * @param email 当前用户邮箱
     * @param password 待修改密码
     */
    private void changePwd(String email, String password) {
        new changePwdAsyncTask().execute(email, password);
    }

    /**
     * 更改密码的异步任务
     */
    class changePwdAsyncTask extends AsyncTask<String, Integer, String> {
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
                Toast.makeText(ChangePwdOutActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePwdOutActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(ChangePwdOutActivity.this, "输入密码与原密码重复!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *  访问服务器修改密码
     * @param email 邮箱
     * @param password 密码
     * @return 返回success或fail
     */
    private String httpPostChangePwdReq(String email, String password) {
        String result = "fail";
        FormBody body = new FormBody.Builder()
                .add("msg", "changePwdAction")
                .add("email", email)
                .add("password", password)
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
