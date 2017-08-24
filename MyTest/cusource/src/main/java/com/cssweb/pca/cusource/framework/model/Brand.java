package cn.unicompay.wallet.client.framework.model;

public class Brand {
	private String brandId;
	private String region;
	private String keyWord;
	private String brandImageUrl;

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getBrandImageUrl() {
		return brandImageUrl;
	}

	public void setBrandImageUrl(String brandImageUrl) {
		this.brandImageUrl = brandImageUrl;
	}

}
