package client.com.rxpermission.util.java;

import android.Manifest;
import android.util.Log;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 *  权限请求工具类
 *  @author zxh17
 * */
public class PermissionUtils {
    public static final String TAG = "Permission";

    private PermissionUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }
    public static void requestPermission(final RequestPermission requestPermission, RxPermissions rxPermissions,
                                          String... permissions) {
        if(permissions==null || permissions.length==0)return;
        //需要申请的（未授权的）
        List<String> needRequest=new ArrayList<>();
        for (String permission: permissions){
            if(!rxPermissions.isGranted(permission)){
                needRequest.add(permission);
            }
        }

        //全部权限都已经申请过，直接执行操作
        if(needRequest.isEmpty()){
            requestPermission.onRequestPermissionSuccess();
        }else {//没有申请过，则开始申请
            rxPermissions
                    .requestEach(needRequest.toArray(new String[needRequest.size()]))
                    .buffer(permissions.length)
                    .subscribe(new Consumer<List<Permission>>() {
                        @Override
                        public void accept(List<Permission> permissions) throws Exception {
                            List<String> failurePermissions = new ArrayList<>();
                            List<String> askNeverAgainPermissions = new ArrayList<>();
                            for (Permission p : permissions) {
                                if (!p.granted) {
                                    if (p.shouldShowRequestPermissionRationale) {
                                        failurePermissions.add(p.name);
                                    } else {
                                        askNeverAgainPermissions.add(p.name);
                                    }
                                }
                            }
                            if (failurePermissions.size() > 0) {
//                               Request permissions failure
                                requestPermission.onRequestPermissionFailure(failurePermissions);
                            }

                            if (askNeverAgainPermissions.size() > 0){
                                //Request permissions failure with ask never again
                                requestPermission.onRequestPermissionFailureWithAskNeverAgain(askNeverAgainPermissions);
                            }

                            if (failurePermissions.size() == 0 && askNeverAgainPermissions.size() == 0){
                                //"Request permissions success"
                                requestPermission.onRequestPermissionSuccess();
                            }
                        }
                    });
        }

    }

    /**
     * 请求外部存储的权限
     */
    public static void externalStorage(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 请求发送短信权限
     */
    public static void sendSms(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.SEND_SMS);
    }

    /**
     * 请求打电话权限
     */
    public static void callPhone(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.CALL_PHONE);
    }

    /**
     * 请求获取手机状态的权限
     */
    public static void readPhonestate(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.READ_PHONE_STATE);
    }

}
