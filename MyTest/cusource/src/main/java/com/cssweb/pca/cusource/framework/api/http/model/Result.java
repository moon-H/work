package cn.unicompay.wallet.client.framework.api.http.model;

public class Result {
	public final static int OK = 0;
	/**
	 * 
	 */
	private int code = 0;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}
}
