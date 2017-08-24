package cn.unicompay.wallet.client.framework.api;

import java.util.List;

import cn.unicompay.wallet.client.framework.api.http.model.DeviceAppVersion;
import cn.unicompay.wallet.client.framework.api.http.model.PageInfo;

public interface GetWalletVersionHistoryListener extends NetworkListener {
	public void onError(int errorCode, String errorMsg);

	public void onResult(List<DeviceAppVersion> list,PageInfo pageInfo);
}
