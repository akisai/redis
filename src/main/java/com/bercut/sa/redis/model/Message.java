package com.bercut.sa.redis.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haimin-a on 09.07.2019.
 */
public class Message implements Serializable {


    private static final long serialVersionUID = -3610210380388498249L;
    private Long messageId;
    private Integer chunk;
    private Integer partCount;
    private List<String> message = new ArrayList<>();
    private String dataEncoding;

    public Message(Long messageId, Integer chunk, Integer partCount, String message, String dataEncoding) {
        this.messageId = messageId;
        this.chunk = partCount - 1;
        this.partCount = partCount;
        for (int i = 0; i < this.partCount; i++) {
            this.message.add("");
        }
        this.message.set(this.chunk, message);
        this.dataEncoding = dataEncoding;
    }

    public Message(InitMessage message) {
        this.messageId = message.getMessageId();
        this.partCount = message.getPartCount();
        this.chunk = this.partCount - 1;
        for (int i = 0; i < this.partCount; i++) {
            this.message.add("");
        }
        this.message.set(message.getChunk(), message.getMessage());
        this.dataEncoding = message.getDataEncoding();
    }

    public void setMessage(String message, Integer chunk) {
        this.message.set(chunk, message);
        this.chunk -= 1;
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

    public List<String> getMessage() {
        return message;
    }

    public String getDataEncoding() {
        return dataEncoding;
    }
}
