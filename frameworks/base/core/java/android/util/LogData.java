package android.util;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class LogData {

    public enum LOG_DATA_TYPE2 {
        runtime_exec,
        remote_webview_debugging,
        audio_source,
        call_phone,
        camera,
        cell_location,
        installed_packages,
        line1_number,
        listen,
        network_operator,
        phone_type,
        running_app_processes,
        running_tasks,
        sim_operator,
        sms_send,
        dos,
    }

    public enum LOG_DATA_TYPE {
        PM_NEW_WAKE_LOCK,
        A_ON_CREATE,
        A_START_ACTIVITY,
        AT_HANDLE_RECEIVER,
        CI_GET_SYSTEM_SERVICE,
        CI_REGISTER_RECEIVER,
        S_ON_CREATE,
        CW_START_SERVICE,
        CW_SEND_BROADCAST,
        CW_START_ACTIVITY,
        AR_START_RECORDING,
        MEDIA_RECORDER_START,
        AM_GET_ACCOUNTS,
        L_GET_LATITUDE,
        L_GET_LONGITUDE,
        LM_GET_LAST_LOCATION,
        LM_GET_LAST_KNOWN_LOCATION,
        AM_GET_ACCOUNTS_BY_TYPE,
        APM_GET_INSTALLED_PACKAGES,
        C_DO_FINAL,
        C_UPDATE,
        M_DO_FINAL,
        SKS_INIT,
        DCL_INIT,
        DF_LOAD,
        DF_LOAD_DEX,
        DF_OPEN_DEX_FILE,
        PCL_INIT,
        BDCL_FIND_LIBRARY,
        DF_INIT,
        DF_LOAD_CLASS,
        AM_OPEN,
        F_EXISTS,
        FIS_READ,
        FOS_WRITE,
        IB_OPEN,
        IB_CLOSE,
        TM_GET_LINE1_NUMBER,
        WI_GET_MAC_ADDRESS,
        S_S_GET_STRING,
        TM_GET_SUBSCRIBER_ID,
        TM_GET_NEIGHBORING_CELL_INFO,
        TM_GET_PHONE_TYPE,
        TM_GET_NETWORK_TYPE,
        TM_GET_CELL_LOCATION,
        TM_GET_CALL_STATE,
        TM_GET_DEVICE_ID,
        TM_GET_DEVICE_SOFTWARE_VERSION,
        TM_GET_NETWORK_OPERATOR_NAME,
        TM_GET_DATA_STATE,
        TM_GET_DATA_ACTIVITY,
        TM_GET_NETWORK_COUNTRY_ISO,
        TM_GET_SIM_OPERATOR_NAME,
        TM_GET_SIM_COUNTRY_ISO,
        TM_GET_SIM_OPERATOR_NUMERIC,
        TM_GET_TELEPHONY_PROPERTY,
        TM_GET_NETWORK_OPERATOR,
        DPM_RESET_PASSWORD,
        TM_LISTEN,
        APM_SET_COMPONENT_ENABLED_SETTING,
        CM_SET_MOBILE_DATA_ENABLED,
        CM_SET_WIFI_ENABLED,
        DHC_SWITCH_PROXY,
        NM_NOTIFY,
        BR_ABORT_BROADCAST,
        CR_REGISTER_CONTENT_OBSERVER,
        SQLD_INSERT,
        SQLD_DELETE,
        SQLD_CREATE,
        SQLD_QUERY,
        B_ENCODE,
        B_ENCODE_TO_STRING,
        B_DECODE,
        WMI_ADD_VIEW,
        PSI_SELECT,
        WV_ADD_JAVASCRIPT_INTERFACE,
        WV_SET_WEB_VIEW_CLIENT,
        WV_SET_WEB_CHROME_CLIENT,
        WV_LOAD_URL,
        WV_SET_WEB_CONTENTS_DEBUGGING_ENABLED,
        U_OPEN_CONNECTION,
        P_CONNECT,
        P_SENDTO,
        P_CLOSE,
        P_RECVFROM,
        AHC_EXECUTE,
        M_INVOKE,
        D_IS_DEBUGGER_CONNECTED,
        PB_START,
        PM_EXEC,
        R_EXEC,
        AM_KILL_BACKGROUND_PROCESSES,
        AM_GET_RUNNING_TASKS,
        AM_GET_RUNNING_APP_PROCESSES,
        AM_SET_ALARM_CLOCK,
        P_KILL_PROCESS,
        R_LOAD,
        R_LOAD_LIBRARY,
        SM_SEND_TEXT_MESSAGE,
        SM_SEND_MULTIPART_TEXT_MESSAGE,
        SM_SEND_DATA_MESSAGE,
        SPI_EI_PUT_BOOLEAN,
        SPI_EI_PUT_STRING,
        SPI_EI_GET_INT,
        SPI_EI_PUT_FLOAT,
        SPI_EI_GET_FLOAT,
        SPI_EI_PUT_LONG,
        SPI_EI_GET_LONG,
        SPI_EI_PUT,
        SPI_EI_GET_BOOLEAN,
        SPI_EI_GET_STRING,
        SPI_EI_GET,
        SPI_EI_PUT_INT,
        CV_GET,
        CV_PUT,
        DOS,
        CALL_PHONE
    }

    private String packageName;

    private StackTraceElement[] elements;
    private Class<?>[] argClasses;
    private Object[] argValues;
    private Class<?> returnClass;
    private Object returnValue;

    private LOG_DATA_TYPE dataType;

    private String other;

    public LogData(StackTraceElement[] elements, LOG_DATA_TYPE dataType) {
        this(elements, dataType, null, null, null, null, null);
    }

    public LogData(StackTraceElement[] elements, LOG_DATA_TYPE dataType, Class<?>[] argClasses, Object[] argValues, Class<?> returnClass, Object returnValue) {
        this(elements, dataType, argClasses, argValues, returnClass, returnValue, null);
    }

    public LogData(StackTraceElement[] elements, LOG_DATA_TYPE dataType, Class<?>[] argClasses, Object[] argValues, Class<?> returnClass, Object returnValue, String other) {
        this(Application.packageName, elements, dataType, argClasses, argValues, returnClass, returnValue, other);
    }

    public LogData(String packageName, StackTraceElement[] elements, LOG_DATA_TYPE dataType, Class<?>[] argClasses, Object[] argValues, Class<?> returnClass, Object returnValue, String other) {
        this.packageName = packageName;
        this.elements = elements;
        this.dataType = dataType;
        this.argClasses = argClasses;
        this.argValues = argValues;
        this.returnClass = returnClass;
        this.returnValue = returnValue;
        this.other = other;
    }


    public static String parseClassName(Class<?> cls) {
        return cls == null ? null : cls.getName();
    }

    public static Object parseValue(Class<?> cls, Object value) {
        if (value != null && value.equals("pass")) {
            return "pass";
        }
        if (cls == Integer.class || cls == int.class) {
            return value;
        }
        if (cls == Short.class || cls == short.class) {
            return value;
        }
        if (cls == Long.class || cls == long.class) {
            return value;
        }
        if (cls == Double.class || cls == double.class) {
            return value;
        }
        if (cls == Float.class || cls == float.class) {
            return value;
        }
        if (cls == Boolean.class || cls == boolean.class) {
            return value;
        }
        if (cls == Byte.class || cls == byte.class) {
            return value;
        }
        if (cls == String.class) {
            return value;
        }
        if (cls == String[].class) {
            JSONArray array = new JSONArray();
            if (value != null) {
                for (String v : (String[]) value) {
                    array.put(v);
                }
            }
            return array;
        }
        if (cls == byte[].class) {
            JSONArray array = new JSONArray();
            if (value != null) {
                for (byte v : (byte[]) value) {
                    array.put(v);
                }
            }
            return array;
        }
        if (cls == File.class) {
            return value != null ? ((File) value).getPath() : null;
        }
        return value == null ? null : value.toString();
    }


    public String toJsonData() {
        JSONArray elementsJson = new JSONArray();
        for (StackTraceElement element : elements) {
            try {
                JSONObject elementJson = new JSONObject();
                elementJson.put("methodName", element.getMethodName());
                elementJson.put("className", element.getClassName());
                elementJson.put("fileName", element.getFileName());
                elementJson.put("lineNumber", element.getLineNumber());

                elementsJson.put(elementJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray argClassNamesJson = new JSONArray();
            JSONArray argValuesJson = new JSONArray();
            if (this.argClasses != null) {
                for (int i = 0; i < this.argClasses.length; ++i) {
                    Class<?> argClass = this.argClasses[i];

                    argClassNamesJson.put(parseClassName(argClass));
                    argValuesJson.put(parseValue(argClass, this.argValues[i]));
                }
            }
            jsonObject.put("packageName", packageName);
            jsonObject.put("dataType", dataType);
            jsonObject.put("elements", elementsJson);
            jsonObject.put("argClassNames", argClassNamesJson);
            jsonObject.put("argValues", argValuesJson);
            jsonObject.put("returnClassName", parseClassName(returnClass));
            jsonObject.put("returnValue", parseValue(returnClass, returnValue));
            jsonObject.put("other", other);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
