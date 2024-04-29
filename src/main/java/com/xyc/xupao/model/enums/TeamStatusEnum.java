package com.xyc.xupao.model.enums;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/29 15:12
 * @Description: TODO 队伍状态枚举
 */
public enum TeamStatusEnum {
	/** 公共 */
	PUBLIC(0, "公开"),
	/** 私人 */
	PRIVATE(1, "私有"),
	/** 秘密 */
	SECRET(2, "加密");

	private int value;
	private String text;

	public  static  TeamStatusEnum getEnumByValue(Integer value){
		if(value == null){
			return null;
		}
		for(TeamStatusEnum teamStatusEnum : TeamStatusEnum.values()){
			if(teamStatusEnum.getValue() == value){
				return teamStatusEnum;
			}
		}
		return null;
	}
	TeamStatusEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
