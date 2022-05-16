import java.awt.Image;
import java.awt.Rectangle;	

public class Alien {
	
	int x,y,w,h,dx,speed=10,prevX,imageFrame = 0,row;
	Image[] anim;
	
	public Alien(int x, int y, int w, int h, Image[] anim, int row) {
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.dx=speed;
		this.anim = anim;
		this.row = row;
	}
	
	public Rectangle getHitbox() {
		return new Rectangle(x,y,w,h);
	}
	
	public int getRow() {
		return this.row;
	}
	
	@Override
	public String toString() {
		return "Alien"+this.hashCode()+"[x="+this.x+",y="+this.y+",frame="+this.imageFrame+"]";
	}
}
