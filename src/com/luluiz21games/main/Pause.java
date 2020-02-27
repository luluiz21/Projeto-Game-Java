package com.luluiz21games.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Pause extends Menu{
	private String[] pauseOptions = {"Continuar","Menu Principal","Sair"};
	private int maxOption = pauseOptions.length - 1;
	public Pause() {
		setOptions(pauseOptions);
	}
	public void tick() {
		if(isUp()) {
			setUp(false);
			if(this.getcurrentOption()>0) {
				setcurrentOption(getcurrentOption()-1);
			}else {
				setcurrentOption(maxOption);
			}
		}
		if(isDown()) {
			setDown(false);;
			if(this.getcurrentOption() < maxOption) {
				setcurrentOption(getcurrentOption()+1);
			}else {
				setcurrentOption(0);
			}
		}
		if(isEnter()) {
			setEnter(false);
			switch (pauseOptions[getcurrentOption()]) {
			case "Continuar": {
				Game.gameState = "NORMAL";
				break;
				
			}case "Menu Principal":{
				Game.gameState = "MENU";
				break;
				
			}case "Sair":
				System.exit(1);
				break;
			}
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE,Game.HEIGHT*Game.SCALE);
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,36));
		g.drawString("COROTINHO", Game.WIDTH*Game.SCALE/2-110, 50);
		
		//Menu options
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString(pauseOptions[0], Game.WIDTH*Game.SCALE/2-50, Game.HEIGHT*Game.SCALE/2 - 70);
		g.drawString(pauseOptions[1], Game.WIDTH*Game.SCALE/2-70, Game.HEIGHT*Game.SCALE/2 );
		g.drawString(pauseOptions[2], Game.WIDTH*Game.SCALE/2-20, Game.HEIGHT*Game.SCALE/2 + 70);
		
		if(pauseOptions[getcurrentOption()] == "Continuar") {
			g.drawString(">", Game.WIDTH*Game.SCALE/2-100, Game.HEIGHT*Game.SCALE/2 - 70);
		}
		if(pauseOptions[getcurrentOption()] == "Menu Principal") {
			g.drawString(">", Game.WIDTH*Game.SCALE/2-100, Game.HEIGHT*Game.SCALE/2 );
		}
		if(pauseOptions[getcurrentOption()] == "Sair") {
			g.drawString(">", Game.WIDTH*Game.SCALE/2-100, Game.HEIGHT*Game.SCALE/2 +70);
		}
	}
}
