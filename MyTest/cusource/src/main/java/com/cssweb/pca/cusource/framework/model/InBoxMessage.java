package cn.unicompay.wallet.client.framework.model;

import android.os.Parcel;
import android.os.Parcelable;

public class InBoxMessage implements Parcelable {
	/**
	 * 
	 */
	/** normal status **/
	public final static int IN_BOX_STATUS = 0;
	/** gone to trash can **/
	public final static int TRASH_BOX_STATUS = 1;
	/**
	 * 
	 */
	private String messageId;
	/**
	 * 
	 */
	private String title;
	/**
	 * 
	 */
	private String inboxMessage;
	/**
	 * 
	 */
	private String cretDtim;
	/**
	 * 
	 */
	private String readYn;

	/**
	 * @return
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return
	 */
	public String getInboxMessage() {
		return inboxMessage;
	}

	/**
	 * @param inboxMessage
	 */
	public void setInboxMessage(String inboxMessage) {
		this.inboxMessage = inboxMessage;
	}

	/**
	 * @return
	 */
	public String getCretDtim() {
		return cretDtim;
	}

	/**
	 * @param cretDtim
	 */
	public void setCretDtim(String cretDtim) {
		this.cretDtim = cretDtim;
	}

	/**
	 * @return
	 */
	public String getReadYn() {
		return readYn;
	}

	/**
	 * @param readYn
	 */
	public void setReadYn(String readYn) {
		this.readYn = readYn;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(this.title);
		parcel.writeString(this.readYn);
		parcel.writeString(this.messageId);
		parcel.writeString(this.inboxMessage);
		parcel.writeString(this.cretDtim);

	}

	public static final Parcelable.Creator<InBoxMessage> CREATOR = new Parcelable.Creator<InBoxMessage>() {

		@Override
		public InBoxMessage createFromParcel(Parcel parcel) {
			InBoxMessage inBoxMessage = new InBoxMessage();
			inBoxMessage.title = parcel.readString();
			inBoxMessage.readYn = parcel.readString();
			inBoxMessage.messageId = parcel.readString();
			inBoxMessage.inboxMessage = parcel.readString();
			inBoxMessage.cretDtim = parcel.readString();
			return inBoxMessage;
		}

		@Override
		public InBoxMessage[] newArray(int i) {
			return new InBoxMessage[i];
		}
	};
}
