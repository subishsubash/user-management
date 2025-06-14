package com.subash.user.management.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

/**
 * This class helps to manage the generic methods for logging
 *
 * @author subash s
 */
public class GenericLogger {

    private final static String COMMA = ", ";

    @Value("${print.log.enable.request}")
    private static boolean logRequest;
    @Value("${print.log.enable.response}")
    private static boolean logResponse;

    /**
     * Method helps to log the API requests
     *
     * @param logger
     * @param operationId
     * @param method
     * @param requestBody
     */
    public static void logRequest(Logger logger, String UUID, String operationId, String method, Object requestBody) {
        if (logRequest) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String requestBodyString = mapper.writeValueAsString(requestBody);
                StringBuffer logMsg = new StringBuffer(UUID);
                logMsg.append(COMMA).append(Constants.LOG_APP).append(Constants.LOG_APP_NAME).append(COMMA).append(Constants.LOG_OPERATION_ID)
                        .append(operationId).append(COMMA).append(Constants.LOG_METHOD).append(method)
                        .append(COMMA).append(Constants.LOG_REQUEST)
                        .append(requestBodyString);
                logger.info(logMsg.toString());
            } catch (Exception e) {
                logger.info(UUID + COMMA + Constants.LOG_FAILURE_MSG + e.getMessage());
            }
        }
    }

    /**
     * @param logger
     * @param UUID
     * @param status
     * @param responseObject
     */
    public static void logResponse(Logger logger, String UUID, String status, Object responseObject) {
        if (logResponse) {
            try {
                StringBuffer logMsg = new StringBuffer(UUID);
                ObjectMapper mapper = new ObjectMapper();
                String responseObjectString = mapper.writeValueAsString(responseObject);
                logMsg.append(COMMA).append(Constants.LOG_APP).append(Constants.LOG_APP_NAME).append(COMMA).append(Constants.LOG_STATUS)
                        .append(status).append(COMMA).append(Constants.LOG_RESPONSE).append(responseObjectString);
                logger.info(logMsg.toString());
            } catch (Exception e) {
                logger.info(UUID + COMMA + Constants.LOG_FAILURE_MSG + e.getMessage());
            }
        }
    }

    /**
     * Method helps to generate the UUID for logger
     *
     * @return
     */
    public static String getUUID() {
        StringBuffer UUIDString = new StringBuffer(Constants.LOG_UUID);
        java.util.UUID uuid = UUID.randomUUID();
        UUIDString.append(uuid);
        return UUIDString.toString();
    }
}
