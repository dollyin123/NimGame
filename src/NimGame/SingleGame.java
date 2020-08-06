package NimGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SingleGame {
    private JFrame gameScreen;
    private JPanel[] panels;
    private JLabel gameTitle;
    private JTextArea[] lines;
    private JTextArea messageArea;
    private JScrollPane messagePane;
    private JButton[] linesButton;
    private JButton endTurnButton, giveUpButton;
    private Thread comThread;
    private int log = 0;
    private int pos;



    public SingleGame() {
        gameScreen = new JFrame("Nim Game");
        panels = new JPanel[20];
        for(int i=0; i<panels.length; i++) {
            panels[i] = new JPanel();
        }
        gameTitle = new JLabel("NIM GAME");
        gameTitle.setFont(new Font("dialog", Font.BOLD, 30));
        lines = new JTextArea[6];
        for(int i=0; i<lines.length; i++) {
            lines[i] = new JTextArea(1, 17);
            lines[i].setEditable(false);
            lines[i].setBackground(Color.WHITE);
        }
        linesButton = new JButton[6];
        for(int i=0; i<linesButton.length; i++) {
            linesButton[i] = new JButton("Line" + (i+1) + " 구슬 빼기");
        }
        messageArea = new JTextArea(10, 27);
        messageArea.setEditable(false);
        messagePane = new JScrollPane(messageArea);
        messagePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        endTurnButton = new JButton("턴 넘기기");
        giveUpButton = new JButton("포기하기");



        gameScreen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        linesButton[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTurnButton.setEnabled(true);
                linesButton[1].setEnabled(false);
                linesButton[2].setEnabled(false);
                linesButton[3].setEnabled(false);
                linesButton[4].setEnabled(false);
                linesButton[5].setEnabled(false);
                Beads.line[0] -= 1;
                beadsSet();
                log += 1;
                messageArea.append(" [Player1] : Line1의 구슬 " + log + "개 제거.\n");
                winCheck();
                pos = messageArea.getText().length();
                messageArea.setCaretPosition(pos);
                messageArea.requestFocus();
            }
        });
        linesButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTurnButton.setEnabled(true);
                linesButton[0].setEnabled(false);
                linesButton[2].setEnabled(false);
                linesButton[3].setEnabled(false);
                linesButton[4].setEnabled(false);
                linesButton[5].setEnabled(false);
                Beads.line[1] -= 1;
                beadsSet();
                log += 1;
                messageArea.append(" [Player1] : Line2의 구슬 " + log + "개 제거.\n");
                winCheck();
                pos = messageArea.getText().length();
                messageArea.setCaretPosition(pos);
                messageArea.requestFocus();
            }
        });
        linesButton[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTurnButton.setEnabled(true);
                linesButton[0].setEnabled(false);
                linesButton[1].setEnabled(false);
                linesButton[3].setEnabled(false);
                linesButton[4].setEnabled(false);
                linesButton[5].setEnabled(false);
                Beads.line[2] -= 1;
                beadsSet();
                log += 1;
                messageArea.append(" [Player1] : Line3의 구슬 " + log + "개 제거.\n");
                winCheck();
                pos = messageArea.getText().length();
                messageArea.setCaretPosition(pos);
                messageArea.requestFocus();
            }
        });
        linesButton[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTurnButton.setEnabled(true);
                linesButton[0].setEnabled(false);
                linesButton[1].setEnabled(false);
                linesButton[2].setEnabled(false);
                linesButton[4].setEnabled(false);
                linesButton[5].setEnabled(false);
                Beads.line[3] -= 1;
                beadsSet();
                log += 1;
                messageArea.append(" [Player1] : Line4의 구슬 " + log + "개 제거.\n");
                winCheck();
                pos = messageArea.getText().length();
                messageArea.setCaretPosition(pos);
                messageArea.requestFocus();
            }
        });
        linesButton[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTurnButton.setEnabled(true);
                linesButton[0].setEnabled(false);
                linesButton[1].setEnabled(false);
                linesButton[2].setEnabled(false);
                linesButton[3].setEnabled(false);
                linesButton[5].setEnabled(false);
                Beads.line[4] -= 1;
                beadsSet();
                log += 1;
                messageArea.append(" [Player1] : Line5의 구슬 " + log + "개 제거.\n");
                winCheck();
                pos = messageArea.getText().length();
                messageArea.setCaretPosition(pos);
                messageArea.requestFocus();
            }
        });
        linesButton[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTurnButton.setEnabled(true);
                linesButton[0].setEnabled(false);
                linesButton[1].setEnabled(false);
                linesButton[2].setEnabled(false);
                linesButton[3].setEnabled(false);
                linesButton[4].setEnabled(false);
                Beads.line[5] -= 1;
                beadsSet();
                log += 1;
                messageArea.append(" [Player1] : Line6의 구슬 " + log + "개 제거.\n");
                winCheck();
                pos = messageArea.getText().length();
                messageArea.setCaretPosition(pos);
                messageArea.requestFocus();
            }
        });

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0; i<linesButton.length; i++) {
                    linesButton[i].setEnabled(false);
                }
                endTurnButton.setEnabled(false);
                giveUpButton.setEnabled(false);
                log = 0;
                COM com = new COM();
                comThread = new Thread(com);
                comThread.setDaemon(true);
                comThread.start();
            }
        });
        giveUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameScreen.dispose();
                new Main();
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
        gameScreen.setLocation(((gameScreen.getToolkit().getScreenSize()).width - gameScreen.getWidth())/2,
                ((gameScreen.getToolkit().getScreenSize()).height - gameScreen.getHeight())/2);
        gameScreen.setVisible(true);

    }



    public void newGame() {
        newLine();
        beadsSet();
    }



    public void newLine() {
        for(int i=0; i<Beads.line.length; i++) {
            Beads.line[i] = (int) (Math.random()*15+1);
        }
    }

    public void beadsSet() {
        emptyLineCheck();
        for(int i=0; i<6; i++) {
            lines[i].setText("");
            for(int j=0; j<Beads.line[i]; j++) {
                lines[i].append("●");
            }
        }
    }

    public void emptyLineCheck() {
        for(int i=0; i<Beads.line.length; i++) {
            if(Beads.line[i] == 0) {
                linesButton[i].setEnabled(false);
            }
        }
    }



    class COM implements Runnable {
        private int[] copyLine = new int[6];
        private int[] tokenCnt = new int[4];
        private int[][] tokenSet = new int[6][4];
        private int largestSingleTokenIndex;
        private int targetLineIndex;
        private int defeatFlag;


        @Override
        public void run() {
            try {
                defeatFlag = 0;
                getToken();
                largestSingleTokenCheck();
                if(defeatFlag == 0) {
                    if(evenNumber()>1) {
                        setTargetLine();
                        int cnt = setRemoveBeadsCnt();
                        for(int i=0; i<cnt; i++) {
                            Thread.sleep(500);
                            Beads.line[targetLineIndex] -= 1;
                            beadsSet();
                            log += 1;
                            messageArea.append(" [COM] : Line" + (targetLineIndex+1) + "의 구슬 " + log + "개 제거.\n");
                            pos = messageArea.getText().length();
                            messageArea.setCaretPosition(pos);
                            messageArea.requestFocus();
                        }
                        Thread.sleep(500);
                    } else {
                        int aliveLineCheck = 0;
                        for(int i=0; i<Beads.line.length; i++) {
                            if(Beads.line[i]>0) {
                                aliveLineCheck += 1;
                            }
                        }
                        int max = 0;
                        int largestIndex = 0;
                        for(int i=0; i<Beads.line.length; i++) {
                            if(max < Beads.line[i]) {
                                max = Beads.line[i];
                                largestIndex = i;
                            }
                        }
                        int removeBeadsCnt = 0;
                        if(aliveLineCheck % 2 == 0) {
                            removeBeadsCnt = Beads.line[largestIndex];
                        } else {
                            removeBeadsCnt = Beads.line[largestIndex]-1;
                        }
                        for(int i=0; i<removeBeadsCnt; i++) {
                            Thread.sleep(500);
                            Beads.line[largestIndex] -= 1;
                            beadsSet();
                            log += 1;
                            messageArea.append(" [COM] : Line" + (targetLineIndex+1) + "의 구슬 " + log + "개 제거.\n");
                            pos = messageArea.getText().length();
                            messageArea.setCaretPosition(pos);
                            messageArea.requestFocus();
                        }
                        Thread.sleep(500);
                    }
                } else {
                    int max = 0;
                    int largestIndex = 0;
                    for(int i=0; i<Beads.line.length; i++) {
                        if(max < Beads.line[i]) {
                            max = Beads.line[i];
                            largestIndex = i;
                        }
                    }
                    int removeBeadsCnt = (int) (Beads.line[largestIndex]/3 + 1);
                    for(int i=0; i<removeBeadsCnt; i++) {
                        Thread.sleep(500);
                        Beads.line[largestIndex] -= 1;
                        beadsSet();
                        log += 1;
                        messageArea.append(" [COM] : Line" + (targetLineIndex+1) + "의 구슬 " + log + "개 제거.\n");
                        pos = messageArea.getText().length();
                        messageArea.setCaretPosition(pos);
                        messageArea.requestFocus();
                    }
                    Thread.sleep(500);
                }
                log = 0;
                for(int i=0; i<linesButton.length; i++) {
                    if(Beads.line[i] != 0) {
                        linesButton[i].setEnabled(true);
                    }
                }
                winCheck();
                giveUpButton.setEnabled(true);
                comThread.interrupt();
            } catch (Exception e) {
            } finally {}
        }



        public void getToken() {
            int[] newCopyLine = new int[6];
            for(int i=0; i<Beads.line.length; i++) {
                newCopyLine[i] = Beads.line[i];
            }
            for(int i=0; i<newCopyLine.length; i++) {
                for(int j=0; j<Beads.token.length; j++) {
                    tokenSet[i][j] = 0;
                    if(newCopyLine[i]>=Beads.token[j]) {
                        newCopyLine[i] -= Beads.token[j];
                        tokenCnt[j] += 1;
                        tokenSet[i][j] = Beads.token[j];
                    }
                }
            }
        }

        public void largestSingleTokenCheck() {
            int singleFlag = 0;
            int overOneLine = 0;
            for(int i=0; i<Beads.line.length; i++) {
                if(Beads.line[i]>1) {
                    overOneLine += 1;
                }
            }
            if(overOneLine>0) {
                for(int i=0; i<tokenCnt.length; i++) {
                    if(tokenCnt[i] % 2 != 0) {
                        largestSingleTokenIndex = i;
                        singleFlag = 1;
                        break;
                    }
                }
            }
            if(singleFlag == 0) {
                defeatAction();
            }
        }

        public void setTargetLine() {
            for(int i=0; i<tokenSet.length; i++) {
                if(tokenSet[i][largestSingleTokenIndex] != 0) {
                    targetLineIndex = i;
                    break;
                }
            }
        }

        public void defeatAction() {
            int max = -1;
            for(int i=0; i<Beads.line.length; i++) {
                if(max<Beads.line[i]) {
                    max = Beads.line[i];
                    targetLineIndex = i;
                }
            }
            defeatFlag = 1;
        }

        public int setRemoveBeadsCnt() {
            int removeCnt = 0;
            for(int i=0; i<Beads.line.length; i++) {
                copyLine[i] = Beads.line[i];
            }
            removeCnt -= copyLine[targetLineIndex];
            for(int i=0; i<tokenCnt.length; i++) {
                if(tokenSet[targetLineIndex][i] != 0) {
                    tokenCnt[i] -= 1;
                }
            }
            for(int i=0; i<tokenCnt.length; i++) {
                if(tokenCnt[i] % 2 != 0) {
                    removeCnt += Beads.token[i];
                }
            }
            return (removeCnt *= -1);
        }

        public void beadsSet() {
            for(int i=0; i<6; i++) {
                lines[i].setText("");
                for(int j=0; j<Beads.line[i]; j++) {
                    lines[i].append("●");
                }
            }
        }

        public int evenNumber() {
            int evenNumberCnt = 0;
            for(int i=0; i<Beads.line.length; i++) {
                if(Beads.line[i]>1) {
                    evenNumberCnt += 1;
                }
            }
            return evenNumberCnt;
        }

    }


    public void winCheck() {
        int total = 0;
        for(int i=0; i<Beads.line.length; i++) {
            total += Beads.line[i];
        }
        if(total == 1) {
            if(giveUpButton.isEnabled()) {
                for(int i=0; i<linesButton.length; i++) {
                    linesButton[i].setEnabled(false);
                }
                endTurnButton.setEnabled(false);
                messageArea.append(" Player1 Victory!");
                pos = messageArea.getText().length();
                messageArea.setCaretPosition(pos);
                messageArea.requestFocus();
                giveUpButton.setText("돌아가기");
            } else {
                for(int i=0; i<linesButton.length; i++) {
                    linesButton[i].setEnabled(false);
                }
                endTurnButton.setEnabled(false);
                messageArea.append(" Player1 Defeat...");
                pos = messageArea.getText().length();
                messageArea.setCaretPosition(pos);
                messageArea.requestFocus();
                giveUpButton.setText("돌아가기");
                giveUpButton.setEnabled(true);
            }
        }
    }

}
