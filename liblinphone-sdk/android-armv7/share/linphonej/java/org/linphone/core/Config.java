/*
Config.java
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


/**
  * The #LinphoneConfig object is used to manipulate a configuration file. 
  */
public interface Config {
    /**
      * Returns the list of sections' names in the LinphoneConfig. 
      */
    public String[] getSectionsNamesList();

    /**
      * Removes entries for key,value in a section. 
      */
    public void cleanEntry(String section, String key);

    /**
      * Removes every pair of key,value in a section and remove the section. 
      */
    public void cleanSection(String section);

    /**
      * Dumps the #LinphoneConfig as INI into a buffer. 
      */
    public String dump();

    /**
      * Dumps the #LinphoneConfig as XML into a buffer. 
      */
    public String dumpAsXml();

    /**
      * Retrieves a configuration item as a boolean, given its section, key, and
      * default value. 
      */
    public boolean getBool(String section, String key, boolean defaultValue);

    /**
      * Retrieves a default configuration item as a float, given its section, key, and
      * default value. 
      */
    public float getDefaultFloat(String section, String key, float defaultValue);

    /**
      * Retrieves a default configuration item as an integer, given its section, key,
      * and default value. 
      */
    public int getDefaultInt(String section, String key, int defaultValue);

    /**
      * Retrieves a default configuration item as a 64 bit integer, given its section,
      * key, and default value. 
      */
    public int getDefaultInt64(String section, String key, int defaultValue);

    /**
      * Retrieves a default configuration item as a string, given its section, key, and
      * default value. 
      */
    public String getDefaultString(String section, String key, String defaultValue);

    /**
      * Retrieves a configuration item as a float, given its section, key, and default
      * value. 
      */
    public float getFloat(String section, String key, float defaultValue);

    /**
      * Retrieves a configuration item as an integer, given its section, key, and
      * default value. 
      */
    public int getInt(String section, String key, int defaultValue);

    /**
      * Retrieves a configuration item as a 64 bit integer, given its section, key, and
      * default value. 
      */
    public int getInt64(String section, String key, int defaultValue);

    /**
      * Retrieves the overwrite flag for a config item. 
      */
    public boolean getOverwriteFlagForEntry(String section, String key);

    /**
      * Retrieves the overwrite flag for a config section. 
      */
    public boolean getOverwriteFlagForSection(String section);

    /**
      * Retrieves a section parameter item as a string, given its section and key. 
      */
    public String getSectionParamString(String section, String key, String defaultValue);

    /**
      * Retrieves the skip flag for a config item. 
      */
    public boolean getSkipFlagForEntry(String section, String key);

    /**
      * Retrieves the skip flag for a config section. 
      */
    public boolean getSkipFlagForSection(String section);

    /**
      * Retrieves a configuration item as a string, given its section, key, and default
      * value. 
      */
    public String getString(String section, String key, String defaultString);

    /**
      * Retrieves a configuration item as a list of strings, given its section, key,
      * and default value. 
      */
    public String[] getStringList(String section, String key, String[] defaultList);

    /**
      * Returns 1 if a given section with a given key is present in the configuration. 
      */
    public int hasEntry(String section, String key);

    /**
      * Returns 1 if a given section is present in the configuration. 
      */
    public int hasSection(String section);

    /**
      * Reads a xml config file and fill the #LinphoneConfig with the read config
      * dynamic values. 
      */
    public String loadFromXmlFile(String filename);

    /**
      * Reads a xml config string and fill the #LinphoneConfig with the read config
      * dynamic values. 
      */
    public void loadFromXmlString(String buffer);

    /**
      * Reads a user config file and fill the #LinphoneConfig with the read config
      * values. 
      */
    public void readFile(String filename);

    /**
      */
    public boolean relativeFileExists(String filename);

    /**
      * Sets a boolean config item. 
      */
    public void setBool(String section, String key, boolean value);

    /**
      * Sets a float config item. 
      */
    public void setFloat(String section, String key, float value);

    /**
      * Sets an integer config item. 
      */
    public void setInt(String section, String key, int value);

    /**
      * Sets a 64 bits integer config item. 
      */
    public void setInt64(String section, String key, int value);

    /**
      * Sets an integer config item, but store it as hexadecimal. 
      */
    public void setIntHex(String section, String key, int value);

    /**
      * Sets the overwrite flag for a config item (used when dumping config as xml) 
      */
    public void setOverwriteFlagForEntry(String section, String key, boolean value);

    /**
      * Sets the overwrite flag for a config section (used when dumping config as xml) 
      */
    public void setOverwriteFlagForSection(String section, boolean value);

    /**
      * Sets a range config item. 
      */
    public void setRange(String section, String key, int minValue, int maxValue);

    /**
      * Sets the skip flag for a config item (used when dumping config as xml) 
      */
    public void setSkipFlagForEntry(String section, String key, boolean value);

    /**
      * Sets the skip flag for a config section (used when dumping config as xml) 
      */
    public void setSkipFlagForSection(String section, boolean value);

    /**
      * Sets a string config item. 
      */
    public void setString(String section, String key, String value);

    /**
      * Sets a string list config item. 
      */
    public void setStringList(String section, String key, String[] value);

    /**
      * Writes the config file to disk. 
      */
    public void sync();

    /**
      * Write a string in a file placed relatively with the Linphone configuration
      * file. 
      */
    public void writeRelativeFile(String filename, String data);

    /**
      * Instantiates a #LinphoneConfig object from a user provided buffer. 
      */
    public Config newFromBuffer(String buffer);

    /**
      * Instantiates a #LinphoneConfig object from a user config file and a factory
      * config file. 
      */
    public Config newWithFactory(String configFilename, String factoryConfigFilename);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class ConfigImpl implements Config {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected ConfigImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String[] getSectionsNamesList(long nativePtr);
    @Override
    synchronized public String[] getSectionsNamesList()  {
        
        return getSectionsNamesList(nativePtr);
    }

    private native void cleanEntry(long nativePtr, String section, String key);
    @Override
    synchronized public void cleanEntry(String section, String key)  {
        
        cleanEntry(nativePtr, section, key);
    }

    private native void cleanSection(long nativePtr, String section);
    @Override
    synchronized public void cleanSection(String section)  {
        
        cleanSection(nativePtr, section);
    }

    private native String dump(long nativePtr);
    @Override
    synchronized public String dump()  {
        
        return dump(nativePtr);
    }

    private native String dumpAsXml(long nativePtr);
    @Override
    synchronized public String dumpAsXml()  {
        
        return dumpAsXml(nativePtr);
    }

    private native boolean getBool(long nativePtr, String section, String key, boolean defaultValue);
    @Override
    synchronized public boolean getBool(String section, String key, boolean defaultValue)  {
        
        return getBool(nativePtr, section, key, defaultValue);
    }

    private native float getDefaultFloat(long nativePtr, String section, String key, float defaultValue);
    @Override
    synchronized public float getDefaultFloat(String section, String key, float defaultValue)  {
        
        return getDefaultFloat(nativePtr, section, key, defaultValue);
    }

    private native int getDefaultInt(long nativePtr, String section, String key, int defaultValue);
    @Override
    synchronized public int getDefaultInt(String section, String key, int defaultValue)  {
        
        return getDefaultInt(nativePtr, section, key, defaultValue);
    }

    private native int getDefaultInt64(long nativePtr, String section, String key, int defaultValue);
    @Override
    synchronized public int getDefaultInt64(String section, String key, int defaultValue)  {
        
        return getDefaultInt64(nativePtr, section, key, defaultValue);
    }

    private native String getDefaultString(long nativePtr, String section, String key, String defaultValue);
    @Override
    synchronized public String getDefaultString(String section, String key, String defaultValue)  {
        
        return getDefaultString(nativePtr, section, key, defaultValue);
    }

    private native float getFloat(long nativePtr, String section, String key, float defaultValue);
    @Override
    synchronized public float getFloat(String section, String key, float defaultValue)  {
        
        return getFloat(nativePtr, section, key, defaultValue);
    }

    private native int getInt(long nativePtr, String section, String key, int defaultValue);
    @Override
    synchronized public int getInt(String section, String key, int defaultValue)  {
        
        return getInt(nativePtr, section, key, defaultValue);
    }

    private native int getInt64(long nativePtr, String section, String key, int defaultValue);
    @Override
    synchronized public int getInt64(String section, String key, int defaultValue)  {
        
        return getInt64(nativePtr, section, key, defaultValue);
    }

    private native boolean getOverwriteFlagForEntry(long nativePtr, String section, String key);
    @Override
    synchronized public boolean getOverwriteFlagForEntry(String section, String key)  {
        
        return getOverwriteFlagForEntry(nativePtr, section, key);
    }

    private native boolean getOverwriteFlagForSection(long nativePtr, String section);
    @Override
    synchronized public boolean getOverwriteFlagForSection(String section)  {
        
        return getOverwriteFlagForSection(nativePtr, section);
    }

    private native String getSectionParamString(long nativePtr, String section, String key, String defaultValue);
    @Override
    synchronized public String getSectionParamString(String section, String key, String defaultValue)  {
        
        return getSectionParamString(nativePtr, section, key, defaultValue);
    }

    private native boolean getSkipFlagForEntry(long nativePtr, String section, String key);
    @Override
    synchronized public boolean getSkipFlagForEntry(String section, String key)  {
        
        return getSkipFlagForEntry(nativePtr, section, key);
    }

    private native boolean getSkipFlagForSection(long nativePtr, String section);
    @Override
    synchronized public boolean getSkipFlagForSection(String section)  {
        
        return getSkipFlagForSection(nativePtr, section);
    }

    private native String getString(long nativePtr, String section, String key, String defaultString);
    @Override
    synchronized public String getString(String section, String key, String defaultString)  {
        
        return getString(nativePtr, section, key, defaultString);
    }

    private native String[] getStringList(long nativePtr, String section, String key, String[] defaultList);
    @Override
    synchronized public String[] getStringList(String section, String key, String[] defaultList)  {
        
        return getStringList(nativePtr, section, key, defaultList);
    }

    private native int hasEntry(long nativePtr, String section, String key);
    @Override
    synchronized public int hasEntry(String section, String key)  {
        
        return hasEntry(nativePtr, section, key);
    }

    private native int hasSection(long nativePtr, String section);
    @Override
    synchronized public int hasSection(String section)  {
        
        return hasSection(nativePtr, section);
    }

    private native String loadFromXmlFile(long nativePtr, String filename);
    @Override
    synchronized public String loadFromXmlFile(String filename)  {
        
        return loadFromXmlFile(nativePtr, filename);
    }

    private native int loadFromXmlString(long nativePtr, String buffer);
    @Override
    synchronized public void loadFromXmlString(String buffer)  {
        
        loadFromXmlString(nativePtr, buffer);
    }

    private native int readFile(long nativePtr, String filename);
    @Override
    synchronized public void readFile(String filename)  {
        
        readFile(nativePtr, filename);
    }

    private native boolean relativeFileExists(long nativePtr, String filename);
    @Override
    synchronized public boolean relativeFileExists(String filename)  {
        
        return relativeFileExists(nativePtr, filename);
    }

    private native void setBool(long nativePtr, String section, String key, boolean value);
    @Override
    synchronized public void setBool(String section, String key, boolean value)  {
        
        setBool(nativePtr, section, key, value);
    }

    private native void setFloat(long nativePtr, String section, String key, float value);
    @Override
    synchronized public void setFloat(String section, String key, float value)  {
        
        setFloat(nativePtr, section, key, value);
    }

    private native void setInt(long nativePtr, String section, String key, int value);
    @Override
    synchronized public void setInt(String section, String key, int value)  {
        
        setInt(nativePtr, section, key, value);
    }

    private native void setInt64(long nativePtr, String section, String key, int value);
    @Override
    synchronized public void setInt64(String section, String key, int value)  {
        
        setInt64(nativePtr, section, key, value);
    }

    private native void setIntHex(long nativePtr, String section, String key, int value);
    @Override
    synchronized public void setIntHex(String section, String key, int value)  {
        
        setIntHex(nativePtr, section, key, value);
    }

    private native void setOverwriteFlagForEntry(long nativePtr, String section, String key, boolean value);
    @Override
    synchronized public void setOverwriteFlagForEntry(String section, String key, boolean value)  {
        
        setOverwriteFlagForEntry(nativePtr, section, key, value);
    }

    private native void setOverwriteFlagForSection(long nativePtr, String section, boolean value);
    @Override
    synchronized public void setOverwriteFlagForSection(String section, boolean value)  {
        
        setOverwriteFlagForSection(nativePtr, section, value);
    }

    private native void setRange(long nativePtr, String section, String key, int minValue, int maxValue);
    @Override
    synchronized public void setRange(String section, String key, int minValue, int maxValue)  {
        
        setRange(nativePtr, section, key, minValue, maxValue);
    }

    private native void setSkipFlagForEntry(long nativePtr, String section, String key, boolean value);
    @Override
    synchronized public void setSkipFlagForEntry(String section, String key, boolean value)  {
        
        setSkipFlagForEntry(nativePtr, section, key, value);
    }

    private native void setSkipFlagForSection(long nativePtr, String section, boolean value);
    @Override
    synchronized public void setSkipFlagForSection(String section, boolean value)  {
        
        setSkipFlagForSection(nativePtr, section, value);
    }

    private native void setString(long nativePtr, String section, String key, String value);
    @Override
    synchronized public void setString(String section, String key, String value)  {
        
        setString(nativePtr, section, key, value);
    }

    private native void setStringList(long nativePtr, String section, String key, String[] value);
    @Override
    synchronized public void setStringList(String section, String key, String[] value)  {
        
        setStringList(nativePtr, section, key, value);
    }

    private native int sync(long nativePtr);
    @Override
    synchronized public void sync()  {
        
        sync(nativePtr);
    }

    private native void writeRelativeFile(long nativePtr, String filename, String data);
    @Override
    synchronized public void writeRelativeFile(String filename, String data)  {
        
        writeRelativeFile(nativePtr, filename, data);
    }

    private native Config newFromBuffer(long nativePtr, String buffer);
    @Override
    synchronized public Config newFromBuffer(String buffer)  {
        
        return (Config)newFromBuffer(nativePtr, buffer);
    }

    private native Config newWithFactory(long nativePtr, String configFilename, String factoryConfigFilename);
    @Override
    synchronized public Config newWithFactory(String configFilename, String factoryConfigFilename)  {
        
        return (Config)newWithFactory(nativePtr, configFilename, factoryConfigFilename);
    }

    private native void unref(long ptr);
    protected void finalize() throws Throwable {
		if (nativePtr != 0) {
			unref(nativePtr);
			nativePtr = 0;
		}
		super.finalize();
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
