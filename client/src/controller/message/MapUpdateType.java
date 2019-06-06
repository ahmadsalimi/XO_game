package controller.message;

public enum MapUpdateType {
    X('X'), O('O'), EMPTY((char) 0);

    private final char value;

    MapUpdateType(char value) {
        this.value = value;
    }

    public char getChar() {
        return value;
    }
}
