
import java.awt.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

/**
 * Class VoltorbFlip - write a description of the class here
 *
 * @author Ran Crook
 * @version 3.5.11
 */
public class VoltorbFlip extends JApplet
{
    private boolean startGame = false;
    private boolean allowFlip;
    private Image bg, logo, tile, infoTile, blankTile;
    private Image tile0, tile1, tile2, tile3;
    private int row, col;
    private int [][] tileID;
    private boolean [][] tileFlipped;
    private Point [][] tileCoord;
    private boolean [][][] tileMarked;
    private int [] nbr;
    private static Graphics g;
    private static Image offscreen;

    public void init () {
        setFocusable(true);
        setSize(500, 500);
        offscreen = createImage(getWidth(), getHeight());
        g = offscreen.getGraphics();
        allowFlip = false;
        try {
            bg = ImageIO.read(getClass().getResource("/Background.png"));
            logo = ImageIO.read(getClass().getResource("/Logo.png"));
            tile = ImageIO.read(getClass().getResource("/Tile.png"));
            infoTile = ImageIO.read(getClass().getResource("/InfoTile.png"));
            blankTile = ImageIO.read(getClass().getResource("/BlankTile.png"));
            tile0 = ImageIO.read(getClass().getResource("/Tile0.png"));
            tile1 = ImageIO.read(getClass().getResource("/Tile1.png"));
            tile2 = ImageIO.read(getClass().getResource("/Tile2.png"));
            tile3 = ImageIO.read(getClass().getResource("/Tile3.png"));
        }
        catch (IOException ex) {}
        row = 0;
        col = 0;
        tileID = new int[5][5];
        tileFlipped = new boolean[5][5];
        tileCoord = new Point[6][6];
        tileMarked = new boolean[5][5][4];
        nbr = new int[4];
        int x = 0;
        int y = 0;
        for (int ind = 0; ind < 4; ind++) {
            nbr[ind] = 0;
        }
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                if (row < 5 && col < 5) {
                    tileID[row][col] = (int)(Math.random()*4);
                    nbr[tileID[row][col]]++;
                    tileFlipped[row][col] = false;
                    for (int ind = 0; ind < 4; ind++) {
                        tileMarked[row][col][ind] = false;
                    }
                }
                tileCoord[row][col] = new Point(x, y);
                x+=84;
            }
            x = 0;
            y+=84;
        }
        addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent ke) {
                if (!startGame) {
                    if (ke.getKeyCode() == KeyEvent.VK_SPACE || ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        startGame = true;
                    }
                }
                if (startGame) {
                    if (ke.getKeyCode() == KeyEvent.VK_R) {
                        reset();
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_UP) {
                        row--;
                        if (row < 0) {
                            row = 4;
                        }
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                        row++;
                        if (row > 4) {
                            row = 0;
                        }
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                        col++;
                        if (col > 4) {
                            col = 0;
                        }
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                        col--;
                        if (col < 0) {
                            col = 4;
                        }
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_0) {
                        tileMarked[row][col][0] = !tileMarked[row][col][0];
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_1) {
                        tileMarked[row][col][1] = !tileMarked[row][col][1];
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_2) {
                        tileMarked[row][col][2] = !tileMarked[row][col][2];
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_3) {
                        tileMarked[row][col][3] = !tileMarked[row][col][3];
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_SPACE || ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (allowFlip) {
                            tileFlipped[row][col] = true;
                        }
                    }
                    allowFlip = true;
                }

                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent me) {
                if (startGame) {
                    if (allowFlip) {
                        for (int row2 = 0; row2 < 5; row2++) {
                            for (int col2 = 0; col2 < 5; col2++) {
                                if ((tileCoord[row2][col2].x <= me.getX() && tileCoord[row2][col2].x + 80 >= me.getX())
                                        && (tileCoord[row2][col2].y <= me.getY() && tileCoord[row2][col2].y + 80 >= me.getY())) {
                                    if (row == row2 && col == col2) {
                                        tileFlipped[row][col] = true;
                                    }
                                    else {
                                        row = row2;
                                        col = col2;
                                    }
                                }
                            }
                        }
                    }
                    allowFlip = true;
                }

                repaint();
            }
        });
    }

    public void reset () {
        allowFlip = false;
        row = 0;
        col = 0;
        for (int ind = 0; ind < 4; ind++) {
            nbr[ind] = 0;
        }
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                tileID[row][col] = (int)(Math.random()*4);
                nbr[tileID[row][col]]++;
                tileFlipped[row][col] = false;
                for (int ind = 0; ind < 4; ind++) {
                    tileMarked[row][col][ind] = false;
                }
            }
        }
    }

    public void paint (Graphics graphics) {
        g.setFont(new Font("Arial Black", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        g.drawImage(bg, 0, 0, this);
        if (!startGame) {
            g.drawImage(logo, 30, 40, this);
            g.drawString("PRESS SPACE OR ENTER TO BEGIN", 75, 255);
//             g.drawString("Use arrow keys or mouse to select tile.", 52, 325);
//             g.drawString("Use space/enter or double click to flip", 55, 345);
//             g.drawString("tile. Use 1, 2, 3 and 0 keys to mark tiles.", 67, 365);
//             g.drawString("Numbers on side indicate sum of row and", 35, 385);
//             g.drawString("number of Voltorbs in row. Win by unveiling", 25, 405);
//             g.drawString("all number tiles, lose by unveiling Voltorb.", 45, 425);
            g.drawString("©Ran Crook 2011-2012", 250, 490);
        }
        if (startGame) {
            g.setFont(new Font("Plain", Font.BOLD, 12));
            g.setColor(Color.RED);
            g.fillRect(tileCoord[row][col].x - 4, tileCoord[row][col].y - 4, 88, 88);
            g.setColor(Color.WHITE);
            int ctr1, ctr2;
            for (int row = 0; row < 6; row++) {
                ctr1 = 0;
                ctr2 = 0;
                for (int col = 0; col < 6; col++) {
                    if (row < 5 && col < 5) {
                        g.drawImage(tile, tileCoord[row][col].x, tileCoord[row][col].y, this);
                        if (tileMarked[row][col][0]) {
                            g.drawString("0", tileCoord[row][col].x + 62, tileCoord[row][col].y + 70);
                        }
                        if (tileMarked[row][col][1]) {
                            g.drawString("1", tileCoord[row][col].x + 10, tileCoord[row][col].y + 17);
                        }
                        if (tileMarked[row][col][2]) {
                            g.drawString("2", tileCoord[row][col].x + 62, tileCoord[row][col].y + 17);
                        }
                        if (tileMarked[row][col][3]) {
                            g.drawString("3", tileCoord[row][col].x + 10, tileCoord[row][col].y + 70);
                        }
                        if (tileFlipped[row][col]) {
                            if (tileID[row][col] == 0) {
                                g.drawImage(tile0, tileCoord[row][col].x, tileCoord[row][col].y, this);
                            }
                            else if (tileID[row][col] == 1) {
                                g.drawImage(tile1, tileCoord[row][col].x, tileCoord[row][col].y, this);
                            }
                            else if (tileID[row][col] == 2) {
                                g.drawImage(tile2, tileCoord[row][col].x, tileCoord[row][col].y, this);
                            }
                            else {
                                g.drawImage(tile3, tileCoord[row][col].x, tileCoord[row][col].y, this);
                            }
                        }
                        ctr1+=tileID[row][col];
                        if (tileID[row][col] == 0) {
                            ctr2++;
                        }
                    }
                    else if ((row == 5 || col == 5) && ((row == 5 && col != 5) || (row != 5 && col == 5))) {
                        g.drawImage(infoTile, tileCoord[row][col].x, tileCoord[row][col].y, this);
                    }
                    else {
                        g.drawImage(blankTile, tileCoord[row][col].x, tileCoord[row][col].y, this);
                    }
                }
                if (row < 5 && col < 5) {
                    g.drawString(Integer.toString(ctr1), tileCoord[row][5].x + 35, tileCoord[row][5].y + 22);
                    g.drawString(Integer.toString(ctr2), tileCoord[row][5].x + 60, tileCoord[row][5].y + 60);
                }
            }
            for (int col = 0; col < 5; col++) {
                ctr1 = 0;
                ctr2 = 0;
                for (int row = 0; row < 5; row++) {
                    ctr1+=tileID[row][col];
                    if (tileID[row][col] == 0) {
                        ctr2++;
                    }
                }
                g.drawString(Integer.toString(ctr1), tileCoord[5][col].x + 35, tileCoord[5][col].y + 22);
                g.drawString(Integer.toString(ctr2), tileCoord[5][col].x + 60, tileCoord[5][col].y + 60);
            }
            g.drawString("Press   'R'", 426, 453);
            g.drawString("to restart", 426, 473);
            int ctr = 0;
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (tileID[row][col] == 0 && tileFlipped[row][col]) {
                        JOptionPane.showMessageDialog(null, "You lose! Press OK to start new game", "GAME OVER", 1);
                        reset();
                    }
                    else if (tileID[row][col] != 0 && tileFlipped[row][col]) {
                        ctr++;
                    }
                }
            }
            if (ctr >= nbr[1] + nbr[2] + nbr[3]) {
                JOptionPane.showMessageDialog(null, "You win! Press OK to start new game", "A WINNER IS YOU", 1);
                reset();
            }
        }

        graphics.drawImage(offscreen, 0, -1, this);
    }
}
