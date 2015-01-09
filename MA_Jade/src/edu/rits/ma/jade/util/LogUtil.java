package edu.rits.ma.jade.util;

import jade.util.Logger;

public class LogUtil {
	public static void logInfo(Object object, String message) {
		Logger.getLogger(object.getClass().getName()).log(Logger.INFO, "At Thread " + Thread.currentThread().getId() + " " + message);
	}
}
