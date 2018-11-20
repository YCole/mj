/**
 * @File: DrmInputStream.java
 * @Description: use this class to decode drm image.
 * @Author: zhi.hongye@hct.com.cn
 * @Date:
 */
package cole.crop;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public final class DrmInputStream extends InputStream {

    static {
        System.loadLibrary("hctdrminputstream");
    }

    private String mPath;
    private long mNativeInputStream = 0;

    public static DrmInputStream open(final File file) {
        DrmInputStream stream = null;
        if (file == null) {
            return null;
        }

        FileDescriptor fd = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fd = fis.getFD();
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {

        }

        try {
            stream = nativeOpen(fd);
            if (fis != null) {
                fis.close();
            }
            if (stream == null) {
                return null;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        return stream;

    }

    public static DrmInputStream open(final Context context, final Uri uri) {
        DrmInputStream stream = null;
        if (context == null) {
            return null;
        }

        ParcelFileDescriptor pfd = null;
        try {
            pfd = context.getContentResolver().openFileDescriptor(uri, "r");
        } catch (FileNotFoundException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        if (pfd == null) {
            return null;
        }


        FileDescriptor fd = pfd.getFileDescriptor();
        try {
            stream = nativeOpen(fd);
            if (stream == null) {
                return null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block

        }
        return stream;

    }

    public static DrmInputStream open(final FileDescriptor fd) {
        DrmInputStream stream = null;

        try {
            stream = nativeOpen(fd);
            if (stream == null) {
                return null;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        return stream;

    }

    public static DrmInputStream open(final String filePath) {
        DrmInputStream stream = null;
        FileInputStream fis = null;
        FileDescriptor fd = null;
        try {
            fis = new FileInputStream(filePath);
            if (fis != null) {
                fd = fis.getFD();
            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            stream = nativeOpen(fd);
            if (fis != null) {
                fis.close();
            }
            if (stream == null) {
                return null;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        return stream;

    }

    private DrmInputStream(final long fileHandle) {
        mNativeInputStream = fileHandle;
    }

    @Override
    public synchronized int read() throws IOException {
        // TODO Auto-generated method stub
        if (mNativeInputStream == 0) {
            throw (new IOException("File not opened."));
        }
        byte[] buffer = new byte[1];
        int readSize = nativeRead(buffer);
        if (readSize <= 0) {
            return -1;
        }
        return buffer[0] & 0xff;
    }

    @SuppressWarnings("JniMissingFunction")
    private static native DrmInputStream nativeOpen(final FileDescriptor fd);

    @SuppressWarnings("JniMissingFunction")
    private native int nativeAvailable();

    @SuppressWarnings("JniMissingFunction")
    private native void nativeClose();

    @SuppressWarnings("JniMissingFunction")
    private native void nativeMark(final int pos);

    @SuppressWarnings("JniMissingFunction")
    private native int nativeMarkSupport();

    @SuppressWarnings("JniMissingFunction")
    private native int nativeRead(final byte[] buffer);

    @SuppressWarnings("JniMissingFunction")
    private native int nativeReadWithOffset(final byte[] buffer,
                                            final int offset, final int length);

    @SuppressWarnings("JniMissingFunction")
    private native void nativeReset();

    @SuppressWarnings("JniMissingFunction")
    private native long nativeSkip(final long byteCount);

    @Override
    public synchronized int available() throws IOException {
        // TODO Auto-generated method stub
        if (mNativeInputStream == 0) {
            throw (new IOException("File not opened."));
        }
        return nativeAvailable();
    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub
        if (mNativeInputStream == 0) {
            super.close();
            throw (new IOException("File not opened."));
        }

        nativeClose();
        super.close();
    }

    @Override
    public void mark(final int readlimit) {
        // TODO Auto-generated method stub
        if (mNativeInputStream == 0) {
            return;
        }
        // return;
        nativeMark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return false;
        // TODO Auto-generated method stub
    }

    @Override
    public synchronized int read(final byte[] buffer, final int offset,
                                 final int length) throws IOException {
        // TODO Auto-generated method stub
        if (mNativeInputStream == 0) {
            throw (new IOException("File not opened."));
        }
        if (buffer == null) {
            throw (new IOException("NonePointer exception."));
        }
        if (length < 0 || offset < 0 || offset > buffer.length
                || ((offset + length) > buffer.length)
                || ((offset + length) < 0)) {
            return -1;
        }

        return nativeReadWithOffset(buffer, offset, length);
    }

    @Override
    public synchronized int read(final byte[] buffer) throws IOException {
        // TODO Auto-generated method stub

        if (mNativeInputStream == 0) {
            throw (new IOException("File not opened."));
        }
        return nativeRead(buffer);
    }

    @Override
    public synchronized void reset() throws IOException {
        // TODO Auto-generated method stub
        if (mNativeInputStream == 0) {
            throw (new IOException("File not opened."));
        }
        nativeReset();
    }

    @Override
    public synchronized long skip(final long byteCount) throws IOException {
        // TODO Auto-generated method stub
        if (mNativeInputStream == 0) {
            throw (new IOException("File not opened."));
        }
        return nativeSkip(byteCount);
    }
}
