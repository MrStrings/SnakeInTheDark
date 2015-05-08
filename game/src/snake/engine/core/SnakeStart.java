package snake.engine.core;

import snake.engine.InterfaceAbstract.GameStart;
import snake.engine.gameScreens.SnakeHub;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**                               Developed By:
 *                                   NoDark
 *                                sessaGlasses
 * @Author Mr.Strings
 */

public class SnakeStart extends Game implements GameStart {
	public SpriteBatch batch;
	
	
	public SpriteBatch getBatch () {return batch;}

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new SnakeHub(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}