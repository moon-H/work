package cn.unicompay.wallet.client.framework.model;

public class PINEvent {
	/**
	 * the type of event.
	 */
	private short evtType;
	/**
	 * the time when event happens
	 */
	private long evtTime;
	/**
	 * the object trigger event
	 */
	private String evtSrc;
	/**
	 * The CRS operation event.
	 */
	public final static short EVT_CRS_OPERATION = 5;
	/**
	 * The screen lock event.
	 */
	public final static short EVT_SCREEN_OFF = 4;
	/**
	 * The screen unlock event.
	 */
	public final static short EVT_SCREEN_ON  = 3;
	/**
	 * The PIN operation event.
	 */
	public final static short EVT_PIN_OPERTATION = 2;
	/**
	 * The successful PIN verification event.
	 */
	public final static short EVT_PIN_VERIFICATION = 1;
	
	/**
	 * @return
	 */
	public String getEvtSrc() {
		return evtSrc;
	}

	/**
	 * @param evtSrc
	 */
	public void setEvtSrc(String evtSrc) {
		this.evtSrc = evtSrc;
	}

	/**
	 * @return
	 */
	public short getEvtType() {
		return evtType;
	}
	
	/**
	 * @param evtType
	 */
	public void setEvtType(short evtType) {
		this.evtType = evtType;
	}
	
	/**
	 * @return
	 */
	public long getEvtTime() {
		return evtTime;
	}
	
	/**
	 * @param evtTime
	 */
	public void setEvtTime(long evtTime) {
		this.evtTime = evtTime;
	}
}
