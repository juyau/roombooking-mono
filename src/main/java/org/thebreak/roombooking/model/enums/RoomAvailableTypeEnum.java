package org.thebreak.roombooking.model.enums;

public enum AvailableTypeEnum {

    WEEKEND(1, "weekend"),
    WEEKDAY(2,  "weekday");

    private final int code;
    private final String description;

    AvailableTypeEnum(int code, String description){
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
