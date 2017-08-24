package cn.unicompay.wallet.client.framework.db;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;
import cn.unicompay.wallet.client.framework.R;
import cn.unicompay.wallet.client.framework.model.Category;
import cn.unicompay.wallet.client.framework.model.SP;
import cn.unicompay.wallet.client.framework.model.SpService;

public class MySpServiceDB extends SQLiteOpenHelper {

	private static final String TAG = "MySpServiceDB";
	/** **/
	private Context context;
	/** **/
	private static final int DATABASE_VERSION = 11;//20140117
	/** **/
	private static final String DB_NAME = "mySpApp.db";
	/** **/
	public static final byte SORT_BY_NAME = 1;
	/** **/
	public static final byte SORT_BY_USAGE_COUNT = 2;
	/** **/
	public static final byte SORT_BY_DEFAULT = 3;
	/** search by uid **/
	public static final short SEARCH_BY_UID = 13;
	/** search by package name **/
	public static final short SEARCH_BY_PACKAGE_NAME = 12;
	/** search by application id **/
	public static final short SEARCH_BY_ID = 11;
	/** keyword for all category **/
	public static final String CATEGORY_ALL_ID = "ALL";
	public static final String CATEGORY_PAYMENTS = "PAYMENTS";
	/** Read and write lock **/
	private static final ReentrantLock LOCK = new ReentrantLock();

	/**
	 * Constructor
	 * 
	 * @param c
	 */
	public MySpServiceDB(Context c) {
		super(c, DB_NAME, null, DATABASE_VERSION);
		this.context = c;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "CreateDB for Service.");
		String createCategorySql = context
				.getString(R.string.createCategoryTable);
		String createSPAppSql = context.getString(R.string.createSPAppTable);
		String createSpSql = context.getString(R.string.createSpTable);
		db.execSQL(createCategorySql);
		db.execSQL(createSPAppSql);
		db.execSQL(createSpSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		Log.d(TAG, "onUpgrade for Service.");
		String dropCategorySql = context.getString(R.string.dropCategoryIf);
		String dropSPAppSql = context.getString(R.string.dropSPAppIf);
		String dropSpSql = context.getString(R.string.dropSpIf);
		db.execSQL(dropSPAppSql);
		db.execSQL(dropCategorySql);
		db.execSQL(dropSpSql);
		onCreate(db);
	}

	// uniquie()
	/**
	 * Return the SP application list.
	 * 
	 * @param categoryId
	 * @param sortBy
	 * @return
	 */
	public Vector<SpService> getSpAppList(String categoryId, byte sortBy) {
		Log.d(TAG, "getSpAppList>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		Cursor c = null;
		Vector<SpService> list = new Vector<SpService>();
		try {
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			String[] selectionArgs = null;
			sql.append(context.getString(R.string.selectSPApp)).append(' ')
					.append(context.getString(R.string.fromAllTable))
					.append(' ').append(context.getString(R.string.whereSPApp));
			if (categoryId.equals(CATEGORY_ALL_ID) == false) {
				sql.append(' ').append(
						context.getString(R.string.whereSPCategory));
				selectionArgs = new String[1];
				selectionArgs[0] = categoryId;
			}
			if (sortBy == SORT_BY_NAME) {
				sql.append(' ').append(
						context.getString(R.string.orderByAppName));
			} else if (sortBy == SORT_BY_USAGE_COUNT) {
				sql.append(' ').append(
						context.getString(R.string.orderByUsageCnt));
			} else {
				sql.append(' ').append(
						context.getString(R.string.orderByDefault));
			}
			c = mDB.rawQuery(sql.toString(), selectionArgs);
			SpService app;
			// app_id, app_ver, app_name, package, icon_img, detail_img,
			// description, tblMySpApp.category_id, category_name, app_state,
			// applet_state, update_date, tblSP.sp_id, sp_name,
			// need_registration, reg_url, cnt_of_usage, uid, aid, app_type
			while (c.moveToNext()) {
				app = new SpService();
				app.setServiceId(c.getString(0));
				app.setServiceVersion(c.getString(1));
				app.setServiceName(c.getString(2));
				app.setAppPackageName(c.getString(3));
				app.setAppIconImageUrl(c.getString(4));
				app.setAppMainImageUrl(c.getString(5));
				app.setAppDetailImageUrl(c.getString(6));
				app.setServiceDesc(c.getString(7));
				app.setCallCenterTel(c.getString(8));
				app.setServiceCategoryId(c.getString(9));
				app.setCategoryName(c.getString(10));
				app.setAppState(c.getShort(11));
				app.setAppletState(c.getShort(12));
				app.setUpdateDate(c.getString(13));
				app.setSpId(c.getString(14));
				app.setSpName(c.getString(15));
				app.setRegistrationNeedYn(c.getString(16));
				app.setSpRegistrationUrl(c.getString(17));
				app.setUsageCount(c.getInt(18));
				app.setUid(c.getString(19));
				app.setAppletAid(c.getString(20));
				app.setServiceType(c.getShort(21));
				app.setPriority(c.getShort(22));
				app.setAppVersion(c.getString(23));
				app.setAppDownloadUrl(c.getString(24));
				app.setSpDeviceAppUseYn(c.getString(25));
				app.setServiceTmpltName(c.getString(26));
				app.setServiceSubscriptionState(c.getShort(27));
				app.setIsNewService(c.getString(28));
				app.setServiceDesc2(c.getString(29));
				list.add(app);
				Log.d(TAG, "getSpAppList>> serviceId::" + app.getServiceId()
						+ ",serviceName::" + app.getServiceName());
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		} finally {
			if (c != null)
				try {
					c.close();
					c = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (mDB != null)
				try {
					mDB.close();
					mDB = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			LOCK.unlock();
		}
	}

	// uniquie()
	/**
	 * Return the SP application list.
	 * 
	 * @param categoryId
	 * @param sortBy
	 * @return
	 */
	public Vector<SpService> getSpAppList2(String categoryId, byte sortBy) {
		Log.d(TAG, "getSpAppList>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		Cursor c = null;
		try {
			Vector<SpService> list = new Vector<SpService>();
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			String[] selectionArgs = null;
			sql.append(context.getString(R.string.selectSPApp)).append(' ')
					.append(context.getString(R.string.fromAllTable))
					.append(' ').append(context.getString(R.string.whereSPApp));
			if (categoryId.equals(CATEGORY_ALL_ID) == false) {
				sql.append(' ').append(
						context.getString(R.string.whereSPCategory));
				selectionArgs = new String[1];
				selectionArgs[0] = categoryId;
			}
			if (sortBy == SORT_BY_NAME) {
				sql.append(' ').append(
						context.getString(R.string.orderByAppName));
			} else if (sortBy == SORT_BY_USAGE_COUNT) {
				sql.append(' ').append(
						context.getString(R.string.orderByUsageCnt));
			} else {
				sql.append(' ').append(
						context.getString(R.string.orderByDefault));
			}
			c = mDB.rawQuery(sql.toString(), selectionArgs);
			SpService app;
			// app_id, app_ver, app_name, package, icon_img, detail_img,
			// description, tblMySpApp.category_id, category_name, app_state,
			// applet_state, update_date, tblSP.sp_id, sp_name,
			// need_registration, reg_url, cnt_of_usage, uid, aid, app_type
			while (c.moveToNext()) {
				app = new SpService();
				app.setServiceId(c.getString(0));
				app.setServiceVersion(c.getString(1));
				app.setServiceName(c.getString(2));
				app.setAppPackageName(c.getString(3));
				app.setAppIconImageUrl(c.getString(4));
				app.setAppMainImageUrl(c.getString(5));
				app.setAppDetailImageUrl(c.getString(6));
				app.setServiceDesc(c.getString(7));
				app.setCallCenterTel(c.getString(8));
				app.setServiceCategoryId(c.getString(9));
				app.setCategoryName(c.getString(10));
				app.setAppState(c.getShort(11));
				app.setAppletState(c.getShort(12));
				app.setUpdateDate(c.getString(13));
				app.setSpId(c.getString(14));
				app.setSpName(c.getString(15));
				app.setRegistrationNeedYn(c.getString(16));
				app.setSpRegistrationUrl(c.getString(17));
				app.setUsageCount(c.getInt(18));
				app.setUid(c.getString(19));
				app.setAppletAid(c.getString(20));
				app.setServiceType(c.getShort(21));
				app.setPriority(c.getShort(22));
				app.setAppVersion(c.getString(23));
				app.setAppDownloadUrl(c.getString(24));
				app.setSpDeviceAppUseYn(c.getString(25));
				app.setServiceTmpltName(c.getString(26));
				app.setServiceSubscriptionState(c.getShort(27));
				app.setIsNewService(c.getString(28));
				app.setServiceDesc2(c.getString(29));
				list.add(app);
				Log.d(TAG, "getSpAppList>> serviceId::" + app.getServiceId()
						+ ",serviceName::" + app.getServiceName());
			}
			return list;
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			LOCK.unlock();
		}
	}

	/**
	 * Return the SP application found by application ID.
	 * 
	 * @param serviceId
	 * @return
	 */
	public SpService getSpAppById(String serviceId) {
		Log.d(TAG, "getSpAppById>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		Cursor c = null;
		try {
			SpService app = null;
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			String[] selectionArgs = new String[] { serviceId };
			sql.append(context.getString(R.string.selectSPApp)).append(' ')
					.append(context.getString(R.string.fromAllTable))
					.append(' ').append(context.getString(R.string.whereSPApp))
					.append(' ')
					.append(context.getString(R.string.whereSPAppId));
			c = mDB.rawQuery(sql.toString(), selectionArgs);
			if (c.moveToNext()) {
				app = new SpService();
				app.setServiceId(c.getString(0));
				app.setServiceVersion(c.getString(1));
				app.setServiceName(c.getString(2));
				app.setAppPackageName(c.getString(3));
				app.setAppIconImageUrl(c.getString(4));
				app.setAppMainImageUrl(c.getString(5));
				app.setAppDetailImageUrl(c.getString(6));
				app.setServiceDesc(c.getString(7));
				app.setCallCenterTel(c.getString(8));
				app.setServiceCategoryId(c.getString(9));
				app.setCategoryName(c.getString(10));
				app.setAppState(c.getShort(11));
				app.setAppletState(c.getShort(12));
				app.setUpdateDate(c.getString(13));
				app.setSpId(c.getString(14));
				app.setSpName(c.getString(15));
				app.setRegistrationNeedYn(c.getString(16));
				app.setSpRegistrationUrl(c.getString(17));
				app.setUsageCount(c.getInt(18));
				app.setUid(c.getString(19));
				app.setAppletAid(c.getString(20));
				app.setServiceType(c.getShort(21));
				app.setPriority(c.getShort(22));
				app.setAppVersion(c.getString(23));
				app.setAppDownloadUrl(c.getString(24));
				app.setSpDeviceAppUseYn(c.getString(25));
				app.setServiceTmpltName(c.getString(26));
				app.setServiceSubscriptionState(c.getShort(27));
				app.setIsNewService(c.getString(28));
				app.setServiceDesc2(c.getString(29));
				Log.d(TAG, "getSpAppById>> serviceId::" + app.getServiceId()
						+ ",serviceName::" + app.getServiceName());
			}
			return app;
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Return the SP application found by uid.
	 * 
	 * @param uid
	 * @return
	 */
	public SpService getSpAppByUid(String uid) {
		Log.d(TAG, "getSpAppByUid>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		Cursor c = null;
		try {
			SpService app = null;
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			String[] selectionArgs = new String[] { uid };
			sql.append(context.getString(R.string.selectSPApp)).append(' ')
					.append(context.getString(R.string.fromAllTable))
					.append(' ').append(context.getString(R.string.whereSPApp))
					.append(' ').append(context.getString(R.string.whereSpUid));
			c = mDB.rawQuery(sql.toString(), selectionArgs);
			if (c.moveToNext()) {
				app = new SpService();
				app.setServiceId(c.getString(0));
				app.setServiceVersion(c.getString(1));
				app.setServiceName(c.getString(2));
				app.setAppPackageName(c.getString(3));
				app.setAppIconImageUrl(c.getString(4));
				app.setAppMainImageUrl(c.getString(5));
				app.setAppDetailImageUrl(c.getString(6));
				app.setServiceDesc(c.getString(7));
				app.setCallCenterTel(c.getString(8));
				app.setServiceCategoryId(c.getString(9));
				app.setCategoryName(c.getString(10));
				app.setAppState(c.getShort(11));
				app.setAppletState(c.getShort(12));
				app.setUpdateDate(c.getString(13));
				app.setSpId(c.getString(14));
				app.setSpName(c.getString(15));
				app.setRegistrationNeedYn(c.getString(16));
				app.setSpRegistrationUrl(c.getString(17));
				app.setUsageCount(c.getInt(18));
				app.setUid(c.getString(19));
				app.setAppletAid(c.getString(20));
				app.setServiceType(c.getShort(21));
				app.setPriority(c.getShort(22));
				app.setAppVersion(c.getString(23));
				app.setAppDownloadUrl(c.getString(24));
				app.setSpDeviceAppUseYn(c.getString(25));
				app.setServiceTmpltName(c.getString(26));
				app.setServiceSubscriptionState(c.getShort(27));
				app.setIsNewService(c.getString(28));
				app.setServiceDesc2(c.getString(29));
				Log.d(TAG, "getSpAppByUid>> serviceId::" + app.getServiceId()
						+ ",serviceName::" + app.getServiceName());
			}
			return app;
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Return the SP application found by package name.
	 * 
	 * @param packageName
	 * @return
	 */
	public SpService getSpAppByPackage(String packageName) {
		Log.d(TAG, "getSpAppByPackage>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		Cursor c = null;
		try {
			SpService app = null;
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			String[] selectionArgs = new String[] { packageName };
			sql.append(context.getString(R.string.selectSPApp)).append(' ')
					.append(context.getString(R.string.fromAllTable))
					.append(' ').append(context.getString(R.string.whereSPApp))
					.append(' ')
					.append(context.getString(R.string.whereSpPackage));
			// Log.d("MySpServiceDB", sql.toString()+","+packageName);
			c = mDB.rawQuery(sql.toString(), selectionArgs);
			if (c.moveToNext()) {
				// Log.d("MySpServiceDB", "getSpAppByPackage");
				app = new SpService();
				app.setServiceId(c.getString(0));
				app.setServiceVersion(c.getString(1));
				app.setServiceName(c.getString(2));
				app.setAppPackageName(c.getString(3));
				app.setAppIconImageUrl(c.getString(4));
				app.setAppMainImageUrl(c.getString(5));
				app.setAppDetailImageUrl(c.getString(6));
				app.setServiceDesc(c.getString(7));
				app.setCallCenterTel(c.getString(8));
				app.setServiceCategoryId(c.getString(9));
				app.setCategoryName(c.getString(10));
				app.setAppState(c.getShort(11));
				app.setAppletState(c.getShort(12));
				app.setUpdateDate(c.getString(13));
				app.setSpId(c.getString(14));
				app.setSpName(c.getString(15));
				app.setRegistrationNeedYn(c.getString(16));
				app.setSpRegistrationUrl(c.getString(17));
				app.setUsageCount(c.getInt(18));
				app.setUid(c.getString(19));
				app.setAppletAid(c.getString(20));
				app.setServiceType(c.getShort(21));
				app.setPriority(c.getShort(22));
				app.setAppVersion(c.getString(23));
				app.setAppDownloadUrl(c.getString(24));
				app.setSpDeviceAppUseYn(c.getString(25));
				app.setServiceTmpltName(c.getString(26));
				app.setServiceSubscriptionState(c.getShort(27));
				app.setIsNewService(c.getString(28));
				app.setServiceDesc2(c.getString(29));
				Log.d(TAG,
						"getSpAppByPackage>> serviceId::" + app.getServiceId()
								+ ",serviceName::" + app.getServiceName());
			}
			return app;
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Return the SP application found by applet ID.
	 * 
	 * @param aid
	 * @return
	 */
	public SpService getSpAppByApplet(String aid) {
		Log.d(TAG, "getSpAppByApplet>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		Cursor c = null;
		try {
			SpService app = null;
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			String[] selectionArgs = new String[] { aid };
			sql.append(context.getString(R.string.selectSPApp)).append(' ')
					.append(context.getString(R.string.fromAllTable))
					.append(' ').append(context.getString(R.string.whereSPApp))
					.append(' ')
					.append(context.getString(R.string.whereAppletId));
			c = mDB.rawQuery(sql.toString(), selectionArgs);
			if (c.moveToNext()) {
				app = new SpService();
				app.setServiceId(c.getString(0));
				app.setServiceVersion(c.getString(1));
				app.setServiceName(c.getString(2));
				app.setAppPackageName(c.getString(3));
				app.setAppIconImageUrl(c.getString(4));
				app.setAppMainImageUrl(c.getString(5));
				app.setAppDetailImageUrl(c.getString(6));
				app.setServiceDesc(c.getString(7));
				app.setCallCenterTel(c.getString(8));
				app.setServiceCategoryId(c.getString(9));
				app.setCategoryName(c.getString(10));
				app.setAppState(c.getShort(11));
				app.setAppletState(c.getShort(12));
				app.setUpdateDate(c.getString(13));
				app.setSpId(c.getString(14));
				app.setSpName(c.getString(15));
				app.setRegistrationNeedYn(c.getString(16));
				app.setSpRegistrationUrl(c.getString(17));
				app.setUsageCount(c.getInt(18));
				app.setUid(c.getString(19));
				app.setAppletAid(c.getString(20));
				app.setServiceType(c.getShort(21));
				app.setPriority(c.getShort(22));
				app.setAppVersion(c.getString(23));
				app.setAppDownloadUrl(c.getString(24));
				app.setSpDeviceAppUseYn(c.getString(25));
				app.setServiceTmpltName(c.getString(26));
				app.setServiceSubscriptionState(c.getShort(27));
				app.setIsNewService(c.getString(28));
				app.setServiceDesc2(c.getString(29));
				Log.d(TAG,
						"getSpAppByApplet>> serviceId::" + app.getServiceId()
								+ ",serviceName::" + app.getServiceName());
			}
			return app;
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Get current category list.
	 * 
	 * @return
	 * @see cn.unicompay.wallet.client.framework.model.Category
	 */
	public Vector<Category> getCategory() {
		Log.d(TAG, "getCategory>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		Cursor c = null;
		try {
			Vector<Category> list = new Vector<Category>();
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.selectCategory));
			c = mDB.rawQuery(sql.toString(), null);
			Category category;
			while (c.moveToNext()) {
				category = new Category();
				category.setCategoryId(c.getString(0));
				category.setCategoryName(c.getString(1));
				list.add(category);
				Log.d(TAG, "getCategory>> id::" + category.getCategoryId()
						+ ",name:" + category.getCategoryName());
			}
			return list;
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Get current SP list
	 * 
	 * @return
	 * @see cn.unicompay.wallet.client.framework.model.SP
	 */
	public Vector<SP> getSP() {
		Log.d(TAG, "getSP>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		Cursor c = null;
		try {
			Vector<SP> list = new Vector<SP>();
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.selectSP));
			c = mDB.rawQuery(sql.toString(), null);
			SP sp;
			while (c.moveToNext()) {
				sp = new SP();
				sp.setSpId(c.getString(0));
				sp.setSpName(c.getString(1));
				list.add(sp);
				Log.d(TAG,
						"getSP>> id::" + sp.getSpId() + ",name::"
								+ sp.getSpName());
			}
			return list;
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Return the row ID from inserting a new SP application.
	 * 
	 * @param app
	 * @return
	 */
	public long insert(SpService app) {
		Log.d(TAG, "insert>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.insertSPApp));
			// service_id, service_ver, service_name, package, icon_img,
			// detail_img, description, category_id, app_state,
			// applet_state, sp_id, need_registration, reg_url, uid, aid,
			// app_type, priority, app_ver)
			// VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
			stmt = mDB.compileStatement(sql.toString());
			bindValue(stmt, 1, app.getServiceId());
			bindValue(stmt, 2, app.getServiceVersion());
			bindValue(stmt, 3, app.getServiceName());
			bindValue(stmt, 4, app.getAppPackageName());
			bindValue(stmt, 5, app.getAppIconImageUrl());
			bindValue(stmt, 6, app.getAppMainImageUrl());
			bindValue(stmt, 7, app.getAppDetailImageUrl());
			bindValue(stmt, 8, app.getServiceDesc());
			bindValue(stmt, 9, app.getCallCenterTel());
			bindValue(stmt, 10, app.getServiceCategoryId());
			stmt.bindLong(11, app.getAppState());
			stmt.bindLong(12, app.getAppletState());
			bindValue(stmt, 13, app.getSpId());
			bindValue(stmt, 14, app.getRegistrationNeedYn());
			bindValue(stmt, 15, app.getSpRegistrationUrl());
			bindValue(stmt, 16, app.getUid());
			bindValue(stmt, 17, app.getAppletAid());
			stmt.bindLong(18, app.getServiceType());
			stmt.bindLong(19, app.getPriority());
			bindValue(stmt, 20, app.getAppVersion());
			bindValue(stmt, 21, app.getAppDownloadUrl());
			bindValue(stmt, 22, app.getSpDeviceAppUseYn());
			bindValue(stmt, 23, app.getServiceTmpltName());
			stmt.bindLong(24, app.getServiceSubscriptionState());
			bindValue(stmt, 25, app.getIsNewService());
			bindValue(stmt, 26, app.getServiceDesc2());
			long rowNum = stmt.executeInsert();
			Log.d(TAG, "insert>> rowNum::" + rowNum);
			stmt.clearBindings();
			return rowNum;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Wrapper method
	 * 
	 * @param stmt
	 * @param index
	 * @param v
	 */
	private void bindValue(SQLiteStatement stmt, int index, String v) {
		if (v != null) {
			stmt.bindString(index, v);
		} else {
			stmt.bindNull(index);
		}
	}

	/**
	 * Return the count of row affected by removing the SP application.
	 * 
	 * @param app
	 * @return
	 * @see #remove(String)
	 * @see #removeAll()
	 */
	public int remove(SpService app) {
		return remove(app.getServiceId());
	}

	/**
	 * Return the count of row affected by removing the SP application by the
	 * application ID.
	 * 
	 * @param serviceId
	 * @return
	 * @see #remove(SpService)
	 * @see #removeAll()
	 */
	public int remove(String serviceId) {
		LOCK.lock();
		SQLiteDatabase mDB = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.removeSPApp)).append(' ')
					.append(context.getString(R.string.simpleWhereId));
			// is there better idea ?
			// I want to separate SQL statements and source code...
			int rows = mDB.delete("tblMySpApp", "service_id = ?",
					new String[] { serviceId });
			Log.d(TAG, "remove>> rows::" + rows);
			return rows;
		} finally {
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Return the count of row affected by removing all the SP applications.
	 * 
	 * @return
	 * @see #remove(SpService)
	 * @see #remove(String)
	 */
	public int removeAll() {
		LOCK.lock();
		SQLiteDatabase mDB = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.removeSPApp));
			// is there better idea ?
			// I want to separate SQL statements and source code...
			int rows = mDB.delete("tblMySpApp", null, null);
			Log.d(TAG, "removeAll>> rows::" + rows);
			return rows;
		} finally {
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Return the count of row affected by updating the SP application.
	 * 
	 * @param app
	 * @return
	 */
	public int update(SpService app) {
		if (app == null || TextUtils.isEmpty(app.getServiceId()))
			return 0;
		LOCK.lock();
		SQLiteDatabase mDB = null;
		try {
			// is there better idea ?
			// I want to separate SQL statements and source code...
			ContentValues values = new ContentValues();
			if (TextUtils.isEmpty(app.getServiceName()) == false)
				values.put("service_name", app.getServiceName());
			if (TextUtils.isEmpty(app.getServiceVersion()) == false)
				values.put("service_ver", app.getServiceVersion());
			if (TextUtils.isEmpty(app.getServiceDesc()) == false)
				values.put("description", app.getServiceDesc());
			if (TextUtils.isEmpty(app.getCallCenterTel()) == false)
				values.put("callcenter", app.getCallCenterTel());
			if (TextUtils.isEmpty(app.getAppDetailImageUrl()) == false)
				values.put("detail_img", app.getAppDetailImageUrl());
			if (TextUtils.isEmpty(app.getAppIconImageUrl()) == false)
				values.put("icon_img", app.getAppIconImageUrl());
			if (TextUtils.isEmpty(app.getAppMainImageUrl()) == false)
				values.put("main_img", app.getAppMainImageUrl());
			if (TextUtils.isEmpty(app.getSpRegistrationUrl()) == false)
				values.put("reg_url", app.getSpRegistrationUrl());
			if (TextUtils.isEmpty(app.getUid()) == false)
				values.put("uid", app.getUid());
			if (TextUtils.isEmpty(app.getAppVersion()) == false) {
				values.put("app_ver", app.getAppVersion());
			}
			if (TextUtils.isEmpty(app.getAppDownloadUrl()) == false) {
				values.put("app_down_url", app.getAppDownloadUrl());
			}
			if (TextUtils.isEmpty(app.getSpDeviceAppUseYn()) == false) {
				values.put("sp_device_app_use", app.getSpDeviceAppUseYn());
			}
			if (TextUtils.isEmpty(app.getServiceTmpltName()) == false) {
				values.put("service_tmplt_name", app.getServiceTmpltName());
			}
			values.put("applet_state", app.getAppletState());
			values.put("service_sub_state", app.getServiceSubscriptionState());
			values.put("is_new_service", app.getIsNewService());
			values.put("description_2",  app.getServiceDesc2());
			
			// values.putNull("update_date");
			mDB = getWritableDatabase();
			int rows = mDB.update("tblMySpApp", values, "service_id = ?",
					new String[] { app.getServiceId() });
			Log.d(TAG, "update>> rows::" + rows);
			return rows;
		} finally {
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Increase the usage count and return the count of row affected by
	 * updating.
	 * 
	 * @param appId
	 * @return
	 */
	public int increaseUsgCnt(String appId) {
		Log.d(TAG, "increaseUsgCnt>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.increaseUseCnt)).append(' ')
					.append(context.getString(R.string.simpleWhereId));
			stmt = mDB.compileStatement(sql.toString());
			bindValue(stmt, 1, appId);
			stmt.execute();
			stmt.clearBindings();
			return 1;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Update the status of application and return the count of rows affected
	 * by.
	 * 
	 * @param app
	 * @return
	 */
	public int updateAppState(SpService app) {
		Log.d(TAG, "updateAppState>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.updateAppState)).append(' ')
					.append(context.getString(R.string.simpleWhereId));
			// UPDATE tblMySpApp SET app_state = ? service_id = ?
			stmt = mDB.compileStatement(sql.toString());
			stmt.bindLong(1, app.getAppState()); // need to check status code
			bindValue(stmt, 2, app.getServiceId());
			stmt.execute();
			stmt.clearBindings();
			return 1;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Update the status of applet and return the count of rows affected by.
	 * 
	 * @param app
	 * @return
	 */
	public int updateAppletState(SpService app) {
		Log.d(TAG, "updateAppletState>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.updateAppletState))
					.append(' ')
					.append(context.getString(R.string.simpleWhereId));
			stmt = mDB.compileStatement(sql.toString());
			stmt.bindLong(1, app.getAppletState()); // need to check status code
			stmt.bindLong(2, app.getServiceSubscriptionState());
			bindValue(stmt, 3, app.getServiceId());
			stmt.execute();
			stmt.clearBindings();
			return 1;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Update the type of application and return the count of rows affected by.
	 * 
	 * @param app
	 * @return
	 */
	public int updateSpType(SpService app) {
		Log.d(TAG, "updateSpType>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.updateSpAppType)).append(' ')
					.append(context.getString(R.string.simpleWhereId));
			stmt = mDB.compileStatement(sql.toString());
			stmt.bindLong(1, app.getServiceType()); // need to check type code
			bindValue(stmt, 2, app.getServiceId());
			stmt.execute();
			stmt.clearBindings();
			return 1;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Update the priority of applet application and return the count of rows
	 * affected by.
	 * 
	 * @param app
	 * @return
	 */
	public int updateSpPriority(SpService app) {
		Log.d(TAG, "updateSpPriority>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.updateSpAppPriority))
					.append(' ')
					.append(context.getString(R.string.simpleWhereId));
			stmt = mDB.compileStatement(sql.toString());
			stmt.bindLong(1, app.getPriority());
			bindValue(stmt, 2, app.getServiceId());
			stmt.execute();
			stmt.clearBindings();
			return 1;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Update the app version of Sp service.
	 * 
	 * @param app
	 * @return
	 */
	public int updateSpAppVer(SpService app) {
		Log.d(TAG, "updateSpAppVer>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.updateSpAppVersion))
					.append(' ')
					.append(context.getString(R.string.simpleWhereId));
			stmt = mDB.compileStatement(sql.toString());
			bindValue(stmt, 1, app.getAppVersion());
			bindValue(stmt, 2, app.getServiceId());
			stmt.execute();
			stmt.clearBindings();
			return 1;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Insert a category row into table.
	 * 
	 * @param category
	 * @return
	 */
	public long insertCategory(Category category) {
		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.insertCategory));
			stmt = mDB.compileStatement(sql.toString());
			bindValue(stmt, 1, category.getCategoryId());
			bindValue(stmt, 2, category.getCategoryName());
			long rowNum = stmt.executeInsert();
			Log.d(TAG, "insertCategory>> rowNum::" + rowNum);
			stmt.clearBindings();
			return rowNum;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Update category name.
	 * 
	 * @param category
	 * @return
	 */
	public int updateCaegory(Category category) {
		Log.d(TAG, "updateCaegory>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.updateCategory));
			stmt = mDB.compileStatement(sql.toString());
			bindValue(stmt, 1, category.getCategoryName());
			bindValue(stmt, 2, category.getCategoryId());
			stmt.execute();
			stmt.clearBindings();
			return 1;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Insert a SP row into table.
	 * 
	 * @param sp
	 * @return
	 */
	public long insertSP(SP sp) {
		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.insertSP));
			// INSERT OR IGNORE INTO tblSP
			// (sp_id, sp_name, sp_imgUrl) VALUES(?,?,?)
			stmt = mDB.compileStatement(sql.toString());
			bindValue(stmt, 1, sp.getSpId());
			bindValue(stmt, 2, sp.getSpName());
			long rowNum = stmt.executeInsert();
			Log.d(TAG, "insertSP>> rowNum::" + rowNum);
			stmt.clearBindings();
			return rowNum;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}

	/**
	 * Update SP name and image.
	 * 
	 * @param sp
	 * @return
	 */
	public int updateSP(SP sp) {
		Log.d(TAG, "updateSP>> ::");
		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.updateSP));
			stmt = mDB.compileStatement(sql.toString());
			bindValue(stmt, 1, sp.getSpName());
			bindValue(stmt, 2, sp.getSpId());
			stmt.execute();
			stmt.clearBindings();
			return 1;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
	}
}
