package cn.unicompay.wallet.client.framework.api.http.model;

public class VerifyLoginWalletClientRq {

	private ClientInfo clientInfo = null;
	private String CRV = null;
	private String cMAC = null;
	
	public ClientInfo getClientInfo() {
		return clientInfo;
	}
	public void setClientInfo(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}
	public String getCRV() {
		return CRV;
	}
	public void setCRV(String cRV) {
		CRV = cRV;
	}
	public String getcMAC() {
		return cMAC;
	}
	public void setcMAC(String cMAC) {
		this.cMAC = cMAC;
	}
	
}
