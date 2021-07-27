package org.thebreak.roombooking.model.response;

public enum CommonCode{

    SUCCESS(true, 200, "OK"),
    FAILED(false, 9999, "Operation failed."),

    // system
    SYSTEM_ERROR(false, 9999, "system error."),

    INVALID_PARAM(false, 3002, "parameter not valid."),

    // database access code
    DB_ENTRY_ALREADY_EXIST(false, 3001, "entry already exist."),
    DB_EMPTY_LIST(true, 3002, "query list result is empty."),
    DB_ENTRY_NOT_FOUND(false, 3003, "data entry not exist.");

    private final int code;
    private final String message;
    private final Boolean success;

    CommonCode(Boolean success, int code, String message){
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return success;
    }
}
