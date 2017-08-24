package cn.unicompay.wallet.client.framework.api.http.model;

import java.util.Vector;

import cn.unicompay.wallet.client.framework.model.Brand;

public class GetBrandListRs extends ResultRs {
	private Vector<Brand> allBrand;

	public Vector<Brand> getList() {
		return allBrand;
	}

	public void setList(Vector<Brand> list) {
		this.allBrand = list;
	}
}
