package android.util;

import android.app.Application;
import org.json.JSONArray;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LogUtil {

//    private static boolean SAVE_TO_FILE = true;
    private final static String SAVE_PATH = "/sdcard/analysis/";
    private final static String CONFIG_PATH = "/sdcard/analysis/config.txt";
    private final static String CONFIG_PACKAGE_NAME_LABEL = "packageName=";
    private final static String CONFIG_SAVE_TYPE_LABEL = "saveType=";

    //todo: #define LOGGER_ENTRY_MAX_PAYLOAD	(60 * 1024)
    public synchronized static void MyLogA(LogData.LOG_DATA_TYPE dataType) {
        try {
            int type = saveType();
            if (type == -1) {
                return;
            }
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            int save_index = -1;
            for (int i = 0; i < elements.length; ++i) {
                if (elements[i].getMethodName().equals("MyLogA")) {
                    save_index = i + 1;
                }
                if (elements[i].getMethodName().equals("MyReflectLog")) {
                    save_index = i + 1;
                }
            }
            int length = elements.length - save_index;
            StackTraceElement[] saveElements = new StackTraceElement[length];
            System.arraycopy(elements, save_index, saveElements, 0, length);

            LogData logData = new LogData(saveElements, dataType);
            String logString = logData.toJsonData();
            if (type == 1) {
                Log.d("LianLog...", logString);
            } else if (type == 0) {
                mkdirs(SAVE_PATH);
                writeFile(SAVE_PATH + Application.packageName + "/analysis.txt", logString, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出函数
     * @param dataType 输出数据类型
     * @param argClasses 参数class list
     * @param argValues 参数值list
     * @param returnClass 返回的class类型
     * @param returnValue 返回的值
     */
    public synchronized static void MyLogB(LogData.LOG_DATA_TYPE dataType, Class<?>[] argClasses, Object[] argValues, Class<?> returnClass, Object returnValue) {
        try {
            int type = saveType();
            if (type == -1) {
                return;
            }
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            int save_index = -1;
            for (int i = 0; i < elements.length; ++i) {
                if (elements[i].getMethodName().equals("MyLogB")) {
                    save_index = i + 1;
                }
                if (elements[i].getMethodName().equals("MyReflectLog")) {
                    save_index = i + 1;
                }
            }
            int length = elements.length - save_index;
            StackTraceElement[] saveElements = new StackTraceElement[length];
            System.arraycopy(elements, save_index, saveElements, 0, length);

            LogData logData = new LogData(saveElements, dataType, argClasses, argValues, returnClass, returnValue);
            //LogData logData = new LogData(saveElements, dataType);
            String logString = logData.toJsonData();
            if (type == 1) {
                Log.d("LianLog...", logString);
            } else if (type == 0) {
                mkdirs(SAVE_PATH);
                writeFile(SAVE_PATH + Application.packageName + "/analysis.txt", logString, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @return 0.save to file, 1.print log, -1.no save
     */
    public static int saveType() {
        FileInputStream fis = null;
        BufferedReader br = null;
        int result = -1;
        try {
            if (Application.packageName == null || Application.packageName.length() == 0) {
                return result;
            }
            File file = new File(CONFIG_PATH);
            if (!file.exists()) {
                return result;
            }
            fis = new FileInputStream(CONFIG_PATH);
            br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(CONFIG_PACKAGE_NAME_LABEL)) {
                    if (!line.substring(CONFIG_PACKAGE_NAME_LABEL.length()).trim().equals(Application.packageName)) {
                        result = -1;
                        break;
                    }
                } else if (line.startsWith(CONFIG_SAVE_TYPE_LABEL)) {
                    result = Integer.parseInt(line.substring(CONFIG_SAVE_TYPE_LABEL.length()).trim());
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }
        }
        return result;
    }

    private static void mkdirs(String dir) {
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }

    private static void writeFile(String filePath, String data, boolean append) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath, append);
            fos.write((data + "\n").getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
