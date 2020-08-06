package NimGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class HostGame {
    public int turn = 1;
    public int gameOver = 0;
    public int pos;
    SocketChannel socketChannel;
    Server server = new Server();

    void startClient() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    socketChannel = SocketChannel.open();
                    socketChannel.configureBlocking(true);
                    socketChannel.connect(new InetSocketAddress("localhost", 5001));
                    SwingUtilities.invokeLater(() -> displayText(" [접속 완료]"));
                } catch (Exception e) {
                    if (socketChannel.isOpen()) {
                        stopClient();
                    }
                    return;
                }
                receive();
            }
        };
        thread.start();
    }

    void stopClient() {
        try {
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (Exception e) {
        }
    }

    void receive() {
        while (true) {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(100);

                int readByteCount = socketChannel.read(byteBuffer);

                if (readByteCount == -1) {
                    throw new IOException();
                }

                byteBuffer.flip();
                Charset charset = Charset.forName("UTF-8");
                String data = charset.decode(byteBuffer).toString();
                receiveData(data);
            } catch (Exception e) {
                stopClient();
                break;
            }
        }
    }

    void send(String data) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Charset charset = Charset.forName("utf-8");
                    ByteBuffer byteBuffer = charset.encode(data);
                    socketChannel.write(byteBuffer);
                } catch (Exception e) {
                    stopClient();
                }
            }
        };
        thread.start();
    }

    private JFrame gameScreen;
    private JPanel[] panels;
    private JLabel gameTitle;
    private JTextArea[] lines;
    private JTextArea messageArea;
    private JScrollPane messagePane;
    private JButton[] linesButton;
    private JButton endTurnButton, giveUpButton, gameStart;
    private int log = 0;

    public HostGame() {
        gameScreen = new JFrame("Nim Game");
        panels = new JPanel[20];
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
        }
        gameTitle = new JLabel("NIM GAME");
        gameTitle.setFont(new Font("dialog", Font.BOLD, 30));
        lines = new JTextArea[6];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new JTextArea(1, 17);
            lines[i].setEditable(false);
            lines[i].setBackground(Color.WHITE);
        }
        linesButton = new JButton[6];
        for (int i = 0; i < linesButton.length; i++) {
            linesButton[i] = new JButton("Line" + (i + 1) + " 구슬 빼기");
        }
        messageArea = new JTextArea(10, 27);
        messageArea.setEditable(false);
        messagePane = new JScrollPane(messageArea);
        messagePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        endTurnButton = new JButton("턴 넘기기");
        giveUpButton = new JButton("포기하기");
        gameStart = new JButton("게임 시작");


        gameScreen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        linesButton[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send(Data.LINE1);
            }
        });
        linesButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send(Data.LINE2);
            }
        });
        linesButton[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send(Data.LINE3);
            }
        });
        linesButton[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send(Data.LINE4);
            }
        });
        linesButton[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send(Data.LINE5);
            }
        });
        linesButton[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send(Data.LINE6);
            }
        });
        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send(Data.ENDTURN);
            }
        });
        giveUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameOver == 0) {
                    send(Data.GIVEUP);
                } else {
                    stopClient();
                    server.stopServer();
                    gameScreen.dispose();
                    new Main();
                }
            }
        });
        gameStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newLine();

                String data = String.format("%02d", Beads.line[0]) +
                        String.format("%02d", Beads.line[1]) +
                        String.format("%02d", Beads.line[2]) +
                        String.format("%02d", Beads.line[3]) +
                        String.format("%02d", Beads.line[4]) +
                        String.format("%02d", Beads.line[5]);

                send(data);
            }
        });


        panels[1].setLayout(new FlowLayout(FlowLayout.CENTER));
        panels[1].add(gameTitle);

        panels[2].setLayout(new FlowLayout(FlowLayout.CENTER));
        panels[2].add(lines[0]);
        panels[2].add(linesButton[0]);

        panels[3].setLayout(new FlowLayout(FlowLayout.CENTER));
        panels[3].add(lines[1]);
        panels[3].add(linesButton[1]);

        panels[4].setLayout(new FlowLayout(FlowLayout.CENTER));
        panels[4].add(lines[2]);
        panels[4].add(linesButton[2]);

        panels[5].setLayout(new FlowLayout(FlowLayout.CENTER));
        panels[5].add(lines[3]);
        panels[5].add(linesButton[3]);

        panels[6].setLayout(new FlowLayout(FlowLayout.CENTER));
        panels[6].add(lines[4]);
        panels[6].add(linesButton[4]);

        panels[7].setLayout(new FlowLayout(FlowLayout.CENTER));
        panels[7].add(lines[5]);
        panels[7].add(linesButton[5]);

        panels[8].setLayout(new FlowLayout(FlowLayout.CENTER));
        panels[8].add(messagePane);

        panels[9].setLayout(new FlowLayout(FlowLayout.CENTER));
        panels[9].add(gameStart);
        panels[9].add(endTurnButton);
        panels[9].add(giveUpButton);

        panels[10].setLayout(new BorderLayout());
        panels[10].add(panels[2], "North");
        panels[10].add(panels[3], "Center");

        panels[11].setLayout(new BorderLayout());
        panels[11].add(panels[4], "North");
        panels[11].add(panels[5], "Center");

        panels[12].setLayout(new BorderLayout());
        panels[12].add(panels[6], "North");
        panels[12].add(panels[7], "Center");

        panels[13].setLayout(new BorderLayout());
        panels[13].add(panels[10], "North");
        panels[13].add(panels[11], "Center");

        panels[14].setLayout(new BorderLayout());
        panels[14].add(panels[13], "North");
        panels[14].add(panels[12], "Center");

        panels[15].setLayout(new BorderLayout());
        panels[15].add(panels[1], "North");
        panels[15].add(panels[14], "Center");
        panels[15].add(panels[8], "South");

        panels[0].setLayout(new BorderLayout());
        panels[0].add(panels[15], "North");
        panels[0].add(panels[9], "South");
        panels[0].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gameScreen.add(panels[0]);
        gameScreen.pack();
        gameScreen.setResizable(false);
        gameScreen.setLocation(((gameScreen.getToolkit().getScreenSize()).width - gameScreen.getWidth()) / 2,
                ((gameScreen.getToolkit().getScreenSize()).height - gameScreen.getHeight()) / 2);
        gameScreen.setVisible(true);

        server.startServer();
        startClient();

        setting();
    }

    public void setting() {
        for (int i = 0; i < linesButton.length; i++) {
            linesButton[i].setEnabled(false);
        }
        endTurnButton.setEnabled(false);
        giveUpButton.setEnabled(false);
    }

    public void newGame() {
        beadsSet();
        for (int i = 0; i < linesButton.length; i++) {
            linesButton[i].setEnabled(true);
        }
        endTurnButton.setEnabled(true);
        giveUpButton.setEnabled(true);
        gameStart.setEnabled(false);
    }


    public void newLine() {
        for (int i = 0; i < Beads.line.length; i++) {
            Beads.line[i] = (int) (Math.random() * 15 + 1);
        }
    }

    public void beadsSet() {
        emptyLineCheck();
        for (int i = 0; i < 6; i++) {
            lines[i].setText("");
            for (int j = 0; j < Beads.line[i]; j++) {
                lines[i].append("●");
            }
        }
    }

    public void emptyLineCheck() {
        for (int i = 0; i < Beads.line.length; i++) {
            if (Beads.line[i] == 0) {
                linesButton[i].setEnabled(false);
            }
        }
    }

    public void displayText(String text) {
        messageArea.append(text + "\n");
        int pos = messageArea.getText().length();
        messageArea.setCaretPosition(pos);
        messageArea.requestFocus();
    }

    public void receiveData(String data) {
        if (turn == 1) {
            switch (data) {
                case Data.LINE1:
                    endTurnButton.setEnabled(true);
                    linesButton[1].setEnabled(false);
                    linesButton[2].setEnabled(false);
                    linesButton[3].setEnabled(false);
                    linesButton[4].setEnabled(false);
                    linesButton[5].setEnabled(false);

                    linesButton[0].setEnabled(false);

                    Beads.line[0] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [HOST] : Line1의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    break;
                case Data.LINE2:
                    endTurnButton.setEnabled(true);
                    linesButton[0].setEnabled(false);
                    linesButton[2].setEnabled(false);
                    linesButton[3].setEnabled(false);
                    linesButton[4].setEnabled(false);
                    linesButton[5].setEnabled(false);

                    linesButton[1].setEnabled(false);

                    Beads.line[1] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [HOST] : Line2의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    break;
                case Data.LINE3:
                    endTurnButton.setEnabled(true);
                    linesButton[0].setEnabled(false);
                    linesButton[1].setEnabled(false);
                    linesButton[3].setEnabled(false);
                    linesButton[4].setEnabled(false);
                    linesButton[5].setEnabled(false);

                    linesButton[2].setEnabled(false);

                    Beads.line[2] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [HOST] : Line3의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    break;
                case Data.LINE4:
                    endTurnButton.setEnabled(true);
                    linesButton[0].setEnabled(false);
                    linesButton[1].setEnabled(false);
                    linesButton[2].setEnabled(false);
                    linesButton[4].setEnabled(false);
                    linesButton[5].setEnabled(false);

                    linesButton[3].setEnabled(false);

                    Beads.line[3] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [HOST] : Line4의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    break;
                case Data.LINE5:
                    endTurnButton.setEnabled(true);
                    linesButton[0].setEnabled(false);
                    linesButton[1].setEnabled(false);
                    linesButton[2].setEnabled(false);
                    linesButton[3].setEnabled(false);
                    linesButton[5].setEnabled(false);

                    linesButton[4].setEnabled(false);

                    Beads.line[4] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [HOST] : Line5의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    break;
                case Data.LINE6:
                    endTurnButton.setEnabled(true);
                    linesButton[0].setEnabled(false);
                    linesButton[1].setEnabled(false);
                    linesButton[2].setEnabled(false);
                    linesButton[3].setEnabled(false);
                    linesButton[4].setEnabled(false);

                    linesButton[5].setEnabled(false);

                    Beads.line[5] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [HOST] : Line6의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    break;
                case Data.ENDTURN:
                    for (int i = 0; i < linesButton.length; i++) {
                        linesButton[i].setEnabled(false);
                    }
                    endTurnButton.setEnabled(false);
                    giveUpButton.setEnabled(false);
                    log = 0;
                    turn = 0;
                    break;
                case Data.GAMESTART:
                    newGame();
                    break;
                case Data.RELINE1:
                    if (Beads.line[0] != 0) {
                        linesButton[0].setEnabled(true);
                    }
                    break;
                case Data.RELINE2:
                    if (Beads.line[1] != 0) {
                        linesButton[1].setEnabled(true);
                    }
                    break;
                case Data.RELINE3:
                    if (Beads.line[2] != 0) {
                        linesButton[2].setEnabled(true);
                    }
                    break;
                case Data.RELINE4:
                    if (Beads.line[3] != 0) {
                        linesButton[3].setEnabled(true);
                    }
                    break;
                case Data.RELINE5:
                    if (Beads.line[4] != 0) {
                        linesButton[4].setEnabled(true);
                    }
                    break;
                case Data.RELINE6:
                    if (Beads.line[5] != 0) {
                        linesButton[5].setEnabled(true);
                    }
                    break;
                case Data.REGIVEUP:
                    stopClient();
                    server.stopServer();
                    gameScreen.dispose();
                    new Main();
                    break;
            }
        } else {
            switch (data) {
                case Data.LINE1:
                    Beads.line[0] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [CLIENT] : Line1의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    int pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    if (gameOver == 0) {
                        send(Data.RELINE1);
                    }
                    break;
                case Data.LINE2:
                    Beads.line[1] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [CLIENT] : Line2의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    if (gameOver == 0) {
                        send(Data.RELINE2);
                    }
                    break;
                case Data.LINE3:
                    Beads.line[2] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [CLIENT] : Line3의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    if (gameOver == 0) {
                        send(Data.RELINE3);
                    }
                    break;
                case Data.LINE4:
                    Beads.line[3] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [CLIENT] : Line4의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    if (gameOver == 0) {
                        send(Data.RELINE4);
                    }
                    break;
                case Data.LINE5:
                    Beads.line[4] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [CLIENT] : Line5의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    if (gameOver == 0) {
                        send(Data.RELINE5);
                    }
                    break;
                case Data.LINE6:
                    Beads.line[5] -= 1;
                    beadsSet();
                    log += 1;
                    messageArea.append(" [CLIENT] : Line6의 구슬 " + log + "개 제거.\n");
                    winCheck();
                    pos = messageArea.getText().length();
                    messageArea.setCaretPosition(pos);
                    messageArea.requestFocus();
                    if (gameOver == 0) {
                        send(Data.RELINE6);
                    }
                    break;
                case Data.ENDTURN:
                    for (int i = 0; i < linesButton.length; i++) {
                        if (Beads.line[i] != 0) {
                            linesButton[i].setEnabled(true);
                        }
                    }
                    giveUpButton.setEnabled(true);
                    log = 0;
                    turn = 1;
                    break;
                case Data.GIVEUP:
                    messageArea.append(" 상대가 포기했습니다.");
                    giveUpButton.setText("돌아가기");
                    giveUpButton.setEnabled(true);
                    gameOver = 1;
                    send(Data.REGIVEUP);
                    break;
            }
        }
    }

    public void winCheck() {
        int total = 0;
        for (int i = 0; i < Beads.line.length; i++) {
            total += Beads.line[i];
        }
        if (total == 1) {
            gameOver = 1;
            if (turn == 1) {
                for (int i = 0; i < linesButton.length; i++) {
                    linesButton[i].setEnabled(false);
                }
                endTurnButton.setEnabled(false);
                messageArea.append(" Victory!");
                giveUpButton.setText("돌아가기");
            } else {
                for (int i = 0; i < linesButton.length; i++) {
                    linesButton[i].setEnabled(false);
                }
                endTurnButton.setEnabled(false);
                messageArea.append(" Defeat...");
                giveUpButton.setText("돌아가기");
                giveUpButton.setEnabled(true);
            }
        }
    }

}

