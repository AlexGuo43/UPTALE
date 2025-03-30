/*
Main
Responsible for creating game, menu, instructions, ending, and game over panel
ICS3U7 - Ms.Strelkovska
Oct 26
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class Main extends JFrame{
	static CardLayout cardL;
	static Container cont;
	private MenuPanel menu;
	private InstructionsPanel instructions;
	private static GamePanel game; 
	static BossPanel bossP;
	private EndingPanel ending;
	private GameOverPanel gameOver;

	public Main(){
		super("Uptale");
		cont=getContentPane();
		cardL=new CardLayout();
		cont.setLayout(cardL);
		this.setResizable(false);
	   
		menu = new MenuPanel(); //making panels
		instructions = new InstructionsPanel();
		game = new GamePanel();
		bossP = new BossPanel();
		ending = new EndingPanel();
		gameOver = new GameOverPanel();
	  
		cont.add("Menu Panel", menu); //adding panels to container
		cont.add("Instructions", instructions);
		cont.add("UPTALE", game);
		cont.add("Boss Fight", bossP);
		cont.add("Ending", ending);
		cont.add("Game Over", gameOver);
	}

	public static void main(String[] args) {
		Main m = new Main();
		m.setSize(1280, 720);
		m.setVisible(true);
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //Closes frame when x button pressed
	}
	
	class MenuPanel extends JPanel implements ActionListener{
		private ImageIcon bg1;
		private JButton startButton, instructionsButton, quitButton;
		private Font font1;
		public MenuPanel(){ //Constructor
			font1 = new Font("SansSerif", Font.BOLD, 24);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(Box.createRigidArea(new Dimension(0, 250))); //to put buttons in correct position
			
			bg1 = new ImageIcon("Backgrounds/menuBG.jpg");
			startButton = new JButton("       Start       "); //empty spaces are to make buttons the same length
			startButton.setFont(font1);
			instructionsButton = new JButton(" How To Play ");
			instructionsButton.setFont(font1);
			quitButton = new JButton("        Quit        ");
			quitButton.setFont(font1);
			startButton.addActionListener(this);
			instructionsButton.addActionListener(this);
			quitButton.addActionListener(this);
			
			startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // sets horizontal allignment to center
			instructionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add(startButton);
			this.add(Box.createRigidArea(new Dimension(0, 30))); //spacing out the buttons
			this.add(instructionsButton);
			this.add(Box.createRigidArea(new Dimension(0, 30)));
			this.add(quitButton);
			this.add(Box.createRigidArea(new Dimension(0, 30)));
			
		} //End of constructor
	
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(bg1.getImage(),0,0,1280,720,null);
		}
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==startButton){
				Main.cardL.show(Main.cont, "UPTALE");
				game.setFocusable(true); //This has to be set after switching to next panel because when you switch panels, there won't be focus on the new panel.
				game.requestFocusInWindow();
			}
			if(e.getSource()==instructionsButton){
				Main.cardL.next(Main.cont);
			}
			if(e.getSource()==quitButton){
				System.exit(0);
			}
		}
	}
	class InstructionsPanel extends JPanel implements ActionListener{
		private ImageIcon instructionsP;
		private JButton backButton;
		public InstructionsPanel(){
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(Box.createRigidArea(new Dimension(0, 650))); //to put buttons in correct position
			
			instructionsP = new ImageIcon("Backgrounds/instructionsP.png");
			backButton = new JButton("                   Back                   "); //spaces to make button more wide
			backButton.addActionListener(this);
			backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add(backButton);
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(instructionsP.getImage(),0,0,null);
		}
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==backButton){
				Main.cardL.first(Main.cont);
			}
		}
	}
	class GamePanel extends JPanel implements ActionListener, KeyListener{
		private JButton backButton;
		private ImageIcon tile1, bgTile1, portalTile, chest, potion;
		private int map[][]= Map.mazeMap1; //for maze
		private Player p1;
		private Timer t;
		private ArrayList<Potion> potionList;
		private int timerCount=0;
		private ArrayList<Chest> chestList;
		
		public GamePanel(){
			
			addKeyListener(this);
			p1 = new Player();
			this.setBackground(Color.BLACK);
            backButton=new JButton("Back"); //Takes you back to menu panel
	        backButton.addActionListener(this);
			
			//loading images
			tile1 = new ImageIcon("Tiles/tile1.png");
			bgTile1 = new ImageIcon("Tiles/bgTile1.png");
			portalTile = new ImageIcon("Tiles/portalTile.png");
			chest = new ImageIcon("Other/treasureChest.png");
			potion = new ImageIcon("Other/potion.png");
			
			//Adding chests
			chestList = new ArrayList<Chest>();
			chestList.add(new Chest(151, 97));
			chestList.add(new Chest(809, 97));
			chestList.add(new Chest(1185, 567));
			//Adding potions
			potionList = new ArrayList<Potion>();
			potionList.add(new Potion(57, 473));
			
	        this.add(backButton);
			t = new Timer(30, this);
			t.start();
		}	   
	    public void actionPerformed(ActionEvent e) {

		    if(e.getSource()==backButton){
				Main.cardL.first(Main.cont); //goes back to menu if back button is clicked
			}
			if(e.getSource()==t){
				timerCount=(timerCount+1)%100000;
				//Player's movement
				if(p1.isMovingRight()){
					if(timerCount%8==0){
						p1.nextSprite_LeftRight(); //walking animation
					}
					p1.moveRight();
					p1.setFacingRight(true);
					for(int i=0; i<chestList.size();i++){ //checking if player has collected a chest
						if(p1.intersects(chestList.get(i))){
							p1.addAmmo(50);
							p1.collectsChest();
							chestList.remove(i);
						}
					}
					for(int i=0; i<potionList.size();i++){ //checking if player has collected a chest
						if(p1.intersects(potionList.get(i))){
							p1.setPoweredUp(true);
							potionList.remove(i);
						}
					}
				}
				if(p1.isMovingUp()){
					if(timerCount%8==0){
						p1.nextSprite_UpDown(); //walking animation
					}
					p1.moveUp();
					p1.setFacingUp(true);
					for(int i=0; i<chestList.size();i++){ //checking if player has collected a chest
						if(p1.intersects(chestList.get(i))){
							p1.addAmmo(50);
							p1.collectsChest();
							chestList.remove(i);
						}
					}
					for(int i=0; i<potionList.size();i++){ //checking if player has collected a chest
						if(p1.intersects(potionList.get(i))){
							p1.setPoweredUp(true);
							potionList.remove(i);
						}
					}
				}
				if(p1.isMovingLeft()){
					if(timerCount%8==0){
						p1.nextSprite_LeftRight(); //walking animation
					}
					p1.moveLeft();
					p1.setFacingLeft(true);
					for(int i=0; i<chestList.size();i++){ //checking if player has collected a chest
						if(p1.intersects(chestList.get(i))){
							p1.addAmmo(50);
							p1.collectsChest();
							chestList.remove(i);
						}
					}
					for(int i=0; i<potionList.size();i++){ //checking if player has collected a chest
						if(p1.intersects(potionList.get(i))){
							p1.setPoweredUp(true);
							potionList.remove(i);
						}
					}
				}
				if(p1.isMovingDown()){
					if(timerCount%8==0){
						p1.nextSprite_UpDown(); //walking animation
					}
					p1.moveDown();
					p1.setFacingDown(true);
					for(int i=0; i<chestList.size();i++){ //checking if player has collected a chest
						if(p1.intersects(chestList.get(i))){
							p1.addAmmo(50);
							p1.collectsChest();
							chestList.remove(i);
						}
					}
					for(int i=0; i<potionList.size();i++){ //checking if player has collected a chest
						if(p1.intersects(potionList.get(i))){
							p1.setPoweredUp(true);
							potionList.remove(i);
						}
					}
				}
				repaint();
			}
		
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			int x=0, y=40;// initional position of the map
			for (int row = 0; row< map.length; row++ ){  //Drawing maze tiles
				for (int col = 0; col < map[0].length; col++){
					if(map[row][col]==0){
						g.drawImage(bgTile1.getImage(), x+47*col,y,47,47,null); 
					}
					if(map[row][col]==1){
						g.drawImage(tile1.getImage(), x+47*col,y,47,47,null); 
					}
					if(map[row][col]==2){
						g.drawImage(portalTile.getImage(), x+47*col,y,47,47,null);
					}
				}     
				y+=47; // reset x for a new row
			}
			for(int i=0; i<chestList.size(); i++){ //Drawing chests
				g.drawImage(chest.getImage(), (int)chestList.get(i).getX(),(int)chestList.get(i).getY(), (int)chestList.get(i).getWidth(), (int)chestList.get(i).getHeight(), null);
			}
			for(int i=0; i<potionList.size(); i++){ //Drawing potions
				g.drawImage(potion.getImage(), (int)potionList.get(i).getX(),(int)potionList.get(i).getY(), (int)potionList.get(i).getWidth(), (int)potionList.get(i).getHeight(), null);
			}
			p1.myDraw(g); //Draws the player, and player's healthbar
		}
		public void keyPressed(KeyEvent ke) {
			if(ke.getKeyCode()==68){ //if key "D" is pressed
				p1.setMovingRight(true);
				p1.setFacingRight(true);
			}
			if(ke.getKeyCode()==87){ //if key "W" is pressed
				p1.setMovingUp(true);
				p1.setFacingUp(true);
			}
			if(ke.getKeyCode()==65){ //if key "A" is pressed
				p1.setMovingLeft(true);
				p1.setFacingLeft(true);
			}
			if(ke.getKeyCode()==83){ //if key "S" is pressed
				p1.setMovingDown(true);
				p1.setFacingDown(true);
			}
		}

		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode()==68){ //if key "D" is released
				p1.setMovingRight(false);
				p1.setFacingRight(false);
				p1.setLastFacingRight(true); //used to know what to display when player is idle
			}
			if(e.getKeyCode()==87){ //if key "W" is released
				p1.setMovingUp(false);
				p1.setFacingUp(false);
				p1.setLastFacingUp(true); //used to know what to display when player is idle
			}
			if(e.getKeyCode()==65){ //if key "A" is released
				p1.setMovingLeft(false);
				p1.setFacingLeft(false);
				p1.setLastFacingLeft(true); //used to know what to display when player is idle
			}
			if(e.getKeyCode()==83){ //if key "S" is released
				p1.setMovingDown(false);
				p1.setFacingDown(false);
				p1.setLastFacingDown(true); //used to know what to display when player is idle
			}
		}
		public void keyTyped(KeyEvent e) {}
		
	}
	class BossPanel extends JPanel implements ActionListener, KeyListener{
		private JButton backButton;
		private JLabel bossName;
		private ImageIcon bg1;
		private Player p1;
		private Boss boss1;
		private Timer t;
		private int timerCount=0, random=0;
		private Portal endPortal;
		private ImageIcon portal;
		private Font font1;
		
		public BossPanel(){
			addKeyListener(this);
			p1 = new Player(640, 480); //Starting position
			boss1 = new Boss();
			
			bg1 = new ImageIcon("Backgrounds/bossBG.jpg");
			portal = new ImageIcon("Tiles/portal.png");
			endPortal = new Portal(300, 480, 47, 60);
			bossName = new JLabel("Abyssal Mage Lv: Unknown"); //appears above boss's healthbar
			font1 = new Font("SansSerif", Font.BOLD, 16);
			bossName.setForeground(Color.WHITE);
			bossName.setFont(font1);
			
			this.add(bossName);
			t = new Timer(30, this);
			t.start();
			
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==t){
				timerCount=(timerCount+1)%100000;
				//if player is defeated
				if(p1.getHP()<=0){
					Main.cardL.last(Main.cont); //takes user to the game over screen
					p1.removeAllProjectiles(); //removes projectiles that were shot before player was defeated
					p1.setStart(false); //so can't take damage during game over screen
					//reseting boss fight if player wants to try again
					p1.setHP(25); 
					p1.setShooting(false);
					p1.setMovingRight(false);
					p1.setMovingLeft(false);
					p1.setMovingUp(false);
					p1.setMovingDown(false);
					p1.setAmmo(200+(p1.getNumChestsCollected()*50)); //resets with original ammo count
					boss1.setHP(600);
					boss1.setPhase1(true);
					boss1.setPhase2(false);
					boss1.setPhase3(false);
				}
				//Player movement
				if(p1.isMovingRight()){
					if(timerCount%8==0){
						p1.nextSprite_LeftRight(); //walking animation
					}
					p1.moveRight();
					if(boss1.getHP()<=0&&p1.intersects(endPortal)){
						Main.cardL.next(Main.cont); //brings to end panel
						p1.setLocation(5, 275);
					}
				}
				if(p1.isMovingUp()){
					if(timerCount%8==0){
						p1.nextSprite_UpDown(); //walking animation
					}
					p1.moveUp();
					if(boss1.getHP()<=0&&p1.intersects(endPortal)){
						Main.cardL.next(Main.cont); //brings to end panel
						p1.setLocation(5, 275);
					}
				}
				if(p1.isMovingLeft()){
					if(timerCount%8==0){
						p1.nextSprite_LeftRight(); //walking animation
					}
					p1.moveLeft();
					if(boss1.getHP()<=0&&p1.intersects(endPortal)){
						Main.cardL.next(Main.cont); //brings to end panel
						p1.setLocation(5, 275);
					}
				}
				if(p1.isMovingDown()){
					if(timerCount%8==0){
						p1.nextSprite_UpDown(); //walking animation
					}
					p1.moveDown();
					if(boss1.getHP()<=0&&p1.intersects(endPortal)){
						Main.cardL.next(Main.cont); //brings to end panel
						p1.setLocation(5, 275);
					}
				}
				//Player's projectile section
				if(p1.isShooting() && timerCount%13==0){ //Spaces out the bullets
					if(p1.getAmmoCount()>0){ //If out of bullets, you can't shoot
						p1.addProjectile();
					}
				}
				
				for (int i=0 ; i<p1.getProjectileListSize(); i++) {
					p1.getProjectile(i).moveUp();
					if(p1.getProjectile(i).intersects(boss1)){ //if projectile hits the boss
						if(boss1.getHP()>0){ //only removes projectile if boss is alive
							p1.removeProjectile(i);
						}
						if(p1.isPoweredUp()){
							boss1.subtractFromHP(4); //player will do 1 extra damage if powered up
						}
						else{
							boss1.subtractFromHP(3);
						}
					}
					else if (p1.getProjectile(i).isExited()){ //if projectile goes out the screen
						p1.removeProjectile(i);
					}
				}
				if(timerCount%5==0){
					p1.nextSprite_Fireball(); //Animating player's projectiles
				}
				//Animating boss sprite
				if(timerCount%8==0){
					boss1.nextSprite();
				}
				//Boss fight mechanics, phase 1
				if(boss1.isPhase1()){
					if(timerCount%5==0){
							boss1.addProjectileP1();
					}
				}
				//Boss fight mechanics, phase 2
				if(boss1.isPhase2()){
					if(timerCount%8==0){
						boss1.addProjectileP2();
					}
				}
				//Boss fight mechanics, phase 3
				if(boss1.isPhase3()){
					if(timerCount%25==0){
						if(boss1.getHP()>0){ //Stops making projectiles when boss is defeated
							boss1.addProjectileP3();
						}
					}
				}
				for (int i=0 ; i<boss1.getProjectileListSize(); i++) {
					if(boss1.isPhase1()){ //phase 1 mechanics
						boss1.getProjectile(i).moveDown();
					}
					if(boss1.isPhase2()){ //phase 2 mechanics
						boss1.getProjectile(i).moveDown();
						if(timerCount%15==0){
							random = (int)(Math.random()*2);
						}
						if(random==0){
							boss1.getProjectile(i).moveLeft();
						}
						if(random==1){
							boss1.getProjectile(i).moveRight();
						}
					}
					if(boss1.isPhase3()){ //phase 3 mechanics
						boss1.getProjectile(i).moveDown();
					}
					if(boss1.getProjectile(i).intersects(p1)){ //if boss's projectile hits the player
						boss1.removeProjectile(i);
						if(p1.isStart()){
							if(boss1.isPhase1() || boss1.isPhase2()){
								p1.takesDamage(2);
							}
							else{
								p1.takesDamage(5); //in phase 3, player will take more damage
							}
						}
					}
					else if (boss1.getProjectile(i).isExited()){ //if projectile goes out the screen
						boss1.removeProjectile(i);
					}
				}
				if(boss1.getHP()<=0){ //removes boss's name after being defeated
					this.remove(bossName);
				}
				repaint();
			}
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(bg1.getImage(),0,0,1280,720,null); //draws background
			
			p1.myDraw(g); //draws the player, player's healthbar, and player's projectiles
			boss1.myDraw(g); //draws the boss, boss's healthbar, and boss's projectiles
			
			if(boss1.getHP()<=0){ //draws portal when boss is defeated
				g.drawImage(portal.getImage(), (int)endPortal.getX(), (int)endPortal.getY(), (int)endPortal.getWidth(), (int)endPortal.getHeight(), null);
			}
		}
		public void keyPressed(KeyEvent ke) {
			if(ke.getKeyCode()==68){ //if key "D" is pressed
				p1.setMovingRight(true);
				p1.setFacingRight(true);
			}
			if(ke.getKeyCode()==87){ //if key "W" is pressed
				p1.setMovingUp(true);
				p1.setFacingUp(true);
			}
			if(ke.getKeyCode()==65){ //if key "A" is pressed
				p1.setMovingLeft(true);
				p1.setFacingLeft(true);
			}
			if(ke.getKeyCode()==83){ //if key "S" is pressed
				p1.setMovingDown(true);
				p1.setFacingDown(true);
			}
			if(ke.getKeyCode()==32){ //if spacebar is pressed
				p1.setShooting(true);
			}
		}

		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode()==68){ //if key "D" is released
				p1.setMovingRight(false);
				p1.setFacingRight(false);
				p1.setLastFacingRight(true);
			}
			if(e.getKeyCode()==87){ //if key "W" is released
				p1.setMovingUp(false);
				p1.setFacingUp(false);
				p1.setLastFacingUp(true);
			}
			if(e.getKeyCode()==65){ //if key "A" is released
				p1.setMovingLeft(false);
				p1.setFacingLeft(false);
				p1.setLastFacingLeft(true);
			}
			if(e.getKeyCode()==83){ //if key "S" is released
				p1.setMovingDown(false);
				p1.setFacingDown(false);
				p1.setLastFacingDown(true);
			}
			if(e.getKeyCode()==32){ //if spacebar is released
				p1.setShooting(false);
			}
		}
		public void keyTyped(KeyEvent e) {}
	}
	class EndingPanel extends JPanel implements ActionListener{
		private ImageIcon endingBG;
		private JButton backButton;
		public EndingPanel(){
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(Box.createRigidArea(new Dimension(0, 600))); //to put buttons in correct position
			
			endingBG = new ImageIcon("Backgrounds/endingBG.png");
			backButton = new JButton("Back to Menu"); //spaces to make button more wide
			backButton.addActionListener(this);
			backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add(backButton);
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(endingBG.getImage(),0,0,null);
		}
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==backButton){
				Main.cardL.first(Main.cont);
			}
		}
		
	}
	class GameOverPanel extends JPanel implements ActionListener{
		private ImageIcon gameOverBG;
		private JButton retryButton;
		private JButton quitButton;
		public GameOverPanel(){
			
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(Box.createRigidArea(new Dimension(0, 600))); //to put buttons in correct position
			
			gameOverBG = new ImageIcon("Backgrounds/gameOverBG.png");
			retryButton = new JButton("Retry");
			retryButton.addActionListener(this);
			quitButton = new JButton("Quit");
			quitButton.addActionListener(this);
			retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add(retryButton);
			this.add(Box.createRigidArea(new Dimension(0, 10))); //spaces the buttons
			this.add(quitButton);
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(gameOverBG.getImage(),0,0,null);
		}
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==retryButton){
				Main.cardL.show(Main.cont, "Boss Fight");
				Main.bossP.setFocusable(true); //gives focus to boss panel
				Main.bossP.requestFocusInWindow();
				Player.setStart(true);
			}
			if(e.getSource()==quitButton){
				System.exit(0);
			}
		}
	}
}