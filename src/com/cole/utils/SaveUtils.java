package cole.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveUtils {
	private static String SP_NAME="spname";
	//写入数据
	public static <SP_NAME> void writeUser(Context mContext, String name, String value){
		SharedPreferences sp=mContext.getSharedPreferences(SP_NAME, mContext.MODE_PRIVATE);
		SharedPreferences.Editor editor=sp.edit();
		editor.putString(name,value);
		editor.commit();
	}
	//读取数据
	public static String readUser(Context mContext, String name){
		SharedPreferences sp=mContext.getSharedPreferences(SP_NAME, mContext.MODE_PRIVATE);

		
		String content=sp.getString(name, "");
		return content;
	}
}
