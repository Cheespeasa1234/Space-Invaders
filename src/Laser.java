import java.awt.Rectangle;

public class Laser {
	public int x,y,w,h,dy;
	public boolean isAlienLaser = false;
	
	public Laser(int playerX) {
		this.x = playerX+18;
		this.y = Game.PREF_H - 108;
		this.w = 6;
		this.h = 20;
		this.dy = -10;
	}
	
	public Laser(int alienX, int alienY, boolean isAlienLaser) {
		this.x = alienX;
		this.y = alienY;
		this.w = 6;
		this.h = 20;
		this.dy = 8;
		this.isAlienLaser = isAlienLaser;
	}
	
	public Rectangle getHitbox() {
		return new Rectangle(x,y,w,h);
	}
}
