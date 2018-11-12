package cole.common;

public class P {

	/**
	 *
	 * 获取验证码
	 * http://222.190.139.10:7070/magicRest/accountRest/getRegisterVerificationCode?number=18055835997&smsType=1
	 *
	 *{"resCode":"000","resMsg":"成功"}
	 */


	public static final String GET_SMS="/magicRest/accountRest/getRegisterVerificationCode?";


	/**
	 *
	 * 获取验证码后设置密码去注册======
	 * http://222.190.139.10:7070/magicRest/accountRest/registermagic?number=13851774165&smsType=1&name=xjd&smsCode=958057&password=123189
	 *
	 *{
	 * "resCode":"111",
	 * "resMsg":"短信验证码过期，请重新获取"
	 * }
	 */

	public static final String CKECK_SMS_Register = "/magicRest/accountRest/registermagic?";


	public static String IP="222.190.139.10";//外网映射地址
	public static String PORT=":7070";

	public static final String CKECK_USE = "/pronline/Msg?FunName=ict_Auth_New";

	public static final String APP = "&logintype=app";
//	   &page=1&dir=1&start=1&type=&limit=100";

	public static final String GET_NEWS_LIST =
			"/pronline/Msg?FunName@ict_school_indexNews";
	public static String USER_NICKNAME = "";
	public static String COMBO_START_TIME = "";
	public static String COMBO_END_TIME = "";
	public static String REAL_NAME = "";
	public static String USER_SAX = "";
	public static String USER_CCOMBO_STYLE = "";
	public static String USER_IMAGEURL = "";
	public static String USER_PHONE = "";
	public static String USER_SCHOOL = "";
	public static String USER_STUDENT_ID = "";
	public static String USER_ID_CARD = "";
	public static String USER_COMBO_ID = "";
	public static String USER_IS_AUTO_RENEW = "";


	public static final String HTTP ="http://" ;
	public static String USER_MY_ACCOUNT_MONEY = "";
	public static String PROPOTOL="http";
//	public static String IP="192.168.20.154";

	public static String VISITOR="&usertype=4";//访客登录
	public static   int LOGIN  = 0x01;

	





	/**
	 * 获取图片
	 *  ======http://192.168.20.154:8006/uploadNews
	 */
	public static final String GET_PIC_DETAIL="/uploadNews";
	/**
	 * 获取图片
	 *  ======http://192.168.20.154:8006/uploadNews
	 */
	public static final String GET_IMAGE_DETAIL="http://192.168.20.154:8006/uploadNews";


	/**
	 * 获新闻的详情
	 * http:
	 */
	public static final String GET_NEWS_DETAILINFO="/pronline/Msg?FunName@ict_school_indexNews";
	public static final String GET_NEWS_DETAIL="/wap/newsDetails.html?id=";


	/**
	 * 验证登录=
	 */
	public static final String CHECK_LOGIN="/pronline/Msg?FunName=ict_check_pass_new";

	/**
	 * 检测网络是否连接的参数
	 */
	public static String CheckIp = "222.73.136.209";
	public static int CheckPort = 6022;

	public static String linkssid = "";
}
