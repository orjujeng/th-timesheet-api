package com.orjujeng.timesheet.utils;

public enum ResultCode {
	ACCOUNT_NUM_NOT_FOUND("001"),
	DATA_UPDATE_ERROR("002"),
	DATA_INSTER_ERROR("003"),
	ACCESS_DENIDE("004"),
	PASSWORD_ERROR("005"),
	AUTH_ACCESS_DENIDE("006"),
	PARAM_WITH_ERROR("007"),
	PROJECT_OCCUPIED("008"),
	STILL_AS_MANAGER("009"),
	NO_DATA_SHOWN("010"),
	VALID_ERROR("401"),
	SUCCESS("200"),
    FAIL("400"),
    INTERNAL_SERVER_ERROR("500");

    public String code;
    ResultCode(String code) {
        this.code = code;
    }
}
