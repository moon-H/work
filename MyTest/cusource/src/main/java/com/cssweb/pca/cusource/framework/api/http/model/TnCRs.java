package cn.unicompay.wallet.client.framework.api.http.model;

public class TnCRs extends ResultRs{

	private String tnc = null;
	
	public String getTnc()
	{
		return tnc;
	}

	public void setTnc(String tnc)
	{
		this.tnc = tnc;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString() + "\n");
		sb.append("TnC::" + tnc + "\n");
		
		return sb.toString();
	}
}
