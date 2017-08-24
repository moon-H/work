package cn.unicompay.wallet.client.framework.model;

import android.os.Parcel;
import android.os.Parcelable;

public class EventStore implements Parcelable {
	private String storeId;
	private String name;
	private String tel;
	private String address;;
	private String longitude;// 经度
	private String latitude;// 纬度

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeString(storeId);
		arg0.writeString(name);
		arg0.writeString(tel);
		arg0.writeString(address);
		arg0.writeString(longitude);
		arg0.writeString(latitude);
	}

	public static final Parcelable.Creator<EventStore> CREATOR = new Creator<EventStore>() {

		@Override
		public EventStore createFromParcel(Parcel source) {

			// TODO Auto-generated method stub
			EventStore store = new EventStore();
			store.storeId = source.readString();
			store.name = source.readString();
			store.tel = source.readString();
			store.address = source.readString();
			store.longitude = source.readString();
			store.latitude = source.readString();
			return store;

		}

		@Override
		public EventStore[] newArray(int size) {

			// TODO Auto-generated method stub
			return new EventStore[size];

		}

	};

}
