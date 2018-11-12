package cole;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cole.net.NetChangeObserver;
import cole.net.NetStateReceiver;
import cole.net.NetworkUtils;
import cole.utils.Utils;

import static cole.utils.ToastUtils.showLongToast;
import static cole.utils.ToastUtils.showShortToast;

/**
 * Created by Coleman on 2016/12/6.
 */
public class MyApp extends Application {

    public static Context mContext;
    //SDCard图片缓存的文件夹夹的名字
    //是否是第一登录
    private static final String shref_filename = "config";
    //ctrl+shift+u
    private static final String IS_FISTLOGIN = "isfirstlogin";

    private NetworkUtils.NetworkType mNetType;

    private NetStateReceiver netStateReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
//        FreelineCore.init(this);


        Context context = getApplicationContext();
// 获取当前包名
        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());

        initNetChangeReceiver();
        Utils.init(mContext);
        isFisrtLogin();
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isFisrtLogin() {

        SharedPreferences shref = mContext.getSharedPreferences(shref_filename, Context.MODE_PRIVATE);
        boolean isFirst = shref.getBoolean(IS_FISTLOGIN, true);
        if (isFirst) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                shref.edit().putBoolean(IS_FISTLOGIN, false).apply();
            }
        }
        return isFirst;
    }


    /**
     * 应用全局的网络变化处理
     */
    private void initNetChangeReceiver() {
        //获取当前网络类型
        mNetType = NetworkUtils.getNetworkType(mContext);
        //定义网络状态的广播接受者
        netStateReceiver = NetStateReceiver.getReceiver();
        //给广播接受者注册一个观察者
        netStateReceiver.registerObserver(netChangeObserver);
        //注册网络变化广播
        NetworkUtils.registerNetStateReceiver(mContext, netStateReceiver);
    }

    private NetChangeObserver netChangeObserver = new NetChangeObserver() {
        @Override
        public void onConnect(NetworkUtils.NetworkType type) {
            if (type == mNetType) return; //net not change
            switch (type) {
                case NETWORK_WIFI:
                    showLongToast("已切换到 WIFI 网络");
                    break;
                case NETWORK_MOBILE:
                    showLongToast("已切换到 2G/3G/4G 网络");
                    break;
            }
            mNetType = type;
        }

        @Override
        public void onDisConnect() {
            showShortToast("网络已断开,请检查网络设置");
            mNetType = NetworkUtils.NetworkType.NETWORK_NO;
        }
    };
}
