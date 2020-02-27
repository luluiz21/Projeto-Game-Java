package com.luluiz21games.grafico;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.luluiz21games.entities.Player;
import com.luluiz21games.main.Game;

public class UI {
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8, 4,50, 5);		
		g.setColor(Color.green);		
		g.fillRect(8, 4, (int) (((double)Game.player.getLife()/Game.player.getMaxLife())*50), 5);
		
		
	}
}
