/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dalvik.system;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Provides a simple {@link ClassLoader} implementation that operates on a list
 * of files and directories in the local file system, but does not attempt to
 * load classes from the network. Android uses this class for its system class
 * loader and for its application class loader(s).
 */
public class PathClassLoader extends BaseDexClassLoader {
    /**
     * Creates a {@code PathClassLoader} that operates on a given list of files
     * and directories. This method is equivalent to calling
     * {@link #PathClassLoader(String, String, ClassLoader)} with a
     * {@code null} value for the second argument (see description there).
     *
     * @param dexPath the list of jar/apk files containing classes and
     * resources, delimited by {@code File.pathSeparator}, which
     * defaults to {@code ":"} on Android
     * @param parent the parent class loader
     */
    public PathClassLoader(String dexPath, ClassLoader parent) {
        super(dexPath, null, null, parent);

        MyReflectLog(
                new Class[]{ String.class, ClassLoader.class},
                new Object[]{ dexPath, parent },
                null,
                null,
                "PCL_INIT"
        );
    }

    /**
     * Creates a {@code PathClassLoader} that operates on two given
     * lists of files and directories. The entries of the first list
     * should be one of the following:
     *
     * <ul>
     * <li>JAR/ZIP/APK files, possibly containing a "classes.dex" file as
     * well as arbitrary resources.
     * <li>Raw ".dex" files (not inside a zip file).
     * </ul>
     *
     * The entries of the second list should be directories containing
     * native library files.
     *
     * @param dexPath the list of jar/apk files containing classes and
     * resources, delimited by {@code File.pathSeparator}, which
     * defaults to {@code ":"} on Android
     * @param librarySearchPath the list of directories containing native
     * libraries, delimited by {@code File.pathSeparator}; may be
     * {@code null}
     * @param parent the parent class loader
     */
    public PathClassLoader(String dexPath, String librarySearchPath, ClassLoader parent) {
        super(dexPath, null, librarySearchPath, parent);

        MyReflectLog(
                new Class[]{ String.class, String.class, ClassLoader.class},
                new Object[]{ dexPath, librarySearchPath, parent },
                null,
                null,
                "PCL_INIT"
        );
    }

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
