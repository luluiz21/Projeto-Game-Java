package com.luluiz21games.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.luluiz21games.main.Game;
import com.luluiz21games.main.Sound;
import com.luluiz21games.world.Camera;

public class SpecialShoot extends Entity{
	private double dx;
	private double dy;
	private double spd = 2;
	private int life = 30, curLife = 0;

	public SpecialShoot(int x, int y, int width, int height, BufferedImage sprite, double dx,double dy) {
		super(x, y, width, height, sprite);
		Sound.piu.play();
		this.dx = dx;
		this.dy = dy;
		
	}
	
	public void tick() {
		this.setX( dx * spd + getXDouble());
		this.setY( dy * spd + getYDouble());
		curLife++;
		if (curLife == life) {
			Game.specialShoot.remove(this);
			return;
		}
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX()- Camera.x , this.getY() - Camera.y , getWidth(), getHeight());
	}
	
}
