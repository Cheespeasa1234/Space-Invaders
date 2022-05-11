import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.util.*;

//Honors Computer Science - Mr. Uhl
//Program description: A template class for creating graphical applications

public class Game extends JPanel implements MouseListener, KeyListener, FocusListener {

	// Variables for the class
	private static final long serialVersionUID = 1L;
	public static final int PREF_W = 224 * 3;
	public static final int PREF_H = 256 * 3;
	
	//assets
	private AssetManager am = new AssetManager("as/");
	private Image playerImage = am.getImage("img/ship.png");
	private Image explosion = am.getImage("img/dead.png");
	private BufferedImage bl = Blur.blurredImage(AssetManager.imageToBufferedImage(am.getImage("img/Alien1-a.png")), 5);
	private Image[] alien1Frames = {am.getImage("img/Alien1-a.png"),am.getImage("img/Alien1-b.png"),explosion};
	private Image[] alien2Frames = {am.getImage("img/Alien2-a.png"),am.getImage("img/Alien2-b.png"),explosion};
	private Image[] alien3Frames = {am.getImage("img/Alien3-a.png"),am.getImage("img/Alien3-b.png"),explosion};

	private ArrayList<Alien> aliens = new ArrayList<Alien>();
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	private Laser playerLaser = null;
	private Player player = new Player();

	private String playState = "Playing";

	private boolean a, d, l, r;
	private int lives = 3;
	private int aliensKilled = 0;
	private int movementCount = 0;
	private int maxWaitTime = 30;
	private int maxCooldown = 20;
	private int fireCooldown = 0;

	private int random(int a, int b) {
		return (int) Math.floor(Math.random() * (b - a + 1) + a);
	}
	
	private Timer wait500 = new Timer(500, new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			explosions.clear();
			wait500.stop();
		}
	});

	private Timer gameTimer = new Timer(1000 / 60, new ActionListener() {
		public void actionPerformed(ActionEvent event) {

			repaint();

			if (playState.equals("Playing"))
				physics();

		}
	});

	private void drawPlaying(Graphics2D g2) {
		
		if(playState.equals("Playing")) {
			// draw the aliens
			for (Alien a : aliens) {
				Image f = a.anim[a.imageFrame];
				g2.drawImage(f, a.x, a.y, a.w + ((a.imageFrame==3)?50:0), a.h, this);
			}

			// draw the lasers
			g2.setColor(Color.RED);
			if(playerLaser != null)
				g2.fill(playerLaser.getHitbox());
			
			// draw the explosions
			for(int i = 0; i < explosions.size(); i++) {
				Explosion e = explosions.get(i);
				g2.drawImage(e.i, e.x, e.y, e.i.getWidth(null), e.i.getHeight(null), null);
			}

			g2.setColor(Color.GREEN);

			// draw the player
			Rectangle p = player.getHitbox();
			int w = playerImage.getWidth(this);
			int h = playerImage.getHeight(this);
			g2.drawImage(playerImage, p.x, p.y, w, h, this);

			g2.drawLine(0, PREF_H - 60, PREF_W, PREF_H - 60);

			if (lives == 3) {
				g2.drawImage(playerImage, 20, PREF_H - 40, w, h, this);
				g2.drawImage(playerImage, 40 + p.width, PREF_H - 40, w, h, this);
			} else if (lives == 2)
				g2.drawImage(playerImage, 20, PREF_H - 40, p.width, p.height, this);

			g2.setColor(Color.WHITE);
			g2.drawString("Movement frame: " + movementCount, 0, 15);
			g2.drawString("Firing timer: " + fireCooldown + " / " + maxCooldown, 0, 30);

			g2.drawString(lives + "", 10, PREF_H - 10);
		}

	}

	private void physics() {

		// Player physics
		boolean L = a || l;
		boolean R = d || r;
		
		int s = 5;
		//player.dx = (L == R) ? 0 : (L) ? -s : s;
		player.dx=L==R?0:L?-s:s;
		player.x += player.dx;

		// Collide with sides
		if (player.x < 0)
			player.x = 0;
		if (player.x + player.w > PREF_W)
			player.x = PREF_W - player.w;

		// Laser movement
		if(playerLaser != null) {
			playerLaser.y += playerLaser.dy;
			
			if(playerLaser.y < 0) {
				explosions.add(new Explosion(explosion, playerLaser.x - explosion.getWidth(null)/2, playerLaser.y));
				playerLaser = null;
				wait500.start();
			}
		}
		
		// Laser killing
		for(int i = 0; i < aliens.size(); i++)
			if(playerLaser!=null&&playerLaser.getHitbox().intersects(aliens.get(i).getHitbox())) {
				aliens.get(i).imageFrame = 2;
				playerLaser = null;
				movementCount = 0;
			}
		


		// Alien gunfire
		fireCooldown++;
		if (fireCooldown >= maxCooldown) {
			fireCooldown = 0;

			maxCooldown = random(15, 30) - (aliensKilled / 3);

		}

		// Alien movement
		movementCount++;
		if (movementCount == maxWaitTime) {
			
			// remove exploded aliens
			for(int i = 0; i < aliens.size(); i++) {
				if(aliens.get(i).imageFrame == 2) {
					aliens.remove(i);
				}
			}

			movementCount = 0;

			boolean anAlienBounced = false;
			for (int i = 0; i < aliens.size(); i++) {

				Alien a = aliens.get(i);
				
				// Move the alien
				a.prevX = a.x;
				a.x += a.dx;
				
				// Change animation frame
				if(a.imageFrame == 0)
					a.imageFrame = 1;
				else if(a.imageFrame == 1)
					a.imageFrame = 0;

				// Detect alien on sides
				int nextPos = a.x + a.dx;
				if (nextPos <= 0 || nextPos + a.w >= PREF_W)
					anAlienBounced = true;

			}

			// Make all aliens bounce
			if (anAlienBounced) {
				for (Alien a : aliens) {
					a.dx = -a.dx;
					a.x = a.prevX;
					a.y += 20;
				}
			}

		}

	}

	// Class constructor BOB tHE BUILDER CAN hE FIX IT?????T?T??/
	public Game() {
		this.setFocusable(true);
		this.setBackground(Color.BLACK);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.addFocusListener(this);

		int rows = 11, colu = 5, y = 40, w = 20, h = 20;
		for (int i = 0; i <= rows; i++) {
			
			for (int j = 0; j < colu; j++) {
				Image[] anim;
				int wBias = 6;
				int xBias = 0;
				if(j == 0) {
					anim = alien1Frames;
					wBias = 0;
					xBias = 2;
				} else if(j < 3) 
					anim = alien2Frames;
				else 
					anim = alien3Frames;
				aliens.add(new Alien(i * 40 + xBias, y - wBias/2, w+wBias, h, anim));
				y += h * 2;
			}
			y = 40;
		}

		gameTimer.start();
	}

	// The method used to add graphical images to the panel
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

		if (playState.equals("Playing"))
			drawPlaying(g2);

	}

	/** ******* METHODS FOR INITIALLY CREATING THE JFRAME AND JPANEL *********/

	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}

	public static void createAndShowGUI() {

		JFrame frame = new JFrame("My First Panel!");
		JPanel gamePanel = new Game();

		frame.getContentPane().add(gamePanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void focusGained(FocusEvent e) {
		gameTimer.start();
	}

	@Override
	public void focusLost(FocusEvent e) {
		gameTimer.stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();
		boolean m = true;

		if(c == KeyEvent.VK_SPACE) {
			if(playState.equals("Playing") && playerLaser == null) {
				playerLaser = new Laser(player.x);
			} else if(playState.equals("Paused")) {
				playState.equals("Playing");
			}
		} else if(c == KeyEvent.VK_P) {
			playState = "Paused";
		}

		if (c == KeyEvent.VK_A)
			a = m;
		if (c == KeyEvent.VK_D)
			d = m;
		if (c == KeyEvent.VK_LEFT)
			l = m;
		if (c == KeyEvent.VK_RIGHT)
			r = m;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int c = e.getKeyCode();
		boolean m = false;

		if (c == KeyEvent.VK_A)
			a = m;
		if (c == KeyEvent.VK_D)
			d = m;
		if (c == KeyEvent.VK_LEFT)
			l = m;
		if (c == KeyEvent.VK_RIGHT)
			r = m;
	}
}