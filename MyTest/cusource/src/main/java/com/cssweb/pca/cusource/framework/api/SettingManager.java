package cn.unicompay.wallet.client.framework.api;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import cn.unicompay.wallet.client.framework.WApplication;

public class SettingManager {
	/** The application context **/
	private WApplication context;
	/** The key name of http cookie value **/
	public static final String HTTP_COOKIE_KEY = "HTTP_COOKIE_KEY";
	/** The key name of last session time. **/
	public static final String LAST_SESSION_TIME_KEY = "LAST_SESSION_TIME_KEY";
	/** Store name **/
	private static final String STORE_NAME = "WALLET_SETTING";
	/** Read and write lock **/
	private static final ReadWriteLock RWLOCK = new ReentrantReadWriteLock(true);
	/** Read Lock **/
	private static final Lock READ_LOCK = RWLOCK.readLock();
	/** Write Lock **/
	private static final Lock WRITE_LOCK = RWLOCK.writeLock();

	/**
	 * Constructor
	 * 
	 * @param c
	 */
	public SettingManager(WApplication c) {
		this.context = c;
	}

	/**
	 * <pre>
	 * Loads saved settings.
	 * This method is thread-safe.
	 * </pre>
	 */
	public void load() {
		READ_LOCK.lock();
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			NetworkManager network = context.getNetworkManager();
			if (network != null) {
				network.setCookie(preferences.getString(HTTP_COOKIE_KEY, ""));
				network.setLastSessionTime(preferences.getLong(
						LAST_SESSION_TIME_KEY, 0));
			}
		} finally {
			READ_LOCK.unlock();
		}
	}

	/**
	 * <pre>
	 * Save the current settings.
	 * This method is thread-safe.
	 * </pre>
	 */
	public void save() {
		WRITE_LOCK.lock();
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			NetworkManager network = context.getNetworkManager();
			if (network != null) {
				preferences.edit()
						.putString(HTTP_COOKIE_KEY, network.getCookie())
						.commit();
				preferences
						.edit()
						.putLong(LAST_SESSION_TIME_KEY,
								network.getLastSessionTime()).commit();
			}
		} finally {
			WRITE_LOCK.unlock();
		}
	}

	/**
	 * Put a string value to the setting.
	 * 
	 * @param key
	 * @param value
	 */
	public void putString(String key, String value) {
		WRITE_LOCK.lock();
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			preferences.edit().putString(key, value).commit();
		} finally {
			WRITE_LOCK.unlock();
		}
	}

	/**
	 * Returns the string value from the current setting.
	 * 
	 * @param key
	 * @param defStr
	 * @return
	 */
	public String getString(String key, String defStr) {
		READ_LOCK.lock();
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			return (preferences.getString(key, defStr));
		} finally {
			READ_LOCK.unlock();
		}
	}

	/**
	 * Put a long value to the setting.
	 * 
	 * @param key
	 * @param value
	 */
	public void putLong(String key, long value) {
		WRITE_LOCK.lock();
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			preferences.edit().putLong(key, value).commit();
		} finally {
			WRITE_LOCK.unlock();
		}
	}

	/**
	 * Returns the long value from the current setting.
	 * 
	 * @param key
	 * @param defLng
	 * @return
	 */
	public long getLong(String key, long defLng) {
		READ_LOCK.lock();
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			return (preferences.getLong(key, defLng));
		} finally {
			READ_LOCK.unlock();
		}
	}

	/**
	 * Put a int value to the setting.
	 * 
	 * @param key
	 * @param value
	 */
	public void putInt(String key, int value) {
		WRITE_LOCK.lock();
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			preferences.edit().putInt(key, value).commit();
		} finally {
			WRITE_LOCK.unlock();
		}
	}

	/**
	 * Returns the int value from the current setting.
	 * 
	 * @param key
	 * @param defInt
	 * @return
	 */
	public int getInt(String key, int defInt) {
		READ_LOCK.lock();
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			return (preferences.getInt(key, defInt));
		} finally {
			READ_LOCK.unlock();
		}
	}

	/**
	 * Put a boolean value to the setting.
	 * 
	 * @param key
	 * @param value
	 */
	public void putBoolean(String key, boolean value) {
		WRITE_LOCK.lock();
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			preferences.edit().putBoolean(key, value).commit();

		} finally {
			WRITE_LOCK.unlock();
		}
	}

	/**
	 * Returns the boolean value from the current setting.
	 * 
	 * @param key
	 * @param defBool
	 * @return
	 */
	public boolean getBoolean(String key, boolean defBool) {
		READ_LOCK.lock();
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			return (preferences.getBoolean(key, defBool));
		} finally {
			READ_LOCK.unlock();
		}
	}

	/**
	 * Removes the value assigned with the key
	 * 
	 * @param key
	 */
	public void remove(String key) {
		WRITE_LOCK.lock();
		try {
			Log.d("SettingManager", "remove>>>>>" + key);
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			preferences.edit().remove(key).commit();

		} finally {
			WRITE_LOCK.unlock();
		}
	}

	/**
	 * Clears all values.
	 */
	void clear() {
		WRITE_LOCK.lock();
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					STORE_NAME, Context.MODE_PRIVATE);
			preferences.edit().clear().commit();

		} finally {
			WRITE_LOCK.unlock();
		}
	}

	/**
	 * @return
	 */
	public WApplication getContext() {
		return context;
	}
}
