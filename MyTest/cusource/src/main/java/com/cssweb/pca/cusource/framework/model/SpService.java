package cn.unicompay.wallet.client.framework.model;

public class SpService {

	// serviceType 1预置2非预置
	/** */
	public static final short NOT_INSTALLED_STATUS = 0x00;
	/** */
	public static final short INSTALLING_STATUS = 0x01;
	/** */
	public static final short INSTALL_READY_STATUS = 0x02;
	/** */
	public static final short ACCESS_READY_STATUS = 0x03;
	/** */
	public static final short AUTH_FAILED_STATUS = 0x04;
	public static final short P3DATA_EXPIRED_STATUS = 0x05;
	/** */
	public static final short INSTALLED_STATUS = 0x10;
	/** */
	public static final short DELETING_STATUS = 0x11;
	/** */
	public static final short DELETE_READY_STATUS = 0x12;
	/** */
	public static final short LOCKED_STATUS = 0xFF;
	/** */
	public static final short SE_TYPE = 0x01;
	/** */
	public static final short NON_SE_TYPE = 0x02;
	/** */
	public static final short MAIN_CARD_PRIORITY = 0x01;
	/** */
	public static final short VOLATILE_PRIORITY = 0x00;
	/** */
	public static final short NORMAL_PRIORITY = 100;
	/** */
	public static final short PRESET_SERVICE = 0x01;
	/** */
	public static final short NOT_PRESET_SERVICE = 0x02;
	/** */
	/** */
	public static final String SERVICE_CATOGRAY_PAMENTS = "PAYMENTS";
	/** */
	private String serviceId;//
	/** */
	private String serviceVersion;//
	/** */
	private String serviceName;//
	/** */
	private String appPackageName;//
	/** */
	private String appIconImageUrl;//
	/** */
	private String appDetailImageUrl;//
	/** */
	private String appMainImageUrl;//
	/** */
	private String serviceDesc;//
	/** */
	private String categoryName;
	/** */
	private String serviceCategoryId;//
	/** */
	private short appState;
	private short serviceState;
	/** */
	private short appletState;
	/** */
	private String updateDate;
	/** */
	private String spId;//
	/** */
	private String spName;//
	/** */
	private int usageCount;
	/** */
	private String uid;
	/** */
	private String appletAid;
	/** */
	private String spRegistrationUrl;//
	/** */
	private short serviceType;
	/** */
	private short priority = NORMAL_PRIORITY;
	private String registrationNeedYn;
	private String serviceAvailableYn;
	/**
	 * 
	 */
	private String appVersion;
	private short serviceSubscriptionState;// 对应的就是appletState
	/** */
	private String callCenterTel;
	/** */
	private String appDownloadUrl;
	/** */
	private String spDeviceAppUseYn;
	/** */
	private String serviceTmpltName;
	
	private String isNewService;
	
	private String serviceDesc2;

	public String getServiceDesc2() {
		return serviceDesc2;
	}

	public void setServiceDesc2(String serviceDesc2) {
		this.serviceDesc2 = serviceDesc2;
	}

	public String getIsNewService() {
		return isNewService;
	}

	public void setIsNewService(String isNewService) {
		this.isNewService = isNewService;
	}

	public String getCallCenterTel() {
		return callCenterTel;
	}

	public void setCallCenterTel(String callCenterTel) {
		this.callCenterTel = callCenterTel;
	}

	public short getServiceSubscriptionState() {
		return serviceSubscriptionState;
	}

	public void setServiceSubscriptionState(short serviceSubscriptionState) {
		this.serviceSubscriptionState = serviceSubscriptionState;
	}

	/**
	 * @return
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return
	 */
	public String getServiceVersion() {
		return serviceVersion;
	}

	/**
	 * @param serviceVersion
	 */
	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	/**
	 * @return
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return
	 */
	public short getAppState() {
		return appState;
	}

	/**
	 * @param appState
	 */
	public void setAppState(short appState) {
		this.appState = appState;
	}

	/**
	 * @return
	 */
	public short getAppletState() {
		return appletState;
	}

	/**
	 * @param appletState
	 */
	public void setAppletState(short appletState) {
		this.appletState = appletState;
	}

	/**
	 * @return
	 */
	public String getAppletAid() {
		return appletAid;
	}

	/**
	 * @param appletAid
	 */
	public void setAppletAid(String appletAid) {
		this.appletAid = appletAid;
	}

	/**
	 * 
	 * @return
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * 
	 * @param categoryName
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * 
	 * @return
	 */
	public String getUpdateDate() {
		return updateDate;
	}

	/**
	 * 
	 * @param updateDate
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 
	 * @return
	 */
	public String getSpId() {
		return spId;
	}

	/**
	 * 
	 * @param spId
	 */
	public void setSpId(String spId) {
		this.spId = spId;
	}

	/**
	 * 
	 * @return
	 */
	public String getSpName() {
		return spName;
	}

	/**
	 * 
	 * @param spName
	 */
	public void setSpName(String spName) {
		this.spName = spName;
	}

	/**
	 * 
	 * @return
	 */
	public int getUsageCount() {
		return usageCount;
	}

	/**
	 * 
	 * @param usageCount
	 */
	public void setUsageCount(int usageCount) {
		this.usageCount = usageCount;
	}

	/**
	 * 
	 * @return
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * 
	 * @param uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return
	 */
	public short getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 */
	public void setPriority(short priority) {
		this.priority = priority;
	}

	/**
	 * @return
	 */
	public String getAppPackageName() {
		return appPackageName;
	}

	/**
	 * @param appPackageName
	 */
	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}

	/**
	 * @return
	 */
	public String getAppIconImageUrl() {
		return appIconImageUrl;
	}

	/**
	 * @param appIconImageUrl
	 */
	public void setAppIconImageUrl(String appIconImageUrl) {
		this.appIconImageUrl = appIconImageUrl;
	}

	/**
	 * @return
	 */
	public String getAppDetailImageUrl() {
		return appDetailImageUrl;
	}

	/**
	 * @param appDetailImageUrl
	 */
	public void setAppDetailImageUrl(String appDetailImageUrl) {
		this.appDetailImageUrl = appDetailImageUrl;
	}

	/**
	 * @return
	 */
	public String getServiceDesc() {
		return serviceDesc;
	}

	/**
	 * @param serviceDesc
	 */
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	/**
	 * @return
	 */
	public String getServiceCategoryId() {
		return serviceCategoryId;
	}

	/**
	 * @param serviceCategoryId
	 */
	public void setServiceCategoryId(String serviceCategoryId) {
		this.serviceCategoryId = serviceCategoryId;
	}

	/**
	 * @return
	 */
	public short getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType
	 */
	public void setServiceType(short serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return
	 */
	public String getSpRegistrationUrl() {
		return spRegistrationUrl;
	}

	/**
	 * @param spRegistrationUrl
	 */
	public void setSpRegistrationUrl(String spRegistrationUrl) {
		this.spRegistrationUrl = spRegistrationUrl;
	}

	/**
	 * @return
	 */
	public String getRegistrationNeedYn() {
		return registrationNeedYn;
	}

	/**
	 * @param registrationNeedYn
	 */
	public void setRegistrationNeedYn(String registrationNeedYn) {
		this.registrationNeedYn = registrationNeedYn;
	}

	/**
	 * 
	 * @return
	 */
	public String getServiceAvailableYn() {
		return serviceAvailableYn;
	}

	/**
	 * 
	 * @param serviceAvailableYn
	 */
	public void setServiceAvailableYn(String serviceAvailableYn) {
		this.serviceAvailableYn = serviceAvailableYn;
	}

	/**
	 * @return
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * @param appVersion
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * @param appMainImageUrl
	 */
	public void setAppMainImageUrl(String appMainImageUrl) {
		this.appMainImageUrl = appMainImageUrl;
	}

	/**
	 * @param return
	 */
	public String getAppMainImageUrl() {
		return appMainImageUrl;
	}

	public short getServiceState() {
		return serviceState;
	}

	public void setServiceState(short serviceState) {
		this.serviceState = serviceState;
	}

	public String getAppDownloadUrl() {
		return appDownloadUrl;
	}

	public void setAppDownloadUrl(String appDownloadUrl) {
		this.appDownloadUrl = appDownloadUrl;
	}

	public String getSpDeviceAppUseYn() {
		return spDeviceAppUseYn;
	}

	public void setSpDeviceAppUseYn(String spDeviceAppUseYn) {
		this.spDeviceAppUseYn = spDeviceAppUseYn;
	}

	public String getServiceTmpltName() {
		return serviceTmpltName;
	}

	public void setServiceTmpltName(String serviceTmpltName) {
		this.serviceTmpltName = serviceTmpltName;
	}

	@Override
	public String toString() {
		return "SpService [serviceId=" + serviceId + ", serviceVersion="
				+ serviceVersion + ", serviceName=" + serviceName
				+ ", appPackageName=" + appPackageName + ", appIconImageUrl="
				+ appIconImageUrl + ", appDetailImageUrl=" + appDetailImageUrl
				+ ", appMainImageUrl=" + appMainImageUrl + ", serviceDesc="
				+ serviceDesc + ", categoryName=" + categoryName
				+ ", serviceCategoryId=" + serviceCategoryId + ", appState="
				+ appState + ", serviceState=" + serviceState
				+ ", appletState=" + appletState + ", updateDate=" + updateDate
				+ ", spId=" + spId + ", spName=" + spName + ", usageCount="
				+ usageCount + ", uid=" + uid + ", appletAid=" + appletAid
				+ ", spRegistrationUrl=" + spRegistrationUrl + ", serviceType="
				+ serviceType + ", priority=" + priority
				+ ", registrationNeedYn=" + registrationNeedYn
				+ ", serviceAvailableYn=" + serviceAvailableYn
				+ ", appVersion=" + appVersion + ", serviceSubscriptionState="
				+ serviceSubscriptionState + ", callCenterTel=" + callCenterTel
				+ ", appDownloadUrl=" + appDownloadUrl + ", spDeviceAppUseYn="
				+ spDeviceAppUseYn + ", serviceTmpltName=" + serviceTmpltName
				+ ", isNewService=" + isNewService
				+ "]";
	}
}
