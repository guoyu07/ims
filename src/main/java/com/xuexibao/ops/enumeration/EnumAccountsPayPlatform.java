package com.xuexibao.ops.enumeration;

public enum EnumAccountsPayPlatform {

	/**
	 * 支付宝
	 */
	ALIPAY(new Integer(1).byteValue());

	private EnumAccountsPayPlatform(Byte type) {
		this.type = type;
	}

	private Byte type;

	public Byte value() {
		return type;
	}
}
