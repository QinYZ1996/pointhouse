package com.pointhouse.chiguan.common.base;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.pointhouse.chiguan.R;
import com.pointhouse.chiguan.common.util.SharedPreferencesUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by czg on 2017/8/3.
 *
 * Permission Group	                     |  Permissions
 *-----------------------------------------------------------------------------
 * android.permission-group.CALENDAR     |  android.permission.READ_CALENDAR
 *                                       |  android.permission.WRITE_CALENDAR
 *-----------------------------------------------------------------------------
 * android.permission-group.CAMERA	     |  android.permission.CAMERA
 *-----------------------------------------------------------------------------
 * android.permission-group.CONTACTS     |  android.permission.READ_CONTACTS
 *                                       |  android.permission.WRITE_CONTACTS
 *                                       |  android.permission.GET_ACCOUNTS
 *- ----------------------------------------------------------------------------
 * android.permission-group.LOCATION     |  android.permission.ACCESS_FINE_LOCATION
 *                                       |  android.permission.ACCESS_COARSE_LOCATION
 *-----------------------------------------------------------------------------
 * android.permission-group.MICROPHONE   |  android.permission.RECORD_AUDIO
 *-----------------------------------------------------------------------------
 * android.permission-group.PHONE	     |  android.permission.READ_PHONE_STATE
 *                                       |  android.permission.CALL_PHONE
 *                                       |  android.permission.READ_CALL_LOG
 *                                       |  android.permission.WRITE_CALL_LOG
 *                                       |  com.android.voicemail.permission.ADD_VOICEMAIL
 *                                       |  android.permission.USE_SIP
 *                                       |  android.permission.PROCESS_OUTGOING_CALLS
 *-----------------------------------------------------------------------------
 * android.permission-group.SENSORS      |  android.permission.BODY_SENSORS
 *-----------------------------------------------------------------------------
 * android.permission-group.SMS	         |  android.permission.SEND_SMS
 *                                       |  android.permission.RECEIVE_SMS
 *                                       |  android.permission.READ_SMS
 *                                       |  android.permission.RECEIVE_WAP_PUSH
 *                                       |  android.permission.RECEIVE_MMS
 *                                       |  android.permission.READ_CELL_BROADCASTS *deleted?
 *-----------------------------------------------------------------------------
 * android.permission-group.STORAGE      |  android.permission.READ_EXTERNAL_STORAGE
 *                                       |  android.permission.WRITE_EXTERNAL_STORAGE
 */

public class PermissionFragmentBase extends Fragment implements EasyPermissions.PermissionCallbacks {

    private static CallbackListener mPCallback;
    private static boolean allowedAppSettings = false;
    private final String sharePrefKey = "PermissionRequestCode";

    /**
     * permission-group
     */
    public enum PermissionGroup {
        /**
         * 日历
         */
        CALENDAR,
        /**
         * 拍照
         */
        CAMERA,
        /**
         * 通讯录
         */
        CONTACTS,
        /**
         * 地理位置
         */
        LOCATION,
        /**
         * 麦克风
         */
        MICROPHONE,
        /**
         * 电话
         */
        PHONE,
        /**
         * 传感器
         */
        SENSORS,
        /**
         * 短信
         */
        SMS,
        /**
         * 存储空间
         */
        STORAGE
    }

    /**
     * Callback interface
     */
    public interface CallbackListener {

        void onPermissionsOK(int requestCode, List<String> perms);

        void onPermissionsNG(int requestCode, List<String> perms);

        void onAppSettingsBack(int requestCode, int resultCode, Intent data);

    }

    /**
     * Request permissions based on customize.
     *
     * @param rationale  hint content
     * @param requestCode [11 ~ ],[1 ~ 9] has been used
     * @param perms customized permissions
     * @param allowAppSet allowedAppSettings
     * @param callbackListener CallbackListener
     */
    public void requestPermissions(String rationale,
                                   int requestCode,
                                   String[] perms,
                                   boolean allowAppSet,
                                   CallbackListener callbackListener) {
        allowedAppSettings = allowAppSet;
        mPCallback = callbackListener;

        EasyPermissions.requestPermissions(
                this,
                rationale,
                R.string.label_ok,
                R.string.label_cancel,
                requestCode,
                perms);

    }

    /**
     * Request permissions based on Permission-Group.
     *
     * @param permgroup defined Permission Group
     * @param allowAppSet allowedAppSettings
     * @param callbackListener CallbackListener
     */
    public void requestPermissions(PermissionGroup permgroup,
                                   boolean allowAppSet,
                                   CallbackListener callbackListener) {
        allowedAppSettings = allowAppSet;
        mPCallback = callbackListener;
        String rationale = "";
        int requestCode = -1;
        String[] perms = null;

        switch (permgroup){
            case CALENDAR:
                rationale = "使用此功能需要打开日历的权限";
                requestCode = 1;
                perms = new String[]{
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR
                };
                break;
            case CAMERA:
                rationale = "使用此功能需要打开拍照的权限";
                requestCode = 2;
                perms = new String[]{
                        Manifest.permission.CAMERA
                };
                break;
            case CONTACTS:
                rationale = "使用此功能需要打开通讯录的权限";
                requestCode = 3;
                perms = new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS
                };
                break;
            case LOCATION:
                rationale = "使用此功能需要打开地理位置的权限";
                requestCode = 4;
                perms = new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };
                break;
            case MICROPHONE:
                rationale = "使用此功能需要打开麦克风的权限";
                requestCode = 5;
                perms = new String[]{
                        Manifest.permission.RECORD_AUDIO
                };
                break;
            case PHONE:
                rationale = "使用此功能需要打开电话的权限";
                requestCode = 6;
                perms = new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.ADD_VOICEMAIL,
                        Manifest.permission.USE_SIP,
                        Manifest.permission.PROCESS_OUTGOING_CALLS
                };
                break;
            case SENSORS:
                rationale = "使用此功能需要打开传感器的权限";
                requestCode = 7;
                perms = new String[]{
                        Manifest.permission.BODY_SENSORS
                };
                break;
            case SMS:
                rationale = "使用此功能需要打开短信的权限";
                requestCode = 8;
                perms = new String[]{
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_WAP_PUSH,
                        Manifest.permission.RECEIVE_MMS
                };
                break;
            case STORAGE:
                rationale = "使用此功能需要打开存储空间的权限";
                requestCode = 9;
                perms = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                break;
        }

        EasyPermissions.requestPermissions(
                this,
                rationale,
                R.string.label_ok,
                R.string.label_cancel,
                requestCode,
                perms);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        try {
            showToast(requestCode, perms, 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mPCallback.onPermissionsOK(requestCode, perms);

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        try {
            showToast(requestCode, perms, 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mPCallback.onPermissionsNG(requestCode, perms);

        StringBuffer sb = getPermissionString(perms);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(getActivity().getApplicationContext(), "已拒绝权限" + sb + "并不再询问", Toast.LENGTH_SHORT).show();
            // 允许跳转到系统设置画面时
            if (allowedAppSettings) {
                new AppSettingsDialog
                        .Builder(this)
                        .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                        .setPositiveButton(R.string.label_ok)
                        .setNegativeButton(R.string.label_cancel)
                        .build()
                        .show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
//            Toast.makeText(this, "已从设置返回，请尝试重新操作", Toast.LENGTH_SHORT).show();

            mPCallback.onAppSettingsBack(requestCode, resultCode, data);
        }
    }

    /**
     *
     * @param requestCode
     * @param perms
     * @param permissionState 0:已获取 1:已拒绝
     * @throws IOException
     * @throws JSONException
     */
    private void showToast(int requestCode, List<String> perms, int permissionState) throws IOException, JSONException {
        String status = SharedPreferencesUtil.readSimple(
                getActivity().getApplicationContext(),
                sharePrefKey,
                String.valueOf(requestCode));

        switch (requestCode) {
            case 1:
                if (permissionState == 0) {
                    if (!"true".equals(status)) {
                        Toast.makeText(getActivity().getApplicationContext(), "已获取日历权限", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.saveSimple(
                                getActivity().getApplicationContext(),
                                sharePrefKey,
                                String.valueOf(requestCode),
                                "true");
                    }
                } else if (permissionState == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "已拒绝日历权限", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveSimple(
                            getActivity().getApplicationContext(),
                            sharePrefKey,
                            String.valueOf(requestCode),
                            "false");
                }
                break;
            case 2:
                if (permissionState == 0) {
                    if (!"true".equals(status)) {
                        Toast.makeText(getActivity().getApplicationContext(), "已获取拍照权限", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.saveSimple(
                                getActivity().getApplicationContext(),
                                sharePrefKey,
                                String.valueOf(requestCode),
                                "true");
                    }
                } else if (permissionState == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "已拒绝拍照权限", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveSimple(
                            getActivity().getApplicationContext(),
                            sharePrefKey,
                            String.valueOf(requestCode),
                            "false");
                }
                break;
            case 3:
                if (permissionState == 0) {
                    if (!"true".equals(status)) {
                        Toast.makeText(getActivity().getApplicationContext(), "已获取通讯录权限", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.saveSimple(
                                getActivity().getApplicationContext(),
                                sharePrefKey,
                                String.valueOf(requestCode),
                                "true");
                    }
                } else if (permissionState == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "已拒绝通讯录权限", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveSimple(
                            getActivity().getApplicationContext(),
                            sharePrefKey,
                            String.valueOf(requestCode),
                            "false");
                }
                break;
            case 4:
                if (permissionState == 0) {
                    if (!"true".equals(status)) {
                        Toast.makeText(getActivity().getApplicationContext(), "已获取地理位置权限", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.saveSimple(
                                getActivity().getApplicationContext(),
                                sharePrefKey,
                                String.valueOf(requestCode),
                                "true");
                    }
                } else if (permissionState == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "已拒绝地理位置权限", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveSimple(
                            getActivity().getApplicationContext(),
                            sharePrefKey,
                            String.valueOf(requestCode),
                            "false");
                }
                break;
            case 5:
                if (permissionState == 0) {
                    if (!"true".equals(status)) {
                        Toast.makeText(getActivity().getApplicationContext(), "已获取麦克风权限", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.saveSimple(
                                getActivity().getApplicationContext(),
                                sharePrefKey,
                                String.valueOf(requestCode),
                                "true");
                    }
                } else if (permissionState == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "已拒绝麦克风权限", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveSimple(
                            getActivity().getApplicationContext(),
                            sharePrefKey,
                            String.valueOf(requestCode),
                            "false");
                }
                break;
            case 6:
                if (permissionState == 0) {
                    if (!"true".equals(status)) {
                        Toast.makeText(getActivity().getApplicationContext(), "已获取电话权限", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.saveSimple(
                                getActivity().getApplicationContext(),
                                sharePrefKey,
                                String.valueOf(requestCode),
                                "true");
                    }
                } else if (permissionState == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "已拒绝电话权限", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveSimple(
                            getActivity().getApplicationContext(),
                            sharePrefKey,
                            String.valueOf(requestCode),
                            "false");
                }
                break;
            case 7:
                if (permissionState == 0) {
                    if (!"true".equals(status)) {
                        Toast.makeText(getActivity().getApplicationContext(), "已获取传感器权限", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.saveSimple(
                                getActivity().getApplicationContext(),
                                sharePrefKey,
                                String.valueOf(requestCode),
                                "true");
                    }
                } else if (permissionState == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "已拒绝传感器权限", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveSimple(
                            getActivity().getApplicationContext(),
                            sharePrefKey,
                            String.valueOf(requestCode),
                            "false");
                }
                break;
            case 8:
                if (permissionState == 0) {
                    if (!"true".equals(status)) {
                        Toast.makeText(getActivity().getApplicationContext(), "已获取短信权限", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.saveSimple(
                                getActivity().getApplicationContext(),
                                sharePrefKey,
                                String.valueOf(requestCode),
                                "true");
                    }
                } else if (permissionState == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "已拒绝短信权限", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveSimple(
                            getActivity().getApplicationContext(),
                            sharePrefKey,
                            String.valueOf(requestCode),
                            "false");
                }
                break;
            case 9:
                if (permissionState == 0) {
                    if (!"true".equals(status)) {
                        Toast.makeText(getActivity().getApplicationContext(), "已获取存储空间权限", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.saveSimple(
                                getActivity().getApplicationContext(),
                                sharePrefKey,
                                String.valueOf(requestCode),
                                "true");
                    }
                } else if (permissionState == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "已拒绝存储空间权限", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveSimple(
                            getActivity().getApplicationContext(),
                            sharePrefKey,
                            String.valueOf(requestCode),
                            "false");
                }
                break;
            default:
                if (permissionState == 0) {
                    if (!"true".equals(status)) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "已获取" + getPermissionString(perms) + "权限", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.saveSimple(
                                getActivity().getApplicationContext(),
                                sharePrefKey,
                                String.valueOf(requestCode),
                                "true");
                    }
                } else if (permissionState == 1) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "已拒绝" + getPermissionString(perms) + "权限", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtil.saveSimple(
                            getActivity().getApplicationContext(),
                            sharePrefKey,
                            String.valueOf(requestCode),
                            "false");
                }
                break;
        }
    }

    private StringBuffer getPermissionString(List<String> perms) {
        // 处理权限名字字符串
        StringBuffer sb = new StringBuffer();
        for (String str : perms) {
            sb.append(str);
            sb.append("\n");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        return sb;
    }
}
