package snake.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import snake.engine.creators.HUDSettings;
import snake.engine.dataManagment.Loader;
import snake.engine.models.HUD;
import snake.player.Player;

/**                               Developed By:
 *                                   NoDark
 *                                sessaGlasses
 * 
 * Game HUD, to display status and general information
 * 
 * @author Mr.Strings &Agustina & Guilherme
 */


public class SnakeHUD extends HUD {

	private BitmapFont font, empqt, flashqt, bulletqt;
	private String fontName = "fonts/ak_sc_o.fnt", hudName = "hud/HUDFinal.png", lanterna = "equipments/flashlights.png";
	private String emp ="equipments/EMPVecto.png", bullet = "equipments/Bullet.png";
	private Texture tex, tex1, tex2, tex3;
	private Sprite sprite, sprite1, sprite2, sprite3;
	
	public SnakeHUD (String levelData) {
		super();
		
		this.setBounds(0, 0, HUDSettings.getHudWidth(), HUDSettings.getHudHeight());
		
		Loader.load(fontName, BitmapFont.class);
		Loader.finishLoadingAsset(fontName);
		font = Loader.get(fontName);
		
		Loader.load(fontName, BitmapFont.class);
		Loader.finishLoadingAsset(fontName);
		empqt = Loader.get(fontName);
		
		Loader.load(fontName, BitmapFont.class);
		Loader.finishLoadingAsset(fontName);
		flashqt = Loader.get(fontName);
		
		Loader.load(fontName, BitmapFont.class);
		Loader.finishLoadingAsset(fontName);
		bulletqt = Loader.get(fontName);
		
		
		Loader.load(hudName, Texture.class);
		Loader.finishLoadingAsset(hudName);
		tex = Loader.get(hudName);

		Loader.load(lanterna, Texture.class);
		Loader.finishLoadingAsset(lanterna);
		tex1 = Loader.get(lanterna);

		Loader.load(emp, Texture.class);
		Loader.finishLoadingAsset(emp);
		tex2 = Loader.get(emp);

		Loader.load(bullet, Texture.class);
		Loader.finishLoadingAsset(bullet);
		tex3 = Loader.get(bullet);
		
		sprite = new Sprite(tex);
		sprite1 = new Sprite(tex1);
		sprite2 = new Sprite(tex2);		
		sprite3 = new Sprite(tex3);
		
		sprite1.setSize(100f, 85f);
		sprite1.setPosition(30, 530);
		sprite2.setSize(94f,100f);
		sprite2.setPosition(30, 400);
		sprite3.setSize(54, 100);
		sprite3.setPosition(30, 270);
		Player player = Player.getCurrentInstance();
	}
	
	
	public void show() {
		//TODO: Auto-generated method snub
	}
	
	
	@Override
	public void act (float delta) {
		super.act(delta);
		/*if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			System.out.println("Position: "+ WorldSettings.getVScreenX_Porc() + ":" + WorldSettings.getVScreenY_Porc());
			System.out.println("Size: "+ WorldSettings.getVScreenWidth_Porc() + ":" + WorldSettings.getVScreenHeight_Porc());
		}*/
	}
	
	
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		// Draw fps
		font.setColor(Color.GREEN);
		if (Gdx.input.isKeyPressed(Input.Keys.H) || Gdx.input.isTouched())
			font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 0, this.getHeight());
		
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			font.setColor(Color.MAGENTA);
			font.draw(batch, "Wow. Just... Wow.", 250, 100);
		}
		
		batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		sprite1.draw(batch);
		sprite2.draw(batch);
		sprite3.draw(batch);
	
		flashqt.setColor(Color.WHITE);
		flashqt.draw(batch, "x1", 140, 590);
		
		empqt.setColor(Color.WHITE);
		empqt.draw(batch, "x1", 140, 460);
		
		bulletqt.setColor(Color.WHITE);
		bulletqt.draw(batch, "x2", 140, 330);
	}


	@Override
	public void dispose() {
		font.dispose();
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
}
