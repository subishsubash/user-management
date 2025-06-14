package com.subash.user.management.util;

/**
 * Generic class to keep all the constant values
 *
 * @author subash s
 */
public class Constants {

    // Constants for logger
    public final static String LOG_APP_NAME = "User Management";
    public final static String LOG_OPERATION_ID = "[OPERATION ID] : ";
    public final static String LOG_METHOD = "[HTTP METHOD] : ";
    public final static String LOG_REQUEST = "[REQUEST BODY] : ";
    public final static String LOG_RESPONSE = "[RESPONSE BODY] : ";
    public final static String LOG_FAILURE_MSG = "[FAILED TO LOG] : ";
    public final static String LOG_UUID = "[UUID] : ";
    public final static String LOG_STATUS = "[STATUS] : ";
    public final static String LOG_APP = "[APPLICATION] : ";

    public final static String LOG_MESSAGE = "[MESSAGE] : ";
    public final static String COMMA = ", ";

    // Operation Id
    public final static String CREATE_USER = "createUser";
    public final static String GET_USER = "getUser";

    // API response
    public static final String CREATE_RECORD_SUCCESS = "Record created successfully";
    public static final String CREATE_RECORD_FAILURE = "Failed to create record";
    public final static String API_PROCESSED_FAILURE = "Error while processing the API";

    // Method
    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";

    //Response Code
    public static final Integer SUCCESS_CODE = 5001;
    public static final Integer FAILURE_CODE = 7001;

}
