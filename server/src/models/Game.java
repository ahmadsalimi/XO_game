package models;

import models.exception.ClientException;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class Game {
    private static Integer number = 0;
    private int row;
    private int column;
    private int gameId;
    private HashMap<Character, PlayerInfo> playersInfo = new HashMap<>();
    private char[][] currentState;
    private char currentTurn = 'X';
    private char winnerSign;
    private int numberOfTurnsPlayed;
    private int combo;
    private boolean finished;

    public Game(Account xAccount, Account oAccount, int row, int column) {
        synchronized (number) {
            this.gameId = number++;
        }
        this.row = row;
        this.column = column;
        this.playersInfo.put('X', new PlayerInfo(xAccount));
        this.playersInfo.put('O', new PlayerInfo(oAccount));
        this.combo = (row == 3 || column == 3) ? 3 : 4;
        this.initializeCurrentState();
    }

    public void put(Position position) {
        playersInfo.get(currentTurn).setLastPosition(position);
        currentState[position.getRow()][position.getColumn()] = currentTurn;
        numberOfTurnsPlayed++;

        if (gameIsFinished(position.getRow(), position.getColumn())) {
            applyGameStatus();
            finished = true;
            return;
        }

        changeCurrentTurn();
    }

    public Position undo() throws ClientException {
        if (currentTurn == 'X') {
            return applyUndo('O');
        }
        return applyUndo('X');
    }

    public boolean invalidPut(Position position) {
        return position.getRow() >= row
                || position.getRow() < 0
                || position.getColumn() >= column
                || position.getColumn() < 0
                || currentState[position.getRow()][position.getColumn()] != 0;
    }

    public char[][] getCurrentState() {
        return currentState;
    }

    public HashMap<Character, PlayerInfo> getPlayersInfo() {
        return playersInfo;
    }


    public Account getOtherAccount(Account account) throws ClientException {
        if (playersInfo.get('X').getAccount().equals(account)) {
            return playersInfo.get('O').getAccount();
        }
        if (playersInfo.get('O').getAccount().equals(account)) {
            return playersInfo.get('X').getAccount();
        }
        throw new ClientException("this game isn't yours");
    }

    public boolean haveAccount(Account account) {
        for (PlayerInfo info : playersInfo.values()) {
            if (info.getAccount().equals(account)) {
                return true;
            }
        }
        return false;
    }

    public int getId() {
        return this.gameId;
    }

    public boolean isFinished() {
        return finished;
    }

    public char getWinnerSign() {
        return winnerSign;
    }

    public boolean canAct(Account account) {
        return playersInfo.get(currentTurn).getAccount().equals(account);
    }

    public char getCurrentTurn() {
        return this.currentTurn;
    }

    private void initializeCurrentState() {
        currentState = new char[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                currentState[i][j] = 0;
            }
        }
    }


    private void changeCurrentTurn() {
        switch (currentTurn) {
            case 'X':
                currentTurn = 'O';
                break;
            case 'O':
                currentTurn = 'X';
                break;
        }
    }

    private void applyGameStatus() {
        if (winnerSign == 0) {
            setDrawsOnScores();
        } else {
            setWinAndLoseOnScores();
        }
    }

    private void setDrawsOnScores() {
        playersInfo.get('X').getAccount().increaseNumberOfDraws();
        playersInfo.get('O').getAccount().increaseNumberOfDraws();
    }

    private void setWinAndLoseOnScores() {
        for (char playerSign : playersInfo.keySet()) {
            if (playerSign == winnerSign) {
                playersInfo.get(playerSign).getAccount().increaseNumberOfWinnings();
            } else {
                playersInfo.get(playerSign).getAccount().increaseNumberOfLoses();
            }
        }
    }

    private boolean gameIsFinished(int i, int j) {
        return checkHorizontal(i, j)
                || checkVertical(i, j)
                || checkAscendingDiagonal(i, j)
                || checkDescendingDiagonal(i, j)
                || checkDraw();
    }

    private boolean checkDraw() {
        return numberOfTurnsPlayed == row * column;
    }

    private boolean checkHorizontal(int i, int j) {
        int start = j >= combo ? -combo + 1 : -j;
        int end = column - j >= combo ? 0 : column - combo - j;

        for (int k = start; k <= end; k++) {
            if (horizontalLineFound(i, j + k)) return true;
        }
        return false;
    }

    private boolean horizontalLineFound(int i, int j) {
        for (int k = 0; k < combo; k++) {
            if (currentState[i][j + k] != currentTurn)
                return false;
        }
        winnerSign = currentTurn;
        return true;
    }

    private boolean checkVertical(int i, int j) {
        int start = i >= combo ? -combo + 1 : -i;
        int end = row - i >= combo ? 0 : row - combo - i;

        for (int k = start; k <= end; k++) {
            if (verticalLineFound(i + k, j)) return true;
        }
        return false;
    }

    private boolean verticalLineFound(int i, int j) {
        for (int k = 0; k < combo; k++) {
            if (currentState[i + k][j] != currentTurn)
                return false;
        }
        winnerSign = currentTurn;
        return true;
    }

    private boolean checkAscendingDiagonal(int i, int j) {
        int start = getStartOfDiagonal(i, j);
        int end = getEndOfDiagonal(i, j);

        for (int k = start; k <= end; k++) {
            if (ascendingDiagonalFound(i + k, j + k)) return true;
        }
        return false;
    }

    private boolean ascendingDiagonalFound(int i, int j) {
        for (int k = 0; k < combo; k++) {
            if (currentState[i + k][j + k] != currentTurn) {
                return false;
            }
        }
        winnerSign = currentTurn;
        return true;
    }

    private boolean checkDescendingDiagonal(int i, int j) {
        int mirroredJ = column - j - 1;
        int start = getStartOfDiagonal(i, mirroredJ);
        int end = getEndOfDiagonal(i, mirroredJ);

        for (int k = start; k <= end; k++) {
            if (descendingDiagonalFound(i + k, j - k)) return true;
        }
        return false;
    }

    private boolean descendingDiagonalFound(int i, int j) {
        boolean win = true;
        for (int k = 0; k < combo; k++) {
            if (currentState[i + k][j - k] != currentTurn) {
                win = false;
                break;
            }
        }
        if (win) {
            winnerSign = currentTurn;
            return true;
        }
        return false;
    }

    private int getStartOfDiagonal(int i, int j) {
        int start;
        if (i < j) {
            start = i >= combo ? -combo + 1 : -i;
        } else {
            start = j >= combo ? -combo + 1 : -j;
        }
        return start;
    }

    private int getEndOfDiagonal(int i, int j) {
        int end;
        if (row - i < column - j) {
            end = row - i >= combo ? 0 : row - combo - i;
        } else {
            end = column - j >= combo ? 0 : column - combo - j;
        }
        return end;
    }

    private Position applyUndo(char playerSign) throws ClientException {
        if (!playersInfo.get(playerSign).isUndoUsed() && playersInfo.get(playerSign).getLastPosition() != null) {

            currentTurn = playerSign;
            Position position = playersInfo.get(playerSign).getLastPosition();
            currentState[position.getRow()][position.getColumn()] = 0;
            playersInfo.get(playerSign).setUndoUsed();
            numberOfTurnsPlayed--;
            return position;
        } else {
            throw new ClientException("you can not undo");
        }
    }

    public char getTurn(Account account) {
        AtomicReference<Character> value = new AtomicReference<>((char) 0);
        playersInfo.forEach((character, playerInfo) -> {
            if (playerInfo.getAccount().equals(account)) {
                value.set(character);
            }
        });
        return value.get();
    }
}
