package com.cssweb.mytest.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.cssweb.mytest.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BODY_SENSORS;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.PROCESS_OUTGOING_CALLS;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_MMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.RECEIVE_WAP_PUSH;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.USE_SIP;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by liwx on 2018/3/9.
 * 动态申请权限工具类
 */

public class PermissionUtils {
    private static final String TAG = "PermissionUtils";

    public static final int CODE_CALENDAR = 0;
    public static final int CODE_CAMERA = 1;
    public static final int CODE_CONTACTS = 2;
    public static final int CODE_LOCATION = 3;
    public static final int CODE_MICROPHONE = 4;
    public static final int CODE_PHONE = 5;
    public static final int CODE_SENSORS = 6;
    public static final int CODE_SMS = 7;
    public static final int CODE_STORAGE = 8;
    public static final int CODE_MULTI_PERMISSION = 100;
    public static final int CODE_MULTI_PERMISSION_LAUNCH_INIT = 101;//请求初始化权限
    private static final String PERMISSION_ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL";

    public static List<String[]> allPermission = new ArrayList<>();
    public static final String[] GROUP_CALENDAR = {READ_CALENDAR, WRITE_CALENDAR};
    public static final String[] GROUP_CAMERA = {CAMERA};
    public static final String[] GROUP_CONTACTS = {READ_CONTACTS, WRITE_CONTACTS, GET_ACCOUNTS};
    public static final String[] GROUP_LOCATION = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
    public static final String[] GROUP_MICROPHONE = {RECORD_AUDIO};
    public static final String[] GROUP_PHONE = {READ_PHONE_STATE, CALL_PHONE, READ_CALL_LOG, WRITE_CALL_LOG, PERMISSION_ADD_VOICEMAIL, USE_SIP,
            PROCESS_OUTGOING_CALLS};
    public static final String[] GROUP_SENSORS = {BODY_SENSORS};
    public static final String[] GROUP_SMS = {SEND_SMS, RECEIVE_SMS, READ_SMS, RECEIVE_WAP_PUSH, RECEIVE_MMS};
    public static final String[] GROUP_STORAGE = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    static {
        allPermission.add(GROUP_CALENDAR);
        allPermission.add(GROUP_CAMERA);
        allPermission.add(GROUP_CONTACTS);
        allPermission.add(GROUP_LOCATION);
        allPermission.add(GROUP_MICROPHONE);
        allPermission.add(GROUP_PHONE);
        allPermission.add(GROUP_SENSORS);
        allPermission.add(GROUP_SMS);
        allPermission.add(GROUP_STORAGE);
    }


    /**
     * 检测权限
     *
     * @return true：已授权； false：未授权；
     */
    public static boolean checkPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    /**
     * 检测多个权限 是否已授权
     *
     * @return 未授权的权限
     */
    public static List<String> checkMultiPermissions(Context context, String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (!checkPermission(context, permissions[i]))
                permissionList.add(permissions[i]);
        }
        return permissionList;
    }

    /**
     * 请求权限
     */
    public static void requestPermission(Context context, String permission, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
    }

    /**
     * 请求多个权限
     */
    public static void requestMultiPermissions(Context context, List permissionList, int requestCode) {
        String[] permissions = (String[]) permissionList.toArray(new String[permissionList.size()]);
        requestMultiPermissions(context, permissions, requestCode);
    }

    /**
     * 请求多个权限
     */
    public static void requestMultiPermissions(Context context, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
    }

    /**
     * 判断是否已拒绝过权限
     *
     * @return
     * @describe :如果应用之前请求过此权限但用户拒绝，此方法将返回 true;
     * -----------如果应用第一次请求权限或 用户在过去拒绝了权限请求，
     * -----------并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
     */
    public static boolean judgePermission(Context context, String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission))
            return true;
        else
            return false;
    }

    /**
     * 检测权限并请求权限：如果没有权限，则请求权限
     */
    public static void checkAndRequestPermission(Context context, String permission, int requestCode) {
        if (!checkPermission(context, permission)) {
            requestPermission(context, permission, requestCode);
        }
    }

    /**
     * 检测并请求多个权限
     */
    public static void checkAndRequestMorePermissions(Context context, String[] permissions, int requestCode) {
        List<String> permissionList = checkMultiPermissions(context, permissions);
        requestMultiPermissions(context, permissionList, requestCode);
    }


    /**
     * 检测权限
     *
     * @describe：具体实现由回调接口决定
     */
    public static void checkPermission(Context context, String permission, PermissionCheckCallBack callBack) {
        if (checkPermission(context, permission)) { // 用户已授予权限
            callBack.onHasPermission();
        } else {
            if (judgePermission(context, permission))  // 用户之前已拒绝过权限申请
                callBack.onUserHasAlreadyTurnedDown(permission);
            else                                       // 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
                callBack.onUserHasAlreadyTurnedDownAndDontAsk(permission);
        }
    }

    /**
     * 检测多个权限
     *
     * @describe：具体实现由回调接口决定
     */
    public static void checkMultiPermissions(Context context, String[] permissions, PermissionCheckCallBack callBack) {
        List<String> permissionList = checkMultiPermissions(context, permissions);
        if (permissionList.size() == 0) {  // 用户已授予权限
            callBack.onHasPermission();
        } else {
            boolean isFirst = true;
            for (int i = 0; i < permissionList.size(); i++) {
                String permission = permissionList.get(i);
                if (judgePermission(context, permission)) {
                    isFirst = false;
                    break;
                }
            }
            String[] unauthorizedMorePermissions = (String[]) permissionList.toArray(new String[permissionList.size()]);
            if (isFirst)// 用户之前已拒绝过权限申请
                callBack.onUserHasAlreadyTurnedDownAndDontAsk(unauthorizedMorePermissions);
            else       // 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
                callBack.onUserHasAlreadyTurnedDown(unauthorizedMorePermissions);

        }
    }


    /**
     * 检测并申请权限
     */
    public static void checkAndRequestPermission(Context context, String permission, int requestCode, PermissionRequestSuccessCallBack callBack) {
        if (checkPermission(context, permission)) {// 用户已授予权限
            callBack.onHasPermission();
        } else {
            requestPermission(context, permission, requestCode);
        }
    }

    /**
     * 检测并申请多个权限
     */
    public static void checkAndRequestMorePermissions(Context context, String[] permissions, int requestCode, PermissionRequestSuccessCallBack
            callBack) {
        List<String> permissionList = checkMultiPermissions(context, permissions);
        if (permissionList.size() == 0) {  // 用户已授予权限
            callBack.onHasPermission();
        } else {
            requestMultiPermissions(context, permissionList, requestCode);
        }
    }

    /**
     * 判断权限是否申请成功
     */
    public static boolean isPermissionRequestSuccess(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    /**
     * 用户申请权限返回
     */
    public static void onRequestPermissionResult(Context context, String permission, int[] grantResults, PermissionCheckCallBack callback) {
        if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
            callback.onHasPermission();
        } else {
            if (PermissionUtils.judgePermission(context, permission)) {
                callback.onUserHasAlreadyTurnedDown(permission);
            } else {
                callback.onUserHasAlreadyTurnedDownAndDontAsk(permission);
            }
        }
    }

    /**
     * 用户申请多个权限返回
     */
    public static void onRequestMorePermissionsResult(Context context, String[] permissions, PermissionCheckCallBack callback) {
        boolean isBannedPermission = false;
        List<String> unGrantedList = checkMultiPermissions(context, permissions);
        if (unGrantedList.size() == 0)
            callback.onHasPermission();
        else {
            for (int i = 0; i < unGrantedList.size(); i++) {
                if (!judgePermission(context, unGrantedList.get(i))) {
                    isBannedPermission = true;
                    break;
                }
            }
            String[] unGrantedArrays = unGrantedList.toArray(new String[unGrantedList.size()]);
            //已禁止再次询问权限
            if (isBannedPermission)
                callback.onUserHasAlreadyTurnedDownAndDontAsk(unGrantedArrays);
            else // 拒绝权限
                callback.onUserHasAlreadyTurnedDown(permissions);
        }

    }


    /**
     * 跳转到权限设置界面
     */
    public static void toAppSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    public interface PermissionRequestSuccessCallBack {
        /**
         * 用户已授予权限
         */
        void onHasPermission();
    }

    /**
     * 获取权限的分组信息
     *
     * @param activity
     * @param permissionArray
     * @return
     */
    public static String getPermissionGroup(Activity activity, String... permissionArray) {
        List<String> unGrantedList = checkMultiPermissions(activity, permissionArray);

        HashSet<Integer> tartgetSet = new HashSet<>();

        //        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < unGrantedList.size(); i++) {
            boolean isFindGroup = false;
            String requestedPermission = unGrantedList.get(i);
            for (int j = 0; j < allPermission.size(); j++) {
                String[] group = allPermission.get(j);
                for (int k = 0; k < group.length; k++) {
                    String permission = group[k];
                    if (requestedPermission.equals(permission)) {
                        isFindGroup = true;
                        break;
                    }
                }
                if (isFindGroup) {
                    tartgetSet.add(j);
                    //                    list.add(j);
                    break;
                }
            }
        }

        StringBuffer buffer = new StringBuffer();
        Resources res = activity.getResources();
        buffer.append(res.getString(R.string.basiclib_permission_app));
        for (Iterator<Integer> it = tartgetSet.iterator(); it.hasNext(); ) {
            int code = it.next();
            switch (code) {
                case CODE_CALENDAR:
                    buffer.append(res.getString(R.string.basiclib_permission_calendar));
                    break;
                case CODE_CAMERA:
                    buffer.append(res.getString(R.string.basiclib_permission_camera));
                    break;
                case CODE_CONTACTS:
                    buffer.append(res.getString(R.string.basiclib_permission_contacts));
                    break;
                case CODE_LOCATION:
                    buffer.append(res.getString(R.string.basiclib_permission_location));
                    break;
                case CODE_MICROPHONE:
                    buffer.append(res.getString(R.string.basiclib_permission_microphone));
                    break;
                case CODE_PHONE:
                    buffer.append(res.getString(R.string.basiclib_permission_phone));
                    break;
                case CODE_SENSORS:
                    buffer.append(res.getString(R.string.basiclib_permission_sensors));
                    break;
                case CODE_SMS:
                    buffer.append(res.getString(R.string.basiclib_permission_sms));
                    break;
                case CODE_STORAGE:
                    buffer.append(res.getString(R.string.basiclib_permission_storage));
                    break;

            }
            if (tartgetSet.size() > 1) {
                if (it.hasNext()) {
                    buffer.append(", ");
                }
            }
        }
        buffer.append(res.getString(R.string.basiclib_permission));

        return buffer.toString();
    }

    public static void showPermissionDialog(final Activity context, String message) {
        showDialog(context, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toAppSetting(context);
            }
        });
    }

    private static void showDialog(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton(context.getString(R.string.basiclib_permission_grant), okListener)
                .setNegativeButton(context.getString(R.string.basiclib_permission_refuse), null).setCancelable(false).create().show();
    }

    public interface PermissionCheckCallBack {

        /**
         * 用户已授予权限
         */
        void onHasPermission();

        /**
         * 用户已拒绝过权限
         *
         * @param permission:被拒绝的权限
         */
        void onUserHasAlreadyTurnedDown(String... permission);

        /**
         * 用户已拒绝过并且已勾选不再询问选项、用户第一次申请权限;
         *
         * @param permission:被拒绝的权限
         */
        void onUserHasAlreadyTurnedDownAndDontAsk(String... permission);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static class CustomGroup {
        /**
         * 拍照需要的权限
         */
        public static final String[] TAKE_PHOTO = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission
                .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        /**
         * 启动初始化权限
         */
        public static final String[] LAUNCH_INIT = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission
                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
        /**
         * 启动盘缠需要的权限
         */
        public static final String[] LAUNCH_PANCHAN = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission
                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        /**
         * 6.0开始BLE蓝牙需要位置权限
         */
        public static final String[] BLE_PERMISSON = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, WRITE_EXTERNAL_STORAGE};
        //日志權限
        public static final String[] LOG_PERMISSON = new String[]{ WRITE_EXTERNAL_STORAGE};

    }

    public static boolean checkPermissionsGranted(Context context, String[] permissions) {
        List<String> result = checkMultiPermissions(context, permissions);
        if (result != null && result.size() > 0)
            return false;
        return true;
    }

}
