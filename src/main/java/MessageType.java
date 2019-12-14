public enum MessageType {
    INFO("INFO"),
    ERROR("ERROR"),
    DEBUG("DEBUG"),
    WARN("WARNING"),
    OTHER;

    String type;

    MessageType() {
    }

    MessageType(String type) {
        this.type = type;
    }
}
