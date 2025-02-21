package com.sse.sseapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionUtil {

	private static final String REGEX = "\\d+(\\.\\d+){2}";
	
	private static boolean isVersion(String version){
		Pattern pattern = Pattern.compile(REGEX);
		Matcher matcher = pattern.matcher(version);
		return matcher.find();
	}
	
	public static boolean geTargetVersion(String currentVsersion,String targetVsersion) {
		boolean result = false;
		boolean isCurrentVersion = isVersion(currentVsersion);
		boolean isTargetVsersion = isVersion(targetVsersion);
		
		if(isCurrentVersion && isTargetVsersion){
			String[] currentVsersionArr = currentVsersion.split("\\.");
			String[] targetVsersionArr = targetVsersion.split("\\.");
			if( Integer.parseInt(currentVsersionArr[0]) > Integer.parseInt(targetVsersionArr[0]) ){
				result = true;
			}else if( Integer.parseInt(currentVsersionArr[0]) == Integer.parseInt(targetVsersionArr[0]) ){
				if( Integer.parseInt(currentVsersionArr[1]) > Integer.parseInt(targetVsersionArr[1]) ){
					result = true;
				}else if( Integer.parseInt(currentVsersionArr[1]) == Integer.parseInt(targetVsersionArr[1]) ){
					if( Integer.parseInt(currentVsersionArr[2]) >= Integer.parseInt(targetVsersionArr[2]) ){
						result = true;
					}
				}
			}
		}
		
		return result;
	}

	//TODO  一下代码待测试
	public static boolean leTargetVersion(String currentVsersion,String targetVsersion) {
		boolean result = false;
		boolean isCurrentVersion = isVersion(currentVsersion);
		boolean isTargetVsersion = isVersion(targetVsersion);
		
		if(isCurrentVersion && isTargetVsersion){
			String[] currentVsersionArr = currentVsersion.split("\\.");
			String[] targetVsersionArr = targetVsersion.split("\\.");
			if( Integer.parseInt(currentVsersionArr[0]) <= Integer.parseInt(targetVsersionArr[0]) ){
				if( Integer.parseInt(currentVsersionArr[1]) <= Integer.parseInt(targetVsersionArr[1]) ){
					if( Integer.parseInt(currentVsersionArr[2]) <= Integer.parseInt(targetVsersionArr[2]) ){
						result = true;
					}
				}
			}
		}
		return result;
	}

	public static boolean lTargetVersion(String currentVsersion,String targetVsersion) {
		boolean result = false;
		boolean isCurrentVersion = isVersion(currentVsersion);
		boolean isTargetVsersion = isVersion(targetVsersion);
		
		if(isCurrentVersion && isTargetVsersion){
			String[] currentVsersionArr = currentVsersion.split("\\.");
			String[] targetVsersionArr = targetVsersion.split("\\.");
			if( Integer.parseInt(currentVsersionArr[0]) < Integer.parseInt(targetVsersionArr[0]) ){
				result = true;
			}else{
				if( Integer.parseInt(currentVsersionArr[1]) < Integer.parseInt(targetVsersionArr[1]) ){
					result = true;
				}else{
					if( Integer.parseInt(currentVsersionArr[2]) < Integer.parseInt(targetVsersionArr[2]) ){
						result = true;
					}
				}
			}
		}
		return result;
	}

	public static boolean gTargetVersion(String currentVsersion,String targetVsersion) {
		boolean result = false;
		boolean isCurrentVersion = isVersion(currentVsersion);
		boolean isTargetVsersion = isVersion(targetVsersion);
		
		if(isCurrentVersion && isTargetVsersion){
			String[] currentVsersionArr = currentVsersion.split("\\.");
			String[] targetVsersionArr = targetVsersion.split("\\.");
			if( Integer.parseInt(currentVsersionArr[0]) >= Integer.parseInt(targetVsersionArr[0]) ){
				if( Integer.parseInt(currentVsersionArr[1]) >= Integer.parseInt(targetVsersionArr[1]) ){
					if( Integer.parseInt(currentVsersionArr[2]) > Integer.parseInt(targetVsersionArr[2]) ){
						result = true;
					}
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		
		System.out.println(geTargetVersion("1.6.0","1.5.0"));
	}
}
