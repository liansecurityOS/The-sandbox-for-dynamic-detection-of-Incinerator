## about sandbox

This is the sandbox currently used by our Incinerator. As a reverse engineering tool, we found that users often need more than just routine static analysis, repeatedly acquiring information like "permissions," "basic information," and so on. Moreover, the demand for rapid positioning dynamic analysis has become increasingly frequent. Therefore, we have integrated the sandbox with static reverse engineering tools, allowing users to reduce the analysis operations on APK reverse code. For unknown malicious samples and samples that require in-depth analysis, Incinerator achieves deep dynamic and static analysis.

The following is a sample analysis report for the sandbox: [https://incinerator.cloud/#/report/share?shareToken=U2FsdGVkX19AbBVzhhQzr34%2BWj12gtvSCN1FanKT15hqp0dUn1bW6PBgb3XwI%2FaQNieVViTGStvftdRyr4c%2BgQ%3D%3D](https://incinerator.cloud/#/report/share?shareToken=U2FsdGVkX19AbBVzhhQzr34%2BWj12gtvSCN1FanKT15hqp0dUn1bW6PBgb3XwI%2FaQNieVViTGStvftdRyr4c%2BgQ%3D%3D)

The sandbox provides a comprehensive suite of functionalities tailored for in-depth analysis of APKs, including:

1. **Network Request Analysis**
   - Captures and analyzes HTTP, DNS, and TCP requests made by the APK.

2. **Behavior Analysis**
   - Dynamically captures and logs the behavior of the APK when executed within the sandbox, providing insights into its runtime operations.

3. **Component Information**
   - Records operations related to the APK's components, including:
     - Activities: Tracks the actions performed by the APK's activities.
     - Services: Monitors the operations of services within the APK.
     - Receivers: Logs the events captured by the APK's broadcast receivers.
     - Providers: Details the interactions with content providers.
     - Broadcasts: Captures the broadcasts made or received by the APK.

4. **Data Access**
   - Logs all data access operations performed by the APK, offering a detailed view of its data handling practices.

This feature set is designed to facilitate a thorough examination of APKs, from network interactions to detailed behavior analysis, thereby enabling users to identify potentially malicious activities and understand the functionality of the application.
