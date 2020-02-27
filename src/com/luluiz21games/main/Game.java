package com.luluiz21games.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;


import com.luluiz21games.entities.Enemy;
import com.luluiz21games.entities.Entity;
import com.luluiz21games.entities.Player;
import com.luluiz21games.entities.Special;
import com.luluiz21games.entities.SpecialShoot;
import com.luluiz21games.grafico.Spritsheet;
import com.luluiz21games.grafico.UI;
import com.luluiz21games.world.Camera;
import com.luluiz21games.world.World;





public class Game extends Canvas implements Runnable, KeyListener, MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private int CUR_LEVEL = 1, MAX_LEVEL =2;
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<SpecialShoot> specialShoot;
	public static Spritsheet spritesheet;
	public static World world;
	public static Random rand;

	
	public static String gameState = "MENU";
	private boolean showMessageGameOver=true;
	private int framesGameOver = 0;
	private boolean restartGame;
	
	public static Player player;
	public UI ui;
	
	public static Menu menu;
	public static Pause pause;
	
	public Game(){
		
		//Sound.music.loop();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		//Inicializando Objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		specialShoot = new ArrayList<SpecialShoot>();
		
		spritesheet = new Spritsheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		menu = new Menu();
		pause = new Pause();
		
	}
	
	public void initFrame() {
		frame = new JFrame("Meu joguinho");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
		thread.join();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	
	}
	
	
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		if(gameState == "NORMAL") {
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			for (int i = 0; i < specialShoot.size(); i++) {
				specialShoot.get(i).tick();
			}
			
			if(enemies.isEmpty()) {
				CUR_LEVEL++;
				if (CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
			}
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver=0;
					if(this.showMessageGameOver) {
						this.showMessageGameOver = false;
					}else {
						this.showMessageGameOver = true;
					}
			}
			if(restartGame) {
				restartGame=false;
				this.gameState ="NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
			}
			
		}else if(gameState == "MENU") {
			menu.tick();
		}else if (gameState == "PAUSE") {
			pause.tick();
		}
		
	}
	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		//renderizar jogo
		world.render(g);
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for (int i = 0; i < specialShoot.size(); i++) {
			specialShoot.get(i).render(g);
		}
		
		ui.render(g);
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
		g.setColor(Color.white);
		g.setFont(new Font("Courier",Font.CENTER_BASELINE,15));
		g.drawString(Game.player.getLife()+"/"+Game.player.getMaxLife(),60,24);
		g.drawString("Especial:" + player.isSpecial() ,580,24);
		
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setColor(Color.white);
			g.setFont(new Font("Courier",Font.BOLD,50));
			g.drawString("Game Over!",WIDTH*SCALE/3 - 20,HEIGHT*SCALE/2 - 50);
			g.setFont(new Font("Courier",Font.BOLD,20));
			if(showMessageGameOver) {
			g.drawString(">Clica enter e tenta de novo<",WIDTH*SCALE/3 - 45,HEIGHT*SCALE/2+20);
			}
		}else if(gameState == "MENU") {
			menu.render(g);
		}else if(gameState == "PAUSE") {
			pause.render(g);
		}
		
		
		bs.show();
	}
	
	
	public void run(){
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns= 1000000000/amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning){
			long now= System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			if(delta>=1){
				tick();
				render();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() - timer>=1000){
				//System.out.println("FPS: " + frames);
				frames = 0;
				timer+=1000;
			}
		}
		stop();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT|| 
				e.getKeyCode() == KeyEvent.VK_D) {
			
			player.setRight(true);
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT|| 
				e.getKeyCode() == KeyEvent.VK_A) {
		
			player.setLeft(true);
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP|| 
				e.getKeyCode() == KeyEvent.VK_W) {
						
			player.setUp(true);
			
			if(gameState == "MENU" ) {
				menu.setUp(true);
			}
			if(gameState == "PAUSE") {
				pause.setUp(true);
			}
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN|| 
				e.getKeyCode() == KeyEvent.VK_S) {
			
			player.setDown(true);
			
			if(gameState == "MENU" ) {
				menu.setDown(true);
			}
			if(gameState == "PAUSE") {
				pause.setDown(true);
			}
			
		}
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.setSpecialShoot(true);
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(gameState == "MENU" ) {
				menu.setEnter(true);
			}
			if(gameState == "PAUSE") {
				pause.setEnter(true);
			}
			if(gameState == "GAME_OVER") {
				restartGame = true;
			}

		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if(gameState=="NORMAL") {
				gameState = "PAUSE";
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT|| 
				e.getKeyCode() == KeyEvent.VK_D) {
			System.out.println("a");
			player.setRight(false);
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT|| 
				e.getKeyCode() == KeyEvent.VK_A) {
			System.out.println("b");
			player.setLeft(false);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP|| 
				e.getKeyCode() == KeyEvent.VK_W) {
			System.out.println("c");
			player.setUp(false);
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN|| 
				e.getKeyCode() == KeyEvent.VK_S) {
			System.out.println("d");
			player.setDown(false);
			
		}if(e.getKeyCode() == KeyEvent.VK_X) {
			player.setSpecialShoot(false);
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.setSpecialMouseShoot(true);
		player.setMx(e.getX() /3);
		player.setMy(e.getY() /3);
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}

