package NimGame;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main {
    private JFrame mainScreen;
    private JPanel[] panels;
    private JLabel mainTitle;
    private JButton singleGameButton, hostGameButton, clientGameButton;
    public Main() {
        mainScreen = new JFrame("Nim Game");
        mainTitle = new JLabel("NIM GAME");
        mainTitle.setFont(new Font("dialog", Font.BOLD, 30));
        singleGameButton = new JButton("Single Game Start");
        hostGameButton = new JButton("Host Game");
        clientGameButton = new JButton("Client Game");
        panels = new JPanel[4];
        for(int i=0; i<panels.length; i++) {
            panels[i] = new JPanel();
        }

        panels[1].setLayout(new GridBagLayout());
        panels[1].add(mainTitle);

        panels[2].setLayout(new GridBagLayout());
        panels[2].add(singleGameButton);

        panels[3].setLayout(new GridBagLayout());
        panels[3].add(hostGameButton);
        panels[3].add(new JLabel(" "));
        panels[3].add(clientGameButton);

        panels[0].setLayout(new BorderLayout());
        panels[0].add(panels[1], "North");
        panels[0].add(panels[2], "Center");
        panels[0].add(panels[3], "South");
        panels[0].setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainScreen.add(panels[0]);
        mainScreen.pack();
        mainScreen.setResizable(false);
        mainScreen.setLocation(((mainScreen.getToolkit().getScreenSize()).width - mainScreen.getWidth())/2,
                ((mainScreen.getToolkit().getScreenSize()).height - mainScreen.getHeight())/2);
        mainScreen.setVisible(true);

        mainScreen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        singleGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SingleGame singleGame = new SingleGame();
                singleGame.newGame();
                mainScreen.dispose();
            }
        });
        hostGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HostGame hostGame = new HostGame();
                mainScreen.dispose();
            }
        });
        clientGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JoinGame joinGame = new JoinGame();
                mainScreen.dispose();

            }
        });
    }
    public static void main(String[] args) {
        new Main();
    }
}
