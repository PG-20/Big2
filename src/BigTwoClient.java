import java.io.*;
import java.net.*;
import java.util.ArrayList;


/**
 * The BigTwoClient class is used to model a Big Two card game on the client side.
 *
 * @author Pranav
 */
public class BigTwoClient implements CardGame, NetworkGame {
    private Deck deck;
    private ArrayList<CardGamePlayer> playerList;
    private ArrayList<Hand> handsOnTable;
    private int currentIndex;
    private int numOfPlayers;
    private int playerID;
    private String playerName;
    private String serverIP;
    private int serverPort;
    private Socket sock;
    private ObjectInputStream reader;
    private ObjectOutputStream oos;
    private BigTwoTable bigTwoTable;

    /**
     * A constructor for creating a Big Two card game. Creates 4
     * players and add them to the player list and also a ‘console’ (i.e., a
     * BigTwoConsole object) for providing the user interface
     */
    public BigTwoClient() {
        playerList = new ArrayList<>();
        for (int i = 0; i < 4; i++ ){
            playerList.add(new CardGamePlayer());
        }
        bigTwoTable = new BigTwoTable(this);
        this.makeConnection();
    }

    /**
     * Returns the playerID (index) of the local player.
     *
     * @return the playerID (index) of the local player
     */
    @Override
    public int getPlayerID() {
        return this.playerID;
    }

    /**
     * Sets the playerID (index) of the local player.
     *
     * @param playerID the playerID (index) of the local player.
     */
    @Override
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Returns the name of the local player.
     *
     * @return the name of the local player
     */
    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Sets the name of the local player.
     *
     * @param playerName the name of the local player
     */
    @Override
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns the IP address of the server.
     *
     * @return the IP address of the server
     */
    @Override
    public String getServerIP() {
        return this.serverIP;
    }

    /**
     * Sets the IP address of the server.
     *
     * @param serverIP the IP address of the server
     */
    @Override
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    /**
     * Returns the TCP port of the server.
     *
     * @return the TCP port of the server
     */
    @Override
    public int getServerPort() {
        return this.serverPort;
    }

    /**
     * Sets the TCP port of the server
     *
     * @param serverPort the TCP port of the server
     */
    @Override
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Makes a network connection to the server.
     */
    @Override
    public void makeConnection() {
        try {
            sock = new Socket("127.0.0.1", 2396);
            this.oos = new ObjectOutputStream(sock.getOutputStream());
            reader = new ObjectInputStream(sock.getInputStream());
            Thread receiveMessageThread = new Thread(new ServerHandler());
            receiveMessageThread.start();
            sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, getPlayerName()));
            sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * checks if the client is connected to the server
     *
     * @return returns status of connection
     */
    public boolean isConnected(){
        return sock.isConnected();
    }

    private class ServerHandler implements Runnable{
        @Override
        public void run() {
            try {
                Object message;
                while((message = reader.readObject()) != null){
                    parseMessage((GameMessage) message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Parses the specified message received from the server.
     *
     * @param message the specified message received from the server
     */
    @Override
    public void parseMessage(GameMessage message) {
        switch (message.getType()){
            case CardGameMessage.PLAYER_LIST:
                String[] names = (String[])message.getData();
                this.setPlayerID(message.getPlayerID());
                for (int i=0; i<playerList.size(); i++){
                    if (names[i] != null) {
                        playerList.get(i).setName(names[i]);
                        numOfPlayers+=1;
                    }
                }
                break;
            case CardGameMessage.JOIN:
                playerList.get(message.getPlayerID()).setName((String)message.getData());
                numOfPlayers+=1;
                break;
            case CardGameMessage.FULL:
                bigTwoTable.printMsg(">> Maximum player capacity reached!!\n");
                break;
            case CardGameMessage.QUIT:
                playerList.get(message.getPlayerID()).setName("");
                bigTwoTable.reset();
                sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                break;
            case CardGameMessage.READY:
                bigTwoTable.printMsg(" >> "+playerList.get(message.getPlayerID()).getName()+" is ready!\n");
                break;
            case CardGameMessage.START:
                start((BigTwoDeck)message.getData());
                break;
            case CardGameMessage.MOVE:
                checkMove(message.getPlayerID(), (int[])message.getData());
                break;
            case CardGameMessage.MSG:
                bigTwoTable.getChatArea().append(message.getData()+"\n");
                bigTwoTable.getChatArea().setCaretPosition(bigTwoTable.getChatArea().getDocument().getLength());
                break;
            default:
                System.out.println("Invalid message type");
        }
    }


    /**
     * Sends the specified message to the server.
     *
     * @param message the specified message to be sent the server
     */
    @Override
    public void sendMessage(GameMessage message) {
        try {
            this.oos.writeObject(message);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Returns the deck of cards being used in this card game.
     *
     * @return the deck of cards being used in this card game
     */
    @Override
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * Returns the number of players in this card game.
     *
     * @return the number of players in this card game
     */
    @Override
    public int getNumOfPlayers() {
        return this.numOfPlayers;
    }

    /**
     * Returns the index of the current player.
     *
     * @return the index of the current player
     */
    @Override
    public int getCurrentIdx() {
        return currentIndex;
    }

    /**
     * Makes a move by the player.
     *
     * @param playerID the playerID of the player who makes the move
     * @param cardIdx  indices of the cards selected by the player
     */
    @Override
    public void makeMove(int playerID, int[] cardIdx) {
        this.sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
    }

    /**
     * Checks the move made by the player.
     *
     * @param playerID the playerID of the player who makes the move
     * @param cardIdx  indices of the cards selected by the player
     */
    @Override
    public void checkMove(int playerID, int[] cardIdx) {
        CardGamePlayer currentPlayer = playerList.get(playerID);
        boolean firstMove = this.getHandsOnTable().isEmpty();
        Hand lastHand = firstMove ? null : this.handsOnTable.get(this.handsOnTable.size() - 1);
        if (cardIdx == null){
            this.bigTwoTable.printMsg(" >> {Pass}");
            if (firstMove || (lastHand.getPlayer() == currentPlayer)){
                this.bigTwoTable.printMsg(" <== Not a legal move!!!\n");
            }
            else {
                this.bigTwoTable.printMsg("\n");
                currentIndex = (getCurrentIdx()+1)%4;
            }
        } else {
            Hand validHand = BigTwoClient.composeHand(currentPlayer, currentPlayer.play(cardIdx));
            if (validHand == null) {
                this.bigTwoTable.printMsg(" >> Not a legal move!!!\n");
            } else {
                this.bigTwoTable.printMsg(" >> {" + validHand.getType() + "} ");
                this.bigTwoTable.printMsg(validHand.toString());
                if (
                        (firstMove && !validHand.contains(new BigTwoCard(0, 2)))
                        || ((!firstMove && lastHand.getPlayer() != currentPlayer)
                            && (!(validHand.size() == lastHand.size())
                        || !(validHand.beats(lastHand))))

                ) {
                    this.bigTwoTable.printMsg(" <== Not a legal move!!!\n");
                } else {
                    this.bigTwoTable.printMsg("\n");
                    this.handsOnTable.add(validHand);
                    currentPlayer.removeCards(validHand);
                    this.bigTwoTable.removeImagesFromPlayer(cardIdx);
                    if (this.endOfGame()) {
                        bigTwoTable.repaint();
                        this.bigTwoTable.endGame();
                    } else {
                        currentIndex = (getCurrentIdx()+1)%4;
                    }
                }
                bigTwoTable.resetSelected();
            }
        }
        bigTwoTable.repaint();
    }


    /**
     * Checks for end of game.
     *
     * @return true if the game ends; false otherwise
     */
    @Override
    public boolean endOfGame() {
        return playerList.get(this.currentIndex).getNumOfCards() == 0;
    }

    /**
     * returns the playerList variable
     *
     * @return playerList variable
     */
    public ArrayList<CardGamePlayer> getPlayerList() {
        return playerList;
    }

    /**
     * a method for retrieving the list of hands played
     * on the table
     *
     * @return handsOnTable private variable
     */
    public ArrayList<Hand> getHandsOnTable() {
        return handsOnTable;
    }

    /**
     * Starts the card game.
     *
     * @param deck the deck of (shuffled) cards to be used in this game
     */
    @Override
    public void start(Deck deck){
        this.deck = deck;
        handsOnTable = new ArrayList<>();
        CardGamePlayer currentPlayer;
        for (int i=0; i<4;i++){
            currentPlayer = playerList.get(i);
            currentPlayer.removeAllCards();
            for (int j =0 ; j <13; j++){
                Card currentCard = deck.removeCard(0);
                if (currentCard.equals(new BigTwoCard(0,2))) {
                    this.currentIndex = i;
                }
                currentPlayer.addCard(currentCard);
            }
            currentPlayer.sortCardsInHand();
        }
        this.bigTwoTable.setActivePlayer(getPlayerID());
        bigTwoTable.go();
        bigTwoTable.printMsg(" >> Game started\n");

    }

    /**
     * a method for returning a valid hand from
     * the specified list of cards of the player
     *
     * @param player
     *              current player
     *
     * @param cards
     *              list of cards to build hand with
     *
     * @return Hand object (will be polymorphed with actual type being one of the valid Hand types)
     */
    public static Hand composeHand(CardGamePlayer player, CardList cards){
        Hand newHand;
        switch (cards.size()){
            case 1:
                newHand = new Single(player, cards);
                return newHand.isValid() ? newHand : null;
            case 2:
                newHand = new Pair(player, cards);
                return  newHand.isValid() ? newHand : null;
            case 3:
                newHand = new Triple(player, cards);
                return  newHand.isValid() ? newHand : null;
            case 5:
                newHand = new StraightFlush(player, cards);
                if (newHand.isValid()) return newHand;
                newHand = new Quad(player, cards);
                if (newHand.isValid()) return newHand;
                newHand = new FullHouse(player, cards);
                if (newHand.isValid()) return newHand;
                newHand = new Flush(player, cards);
                if (newHand.isValid()) return newHand;
                newHand = new Straight(player, cards);
                if (newHand.isValid()) return newHand;
                return null;
            default:
                return null;

        }
    }

    /**
     * a method for starting a Big Two card game. Creates a Big Two
     * card game, create and shuffle a deck of cards, and start the game with
     * the deck of cards.
     *
     * @param args
     *            (unused)
     */
    public static void main(String[] args){
        BigTwoClient bigTwoClient = new BigTwoClient();
    }
}