package com.yin.android.sociallibrary.listener;


import com.yin.android.sociallibrary.PlatformType;
import java.util.Map;

/**
 * description:
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
public interface AuthListener {
    void onComplete(PlatformType platform_type, Map<String, String> map);

    void onError(PlatformType platform_type, String err_msg);

    void onCancel(PlatformType platform_type);
}
