/*
AccountCreator.java
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
  * The #LinphoneAccountCreator object used to configure an account on a server via
  * XML-RPC. 
  */
public interface AccountCreator {
    enum Status {
        /**
        * Request status. 
        */
        RequestOk(0),

        /**
        * Request failed. 
        */
        RequestFailed(1),

        /**
        * Request failed due to missing argument(s) 
        */
        MissingArguments(2),

        /**
        * Request failed due to missing callback(s) 
        */
        MissingCallbacks(3),

        /**
        * Account status. 
        */
        AccountCreated(4),

        /**
        * Account not created. 
        */
        AccountNotCreated(5),

        /**
        * Account exist. 
        */
        AccountExist(6),

        /**
        * Account exist with alias. 
        */
        AccountExistWithAlias(7),

        /**
        * Account not exist. 
        */
        AccountNotExist(8),

        /**
        * Account was created with Alias. 
        */
        AliasIsAccount(9),

        /**
        * Alias exist. 
        */
        AliasExist(10),

        /**
        * Alias not exist. 
        */
        AliasNotExist(11),

        /**
        * Account activated. 
        */
        AccountActivated(12),

        /**
        * Account already activated. 
        */
        AccountAlreadyActivated(13),

        /**
        * Account not activated. 
        */
        AccountNotActivated(14),

        /**
        * Account linked. 
        */
        AccountLinked(15),

        /**
        * Account not linked. 
        */
        AccountNotLinked(16),

        /**
        * Server. 
        */
        ServerError(17);

        protected final int mValue;

        private Status (int value) {
            mValue = value;
        }

        static public Status fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return RequestOk;
            case 1: return RequestFailed;
            case 2: return MissingArguments;
            case 3: return MissingCallbacks;
            case 4: return AccountCreated;
            case 5: return AccountNotCreated;
            case 6: return AccountExist;
            case 7: return AccountExistWithAlias;
            case 8: return AccountNotExist;
            case 9: return AliasIsAccount;
            case 10: return AliasExist;
            case 11: return AliasNotExist;
            case 12: return AccountActivated;
            case 13: return AccountAlreadyActivated;
            case 14: return AccountNotActivated;
            case 15: return AccountLinked;
            case 16: return AccountNotLinked;
            case 17: return ServerError;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for Status");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum TransportStatus {
        /**
        * Transport ok. 
        */
        Ok(0),

        /**
        * Transport invalid. 
        */
        Unsupported(1);

        protected final int mValue;

        private TransportStatus (int value) {
            mValue = value;
        }

        static public TransportStatus fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Ok;
            case 1: return Unsupported;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for TransportStatus");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum DomainStatus {
        /**
        * Domain ok. 
        */
        Ok(0),

        /**
        * Domain invalid. 
        */
        Invalid(1);

        protected final int mValue;

        private DomainStatus (int value) {
            mValue = value;
        }

        static public DomainStatus fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Ok;
            case 1: return Invalid;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for DomainStatus");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum ActivationCodeStatus {
        /**
        * Activation code ok. 
        */
        Ok(0),

        /**
        * Activation code too short. 
        */
        TooShort(1),

        /**
        * Activation code too long. 
        */
        TooLong(2),

        /**
        * Contain invalid characters. 
        */
        InvalidCharacters(3);

        protected final int mValue;

        private ActivationCodeStatus (int value) {
            mValue = value;
        }

        static public ActivationCodeStatus fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Ok;
            case 1: return TooShort;
            case 2: return TooLong;
            case 3: return InvalidCharacters;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for ActivationCodeStatus");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum LanguageStatus {
        /**
        * Language ok. 
        */
        Ok(0);

        protected final int mValue;

        private LanguageStatus (int value) {
            mValue = value;
        }

        static public LanguageStatus fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Ok;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for LanguageStatus");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum PasswordStatus {
        /**
        * Password ok. 
        */
        Ok(0),

        /**
        * Password too short. 
        */
        TooShort(1),

        /**
        * Password too long. 
        */
        TooLong(2),

        /**
        * Contain invalid characters. 
        */
        InvalidCharacters(3),

        /**
        * Missing specific characters. 
        */
        MissingCharacters(4);

        protected final int mValue;

        private PasswordStatus (int value) {
            mValue = value;
        }

        static public PasswordStatus fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Ok;
            case 1: return TooShort;
            case 2: return TooLong;
            case 3: return InvalidCharacters;
            case 4: return MissingCharacters;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for PasswordStatus");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum EmailStatus {
        /**
        * Email ok. 
        */
        Ok(0),

        /**
        * Email malformed. 
        */
        Malformed(1),

        /**
        * Contain invalid characters. 
        */
        InvalidCharacters(2);

        protected final int mValue;

        private EmailStatus (int value) {
            mValue = value;
        }

        static public EmailStatus fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Ok;
            case 1: return Malformed;
            case 2: return InvalidCharacters;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for EmailStatus");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum UsernameStatus {
        /**
        * Username ok. 
        */
        Ok(0),

        /**
        * Username too short. 
        */
        TooShort(1),

        /**
        * Username too long. 
        */
        TooLong(2),

        /**
        * Contain invalid characters. 
        */
        InvalidCharacters(3),

        /**
        * Invalid username. 
        */
        Invalid(4);

        protected final int mValue;

        private UsernameStatus (int value) {
            mValue = value;
        }

        static public UsernameStatus fromInt(int value) throws RuntimeException {
            switch(value) {
            case 0: return Ok;
            case 1: return TooShort;
            case 2: return TooLong;
            case 3: return InvalidCharacters;
            case 4: return Invalid;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for UsernameStatus");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    enum PhoneNumberStatus {
        /**
        * Phone number ok. 
        */
        Ok(1),

        /**
        * Phone number too short. 
        */
        TooShort(2),

        /**
        * Phone number too long. 
        */
        TooLong(4),

        /**
        * Country code invalid. 
        */
        InvalidCountryCode(8),

        /**
        * Phone number invalid. 
        */
        Invalid(16);

        protected final int mValue;

        private PhoneNumberStatus (int value) {
            mValue = value;
        }

        static public PhoneNumberStatus fromInt(int value) throws RuntimeException {
            switch(value) {
            case 1: return Ok;
            case 2: return TooShort;
            case 4: return TooLong;
            case 8: return InvalidCountryCode;
            case 16: return Invalid;
            default:
                throw new RuntimeException("Unhandled enum value " + value + " for PhoneNumberStatus");
            }
        }

        public int toInt() {
            return mValue;
        }
    };

    /**
      * Get the activation code. 
      */
    public String getActivationCode();

    /**
      * Set the activation code. 
      */
    public AccountCreator.ActivationCodeStatus setActivationCode(String activationCode);

    /**
      * Get the display name. 
      */
    public String getDisplayName();

    /**
      * Set the display name. 
      */
    public AccountCreator.UsernameStatus setDisplayName(String displayName);

    /**
      * Get the domain. 
      */
    public String getDomain();

    /**
      * Set the domain. 
      */
    public AccountCreator.DomainStatus setDomain(String domain);

    /**
      * Get the email. 
      */
    public String getEmail();

    /**
      * Set the email. 
      */
    public AccountCreator.EmailStatus setEmail(String email);

    /**
      * Get the ha1. 
      */
    public String getHa1();

    /**
      * Set the ha1. 
      */
    public AccountCreator.PasswordStatus setHa1(String ha1);

    /**
      * Get the language use in email of SMS. 
      */
    public String getLanguage();

    /**
      * Set the language to use in email or SMS if supported. 
      */
    public AccountCreator.LanguageStatus setLanguage(String lang);

    /**
      * Get the password. 
      */
    public String getPassword();

    /**
      * Set the password. 
      */
    public AccountCreator.PasswordStatus setPassword(String password);

    /**
      * Get the RFC 3966 normalized phone number. 
      */
    public String getPhoneNumber();

    /**
      * Get the route. 
      */
    public String getRoute();

    /**
      * Set the route. 
      */
    public AccountCreator.Status setRoute(String route);

    /**
      * get Transport 
      */
    public TransportType getTransport();

    /**
      * Set Transport. 
      */
    public AccountCreator.TransportStatus setTransport(TransportType transport);

    /**
      * Get the username. 
      */
    public String getUsername();

    /**
      * Set the username. 
      */
    public AccountCreator.UsernameStatus setUsername(String username);

    /**
      * Send a request to activate an account on server. 
      */
    public AccountCreator.Status activateAccount();

    /**
      * Send a request to activate an alias. 
      */
    public AccountCreator.Status activateAlias();

    /**
      * Configure an account (create a proxy config and authentication info for it). 
      */
    public ProxyConfig configure();

    /**
      * Send a request to create an account on server. 
      */
    public AccountCreator.Status createAccount();

    /**
      * Create and configure a proxy config and a authentication info for an account
      * creator. 
      */
    public ProxyConfig createProxyConfig();

    /**
      * Send a request to know if an account is activated on server. 
      */
    public AccountCreator.Status isAccountActivated();

    /**
      * Send a request to know the existence of account on server. 
      */
    public AccountCreator.Status isAccountExist();

    /**
      * Send a request to know if an account is linked. 
      */
    public AccountCreator.Status isAccountLinked();

    /**
      * Send a request to know if an alias is used. 
      */
    public AccountCreator.Status isAliasUsed();

    /**
      * Send a request to link an account to an alias. 
      */
    public AccountCreator.Status linkAccount();

    /**
      * Send a request to recover an account. 
      */
    public AccountCreator.Status recoverAccount();

    /**
      * Reset the account creator entries like username, password, phone number... 
      */
    public void reset();

    /**
      * Set the phone number normalized. 
      */
    public int setPhoneNumber(String phoneNumber, String countryCode);

    /**
      * Send a request to update an account. 
      */
    public AccountCreator.Status updateAccount();

    public void setListener(AccountCreatorListener listener);

    /**
      * Sets the object to store in this object user's data
      */
    public void setUserData(Object data);

    /**
      * Gets the object stored in this object user's data
      */
    public Object getUserData();
}

class AccountCreatorImpl implements AccountCreator {

    protected long nativePtr = 0;
    protected Object userData = null;

    protected AccountCreatorImpl(long ptr) {
        nativePtr = ptr;
    }


    private native String getActivationCode(long nativePtr);
    @Override
    synchronized public String getActivationCode()  {
        
        return getActivationCode(nativePtr);
    }

    private native int setActivationCode(long nativePtr, String activationCode);
    @Override
    synchronized public AccountCreator.ActivationCodeStatus setActivationCode(String activationCode)  {
        
        return AccountCreator.ActivationCodeStatus.fromInt(setActivationCode(nativePtr, activationCode));
    }

    private native String getDisplayName(long nativePtr);
    @Override
    synchronized public String getDisplayName()  {
        
        return getDisplayName(nativePtr);
    }

    private native int setDisplayName(long nativePtr, String displayName);
    @Override
    synchronized public AccountCreator.UsernameStatus setDisplayName(String displayName)  {
        
        return AccountCreator.UsernameStatus.fromInt(setDisplayName(nativePtr, displayName));
    }

    private native String getDomain(long nativePtr);
    @Override
    synchronized public String getDomain()  {
        
        return getDomain(nativePtr);
    }

    private native int setDomain(long nativePtr, String domain);
    @Override
    synchronized public AccountCreator.DomainStatus setDomain(String domain)  {
        
        return AccountCreator.DomainStatus.fromInt(setDomain(nativePtr, domain));
    }

    private native String getEmail(long nativePtr);
    @Override
    synchronized public String getEmail()  {
        
        return getEmail(nativePtr);
    }

    private native int setEmail(long nativePtr, String email);
    @Override
    synchronized public AccountCreator.EmailStatus setEmail(String email)  {
        
        return AccountCreator.EmailStatus.fromInt(setEmail(nativePtr, email));
    }

    private native String getHa1(long nativePtr);
    @Override
    synchronized public String getHa1()  {
        
        return getHa1(nativePtr);
    }

    private native int setHa1(long nativePtr, String ha1);
    @Override
    synchronized public AccountCreator.PasswordStatus setHa1(String ha1)  {
        
        return AccountCreator.PasswordStatus.fromInt(setHa1(nativePtr, ha1));
    }

    private native String getLanguage(long nativePtr);
    @Override
    synchronized public String getLanguage()  {
        
        return getLanguage(nativePtr);
    }

    private native int setLanguage(long nativePtr, String lang);
    @Override
    synchronized public AccountCreator.LanguageStatus setLanguage(String lang)  {
        
        return AccountCreator.LanguageStatus.fromInt(setLanguage(nativePtr, lang));
    }

    private native String getPassword(long nativePtr);
    @Override
    synchronized public String getPassword()  {
        
        return getPassword(nativePtr);
    }

    private native int setPassword(long nativePtr, String password);
    @Override
    synchronized public AccountCreator.PasswordStatus setPassword(String password)  {
        
        return AccountCreator.PasswordStatus.fromInt(setPassword(nativePtr, password));
    }

    private native String getPhoneNumber(long nativePtr);
    @Override
    synchronized public String getPhoneNumber()  {
        
        return getPhoneNumber(nativePtr);
    }

    private native String getRoute(long nativePtr);
    @Override
    synchronized public String getRoute()  {
        
        return getRoute(nativePtr);
    }

    private native int setRoute(long nativePtr, String route);
    @Override
    synchronized public AccountCreator.Status setRoute(String route)  {
        
        return AccountCreator.Status.fromInt(setRoute(nativePtr, route));
    }

    private native int getTransport(long nativePtr);
    @Override
    synchronized public TransportType getTransport()  {
        
        return TransportType.fromInt(getTransport(nativePtr));
    }

    private native int setTransport(long nativePtr, int transport);
    @Override
    synchronized public AccountCreator.TransportStatus setTransport(TransportType transport)  {
        
        return AccountCreator.TransportStatus.fromInt(setTransport(nativePtr, transport.toInt()));
    }

    private native String getUsername(long nativePtr);
    @Override
    synchronized public String getUsername()  {
        
        return getUsername(nativePtr);
    }

    private native int setUsername(long nativePtr, String username);
    @Override
    synchronized public AccountCreator.UsernameStatus setUsername(String username)  {
        
        return AccountCreator.UsernameStatus.fromInt(setUsername(nativePtr, username));
    }

    private native int activateAccount(long nativePtr);
    @Override
    synchronized public AccountCreator.Status activateAccount()  {
        
        return AccountCreator.Status.fromInt(activateAccount(nativePtr));
    }

    private native int activateAlias(long nativePtr);
    @Override
    synchronized public AccountCreator.Status activateAlias()  {
        
        return AccountCreator.Status.fromInt(activateAlias(nativePtr));
    }

    private native ProxyConfig configure(long nativePtr);
    @Override
    synchronized public ProxyConfig configure()  {
        
        return (ProxyConfig)configure(nativePtr);
    }

    private native int createAccount(long nativePtr);
    @Override
    synchronized public AccountCreator.Status createAccount()  {
        
        return AccountCreator.Status.fromInt(createAccount(nativePtr));
    }

    private native ProxyConfig createProxyConfig(long nativePtr);
    @Override
    synchronized public ProxyConfig createProxyConfig()  {
        
        return (ProxyConfig)createProxyConfig(nativePtr);
    }

    private native int isAccountActivated(long nativePtr);
    @Override
    synchronized public AccountCreator.Status isAccountActivated()  {
        
        return AccountCreator.Status.fromInt(isAccountActivated(nativePtr));
    }

    private native int isAccountExist(long nativePtr);
    @Override
    synchronized public AccountCreator.Status isAccountExist()  {
        
        return AccountCreator.Status.fromInt(isAccountExist(nativePtr));
    }

    private native int isAccountLinked(long nativePtr);
    @Override
    synchronized public AccountCreator.Status isAccountLinked()  {
        
        return AccountCreator.Status.fromInt(isAccountLinked(nativePtr));
    }

    private native int isAliasUsed(long nativePtr);
    @Override
    synchronized public AccountCreator.Status isAliasUsed()  {
        
        return AccountCreator.Status.fromInt(isAliasUsed(nativePtr));
    }

    private native int linkAccount(long nativePtr);
    @Override
    synchronized public AccountCreator.Status linkAccount()  {
        
        return AccountCreator.Status.fromInt(linkAccount(nativePtr));
    }

    private native int recoverAccount(long nativePtr);
    @Override
    synchronized public AccountCreator.Status recoverAccount()  {
        
        return AccountCreator.Status.fromInt(recoverAccount(nativePtr));
    }

    private native void reset(long nativePtr);
    @Override
    synchronized public void reset()  {
        
        reset(nativePtr);
    }

    private native int setPhoneNumber(long nativePtr, String phoneNumber, String countryCode);
    @Override
    synchronized public int setPhoneNumber(String phoneNumber, String countryCode)  {
        
        return setPhoneNumber(nativePtr, phoneNumber, countryCode);
    }

    private native int updateAccount(long nativePtr);
    @Override
    synchronized public AccountCreator.Status updateAccount()  {
        
        return AccountCreator.Status.fromInt(updateAccount(nativePtr));
    }

    private native void setListener(long nativePtr, AccountCreatorListener listener);
    @Override
    synchronized public void setListener(AccountCreatorListener listener)  {
        
        setListener(nativePtr, listener);
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
