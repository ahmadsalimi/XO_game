package models;

import java.util.Comparator;

public class PlayerSorter implements Comparator<Account> {
    @Override
    public int compare(Account account1, Account account2) {
        if (account2.getNumberOfWinnings() != account1.getNumberOfWinnings()) {
            return account2.getNumberOfWinnings() - account1.getNumberOfWinnings();
        }
        if (account1.getNumberOfLosses() != account2.getNumberOfLosses()) {
            return account1.getNumberOfLosses() - account2.getNumberOfLosses();
        }
        if (account1.getNumberOfDraws() != account2.getNumberOfDraws()) {
            return account1.getNumberOfDraws() - account2.getNumberOfDraws();
        }
        return account1.getName().compareTo(account2.getName());
    }
}
