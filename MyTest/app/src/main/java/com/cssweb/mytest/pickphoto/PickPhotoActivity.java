package com.cssweb.mytest.pickphoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cssweb.mytest.HexConverter;
import com.cssweb.mytest.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PickPhotoActivity extends AppCompatActivity {
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    private static final String TAG = "PickPhotoActivity";
    private Gson mGson;
    private String fileName;
    String path = "HAHA/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        mGson = new Gson();
        String parentPath = Environment.getExternalStorageDirectory() + File.separator + "CssAG" + File.separator + "temp" + File.separator;
        String name = "Afc.zip";
        File afcFile = new File(parentPath);
        if (!afcFile.exists()) {
            afcFile.mkdirs();
        }
        File zipFile = new File(parentPath + name);
        if (!zipFile.exists()) {
            try {
                zipFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        copyFilesFassets(PickPhotoActivity.this, "Afc.zip", parentPath + name);


        findViewById(R.id.btn_photo1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                upload("");
                //相册
                // 激活系统图库，选择一张图片
                //                Intent intent = new Intent(Intent.ACTION_PICK);
                //                intent.setType("image/*");
                //                intent.putExtra("crop", "true");
                //                intent.putExtra("scale", "true");
                //                intent.putExtra("scaleUpIfNeeded", true);
                //                intent.putExtra("aspectX", 1.5);
                //                intent.putExtra("aspectY", 1);
                //                intent.putExtra("outputX", 100);
                //                intent.putExtra("outputY", 75);
                //                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                //                intent.putExtra("return-data", false);
                //                try {
                //                    startActivityForResult(intent, 1);
                //                } catch (Exception e) {
                //                    Log.v("123", "No Activity found to handle Intent; e = " + e.toString());
                //                }

                //                Intent intent = new Intent(contextWrap.getActivity(), AlbumSelectActivity.class);
                //                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, limit>0? limit:1);


            }
        });


        findViewById(R.id.btn_photo2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
                //                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //                //设置选择类型为图片类型
                //                intent.setType("image/*");
                //                startActivityForResult(intent, 1);

                File tempFiel = getImgCacheDirectory(getApplicationContext(), "HAHA/");
                path = tempFiel.getPath();
                Log.d(TAG, "#### temp file path = " + tempFiel.getPath());
                //构建隐式Intent
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
                fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path + fileName)));
                startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);


            }
        });
        Log.d(TAG, " " + 1 % 3);
        Log.d(TAG, " " + 4 % 3);
        Log.d(TAG, " " + 7 % 3);
        Log.d(TAG, " " + 10 % 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("123", "##########  requestCode = " + requestCode);
        switch (requestCode) {
            case 1:
                //用户点击了取消
                File file = new File(path + fileName);
                Log.d(TAG, "Result Path = " + file.getPath());
                Log.e("mTag", "FILE SIZE = " + file.length() / 1024 + "");


                //                if (data != null) {
                //                    // 得到图片的全路径
                //                    Uri uri = data.getData();
                //                    if (uri != null) {
                //                        String path = CssFileUtils.getRealPathFromUri(getApplicationContext(), uri);
                //                        Log.d("123", "path = " + path);
                //                        //                    crop(uri);
                //                    } else {
                //                        Log.d("123", "uri is null ");
                //                    }
                //                } else
                //                    Log.d("123", "data is null");
                break;
        }
    }

    public static File getImgCacheDirectory(Context context, String parentCacheDir) {
        File appCacheDir = null;
        if ("mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context.getApplicationContext())) {
            appCacheDir = new File(context.getExternalFilesDir(""), parentCacheDir);
        }
        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
            // appCacheDir = context.getDir(fileName, Context.MODE_PRIVATE);
        }
        return appCacheDir;
    }

    public static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == 0;
    }

    /*
         * 剪切图片
         */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, 101);
    }

    /**
     * 将Bitmap写入SD卡中的一个文件中,并返回写入文件的Uri
     *
     * @param bm
     * @param dirPath
     * @return
     */
    private Uri saveBitmap(Bitmap bm, String dirPath) {
        //新建文件夹用于存放裁剪后的图片
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/" + dirPath);
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }

        //新建文件存储裁剪后的图片
        File img = new File(tmpDir.getAbsolutePath() + "/lwx.png");
        try {
            //打开文件输出流
            FileOutputStream fos = new FileOutputStream(img);
            //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            //刷新输出流
            fos.flush();
            //关闭输出流
            fos.close();
            //返回File类型的Uri
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private File createImageFile(String prefix) {
        File image = null;
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_hh_MM_ss").format(new Date());
        String imageName = prefix + "_JPEG_" + timeStamp + "_";
        File imageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            image = File.createTempFile(imageName, ".jpg", imageDir);
        } catch (IOException e) {
            image = null;
            e.printStackTrace();
        }
        return image;
    }

    private void startImageZoom(Uri uri) {
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    private Uri convertUri(Uri uri) {
        InputStream is;
        try {
            //Uri ----> InputStream
            is = getContentResolver().openInputStream(uri);
            //InputStream ----> Bitmap
            Bitmap bm = BitmapFactory.decodeStream(is);
            //关闭流
            is.close();
            return saveBitmap(bm, "temp");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String BitmapToString(Bitmap bitmap) {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        // 将bitmap压缩成30%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bao);
        // 将bitmap转化为一个byte数组
        byte[] bs = bao.toByteArray();
        // 将byte数组用BASE64加密
        String photoStr = HexConverter.bytesToHexString(bs);
        // 返回String
        return photoStr;
    }

    public byte[] BitmapToByte(Bitmap bitmap) {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        // 将bitmap压缩成30%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bao);
        // 将bitmap转化为一个byte数组
        byte[] bs = bao.toByteArray();
        // 将byte数组用BASE64加密
        //        String photoStr = HexConverter.bytesToHexString(bs);
        // 返回String
        return bs;
    }

    public void upload(final String s) {
        final String ur = "http://192.168.2.244:8180/corpay/ci/svlt/uploadUserHeadPic";
        //        final String ur = "http://192.168.2.244:8180/corpay/ci/svlt/uploadPictrue";
        //        final String ur = "http://192.168.2.244:8090/bbsserver/ci/uptest/interfaceUpload";
        //        final String ur = "http://192.168.2.244:8090/bbsserver/ci/uptest/interfaceUpload";

        // 将bitmap转为string，并使用BASE64加密
        //        String photo = BitmapToString(getBitmapFromAsset(getApplicationContext(), "gdg.png"));
        //        Log.d(TAG, "photo = " + photo);
        //        byte[] photoByte = BitmapToByte(getBitmapFromAsset(getApplicationContext(), "gdg.png"));
        InputStream inputStream = Bitmap2InputStream(getBitmapFromAsset(getApplicationContext(), "gdg.png"));
        InputStream inputStream2 = Bitmap2InputStream(getBitmapFromAsset(getApplicationContext(), "bb.png"));
        InputStream inputStream3 = Bitmap2InputStream(getBitmapFromAsset(getApplicationContext(), "cc.png"));
        // 获取到图片的名字
        String name = "lwx";
        // new一个请求参数
        RequestParams params = new RequestParams();
        //------bbs request start

        // 将图片和名字添加到参数中
        //        params.put("imageType", 0);
        //        params.put("masterId", "135790");
        //        params.put("photo0", inputStream);
        //        params.put("photo1", inputStream2);
        //        params.put("photo2", inputStream3);
        //------bbs request end

        //--shankephone start
        params.put("imageType", 0);
        params.put("isThirdPartyLogin", "N");
        params.put("thirdPartyType", "weixin");
        params.put("userName", "15502142822");
        params.put("photo", inputStream);

        //--shankephone end


        AsyncHttpClient client = new AsyncHttpClient();
        // 调用AsyncHttpClient的post方法


        //        File temp = new File(Environment.getExternalStorageDirectory().getPath() + "/temp.jpg");
        //        FileInputStream fis = null;
        //        try {
        //            fis = new FileInputStream(temp);
        //            int size = fis.available();
        //            Log.d(TAG, "file size = " + size);
        //        } catch (FileNotFoundException e) {
        //            e.printStackTrace();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //        try {
        //            params.put("photo", temp);
        //            //            params.put("photo", temp);
        //        } catch (FileNotFoundException e) {
        //            e.printStackTrace();
        //        }

        //-------------------------
        //        UploadTestRq rq = new UploadTestRq();
        //        rq.setName("wanghelin");
        //        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        //        // 将bitmap压缩成30%
        //        getBitmapFromAsset(getApplicationContext(), "gdg.png").compress(Bitmap.CompressFormat.JPEG, 30, bao);
        //        // 将bitmap转化为一个byte数组
        //        byte[] bs = bao.toByteArray();
        //        ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
        //        // 将bitmap压缩成30%
        //        getBitmapFromAsset(getApplicationContext(), "aa.jpg").compress(Bitmap.CompressFormat.JPEG, 30, bao);
        //        // 将bitmap转化为一个byte数组
        //        byte[] bs2 = bao2.toByteArray();
        //        ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
        //        // 将bitmap压缩成30%
        //        getBitmapFromAsset(getApplicationContext(), "fj.jpg").compress(Bitmap.CompressFormat.JPEG, 30, bao);
        //        // 将bitmap转化为一个byte数组
        //        byte[] bs1 = bao2.toByteArray();
        //        rq.setPhoto(bs);
        //        rq.setPhoto2(bs2);
        //        rq.setPhoto2(bs1);
        //        StringEntity testReq = null;
        //        try {
        //            testReq = converRequest(rq);
        //        } catch (UnsupportedEncodingException e) {
        //            e.printStackTrace();
        //        }
        //-------------------------

        client.post(ur, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                Log.e(TAG, "sendRequest::onFailuer : url = " + ur + "  HttpCode :: " + arg0);
                if (arg3 != null) {
                    Log.e(TAG, "sendRequest::onFailuer error :: " + arg3.getMessage());
                }
                Toast.makeText(getApplicationContext(), "上传失败!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                Log.d(TAG, "sendRequest::onSuccess:: HttpCode :: " + arg0);
                Toast.makeText(getApplicationContext(), "上传成功!", Toast.LENGTH_SHORT).show();
                if (arg2 != null) {
                    Log.d(TAG, "sendRequest::onSuccess:: Response :: " + new String(arg2));
                }
            }
        });
        //        client.post(getApplicationContext(), ur, testReq, "application/json; charset=UTF-8", new AsyncHttpResponseHandler() {
        //
        //            @Override
        //            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        //                Log.e(TAG, "sendRequest::onFailuer : url = " + ur + "  HttpCode :: " + statusCode);
        //                if (error != null) {
        //                    Log.e(TAG, "sendRequest::onFailuer error :: " + error.getMessage());
        //                }
        //                //                Toast.makeText(getApplicationContext(), "上传失败!", Toast.LENGTH_SHORT).show();
        //            }
        //
        //            @Override
        //            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        //                Log.d(TAG, "sendRequest::onSuccess:: HttpCode :: " + statusCode);
        //            }
        //        });
    }

    private StringEntity converRequest(Request request) throws UnsupportedEncodingException {
        JsonElement localJsonElement = this.mGson.toJsonTree(request);
        JsonObject requstJsonObject = new JsonObject();
        requstJsonObject.add(request.getClass().getSimpleName(), localJsonElement);
        Log.d(TAG, "sendRequest::request :: " + requstJsonObject.toString());
        return new StringEntity(requstJsonObject.toString(), "UTF-8");
    }

    private static AssetManager assetManager;
    private static BitmapFactory.Options opt;

    public static Bitmap getBitmapFromAsset(Context context, String fileName) {
        if (assetManager == null)
            assetManager = context.getResources().getAssets();
        InputStream is = null;
        try {
            is = assetManager.open(fileName);
            return BitmapFactory.decodeStream(is, null, getBitmapOpt());
        } catch (Exception e) {
            Log.d(TAG, "read bitmap from assets failed! " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    public InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 30, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    public static BitmapFactory.Options getBitmapOpt() {

        if (opt == null)
            opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        //        opt.inJustDecodeBounds = false;
        //        opt.inPurgeable = true;
        //        opt.inInputShareable = true;
        // 获取资源图片
        return opt;

    }


    public void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
        }
    }


}
