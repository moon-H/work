package com.cssweb.mytest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by liwx on 2015/12/27.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    /**
     * DeCompress the ZIP to the path
     *
     * @param zipFileString name of ZIP
     * @param outPathString path to be unZIP
     * @throws Exception
     */
    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {

                File file = new File(outPathString + File.separator + szName);
                file.createNewFile();
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    public static void deleteFile(String filePath) {
        File folder = new File(filePath);
        Log.d(TAG, " is exist = " + folder.exists());
        boolean result = folder.delete();
        Log.d(TAG, "delete result = " + result);
    }

    public static String getSignature(Context context, String pkgName) {
        String signature = "";
        android.content.pm.Signature[] callerSigs = getSignature(context, context.getApplicationInfo().uid, pkgName);
        try {
            signature = HexConverter.bytesToHexString(MessageDigest.getInstance("SHA1").digest(callerSigs[0].toByteArray()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signature;
    }

    private static android.content.pm.Signature[] getSignature(Context context, int uid, String packagename) {
        PackageManager pm = context.getPackageManager();
        String[] packages = pm.getPackagesForUid(uid);
        if (packages != null) {
            {
                try {
                    return pm.getPackageInfo(packagename, PackageManager.GET_SIGNATURES).signatures;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
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

    public static BitmapFactory.Options getBitmapOpt() {

        if (opt == null)
            opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inJustDecodeBounds = false;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        return opt;

    }
    public static void runOnUiThread(Runnable r) {
        Looper.prepare();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(r);
        Looper.loop();
    }

    /**
     * 字符串左补字符
     *
     * @param param
     * @param size
     * @param insertStr 需要补充的字符
     * @return
     * @throws Exception
     */
    public static String addLeftChar(String param, int size, String insertStr) {
        if (size <= param.length()) {
            return param;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= size - param.length(); i++) {
            sb.append(insertStr);
        }
        sb.append(param);
        return sb.toString();
    }
}
