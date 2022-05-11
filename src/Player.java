import java.awt.Rectangle;

public class Player {
	public int x, y, w, h,dx;
	public boolean l=false,r=false;
	
	public Player() {
		this.w = 30;
		this.h = 10;
		this.x = Game.PREF_W/2 - this.w / 2;
		this.y = Game.PREF_H - 120;
		this.dx= 0;
	}
	
	public Rectangle getHitbox() { 
		return new Rectangle(x,y,w,h);
	}
}
