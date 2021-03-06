package com.ephemerality.aphelion.spawn.entities.nob;

import com.ephemerality.aphelion.spawn.entities.nob.Nob;

public class Tile extends Nob{
	/**
	 * All ID numbers are 5 digit.
	 * First Digit signifies what type the entity is,
	 * 		0 for null type
	 * 		1 for tiles
	 * 		2 for environment tiles
	 * 
	 * The sign signifies if it is collidable.
	 * 		- is collidable
	 * 		+ is not collidable
	 * 
	 * 
	 * 
	 */
	
	
	
	public final static short VOID_ID	 = -10000;
	public final static short GRASS_ID	 =  10001;
	public final static short DIRT_ID	 =  10002;
	public final static short BRICK_ID 	 = -10003;
	public final static short WOOD_ID 	 =  10004;
	public final static short SAND_ID	 =  10005;
	public final static short WATER_ID	 = -10006;
	public final static short GRAVEL_ID	 =  10007;
	
	
	public Tile(float x, float y, int w, int h, short ID) {
		super(x, y, w, h, true, ID);
	}

}
