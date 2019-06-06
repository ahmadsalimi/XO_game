package view;

public enum ErrorType {
    CLIENT_ERROR("Client error!"),
    SERVER_ERROR("Server error!");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
