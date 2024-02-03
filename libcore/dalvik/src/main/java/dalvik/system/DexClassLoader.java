/*
 * Copyright (C) 2008 The Android Open Source Project
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

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A class loader that loads classes from {@code .jar} and {@code .apk} files
 * containing a {@code classes.dex} entry. This can be used to execute code not
 * installed as part of an application.
 *
 * <p>This class loader requires an application-private, writable directory to
 * cache optimized classes. Use {@code Context.getCodeCacheDir()} to create
 * such a directory: <pre>   {@code
 *   File dexOutputDir = context.getCodeCacheDir();
 * }</pre>
 *
 * <p><strong>Do not cache optimized classes on external storage.</strong>
 * External storage does not provide access controls necessary to protect your
 * application from code injection attacks.
 */
public class DexClassLoader extends BaseDexClassLoader {
    /**
     * Creates a {@code DexClassLoader} that finds interpreted and native
     * code.  Interpreted classes are found in a set of DEX files contained
     * in Jar or APK files.
     *
     * <p>The path lists are separated using the character specified by the
     * {@code path.separator} system property, which defaults to {@code :}.
     *
     * @param dexPath the list of jar/apk files containing classes and
     *     resources, delimited by {@code File.pathSeparator}, which
     *     defaults to {@code ":"} on Android
     * @param optimizedDirectory directory where optimized dex files
     *     should be written; must not be {@code null}
     * @param librarySearchPath the list of directories containing native
     *     libraries, delimited by {@code File.pathSeparator}; may be
     *     {@code null}
     * @param parent the parent class loader
     */
    public DexClassLoader(String dexPath, String optimizedDirectory,
            String librarySearchPath, ClassLoader parent) {
        super(dexPath, new File(optimizedDirectory), librarySearchPath, parent);

        MyReflectLog(
                new Class[]{ String.class, String.class, String.class, ClassLoader.class},
                new Object[]{ dexPath, optimizedDirectory, librarySearchPath, parent },
                null,
                null,
                "DCL_INIT"
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
