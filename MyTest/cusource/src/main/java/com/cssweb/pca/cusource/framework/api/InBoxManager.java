package cn.unicompay.wallet.client.framework.api;

import java.util.List;
import java.util.Vector;

import cn.unicompay.wallet.client.framework.WApplication;
import cn.unicompay.wallet.client.framework.api.http.InBoxGateWay;
import cn.unicompay.wallet.client.framework.api.http.model.DeleteInBoxRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetInBoxListRq;
import cn.unicompay.wallet.client.framework.api.http.model.GetInBoxListRs;
import cn.unicompay.wallet.client.framework.api.http.model.Result;
import cn.unicompay.wallet.client.framework.api.http.model.ResultRs;
import cn.unicompay.wallet.client.framework.api.http.model.UpdateMsgInBoxRq;
import cn.unicompay.wallet.client.framework.db.MyInBoxDB;
import cn.unicompay.wallet.client.framework.model.InBoxMessage;
import cn.unicompay.wallet.client.framework.util.Util;

import com.skcc.wallet.core.http.exception.NoNetworkException;
import com.skcc.wallet.core.http.exception.NoResponseException;
import com.skcc.wallet.core.http.exception.ResNotOKException;

public class InBoxManager {
	/** Context **/
	private WApplication context;

	private MyInBoxDB database;

	/**
	 * @param context
	 */
	public InBoxManager(WApplication context) {
		this.context = context;
		database = new MyInBoxDB(context);
	}

	public void getInBoxByPage(final int maxNumber, final int startIndex,
			final InBoxListener l) {
		new Thread(new Runnable() {
			public void run() {
				final Vector<InBoxMessage> list = database.getInBoxByPage(
						maxNumber, startIndex);
				Util.makeLooperThread(new Runnable() {
					@Override
					public void run() {
						l.onList(list);
					}
				});

			}
		}).start();
	}

	/**
	 * Get my messages in box.
	 * 
	 * @param l
	 */
	public void listMyInBox(final InBoxListener l) {
		new Thread(new Runnable() {
			public void run() {
				final Vector<InBoxMessage> list = database.getMyInBoxMsgs();
				if (l != null) {
					Util.makeLooperThread(new Runnable() {
						public void run() {
							l.onList(list);
						}
					});
				}
			}
		}).start();
	}

	/**
	 * @param msgId
	 * @param l
	 */
	public void removeMsg(final String msgId, final InBoxListener l) {
		new Thread(new Runnable() {
			public void run() {
				InBoxGateWay gateway = context.getNetworkManager()
						.getInBoxGateWay();
				DeleteInBoxRq req = new DeleteInBoxRq();
				req.setMessageId(msgId);
				ResultRs res;
				try {
					res = gateway.deleteInboxMessage(req);
					if (l != null) {
						if (res == null
								|| res.getResult().getCode() != Result.OK) {
							Util.makeLooperThread(new Runnable() {
								public void run() {
									l.onError();
								}
							});
							return;
						} else {
							database.remove(msgId);
							listMyInBox(l);
						}
					}
				} catch (NoNetworkException e) {
					Util.makeLooperThread(new Runnable() {
						public void run() {
							l.onNoNetwork();
						}
					});
				} catch (NoResponseException e) {
					Util.makeLooperThread(new Runnable() {
						public void run() {
							l.onNoResponse();
						}
					});
				} catch (ResNotOKException e) {
					Util.makeLooperThread(new Runnable() {
						public void run() {
							l.onError();
						}
					});
					return;
				}
			}
		}).start();
	}

	/**
	 * Set the status of message in box to be read.
	 * 
	 * @param msgId
	 */
	public void readMsg(final List<InBoxMessage> list) {
		new Thread(new Runnable() {
			public void run() {
				for (InBoxMessage message : list) {
					message.setReadYn("Y");
					database.updateReadYN(message);
				}
				InBoxGateWay gateway = context.getNetworkManager()
						.getInBoxGateWay();
				UpdateMsgInBoxRq req = new UpdateMsgInBoxRq();
				req.setUpdateInboxMsgStatusList(list);
				try {
					ResultRs res = gateway.updateInboxMessageStatus(req);
					if (res != null && res.getResult().getCode() == Result.OK) {

					}
				} catch (NoNetworkException e) {
					e.printStackTrace();
				} catch (NoResponseException e) {
					e.printStackTrace();
				} catch (ResNotOKException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * @return
	 */
	public WApplication getContext() {
		return context;
	}

	/**
	 * @return the count of unread messages
	 */
	public int getUnreadMsgCnt() {
		return database.getCountUnReadInBox();
	}

	/**
	 * @return the count of messages
	 */
	public int getAllMsgCnt() {
		return database.getMsgCountInBox();
	}

	/**
	 * Get messages on line.
	 * 
	 * @throws ResNotOKException
	 */
	public void getMyInBoxMsg() throws NoNetworkException, NoResponseException,
			ResNotOKException {
		InBoxGateWay gateway = context.getNetworkManager().getInBoxGateWay();
		GetInBoxListRq req = new GetInBoxListRq(1, 100);

		GetInBoxListRs res;
		res = gateway.getInboxMessageList(req);
		if (res != null && res.getResult().getCode() == Result.OK) {
			Vector<InBoxMessage> remoteList = res.getInboxMessageList();
			database.removeAll();
			Vector<InBoxMessage> localList = database.getAllMyInBoxMsgs();
			if (remoteList != null) {
				boolean isSaved = false;
				for (InBoxMessage msg1 : remoteList) {
					isSaved = false;
					if (localList != null && localList.isEmpty() == false) {
						for (InBoxMessage msg2 : localList) {
							if (msg1.getMessageId().equals(msg2.getMessageId())) {
								isSaved = true;
								break;
							}
						}
					}
					if (isSaved == false) {
						database.insert(msg1);
					}
				}
			}
		} else {
			return;
		}
	}

	/**
	 * @return
	 */
	public MyInBoxDB getDatabase() {
		return database;
	}
}
