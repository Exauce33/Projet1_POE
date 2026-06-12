class Message {
    private final String messageID;
    private final int    messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private String status;

    public Message(String messageID, int messageNumber,
                   String recipient, String messageText, String messageHash) {
        this.messageID     = messageID;
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageHash   = messageHash;
        this.status = "Pending";
    }

    public String getMessageID()     { return messageID; }
    public int    getMessageNumber() { return messageNumber; }
    public String getRecipient()     { return recipient; }
    public String getMessageText()   { return messageText; }
    public String getMessageHash()   { return messageHash; }
    public String getStatus()        { return status; }
    public void   setStatus(String status) { this.status = status; }
}
