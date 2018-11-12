/*
Factory.java
Copyright (C) 2010  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package org.linphone.core;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.linphone.mediastream.Log;
import org.linphone.mediastream.Version;
import org.linphone.core.tools.OpenH264DownloadHelper;

/**
  * #LinphoneFactory is a singleton object devoted to the creation of all the
  * object of Liblinphone that cannot be created by #LinphoneCore itself. 
  */
public abstract class Factory {
    static Factory _Factory;

    public static final synchronized Factory instance() {
		try {
			if (_Factory == null) {
				_Factory = new FactoryImpl(0); // This value is not relevant, correct factory pointer will be used in JNI layer
			}
		} catch (Exception e) {
			System.err.println("Cannot instanciate factory");
		}
		return _Factory;
	}

    abstract public OpenH264DownloadHelper createOpenH264DownloadHelper(Context context);

    /**
      * Gets the LoggingService singleton
      */
    abstract public LoggingService getLoggingService();

    abstract public void setDebugMode(boolean enable, String tag);

    abstract public Core getCore(long ptr);

    /**
      * Get the directory where the data resources are located. 
      */
    abstract public String getDataResourcesDir();

    /**
      * Set the directory where the data resources are located. 
      */
    abstract public void setDataResourcesDir(String path);

    /**
      * Returns a bctbx_list_t of all DialPlans. 
      */
    abstract public DialPlan[] getDialPlans();

    /**
      * Get the directory where the image resources are located. 
      */
    abstract public String getImageResourcesDir();

    /**
      * Set the directory where the image resources are located. 
      */
    abstract public void setImageResourcesDir(String path);

    /**
      * Sets the log collection path. 
      */
    abstract public void setLogCollectionPath(String path);

    /**
      * Get the directory where the mediastreamer2 plugins are located. 
      */
    abstract public String getMspluginsDir();

    /**
      * Set the directory where the mediastreamer2 plugins are located. 
      */
    abstract public void setMspluginsDir(String path);

    /**
      * Get the directory where the ring resources are located. 
      */
    abstract public String getRingResourcesDir();

    /**
      * Set the directory where the ring resources are located. 
      */
    abstract public void setRingResourcesDir(String path);

    /**
      * Get the directory where the sound resources are located. 
      */
    abstract public String getSoundResourcesDir();

    /**
      * Set the directory where the sound resources are located. 
      */
    abstract public void setSoundResourcesDir(String path);

    /**
      * Get the list of standard video definitions supported by Linphone. 
      */
    abstract public VideoDefinition[] getSupportedVideoDefinitions();

    /**
      * Get the top directory where the resources are located. 
      */
    abstract public String getTopResourcesDir();

    /**
      * Set the top directory where the resources are located. 
      */
    abstract public void setTopResourcesDir(String path);

    /**
      * Parse a string holding a SIP URI and create the according #LinphoneAddress
      * object. 
      */
    abstract public Address createAddress(String addr);

    /**
      * Creates a #LinphoneAuthInfo object. 
      */
    abstract public AuthInfo createAuthInfo(String username, String userid, String passwd, String ha1, String realm, String domain);

    /**
      * Creates an object #LinphoneBuffer. 
      */
    abstract public Buffer createBuffer();

    /**
      * Creates an object #LinphoneBuffer. 
      */
    abstract public Buffer createBufferFromData(byte[] data, int size);

    /**
      * Creates an object #LinphoneBuffer. 
      */
    abstract public Buffer createBufferFromString(String data);

    /**
      * Creates an object #LinphoneConfig. 
      */
    abstract public Config createConfig(String path);

    /**
      * Creates an object #LinphoneConfig. 
      */
    abstract public Config createConfigFromString(String data);

    /**
      * Creates an object #LinphoneConfig. 
      */
    abstract public Config createConfigWithFactory(String path, String factoryPath);

    /**
      * Creates an object #LinphoneContent. 
      */
    abstract public Content createContent();

    /**
      * Instantiate a #LinphoneCore object. 
      */
    abstract public Core createCore(String configPath, String factoryConfigPath, Object systemContext);

    /**
      * Instantiate a #LinphoneCore object with a given LinphoneConfig. 
      */
    abstract public Core createCoreWithConfig(Config config, Object systemContext);

    /**
      * Creates an object LinphoneErrorInfo. 
      */
    abstract public ErrorInfo createErrorInfo();

    /**
      * Creates an object LinphoneRange. 
      */
    abstract public Range createRange();

    /**
      * Creates an object LinphoneTransports. 
      */
    abstract public Transports createTransports();

    /**
      * Creates an object #LinphoneTunnelConfig. 
      */
    abstract public TunnelConfig createTunnelConfig();

    /**
      * Create an empty #LinphoneVcard. 
      */
    abstract public Vcard createVcard();

    /**
      * Creates an object LinphoneVideoActivationPolicy. 
      */
    abstract public VideoActivationPolicy createVideoActivationPolicy();

    /**
      * Create a #LinphoneVideoDefinition from a given width and height. 
      */
    abstract public VideoDefinition createVideoDefinition(int width, int height);

    /**
      * Create a #LinphoneVideoDefinition from a given standard definition name. 
      */
    abstract public VideoDefinition createVideoDefinitionFromName(String name);

    /**
      * Enables or disables log collection. 
      */
    abstract public void enableLogCollection(LogCollectionState state);

    /**
      * Sets the object to store in this object user's data
      */
    abstract public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    abstract public Object getUserData();
}

class FactoryImpl extends Factory {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected FactoryImpl(long ptr) {
        nativePtr = ptr;
    }

	private static boolean loadOptionalLibrary(String s) {
		try {
			System.loadLibrary(s);
			return true;
		} catch (Throwable e) {
			android.util.Log.w("FactoryImpl", "Unable to load optional library " + s + ": " + e.getMessage());
		}
		return false;
	}

	static {
		System.loadLibrary("c++_shared");
		loadOptionalLibrary("ffmpeg-linphone");
		System.loadLibrary("bctoolbox");
		System.loadLibrary("ortp");
		System.loadLibrary("mediastreamer_base");
		System.loadLibrary("mediastreamer_voip");
		System.loadLibrary("linphone");
		Version.dumpCapabilities();
	}

    public OpenH264DownloadHelper createOpenH264DownloadHelper(Context context) {
		if (context == null) {
			new CoreException("Cannot create OpenH264DownloadHelper");
			return null;
		}
		return new OpenH264DownloadHelper(context);
	}

	private native Core getCore(long nativePtr, long ptr);
	@Override
	public Core getCore(long ptr) {
	    return getCore(nativePtr, ptr);
	}

    @Override
	public LoggingService getLoggingService() {
		LoggingService l = new LoggingServiceImpl(0);
		return l.get();
	}

    @Override
    public native void setDebugMode(boolean enable, String tag);

    private native String getDataResourcesDir(long nativePtr);
    @Override
    synchronized public String getDataResourcesDir()  {
        
        return getDataResourcesDir(nativePtr);
    }

    private native void setDataResourcesDir(long nativePtr, String path);
    @Override
    synchronized public void setDataResourcesDir(String path)  {
        
        setDataResourcesDir(nativePtr, path);
    }

    private native DialPlan[] getDialPlans(long nativePtr);
    @Override
    synchronized public DialPlan[] getDialPlans()  {
        
        return getDialPlans(nativePtr);
    }

    private native String getImageResourcesDir(long nativePtr);
    @Override
    synchronized public String getImageResourcesDir()  {
        
        return getImageResourcesDir(nativePtr);
    }

    private native void setImageResourcesDir(long nativePtr, String path);
    @Override
    synchronized public void setImageResourcesDir(String path)  {
        
        setImageResourcesDir(nativePtr, path);
    }

    private native void setLogCollectionPath(long nativePtr, String path);
    @Override
    synchronized public void setLogCollectionPath(String path)  {
        
        setLogCollectionPath(nativePtr, path);
    }

    private native String getMspluginsDir(long nativePtr);
    @Override
    synchronized public String getMspluginsDir()  {
        
        return getMspluginsDir(nativePtr);
    }

    private native void setMspluginsDir(long nativePtr, String path);
    @Override
    synchronized public void setMspluginsDir(String path)  {
        
        setMspluginsDir(nativePtr, path);
    }

    private native String getRingResourcesDir(long nativePtr);
    @Override
    synchronized public String getRingResourcesDir()  {
        
        return getRingResourcesDir(nativePtr);
    }

    private native void setRingResourcesDir(long nativePtr, String path);
    @Override
    synchronized public void setRingResourcesDir(String path)  {
        
        setRingResourcesDir(nativePtr, path);
    }

    private native String getSoundResourcesDir(long nativePtr);
    @Override
    synchronized public String getSoundResourcesDir()  {
        
        return getSoundResourcesDir(nativePtr);
    }

    private native void setSoundResourcesDir(long nativePtr, String path);
    @Override
    synchronized public void setSoundResourcesDir(String path)  {
        
        setSoundResourcesDir(nativePtr, path);
    }

    private native VideoDefinition[] getSupportedVideoDefinitions(long nativePtr);
    @Override
    synchronized public VideoDefinition[] getSupportedVideoDefinitions()  {
        
        return getSupportedVideoDefinitions(nativePtr);
    }

    private native String getTopResourcesDir(long nativePtr);
    @Override
    synchronized public String getTopResourcesDir()  {
        
        return getTopResourcesDir(nativePtr);
    }

    private native void setTopResourcesDir(long nativePtr, String path);
    @Override
    synchronized public void setTopResourcesDir(String path)  {
        
        setTopResourcesDir(nativePtr, path);
    }

    private native Address createAddress(long nativePtr, String addr);
    @Override
    synchronized public Address createAddress(String addr)  {
        
        return (Address)createAddress(nativePtr, addr);
    }

    private native AuthInfo createAuthInfo(long nativePtr, String username, String userid, String passwd, String ha1, String realm, String domain);
    @Override
    synchronized public AuthInfo createAuthInfo(String username, String userid, String passwd, String ha1, String realm, String domain)  {
        
        return (AuthInfo)createAuthInfo(nativePtr, username, userid, passwd, ha1, realm, domain);
    }

    private native Buffer createBuffer(long nativePtr);
    @Override
    synchronized public Buffer createBuffer()  {
        
        return (Buffer)createBuffer(nativePtr);
    }

    private native Buffer createBufferFromData(long nativePtr, byte[] data, int size);
    @Override
    synchronized public Buffer createBufferFromData(byte[] data, int size)  {
        
        return (Buffer)createBufferFromData(nativePtr, data, size);
    }

    private native Buffer createBufferFromString(long nativePtr, String data);
    @Override
    synchronized public Buffer createBufferFromString(String data)  {
        
        return (Buffer)createBufferFromString(nativePtr, data);
    }

    private native Config createConfig(long nativePtr, String path);
    @Override
    synchronized public Config createConfig(String path)  {
        
        return (Config)createConfig(nativePtr, path);
    }

    private native Config createConfigFromString(long nativePtr, String data);
    @Override
    synchronized public Config createConfigFromString(String data)  {
        
        return (Config)createConfigFromString(nativePtr, data);
    }

    private native Config createConfigWithFactory(long nativePtr, String path, String factoryPath);
    @Override
    synchronized public Config createConfigWithFactory(String path, String factoryPath)  {
        
        return (Config)createConfigWithFactory(nativePtr, path, factoryPath);
    }

    private native Content createContent(long nativePtr);
    @Override
    synchronized public Content createContent()  {
        
        return (Content)createContent(nativePtr);
    }

    private native Core createCore(long nativePtr, String configPath, String factoryConfigPath, Object systemContext);
    @Override
    synchronized public Core createCore(String configPath, String factoryConfigPath, Object systemContext)  {
        
        return (Core)createCore(nativePtr, configPath, factoryConfigPath, systemContext);
    }

    private native Core createCoreWithConfig(long nativePtr, Config config, Object systemContext);
    @Override
    synchronized public Core createCoreWithConfig(Config config, Object systemContext)  {
        
        return (Core)createCoreWithConfig(nativePtr, config, systemContext);
    }

    private native ErrorInfo createErrorInfo(long nativePtr);
    @Override
    synchronized public ErrorInfo createErrorInfo()  {
        
        return (ErrorInfo)createErrorInfo(nativePtr);
    }

    private native Range createRange(long nativePtr);
    @Override
    synchronized public Range createRange()  {
        
        return (Range)createRange(nativePtr);
    }

    private native Transports createTransports(long nativePtr);
    @Override
    synchronized public Transports createTransports()  {
        
        return (Transports)createTransports(nativePtr);
    }

    private native TunnelConfig createTunnelConfig(long nativePtr);
    @Override
    synchronized public TunnelConfig createTunnelConfig()  {
        
        return (TunnelConfig)createTunnelConfig(nativePtr);
    }

    private native Vcard createVcard(long nativePtr);
    @Override
    synchronized public Vcard createVcard()  {
        
        return (Vcard)createVcard(nativePtr);
    }

    private native VideoActivationPolicy createVideoActivationPolicy(long nativePtr);
    @Override
    synchronized public VideoActivationPolicy createVideoActivationPolicy()  {
        
        return (VideoActivationPolicy)createVideoActivationPolicy(nativePtr);
    }

    private native VideoDefinition createVideoDefinition(long nativePtr, int width, int height);
    @Override
    synchronized public VideoDefinition createVideoDefinition(int width, int height)  {
        
        return (VideoDefinition)createVideoDefinition(nativePtr, width, height);
    }

    private native VideoDefinition createVideoDefinitionFromName(long nativePtr, String name);
    @Override
    synchronized public VideoDefinition createVideoDefinitionFromName(String name)  {
        
        return (VideoDefinition)createVideoDefinitionFromName(nativePtr, name);
    }

    private native void enableLogCollection(long nativePtr, int state);
    @Override
    synchronized public void enableLogCollection(LogCollectionState state)  {
        
        enableLogCollection(nativePtr, state.toInt());
    }


    @Override
    public void setUserData(Object data) {
        userData = data;
    }

    @Override
    public Object getUserData() {
        return userData;
    }
}
