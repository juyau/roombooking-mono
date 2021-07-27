package com.brothermiles.studyon.common.model.response;

/**
 *
 * 10000-- general error code
 * 22000-- media error code
 * 23000-- user center error code
 * 24000-- cms error code
 * 25000-- file system error code
 */
public interface ResultCode {
    // operation success or not, true for success
    boolean success();
    // operation code
    int code();
    // message
    String message();

}
