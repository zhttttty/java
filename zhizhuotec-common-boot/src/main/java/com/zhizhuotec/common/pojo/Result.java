package com.zhizhuotec.common.pojo;

public class Result {

	private String flag;

	private String msg;

	private Object data;

	public static Result ok(Object data) {
		return new Result(data);
	}

	public static Result ok() {
		return new Result(null);
	}

	public static Result build(String flag, String msg, Object data) {
		return new Result(flag, msg, data);
	}

	public static Result build(String flag, String msg) {
		return new Result(flag, msg, null);
	}

	public Result() {

	}

	public Result(Object data) {
		this.flag = "000";
		this.msg = "请求成功";
		this.data = data;
	}

	public Result(String flag, String msg, Object data) {
		this.flag = flag;
		this.msg = msg;
		this.data = data;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
