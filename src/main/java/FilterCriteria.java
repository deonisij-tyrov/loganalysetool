import java.time.LocalDateTime;

public class FilterCriteria {

    private String userName;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private MessageType messageTemplate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String usetId) {
        this.userName = usetId;
    }

    public MessageType getMessageType() {
        return messageTemplate;
    }

    public void setMessageTemplate(MessageType messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime from) {
        this.fromDate = from;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime to) {
        this.toDate = to;
    }
}
