package com.ephemerality.aphelion.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.ephemerality.aphelion.graphics.LoadManager;
import com.ephemerality.aphelion.graphics.ScreenManager;
import com.ephemerality.aphelion.input.InputManager;
import com.ephemerality.aphelion.input.Save;
import com.ephemerality.aphelion.spawn.entities.EntityManager;
import com.ephemerality.aphelion.spawn.entities.player.Player;
import com.ephemerality.aphelion.spawn.world.Level;
import com.ephemerality.aphelion.spawn.world.MapManager;
import com.ephemerality.aphelion.spawn.world.Warp;
import com.ephemerality.aphelion.spawn.world.script.ScriptManager;
import com.ephemerality.aphelion.ui.UIManager;
import com.ephemerality.aphelion.util.FileManager;
import com.ephemerality.aphelion.util.debug.Debug;

public class GameManager {
	
	public ScriptManager script;
	public EntityManager ent;
	public MapManager map;
	public UIManager ui;
	public Save save;
	
	public boolean isPaused;
	
	public static float playTime;
	public static boolean requestedWarp;
	public static Warp warpTo;
	
	
	public GameManager(ScreenManager screen, LoadManager assets, String name) {
		map = new MapManager();
		ent = new EntityManager(screen, assets, map);
		ui = new UIManager(ent.getPlayer());
		script = new ScriptManager(this);
		save = new Save(name);
		rect = new Rectangle(Gdx.graphics.getWidth() - map.mapWidth, Gdx.graphics.getHeight() - map.mapHeight, map.mapWidth, map.mapHeight);
	}
	public GameManager(ScreenManager screen, LoadManager assets, Save save) {
		this.save = save;
		map = new MapManager();
		ent = new EntityManager(screen, assets, map);
		ui = new UIManager(ent.getPlayer());
		script = new ScriptManager(this);
		rect = new Rectangle(Gdx.graphics.getWidth() - map.mapWidth, Gdx.graphics.getHeight() - map.mapHeight, map.mapWidth, map.mapHeight);
	}
	public void update() {
		if(!isPaused) {
			playTime += Gdx.graphics.getRawDeltaTime();
			ent.update();
			script.update();
			if(requestedWarp) {
				warp(warpTo);
				requestedWarp = false;
			}
		}
		if(InputManager.checkForPause()) {
			isPaused = !isPaused;
			ui.setPause(isPaused);
		}
		ui.update();
	}
	public static void requestWarp(Warp warp) {
		requestedWarp = true;
		warpTo = warp;
	}
	public Warp warp(Warp warp) {
		if(!warp.inLevel) {
			String name = warp.getDestination();
			loadLevel(name, "maps/" + name + Level.EXTENSION, false);
		}
		Player player = ent.getPlayer();
		warp.positionBody(player.body);
		player.screen.setPosition(player.body.x, player.body.y);
		return warp;
	}
	public void loadLevel(String name, String path, boolean absolutepath) {
		map.load(name, path, absolutepath);
		ent.refreshQuad(map);
	}
	public void resizeLevel(int w, int h) {
		map.resize(w, h);
		ent.refreshQuad(map);
	}
	public void render(ScreenManager screen) {
		//Background
		screen.getSpriteBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		map.renderBackGround(screen);
		ent.render(screen);
		screen.getSpriteBatch().flush();
		
		//Alpha
		map.renderAlphaMask(screen);
		screen.renderFixedRectangle(rect, Color.BLACK, rect.x, rect.y);
		
		//ForeGround
		map.renderForeGround(screen, ent);
		ui.render(screen);
		script.render(screen);
		screen.getSpriteBatch().flush();
	}
	//MinimapDebug
	Rectangle rect;
	
	public boolean save() {
//		C:\Users\Josh\Documents\NecroHero
		byte[] data = save.toByteArray(this);
		if(FileManager.writeToFile(Gdx.files.getExternalStoragePath() + "Documents\\NecroHero\\" + save.getFormattedName() + Save.EXTENSION, data, true)) {
			Debug.pushToConsole("Saved to \"" + Gdx.files.getExternalStoragePath() + "Documents\\NecroHero\\" + save.getFormattedName() + Save.EXTENSION + "\"", false);
			return true;
		}
		return false;
	}
	public void dispose() {
		
	}
}