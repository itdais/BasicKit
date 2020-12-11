package com.itdais.basekit.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Author:  ding.jw
 * Description:
 * 设备唯一号工具类
 */
public class AndroidIdUtils {
    private static final String TAG = AndroidIdUtils.class.getSimpleName();
    /**
     * Android_ID的bug，一些设备会拿到固定的编号
     */
    private static final String ANDROID_ID = "9774d56d682e549c";
    //生成文件UUID
    private static final String INSTALLATION = "INSTALLATION";
    private static String sID = null;

    /**
     * Return whether the device is phone.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPhone() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    private static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取android唯一编号
     * 1、imei号 15位
     * 2、MEID
     * 3、androidid(厂商定制系统存在Bug)
     * 5.Build.SERIAL串号(个别设备获取不到数据，8.0后启用，使用TelephonyManager.getSerial()也需要权限)
     *
     * @return
     */
    public String getId() {
        //1、获取设备编号
        String imei = getTelephonyId();
        if (!TextUtils.isEmpty(imei)) {
            return imei;
        }
        //2、获取androidId
        String androidId = getAndroidId();
        if (!TextUtils.isEmpty(androidId)) {
            return androidId;
        }
        //3、生成设备编号
        String deviceUuid = getDeviceId();
        if (!TextUtils.isEmpty(deviceUuid)) {
            return androidId;
        }
        return getRandomUUID();
    }

    /**
     * 根据设备信息生成设备唯一号
     *
     * @return
     */
    public static String getDeviceId() {
        try {
            StringBuffer sb = new StringBuffer("35")
                    .append(Build.BOARD.length() % 10)
                    .append(Build.BRAND.length() % 10)
                    .append(Build.DEVICE.length() % 10)
                    .append(Build.HARDWARE.length() % 10)
                    .append(Build.ID.length() % 10)
                    .append(Build.HOST.length() % 10)
                    .append(Build.MODEL.length() % 10)
                    .append(Build.MANUFACTURER.length() % 10)
                    .append(Build.TAGS.length() % 10)
                    .append(Build.TYPE.length() % 10)
                    .append(Build.PRODUCT.length() % 10);
            if (!TextUtils.isEmpty(Build.SERIAL)) {
                sb.append(Build.SERIAL.length() % 10);
                return new UUID(sb.toString().hashCode(),
                        Build.SERIAL.hashCode()).toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 生成uui的保存到文件里
     *
     * @return
     */
    public synchronized static String getRandomUUID() {
        if (sID == null) {
            File installation = new File(Utils.getContext().getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists()) {
                    writeInstallationFile(installation);
                }
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    /**
     * 获取设备唯一号
     * android8.0以后获取imei号需要权限
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return
     */
    public static String getTelephonyId() {
        TelephonyManager tm = getTelephonyManager();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(Utils.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                String imei = tm.getImei();
                if (!TextUtils.isEmpty(imei)) {
                    return imei;
                }
                String meid = tm.getMeid();
                if (!TextUtils.isEmpty(meid)) {
                    return meid;
                }
            }
        } else {
            return tm.getDeviceId();
        }
        return "";
    }

    /**
     * 获取imei
     * imei的长度为15位
     * IMEI由15位数字组成，其组成为：
     * 1、前6位数(TAC)是”型号核准号码”，一般代表机型。
     * 2、接着的2位数(FAC)是”最后装配号”，一般代表产地。
     * 3、之后的6位数(SNR)是”串号”，一般代表生产顺序号。
     * 4、最后1位数(SP)通常是”0”，为检验码，目前暂备用
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return
     */
    public static String getImei() {
        TelephonyManager tm = getTelephonyManager();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(Utils.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                return tm.getImei();
            }
        } else {
            String deviceId = tm.getDeviceId();
            if (!TextUtils.isEmpty(deviceId) && deviceId.length() >= 15) {
                return deviceId;
            }
        }
        return "";
    }

    /**
     * 获取meid
     * meid的长度为14位
     * MEID由14个十六进制数字标识，第15位为校验位，不参与空中传输。
     * RR：范围A0-FF，由官方分配
     * XXXXXX：范围 000000-FFFFFF，由官方分配
     * ZZZZZZ：范围 000000-FFFFFF，厂商分配给每台终端的流水号
     * C／CD：0-F，校验码
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return
     */
    public static String getMeid() {
        TelephonyManager tm = getTelephonyManager();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(Utils.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                return tm.getMeid();
            }
        } else {
            String deviceId = tm.getDeviceId();
            if (!TextUtils.isEmpty(deviceId) && deviceId.length() == 14) {
                return deviceId;
            }
        }
        return "";
    }

    /**
     * 获取androidid
     * 1. 厂商定制系统的Bug：不同的设备可能会产生相同的ANDROID_ID：9774d56d682e549c。
     * 2. 厂商定制系统的Bug：有些设备返回的值为null。
     *
     * @return
     */
    public static String getAndroidId() {
        String androidID = Settings.Secure.getString(Utils.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidID) && !androidID.equals(ANDROID_ID)) {
            return androidID;
        }
        return "";
    }

    /**
     * 读取文件内内容
     *
     * @param installation
     * @return
     * @throws IOException
     */
    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = null;
        byte[] bytes = null;
        try {
            f = new RandomAccessFile(installation, "r");
            bytes = new byte[(int) f.length()];
            f.readFully(bytes);
        } catch (Exception e) {
            Log.i(TAG, e.toString(), e);
        } finally {
            if (null != f) {
                f.close();
            }
        }
        if (bytes != null) {
            return new String(bytes);
        } else {
            return "";
        }
    }

    /**
     * 向文件内写入uuid
     *
     * @param installation
     * @throws IOException
     */
    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(installation);
            String id = UUID.randomUUID().toString();
            out.write(id.getBytes());
        } catch (Exception e) {
            Log.i(TAG, e.toString(), e);
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }
}
