package com.bercut.sa.redis.model;

/**
 * Created by haimin-a on 10.07.2019.
 */
public class InitMessage {

    private Long messageId;
    private Integer chunk;
    private Integer partCount;
    private String message;
    private String dataEncoding;

    public InitMessage(Long messageId, Integer chunk, Integer partCount, String message, String dataEncoding) {
        this.messageId = messageId;
        this.chunk = chunk;
        this.partCount = partCount;
        this.message = message;
        this.dataEncoding = dataEncoding;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Integer getChunk() {
        return chunk;
    }

    public Integer getPartCount() {
        return partCount;
    }

    public String getMessage() {
        return message;
    }

    public String getDataEncoding() {
        return dataEncoding;
    }
}
