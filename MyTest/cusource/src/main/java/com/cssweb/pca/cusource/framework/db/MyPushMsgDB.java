package cn.unicompay.wallet.client.framework.db;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;
import cn.unicompay.wallet.client.framework.R;
import cn.unicompay.wallet.client.framework.model.PushNotificationVO;

import com.google.gson.Gson;

/**
 * database for Push message.
 * @author hj21
 *
 */
public class MyPushMsgDB extends SQLiteOpenHelper {
	private static final String TAG = "MyPushMsgDB";
	/** **/
	private static final int DATABASE_VERSION = 4;
	/** **/
	private static final String DB_NAME= "myPushMsg.db";
	/** **/
	private Context context;
	/** Read and write lock**/
	private static final ReentrantLock LOCK = new ReentrantLock();
	
	/**
	 * Constructor
	 * @param c
	 */
	public MyPushMsgDB(Context c){
		super(c,DB_NAME,null,DATABASE_VERSION);
		this.context = c;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createPushSql = context.getString(R.string.createPushTable);
		db.execSQL(createPushSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		String dropPushSql = context.getString(R.string.dropPushIf);
		db.execSQL(dropPushSql);
		onCreate(db);
	}
	
	/**
	 * Return the push notice found by tid.
	 * @param tid
	 * @return
	 */
	public PushNotificationVO getPushNoticeById(String tid){
		PushNotificationVO notice = null;
		LOCK.lock();
		
		SQLiteDatabase mDB = null;
		Cursor c = null;

		try{
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			String[] selectionArgs = new String[]{tid};
			
			sql.append(context.getString(R.string.selectPush)).append(' ')
			.append(context.getString(R.string.wherePushId));
			
			Log.d(TAG, "getPushNoticeById>> ::");
			c = mDB.rawQuery(sql.toString(), selectionArgs);
			Gson gson = new Gson();
			String data;
			if(c.moveToNext()) {
				notice = new PushNotificationVO();
				notice.setTid(c.getString(0));
				notice.setEvent(c.getString(1));
				notice.setAps(c.getString(2));
				data = c.getString(3);
				if(TextUtils.isEmpty(data)==false)
					notice.setData(gson.fromJson(data, HashMap.class));
				notice.setStatus(c.getInt(4));
				Log.d(TAG, "getPushNoticeById>> tid::"+notice.getTid()+",event::"+notice.getEvent());
			}
		} finally {
			if( c!= null ) try{c.close();} catch(Exception e){}
			if( mDB != null ) try{mDB.close();} catch(Exception e){}

			LOCK.unlock();
		}
		return notice;
	}
	
	/**
	 * Return the push notices found by package name.
	 * @param pakcageName
	 * @return
	 */
	public Vector<PushNotificationVO> getPushNoticeByPackage(String pakcageName){
		Vector<PushNotificationVO> list = new Vector<PushNotificationVO>();
		
		LOCK.lock();
		
		SQLiteDatabase mDB = null;
		Cursor c = null;

		try{
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			String[] selectionArgs = new String[]{"%"+pakcageName+"%"};
			
			sql.append(context.getString(R.string.selectPush)).append(' ')
			.append(context.getString(R.string.wherePushPackage));
			
			Log.d(TAG, "getPushNoticeByPackage>> ::");
			c = mDB.rawQuery(sql.toString(), selectionArgs);
			PushNotificationVO notice;
			String data;
			Gson gson = new Gson();
			while(c.moveToNext()) {
				notice = new PushNotificationVO();
				notice.setTid(c.getString(0));
				notice.setEvent(c.getString(1));
				notice.setAps(c.getString(2));
				data = c.getString(3);
				if(TextUtils.isEmpty(data)==false)
					notice.setData(gson.fromJson(data, HashMap.class));
				notice.setStatus(c.getInt(4));
				list.add(notice);
				Log.d(TAG, "getPushNoticeByPackage>> tid::"+notice.getTid()+",event::"+notice.getEvent());
			}
		} finally {
			if( c!= null ) try{c.close();} catch(Exception e){}
			if( mDB != null ) try{mDB.close();} catch(Exception e){}

			LOCK.unlock();
		}
		return list;
	}
	
	/**
	 * Returns the push notices not handled yet.
	 * @return
	 */
	public Vector<PushNotificationVO> getUnhandledNotices() {
		Vector<PushNotificationVO> list = new Vector<PushNotificationVO>();
		
		LOCK.lock();
		
		SQLiteDatabase mDB = null;
		Cursor c = null;

		try{
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			
			sql.append(context.getString(R.string.selectPush)).append(' ')
			.append(context.getString(R.string.wherePushStatus));
			
			Log.d(TAG, "getUnhandledNotices>> ::");
			c = mDB.rawQuery(sql.toString(), null);
			PushNotificationVO notice;
			String data;
			Gson gson = new Gson();
			while(c.moveToNext()) {
				notice = new PushNotificationVO();
				notice.setTid(c.getString(0));
				notice.setEvent(c.getString(1));
				notice.setAps(c.getString(2));
				data = c.getString(3);
				if(TextUtils.isEmpty(data)==false)
					notice.setData(gson.fromJson(data, HashMap.class));
				notice.setStatus(c.getInt(4));
				list.add(notice);
				Log.d(TAG, "getUnhandledNotices>> tid::"+notice.getTid()+",event::"+notice.getEvent());
			}
		} finally {
			if( c!= null ) try{c.close();} catch(Exception e){}
			if( mDB != null ) try{mDB.close();} catch(Exception e){}

			LOCK.unlock();
		}
		return list;
	}
	
	/**
	 * Return the row ID from inserting a new push notice.
	 * @param notice
	 * @return
	 */
	public long insert(PushNotificationVO notice){
		long rowNum = 0;
		LOCK.lock();
		
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;

		try{
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.insertPush));
			
			Gson gson = new Gson();
			stmt = mDB.compileStatement(sql.toString());
			bindValue(stmt,1,notice.getTid());
			bindValue(stmt,2,notice.getEvent());
			bindValue(stmt,3,notice.getAps());
			Object data = notice.getData();
			if(data!=null) {
				bindValue(stmt,4,gson.toJson(notice.getData()));
			} else {
				stmt.bindNull(4);
			}
			stmt.bindLong(5, notice.getStatus());
			
			rowNum = stmt.executeInsert();
			Log.d(TAG, "insert>> rowNum::"+rowNum);
			
			stmt.clearBindings();
 
		} finally {
			if( stmt!= null ) try{stmt.close();} catch(Exception e){}
			if( mDB != null ) try{mDB.close();} catch(Exception e){}

			LOCK.unlock();
		}
		return rowNum;
	}
	
	/**
	 * Wrapper method
	 * @param stmt
	 * @param index
	 * @param v
	 */
	private void bindValue(SQLiteStatement stmt,int index,String v){
		if(v!=null) {
			stmt.bindString(index, v);
		} else {
			stmt.bindNull(index);
		}
	}
	
	/**
	 * Return the count of row affected by removing the push notice by the tid.
	 * @param tid
	 * @return
	 * @see #removeAll()
	 */
	public int remove(String tid){
		int rows = 0;
		LOCK.lock();
		
		SQLiteDatabase mDB = null;

		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.removePush)).append(' ')
			.append(context.getString(R.string.wherePushId));
			
			rows = mDB.delete("tblPushMsg", "noti_tid = ?", new String[]{tid});
			Log.d(TAG, "remove>> rows::"+rows);
			
		} finally {
			if( mDB != null ) try{mDB.close();} catch(Exception e){}

			LOCK.unlock();
		}
		return rows;
	}
	
	/**
	 * Return the count of row affected by removing all the push notices.
	 * @return
	 * @see #remove(String)
	 */
	public int removeAll(){
		int rows = 0;
		LOCK.lock();

		SQLiteDatabase mDB = null;
 
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			
			sql.append(context.getString(R.string.removePush));
			
			rows = mDB.delete("tblPushMsg", null, null);
			Log.d(TAG, "removeAll>> rows::"+rows);
 		} finally {
 			if( mDB != null ) try{mDB.close();} catch(Exception e){}

			LOCK.unlock();
		}
		return rows;
	}
	
	/**
	 * Return the count of row affected by updating the push notice.
	 * @param notice
	 * @return
	 */
	public int update(PushNotificationVO notice){
		LOCK.lock();
		
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;

		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.updatePushState)).append(' ')
			.append(context.getString(R.string.wherePushId));
			
			stmt = mDB.compileStatement(sql.toString());
			stmt.bindLong(1, notice.getStatus());
			bindValue(stmt,2,notice.getTid());
			Log.d(TAG, "update>> ::");
			
			stmt.execute();
			stmt.clearBindings();
		} finally {
			if( stmt!= null ) try{stmt.close();} catch(Exception e){}
			if( mDB != null ) try{mDB.close();} catch(Exception e){}

			LOCK.unlock();
		}
		return 1;
	}
}
