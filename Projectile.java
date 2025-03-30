/*
Projectile
This class is responsible for everything to do with projectiles
ICS3U7 - Ms.Strelkovska
Nov 3
*/
import java.awt.*;

public class Projectile extends Rectangle{
    private int speed;
	public Projectile(int x, int y, int speed){ //used for player
		super(x,y,15,20);
		this.speed=speed;
	}
	public Projectile(int x, int y, int width, int height, int speed){ //used for boss and player if collect potion(increased attack)
		super(x, y, width, height);
		this.speed=speed;
	}
	public void moveUp(){ //moves the projectile upwards, used for player's projectiles
		y-=speed;
	}
	public void moveDown(){ //moves the projectile downwards, used for boss's projectiles
		y+=speed;
	}
	public void moveRight(){ //moves the projectile right, used for boss's projectiles
		x+=speed;
	}
	public void moveLeft(){ //moves the projectile left, used for boss's projectiles
		x-=speed;
	}
	public boolean isExited(){ //checking if projectile has exited the screen 
	   return x>1230 || x<0 || y>630 || y<0;
	}
}
