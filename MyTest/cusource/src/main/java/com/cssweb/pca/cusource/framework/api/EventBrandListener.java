package cn.unicompay.wallet.client.framework.api;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.Brand;

public interface EventBrandListener extends NetworkListener {
	public void onError();

	public void onList(Vector<Brand> list);
}
