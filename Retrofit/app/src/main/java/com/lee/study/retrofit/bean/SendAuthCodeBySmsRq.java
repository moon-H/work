
package com.lee.study.retrofit.bean;


import com.lee.study.retrofit.base.Request;

public class SendAuthCodeBySmsRq extends Request {

	private String msisdn;
	private String walletId;
	private String seId;
	private String sign;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public String getSeId() {
		return seId;
	}

	public void setSeId(String seId) {
		this.seId = seId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Override
	public String toString() {
		return "SendAuthCodeBySmsRq{" + "msisdn='" + msisdn + '\'' + ", walletId='" + walletId + '\'' + ", seId='" + seId + '\'' + ", sign='" + sign + '\'' + '}';
	}
}
