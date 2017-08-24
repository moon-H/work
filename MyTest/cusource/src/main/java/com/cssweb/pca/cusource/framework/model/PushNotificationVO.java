package cn.unicompay.wallet.client.framework.model;

import java.util.HashMap;

public class PushNotificationVO {
	public static final int PUSH_INIT_STATUS = 0;
	public static final int PUSH_RCV_STATUS = 1;
	public static final int PUSH_RCV_CONFIRM_STATUS = 2;
	/**
	 * 
	 */
	private String tid;
	/**
	 * 
	 */
	private String event;
	/**
	 * 
	 */
	private String aps;
	/**
	 * 
	 */
	private HashMap<Object, Object> data;

	private int status;

	/**
	 * @return
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

	/**
	 * @return
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return
	 */
	public String getAps() {
		return aps;
	}

	/**
	 * @param aps
	 */
	public void setAps(String aps) {
		this.aps = aps;
	}

	/**
	 * @return
	 */
	public HashMap getData() {
		return data;
	}

	/**
	 * @param data
	 */
	public void setData(HashMap data) {
		this.data = data;
	}

	/**
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	static class Aps {
		/**
		 * 
		 */
		private String alert;

		/**
		 * @return
		 */
		public String getAlert() {
			return alert;
		}

		/**
		 * @param alert
		 */
		public void setAlert(String alert) {
			this.alert = alert;
		}
	}
}
