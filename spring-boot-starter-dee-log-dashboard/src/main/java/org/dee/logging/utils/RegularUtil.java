package org.dee.logging.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mjh
 * 正则表达式取值
 */
public class RegularUtil {

	//方括号[]
	public final static String SQUARE_BRACKETS = "1";

	//大括号{}
	public final static String BRACES = "2";

	//圆括号()
	public final static String PARENTHESES = "3";


	public static void main(String[] args) {
		String s = "1是[1]";
		List<String> a = outputMsg(s,"是");
//		String str = "(1)(2)";
//		List<String> a = outputMsg3(str);
//		String str = "{1}{2}";
//		List<String> a = outputMsg2(str);
		for(String as : a){
			System.out.println(as);
		}
	}

	/**
	 * 使用正则表达式提取符号中的内容
	 * @param msg
	 * @param prefix
	 * @param symbolType
	 * @return
	 */
	public static ArrayList<String> outputMsgs(String msg, String prefix, String symbolType) {
		String patternType = "";
		//方括号[]
		if(SQUARE_BRACKETS.equals(symbolType)){
			patternType = "("+prefix+"\\[[^\\]]*\\])";
		}
		//大括号{}
		if(BRACES.equals(symbolType)){
			patternType = "("+prefix+"\\{[^}]*)\\}";
		}
		//圆括号()
		if(PARENTHESES.equals(symbolType)){
			patternType = "\\(.*?\\)";
		}
		ArrayList<String> list=new ArrayList<String>();
		Pattern p = Pattern.compile(patternType);
		Matcher m = p.matcher(msg);
		while(m.find()){
			String mgroup = m.group().replace(prefix, "");
			list.add(mgroup.substring(1, mgroup.length()-1));
		}
		return list;
	}

	/**
	 * 使用正则表达式提取[]中的内容
	 * @param msg
	 * @return
	 */
	public static ArrayList<String> outputMsg(String msg, String prefix){
		ArrayList<String> list=new ArrayList<String>();
		Pattern p = Pattern.compile("("+prefix+"\\[[^\\]]*\\])");
		Matcher m = p.matcher(msg);
		while(m.find()){
			String mgroup = m.group().replace(prefix, "");
			list.add(mgroup.substring(1, mgroup.length()-1));
		}
		return list;
	}

	/**
	 * 使用正则表达式提取{}中的内容
	 * @param msg
	 * @return
	 */
	public static ArrayList<String> outputMsg2(String msg){
		ArrayList<String> list=new ArrayList<String>();
		Pattern p = Pattern.compile("\\{([^}]*)\\}");
		Matcher m = p.matcher(msg);
		while(m.find()){
			list.add(m.group().substring(1, m.group().length()-1));
		}
		return list;
	}

	/**
	 * 使用正则表达式提取{}中的内容
	 * @param msg
	 * @return
	 */
	public static ArrayList<String> outputMsg22(String msg, String prefix){
		ArrayList<String> list=new ArrayList<String>();
		Pattern p = Pattern.compile("("+prefix+"\\{[^}]*)\\}");
		Matcher m = p.matcher(msg);
		while(m.find()){
			String mgroup = m.group().replace(prefix, "");
			list.add(mgroup.substring(1, mgroup.length()-1));
		}
		return list;
	}

	/**
	 * 使用正则表达式提取()中的内容
	 * @param msg
	 * @return
	 */
	public static ArrayList<String> outputMsg3(String msg){
		ArrayList<String> list=new ArrayList<String>();
		Pattern p = Pattern.compile("\\(.*?\\)");
		Matcher m = p.matcher(msg);
		while(m.find()){
			list.add(m.group().substring(1, m.group().length()-1));
		}
		return list;
	}

}
