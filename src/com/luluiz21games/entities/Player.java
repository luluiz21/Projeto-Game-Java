package com.luluiz21games.entities;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;



import com.luluiz21games.grafico.Spritsheet;
import com.luluiz21games.main.Game;
import com.luluiz21games.main.Sound;
import com.luluiz21games.world.Camera;
import com.luluiz21games.world.World;

public class Player extends Entity{
	
	private boolean right, up, left, down;
	private final int rightDir=0,leftDir=1,upDir=2,downDir=3;
	private int dir = rightDir;
	private boolean moved;
	private int frames=0, maxFrames = 10;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] specialPlayer;
	private BufferedImage damagePlayer;
	private int index = 0, maxIndex = 2;
	private int life = 100 , maxLife = 100;
	private boolean special = false;
	private boolean isDamage= false;
	private int damageFrames = 0;
	private int specialFrames = 0, maxSpecialFrames =5;
	private int specialIndex = 0, maxSpecialIndex = 4;
	private boolean specialShoot;
	private boolean specialMouseShoot;
	private int mx,my;
	

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[3];
		upPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		downPlayer = new BufferedImage[3];
		specialPlayer = new BufferedImage[4];
		damagePlayer =  Game.spritesheet.getSprite(0,16,16,16);
		
		for(int i = 0; i < 3; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16),32,16,16);
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16),48,16,16);
			upPlayer[i] = Game.spritesheet.getSprite(32 + (i*16),16,16,16);
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i*16),0,16,16);
		}
		for(int i = 0; i < specialPlayer.length; i++) {
			specialPlayer[i] = Game.spritesheet.getSprite((16*5) + (i*16),32,16,16);
		}
	}
	
	
	
	
	public void tick() {
		moved = false;
		setSpeed(2);
		if(right && World.isFree(this.getX()+this.getSpeed(), this.getY())) {
			moved=true;
			setX(getX()+getSpeed());
			dir = rightDir;
			
		}else if(left && World.isFree(getX()-getSpeed(), getY())) {
			moved=true;
			setX(getX()-getSpeed());
			dir = leftDir;
			moved = true;
		}
		if(up && World.isFree(getX(), getY()-getSpeed())) {
			moved=true;
			setY(getY()-getSpeed());
			dir = upDir;
			moved = true;
		}
		else if(down && World.isFree(getX(), getY()+getSpeed())) {
			moved=true;
			setY(getY()+getSpeed());
			dir = downDir;
			moved = true;
		}
		if(moved) {
			frames++;
			if(frames== maxFrames) {
				frames=0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
				
			}
		}
		if(isSpecial()) {
			specialFrames++;
			if(specialFrames>=maxSpecialFrames) {
				specialFrames = 0;
				specialIndex++;
				if(specialIndex >= maxSpecialIndex) {
					specialIndex=0;
				}
			}
			
			if(isSpecialShoot() ) {
				setSpecialShoot(false);
				int dx = 0;
				int dy = 0;
				int px =7;
				int py = 7;
				switch(dir) {
				case rightDir:
					dx=1;
					break;
				case leftDir:
					dx=-1;
					break;
				case upDir:
					dy = -1;
					break;
				case downDir:
					dy = 1;
					break;
				}
				
				SpecialShoot specialShoot = new SpecialShoot(this.getX()+ px,this.getY() + py, 3,3,null,dx,dy);
				
				Game.specialShoot.add(specialShoot);
			}
			if(isSpecialMouseShoot() ) {
				setSpecialMouseShoot(false);
				int px = 8;
				int py = 8;
				
				
				
				double angle = Math.atan2(my - (this.getY()+py - Camera.y),mx - (this.getX()+px - Camera.x));
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				
				
				
				System.out.println(dx);
				System.out.println(dy);
				
				//System.out.println(angle);
				
				
				
				SpecialShoot specialShoot = new SpecialShoot(this.getX() + px, this.getY() + py , 3,3,null,dx,dy);
				Game.specialShoot.add(specialShoot);
			}
			
		}
		
		checkcollisionLife();
		checkcollisionSpecial();
		if(isDamage) {
			this.damageFrames++;
			if(this.damageFrames==5) {
				isDamage = false;
				damageFrames = 0;
			}
		}
		if(Game.player.getLife()<=0) {
			Game.gameState = "GAME_OVER";
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void render(Graphics g) {
		
		if(isSpecial()) {
			g.drawImage(specialPlayer[specialIndex], this.getX() - Camera.x,this.getY() - Camera.y,null);
		}		
		if(!isDamage) {
			if(dir == rightDir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y,null);
			} if(dir ==leftDir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y,null);
			} if(dir == upDir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y,null);
			} if(dir == downDir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y,null);
			}
		}else {
			g.drawImage(damagePlayer, this.getX() - Camera.x,this.getY() - Camera.y,null);
		}
		
	}
	
	public void setUp(boolean up) {
		this.up = up;
	}
	public boolean getUp() {
		return up;
	}
	public void setRight(boolean right) {
		this.right = right;
	}
	public boolean getRight() {
		return right;
	}
	public void setLeft(boolean left) {
		this.left = left;
	}
	public boolean getLeft() {
		return left;
	}
	public void setDown(boolean down) {
		this.down = down;
	}
	public boolean getDown() {
		return down;
	}
	
	public void checkcollisionLife(){
		for(int i = 0; i < Game.entities.size();i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof LifePack) {
				if(Entity.isCollinding(this, atual)) {
					life+=20;
					if(life >=80) {
						life = 100;
					}
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkcollisionSpecial(){
		for(int i = 0; i < Game.entities.size();i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Special) {
				if(Entity.isCollinding(this, atual)) {
					special = true;
					Game.entities.remove(atual);
				}
			}
		}
	}



	public BufferedImage[] getRightPlayer() {
		return rightPlayer;
	}



	public void setRightPlayer(BufferedImage[] rightPlayer) {
		this.rightPlayer = rightPlayer;
	}



	public BufferedImage[] getLeftPlayer() {
		return leftPlayer;
	}



	public void setLeftPlayer(BufferedImage[] leftPlayer) {
		this.leftPlayer = leftPlayer;
	}



	public BufferedImage[] getUpPlayer() {
		return upPlayer;
	}



	public void setUpPlayer(BufferedImage[] upPlayer) {
		this.upPlayer = upPlayer;
	}



	public BufferedImage[] getDownPlayer() {
		return downPlayer;
	}



	public void setDownPlayer(BufferedImage[] downPlayer) {
		this.downPlayer = downPlayer;
	}




	public int getLife() {
		return life;
	}




	public void setLife(int life) {
		this.life = life;
	}




	public int getMaxLife() {
		return maxLife;
	}




	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}




	public boolean isSpecial() {
		return special;
	}




	public boolean isDamage() {
		return isDamage;
	}




	public void setDamage(boolean isDamage) {
		this.isDamage = isDamage;
	}




	public boolean isSpecialShoot() {
		return specialShoot;
	}




	public void setSpecialShoot(boolean specialShoot) {
		this.specialShoot = specialShoot;
	}




	public boolean isSpecialMouseShoot() {
		return specialMouseShoot;
	}




	public void setSpecialMouseShoot(boolean specialMouseShoot) {
		this.specialMouseShoot = specialMouseShoot;
	}




	public int getMy() {
		return my;
	}




	public void setMy(int my) {
		this.my = my;
	}




	public int getMx() {
		return mx;
	}




	public void setMx(int mx) {
		this.mx = mx;
	}

	

}
