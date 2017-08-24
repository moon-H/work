package cn.unicompay.wallet.client.framework.api.http.model;

public class RecordSpOrderRq {
	String customerId;
	String serviceId;
	String orderId;
	String writeCardResult;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getWriteCardResult() {
		return writeCardResult;
	}

	public void setWriteCardResult(String writeCardResult) {
		this.writeCardResult = writeCardResult;
	}

}
