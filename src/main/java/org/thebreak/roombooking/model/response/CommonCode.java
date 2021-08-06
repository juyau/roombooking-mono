package org.thebreak.roombooking.model.response;

public enum CommonCode{

    SUCCESS(true, 200, "OK"),
    FAILED(false, 9999, "Operation failed."),

    // system
    SYSTEM_ERROR(false, 9000, "system error."),
    INVALID_PARAM(false, 3000, "parameter not valid."),

    // request param check
    REQUEST_FIELD_MISSING(false, 4001, "Missing required field(s)."),
    REQUEST_FIELD_EMPTY(false, 4002, "Request included empty field(s)"),
    REQUEST_FIELD_INVALID(false, 4003, "Request field(s) include invalid data"),

    // Room code
    ROOM_ENTRY_ALREADY_EXIST(false, 5001, "room with same address and room number already exist."),

    // database access code
    DB_ENTRY_ALREADY_EXIST(false, 3001, "entry already exist."),
    DB_EMPTY_LIST(true, 3002, "query list result is empty."),
    DB_DELETE_FAILED(false, 3003, "failed to delete entry."),
    DB_ENTRY_NOT_FOUND(false, 3004, "data entry not exist.");

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
