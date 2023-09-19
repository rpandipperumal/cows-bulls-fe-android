package com.techtweakz.cowsandbulls;

public class GameData {
    private String fromUser;

    private String toUser;

    private String word;

    private String roomName;

    private int cowsCount;

    private int bullsCount;

    public GameData() {
        super();
    }

    /**
     * @param fromUser
     * @param toUser
     * @param word
     * @param cowsCount
     * @param bullsCount
     */
    public GameData(String fromUser, String toUser, String word, String roomName, int cowsCount, int bullsCount) {
        super();
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.word = word;
        this.roomName = roomName;
        this.cowsCount = cowsCount;
        this.bullsCount = bullsCount;
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

    /**
     * @return the cowsCount
     */
    public int getCowsCount() {
        return cowsCount;
    }

    /**
     * @param cowsCount the cowsCount to set
     */
    public void setCowsCount(int cowsCount) {
        this.cowsCount = cowsCount;
    }

    /**
     * @return the bullsCount
     */
    public int getBullsCount() {
        return bullsCount;
    }

    /**
     * @param bullsCount the bullsCount to set
     */
    public void setBullsCount(int bullsCount) {
        this.bullsCount = bullsCount;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
