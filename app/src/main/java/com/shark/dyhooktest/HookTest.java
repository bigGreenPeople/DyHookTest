package com.shark.dyhooktest;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookTest implements IXposedHookLoadPackage {

    public static final String SHARK = "Shark Chilli";

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("com.ss.android.ugc.aweme")) {
            XposedBridge.log(" has Hooked!");
            Class clazz = loadPackageParam.classLoader.loadClass(
                    "com.ss.android.common.applog.UserInfo");
            XposedHelpers.findAndHookMethod(clazz,
                    "getUserInfo", int.class, String.class, String[].class,
                    new XC_MethodHook() {

                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            StringBuilder sb = new StringBuilder();
                            //得到参数二
                            String[] arr = (String[]) param.args[2];
                            //合并字符串数组
                            if (null != arr) {
                                for (int i = 0; i < arr.length; i++) {
                                    sb.append(arr[i] + ":");
                                }
                            }

                            String arg1 = (String) param.args[1];
                            //判断是否是我们需要的接口
                            if (arg1.contains("aweme/v1/feed")) {
                                Log.i(SHARK, "arg0:" + param.args[0]);
                                Log.i(SHARK, "arg1:" + arg1);
                                Log.i(SHARK, "arg2:" + sb);
                                Log.i(SHARK, "result:" + param.getResult());
                            }
                        }
                    });


            XposedHelpers.findAndHookMethod(clazz,
                    "initUser", String.class,
                    new XC_MethodHook() {

                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.i(SHARK, "initUser arg0:" + param.args[0]);
                            Log.i(SHARK, "initUser result:" + param.getResult());
                        }
                    });
        }
    }
}
