package com.luluiz21games.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.luluiz21games.main.Game;
import com.luluiz21games.main.Sound;
import com.luluiz21games.world.Camera;
import com.luluiz21games.world.World;

public class Enemy extends Entity{
	
	private int maskX=8, maskY=8 , maskH=10, maskW=10;
	
	private int frames=0, maxFrames = 20;
	private BufferedImage[] enemySprite;
	private int index = 0, maxIndex = 1;
	private int life = 3;
	private int damageFrames = 10, damageCurrent =0;
	private int damageCauseFrames = 20, damageCauseCurrent =0;
	private boolean isEnemyDamage;
	private boolean isCauseDamage;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		setSpeed(1);
		enemySprite = new BufferedImage[4];
		for (int i = 0; i < enemySprite.length ; i++) {
			int aux = 16* i;
			enemySprite[i] = Game.spritesheet.getSprite(96 + aux, 0, 16, 16);
		}
		
		

	}
	
	public void tick() {
		if(!isCollidingWithPlayer()) {
			
			if(getX()<Game.player.getX() && World.isFree(this.getX()+this.getSpeed(), this.getY())
					&& !isColliding(this.getX()+this.getSpeed(), this.getY())) {
				
				setX(getX()+getSpeed());
			}
			else if(getX() > Game.player.getX() && World.isFree(getX()-getSpeed(), getY())
					&& !isColliding(getX()-getSpeed(), getY())) {
				
				setX(getX()-getSpeed());
			}
			else if(getY()<Game.player.getY() && World.isFree(getX(), getY()+getSpeed())
					&& !isColliding(this.getX(), this.getY()+getSpeed())) {
				
				setY(getY()+getSpeed());
			}
			else if(getY() > Game.player.getY() && World.isFree(getX(), getY()-getSpeed())
					&& !isColliding(this.getX(), this.getY()-getSpeed())) {
				
				setY(getY()-getSpeed());
			}
		
		}else {
			//colidindo
			if(Game.rand.nextInt(100)<10) {
				Sound.dano.play();
				Game.player.setLife(Game.player.getLife()-1);
				
				Game.player.setDamage(true);
				isCauseDamage = true;
				
			}
			if(Game.player.getLife() <= 0) {
				//System.exit(1);
			}
		}
		
		
		frames++;
		if(frames== maxFrames) {
			frames=0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
		
		collindingSpecial();
		
		if(life<=0) {
			destroySelf();
			return;
		}
		if(isEnemyDamage) {
			this.damageCurrent++;
			if(damageCurrent == damageFrames) {
				damageCurrent = 0;
				isEnemyDamage = false;
			}
		}
		if(isCauseDamage) {
			this.damageCauseCurrent++;
			if(damageCauseCurrent == damageCauseFrames) {
				damageCauseCurrent = 0;
				isCauseDamage = false;
			}
		}
		
	}
	
	public void destroySelf() {
		Game.entities.remove(this);
		Game.enemies.remove(this);
	}
	
	public void collindingSpecial() {
		for(int i = 0; i < Game.specialShoot.size();i++) {
			Entity e = Game.specialShoot.get(i);
			if(e instanceof SpecialShoot) {
				if(Entity.isCollinding(this, e)) {
					isEnemyDamage = true;
					life --;
					Sound.ount.play();
					Game.specialShoot.remove(i);
					return;
				}
			}
		}
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle currentEnemy = new Rectangle(this.getX() + maskX,this.getY()+maskY,maskW,maskH);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
		
		return currentEnemy.intersects(player);
	}
	
	public boolean isColliding(int nextX, int nextY){
		Rectangle currentEnemy = new Rectangle(nextX + maskX,nextY+maskY,maskW,maskH);
		
		for(int i = 0; i< Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(e.getX() + maskX,e.getY()+maskY,maskW,maskH);
			if(currentEnemy.intersects(targetEnemy)) {
				return true;
			}
			
		}
		
		return false;
	}
	
	public void render(Graphics g) {
		//super.render(g);
		if(isCauseDamage) {
			g.drawImage(enemySprite[3],this.getX() - Camera.x,this.getY() - Camera.y,null);
		}else if(isEnemyDamage){
			g.drawImage(enemySprite[2],this.getX() - Camera.x,this.getY() - Camera.y,null);
		}else {
			g.drawImage(enemySprite[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
		}
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW , maskH);
	}

}
