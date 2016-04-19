package com.xuexibao.ops.enumeration;

public enum EnumPaymentStatus {
	/**
	 * 待支付
	 */
	UNPAID(1000, "待支付"),
	/**
	 * 系统异常
	 */
	ALIPAY_SYSTEM_ERROR(4000, "系统异常"),
	/**
	 * 订单参数错误
	 */
	ORDER_ARG_ERROR(4001, "订单参数错误"),
	/**
	 * 已经取消，用户行为
	 */
	USER_CANCEL(6001, "用户取消支付"),
	/**
	 * 网络连接异常
	 */
	NETWORK_ERROR(6002, "网络连接异常"),
	/**
	 * 超时取消
	 */
	TIMEOUT_CANCEL(6003, "超时取消支付"),
	/**
	 * 未知异常
	 */
	UNKOWN_ERROR(8000, "未知异常"),
	/**
	 * 交易成功
	 */
	SUCCESSFUL(9000, "交易成功"),
	/**
	 * 已删除
	 */
	DELETED(-1, "已删除");

	private EnumPaymentStatus(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private Integer type;
	private String desc;

	public Integer getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
