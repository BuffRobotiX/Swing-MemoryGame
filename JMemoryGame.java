// 
// David Buff
// 
// Description: 
//      A simple memory game utilizing a grid of buttons.
//

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

class JMemoryGame implements ActionListener {
    JFrame frame;
    ImageIcon[] icons;
    JButton[] buttons;
    ImageIcon[] hiddenIcons;
    int numClicks;
    ImageIcon question;
    ImageIcon tileFlip;
    boolean[] completed;
    int firstClicked;

    JMemoryGame() {
    	frame = new JFrame("JMemoryGame");
    	frame.setSize(500, 500);
    	frame.setLocationRelativeTo(null);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4,4));

        JMenuBar menu = new JMenuBar();
        JMenuItem quitMenu = new JMenuItem("Give Up?");
        JMenuItem resetMenu = new JMenuItem("Reset");
        JMenuItem exitMenu = new JMenuItem("Exit");
        JMenuItem aboutMenu = new JMenuItem("About");
        menu.add(quitMenu);
        menu.add(resetMenu);
        menu.add(exitMenu);
        menu.add(aboutMenu);

        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                switch (ae.getActionCommand()) {
                    case "Give Up?": showTiles();
                    break;
                    case "Reset": newGame();
                    break;
                    case "Exit": System.exit(0);
                    break;
                    case "About": JOptionPane.showMessageDialog(frame, "(C) David F. Buff 2014", "About JMemoryGame", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        };

        quitMenu.addActionListener(menuListener);
        resetMenu.addActionListener(menuListener);
        exitMenu.addActionListener(menuListener);
        aboutMenu.addActionListener(menuListener);

        question = new ImageIcon("question.png");
        tileFlip = new ImageIcon("tileFlip.gif");

        icons = new ImageIcon[8];
        for (int i = 0; i < 8; i++) {
            icons[i] = new ImageIcon("fox" + Integer.toString(i+1) + ".png");
        }

        hiddenIcons = new ImageIcon[16];

        buttons = new JButton[16];
        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton(question);
            buttons[i].setContentAreaFilled(false);
            buttons[i].setRolloverIcon(tileFlip);
            buttons[i].setActionCommand(Integer.toString(i));
            buttons[i].addActionListener(this);
            frame.add(buttons[i]);
        }

        completed = new boolean[16];
        numClicks = 0;
        firstClicked = -1;

        newGame();

        frame.setJMenuBar(menu);
    	frame.setVisible(true);
    }

    private void newGame() {
        ArrayList<Integer> availablePlaces = new ArrayList<Integer>();
        for (int i = 0; i < 16; i++)
            availablePlaces.add(i);

        Random rand = new Random();
        for (int i = 0; i < 8; i++) {
            int pos = rand.nextInt(availablePlaces.size());
            hiddenIcons[availablePlaces.get(pos)] = icons[i];
            availablePlaces.remove(pos);
            pos = rand.nextInt(availablePlaces.size());
            hiddenIcons[availablePlaces.get(pos)] = icons[i];
            availablePlaces.remove(pos);
        }
        for (int i = 0; i < 16; i++) {
            completed[i] = false;
        }
        clearTiles();
    }

    private void clearTiles() {
        for (int i = 0; i < 16; i++) {
            if (!completed[i]) {
                buttons[i].setIcon(question);
                buttons[i].setEnabled(true);
                buttons[i].setRolloverIcon(tileFlip);
                buttons[i].setBorderPainted(true);
            }
        }
    }

    private void showTiles() {
        for (int i = 0; i < 16; i++) {
            buttons[i].setIcon(hiddenIcons[i]);
            buttons[i].setRolloverIcon(hiddenIcons[i]);
            buttons[i].setBorderPainted(false);
            completed[i] = true;
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (numClicks < 2) {
            if (buttons[Integer.parseInt(ae.getActionCommand())].getIcon() == question) {
                buttons[Integer.parseInt(ae.getActionCommand())].setIcon(hiddenIcons[Integer.parseInt(ae.getActionCommand())]);
                buttons[Integer.parseInt(ae.getActionCommand())].setRolloverIcon(hiddenIcons[Integer.parseInt(ae.getActionCommand())]);
                buttons[Integer.parseInt(ae.getActionCommand())].setBorderPainted(false);
                numClicks++;
                if (numClicks == 1) {
                    firstClicked = Integer.parseInt(ae.getActionCommand());
                }
                else if (numClicks == 2) {
                    if (buttons[firstClicked].getIcon() == buttons[Integer.parseInt(ae.getActionCommand())].getIcon()) {
                        completed[firstClicked] = true;
                        completed[Integer.parseInt(ae.getActionCommand())] = true;
                    }
                }
            }
        }
        else {
            clearTiles();
            buttons[Integer.parseInt(ae.getActionCommand())].setIcon(hiddenIcons[Integer.parseInt(ae.getActionCommand())]);
            buttons[Integer.parseInt(ae.getActionCommand())].setRolloverIcon(hiddenIcons[Integer.parseInt(ae.getActionCommand())]);
            buttons[Integer.parseInt(ae.getActionCommand())].setBorderPainted(false);
            numClicks = 1;
            firstClicked = Integer.parseInt(ae.getActionCommand());
        }
    }

    public static void main(String args[]) { 
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JMemoryGame();
            }
        });
    }
}