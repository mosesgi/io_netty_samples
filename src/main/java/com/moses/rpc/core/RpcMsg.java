package com.moses.rpc.core;

import java.io.Serializable;

public class RpcMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2149297353361444100L;

	private String className;
	private String methodName;
	private Class<?>[] params; // parameter type array
	private Object[] values; // parameter value array

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParams() {
		return params;
	}

	public void setParams(Class<?>[] params) {
		this.params = params;
	}

	public Object[] getValues() {
		return values;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

}
