package snake.tests;

import snake.engine.dataManagment.Loader;
import snake.engine.models.GameWorld;
import snake.visuals.enhanced.LightMapEntity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;



/**                              Developed By:
 *                                  NoDark
 *                               sessaGlasses
 *                               
 * <br> Player for testing purposes at TempleMap </br>
 * 
 * @author Mr.Strings
 */

public class Magician_Test extends LightMapEntity {
	
	private Sprite sprite;
	private Weapon weapon;
	private FlashLight_test flashlight;
	private String texName = "demos/magician.png";
	
	
	public Magician_Test (GameWorld world) {
		super(world);
		this.setSize(30, 30);
		this.setOrigin(13, 16); // A origem ficou zoada pois o PNG nao ficou bom -- arrumar isso
		
		//Procedimento padrao para se carregar um arquivo (FORMA EFICIENTE!!)
		Loader.load(texName, Texture.class);
		Loader.getManager().finishLoadingAsset(texName);
		
		//Cria a imagem
		Texture texture = Loader.get(texName);
		sprite = new Sprite(texture);
		
		//Adiciona equipamento arma
		weapon = new Weapon (world);
		this.addActor(weapon);
		weapon.setPosition(15.25f, 22.5f);
		
		//adiciona equipamento lanterna_teste
		flashlight = new FlashLight_test (world);
		this.addActor(flashlight);
		flashlight.setPosition(15f, 20);
		
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
		super.createLights(); //Importante para criar as luzes/sombra dos filhos
	}

	@Override
	public void dispose() {
		super.dispose();
		if (this.getParent() != null || this.getStage() != null) {
			this.remove();
		}
		Loader.unload(texName);
	}

	@Override
	public String getType() {
		return "player";
	}
}
