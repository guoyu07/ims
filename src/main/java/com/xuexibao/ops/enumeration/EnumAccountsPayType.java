package com.xuexibao.ops.enumeration;

public enum EnumAccountsPayType {

	/**
	 * 充值
	 */
	CHARGE(new Integer(1).byteValue()),

	/**
	 * 购买内容
	 */
	BUY(new Integer(2).byteValue()),
	
	/**
	 * 退款
	 */
	REFUND(new Integer(3).byteValue());

	private EnumAccountsPayType(Byte type) {
		this.type = type;
	}

	private Byte type;

	public Byte value() {
		return type;
	}
}
