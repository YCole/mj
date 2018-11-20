/*
 * Copyright (C) 2010 The Android Open Source Project
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

package cole.crop;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MediaDetails implements Iterable<Entry<Integer, Object>> {
    @SuppressWarnings("unused")
    private static final String TAG = "MediaDetails";

    private TreeMap<Integer, Object> mDetails = new TreeMap<Integer, Object>();
    private HashMap<Integer, Integer> mUnits = new HashMap<Integer, Integer>();

    public static final int INDEX_TITLE = 1;
    public static final int INDEX_DESCRIPTION = 2;
    public static final int INDEX_DATETIME = 3;
    public static final int INDEX_LOCATION = 4;
    public static final int INDEX_WIDTH = 5;
    public static final int INDEX_HEIGHT = 6;
    public static final int INDEX_ORIENTATION = 7;
    public static final int INDEX_DURATION = 8;
    public static final int INDEX_MIMETYPE = 9;
    public static final int INDEX_SIZE = 10;

    // for EXIF
    public static final int INDEX_MAKE = 100;
    public static final int INDEX_MODEL = 101;
    public static final int INDEX_FLASH = 102;
    public static final int INDEX_FOCAL_LENGTH = 103;
    public static final int INDEX_WHITE_BALANCE = 104;
    public static final int INDEX_APERTURE = 105;
    public static final int INDEX_SHUTTER_SPEED = 106;
    public static final int INDEX_EXPOSURE_TIME = 107;
    public static final int INDEX_ISO = 108;

    public static final int INDEX_DRM_PLAY_COUNT = 120;
    public static final int INDEX_DRM_PLAY_INTERVAL_START = 121;
    public static final int INDEX_DRM_PLAY_INTERVAL = 122;
    public static final int INDEX_DRM_PLAY_TIMERANGE_START = 123;
    public static final int INDEX_DRM_PLAY_TIMERANGE_EXPIRES = 124;

    public static final int INDEX_DRM_DISPLAY_COUNT = 125;
    public static final int INDEX_DRM_DISPLAY_INTERVAL_START = 126;
    public static final int INDEX_DRM_DISPLAY_INTERVAL = 127;
    public static final int INDEX_DRM_DISPLAY_TIMERANGE_START = 128;
    public static final int INDEX_DRM_DISPLAY_TIMERANGE_EXPIRES = 129;

    public static final int INDEX_DRM_EXECUTE_COUNT = 130;
    public static final int INDEX_DRM_EXECUTE_INTERVAL_START = 131;
    public static final int INDEX_DRM_EXECUTE_INTERVAL = 132;
    public static final int INDEX_DRM_EXECUTE_TIMERANGE_START = 133;
    public static final int INDEX_DRM_EXECUTE_TIMERANGE_EXPIRES = 134;

    public static final int INDEX_DRM_PRINT_COUNT = 135;
    public static final int INDEX_DRM_PRINT_INTERVAL_START = 136;
    public static final int INDEX_DRM_PRINT_INTERVAL = 137;
    public static final int INDEX_DRM_PRINT_TIMERANGE_START = 138;
    public static final int INDEX_DRM_PRINT_TIMERANGE_EXPIRES = 139;
    public static final int INDEX_DRM_NO_LICENSE = 140;

    public static final int INDEX_DRM_FL_PLAY = 141;
    public static final int INDEX_DRM_FL_DISPLAY = 142;
    public static final int INDEX_DRM_FL_EXECUTE = 143;
    public static final int INDEX_DRM_FL_PRINT = 144;

    // Put this last because it may be long.
    public static final int INDEX_PATH = 200;

    public static class FlashState {
        private static int FLASH_FIRED_MASK = 1;
        private static int FLASH_RETURN_MASK = 2 | 4;
        private static int FLASH_MODE_MASK = 8 | 16;
        private static int FLASH_FUNCTION_MASK = 32;
        private static int FLASH_RED_EYE_MASK = 64;
        private int mState;

        public FlashState(int state) {
            mState = state;
        }

        public boolean isFlashFired() {
            return (mState & FLASH_FIRED_MASK) != 0;
        }
    }

    public void addDetail(int index, Object value) {
        mDetails.put(index, value);
    }

    public Object getDetail(int index) {
        return mDetails.get(index);
    }

    public int size() {
        return mDetails.size();
    }

    @Override
    public Iterator<Entry<Integer, Object>> iterator() {
        return mDetails.entrySet().iterator();
    }

    public void setUnit(int index, int unit) {
        mUnits.put(index, unit);
    }

    public boolean hasUnit(int index) {
        return mUnits.containsKey(index);
    }

    public int getUnit(int index) {
        return mUnits.get(index);
    }

    private static void setExifData(MediaDetails details, ExifTag tag, int key) {
        if (tag != null) {
            String value = null;
            int type = tag.getDataType();
            if (type == ExifTag.TYPE_UNSIGNED_RATIONAL
                    || type == ExifTag.TYPE_RATIONAL) {
                value = String.valueOf(tag.getValueAsRational(0).intValue());
            } else if (type == ExifTag.TYPE_ASCII) {
                value = tag.getValueAsString();
            } else {
                value = String.valueOf(tag.forceGetValueAsLong(0));
            }
            if (key == MediaDetails.INDEX_FLASH) {
                FlashState state = new FlashState(
                        Integer.valueOf(value.toString()));
                details.addDetail(key, state);
            } else {
                details.addDetail(key, value);
            }
        }
    }

    public static void extractExifInfo(MediaDetails details, String filePath) {

        ExifInterface exif = new ExifInterface();
      /*  try {
            exif.readExif(filePath);
        } catch (Exception e) {
            Log.w(TAG, "Could not read exif from file: " + filePath, e);
        }

        setExifData(details, exif.getTag(ExifInterface.TAG_FLASH),
                MediaDetails.INDEX_FLASH);
        setExifData(details, exif.getTag(ExifInterface.TAG_IMAGE_WIDTH),
                MediaDetails.INDEX_WIDTH);
        setExifData(details, exif.getTag(ExifInterface.TAG_IMAGE_LENGTH),
                MediaDetails.INDEX_HEIGHT);
        setExifData(details, exif.getTag(ExifInterface.TAG_MAKE),
                MediaDetails.INDEX_MAKE);
        setExifData(details, exif.getTag(ExifInterface.TAG_MODEL),
                MediaDetails.INDEX_MODEL);
        setExifData(details, exif.getTag(ExifInterface.TAG_F_NUMBER),
                MediaDetails.INDEX_APERTURE);

        // Log.e("zhy", "ISO:" +
        // exif.getTag(ExifInterface.TAG_ISO_SPEED_RATINGS));
        setExifData(details, exif.getTag(ExifInterface.TAG_ISO_SPEED_RATINGS),
                MediaDetails.INDEX_ISO);
        setExifData(details, exif.getTag(ExifInterface.TAG_WHITE_BALANCE),
                MediaDetails.INDEX_WHITE_BALANCE);
        setExifData(details, exif.getTag(ExifInterface.TAG_EXPOSURE_TIME),
                MediaDetails.INDEX_EXPOSURE_TIME);
        ExifTag focalTag = exif.getTag(ExifInterface.TAG_FOCAL_LENGTH);
        if (focalTag != null) {
            details.addDetail(MediaDetails.INDEX_FOCAL_LENGTH, focalTag
                    .getValueAsRational(0).toDouble());
            details.setUnit(MediaDetails.INDEX_FOCAL_LENGTH, R.string.unit_mm);
        }*/
    }
}
