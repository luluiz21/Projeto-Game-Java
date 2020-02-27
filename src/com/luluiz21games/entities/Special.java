package com.luluiz21games.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.luluiz21games.main.Game;
import com.luluiz21games.world.Camera;

public class Special extends Entity{
	
	private int index= 0, maxIndex = 3;
	private int frames = 0, maxFrames = 5;
	
	private BufferedImage[] special;
	
	public Special(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		special = new BufferedImage[4];
		
		for(int i = 0; i < 4; i++) {
			special[i] = Game.spritesheet.getSprite((16*5) + (i*16),16,16,16);
		}
	}
	
	public void tick() {
		frames++;
		if(frames== maxFrames) {
			frames=0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
			
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(special[index], this.getX() - Camera.x,this.getY() - Camera.y ,null);
	}
	
	
}
