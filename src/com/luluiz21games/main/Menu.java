package com.luluiz21games.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.luluiz21games.world.World;



public class Menu {
	
	private String[] options = {"Novo jogo","Carregar jogo","Sair"};
	
	private int currentOption = 0;
	private int maxOption = options.length-1;
	
	private boolean down;
	private boolean up;
	private boolean enter;
	
	public void tick() {
		
		if(up) {
			up = false;
			if(currentOption>0) {
				currentOption--;
			}else {
				currentOption = maxOption;
			}
		}
		if(down) {
			down = false;
			if(currentOption < maxOption) {
				currentOption++;
			}else {
				currentOption = 0;
			}
		}
		if(enter) {
			enter = false;
			switch (options[currentOption]) {
			case "Novo jogo": {
				Game.gameState = "NORMAL";
				int CUR_LEVEL = 1;
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
				break;
				
			}case "Carregar jogo":{
				break;
				
			}case "Sair":
				System.exit(1);
				break;
			}
		}
		
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE,Game.HEIGHT*Game.SCALE);
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,36));
		g.drawString("COROTINHO", Game.WIDTH*Game.SCALE/2-110, 50);
		
		//Menu options
		g.setFont(new Font("arial",Font.BOLD,20));
		g.drawString(options[0], Game.WIDTH*Game.SCALE/2-50, Game.HEIGHT*Game.SCALE/2 - 70);
		g.drawString(options[1], Game.WIDTH*Game.SCALE/2-70, Game.HEIGHT*Game.SCALE/2 );
		g.drawString(options[2], Game.WIDTH*Game.SCALE/2-20, Game.HEIGHT*Game.SCALE/2 + 70);
		
		if(options[currentOption] == "Novo jogo") {
			g.drawString(">", Game.WIDTH*Game.SCALE/2-100, Game.HEIGHT*Game.SCALE/2 - 70);
		}
		if(options[currentOption] == "Carregar jogo") {
			g.drawString(">", Game.WIDTH*Game.SCALE/2-100, Game.HEIGHT*Game.SCALE/2 );
		}
		if(options[currentOption] == "Sair") {
			g.drawString(">", Game.WIDTH*Game.SCALE/2-100, Game.HEIGHT*Game.SCALE/2 +70);
		}
		
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isEnter() {
		return enter;
	}

	public void setEnter(boolean enter) {
		this.enter = enter;
	}
	public int getcurrentOption() {
		return currentOption;
	}
	public void setcurrentOption(int currentOption) {
		this.currentOption = currentOption;
	}

}
