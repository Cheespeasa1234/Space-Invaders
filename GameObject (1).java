import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

//Derek Zheng
//Program description:
//Feb 14, 2022

public class GameObject
{ //variables to represent the object we are creating... insantance variables
   private int x, y, w, h;
   private int dx, dy;
   int xmin, xmax, ymin, ymax;
   private Color color;
   private boolean up, down, left, right;
   private int upKey, downKey, leftKey, rightKey;
   private int gravity;
   private boolean isWanderer;
   private int hitsRemaining;
   private int row;
   private boolean highestalien;


   //this is a constructor... it allows us to give initial values to the variables.
   public GameObject(int x, int y)
   {
      this.x = x;
      this.y = y;
   }
   //another constructor, takes four parameters
   public GameObject(int x, int y, int w, int h)
   {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
   }
   //another constructor, takes five parameters
   public GameObject(int x, int y, int w, int h, Color color)
   {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.color = color;
   }
   public GameObject(int x, int y, int w, int h, int dx, int dy, int xmin, int xmax, int ymin, int ymax, boolean isWanderer, Color color)
   {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.dx = dx;
      this.dy = dy;
      this.xmax = xmax;
      this.xmin = xmin;
      this.ymax = ymax;
      this.ymin = ymin;
      this.color = color;
      this.isWanderer = isWanderer;
      
   }
   public GameObject(int x, int y, int w, int h, int dx, int dy, int xmin, int xmax, int ymin, int ymax, boolean isWanderer, Color color, int hitsRemaining)
   {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.dx = dx;
      this.dy = dy;
      this.xmax = xmax;
      this.xmin = xmin;
      this.ymax = ymax;
      this.ymin = ymin;
      this.color = color;
      this.isWanderer = isWanderer;
      this.hitsRemaining = hitsRemaining;
   }
   public GameObject (int x, int y, int w, int h, int dx, int dy, int xmin, int xmax, int ymin, int ymax, boolean isWanderer, Color color, int hitsRemaining, int row, Boolean highestalien)
   {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.dx = dx;
      this.dy = dy;
      this.xmax = xmax;
      this.xmin = xmin;
      this.ymax = ymax;
      this.ymin = ymin;
      this.color = color;
      this.isWanderer = isWanderer;
      this.hitsRemaining = hitsRemaining;
      this.row = row;
      this.highestalien = highestalien;
   }


   
   public boolean getUp() {
      return this.up;
   }


   public boolean getDown() {
      return this.down;
   }


   public boolean getLeft() {
      return this.left;
   }


   public boolean getRight() {
      return this.right;
   }


   public int getGravity() {
      return this.gravity;
   }

   public void setGravity(int gravity) {
      this.gravity = gravity;
   }

   public boolean isIsWanderer() {
      return this.isWanderer;
   }

   public boolean getIsWanderer() {
      return this.isWanderer;
   }

   public void setIsWanderer(boolean isWanderer) {
      this.isWanderer = isWanderer;
   }

   public int getHitsRemaining() {
      return this.hitsRemaining;
   }

   public void setHitsRemaining(int hitsRemaining) {
      this.hitsRemaining = hitsRemaining;
   }
   
   
   
   

   //methods for brick object
   public void draw(Graphics2D g2)
   {
      g2.setColor(color);
      g2.fillRect(x, y, w, h);
      g2.setStroke(new BasicStroke(3));
//      g2.setColor(Color.BLACK);
      g2.drawRect(x, y, w, h);
   }
   public void drawAsCircle(Graphics2D g2)
   {
      g2.setColor(color);
      g2.fillOval(x, y, w, h);
//      g2.setStroke(new BasicStroke(3));
      g2.setColor(Color.BLACK);
//      g2.drawOval(x, y, w, h);
   }
   public void setRandomColor()
   {
      int r = (int) (Math.random()*256);
      int g = (int) (Math.random()*256);
      int b = (int) (Math.random()*256);
      color = (new Color(r, g, b));
   }
   
   public void updateForBrickBreaker()
   {
      x += dx;
      y += dy;
      
      if(x + w > xmax || x < xmin)
      {
         dx = -dx;
      }
      if(y < ymin)
      {
         dy = -dy;
      }
   }
   
   public void update()
   {
      
      dy += gravity;
      
      x += dx;
      y += dy;
      
      if(x + w > xmax || x < xmin)
      {
         dx = -dx;
      }
      if(y + h > ymax || y < ymin)
      {
         dy = -dy;
      }
   }
   public void updateForPong()
   {
      x += dx;
      y += dy;
      

      if(y + h > ymax || y < ymin)
      {
         dy = -dy;
      }
   }
   public void updateUsingKeys()
   {
      if(left)
         x-=dx;
      if(right)
         x+=dx;
      if(up)
         y-=dy;
      if(down)
         y+=dy;
      //keep brick on panel
      if(x <= xmin)
         x = xmin;
      if(x + w >= xmax)
         x = xmax - w;
      if(y <= ymin)
         y = ymin;
      if(y + h >= ymax)
         y = ymax - h;
      
   }
   public boolean isOffPanel()
   {
      return (x < -w || x > xmax);
   }   
   public void keyWasPressed(int key)
   {
      if(key == upKey)
         up = true;
      if(key == downKey)
         down = true;
      if(key == leftKey)
         left = true;
      if(key == rightKey)
         right = true;      
   }
   public void keyWasReleased(int key)
   {
      if(key == upKey)
         up = false;
      if(key == downKey)
         down = false;
      if(key == leftKey)
         left = false;
      if(key == rightKey)
         right = false;
   }
   public int getX()
   {
      return x;
   }
   public void setX(int x) 
   {
      this.x = x;
   }
   
   public int getY()
   {
      return y;
   }
   public void setY(int y)
   {
      this.y = y;
   }
   public int getW()
   {
      return w;
   }
   public void setW(int w)
   {
      this.w = w;
   }
   public int getH()
   {
      return h;
   }
   public void setH(int h)
   {
      this.h = h;
   }
   public int getDx()
   {
      return dx;
   }
   public void setDx(int dx)
   {
      this.dx = dx;
   }
   public int getDy()
   {
      return dy;
   }
   public void setDy(int dy)
   {
      this.dy = dy;
   }
   public Color getColor()
   {
      return color;
   }
   public void setColor(Color color)
   {
      this.color = color;
   }
   public int getXmin()
   {
      return xmin;
   }
   public void setXmin(int xmin)
   {
      this.xmin = xmin;
   }
   public int getXmax()
   {
      return xmax;
   }
   public void setXmax(int xmax)
   {
      this.xmax = xmax;
   }
   public int getYmin()
   {
      return ymin;
   }
   public void setYmin(int ymin)
   {
      this.ymin = ymin;
   }
   public int getYmax()
   {
      return ymax;
   }
   public void setYmax(int ymax)
   {
      this.ymax = ymax;
   }
   
   
   public boolean isUp()
   {
      return up;
   }
   public void setUp(boolean up)
   {
      this.up = up;
   }
   public boolean isDown()
   {
      return down;
   }
   public void setDown(boolean down)
   {
      this.down = down;
   }
   public boolean isLeft()
   {
      return left;
   }
   public void setLeft(boolean left)
   {
      this.left = left;
   }
   public boolean isRight()
   {
      return right;
   }
   public void setRight(boolean right)
   {
      this.right = right;
   }
   public int getUpKey()
   {
      return upKey;
   }
   public void setUpKey(int upKey)
   {
      this.upKey = upKey;
   }
   public int getDownKey()
   {
      return downKey;
   }
   public void setDownKey(int downKey)
   {
      this.downKey = downKey;
   }
   public int getLeftKey()
   {
      return leftKey;
   }
   public void setLeftKey(int leftKey)
   {
      this.leftKey = leftKey;
   }
   public int getRightKey()
   {
      return rightKey;
   }
   public void setRightKey(int rightKey)
   {
      this.rightKey = rightKey;
   }
   public boolean isWanderer()
   {
      return isWanderer;
   }

   public int getRow() {
      return this.row;
   }

   public void setRow(int row) {
      this.row = row;
   }

   public void setWanderer(boolean isWanderer)
   {
      this.isWanderer = isWanderer;
   }
   @Override
   public String toString()
   {
      return "Brick [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", dx=" + dx + ", dy=" + dy + ", xmin=" + xmin
            + ", xmax=" + xmax + ", ymin=" + ymin + ", ymax=" + ymax + ", wanderer?= " + isWanderer +", color=" + color + "]";
   }
   //collisions methods
   /** Determines the intersecting side for the brick in relation to another brick
   *  return true for a collision and false otherwise
   */
   public boolean checkAndReactToCollisionWith(GameObject r)
   {
      int xm = x + w/2; //use the center of the moving brick as a reference
      int ym = y + h/2; //use the center of the moving brick as a reference
      
      int side = getSideForIntersection(r, xm, ym); //get the moving brick in relation to the other brick
      
      if(side == 0)      //Is the moving brick above the other brick?
         return checkCollisionTopOfRectangle(r);
      else if(side == 1) //Is the moving brick to the right of the other brick?
         return checkCollisionRightSideOfRectangle(r);
      else if(side == 2) //Is the moving brick below the other brick?
         return checkCollisionBottomOfRectangle(r);
      else if(side == 3) //Is the moving brick to the left of the other brick?
         return checkCollisionLeftSideOfRectangle(r);
      
      return false;
   }

   /**Returns the side where a collision would occur if possible
   *    0 = top
   *    1 = right
   *    2 = bottom
   *    3 = left
   */
   private int getSideForIntersection(GameObject r, int x1, int y1)
   {
      double slopeMajor = (double) r.h / r.w;         //major diagonal slope
      double slopeMinor = (double) -r.h / r.w;        //minor diagonal slope
      double bMajor = r.y - slopeMajor * r.x;         //major diagonal y-intercept
      double bMinor = r.y - slopeMinor * (r.x + r.w); //minor diagonal y-intercept
      
      boolean aboveMajor = y1 < slopeMajor * x1 + bMajor; //Is the given point above the major diagonal
      boolean aboveMinor = y1 < slopeMinor * x1 + bMinor; //Is the given point above the minor diagonal
      
      if(aboveMajor  && aboveMinor)  return 0; //The point is above the other brick
      if(aboveMajor  && !aboveMinor) return 1; //The point is to the right of the other brick
      if(!aboveMajor && !aboveMinor) return 2; //The point is below the other brick
      if(!aboveMajor && aboveMinor)  return 3; //The point is to the left of the other brick
      
      return -1;   //Should never get here since "not above" is below OR ON a diagonal
   }

   private boolean checkCollisionLeftSideOfRectangle(GameObject r)
   {
      boolean collision = false;
      
      if(y + h > r.y && y < r.y + r.h) {
         if(x + w > r.x) {
            dx = -Math.abs(dx);
            x = r.x - w;
            if(x <= xmin) {  //don't let the brick get bumped off the panel
               x = xmin;
               // r.x = x + w;  //in case  the colliding brick is moving, stop it from overlapping this brick the the edges of the panel
            }
            /*YOU NEED TO ADD THIS NEXT LINE!*/
            if(!r.isWanderer)
            r.dx = -r.dx; //use dy for top and bottom and dx for left and right



            if(x <= xmin) {  //don't let the brick get bumped off the panel

               x = xmin;

               // r.x = x + w;  //in case the colliding brick is moving, stop it from overlapping this brick the the edges of the panel

            }

            collision = true;
         }
      }
      return collision;
   }

   private boolean checkCollisionRightSideOfRectangle(GameObject r)
   {
      boolean collision = false;
      
      if(y + h > r.y && y < r.y + r.h) {
      if(x < r.x + r.w) {
            dx = Math.abs(dx);
            x = r.x + r.w;
            if(x + w >= xmax) {  //don't let the brick get bumped off the panel
               x = xmax - w;
               // r.x = x - r.w;       //in case the colliding brick is moving, stop it from overlapping this brick the the edges of the panel
            }
            /*YOU NEED TO ADD THIS NEXT LINE!*/
            if(!r.isWanderer)
               r.dx = -r.dx; //use dy for top and bottom and dx for left and right



            if(x <= xmin) {  //don't let the brick get bumped off the panel

               x = xmin;

               // r.x = x + w;  //in case the colliding brick is moving, stop it from overlapping this brick the the edges of the panel

            }
            collision = true;
         }
      }
      return collision;
   }

   private boolean checkCollisionBottomOfRectangle(GameObject r)
   {
      boolean collision = false;
      
      if(x + w > r.x && x < r.x + r.w) {
         if(y < r.y + r.h) {
            dy = Math.abs(dy);
            y = r.y + r.h;
            
               // r.y = y - r.h;   //in case the colliding brick is moving, stop it from overlapping this brick the the edges of the panel
            
            /*YOU NEED TO ADD THIS NEXT LINE!*/
            if(!r.isWanderer)
               r.dy = -r.dy; //use dy for top and bottom and dx for left and right



               if(y + h >= ymax) { //don't let the brick get bumped off the panel
                  y = ymax - h;
               }
               // r.y = y + h;  //in case the colliding brick is moving, stop it from overlapping this brick the the edges of the panel
         
            collision = true;
         }
      }
      return collision;
   }

   private boolean checkCollisionTopOfRectangle(GameObject r)
   {
      boolean collision = false;
      
      if(x + w > r.x && x < r.x + r.w) {
         if(y + h > r.y) {
            dy = -Math.abs(dy);
            y = r.y - h;
               
               // r.y = y + h;  //in case the colliding brick is moving, stop it from overlapping this brick the the edges of the panel
              
               /*YOU NEED TO ADD THIS NEXT LINE!*/
               if(!r.isWanderer)
                  r.dy = -r.dy; //use dy for top and bottom and dx for left and right

               if(y <= ymin) {  //don't let the brick get bumped off the panel

                  y = ymin;

                  // r.y = y + h;  //in case the colliding brick is moving, stop it from overlapping this brick the the edges of the panel
            }
            collision = true;
      }
      }
      return collision;
   }
}

