package socket.pad.common;

/**
 * Created by liwx on 2016/3/16.
 */
public class TicketTransaction extends Response {
    private int TxnType;
    private int TxnResultCode;
    private String TxnDetailCode;
    private int CardPhyType;
    private String CardIssuerId;
    private String CardSN;
    private short ProductTypeId;
    private int ProductCategory;
    private String ProductExpireDate;
    private int ProductClass;
    private int PassengerType;
    private int LanguageId;
    private int ProductRemainmingValue;
    private int TransactionValue;
    private int TransactionDiscountValue;
    private int TicketUsedCount;

    public int getTicketUsedCount() {
        return TicketUsedCount;
    }

    public void setTicketUsedCount(int ticketUsedCount) {
        TicketUsedCount = ticketUsedCount;
    }

    public String getProductExpireDate() {
        return ProductExpireDate;
    }

    public void setProductExpireDate(String productExpireDate) {
        ProductExpireDate = productExpireDate;
    }

    public int getTxnType() {
        return TxnType;
    }

    public void setTxnType(int txnType) {
        TxnType = txnType;
    }

    public int getTxnResultCode() {
        return TxnResultCode;
    }

    public void setTxnResultCode(int txnResultCode) {
        TxnResultCode = txnResultCode;
    }

    public String getTxnDetailCode() {
        return TxnDetailCode;
    }

    public void setTxnDetailCode(String txnDetailCode) {
        TxnDetailCode = txnDetailCode;
    }

    public int getCardPhyType() {
        return CardPhyType;
    }

    public void setCardPhyType(int cardPhyType) {
        CardPhyType = cardPhyType;
    }

    public String getCardIssuerId() {
        return CardIssuerId;
    }

    public void setCardIssuerId(String cardIssuerId) {
        CardIssuerId = cardIssuerId;
    }

    public String getCardSN() {
        return CardSN;
    }

    public void setCardSN(String cardSN) {
        CardSN = cardSN;
    }


    public int getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(int productCategory) {
        ProductCategory = productCategory;
    }

    public int getProductClass() {
        return ProductClass;
    }

    public void setProductClass(int productClass) {
        ProductClass = productClass;
    }

    public int getLanguageId() {
        return LanguageId;
    }

    public void setLanguageId(int languageId) {
        LanguageId = languageId;
    }

    public int getProductRemainmingValue() {
        return ProductRemainmingValue;
    }

    public void setProductRemainmingValue(int productRemainmingValue) {
        ProductRemainmingValue = productRemainmingValue;
    }

    public int getTransactionValue() {
        return TransactionValue;
    }

    public void setTransactionValue(int transactionValue) {
        TransactionValue = transactionValue;
    }

    public int getTransactionDiscountValue() {
        return TransactionDiscountValue;
    }

    public void setTransactionDiscountValue(int transactionDiscountValue) {
        TransactionDiscountValue = transactionDiscountValue;
    }

    public int getPassengerType() {
        return PassengerType;
    }

    public void setPassengerType(int passengerType) {
        PassengerType = passengerType;
    }

    public short getProductTypeId() {
        return ProductTypeId;
    }

    public void setProductTypeId(short productTypeId) {
        ProductTypeId = productTypeId;
    }

    @Override
    public String toString() {
        return "{" +
            "TxnType=" + TxnType +
            ", TxnResultCode=" + TxnResultCode +
            ", TxnDetailCode='" + TxnDetailCode + '\'' +
            ", CardPhyType=" + CardPhyType +
            ", CardIssuerId='" + CardIssuerId + '\'' +
            ", CardSN='" + CardSN + '\'' +
            ", ProductTypeId=" + ProductTypeId +
            ", ProductCategory=" + ProductCategory +
            ", ProductExpireDate='" + ProductExpireDate + '\'' +
            ", ProductClass=" + ProductClass +
            ", PassengerType=" + PassengerType +
            ", LanguageId=" + LanguageId +
            ", ProductRemainmingValue=" + ProductRemainmingValue +
            ", TransactionValue=" + TransactionValue +
            ", TransactionDiscountValue=" + TransactionDiscountValue +
            ", TicketUsedCount=" + TicketUsedCount +
            '}';
    }
}
