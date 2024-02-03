package java.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LogUtil {

    public static void MyReflectLog(Class<?>[] argClasses, Object[] argValues, Class<?> returnClass, Object returnValue, String typeName) {
        try {
            Class<?> logDataTypeClass = Class.forName("android.util.LogData$LOG_DATA_TYPE");
            Field runtimeExecField = logDataTypeClass.getField(typeName);

            Class<?> logUtilClass = Class.forName("android.util.LogUtil");
            Method method = logUtilClass.getMethod("MyLogB", logDataTypeClass, Class[].class, Object[].class, Class.class, Object.class);

            Object runtimeExec = runtimeExecField.get(null);
            method.invoke(null, runtimeExec, argClasses, argValues, returnClass, returnValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
