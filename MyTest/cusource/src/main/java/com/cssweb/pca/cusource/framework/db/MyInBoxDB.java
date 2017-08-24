package cn.unicompay.wallet.client.framework.db;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;
import cn.unicompay.wallet.client.framework.R;
import cn.unicompay.wallet.client.framework.model.InBoxMessage;

/**
 * database for Inbox.
 * 
 * @author hj21
 * 
 */
public class MyInBoxDB extends SQLiteOpenHelper {
	private static final String TAG = "MyInBoxDB";
	/** **/
	private static final int DATABASE_VERSION = 2;
	/** **/
	private static final String DB_NAME = "myInbox.db";
	/** **/
	private Context context;
	/** Read and write lock **/
	private static final ReentrantLock LOCK = new ReentrantLock();

	Uri baseuri = Uri.parse("content://cn.unicompay.wallet");

	/**
	 * Constructor
	 * 
	 * @param c
	 */
	public MyInBoxDB(Context c) {
		super(c, DB_NAME, null, DATABASE_VERSION);
		this.context = c;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createPushSql = context.getString(R.string.createInboxTable);
		db.execSQL(createPushSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		String dropPushSql = context.getString(R.string.dropInboxIf);
		db.execSQL(dropPushSql);
		onCreate(db);
	}

	/**
	 * Return a message in box by message id.
	 * 
	 * @param msgId
	 * @return
	 */
	public InBoxMessage getInBoxMsgById(String msgId) {
		InBoxMessage res = null;

		LOCK.lock();

		SQLiteDatabase mDB = null;
		Cursor c = null;

		try {
			mDB = getReadableDatabase();

			StringBuffer sql = new StringBuffer();
			String[] selectionArgs = new String[] { msgId };

			sql.append(context.getString(R.string.selectInbox)).append(' ')
					.append(context.getString(R.string.whereInboxId));

			Log.d(TAG, "getInBoxMsgById>> ");
			c = mDB.rawQuery(sql.toString(), selectionArgs);
			if (c.moveToNext()) {
				res = new InBoxMessage();
				res.setMessageId(c.getString(0));
				res.setTitle(c.getString(1));
				res.setInboxMessage(c.getString(2));
				res.setReadYn(c.getString(3));
				res.setCretDtim(c.getString(4));

				Log.d(TAG, "getInBoxMsgById>> title::" + res.getTitle()
						+ ", id::" + res.getMessageId());
			}
			c.close();
			mDB.close();
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

		return res;
	}

	/**
	 * Return messages in box according to their status.
	 * 
	 * @param status
	 * @return
	 * @see InBoxMessage#IN_BOX_STATUS
	 * @see InBoxMessage#TRASH_BOX_STATUS
	 */
	public Vector<InBoxMessage> getInBoxMsgsByStatus(int status) {
		Vector<InBoxMessage> res = new Vector<InBoxMessage>();

		LOCK.lock();

		SQLiteDatabase mDB = null;
		Cursor c = null;

		try {
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			String[] selectionArgs = new String[] { String.valueOf(status) };

			sql.append(context.getString(R.string.selectInbox)).append(' ')
					.append(context.getString(R.string.whereInboxStatus))
					.append(' ')
					.append(context.getString(R.string.orderByDate));

			Log.d(TAG, "getInBoxMsgsByStatus>> ::" + sql.toString());
			c = mDB.rawQuery(sql.toString(), selectionArgs);
			InBoxMessage msg;
			while (c.moveToNext()) {
				msg = new InBoxMessage();
				msg.setMessageId(c.getString(0));
				msg.setTitle(c.getString(1));
				msg.setInboxMessage(c.getString(2));
				msg.setReadYn(c.getString(3));
				msg.setCretDtim(c.getString(4));
				res.add(msg);
				Log.d(TAG, "getInBoxMsgsByStatus>> title::" + msg.getTitle()
						+ ", id::" + msg.getMessageId());
			}

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

		return res;
	}

	public Vector<InBoxMessage> getInBoxByPage(int maxNumber, int startIndex) {
		Vector<InBoxMessage> res = new Vector<InBoxMessage>();

		LOCK.lock();

		SQLiteDatabase mDB = null;
		Cursor c = null;

		try {
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.selectInbox)).append(' ')
					.append(context.getString(R.string.orderByReadYN))
					.append(' ').append(context.getString(R.string.limit));
			// "select msg_id, msg_title, msg_content, msg_read_yn, create_date from tblInbox limit ? offset ?"
			c = mDB.rawQuery(sql.toString(), new String[] { maxNumber + "",
					startIndex + "" });
			InBoxMessage msg;
			while (c.moveToNext()) {
				msg = new InBoxMessage();
				msg.setMessageId(c.getString(0));
				msg.setTitle(c.getString(1));
				msg.setInboxMessage(c.getString(2));
				msg.setReadYn(c.getString(3));
				msg.setCretDtim(c.getString(4));
				res.add(msg);

			}

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

		return res;
	}

	/**
	 * <pre>
	 * Get all messages in box.
	 * This method equals {@link #getInBoxMsgsByStatus()} with parameter {@link InBoxMessage#IN_BOX_STATUS}.
	 * </pre>
	 * 
	 * @return
	 */
	public Vector<InBoxMessage> getMyInBoxMsgs() {
		return getInBoxMsgsByStatus(InBoxMessage.IN_BOX_STATUS);
	}

	/**
	 * <pre>
	 * Get all saved messages in box.
	 * </pre>
	 * 
	 * @return
	 */
	public Vector<InBoxMessage> getAllMyInBoxMsgs() {
		Vector<InBoxMessage> res = new Vector<InBoxMessage>();

		LOCK.lock();

		SQLiteDatabase mDB = null;
		Cursor c = null;

		try {
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();

			sql.append(context.getString(R.string.selectInbox));

			Log.d(TAG, "getAllMyInBoxMsgs>> ::");
			c = mDB.rawQuery(sql.toString(), null);
			InBoxMessage msg;
			while (c.moveToNext()) {
				msg = new InBoxMessage();
				msg.setMessageId(c.getString(0));
				msg.setTitle(c.getString(1));
				msg.setInboxMessage(c.getString(2));
				msg.setReadYn(c.getString(3));
				msg.setCretDtim(c.getString(4));
				res.add(msg);
				Log.d(TAG, "getAllMyInBoxMsgs>> title::" + msg.getTitle()
						+ ", id::" + msg.getMessageId());
			}

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

		return res;
	}

	/**
	 * Get the count of message in box
	 * 
	 * */
	public int getMsgCountInBox() {
		int res = 0;
		LOCK.lock();
		SQLiteDatabase mDB = null;
		Cursor c = null;

		try {
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();

			sql.append(context.getString(R.string.selectInboxCnt));

			Log.d(TAG, "getCountMsgInBox>> ::");
			c = mDB.rawQuery(sql.toString(), null);
			if (c.moveToNext()) {
				res = c.getInt(0);
				Log.d(TAG, "getCountMsgInBox>> count::" + res);
			}

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

		return res;
	}

	/**
	 * Get the count of unread messages in box.
	 * 
	 * @return
	 */
	public int getCountUnReadInBox() {
		int res = 0;

		LOCK.lock();

		SQLiteDatabase mDB = null;
		Cursor c = null;

		try {
			mDB = getReadableDatabase();
			StringBuffer sql = new StringBuffer();
			String[] selectionArgs = new String[] { "N" };

			sql.append(context.getString(R.string.selectInboxCnt)).append(' ')
					.append(context.getString(R.string.whereInboxReadYN));

			Log.d(TAG, "getCountUnReadInBox>> ::");
			c = mDB.rawQuery(sql.toString(), selectionArgs);
			if (c.moveToNext()) {
				res = c.getInt(0);
				Log.d(TAG, "getCountUnReadInBox>> count::" + res);
			}

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

		return res;
	}

	/**
	 * Return the row ID from inserting a new message.
	 * 
	 * @param msg
	 * @return
	 */
	public long insert(InBoxMessage msg) {
		LOCK.lock();

		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;

		long rowNum = 0;
		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.insertInbox));

			stmt = mDB.compileStatement(sql.toString());
			bindValue(stmt, 1, msg.getMessageId());
			bindValue(stmt, 2, msg.getTitle());
			bindValue(stmt, 3, msg.getInboxMessage());
			bindValue(stmt, 4, msg.getReadYn().toUpperCase());
			bindValue(stmt, 5, msg.getCretDtim());

			rowNum = stmt.executeInsert();
			Log.d(TAG, "insert>> rowNum::" + rowNum);

			stmt.clearBindings();
			stmt.close();
			mDB.close();
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
		return rowNum;
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
	 * Return the count of row affected by removing the message by the msgId.
	 * 
	 * @param msgId
	 * @return
	 * @see #removeAll()
	 */
	public int remove(String msgId) {
		int rows = 0;
		LOCK.lock();

		SQLiteDatabase mDB = null;

		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.removeInbox)).append(' ')
					.append(context.getString(R.string.whereInboxId));

			rows = mDB.delete("tblInbox", "msg_id = ?", new String[] { msgId });
			Log.d(TAG, "remove>> rows::" + rows);

		} finally {
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}

			LOCK.unlock();
		}
		return rows;
	}

	/**
	 * Return the count of row affected by removing all the messages.
	 * 
	 * @return
	 * @see #remove(String)
	 */
	public int removeAll() {
		int rows = 0;
		LOCK.lock();

		SQLiteDatabase mDB = null;

		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.removeInbox));
			rows = mDB.delete("tblInbox", null, null);
			// mDB.delete(sql.toString(), null, null);
			Log.d(TAG, "removeAll>> rows::" + rows);

		} finally {
			if (mDB != null)
				try {
					mDB.close();
				} catch (Exception e) {
				}
			LOCK.unlock();
		}
		return rows;
	}

	/**
	 * Return the count of row affected by updating the message.
	 * 
	 * @param msg
	 * @return
	 */
	public int updateReadYN(InBoxMessage msg) {
		LOCK.lock();

		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;

		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.updateInboxReadYN))
					.append(' ')
					.append(context.getString(R.string.whereInboxId));

			context.getContentResolver().notifyChange(baseuri, null);

			stmt = mDB.compileStatement(sql.toString());
			bindValue(stmt, 1, msg.getReadYn());
			bindValue(stmt, 2, msg.getMessageId());

			stmt.execute();
			stmt.clearBindings();
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
		return 1;
	}

	/**
	 * Return the count of row affected by updating the messages' status to
	 * trash bin.
	 * 
	 * @param msg
	 * @return
	 */
	public int moveToTrash(InBoxMessage msg) {
		return updateStatus(msg, InBoxMessage.TRASH_BOX_STATUS);
	}

	/**
	 * Return the count of row affected by updating the messages' status to
	 * normal in box.
	 * 
	 * @param msg
	 * @return
	 */
	public int moveToInBox(InBoxMessage msg) {
		return updateStatus(msg, InBoxMessage.IN_BOX_STATUS);
	}

	/**
	 * @param msg
	 * @param status
	 * @return
	 */
	private int updateStatus(InBoxMessage msg, int status) {

		LOCK.lock();
		SQLiteDatabase mDB = null;
		SQLiteStatement stmt = null;

		try {
			mDB = getWritableDatabase();
			StringBuffer sql = new StringBuffer();
			sql.append(context.getString(R.string.updateInboxStatus))
					.append(' ')
					.append(context.getString(R.string.whereInboxId));

			Log.d(TAG, "updateStatus>> ::");
			stmt = mDB.compileStatement(sql.toString());
			stmt.bindLong(1, status);
			bindValue(stmt, 2, msg.getMessageId());

			stmt.execute();
			stmt.clearBindings();
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
		return 1;
	}
}
