package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.PushNotificationVO;


public class GetMyNotiRs extends ResultRs{

	/**
	 * 
	 */
	private Vector<PushNotificationVO> notiList = null;

	/**
	 * @return
	 */
	public Vector<PushNotificationVO> getNotiList() {
		return notiList;
	}

	/**
	 * @param notiList
	 */
	public void setNotiList(Vector<PushNotificationVO> notiList) {
		this.notiList = notiList;
	}
	
}
