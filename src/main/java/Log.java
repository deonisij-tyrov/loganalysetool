
import java.time.LocalDateTime;

public class Log {

    private String userName;
    private LocalDateTime time;
    private String message;
    private MessageType messageType;

    public Log(String userName, LocalDateTime time, String message, MessageType messageType) {
        this.userName = userName;
        this.time = time;
        this.message = message;
        this.messageType = messageType;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        String delimiter = " | ";
        StringBuilder builder = new StringBuilder();

        builder.append(messageType.type);
        builder.append(delimiter);
        builder.append(userName);
        builder.append(delimiter);
        builder.append(time.getHour()).append(":").append(time.getMinute()).append(":")
                .append(time.getSecond()).append(" ").append(time.getDayOfMonth())
                .append(".").append(time.getMonthValue()).append(".").append(time.getYear());
        builder.append(delimiter);
        builder.append(message);
        builder.append("\n");

        return builder.toString();
    }
}
