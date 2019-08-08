import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class BigTwoTable implements CardGameTable {
    private CardGame game;
    private boolean[] selected = new boolean[13];
    private int activePlayer;
    private JFrame frame;
    private JPanel bigTwoPanel;
    private JButton playButton;
    private JButton passButton;
    private JTextArea msgArea;
    private JTextArea chatArea;
    private JTextField messageField;
    private ImageIcon[][] cardImages;
    private ImageIcon cardBackImage;
    private ImageIcon[] avatars;
    private PlayerPanel[] playerPanels;
    private boolean disabled = false;
    private Image backgroundImage;

    private static final String[] avatarsName = new String[]{"wolverine", "captain", "ironman", "deadpool"};
    private static final char[] suits = new char[]{'d', 'c', 'h', 's'};
    private static final char[] ranks = new char[]{'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};

    /**
     * Used to create an instance of the BigTwoTable class
     * Makes a frame and panels and stores them in instance variables of the instance
     *
     * @param game
     *            The game for which the table instance is being created
     */
    public BigTwoTable(CardGame game) {
        this.game = game;
        cardImages = new ImageIcon[4][13];
        avatars = new ImageIcon[4];
        String cardImagesPath = "images/cards/";
        cardBackImage = new ImageIcon(new ImageIcon(cardImagesPath + "b.gif").getImage().getScaledInstance(-1, 140, Image.SCALE_SMOOTH));
        backgroundImage = new ImageIcon(new ImageIcon("images/bgimg.jpg").getImage().getScaledInstance(1000,-1, Image.SCALE_SMOOTH)).getImage();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                cardImages[i][j] = new ImageIcon(new ImageIcon(cardImagesPath + ranks[j] + suits[i] + ".gif").getImage().getScaledInstance(-1, 140, Image.SCALE_SMOOTH));
            }
            avatars[i] = new ImageIcon(new ImageIcon("images/" + avatarsName[i] + ".png").getImage().getScaledInstance(130, -1, Image.SCALE_SMOOTH));
        }

        frame = new JFrame("Big Two Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        bigTwoPanel = new JPanel();
        bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.Y_AXIS));

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setFont(new Font("SansSerif", Font.PLAIN, 22));
        JMenuItem quit = new JMenuItem("Quit");
        JMenuItem restart = new JMenuItem("Connect");
        quit.setFont(new Font("SansSerif", Font.PLAIN, 22));
        restart.setFont(new Font("SansSerif", Font.PLAIN, 22));
        quit.addActionListener(new QuitItemListener());
        restart.addActionListener(new ConnectItemListener());
        gameMenu.add(quit);
        gameMenu.add(restart);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        playerPanels = new PlayerPanel[5];
        for (int i = 0; i < 5; i++) {
            PlayerPanel playerPanel = new PlayerPanel();
            playerPanels[i] = playerPanel;
            bigTwoPanel.add(playerPanel);
        }

        JPanel buttonPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JPanel messagePanel = new JPanel();

        JLabel messageLabel = new JLabel("Message: ");
        messageField = new JTextField(20);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        messageField.setFont(new Font("SansSerif", Font.BOLD, 20));
        messageField.addActionListener(new MessageListener());
        messagePanel.add(messageLabel);
        messagePanel.add(messageField);
        bottomPanel.add(buttonPanel);
        bottomPanel.add(messagePanel);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.lightGray);
        messagePanel.setBackground(Color.lightGray);

        playButton = new JButton("Play");
        playButton.setFont(new Font("SansSerif", Font.BOLD, 25));
        playButton.addActionListener(new PlayButtonListener());
        playButton.setEnabled(false);

        passButton = new JButton("Pass");
        passButton.setFont(new Font("SansSerif", Font.BOLD, 25));
        passButton.addActionListener(new PassButtonListener());
        passButton.setEnabled(false);

        buttonPanel.add(passButton);
        buttonPanel.add(playButton);
        System.out.println(buttonPanel.getSize());
        frame.add(bottomPanel, BorderLayout.SOUTH);

        msgArea = new JTextArea(1, 53);
        msgArea.setEditable(false);
        msgArea.setBackground(Color.black);
        msgArea.setFont(new Font("SansSerif", Font.BOLD, 20));
        msgArea.setForeground(Color.white);
        msgArea.setLineWrap(true);

        JScrollPane scrollableMessageArea = new JScrollPane(msgArea);
        scrollableMessageArea.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollableMessageArea.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        chatArea = new JTextArea(1, 53);
        chatArea.setEditable(false);
        chatArea.setBackground(Color.black);
        chatArea.setFont(new Font("SansSerif", Font.BOLD, 20));
        chatArea.setForeground(Color.white);
        chatArea.setLineWrap(true);

        JScrollPane scrollableChatArea = new JScrollPane(chatArea);
        scrollableChatArea.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollableChatArea.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        JPanel textAreas = new JPanel();
        textAreas.setLayout(new BoxLayout(textAreas, BoxLayout.Y_AXIS));
        textAreas.add(scrollableMessageArea);
        textAreas.add(scrollableChatArea);

        frame.add(textAreas, BorderLayout.EAST);
        frame.add(bigTwoPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        ((BigTwoClient)game).setPlayerName(JOptionPane.showInputDialog("What is your name?"));
    }

    /**
     * Used to setup the game
     */
    public void go() {
        for (int i = 0; i < 4; i++) {
            PlayerPanel playerPanel = playerPanels[i];
            playerPanel.setEnabled(i == activePlayer);
            playerPanel.initialise(i);
        }

        passButton.setEnabled(game.getCurrentIdx() == activePlayer);
        playButton.setEnabled(game.getCurrentIdx() == activePlayer);

        PlayerPanel lastHandPanel = playerPanels[4];
        lastHandPanel.setEnabled(false);
        lastHandPanel.initialise(4);
    }

    private class PlayButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getSelected() != null) {
                game.makeMove(activePlayer, getSelected());
            } else {
                printMsg(" >> Illegal move! Select a card or press Pass\n");
            }
        }
    }

    private class PassButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getSelected() != null)
                resetSelected();
            game.makeMove(activePlayer, getSelected());
        }
    }

    private class QuitItemListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class ConnectItemListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!((BigTwoClient)game).isConnected()) {
                ((BigTwoClient) game).makeConnection();
            } else {
                JOptionPane.showMessageDialog(frame, "Already connected to the server!!");
            }
        }
    }

    private class MessageListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            ((BigTwoClient)game).sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, messageField.getText()));
            messageField.setText("");
        }
    }

    private class PlayerPanel extends JPanel implements MouseListener {

        private ArrayList<CardIcon> playerCardImages;
        private int playerID = -1;
        private static final int Y_UNSELECTED = 18;
        private static final int Y_SELECTED = 3;

        public PlayerPanel() {
            addMouseListener(this);
        }

        public void emptyPanel(){
            this.playerID = -1;
            this.playerCardImages = null;
            this.repaint();
        }

        public void initialise(int playerID) {
            this.playerID = playerID;
            this.playerCardImages = new ArrayList<>();

            if (this.playerID != 4) {
                CardGamePlayer player = game.getPlayerList().get(playerID);
                CardList cardsInHand = player.getCardsInHand();
                for (int k = 0; k < cardsInHand.size(); k++) {
                    Card card = cardsInHand.getCard(k);
                    CardIcon cardIcon = new CardIcon(cardImages[card.getSuit()][card.getRank()], k);
                    playerCardImages.add(cardIcon);
                }
            }
            this.repaint();
        }

        public void deleteCards(int[] cardPositions) {
            for (int i = 0; i < cardPositions.length; i++) {
                final int k = i;
                playerCardImages.removeIf((CardIcon x) -> x.getPosition() == cardPositions[k]);
            }
        }

        public void updatePosition() {
            for (int i = 0; i < playerCardImages.size(); i++) {
                playerCardImages.get(i).setPosition(i);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, null);
            if (this.playerID == 4) {
                g.drawLine(0, 0, 1000, 0);
                ArrayList<Hand> handsOnTable = game.getHandsOnTable();
                Hand lastHand = handsOnTable.isEmpty() ? null : handsOnTable.get(handsOnTable.size() - 1);
                if (lastHand == null) {
                    g.setFont(new Font("SansSerif", Font.BOLD, 28));
                    g.drawString("Table Empty", this.getWidth() / 2 - 85, this.getHeight() / 2);
                } else {
                    g.setFont(new Font("SansSerif", Font.BOLD, 20));
                    g.setColor(Color.WHITE);
                    g.drawString("Last Hand (played by " + lastHand.getPlayer().getName() + ")", 30, 22);
                    Point origin = new Point(30, 30);
                    for (int p = 0; p < lastHand.size(); p++, origin.x += 120) {
                        Card card = lastHand.getCard(p);
                        CardIcon cardIcon = new CardIcon(cardImages[card.getSuit()][card.getRank()], 0);
                        cardIcon.moveTo(origin.x, origin.y);
                        cardIcon.draw(g, this);
                    }
                }
            } else if (playerID != -1){
                if (playerID == game.getCurrentIdx()) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.WHITE);
                }
                ImageIcon playerAvatar = avatars[playerID];
                playerAvatar.paintIcon(this, g, 10, 45);
                g.setFont(new Font("SansSerif", Font.BOLD, 22));
                g.drawString(game.getPlayerList().get(playerID).getName(), 33, 30);
                g.setColor(Color.black);

                Point origin = new Point(200, Y_UNSELECTED);
                for (int k = 0; k < playerCardImages.size(); k++, origin.x += 35) {
                    if (activePlayer == this.playerID) {
                        CardIcon card = playerCardImages.get(k);
                        card.moveTo(origin.x, selected[k] ? Y_SELECTED : Y_UNSELECTED);
                        card.draw(g, this);
                    } else {
                        ImageIcon card = cardBackImage;
                        card.paintIcon(this, g, origin.x, origin.y);
                    }
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!disabled) {
                int xOfPointClicked = e.getX();
                int yOfPointClicked = e.getY();

                for (int i = playerCardImages.size() - 1; i >= 0; i--) {
                    CardIcon possibleCard = playerCardImages.get(i);
                    if (possibleCard.contains(xOfPointClicked, yOfPointClicked)) {
                        if (possibleCard.isClicked()) {
                            possibleCard.moveTo(possibleCard.getX(), Y_UNSELECTED);
                            possibleCard.setClicked(false);
                            selected[possibleCard.getPosition()] = false;
                        } else {
                            possibleCard.moveTo(possibleCard.getX(), Y_SELECTED);
                            possibleCard.setClicked(true);
                            selected[possibleCard.getPosition()] = true;
                        }
                        break;
                    }
                }
                this.repaint();
            }
        }

        /*Methods to be ignore*/
        public void mousePressed(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
    }

    /**
     * Sets the index of the active player (i.e., the current player).
     *
     * @param activePlayer an int value representing the index of the active player
     */
    @Override
    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }


    /**
     * Removes the images and updates positions of the cards played from the current player
     */
    public void removeImagesFromPlayer(int[] cardIdx) {
        playerPanels[game.getCurrentIdx()].deleteCards(cardIdx);
        playerPanels[game.getCurrentIdx()].updatePosition();
    }

    /**
     * Returns an array of indices of the cards selected.
     *
     * @return an array of indices of the cards selected
     */
    @Override
    public int[] getSelected() {
        int count = 0;
        for (int j = 0; j < this.selected.length; j++) {
            if (this.selected[j]) count++;
        }
        if (count > 0) {
            int[] input = new int[count];
            int ct = 0;
            for (int i = 0; i < this.selected.length; i++) {
                if (this.selected[i]) {
                    input[ct] = i;
                    ct++;
                }
            }
            return input;
        } else {
            return null;
        }
    }


    /**
     * Resets the list of selected cards to an empty list.
     */
    @Override
    public void resetSelected() {
        for (int i = 0; i < this.selected.length; i++) {
            this.selected[i] = false;
        }
    }

    /**
     * A getter for the chatArea component
     *
     * @return the chatArea component
     */
    public JTextArea getChatArea() {
        return chatArea;
    }

    /**
     * Repaints the GUI.
     */
    @Override
    public void repaint() {
        playerPanels[(game.getCurrentIdx() + 3) % 4].repaint();
        playerPanels[game.getCurrentIdx()].repaint();
        playerPanels[4].repaint();
        passButton.setEnabled(game.getCurrentIdx() == activePlayer);
        playButton.setEnabled(game.getCurrentIdx() == activePlayer);
    }

    /**
     * Prints the specified string to the message area of the card game table.
     *
     * @param msg the string to be printed to the message area of the card game
     *            table
     */
    @Override
    public void printMsg(String msg) {
        this.msgArea.append(msg);
        this.msgArea.setCaretPosition(msgArea.getDocument().getLength());
    }

    /**
     * Clears the message area of the card game table.
     */
    @Override
    public void clearMsgArea() {
        this.msgArea.setText("");
    }

    /**
     * Resets the GUI.
     */
    @Override
    public void reset() {
        this.clearMsgArea();
        this.resetSelected();
        for (PlayerPanel p : playerPanels) {
            p.emptyPanel();
        }
        passButton.setEnabled(false);
        playButton.setEnabled(false);
    }

    /**
     * Enables user interactions.
     */
    @Override
    public void enable() {
        this.disabled = false;
        playButton.setEnabled(true);
        passButton.setEnabled(true);
    }

    /**
     * Disables user interactions.
     */
    @Override
    public void disable() {
        this.disabled = true;
        playButton.setEnabled(false);
        passButton.setEnabled(false);
    }

    /**
     * Displays the Game ending message
     */
    public void endGame() {
        String message = "Game ends\n";
        for (CardGamePlayer player : this.game.getPlayerList()) {
            message+= player.getNumOfCards() == 0
                    ? player.getName() + " wins the game.\n"
                    : player.getName() + " has " + player.getNumOfCards() + " cards in hand.\n";
        }
        JOptionPane.showMessageDialog(frame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        this.reset();
        ((BigTwoClient)this.game).sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
    }
}
