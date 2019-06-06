package controller;

import controller.message.GameState;
import controller.message.MapUpdateType;
import controller.message.Message;
import models.*;
import models.exception.ClientException;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class DataHandler {
    private static final DataHandler DATA_HANDLER = new DataHandler();
    private final HashMap<Integer, SocketPair> sockets = new HashMap<>();
    private final HashMap<Socket, Account> onlineAccounts = new HashMap<>();
    private final HashMap<String, SocketPair> accountSockets = new HashMap<>();
    private final HashMap<Account, Game> onlineGames = new HashMap<>();
    private final HashMap<Integer, Game> onlineGamesById = new HashMap<>();
    private final HashMap<Integer, Game> pausedGamesById = new HashMap<>();

    private DataHandler() {
    }

    static DataHandler getInstance() {
        return DATA_HANDLER;
    }

    void addSocket(SocketPair socketPair) {
        sockets.put(socketPair.getSocket().getPort(), socketPair);
        Message.makePortMessage(socketPair.getSocket().getPort()).sendTo(socketPair);
    }

    void removeSocket(Socket socket) throws ClientException {
        sockets.remove(socket.getPort());
        Account myAccount = onlineAccounts.get(socket);
        if (myAccount != null) {
            String name = onlineAccounts.get(socket).getName();
            System.out.println(name + " logged out.");

            loseGameIfPlaying(myAccount);

            accountSockets.remove(name);
            onlineAccounts.remove(socket);

            ArrayList<Integer> willBeDeleted = new ArrayList<>();
            pausedGamesById.forEach(((integer, game) -> {
                if (game.haveAccount(myAccount)) {
                    willBeDeleted.add(integer);
                }
            }));

            willBeDeleted.forEach(pausedGamesById::remove);
        }
    }

    private void loseGameIfPlaying(Account myAccount) throws ClientException {
        Game game = onlineGames.get(myAccount);
        if (game != null) {
            Account opponentAccount = game.getOtherAccount(myAccount);
            opponentAccount.increaseNumberOfWinnings();
            onlineGames.remove(myAccount);
            onlineGames.remove(opponentAccount);
            onlineGamesById.remove(game.getId());
            sendGameResultMessages(
                    accountSockets.get(myAccount.getName()), GameState.LOSE,
                    accountSockets.get(opponentAccount.getName()), GameState.WIN
            );

        }
    }

    void login(Message message) throws ClientException {
        if (alreadyRegistered(message.getLoginMessage().getUsername())) {
            throw new ClientException("username is repetitious");
        }

        SocketPair socketPair = sockets.get(message.getPort());

        onlineAccounts.put(socketPair.getSocket(), new Account(message.getLoginMessage().getUsername()));
        accountSockets.put(message.getLoginMessage().getUsername(), socketPair);

        System.out.println(message.getLoginMessage().getUsername() + " logged in successfully");
        Message.makeDoneMessage(message.getPort(), message.getMessageId()).sendTo(socketPair);
    }

    private boolean alreadyRegistered(String name) {
        return searchOnlineAccount(name) != null;
    }

    private Account searchOnlineAccount(String name) {
        for (Account account : onlineAccounts.values()) {
            if (account.equals(new Account(name))) {
                return account;
            }
        }

        return null;
    }

    private Account[] sortPlayersByScore() {
        Account[] accounts = new Account[onlineAccounts.size()];
        accounts = onlineAccounts.values().toArray(accounts);
        Arrays.sort(accounts, new PlayerSorter());
        return accounts;
    }

    void newGame(Message message) throws ClientException {
        SocketPair mySocketPair = sockets.get(message.getPort());
        Account myAccount = onlineAccounts.get(mySocketPair.getSocket());

        String opponentUsername = message.getNewGameMessage().getOpponentUsername();

        if (!alreadyRegistered(opponentUsername)) {
            throw new ClientException("opponent username is not valid");
        }

        SocketPair opponentSocketPair = accountSockets.get(opponentUsername);
        Account opponentAccount = onlineAccounts.get(opponentSocketPair.getSocket());

        if (myAccount.equals(opponentAccount)) {
            throw new ClientException("opponent username is not valid");
        }

        if (onlineGames.get(opponentAccount) != null) {
            throw new ClientException("this user is playing right now");
        }


        int row = message.getNewGameMessage().getRow();
        int column = message.getNewGameMessage().getColumn();

        Game newGame = new Game(myAccount, opponentAccount, row, column);

        System.out.println("game made");

        onlineGames.put(myAccount, newGame);
        onlineGames.put(opponentAccount, newGame);
        onlineGamesById.put(newGame.getId(), newGame);

        Message.makeMakeGameMessage(
                mySocketPair.getSocket().getPort(),
                opponentUsername,
                newGame.getCurrentState(),
                newGame.getId(),
                newGame.getCurrentTurn(),
                'X'
        ).sendTo(mySocketPair);

        Message.makeMakeGameMessage(
                opponentSocketPair.getSocket().getPort(),
                myAccount.getName(),
                newGame.getCurrentState(),
                newGame.getId(),
                newGame.getCurrentTurn(),
                'O'
        ).sendTo(opponentSocketPair);

        System.out.println("messages sent");
    }

    void resumeGame(Message message) throws ClientException {
        int gameId = message.getOtherGameActions().getGameId();

        Game game = pausedGamesById.get(gameId);

        if (game == null) {
            throw new ClientException("no such saved game!");
        }

        SocketPair mySocketPair = sockets.get(message.getPort());
        Account myAccount = onlineAccounts.get(mySocketPair.getSocket());

        Account opponentAccount = game.getOtherAccount(onlineAccounts.get(sockets.get(message.getPort()).getSocket()));

        if (!onlineAccounts.values().contains(opponentAccount)) {
            throw new ClientException("opponent is not online");
        }

        if (onlineGames.get(opponentAccount) != null) {
            throw new ClientException("this user is playing right now");
        }

        SocketPair opponentSocketPair = accountSockets.get(opponentAccount.getName());

        pausedGamesById.remove(gameId);
        onlineGames.put(myAccount, game);
        onlineGames.put(opponentAccount, game);
        onlineGamesById.put(gameId, game);

        Message.makeMakeGameMessage(
                mySocketPair.getSocket().getPort(),
                opponentAccount.getName(),
                game.getCurrentState(),
                gameId,
                game.getCurrentTurn(),
                game.getTurn(myAccount)
        ).sendTo(mySocketPair);

        Message.makeMakeGameMessage(
                opponentSocketPair.getSocket().getPort(),
                myAccount.getName(),
                game.getCurrentState(),
                gameId,
                game.getCurrentTurn(),
                game.getTurn(opponentAccount)
        ).sendTo(opponentSocketPair);
    }

    void sendScoreBoardList(Message message) {
        Account[] accounts = sortPlayersByScore();
        Message.makeScoreBoardListMessage(message.getPort(), accounts).sendTo(sockets.get(message.getPort()));
    }

    private ArrayList<GameInfo> getPausedGamesInfo(Account account) {
        ArrayList<GameInfo> gamesInfo = new ArrayList<>();
        for (Game game : pausedGamesById.values()) {
            if (game.haveAccount(account)) {
                gamesInfo.add(new GameInfo(game));
            }
        }
        return gamesInfo;
    }

    void sendPausedGames(Message message) {
        ArrayList<GameInfo> gamesInfo = getPausedGamesInfo(onlineAccounts.get(sockets.get(message.getPort()).getSocket()));
        Message.makePausedGamesListMessage(message.getPort(), gamesInfo).sendTo(sockets.get(message.getPort()));
    }

    void put(Message message) throws ClientException {
        Game game = onlineGamesById.get(message.getPutMessage().getGameId());

        if (game == null) {
            throw new ClientException("no such online game");
        }

        if (!game.canAct(onlineAccounts.get(sockets.get(message.getPort()).getSocket()))) {
            throw new ClientException("it's not your turn");
        }

        char currentTurn = game.getCurrentTurn();

        Position position = message.getPutMessage().getPosition();

        if (game.invalidPut(position)) {
            throw new ClientException("can't put here");
        }

        game.put(position);

        Account xAccount = game.getPlayersInfo().get('X').getAccount();
        SocketPair xSocketPair = accountSockets.get(xAccount.getName());

        Account oAccount = game.getPlayersInfo().get('O').getAccount();
        SocketPair oSocketPair = accountSockets.get(oAccount.getName());

        Message.makeMapUpdateMessage(
                xSocketPair.getSocket().getPort(),
                new Pair<>(position, MapUpdateType.valueOf(String.valueOf(currentTurn))),
                game.getCurrentTurn()
        ).sendTo(xSocketPair);

        Message.makeMapUpdateMessage(
                oSocketPair.getSocket().getPort(),
                new Pair<>(position, MapUpdateType.valueOf(String.valueOf(currentTurn))),
                game.getCurrentTurn()
        ).sendTo(oSocketPair);

        if (game.isFinished()) {
            onlineGames.remove(xAccount);
            onlineGames.remove(oAccount);
            onlineGamesById.remove(game.getId());

            switch (game.getWinnerSign()) {
                case 'X':
                    sendGameResultMessages(xSocketPair, GameState.WIN, oSocketPair, GameState.LOSE);
                    break;
                case 'O':
                    sendGameResultMessages(xSocketPair, GameState.LOSE, oSocketPair, GameState.WIN);
                    break;
                default:
                    sendGameResultMessages(xSocketPair, GameState.DRAW, oSocketPair, GameState.DRAW);
            }
        }
    }

    private void sendGameResultMessages(SocketPair xSocketPair, GameState xResult, SocketPair oSocketPair, GameState oResult) {
        Message.makeFinishGameMessage(
                xSocketPair.getSocket().getPort(), xResult
        ).sendTo(xSocketPair);

        Message.makeFinishGameMessage(
                oSocketPair.getSocket().getPort(), oResult
        ).sendTo(oSocketPair);
    }

    void undo(Message message) throws ClientException {
        Game game = onlineGamesById.get(message.getOtherGameActions().getGameId());

        if (game == null) {
            throw new ClientException("no such online game");
        }

        if (game.canAct(onlineAccounts.get(sockets.get(message.getPort()).getSocket()))) {
            throw new ClientException("it's your turn");
        }

        Position position = game.undo();

        game.getPlayersInfo().forEach(
                (character, playerInfo) -> {
                    SocketPair socketPair = accountSockets.get(playerInfo.getAccount().getName());
                    Message.makeMapUpdateMessage(
                            socketPair.getSocket().getPort(),
                            new Pair<>(position, MapUpdateType.EMPTY),
                            game.getCurrentTurn()
                    ).sendTo(socketPair);
                }
        );
    }

    void pause(Message message) throws ClientException {
        Game game = onlineGamesById.get(message.getOtherGameActions().getGameId());
        closeGame(game);
        game.getPlayersInfo().forEach(
                (character, playerInfo) -> {
                    SocketPair socketPair = accountSockets.get(playerInfo.getAccount().getName());
                    Message.makePauseMessage(
                            socketPair.getSocket().getPort(), game.getId()
                    ).sendTo(socketPair);
                }
        );
        pausedGamesById.put(game.getId(), game);
    }

    void stop(Message message) throws ClientException {
        Game game = onlineGamesById.get(message.getOtherGameActions().getGameId());
        game.getPlayersInfo().forEach(
                (character, playerInfo) -> {
                    SocketPair socketPair = accountSockets.get(playerInfo.getAccount().getName());
                    Message.makeStopMessage(
                            socketPair.getSocket().getPort(), game.getId()
                    ).sendTo(socketPair);
                }
        );
        closeGame(game);
    }

    private void closeGame(Game game) throws ClientException {
        if (game == null) {
            throw new ClientException("no such online game");
        }

        game.getPlayersInfo().forEach(
                (character, playerInfo) -> onlineGames.remove(playerInfo.getAccount())
        );
        onlineGamesById.remove(game.getId());
    }
}
