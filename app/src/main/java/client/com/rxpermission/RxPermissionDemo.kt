package client.com.rxpermission

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import client.com.rxpermission.util.java.PermissionUtils
import client.com.rxpermission.util.java.RequestPermission
import com.tbruyelle.rxpermissions2.RxPermissions


class RxPermissionDemo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_permission_demo)
        requestPermis()
    }

    private fun requestPermis() {
       PermissionUtils.externalStorage(object :RequestPermission{
           override fun onRequestPermissionSuccess() {
               //request permission success, do something.
               //如网络请求之类的
           }

           override fun onRequestPermissionFailure(permissions: MutableList<String>?) {
                //Request permissions failure
               //如/隐藏下拉刷新的进度条
           }

           override fun onRequestPermissionFailureWithAskNeverAgain(permissions: MutableList<String>?) {
               //Request permissions failure
               //如/隐藏下拉刷新的进度条
           }
       }, RxPermissions(this))
    }
}
