package snake.tests;

import box2dLight.ConeLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import snake.engine.creators.WorldSettings;
import snake.engine.dataManagment.Loader;
import snake.engine.models.GameWorld;
import snake.visuals.Lights;
import snake.visuals.enhanced.LightMapEntity;



/**                              Developed By:
 *                                  NoDark
 *                               sessaGlasses
 *                               
 * <br> Player for testing purposes at TempleMap </br>
 * 
 * @author Mr.Strings
 */

public class Magician_Test extends LightMapEntity {
	
	private ConeLight light;
	private Sprite sprite;
	private Weapon weapon;
	private String texName = "magician.png";
	
	
	public Magician_Test (GameWorld world) {
		super(world);

		world.addActor(this);
		
		//Procedimento padrao para se carregar um arquivo (FORMA EFICIENTE!!)
		Loader.load(texName, Texture.class);
		while (!Loader.isLoaded(texName))
				Loader.update();
		
		//Cria a imagem
		Texture texture = Loader.get(texName);
		sprite = new Sprite(texture);
					
		sprite.setAlpha(1f); //Transparencia -- de 0 a 1 (0 eh invisivel)
		
		this.setBounds(0, WorldSettings.heightFix(0), 30, 30); // Perceba o heightFix -- otimo para trabalhar com porcentagem em relacao ao mundo
		//... Com o heightFix, o topo fica 100, o chao fica 0 (Highly recommended)
		this.setOrigin(13, 16); // A origem ficou zoada pois o PNG nao ficou bom -- arrumar isso
		
		
		weapon = new Weapon (world);
		this.addActor(weapon);
		weapon.setBounds(17.5f, 20, 5, 5);
		weapon.setRotation(90);
	}
	
	@Override
	public void act (float delta) { // Aqui se realizam as atualizacoes
		super.act(delta);
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			moveBy(-.3f, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			moveBy(+.3f, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			moveBy(0, +.3f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			moveBy(0, -.3f);
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.C)) {
			rotateBy(5);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.V)) {
			rotateBy(-5);
		}
		
		light.setPosition(getOriginX() + getX(), getOriginY() + getY());
		light.setDirection(getRotation() + 90);
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) { //Aqui se desenha
		
		batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(), //Esse tanto de parametro e necessario para movimento automatico
				getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		super.draw(batch, parentAlpha);
	}

	@Override
	public boolean hasLights() {
		return false;
	}

	@Override
	public void createLights() { //Criacao de luzes tem que ser algo separado (senao da pau) -- tudo aqui
		light = new ConeLight (Lights.getRayhandler(), 5000, new Color(1f, 1f, .5f, 1f),
				   			   100, 50, WorldSettings.heightFix(50), 90, 30);
		super.createLights(); //Importante para criar as luzes/sombra dos filhos
	} //Se quiser destruir a luz, pode ser em qualquer lugar

	@Override
	public void disposeLights() {
		light.remove(); // IF you don't remove stuff gets crazy
		light.dispose();
		super.disposeLights();
	}

	@Override
	public void dispose() {
		Loader.unload(texName);
		
	}
}
