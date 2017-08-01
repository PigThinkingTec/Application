package com.pigthinkingtec.framework.databean.message;

/**
 * メッセージを識別する列挙型
 * 
 *
 * @author yizhou
 */
public enum MessageType {

    Error("error"),Warning("warn"),Infomation("info"),Dialog("dialog");
    
    private String type;

    private MessageType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
    
    
}
