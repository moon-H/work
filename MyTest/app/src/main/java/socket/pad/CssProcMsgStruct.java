package socket.pad;

/**
 * Created by IntelliJ IDEA.
 * User: wangjf
 * Date: 2016-3-10
 * Time: 2:36:05
 * To change this template use File | Settings | File Templates.
 */
public class CssProcMsgStruct {

    public int iCmd = 0;                /*!<命令代码*/
    public int iAppendArgLen = 0;    /*!<附加参数长度(0便是不使用)*/
    public int iParam0 = 0;            /*!<参数1*/
    public int iParam1 = 0;            /*!<参数2*/
    public int iParam2 = 0;            /*!<参数3*/
    public int iParam3 = 0;            /*!<参数4*/
    public int iParam4 = 0;            /*!<参数5*/
    public int iParam5 = 0;            /*!<参数6*/
    public String pszParamData = null;    /*!<附加数据段（具体格式以及含义由各命令自行规定）*/

    @Override
    public String toString() {
        return "CssProcMsgStruct{" +
            "iCmd=" + iCmd +
            ", iAppendArgLen=" + iAppendArgLen +
            ", iParam0=" + iParam0 +
            ", iParam1=" + iParam1 +
            ", iParam2=" + iParam2 +
            ", iParam3=" + iParam3 +
            ", iParam4=" + iParam4 +
            ", iParam5=" + iParam5 +
            ", pszParamData='" + pszParamData + '\'' +
            '}';
    }
}
