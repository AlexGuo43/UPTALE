/*
Boss
Responsible for everything to do with the boss
ICS3U7 - Ms.Strelkovska
Nov 3
*/
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Boss extends Rectangle{
	private int hp; //health
	private boolean phase1, phase2, phase3;
	private ArrayList<Projectile> projectileList;
	private ImageIcon mageball_1, arcaneball_1;
	private ArrayList<ImageIcon> bossSprites_Phase1, bossSprites_Phase2, bossSprites_Phase3;
	private int spriteCount=0;
	public Boss(){
		phase1=true; //starts at phase 1
		phase2=false;
		phase3=false;
		x=550;
		y=60;
		height=250;
		width=200;
		hp=600;
		//loading sprite animations
		bossSprites_Phase1 = new ArrayList<ImageIcon>();
		bossSprites_Phase2 = new ArrayList<ImageIcon>();
		bossSprites_Phase3 = new ArrayList<ImageIcon>();
		
		for(int i=1; i<=8; i++){
			bossSprites_Phase1.add(new ImageIcon("Sprites/BossPhase1/Boss"+i+"_phase1.png"));
		}
		for(int i=1; i<=8; i++){
			bossSprites_Phase2.add(new ImageIcon("Sprites/BossPhase2/Boss"+i+"_phase2.png"));
		}
		for(int i=1; i<=8; i++){
			bossSprites_Phase3.add(new ImageIcon("Sprites/BossPhase3/Boss"+i+"_phase3.png"));
		}
		//loading projectiles
		projectileList = new ArrayList<Projectile>();
		mageball_1 = new ImageIcon("Projectiles/mageball_1.png");
		arcaneball_1 = new ImageIcon("Projectiles/arcaneball_1.png");
	}
	public void myDraw(Graphics g){ //used in boss panel's paintComponent
		//Drawing boss
		if(phase1){
			g.drawImage(bossSprites_Phase1.get(spriteCount).getImage(), x, y, width, height, null);
		}
		else if(phase2){
			g.drawImage(bossSprites_Phase2.get(spriteCount).getImage(), x-50, y, width+50, height, null);
		}
		else{
			if(hp>0){ //won't draw once boss is defeated
				g.drawImage(bossSprites_Phase3.get(spriteCount).getImage(), x, y, width, height, null);
			}
		}
		//Drawing boss's projectiles
		for(int i=0; i<projectileList.size(); i++){ 
			if(phase1 || phase2){
				g.drawImage(mageball_1.getImage(), (int)projectileList.get(i).getX(),(int)projectileList.get(i).getY(),(int)projectileList.get(i).getWidth(),(int)projectileList.get(i).getHeight(),null);
			}
			else{
				g.drawImage(arcaneball_1.getImage(), (int)projectileList.get(i).getX(),(int)projectileList.get(i).getY(),(int)projectileList.get(i).getWidth(),(int)projectileList.get(i).getHeight(),null);
			}
		}
		//Drawing boss's healthbar
		if(hp>0){
			g.setColor(Color.red);
			g.fillRect(x-width, 30, 600, 20);
			g.setColor(Color.green);
			g.fillRect(x-width, 30, hp, 20);
		}
	}
	//Setter methods
	public void subtractFromHP(int x){ //used when boss collides with player's projectile
		hp-=x;
	}
	public void addProjectileP1(){ //P1=Phase 1
		projectileList.add(new Projectile((int)(Math.random()*1281), 310, 45, 50, 8));
	}
	public void addProjectileP2(){ //P2=Phase 2
		projectileList.add(new Projectile((int)(Math.random()*1281), 310, 50, 55, 6));
	}
	public void addProjectileP3(){ //P3=Phase 3
		projectileList.add(new Projectile((int)(Math.random()*1281), 310, 150, 200, 3));
	}
	public void removeProjectile(int index){
		projectileList.remove(index);
	}
	public void setHP(int x){ //used when player wants to retry boss fight
		hp=x;
	}
	public void setPhase1(boolean phase1){ //used when player wants to retry boss fight
		this.phase1=phase1;
	}
	public void setPhase2(boolean phase2){ //used when player wants to retry boss fight
		this.phase2=phase2;
	}
	public void setPhase3(boolean phase3){ //used when player wants to retry boss fight
		this.phase3=phase3;
	}
	public void nextSprite(){ //used for animating boss's sprites
		spriteCount++;
		if(spriteCount>7){
			spriteCount=0; //max index for boss sprite's arraylists
		}
	}
	//Getter methods
	public int getHP(){
		return hp;
	}
	public boolean isPhase1(){
		if(hp<400){
			phase1=false;
			phase2=true;
		}
		return phase1;
	}
	public boolean isPhase2(){
		if(hp<200){
			phase2=false;
			phase3=true;
		}
		return phase2;
	}
	public boolean isPhase3(){
		return phase3;
	}
	public Projectile getProjectile(int index){
		return projectileList.get(index);
	}
	public int getProjectileListSize(){
		return projectileList.size();
	}
}