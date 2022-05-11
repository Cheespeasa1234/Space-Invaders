import java.awt.Image;
import java.awt.Rectangle;	

public class Alien {
	
	int x,y,w,h,dx,speed=10,prevX;
	int imageFrame = 0;
	Image[] anim;
	
	public Alien(int x, int y, int w, int h, Image[] anim) {
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.dx=speed;
		this.anim = anim;
	}
	
	public Rectangle getHitbox() {
		return new Rectangle(x,y,w,h);
	}
}
