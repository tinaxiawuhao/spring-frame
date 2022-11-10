package com.example.springframe.exception;

import javax.script.ScriptException;

/**
 * 自定义异常：js执行算术表达式出现NaN时抛出异常
 */
public class NaNException extends ScriptException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8760911634666797786L;

	public NaNException(Exception e) {
		super(e);
	}

	public NaNException(String s) {
		super(s);
	}
	
}
