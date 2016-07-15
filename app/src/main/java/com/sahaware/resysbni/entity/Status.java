package com.sahaware.resysbni.entity;

public class Status {

    private String Message;
    private String description;
    private Boolean Succeeded;
    private Integer code;

    /**
     *
     * @return
     * The Message
     */
    public String getMessage() {
        return Message;
    }

    /**
     *
     * @param Message
     * The Message
     */
    public void setMessage(String Message) {
        this.Message = Message;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The Succeeded
     */
    public Boolean getSucceeded() {
        return Succeeded;
    }

    /**
     *
     * @param Succeeded
     * The Succeeded
     */
    public void setSucceeded(Boolean Succeeded) {
        this.Succeeded = Succeeded;
    }

    /**
     *
     * @return
     * The code
     */
    public Integer getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

}