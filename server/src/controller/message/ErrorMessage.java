package controller.message;

class ErrorMessage {
    private String messageId;
    private String errorMessage;

    ErrorMessage(String  messageId, String errorMessage) {
        this.messageId = messageId;
        this.errorMessage = errorMessage;
    }
}
