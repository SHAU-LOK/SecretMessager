package com.shootloking.secretmessager.event;

/**
 * Created by shau-lok on 3/20/16.
 */
public class EncryptEvent {

    public EncryptEvent(String body) {
        this.body = body;
    }

    public long EncryptConsumeTime;
    public String body;
    public String description;
    public String beforeLength;
    public String afterLength;
    public String plainText;
    public String cipherText;

}
