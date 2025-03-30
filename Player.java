/*
Player
This class is responsible for everything to do with the player
ICS3U7 - Ms.Strelkovska
Oct 30
*/
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Player extends Rectangle{
	private static int hp, ammoCount, speed, chestsCollected; //hp=health
	private boolean shooting;
	private boolean movingLeft, movingRight, movingDown, movingUp, facingLeft, facingRight, facingDown, facingUp;
	private boolean lastFacingLeft=false, lastFacingRight=false, lastFacingUp=false, lastFacingDown=false;
	private static boolean start, poweredUp;
	private static ArrayList<ImageIcon> player_right, player_left, player_up, player_down;
	private ArrayList<ImageIcon> fireballSprites;
	private ArrayList<Tile> tileList; //used for collision
	private ArrayList<Projectile> projectileList;
	private Portal portal_1;
	private int map[][]= Map.mazeMap1; //for maze
	private int bossMap[][]= Map.bossMap; //for boss fight
	private int spriteCount_LeftRight=0; //up/down's arraylist max index is 3 while left/right's arraylist max index is 1
	private int spriteCount_UpDown=0; 
	private int spriteCount_Fireball=0;
	private static ImageIcon attackUpIcon;
	
	public Player(){ //used for player in game panel (maze)
		//loading player sprites for myDraw
		player_right = new ArrayList<ImageIcon>();
		player_left = new ArrayList<ImageIcon>();
		player_up = new ArrayList<ImageIcon>();
		player_down = new ArrayList<ImageIcon>();
		attackUpIcon = new ImageIcon("Other/attackUpIcon.png");
		for(int i=1; i<=2; i++){
			player_right.add(new ImageIcon("Sprites/Player/Player"+i+"_right.png"));
		}
		for(int i=1; i<=2; i++){
			player_left.add(new ImageIcon("Sprites/Player/Player"+i+"_left.png"));
		}
		for(int i=1; i<=4; i++){
			player_up.add(new ImageIcon("Sprites/Player/Player"+i+"_up.png"));
		}
		for(int i=1; i<=4; i++){
			player_down.add(new ImageIcon("Sprites/Player/Player"+i+"_down.png"));
		}
		tileList=new ArrayList<Tile>();
		int dx=0, dy=40;// initional position of the map
		for(int row = 0; row< map.length; row++ ){
			for(int col = 0; col < map[0].length; col++ ){
				if(map[row][col]==1){
					tileList.add(new Tile(dx+47*col,dy,47,47));
				}
				if(map[row][col]==2){
					portal_1=new Portal(dx+48*col,dy,47,47);
				}
			}     
			dy+=47; // reset x for a new row
		}
		facingRight=true;
		ammoCount=200;
		hp=25;
		speed=4;
		x=5;
		y=275;
		width=25;
		height=35;
		chestsCollected=0;
		projectileList = new ArrayList<Projectile>();
		poweredUp=false;
	}
	public Player(int x, int y){ //used for player in boss panel
		
		//loading player's projectile sprites
		fireballSprites = new ArrayList<ImageIcon>();
		for(int i=1; i<=4; i++){
			fireballSprites.add(new ImageIcon("Projectiles/Fireball/fireball_"+i+".png"));
		}
		portal_1=new Portal(0,0,47,47);
		tileList=new ArrayList<Tile>();
		int dx=0, dy=40;// initional position of the map
		for(int row = 0; row< bossMap.length; row++ ){
			for(int col = 0; col < bossMap[0].length; col++ ){
				if(bossMap[row][col]==1){
					tileList.add(new Tile(dx+47*col,dy,47,47)); //used for boundaries for boss fight
				}
			}     
			dy+=52; // reset x for a new row
		}
		shooting=false;
		this.x=x;
		this.y=y;
		width=25;
		height=35;
		projectileList = new ArrayList<Projectile>();
	}
	public void myDraw(Graphics g){
		//Draws player walking animations
		if(facingRight){
			//to make player's head bob up and down slightly when walking right
			if(spriteCount_LeftRight==1){
				g.drawImage(player_right.get(spriteCount_LeftRight).getImage(), x, y+1, width, height, null);
			}
			else if(spriteCount_LeftRight==0){
				g.drawImage(player_right.get(spriteCount_LeftRight).getImage(), x, y, width, height, null);
			}
		}
		else if(facingLeft){
			//to make player's head bob up and down slightly when walking left
			if(spriteCount_LeftRight==1){
				g.drawImage(player_left.get(spriteCount_LeftRight).getImage(), x, y+1, width, height, null);
			}
			else if(spriteCount_LeftRight==0){
				g.drawImage(player_left.get(spriteCount_LeftRight).getImage(), x, y, width, height, null);
			}
		}
		else if(facingUp){
			g.drawImage(player_up.get(spriteCount_UpDown).getImage(), x, y, width, height, null);
		}
		else if(facingDown){
			g.drawImage(player_down.get(spriteCount_UpDown).getImage(), x, y, width, height, null);
		}
		if(!facingRight&&!facingLeft&&!facingDown&&!facingUp){ //when not moving (idle)
			if(lastFacingDown){ //if player was last facing down, draws player facing down when idle
				g.drawImage(player_down.get(0).getImage(), x, y, width, height, null);
			}
			if(lastFacingUp){ //if player was last facing up, draws player facing up when idle
				g.drawImage(player_up.get(0).getImage(), x, y, width, height, null);
			}
			if(lastFacingRight){ //if player was last facing right, draws player facing right when idle
				g.drawImage(player_right.get(0).getImage(), x, y, width, height, null);
			}
			if(lastFacingLeft){ //if player was last facing left, draws player facing left when idle
				g.drawImage(player_left.get(0).getImage(), x, y, width, height, null);
			}
			
		}
		//Drawing player's projectiles
		for(int i=0; i<projectileList.size(); i++){
			g.drawImage(fireballSprites.get(spriteCount_Fireball).getImage(), (int)projectileList.get(i).getX(),(int)projectileList.get(i).getY(),(int)projectileList.get(i).getWidth(),(int)projectileList.get(i).getHeight(),null);
		}
		//Drawing player's healthbar
		g.setColor(Color.red);
		g.fillRect(x,y-8,25,3);
		if(hp>0){
			g.setColor(Color.green);
			g.fillRect(x,y-8,hp,3);
		}
		//Drawing powerup if potion is collected
		if(poweredUp){
			g.drawImage(attackUpIcon.getImage(), x+27, y-5, 13, 13, null);
		}
	}
	public void moveRight(){ //used to move the player to the right, and checks collision
		x+=speed;
		for(int i=0;i<tileList.size();i++){ //checking collision with solid tile
			if(this.intersects(tileList.get(i))){
				x-=speed;
				break;
			}
		}
		if(this.intersects(portal_1)){ //once reached end of maze, if entered portal
			Main.cardL.next(Main.cont);
			Main.bossP.setFocusable(true); //gives focus to boss panel
			Main.bossP.requestFocusInWindow();
			x=5;
			y=275;
			movingRight=false;
			start=true; //player can now take damage
		}
		if(x>1230){ //setting boundaries
			x=1230;
		}
	}
	public void moveLeft(){ //used to move the player to the left, and checks collision
		x-=speed;
		for(int i=0;i<tileList.size();i++){
			if(this.intersects(tileList.get(i))){
				x+=speed;
				break;
			}
		}
		if(x<0){ //boundaries
			x=0;
		}
	}
	public void moveUp(){ //used to move the player up, and checks collision
		y-=speed;
		for(int i=0;i<tileList.size();i++){
			if(this.intersects(tileList.get(i))){
				y+=speed;
				break;
			}
		}
		if(y<0){ //boundaries
			y=0;
		}
	}
	public void moveDown(){ //used to move the player down, and checks collision
		y+=speed;
		for(int i=0;i<tileList.size();i++){
			if(this.intersects(tileList.get(i))){
				y-=speed;
				break;
			}
		}
		if(y>630){ //boundaries
			y=630;
		}
	}
	//Setter methods
	public void setMovingRight(boolean movingRight){
		this.movingRight=movingRight;
	}
	public void setMovingLeft(boolean movingLeft){
		this.movingLeft=movingLeft;
	}
	public void setMovingUp(boolean movingUp){
		this.movingUp=movingUp;
	}
	public void setMovingDown(boolean movingDown){
		this.movingDown=movingDown;
	}
	public void setFacingRight(boolean facingRight){
		this.facingRight=facingRight;
	}
	public void setFacingLeft(boolean facingLeft){
		this.facingLeft=facingLeft;
	}
	public void setFacingUp(boolean facingUp){
		this.facingUp=facingUp;
	}
	public void setFacingDown(boolean facingDown){
		this.facingDown=facingDown;
	}
	public void addProjectile(){
		if(poweredUp){
			projectileList.add(new Projectile(x, y, 25, 30, 8)); //makes larger projectiles if player collected a potion
		}
		else{
			projectileList.add(new Projectile(x, y, 8));
		}
		ammoCount--;
	}
	public void removeProjectile(int index){
		projectileList.remove(index);
	}
	public void setShooting(boolean shooting){
		this.shooting = shooting;
	}
	public void takesDamage(int x){ //used in boss panel when player gets hit by boss projectile
		hp-=x;
	}
	public void addAmmo(int x){ //used when player collects a chest
		ammoCount+=x;
	}
	public void setAmmo(int x){ //used when player is defeated and wants to retry
		ammoCount=x;
	}
	public void setLastFacingUp(boolean lastFacingUp){
		this.lastFacingUp=lastFacingUp;
		lastFacingDown=false;
		lastFacingRight=false;
		lastFacingLeft=false;
	}
	public void setLastFacingDown(boolean lastFacingDown){
		this.lastFacingDown=lastFacingDown;
		lastFacingUp=false;
		lastFacingLeft=false;
		lastFacingRight=false;
	}
	public void setLastFacingRight(boolean lastFacingRight){
		this.lastFacingRight=lastFacingRight;
		lastFacingLeft=false;
		lastFacingUp=false;
		lastFacingDown=false;
	}
	public void setLastFacingLeft(boolean lastFacingLeft){
		this.lastFacingLeft=lastFacingLeft;
		lastFacingRight=false;
		lastFacingUp=false;
		lastFacingDown=false;
	}
	public void setHP(int x){ //used when player wants to retry boss fight
		hp = x;
	}
	public static void setStart(boolean isStart){ //used to change if player can take damage or not
		start=isStart;
	}
	public void removeAllProjectiles(){ //used to remove player's projectiles when player is defeated
		projectileList.clear();
	}
	public void nextSprite_LeftRight(){ //used for changing sprite when walking left/right
		spriteCount_LeftRight++;
		if(spriteCount_LeftRight>1){ //max index for player's left/right arraylists
			spriteCount_LeftRight=0; 
		}
	}
	public void nextSprite_UpDown(){ //used for changing sprite when walking up/down
		spriteCount_UpDown++;
		if(spriteCount_UpDown>3){ //max index for player's up/down arraylists
			spriteCount_UpDown=0;
		}
	}
	public void nextSprite_Fireball(){ //used for animating player's fireball projectile
		spriteCount_Fireball++;
		if(spriteCount_Fireball>3){ //max index for fireballSprites arraylist
			spriteCount_Fireball=0;
		}
	}
	public void collectsChest(){
		chestsCollected++;
	}
	public void setPoweredUp(boolean poweredUp){ //used when player collects a potion
		this.poweredUp=poweredUp;
	}
	//Getter methods
	public boolean isMovingRight(){
		return movingRight;
	}
	public boolean isMovingLeft(){
		return movingLeft;
	}
	public boolean isMovingUp(){
		return movingUp;
	}
	public boolean isMovingDown(){
		return movingDown;
	}
	public boolean isFacingLeft(){
		return facingLeft;
	}
	public boolean isFacingRight(){
		return facingRight;
	}
	public boolean isFacingDown(){
		return facingDown;
	}
	public boolean isFacingUp(){
		return facingUp;
	}
	public int getAmmoCount(){
		return ammoCount;
	}
	public int getHP(){
		return hp;
	}
	public Projectile getProjectile(int index){
		return projectileList.get(index);
	}
	public int getProjectileListSize(){
		return projectileList.size();
	}
	public boolean isShooting(){
		return shooting;
	}
	public boolean isStart(){ //used so that player won't take damage at certain times e.g. During game over screen
		return start;
	}
	public int getNumChestsCollected(){
		return chestsCollected;
	}
	public boolean isPoweredUp(){
		return poweredUp;
	}
}