package snake.tests;

import snake.engine.creators.ScreenCreator;
import snake.engine.creators.WorldSettings;
import snake.engine.dataManagment.Loader;
import snake.visuals.Lights;
import snake.visuals.enhanced.VisualGameWorld;
import box2dLight.Light;
import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;	
import com.badlogic.gdx.graphics.g2d.Sprite;


/**                               Developed By:
 *                                   NoDark
 *                                sessaGlasses
 *                                
 * <br> Map for testing purposes only. (BAD DESIGN) </br>
 * @author Mr.Strings
 */

public class ForestMap_test extends VisualGameWorld {
	
	// The code below is simply a prototype for testing purposes 
	private Sprite sprite;
	private Sprite entity; //must be changed to Map Entities
	Light light;
	private float velocity = .2f;
	private int x = 1;
	private boolean y = false, triggered = false;
	private String texName = "demos/foggy_forest_by_BrokenLens.jpeg";
	private String entityName = "demos/Scary_Silhouette.png";
	
	public ForestMap_test (String LevelData/* Add other parameters of choice*/) {
		
		//Procedimento padrao para se carregar um arquivo (FORMA EFICIENTE!!)
		Loader.load(texName, Texture.class);
		Loader.getManager().finishLoadingAsset(texName);
				
		//Cria a imagem
		Texture texture = Loader.get(texName);
		sprite = new Sprite(texture);
		sprite.setSize(WorldSettings.getWorldWidth(), WorldSettings.getWorldHeight());
		
		//Procedimento padrao para se carregar um arquivo (FORMA EFICIENTE!!)
		Loader.load(entityName, Texture.class);
		Loader.getManager().finishLoadingAsset(entityName);
						
		//Cria a imagem
		Texture texture2 = Loader.get(entityName);
		entity = new Sprite(texture2);
		
		entity.setSize(4, 12);
		entity.setAlpha(1f);
		entity.setPosition(60, 15);
	}
	
	
	public void show () {
		WorldSettings.setAmbientColor(Color.BLACK);
		WorldSettings.setWorld2ScreenRatio(1);
		
	}
	
	
	
	@Override
	public void act(float delta) {
		
		
		//Adds new screen on top of this one
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			try {
				ScreenCreator.addAndGo("SnakeScreen", "TempleMap", "");
			}  catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Back to previous screen
		if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
			try {
				ScreenCreator.backToPrevious();
			} catch (Exception e) { //OR... Creates new SnakeHub Screen
				try {
					ScreenCreator.switchAndGo("SnakeLevel", "MainMenu", "");
				} catch (Exception excp) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		//Do level stuff
		if (light.getX() >= 100) {
			velocity *=-1;
			y = false;
		} else if (light.getX() <= 0) {
			x++;
			if (x % 3 == 0) {
				y = true;
				entity.setPosition(entity.getX() - entity.getWidth()*1/2, entity.getY() - entity.getHeight()*3 /4);
				entity.setSize(entity.getWidth()* 2f, entity.getHeight() * 2f);
			}
			velocity *= -1;
		}
		light.setPosition(light.getX() + velocity, light.getY());
		
		
		if (x == 9 && light.getX() >= 90) {
			triggered = true;
			WorldSettings.setAmbientColor(new Color(1f, 0, 0, 1f));
		}
		
		if (triggered && light.getX() > 91) {
			Gdx.app.exit();
		}
		
		
		//Camera Movement
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			getStage().getCameraMan().moveCamera(-20f * delta, 0);
			
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			getStage().getCameraMan().moveCamera(20f * delta, 0);
			
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			getStage().getCameraMan().moveCamera( 0, -20f * delta);
			
		if (Gdx.input.isKeyPressed(Input.Keys.UP))
			getStage().getCameraMan().moveCamera(0, 20f * delta);
			
		//Camera Zoom
		if (Gdx.input.isKeyPressed(Input.Keys.O))
			getStage().getCameraMan().zoomCamera(-.5f * delta);
			
		if (Gdx.input.isKeyPressed(Input.Keys.P))
			getStage().getCameraMan().zoomCamera(.5f * delta);
		
		
		//Virtual Camera Movement
		if (Gdx.input.isKeyPressed(Input.Keys.L))
			getStage().getCameraMan().moveVCamera(.01f, 0);
		
		if (Gdx.input.isKeyPressed(Input.Keys.J))
			getStage().getCameraMan().moveVCamera(-.01f, 0);
			
		if (Gdx.input.isKeyPressed(Input.Keys.I))
			getStage().getCameraMan().moveVCamera(0, .01f);
		
		if (Gdx.input.isKeyPressed(Input.Keys.K))
			getStage().getCameraMan().moveVCamera(0, -.01f);
			
		//Virtual Camera Zoom
		if (Gdx.input.isKeyPressed(Input.Keys.U))
			getStage().getCameraMan().zoomVCamera(.01f);
			
		if (Gdx.input.isKeyPressed(Input.Keys.Y))
			getStage().getCameraMan().zoomVCamera(-.01f);
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		sprite.draw(batch);
		if (y)
			entity.draw(batch);
		super.drawChildren(batch, parentAlpha);
	}


	public void createLights() {
		light = new PointLight (Lights.getRayhandler(), 5000, new Color(1f, 0f, .5f, 1f), 40, 50, WorldSettings.heightFix(50));
		light.setSoft(false);
	}
	
	@Override
	public void dispose() {
		Loader.unload(texName);
		Loader.unload(entityName);
		super.dispose();
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
