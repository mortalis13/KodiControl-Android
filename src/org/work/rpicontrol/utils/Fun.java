package org.work.rpicontrol.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.res.Configuration;


public class Fun {
  
  private static Context context;
  
  public static StringBuilder SSH_LOG_TEXT = new StringBuilder();
  
  public static void setContext(Context context) {
    if (Fun.context == null) Fun.context = context;
  }
  
  public static boolean fileExists(String filePath) {
    return new File(filePath).exists();
  }
  
  public static String getParentFolder(String filePath) {
    return new File(filePath).getParentFile().getAbsolutePath();
  }
  
  public static boolean createFolder(String path) {
    boolean result = true;
    
    try {
      File f = new File(path);
      if (!f.exists()) {
        f.mkdir();
      }
    }
    catch (Exception e) {
      loge("createFolder() exception" + e);
      e.printStackTrace();
      result = false;
    }
    
    return result;
  }
  
  public static int getRandomInt(int from, int to) {
    return from + new Random().nextInt(to - from + 1);
  }
  
  
  //---------------------------------------------- Resources ----------------------------------------------
  
  public static byte[] getRawResource(int resourceId) {
    if (context == null) return null;
    InputStream is = context.getResources().openRawResource(resourceId);
    
    byte[] buf = null;
    
    try {
      buf = new byte[is.available()];
      is.read(buf);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    return buf;
  }
  
  public static int getColor(int resourceId) {
    if (context == null) return 0;
    return context.getResources().getColor(resourceId);
  }
  
  public static String getString(int resourceId) {
    if (context == null) return null;
    return context.getResources().getString(resourceId);
  }
  
  public static int getInteger(int resourceId) {
    if (context == null) return 0;
    return context.getResources().getInteger(resourceId);
  }
  
  public static int getDimension(int resourceId) {
    if (context == null) return 0;
    return (int) context.getResources().getDimension(resourceId);
  }
  
  
  //---------------------------------------------- Log ----------------------------------------------
  
  private static void log(Object value, Vars.LogLevel level) {
    String msg = null;
    if (value != null) {
      msg = value.toString();
      if (Vars.APP_LOG_LEVEL == Vars.LogLevel.VERBOSE) {
        msg += " " + getCallerLogInfo();
      }
    }
    
    try {
      if (Vars.APP_LOG_LEVEL.compareTo(level) <= 0) {
        switch (level) {
        case INFO:
          Log.i(Vars.APP_LOG_TAG, msg);
          break;
        case DEBUG:
          Log.d(Vars.APP_LOG_TAG, msg);
          break;
        case WARN:
          Log.w(Vars.APP_LOG_TAG, msg);
          break;
        case ERROR:
          Log.e(Vars.APP_LOG_TAG, msg);
          break;
        }
      }
    }
    catch (Exception e) {
      System.out.println(Vars.APP_LOG_TAG + " :: " + msg);
    }
  }
  
  
  public static void log(String format, Object... values) {
    try {
      log(String.format(format, values));
    }
    catch (Exception e) {
      loge("Fun.log(format, values) Exception, " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  public static void logd(String format, Object... values) {
    try {
      logd(String.format(format, values));
    }
    catch (Exception e) {
      loge("Fun.logd(format, values) Exception, " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  public static void loge(String format, Object... values) {
    try {
      loge(String.format(format, values));
    }
    catch (Exception e) {
      loge("Fun.loge(format, values) Exception, " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  public static void logw(String format, Object... values) {
    try {
      logw(String.format(format, values));
    }
    catch (Exception e) {
      loge("Fun.loge(format, values) Exception, " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  public static void log(Object value) {
    log(value, Vars.LogLevel.INFO);
  }
  
  public static void logd(Object value) {
    log(value, Vars.LogLevel.DEBUG);
  }
  
  public static void logw(Object value) {
    log(value, Vars.LogLevel.WARN);
  }
  
  public static void loge(Object value) {
    log(value, Vars.LogLevel.ERROR);
  }
  
  public static void toast(Context context, String msg) {
    if (context == null) return;
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
  }
  
  
  //---------------------------------------------- Utils ----------------------------------------------
  
  public static void printBytes(byte[] bytes) {
    log("----- Bytes:");
    for (int i = 0; i < bytes.length; i++) {
      // System.out.print(Integer.toHexString((int) (bytes[i] & 0xff)) + " ");
      log(Integer.toHexString((int) (bytes[i] & 0xff)) + " ");
    }
    log("\n----- End Bytes:\n");
  }
  
  public static int getInt(byte[] bytes) {
    int res;
    ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
    buffer.put(bytes);
    buffer.flip();
    res = buffer.getInt();
    
    return res;
  }
  
  private static String getCallerLogInfo() {
    String result = "";
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    
    if (stackTrace != null && stackTrace.length > 1) {
      boolean currentFound = false;
      
      int len = stackTrace.length;
      for (int i = 0; i < len; i++) {
        StackTraceElement stackElement = stackTrace[i];
        String className = stackElement.getClassName();
        
        if (className != null && className.equals(Fun.class.getName())) {
          currentFound = true;
        }
        
        if (currentFound && className != null && !className.equals(Fun.class.getName())) {
          String resultClass = stackElement.getClassName();
          String method = stackElement.getMethodName();
          int line = stackElement.getLineNumber();
          result = "[" + resultClass + ":" + method + "():" + line + "]";
          break;
        }
      }
    }
    
    return result;
  }
  
  public static float dpToPx(float dp) {
    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    return px;
  }
  
  public static int getScreenWidth() {
    if (context == null) return 0;
    int result = 0;
    
    try {
      DisplayMetrics displayMetrics = new DisplayMetrics();
      Activity activity = (Activity) context;
      activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
      result = displayMetrics.widthPixels;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    return result;
  }
  
  public static int getScreenHeight() {
    if (context == null) return 0;
    int result = 0;
    
    try {
      DisplayMetrics displayMetrics = new DisplayMetrics();
      Activity activity = (Activity) context;
      activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
      result = displayMetrics.heightPixels;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    return result;
  }
  
  public static float getTextWidth(TextView textView, String text) {
    if (context == null) return 0;
    
    Paint paint = textView.getPaint();
    float textWidth = paint.measureText(text);
    
    return textWidth;
  }
  
  public static void hideKeyboard() {
    Configuration config = context.getResources().getConfiguration();
    if (config.keyboardHidden != Configuration.KEYBOARDHIDDEN_YES) {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.toggleSoftInput(0, 0);
    }
  }
  
  public static void hideKeyboard(View view) {
    if (view == null) return;
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }
  
  
  //---------------------------------------------- Read/Write ----------------------------------------------
  
  public static byte[] readBinFile(String name) {
    logd("Fun.readBinFile()");
    
    byte[] result = null;
    
    try {
      FileInputStream f = new FileInputStream(name);
      
      int fileSize = f.available();
      result = new byte[fileSize];
      f.read(result);
      f.close();
    }
    catch (Exception e) {
      e.printStackTrace();
      result = null;
    }
    
    logd("// Fun.readBinFile()");
    
    return result;
  }
  
  public static String readFile(File file) {
    try {
      FileInputStream f = new FileInputStream(file.getPath());
      
      int fileSize = f.available();
      byte[] buf = new byte[fileSize];
      f.read(buf);
      
      f.close();
      
      return new String(buf);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  
  // --- Prefs ---
  
  public static void saveSharedPref(Context context, String key, String value) {
    if (context == null) {
      if (Fun.context == null) return;
      context = Fun.context;
    }
    
    SharedPreferences sharedPreferences = context.getSharedPreferences(Vars.PREFS_FILE, 0);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(key, value);
    editor.commit();
  }
  
  public static String getSharedPref(Context context, String key) {
    return getSharedPref(context, key, null);
  }
  
  public static String getSharedPref(Context context, String key, String def) {
    if (context == null) {
      if (Fun.context == null) return null;
      context = Fun.context;
    }
    
    SharedPreferences sharedPreferences = context.getSharedPreferences(Vars.PREFS_FILE, 0);
    return sharedPreferences.getString(key, def);
  }
  
  
  public static boolean empty(Object object) {
    if (object == null) {
      return true;
    }
    if (object instanceof String) {
      return ((String) object).trim().length() == 0;
    }
    if (object instanceof Collection) {
      return ((Collection) object).isEmpty();
    }
    if (object.getClass().isArray()) {
      if (((Object[]) object).length == 0) {
        return true;
      }
    }
    
    return false;
  }
  
}
