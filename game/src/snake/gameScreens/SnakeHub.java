package snake.gameScreens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*                               Developed By:
 *                                  NoDark
 *                               sessaGlasses
 * Module: Mr.Strings
 */

public class SnakeHub implements Screen, InputProcessor {
	private Game game;
	private SpriteBatch batch;
	private BitmapFont font;
	private float w, h;
	String instructions[]; //will be changed to buttons

	public SnakeHub(Game game, SpriteBatch batch) {
		this.game = game;
		this.batch = batch;

		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		Gdx.input.setInputProcessor(this);
		
		instructions = new String[3];
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		update();
		draw();
	}

	private void draw() {
		font = new BitmapFont(Gdx.files.internal("ak_sc_o.fnt"), false);
		font.setColor(Color.GREEN);

		batch.begin();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched())
			font.draw(batch, "You're touching it! (maybe pressing space button).", 0, 80);

		font.draw(batch, instructions[0], w / 2 - font.getBounds(instructions[0]).width
				/ 2, h / 2 - font.getBounds(instructions[0]).height / 2 + 50);
		font.draw(batch, instructions[1], w / 2
				- font.getBounds(instructions[1]).width / 2,
				h / 2 - font.getBounds(instructions[1]).height / 2 - 20);

		font = new BitmapFont();
		font.setColor(Color.GREEN);
		font.setScale(3f);
		font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 0, h*99/100);
		batch.end();
	}

	private void update() {
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_1))
			game.setScreen(new SnakeLevel(
					game/* Will be added which level */, batch));

		instructions[0] = new String("Para comecar o jogo");
		instructions[1] = new String("tecle 1");
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.F2) {
			// set resolution to default and toogles full-screen
			Gdx.graphics.setDisplayMode(
					Gdx.graphics.getDesktopDisplayMode().width,
					Gdx.graphics.getDesktopDisplayMode().height,
					!Gdx.graphics.isFullscreen());

			System.out.println("Toogled fullscreen\n");
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
