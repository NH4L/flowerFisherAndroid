package cn.aysst.www.flowerfish.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.*;
import cn.aysst.www.flowerfish.*;
import cn.aysst.www.flowerfish.DialogFragment.EditDialogFragment;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.aysst.www.flowerfish.utils.DownloadUtil;
import cn.aysst.www.flowerfish.utils.Http;
import cn.aysst.www.flowerfish.utils.MyBitmapUtils;
import cn.aysst.www.flowerfish.utils.UploadFiles;
import cn.aysst.www.flowerfish.wheelview.BottomDialog;
import cn.aysst.www.flowerfish.wheelview.WheelView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;
import static cn.aysst.www.flowerfish.utils.Http.BASE_URL;

public class FragmentPerson extends Fragment implements View.OnClickListener, EditDialogFragment.NoticeDialogListener{
    final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20000, TimeUnit.MILLISECONDS)
            .readTimeout(20000, TimeUnit.MILLISECONDS)
            .build();
    public static FragmentPerson newInstance() {
		FragmentPerson fragment = new FragmentPerson();                ;
		return fragment;
	}

    private CircleImageView circleImageView;
    private TextView userName, userSignature;
    private TextView myMailText;
    private TextView myPhoneText;
    private TextView mySexText;
    private TextView showMailView;
    private TextView showPhoneView;
    private TextView showSexView;
    private BottomDialog bottomDialog;
    private Bitmap portraitBit = null;

    private static final int CHANGE_PORTRAIT = 1;
    private static final int REQUEST_RE_STORAGE_PER = 100;
    private static final int REQUEST_WE_STORAGE_PER = 102;
    private static final int REQUEST_CAMERA_PER = 101;

    private String name = "";
    private String email = "";
    private String sex = "";
    private String phone = "";
    private String signature = "";
    private String portraitAddr = "";
    private boolean result;
    private String exeSexResult = "";
    private String exePhoneSesult = "";
    private String exeSignatureSesult = "";
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_person, null);
        init(view);
        return view;
    }

    protected void init(View view){

        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", MainActivity.MODE_PRIVATE);
        name = preferences.getString("name", "");
        email = preferences.getString("email","");
        sex = preferences.getString("sex","");
        phone = preferences.getString("phone","");
        signature = preferences.getString("signature","");
        portraitAddr = preferences.getString("portraitAddr","");

        myMailText = (TextView)view.findViewById(R.id.my_mail_onhomepage);
        myPhoneText = (TextView)view.findViewById(R.id.my_phone_onhomepage);
        mySexText = (TextView)view.findViewById(R.id.my_sex_onhomepage);
        showMailView = (TextView)view.findViewById(R.id.show_mail_onhomepage);
        showPhoneView = (TextView)view.findViewById(R.id.show_phone_onhomepage);
        showSexView = (TextView)view.findViewById(R.id.show_sex_onhomepage);
        circleImageView = (CircleImageView)view.findViewById(R.id.portrait_onhomepage);
        userName = (TextView) view.findViewById(R.id.user_name_onhomepage);
        userSignature = (TextView)view.findViewById(R.id.user_signature_onhomepage);


        if (portraitAddr.equals("无")) {
            circleImageView.setImageResource(R.drawable.people_fill);
        } else {
            new MyBitmapUtils().disPlay(circleImageView, portraitAddr);
        }

        userName.setText(name);
        userSignature.setText(signature);
        showMailView.setText(email);
        showPhoneView.setText(phone);
        showSexView.setText(sex);

        myMailText.setOnClickListener(this);
        myPhoneText.setOnClickListener(this);
        mySexText.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        view.findViewById(R.id.my_mail_layout).setOnClickListener(this);
        view.findViewById(R.id.my_phone_layout).setOnClickListener(this);
        view.findViewById(R.id.my_sex_layout).setOnClickListener(this);
        view.findViewById(R.id.my_signature_layout).setOnClickListener(this);
        ((Button)view.findViewById(R.id.btn_logout)).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_mail_layout:
                Intent intent = new Intent(getActivity(), ChangeEmailActivity.class);
                startActivity(intent);
                break;
            case R.id.my_phone_layout:
                showEditPhoneDialog();
                break;
            case R.id.my_signature_layout:
                showEditSignatureDialog();
                break;
            case R.id.my_sex_layout:
                View outerView1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_date_time, null);
                final WheelView wv1 = (WheelView) outerView1.findViewById(R.id.wv1);
                List<String> list = new ArrayList<>();
                list.add("男");
                list.add("女");
                list.add("无");
                switch (sex){
                    case "男":
                        wv1.setItems(list,0);
                        break;
                    case "女":
                        wv1.setItems(list,1);
                        break;
                    case "无":
                        wv1.setItems(list,2);
                        break;
                    default:
                        wv1.setItems(list,2);
                        break;
                }
                TextView tv_ok = (TextView) outerView1.findViewById(R.id.tv_ok);
                final TextView tv_cancel = (TextView) outerView1.findViewById(R.id.tv_cancel);
                //点击确定
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        bottomDialog.dismiss();
                        String mSelect = wv1.getSelectedItem();
                        String result = httpPostSexReq(mSelect);
                        if (result.equals("success")) {
                            showSexView.setText(mSelect);
                            Toast.makeText(getActivity(), "修改成功!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "输入重复,修改失败!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                //点击取消
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        bottomDialog.dismiss();
                    }
                });
                //防止弹出两个窗口
                if (bottomDialog !=null && bottomDialog.isShowing()) {  return; }

                bottomDialog = new BottomDialog(getContext(), R.style.ActionSheetDialogStyle);//将布局设置给Dialog
                bottomDialog.setContentView(outerView1);
                bottomDialog.show();//显示对话框
                break;
            case R.id.portrait_onhomepage:
                if (openGallary()){
                    break;
                }else {
                    return;
                }
            case R.id.btn_logout:
                getActivity().finish();
                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent2);
                break;
            default:
                Intent intent3 = new Intent();
                intent3.putExtra("isChangePortrait",false);
                getActivity().setResult(RESULT_OK,intent3);
                break;
        }
    }

    private boolean openGallary(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WE_STORAGE_PER);
            return false;
        }else if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_RE_STORAGE_PER);
            return false;
        }else if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PER);
            return false;
        }else {
            Matisse.from(getActivity())
                    .choose(MimeType.allOf())//图片类型
                    .countable(true)//true:选中后显示数字;false:选中后显示对号
                    .maxSelectable(1)//可选的最大数
                    .capture(true)//选择照片时，是否显示拍照
                    .captureStrategy(new CaptureStrategy(true, "cn.aysst.www.doctor.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                    .imageEngine(new GlideEngine())//图片加载引擎
                    .forResult(CHANGE_PORTRAIT);//
            return true;
        }
    }

    /**
     * 图片转换成base64字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgBytes = baos.toByteArray();// 转为byte数组
        return android.util.Base64.encodeToString(imgBytes, android.util.Base64.DEFAULT);
    }



    /**
     * 通过uri获取图片并进行压缩
     * @param uri
     */
    public static Bitmap getBitmapFromUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHANGE_PORTRAIT:
                if (resultCode == RESULT_OK){
                    List<Uri> result = Matisse.obtainResult(data);
                    Uri portraitUri = result.get(0);
                    Toast.makeText(getActivity(),portraitUri.toString(),Toast.LENGTH_SHORT).show();
                    String imgPath = null;

                    if (portraitUri != null){

                        if (portraitUri.toString().contains("cn.aysst.www.doctor.fileprovider")){
                            imgPath =getFPUriToPath(getContext(),portraitUri);
                        }else {
                            if(Build.VERSION.SDK_INT >= 19){
                                imgPath = getPathFromUriOnKitKat(portraitUri);
                            }else {
                                imgPath = getPathFromUriBeforeKitKat(portraitUri);
                            }
                            try {
                                portraitBit = getBitmapFromUri(getActivity(), portraitUri);
                                //circleImageView.setImageBitmap(portraitBit);
                                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), portraitBit, null,null));

                                Intent intent = new Intent();
                                intent.putExtra("isChangePortrait",true);
                                intent.putExtra("portraitBitStr",uri);
                                getActivity().setResult(RESULT_OK,intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        uploadPortrait(imgPath);
                    }
                }
                break;
        }
    }

    /**
     * 获取FileProvider path
     * author zx
     * version 1.0
     * since 2018/5/4
     * @param context
     * @param uri
     * @return 图片路劲
     */
    private static String getFPUriToPath(Context context, Uri uri) {
        try {
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs != null) {
                String fileProviderClassName = FileProvider.class.getName();
                for (PackageInfo pack : packs) {
                    ProviderInfo[] providers = pack.providers;
                    if (providers != null) {
                        for (ProviderInfo provider : providers) {
                            if (uri.getAuthority().equals(provider.authority)) {
                                if (provider.name.equalsIgnoreCase(fileProviderClassName)) {
                                    Class<FileProvider> fileProviderClass = FileProvider.class;
                                    try {
                                        Method getPathStrategy = fileProviderClass.getDeclaredMethod("getPathStrategy", Context.class, String.class);
                                        getPathStrategy.setAccessible(true);
                                        Object invoke = getPathStrategy.invoke(null, context, uri.getAuthority());
                                        if (invoke != null) {
                                            String PathStrategyStringClass = FileProvider.class.getName() + "$PathStrategy";
                                            Class<?> PathStrategy = Class.forName(PathStrategyStringClass);
                                            Method getFileForUri = PathStrategy.getDeclaredMethod("getFileForUri", Uri.class);
                                            getFileForUri.setAccessible(true);
                                            Object invoke1 = getFileForUri.invoke(invoke, uri);
                                            if (invoke1 instanceof File) {
                                                String filePath = ((File) invoke1).getAbsolutePath();
                                                return filePath;
                                            }
                                        }
                                    } catch (NoSuchMethodException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(19)
    private String getPathFromUriOnKitKat(Uri uri){
        String imagePath = null;
        if(DocumentsContract.isDocumentUri(getContext(),uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }

        if(imagePath != null){
            Log.d("文件路径", imagePath);
            return imagePath;
        }else {
            Toast.makeText(getActivity(),"无法得到图片文件",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private String getPathFromUriBeforeKitKat(Uri uri){
        String imagePath = getImagePath(uri,null);
        if(imagePath != null){
            return imagePath;
        } else {
            Toast.makeText(getActivity(),"无法得到图片文件",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 获取到图片的路径path
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_WE_STORAGE_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity(),"“写”内存权限已开放",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),"没有给予内存权限",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_RE_STORAGE_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity(),"“读”内存权限已开放",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),"没有给予内存权限",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CAMERA_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getActivity(),"照相机权限已开放",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),"没有给予照相机权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void showEditSignatureDialog(){
        EditDialogFragment dialogFragment = new EditDialogFragment();
        bundle = new Bundle();
        bundle.putString("message","我的签名");
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getActivity().getSupportFragmentManager(), "EditSignatureDialog");
    }

    private void showEditPhoneDialog(){
        EditDialogFragment dialogFragment = new EditDialogFragment();
        bundle = new Bundle();
        bundle.putString("message","我的手机");
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getActivity().getSupportFragmentManager(),"EditPhoneDialog");
    }

    @Override
    public void onDialogPositiveClick(EditDialogFragment dialog) {
        if(bundle != null){
            switch (bundle.getString("message")){
                case "我的签名":
                    String textSignature = dialog.myData;
                    String signatureResult = httpPostSignatureReq(textSignature);
                    if (signatureResult.equals("success")){
                        userSignature.setText(textSignature);
                        Toast.makeText(getActivity(), "签名修改成功!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "签名输入重复!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "我的邮箱":

                    //String newEmail = intent.getStringExtra("email");

                    //showMailView.setText(dialog.myData);
                    break;
                case "我的手机":
                    String phoneNumber = dialog.myData;
                    if (phoneNumber.length() == 11) {
                        result = true;
                    } else {
                        result = false;
                    }
                    for (int i=0; i<phoneNumber.length(); i++){
                        char ch = phoneNumber.charAt(i);
                        if (ch < '0' || ch > '9') {
                            System.out.println(ch);
                            result = false;
                            break;
                        }
                    }
                    if (result) {
                        String result = httpPostPhoneReq(phoneNumber);
                        if (result.equals("success")){
                            showPhoneView.setText(dialog.myData);
                            Toast.makeText(getActivity(), "手机号修改成功!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "手机号输入重复!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "请输入正确格式的手机号!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDialogNegativeClick(EditDialogFragment dialog) {

    }

    /**
     * 上传图片到服务器
     * @param imgPath
     */
    public void uploadPortrait(String imgPath) {
        new UploadProtraitAsyncTask().execute(imgPath);
    }

    /**
     * 更新数据库个人信息的头像
     * @param imgDddr
     */
    public void updateProtrait(String imgDddr){
        new UpdateProtraitAsyncTask().execute(imgDddr);
    }

    /**
     * 上传图片的异步任务
     */
    class UploadProtraitAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return UploadFiles.uploadHeadPortrait(params[0], name);
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("fail")) {
                String imageUrl = BASE_URL + result;
                updateProtrait(imageUrl);
            } else {
                Toast.makeText(getActivity(), "上传图片失败,请重试!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 更新数据库头像的异步任务
     */
    class UpdateProtraitAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return httpPostUpdateHeadPortraitReq(params[0], name);
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("fail")) {
                circleImageView.setImageBitmap(portraitBit);
                Toast.makeText(getActivity(), "更新头像成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String httpPostUpdateHeadPortraitReq(String imgAddr, String username) {
        String result = "fail";
        FormBody body = new FormBody.Builder()
                .add("msg", "exeHeadPortraitReqAction")
                .add("imgAddr", imgAddr)
                .add("username", username)
                .build();

        final Request request = new Request.Builder()
                .url(BASE_URL + "/ExecuteUserInfoServlet")
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

    private String httpPostSexReq(String sex) {

        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", MainActivity.MODE_PRIVATE);
        String username = preferences.getString("name", "");
        Log.d("性别", sex);

        FormBody body = new FormBody.Builder()
                .add("msg", "sexAction")
                .add("sex", sex)
                .add("name", username)
                .build();

        final Request request = new Request.Builder()
                .url(BASE_URL + "/ExecuteUserInfoServlet")
                .post(body)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        exeSexResult = response.body().string();
                        Log.d("postData", exeSexResult);
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try{
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return exeSexResult;
    }

    private String httpPostPhoneReq(String phone) {

        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", MainActivity.MODE_PRIVATE);
        String username = preferences.getString("name", "");
        Log.d("手机号", phone);

        FormBody body = new FormBody.Builder()
                .add("msg", "phoneAction")
                .add("phone", phone)
                .add("name", username)
                .build();

        final Request request = new Request.Builder()
                .url(BASE_URL + "/ExecuteUserInfoServlet")
                .post(body)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        exePhoneSesult = response.body().string();
                        Log.d("postData", exePhoneSesult);
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try{
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return exePhoneSesult;
    }

    private String httpPostSignatureReq(String signature) {

        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", MainActivity.MODE_PRIVATE);
        String username = preferences.getString("name", "");
        Log.d("签名", signature);
        FormBody body = new FormBody.Builder()
                .add("msg", "signatureAction")
                .add("signature", signature)
                .add("name", username)
                .build();

        final Request request = new Request.Builder()
                .url(BASE_URL + "/ExecuteUserInfoServlet")
                .post(body)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        exeSignatureSesult = response.body().string();
                        Log.d("postData", exeSignatureSesult);
                    } else {
                        throw new IOException("back code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try{
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return exeSignatureSesult;
    }

}
