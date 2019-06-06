package models;

public class Account {
    private String name;
    private int numberOfWinnings;
    private int numberOfDraws;
    private int numberOfLosses;

    public Account(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return name.equals(account.name);
    }

    String getName() {
        return name;
    }

    int getNumberOfWinnings() {
        return numberOfWinnings;
    }

    int getNumberOfDraws() {
        return numberOfDraws;
    }

    int getNumberOfLosses() {
        return numberOfLosses;
    }
}