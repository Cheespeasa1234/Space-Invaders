import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class spaceinvaders extends JPanel implements KeyListener {
   private static final long serialVersionUID = 1L;
   private static final int PREF_W = 600;
   private static final int PREF_H = 400;
   private RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
         RenderingHints.VALUE_ANTIALIAS_ON);
   private Font textFont = new Font("Blippo", Font.PLAIN, 25);
   private Font numberFont = new Font("Blippo", Font.PLAIN, 70);
   private GameObject ship;
   private GameObject defaultlaser;
   private Timer timer;
   private boolean playing;
   private ArrayList<GameObject> aliens;
   private ArrayList<GameObject> laser;
   private ArrayList <GameObject> alienlaser;
   private ArrayList <GameObject> highestYaliens;
   private boolean gameOver;
   private Clip collisionSound;
   private Clip wallhitsound;
   private Clip scoresound;
   private int alienX;
   private int alienY;
   private int score;
   private int level;
   private boolean nextLevel;
   private int alienmovementtimer;
   private boolean youwin;
   private int lasertimer;
   private boolean laserhasfired;
   private boolean laserready;
   private boolean spacepressed; 
   private int countdownuntilalienlaser;  
   private boolean check0, check1, check2, check3, check4, check5, check6, check7, check8, check9, check10;

   // Constructor
   public spaceinvaders() {
      addKeyListener(this);
      setFocusable(true);
      requestFocus();
      aliens = new ArrayList<GameObject>();
      laser = new ArrayList<GameObject>();
      alienlaser = new ArrayList<GameObject>();
      highestYaliens = new ArrayList<GameObject>();
      alienmovementtimer = 0;
      level = 5;
      alienX = 0;
      alienY = 15;
      youwin = false;
      laserhasfired = false;
      nextLevel = true;
      spacepressed = false;
 
      ship = new GameObject(PREF_W / 2 - 40, 350, 20, 15, 3, 0, 0, PREF_W, 0, PREF_H, true, Color.WHITE);
      ship.setUpKey(KeyEvent.VK_W);
      ship.setDownKey(KeyEvent.VK_S);
      ship.setLeftKey(KeyEvent.VK_LEFT);
      ship.setRightKey(KeyEvent.VK_RIGHT);
      defaultlaser = new GameObject(ship.getX() + ship.getW() / 2 - 5, 350, 5, 5, 0, 0, 0, PREF_W, 0, PREF_H, false,
            Color.WHITE);
           

      try {
         // Open an audio input stream
         URL url = this.getClass().getClassLoader().getResource("ballhitpaddlesound.wav");
         AudioInputStream audio = AudioSystem.getAudioInputStream(url);
         collisionSound = AudioSystem.getClip(); // initialize a sound clip object
         collisionSound.open(audio); // direct the clip to play the audio defined above
         URL url2 = this.getClass().getClassLoader().getResource("ballhitwallsound.wav");
         AudioInputStream audio2 = AudioSystem.getAudioInputStream(url2);
         wallhitsound = AudioSystem.getClip(); // initialize a sound clip object
         wallhitsound.open(audio2); // direct the clip to play the audio defined above
         URL url3 = this.getClass().getClassLoader().getResource("pongscoresound.wav");
         AudioInputStream audio3 = AudioSystem.getAudioInputStream(url3);
         scoresound = AudioSystem.getClip(); // initialize a sound clip object
         scoresound.open(audio3); // direct the clip to play the audio defined above
      } catch (Exception e) {
         e.printStackTrace();
      }

      timer = new Timer(5, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ship.updateUsingKeys();
            int alienwidth = 15;
            int alienheight = 15;
            if (nextLevel) {
               for (int i = 1; i < level + 1; i++) {
                  for (int b = 1; b <= 11; b++) {
                     alienX = 85 + b * 34;
                     alienY = 20 + i * 34;
                     int randomnum = 1;
                     GameObject addedAlien = new GameObject(alienX, alienY, alienwidth, alienheight, 12, 0, 0, PREF_W, 0,
                           PREF_H, false, Color.WHITE, randomnum, b, false);

                     aliens.add(addedAlien);
                     nextLevel = false;
                  }
               }
               highestYaliens.add(aliens.get(0));
               highestYaliens.add(aliens.get(1));
               highestYaliens.add(aliens.get(2));
               highestYaliens.add(aliens.get(3));
               highestYaliens.add(aliens.get(4));
               highestYaliens.add(aliens.get(5));
               highestYaliens.add(aliens.get(6));
               highestYaliens.add(aliens.get(7));
               highestYaliens.add(aliens.get(8));
               highestYaliens.add(aliens.get(9));
               highestYaliens.add(aliens.get(10));
               initiallysetHYA(0);
               initiallysetHYA(1);
               initiallysetHYA(2);
               initiallysetHYA(3);
               initiallysetHYA(4);
               initiallysetHYA(5);
               initiallysetHYA(6);
               initiallysetHYA(7);
               initiallysetHYA(8);
               initiallysetHYA(9);
               initiallysetHYA(10);
            }


            if (playing) {
               if(!laserhasfired)
               {
                  if (spacepressed){
                     defaultlaser = new GameObject(ship.getX() + ship.getW() / 2 - 2, 350, 5, 5, 0, 0, 0, PREF_W, 0, PREF_H, false, Color.WHITE);
                     laser.add(defaultlaser);
                     for(GameObject l : laser)
                     laserready = true;
                     laserhasfired = true;
                     spacepressed = false;
                  }
               }
               if(laserready)
               {
                  laserhasfired = true;
                  for(GameObject l : laser)
                  l.setDy(-1);
               }
               for (GameObject l : laser) {
                  l.setY(l.getY() + l.getDy());
               }
               if(laser.size() == 0)
               {
                  lasertimer += 1;
                  if(lasertimer == 1){
                  laserhasfired = false;
                  lasertimer = 0;
                  spacepressed = false;
                  }
               }
               for(int l = laser.size() - 1; l > -1; l--)
               {
                  if(laser.get(l).getY() <= 0)
                  laser.remove(laser.get(l));
               }
               aliens.get(aliens.size() - 1).getY();

               alienmovementtimer += 1;
               for (GameObject b : aliens) {
                  if (alienmovementtimer == 100) {
                     for (GameObject g : aliens) {
                        g.setX(g.getX() + g.getDx());
                        alienmovementtimer = 0;
                     }
                  }
                  if (b.getX() + b.getW() >= PREF_W) {
                     for (GameObject g : aliens) {
                        g.setY(g.getY() + 20);
                        g.setX(g.getX() - 12);
                        g.setDx(-g.getDx());
                     }
                  }
                  if (b.getX() <= 0) {
                     for (GameObject g : aliens) {
                        g.setY(g.getY() + 20);
                        g.setX(g.getX() + 12);
                        g.setDx(-g.getDx());
                     }
                  }
               }
               
               for (int l = laser.size() - 1; l > -1; l--) {
                  for (int q = aliens.size() - 1; q > -1; q--) {
                     if (laser.get(l).checkAndReactToCollisionWith(aliens.get(q))) {
                     aliens.remove(q);
                     for(GameObject a: aliens)
                     {
                        if (a.getRow() == 1)
                           check0 = true;
                        if(a.getRow() == 2)
                           check1 = true;
                        if(a.getRow() == 3)
                           check2 = true;
                        if(a.getRow() == 4)
                           check3 = true;
                        if(a.getRow() == 5)
                           check4 = true;
                        if(a.getRow() == 6)
                           check5 = true;
                        if(a.getRow() == 7)
                           check6 = true;
                        if(a.getRow() == 8)
                           check7 = true;
                        if(a.getRow() == 9)
                           check8 = true;
                        if(a.getRow() == 10)
                           check9 = true;
                        if(a.getRow() == 11)
                           check10 = true;
                     }
                     if (check10)
                     resethighestYaliens(10);
                     if(check9)
                     resethighestYaliens(9);
                     if(check8)
                     resethighestYaliens(8);
                     if(check7)
                     resethighestYaliens(7);
                     if(check6)
                     resethighestYaliens(6);
                     if(check5)
                     resethighestYaliens(5);
                     if(check4)
                     resethighestYaliens(4);
                     if(check3)
                     resethighestYaliens(3);
                     if(check2)
                     resethighestYaliens(2);
                     if(check1)
                     resethighestYaliens(1);
                     if(check0)
                     resethighestYaliens(0);

                     check0 = false; check1 = false; check2 = false; check3 = false; check4 = false; check5 = false; check6 = false; check7 = false; check8 = false; check9 = false; check10 = false;
                     
                        System.out.println("alienhit");
                        laser.remove(laser.get(l));
                        break;
                     }
                  }
               }
               if (aliens.size() == 0) {
                  youwin = true;
                  playing = false;
                  gameOver = true;
               }
            }
            repaint();
         }
      });
      timer.start();

   }

   public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setColor(Color.BLACK);
      g2.fillRect(0, 0, PREF_W, PREF_H);
      g2.setColor(Color.WHITE);
      g2.setRenderingHints(hints);
      g2.setFont(numberFont);
      g2.setFont(textFont);
      g2.drawString("Balls left: " + laser.size(), 0, 360);
      g2.drawString("Alien Destroyed: " + score, 0, 400);
      // g2.drawString("Score time: " + scoretimer, 0, 380);
      if (!playing && !gameOver)
         drawStringAtCenter(g2, "Press <Space> to play", PREF_H / 2 - 50);
      if (gameOver && !youwin) {
         drawStringAtCenter(g2, "Press <R> to reset", 330);
         drawStringAtCenter(g2, "Game Over", 270);
         drawStringAtCenter(g2, "You got " + alienmovementtimer + " points!", 300);
      }
      if (gameOver && youwin) {
         drawStringAtCenter(g2, "Press <R> to reset", 330);
         drawStringAtCenter(g2, "You Win!", 270);
         drawStringAtCenter(g2, "You got " + alienmovementtimer + " points!", 300);
      }

      ship.draw(g2);
      for (GameObject b : aliens) {
         b.draw(g2);
         g2.setColor(Color.RED);
         g2.setColor(Color.WHITE);
      }

      for (GameObject b : laser) {
         b.drawAsCircle(g2);
      }

   }

   public void changeAlienDirection() {
      for (int i = 0; i < aliens.size(); i++) {
         aliens.get(i).setDx(-aliens.get(i).getDx());
         aliens.get(i).setY(aliens.get(i).getY() + 25);
      }
   }
   
   public void drawStringAtCenter(Graphics2D g2, String string, int height) {
      FontMetrics fm = g2.getFontMetrics();
      int messageWidth = fm.stringWidth(string);
      int startX = PREF_W / 2 - messageWidth / 2;
      g2.drawString(string, startX, height);
   }
   
   public void drawStringAtQuarter(Graphics2D g2, String string, int height, boolean isFirst) {
      if (isFirst) {
         FontMetrics fm = g2.getFontMetrics();
         int messageWidth = fm.stringWidth(string);
         int startX = PREF_W / 2 - messageWidth / 2 - PREF_W / 4;
         g2.drawString(string, startX, height);
      }
      if (!isFirst) {
         FontMetrics fm = g2.getFontMetrics();
         int messageWidth = fm.stringWidth(string);
         int startX = PREF_W / 2 - messageWidth / 2 + PREF_W / 4;
         g2.drawString(string, startX, height);
      }
   }
   public void resethighestYaliens(int index)
   {
      if(highestYaliens.get(index) == null)
         {
            highestYaliens.remove(highestYaliens.get(index));
         }
            highestYaliens.set(index, null);
            for(GameObject a: aliens)
            {
               if(a.getRow() == index + 1)
               {
                  highestYaliens.set(index, a);
                  if (a.getY() > highestYaliens.get(index).getY()){
                     highestYaliens.set(index, a);
                  }
               }
            }
                     // if(highestYaliens.get(0) == null)
                     // {
                     //    highestYaliens.remove(highestYaliens.get(0));
                     // }
                     //    highestYaliens.set(0, null);
                     //    for(GameObject a: aliens)
                     //    {
                     //       if(a.getRow() == 1)
                     //       {
                     //             highestYaliens.set(0, a);
                     //          if (a.getY() > highestYaliens.get(0).getY()){
                     //             highestYaliens.set(0, a);
                     //          }
                     //          System.out.println(highestYaliens.get(0).getY());
                     //       }
                     //    }

   }
   public void initiallysetHYA(int index)
   {
      for(GameObject a: aliens)
      {
         if (a.getRow() == index + 1 && a.getY() > highestYaliens.get(index).getY()){
            highestYaliens.set(index, a);
         }
      }
   }
   
   @Override
   
   public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();
      if (key == KeyEvent.VK_SPACE) {
         if (!gameOver || !youwin)
         playing = true;
      }
      if(key == KeyEvent.VK_SPACE)
      {
         spacepressed = true;
      }
      if (key == KeyEvent.VK_R && !playing && gameOver) {
         score = 0;
         alienmovementtimer = 0;
         defaultlaser.setY(350 - 10);
         defaultlaser.setX(ship.getX() + ship.getW() / 2 - 5);
         for (int j = laser.size() - 1; j > -1; j--) {
            laser.remove(laser.get(j));
         }
         laser.add(defaultlaser);
         gameOver = false;
         youwin = false;
         for (int j = aliens.size() - 1; j > -1; j--) {
            aliens.remove(aliens.get(j));
         }
         nextLevel = true;
         ship.setX(PREF_W / 2 - 40);
         ship.setY(350);

      }

      ship.keyWasPressed(key);
   }

   @Override
   public void keyReleased(KeyEvent e) {
      int key = e.getKeyCode();
      ship.keyWasReleased(key);
   }

   @Override
   public void keyTyped(KeyEvent e) {
   }

   private static void createAndShowGUI() {
      spaceinvaders gamePanel = new spaceinvaders();
      JFrame frame = new JFrame("My Frame");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(gamePanel);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setBackground(Color.WHITE);
      frame.setVisible(true);
      frame.setResizable(false);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGUI();
         }
      });
   }
}