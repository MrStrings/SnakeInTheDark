package snake.map;

import snake.engine.GameWorld;
import com.badlogic.gdx.scenes.scene2d.Group;

/**                              Developed By:
 *                                  NoDark
 *                               sessaGlasses
 *                               
 * Entity that belongs to a GameWorld             
 * 
 * @author Mr.Strings
 * 
 */


public abstract class MapEntity extends Group {
	
	protected int xInMap, yInMap;
	
	public MapEntity (GameWorld world) {
		world.addActor(this);
	}
}
