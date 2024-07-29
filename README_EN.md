## Dynamic Detection

### Environment Setup
1. Download AOSP source code ([android-7.1.2_r33](https://source.android.com/docs/setup/download/downloading)).
2. Code modifications and resource additions:
   1. Copy all files from the sandbox project to the `aosp source` directory. For details on code modifications, see: [Code Analysis](#Detection Principles and Code Analysis).
   2. If packet capturing is needed, embed the packet capture software in the /system/app/ directory (e.g., [PCAPdroid](https://github.com/emanuele-f/PCAPdroid)).
3. [Compile](https://source.android.com/docs/setup/build/building).
4. Flash the device/run the emulator.

### Detection Steps
The following steps involve writing code to automate the process:
1. Decompile the APK (or use aapt) to get basic information such as package name and component info.
2. Configure the detection environment:
   1. Install the APK:
      ```shell
      adb install -r -g xxx.apk
      ```
   2. Modify config.txt:
      ```shell
      # Modify the /sdcard/analysis/config.txt file,
      # Set package name: packageName=<PACKAGE_NAME>
      # Set output type: saveType (0. save to file, 1. print to log, -1. do not save)
      adb shell echo -e "packageName=<PACKAGE_NAME>\nsaveType=0" > /sdcard/analysis/config.txt
      ```
   3. Start packet capture:
      ```shell
      # PCAPdroid example (start packet capture, and set: save pcap file, capture specified app, root mode capture)
      adb shell am start \
      -e action start \
      -e pcap_dump_mode pcap_file \
      -e pcap_name xxx.pcap \
      -e app_filter <PACKAGE_NAME> \
      -e root_capture true \
      -n com.emanuelef.remote_capture.debug/com.emanuelef.remote_capture.activities.CaptureCtrl
      ```
3. Run the APK, sequentially start all components, and rely on monkey testing:
   ```shell
   # Some commands as follows, used alternately
   # Start all components, use su for non-exported components
   adb shell am start -n <PACKAGE_NAME>/<PACKAGE_NAME>.<ACTIVITY_NAME>
   adb shell am startservice -n <PACKAGE_NAME>/<PACKAGE_NAME>.<SERVICE_NAME>
   adb shell am broadcast -n <PACKAGE_NAME>/<PACKAGE_NAME>.<RECEIVER_NAME>
   # Monkey testing
   adb shell monkey -p <PACKAGE_NAME> ...
   ```
4. Retrieve and parse results:
   1. Pull data:
      ```shell
      # Pull data from the device
      adb pull /sdcard/analysis/<PACKAGE_NAME>/analysis.txt
      adb pull /sdcard/Download/PCAPdroid/xxx.pcap
      ```
   2. Call flow analysis:
      Each line in analysis.txt represents a call flow in JSON format. For specific field formats, see [Code Analysis - Log Structure Class](#item1).
   3. Packet analysis:
      Parse the pcap file to get network information.
   4. Result analysis and output:
      1. Analyze all dynamic call flows based on your strategy and output the results.
      2. Analyze all packets based on your strategy and output the results.
5. Clean up the environment:
   1. Uninstall the APK:
      ```shell
      adb uninstall <PACKAGE_NAME>
      ```
   2. Stop packet capture:
      ```shell
      adb shell am start -e action stop -n com.emanuelef.remote_capture.debug/com.emanuelef.remote_capture.activities.CaptureCtrl
      ```
   3. Delete data files:
      ```shell
      adb shell rm /sdcard/Download/PCAPdroid/xxx.pcap /sdcard/analysis/<PACKAGE_NAME>/analysis.txt
      ```

### Detection Principles and Code Analysis
1. Modify AOSP source code to allow all APKs to automatically obtain permissions:
   1. Main code modification: android.content.pm.PackageParser
      ```java
      package android.content.pm;

      public class PackageParser {
          public void initPermissions() {
              // pass
          }
      }
      ```
2. Insert code in specified functions (e.g., LogUtil.MyLogB(...)) to output parameter information, return values, and call stacks.
   1. Main log output class: android.util.LogUtil <a id="item1"></a>
      ```java
      package android.util;

      public class LogUtil {
          // Path where analysis results are saved, specific path: SAVE_PATH + Application.packageName + "/analysis.txt"
          private final static String SAVE_PATH = "/sdcard/analysis/";
          // Configuration file path, specific format:
          // packageName=com.example.test
          // saveType=0
          private final static String CONFIG_PATH = "/sdcard/analysis/config.txt";
          // Package name prefix in the configuration file
          private final static String CONFIG_PACKAGE_NAME_LABEL = "packageName=";
          // Save type prefix in the configuration file
          private final static String CONFIG_SAVE_TYPE_LABEL = "saveType=";

          /**
          * Output function
          * @param dataType output data type
          * @param argClasses parameter class list
          * @param argValues parameter value list
          * @param returnClass return class type
          * @param returnValue return value
          */
          public synchronized static void MyLogB(LogData.LOG_DATA_TYPE dataType, Class<?>[] argClasses, Object[] argValues, Class<?> returnClass, Object returnValue) {
              // pass
          }

          /**
          * @return 0. save to file, 1. print log, -1. no save
          */
          public static int saveType() {
              // pass
          }
      }
      ```

   2. Log structure class: android.util.LogData
      ```java
      package android.util;

      public class LogData {
          // Current package name
          private String packageName;
          // Call stack information
          private StackTraceElement[] elements;
          // Parameter class information
          private Class<?>[] argClasses;
          // Parameter values
          private Object[] argValues;
          // Return class information
          private Class<?> returnClass;
          // Return value
          private Object returnValue;

          // Current log type
          private LOG_DATA_TYPE dataType;
          // Other information
          private String other;

          // The following enum types are not explained one by one, see the corresponding functions in the code for details
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
              SPI_EI_GET

_INT,
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
      }
      ```

   3. Call stack class: [java.lang.StackTraceElement](https://github.com/frohoff/jdk8u-jdk/blob/master/src/share/classes/java/lang/StackTraceElement.java).

3. Use am commands to start components and automate operations with monkey.
4. Capture network information with packet capture tools, such as PCAPdroid, and save pcap files.