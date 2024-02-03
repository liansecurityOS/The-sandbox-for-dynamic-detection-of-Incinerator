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

package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.LogData;
import android.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to store a set of values that the {@link ContentResolver}
 * can process.
 */
public final class ContentValues implements Parcelable {
    public static final String TAG = "ContentValues";

    /** Holds the actual values */
    private HashMap<String, Object> mValues;

    /**
     * Creates an empty set of values using the default initial size
     */
    public ContentValues() {
        // Choosing a default size of 8 based on analysis of typical
        // consumption by applications.
        mValues = new HashMap<String, Object>(8);
    }

    /**
     * Creates an empty set of values using the given initial size
     *
     * @param size the initial size of the set of values
     */
    public ContentValues(int size) {
        mValues = new HashMap<String, Object>(size, 1.0f);
    }

    /**
     * Creates a set of values copied from the given set
     *
     * @param from the values to copy
     */
    public ContentValues(ContentValues from) {
        mValues = new HashMap<String, Object>(from.mValues);
    }

    /**
     * Creates a set of values copied from the given HashMap. This is used
     * by the Parcel unmarshalling code.
     *
     * @param values the values to start with
     * {@hide}
     */
    private ContentValues(HashMap<String, Object> values) {
        mValues = values;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ContentValues)) {
            return false;
        }
        return mValues.equals(((ContentValues) object).mValues);
    }

    @Override
    public int hashCode() {
        return mValues.hashCode();
    }

    /**
     * Adds a value to the set.
     *
     * @param key the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, String value) {
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_PUT,
                new Class[]{ String.class, String.class },
                new Object[]{ key, value },
                null, null);

        mValues.put(key, value);
    }

    /**
     * Adds all values from the passed in ContentValues.
     *
     * @param other the ContentValues from which to copy
     */
    public void putAll(ContentValues other) {
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_PUT,
                new Class[]{ ContentValues.class },
                new Object[]{ other },
                null, null);

        mValues.putAll(other.mValues);
    }

    /**
     * Adds a value to the set.
     *
     * @param key the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Byte value) {
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_PUT,
                new Class[]{ String.class, Byte.class },
                new Object[]{ key, value },
                null, null);

        mValues.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Short value) {
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_PUT,
                new Class[]{ String.class, Short.class },
                new Object[]{ key, value },
                null, null);

        mValues.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Integer value) {
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_PUT,
                new Class[]{ String.class, Integer.class },
                new Object[]{ key, value },
                null, null);
        mValues.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Long value) {
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_PUT,
                new Class[]{ String.class, Long.class },
                new Object[]{ key, value },
                null, null);
        mValues.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Float value) {
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_PUT,
                new Class[]{ String.class, Float.class },
                new Object[]{ key, value },
                null, null);
        mValues.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Double value) {
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_PUT,
                new Class[]{ String.class, Double.class },
                new Object[]{ key, value },
                null, null);
        mValues.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, Boolean value) {
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_PUT,
                new Class[]{ String.class, Boolean.class },
                new Object[]{ key, value },
                null, null);
        mValues.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key the name of the value to put
     * @param value the data for the value to put
     */
    public void put(String key, byte[] value) {
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_PUT,
                new Class[]{ String.class, byte[].class },
                new Object[]{ key, value },
                null, null);
        mValues.put(key, value);
    }

    /**
     * Adds a null value to the set.
     *
     * @param key the name of the value to make null
     */
    public void putNull(String key) {
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_PUT,
                new Class[]{ String.class },
                new Object[]{ key },
                null, null);
        mValues.put(key, null);
    }

    /**
     * Returns the number of values.
     *
     * @return the number of values
     */
    public int size() {
        return mValues.size();
    }

    /**
     * Remove a single value.
     *
     * @param key the name of the value to remove
     */
    public void remove(String key) {
        mValues.remove(key);
    }

    /**
     * Removes all values.
     */
    public void clear() {
        mValues.clear();
    }

    /**
     * Returns true if this object has the named value.
     *
     * @param key the value to check for
     * @return {@code true} if the value is present, {@code false} otherwise
     */
    public boolean containsKey(String key) {
        return mValues.containsKey(key);
    }

    /**
     * Gets a value. Valid value types are {@link String}, {@link Boolean},
     * {@link Number}, and {@code byte[]} implementations.
     *
     * @param key the value to get
     * @return the data for the value, or {@code null} if the value is missing or if {@code null}
     *         was previously added with the given {@code key}
     */
    public Object get(String key) {
        Object result = mValues.get(key);
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_GET,
                new Class[]{ String.class },
                new Object[]{ key },
                Object.class, result);
        return result;
    }

    /**
     * Gets a value and converts it to a String.
     *
     * @param key the value to get
     * @return the String for the value
     */
    public String getAsString(String key) {
        Object value = mValues.get(key);
        String result = value != null ? value.toString() : null;
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_GET,
                new Class[]{ String.class },
                new Object[]{ key },
                String.class, result);
        return result;
    }

    /**
     * Gets a value and converts it to a Long.
     *
     * @param key the value to get
     * @return the Long value, or {@code null} if the value is missing or cannot be converted
     */
    public Long getAsLong(String key) {
        Object value = mValues.get(key);
        Long result = null;
        try {
            result = value != null ? ((Number) value).longValue() : null;
//            return value != null ? ((Number) value).longValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    result = Long.valueOf(value.toString());
//                    return Long.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Long value for " + value + " at key " + key);
//                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Long: " + value, e);
//                return null;
            }
        }
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_GET,
                new Class[]{ String.class },
                new Object[]{ key },
                Long.class, result);
        return result;
    }

    /**
     * Gets a value and converts it to an Integer.
     *
     * @param key the value to get
     * @return the Integer value, or {@code null} if the value is missing or cannot be converted
     */
    public Integer getAsInteger(String key) {
        Object value = mValues.get(key);
        Integer result = null;
        try {
            result = value != null ? ((Number) value).intValue() : null;
//            return value != null ? ((Number) value).intValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    result = Integer.valueOf(value.toString());
//                    return Integer.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Integer value for " + value + " at key " + key);
//                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Integer: " + value, e);
//                return null;
            }
        }
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_GET,
                new Class[]{ String.class },
                new Object[]{ key },
                Integer.class, result);
        return result;
    }

    /**
     * Gets a value and converts it to a Short.
     *
     * @param key the value to get
     * @return the Short value, or {@code null} if the value is missing or cannot be converted
     */
    public Short getAsShort(String key) {
        Object value = mValues.get(key);
        Short result = null;
        try {
            result = value != null ? ((Number) value).shortValue() : null;
//            return value != null ? ((Number) value).shortValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    result = Short.valueOf(value.toString());
//                    return Short.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Short value for " + value + " at key " + key);
//                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Short: " + value, e);
//                return null;
            }
        }
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_GET,
                new Class[]{ String.class },
                new Object[]{ key },
                Short.class, result);
        return result;
    }

    /**
     * Gets a value and converts it to a Byte.
     *
     * @param key the value to get
     * @return the Byte value, or {@code null} if the value is missing or cannot be converted
     */
    public Byte getAsByte(String key) {
        Object value = mValues.get(key);
        Byte result = null;
        try {
            result = value != null ? ((Number) value).byteValue() : null;
//            return value != null ? ((Number) value).byteValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    result = Byte.valueOf(value.toString());
//                    return Byte.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Byte value for " + value + " at key " + key);
//                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Byte: " + value, e);
//                return null;
            }
        }
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_GET,
                new Class[]{ String.class },
                new Object[]{ key },
                Byte.class, result);
        return result;
    }

    /**
     * Gets a value and converts it to a Double.
     *
     * @param key the value to get
     * @return the Double value, or {@code null} if the value is missing or cannot be converted
     */
    public Double getAsDouble(String key) {
        Object value = mValues.get(key);
        Double result = null;
        try {
            result = value != null ? ((Number) value).doubleValue() : null;
//            return value != null ? ((Number) value).doubleValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    result = Double.valueOf(value.toString());
//                    return Double.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Double value for " + value + " at key " + key);
//                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Double: " + value, e);
//                return null;
            }
        }
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_GET,
                new Class[]{ String.class },
                new Object[]{ key },
                Double.class, result);
        return result;
    }

    /**
     * Gets a value and converts it to a Float.
     *
     * @param key the value to get
     * @return the Float value, or {@code null} if the value is missing or cannot be converted
     */
    public Float getAsFloat(String key) {
        Object value = mValues.get(key);
        Float result = null;
        try {
            result = value != null ? ((Number) value).floatValue() : null;
//            return value != null ? ((Number) value).floatValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    result = Float.valueOf(value.toString());
//                    return Float.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Float value for " + value + " at key " + key);
//                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Float: " + value, e);
//                return null;
            }
        }
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_GET,
                new Class[]{ String.class },
                new Object[]{ key },
                Float.class, result);
        return result;
    }

    /**
     * Gets a value and converts it to a Boolean.
     *
     * @param key the value to get
     * @return the Boolean value, or {@code null} if the value is missing or cannot be converted
     */
    public Boolean getAsBoolean(String key) {
        Object value = mValues.get(key);
        Boolean result = null;
        try {
            result = (Boolean) value;
//            return (Boolean) value;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                result = Boolean.valueOf(value.toString());
//                return Boolean.valueOf(value.toString());
            } else if (value instanceof Number) {
                result = ((Number) value).intValue() != 0;
//                return ((Number) value).intValue() != 0;
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Boolean: " + value, e);
//                return null;
            }
        }
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_GET,
                new Class[]{ String.class },
                new Object[]{ key },
                Boolean.class, result);
        return result;
    }

    /**
     * Gets a value that is a byte array. Note that this method will not convert
     * any other types to byte arrays.
     *
     * @param key the value to get
     * @return the {@code byte[]} value, or {@code null} is the value is missing or not a
     *         {@code byte[]}
     */
    public byte[] getAsByteArray(String key) {
        Object value = mValues.get(key);
        byte[] result = null;
        if (value instanceof byte[]) {
            result = (byte[]) value;
//            return (byte[]) value;
        } else {
//            return null;
        }
        LogUtil.MyLogB(LogData.LOG_DATA_TYPE.CV_GET,
                new Class[]{ String.class },
                new Object[]{ key },
                byte[].class, result);
        return result;
    }

    /**
     * Returns a set of all of the keys and values
     *
     * @return a set of all of the keys and values
     */
    public Set<Map.Entry<String, Object>> valueSet() {
        return mValues.entrySet();
    }

    /**
     * Returns a set of all of the keys
     *
     * @return a set of all of the keys
     */
    public Set<String> keySet() {
        return mValues.keySet();
    }

    public static final Parcelable.Creator<ContentValues> CREATOR =
            new Parcelable.Creator<ContentValues>() {
        @SuppressWarnings({"deprecation", "unchecked"})
        public ContentValues createFromParcel(Parcel in) {
            // TODO - what ClassLoader should be passed to readHashMap?
            HashMap<String, Object> values = in.readHashMap(null);
            return new ContentValues(values);
        }

        public ContentValues[] newArray(int size) {
            return new ContentValues[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("deprecation")
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeMap(mValues);
    }

    /**
     * Unsupported, here until we get proper bulk insert APIs.
     * {@hide}
     */
    @Deprecated
    public void putStringArrayList(String key, ArrayList<String> value) {
        mValues.put(key, value);
    }

    /**
     * Unsupported, here until we get proper bulk insert APIs.
     * {@hide}
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public ArrayList<String> getStringArrayList(String key) {
        return (ArrayList<String>) mValues.get(key);
    }

    /**
     * Returns a string containing a concise, human-readable description of this object.
     * @return a printable representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String name : mValues.keySet()) {
            String value = getAsString(name);
            if (sb.length() > 0) sb.append(" ");
            sb.append(name + "=" + value);
        }
        return sb.toString();
    }
}
