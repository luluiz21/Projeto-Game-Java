package com.luluiz21games.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.luluiz21games.main.Game;
import com.luluiz21games.world.Camera;


public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(5*16, 0, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage SPECIAL_EN = Game.spritesheet.getSprite(5*16, 16, 16, 16);
	
	private int maskX,maskY,maskW,maskH;
	
	private double x;
	private double y;
	private int width;
	private int height;
	private double speed;
	
	private BufferedImage sprite;
	
	public Entity(double x, double y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		this.maskX=0;
		this.maskY = 0;
		this.maskH = height;
		this.maskW = width;
	}
	
	public void setMask(int maskX, int maskY, int maskW,int maskH) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.maskW = maskW;
		this.maskH = maskH;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public int getX() {
		return (int) x;
	}
	public double getXDouble() {
		return x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getY() {
		return (int) y;
	}
	public double getYDouble() {
		return y;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getHeight() {
		return height;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getWidth() {
		return width;
	}
	
	public void tick() {
		
	}
	
	public static boolean isCollinding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX,e1.getY()+ e1.maskY,e1.maskW,e1.maskH);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX,e2.getY()+ e2.maskY,e2.maskW,e2.maskH);
		
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW,maskH);
	}

	public int getSpeed() {
		return (int)speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
}
