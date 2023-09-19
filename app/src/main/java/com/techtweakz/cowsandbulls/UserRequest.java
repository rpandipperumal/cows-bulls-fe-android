package com.techtweakz.cowsandbulls;

public class UserRequest {

    private String fromUser;

    private String toUser;

    private String word;


    public UserRequest() {

        super();
    }

    /**
     * @param fromUser
     * @param toUser
     * @param word
     */
    public UserRequest(String fromUser, String toUser, String word) {
        super();
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.word = word;
    }

    /**
     * @return the fromUser
     */
    public String getFromUser() {
        return fromUser;
    }


    /**
     * @param fromUser the fromUser to set
     */
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }


    /**
     * @return the toUser
     */
    public String getToUser() {
        return toUser;
    }


    /**
     * @param toUser the toUser to set
     */
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }


    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }


    /**
     * @param word the word to set
     */
    public void setWord(String word) {
        this.word = word;
    }


}
