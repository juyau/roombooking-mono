package org.thebreak.roombooking.common;

public enum BookingStatusEnum {

    UNPAID(1, "unpaid"),
    PAID(2,  "Paid"),
    FINISHED(3,  "Finished"),
    CANCELLED(4,  "cancelled"),
    REFUNDED(5,  "Refunded");

    private final int code;
    private final String description;

    BookingStatusEnum(int code, String description){
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
