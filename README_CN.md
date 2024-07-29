# 动态检测

### 环境配置
1. 下载aosp源码([android-7.1.2_r33](https://source.android.com/docs/setup/download/downloading))
2. 代码修改，资源添加
   1. 复制沙箱项目所有文件到`aosp源码`目录下，代码修改详情见：[代码解析](#检测原理与代码解析)
   2. 如需抓包，可将抓包软件嵌入/system/app/目录下（例如：[PCAPdroid](https://github.com/emanuele-f/PCAPdroid)）
3. [编译](https://source.android.com/docs/setup/build/building)
4. 刷机/模拟器运行

### 检测步骤
以下步骤自行编写代码，实现自动化
1. 反编译apk（或通过aapt），获取基础信息，包括：包名、组件信息等
2. 配置检测环境
   1. 安装apk
      ```shell
      adb install -r -g xxx.apk 
      ```
   2. 修改config.txt
        ```shell
        # 修改/sdcard/analysis/config.txt文件，
        # 设置包名：packageName=<PACKAGE_NAME>
        # 设置输出类型：saveType（0.保存到文件, 1.打印到log, -1.不保存）的值
        adb shell echo -e "packageName=<PACKAGE_NAME>\nsaveType=0" > /sdcard/analysis/config.txt
        ```
   3. 启动抓包
        ```shell
        # PCAPdroid例子(启动抓包，并设置：保存pcap文件、抓取指定应用、root模式抓取)
        adb shell am start \
        -e action start \
        -e pcap_dump_mode pcap_file \
        -e pcap_name xxx.pcap \
        -e app_filter <PACKAGE_NAME> \
        -e root_capture true \
        -n com.emanuelef.remote_capture.debug/com.emanuelef.remote_capture.activities.CaptureCtrl
        ```
3. 运行apk，逐一启动所有组件，并依赖monkey测试
   ```shell
   # 部分命令如下，交替使用
   # 启动所有组件，没有导出的组件使用su启动
   adb shell am start -n <PACKAGE_NAME>/<PACKAGE_NAME>.<ACTIVITY_NAME>
   adb shell am startservice -n <PACKAGE_NAME>/<PACKAGE_NAME>.<SERVICE_NAME>
   adb shell am broadcast -n <PACKAGE_NAME>/<PACKAGE_NAME>.<RECEIVER_NAME>
   # monkey测试
   adb shell monkey -p <PACKAGE_NAME> ...
   ```
4. 结果获取与解析
   1. 数据拉取
        ```shell
        # 从设备拉取数据
        adb pull /sdcard/analysis/<PACKAGE_NAME>/analysis.txt
        adb pull /sdcard/Download/PCAPdroid/xxx.pcap
        ```
   2. 调用流程解析
      
      analysis.txt中，每一行表示一个调用流程，json格式，具体字段格式查看[代码解析-日志结构类](#item1)
   
   3. 数据包解析
      
      pcap文件解析，获取网络信息

   4. 结果分析与输出
      1. 按自己的策略，将所有动态调用流程进行分析，输出结果
      2. 按自己的策略，将所有数据包进行分析，输出结果
5. 清理环境
   1. 卸载apk
      ```shell
      adb uninstall <PACKAGE_NAME>
      ```
   2. 停止抓包
        ```shell
        adb shell am start -e action stop -n com.emanuelef.remote_capture.debug/com.emanuelef.remote_capture.activities.CaptureCtrl
        ```
   3. 删除数据文件
      ```shell
      adb shell rm /sdcard/Download/PCAPdroid/xxx.pcap /sdcard/analysis/<PACKAGE_NAME>/analysis.txt
      ```

### 检测原理与代码解析
1. 修改aosp源码，令所有apk能够自动获取权限
    1. 主要修改代码：android.content.pm.PackageParser   
         ```java
        package android.content.pm;
   
        public class PackageParser {
            public void initPermissions() {
               // pass
            }
        }
         ```
2. 通过在指定的函数中插入代码（例如：LogUtil.MyLogB(...)），将参数信息、返回值、调用栈进行输出
   1. 主要输出日志类：android.util.LogUtil <a id="item1"></a>
         ```java
        package android.util;
      
        public class LogUtil {
            // 分析结果所保存的路径，具体路径：SAVE_PATH + Application.packageName + "/analysis.txt"
            private final static String SAVE_PATH = "/sdcard/analysis/";
            // 配置文件路径，具体格式：
            // packageName=com.example.test
            // saveType=0
            private final static String CONFIG_PATH = "/sdcard/analysis/config.txt";
            // 配置文件中的包名，前缀部分
            private final static String CONFIG_PACKAGE_NAME_LABEL = "packageName=";
            // 配置文件中的保存类型，前缀部分
            private final static String CONFIG_SAVE_TYPE_LABEL = "saveType=";
      
            /**
            * 输出函数
            * @param dataType 输出数据类型
            * @param argClasses 参数class list
            * @param argValues 参数值list
            * @param returnClass 返回的class类型
            * @param returnValue 返回的值
            */
            public synchronized static void MyLogB(LogData.LOG_DATA_TYPE dataType, Class<?>[] argClasses, Object[] argValues, Class<?> returnClass, Object returnValue) {
                // pass   
            }
      
            /**
            * @return 0.save to file, 1.print log, -1.no save
            */
            public static int saveType() {
                // pass
            }
        }
        ```
      
     2. 日志结构类：android.util.LogData
          ```java
         package android.util;
        
         public class LogData {
             // 当前包名
             private String packageName;
             // 调用栈信息
             private StackTraceElement[] elements;
             // 参数class信息
             private Class<?>[] argClasses;
             // 参数值
             private Object[] argValues;
             // 返回值class信息
             private Class<?> returnClass;
             // 返回值
             private Object returnValue;
        
             // 当前日志类型
             private LOG_DATA_TYPE dataType;
             // 其他信息
             private String other;
           
             // 以下枚举类型不一一说明，详情可查看代码中对应的函数
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
         }
         ```
   3. 调用栈类：[java.lang.StackTraceElement](https://github.com/frohoff/jdk8u-jdk/blob/master/src/share/classes/java/lang/StackTraceElement.java)
3. 使用am命令启动组件，通过monkey自动化操作
4. 通过抓包工具抓取网络信息，例如：PCAPdroid，保存pcap文件