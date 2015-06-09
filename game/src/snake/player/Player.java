package snake.player;

import java.util.ArrayList;
import java.util.List;
import snake.drone.IObserver;
import snake.engine.dataManagment.Loader;
import snake.engine.models.GameWorld;
import snake.equipment.EquipmentCreator;
import snake.equipment.IEquipment;
import snake.map.CellType;
import snake.map.IMapAccess;
import snake.map.IMapEntity;
import snake.map.TiledMapWorld;
import snake.tests.FlashLight_test;
import snake.visuals.enhanced.LightMapEntity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**                              Developed By:
 *                                  NoDark
 *                               sessaGlasses
 *                               
 * <br> Player for testing purposes at TempleMap </br>
 * 
 * @author Mr.Strings & Guilherme Higa
 */

public class Player extends LightMapEntity {
	
	//Singleton area
	private static Player player;
	
	private IMapAccess world;
	
	private static final int DOWN = 0, LEFT = 1, RIGHT = 2, UP = 3;
	private static final int ANIMATION_WALK_STATES_NUM = 4, ANIMATION_STILL_STATES_NUM = 4;
	
	private enum State {STANDING, MOVING};
	
	//Animation
	private Texture walkSheet, standingSheet; //debugar o esquema
	private Animation[] animatedWalk, animatedStanding;
	private Animation currentAnimation;
	private TextureRegion currentFrame, region;
	private String walkTexName = "character/CharacterSprite.png", standingTexName = "character/BlinkingCharacterSprite.png";
	
	private static final int FRAME_ROWS_WALK = 4, FRAME_COLS_WALK = 3;
	private static final int FRAME_ROWS_STANDING = 4, FRAME_COLS_STANDING = 3;
	
	/* Movement */
	private float speed = 3f;
	private float walkAnimationSpeed = .1f, standingAnimationSpeed = .5f;
	private Vector2 direction;
	private State state = State.STANDING;
	private float distanceMoved;
	
	//Equipments
	private IEquipment sensor;
	private FlashLight_test flashlight;
	
	//Stuff
	private float stateTime = 0;
	private float lastPosX, lastPosY;

	private List<IObserver> observers = new ArrayList<IObserver>();
	
	private Player (GameWorld world) {
		super(world);
		
		this.world = ((TiledMapWorld) world).getMapAccess();

		this.setSize(1f, 1f);
		this.setOrigin(0,0); // A origem ficou zoada pois o PNG nao ficou bom -- arrumar isso
		
		this.setPosition(1, 1); // TODO: definir pelo mapa
		
		direction = new Vector2();
		
		//Procedimento padrao para se carregar um arquivo (FORMA EFICIENTE!!)
		Loader.load(walkTexName, Texture.class);
		Loader.finishLoadingAsset(walkTexName);
		walkSheet = Loader.get(walkTexName);
		
		Loader.load(standingTexName, Texture.class);
		Loader.finishLoadingAsset(standingTexName);
		standingSheet = Loader.get(standingTexName);
		
		
		
		//Cria as animacoes
		animatedWalk = new Animation[4];
	    for (int i = 0; i < ANIMATION_WALK_STATES_NUM; i++) {

	    	region = new TextureRegion(walkSheet, 0, (float) i/FRAME_ROWS_WALK, 1,(float) (i+1) /FRAME_ROWS_WALK);
	    	
			TextureRegion[][] tmp2 = region.split(region.getRegionWidth()/FRAME_COLS_WALK,region.getRegionHeight());
			
			animatedWalk[i] = new Animation(walkAnimationSpeed, tmp2[0]);
	    }
	    
	    animatedStanding = new Animation[4];
	    for (int i = 0; i < ANIMATION_STILL_STATES_NUM; i++) {

	    	region = new TextureRegion(standingSheet, 0, (float) i/FRAME_ROWS_STANDING, 1,(float) (i+1) /FRAME_ROWS_STANDING);
	    	
			TextureRegion[][] tmp2 = region.split(region.getRegionWidth()/FRAME_COLS_STANDING,region.getRegionHeight());
			
			animatedStanding[i] = new Animation(standingAnimationSpeed, tmp2[0]);
	    }
	    
	    currentAnimation = animatedStanding[DOWN];
	    

		//adiciona equipamento lanterna_teste
		flashlight = new FlashLight_test (world);
		this.addActor(flashlight);
		flashlight.setPosition(1f,.5f);
		flashlight.setOrigin(-.5f, 0f);

		//adiciona sensor
		sensor = EquipmentCreator.createFactory("sensor").create(.5f, .5f, false);
		addActor((Actor) sensor);
	}
	
	static public Player getInstance(GameWorld world){
		if(player == null || player.getWorld() != world) {
			player = new Player(world);
		}
		return player;
	}
	
	@Override
	public void act (float delta) { // Aqui se realizam as atualizacoes
		super.act(delta);
		
		stateTime += delta;
		currentFrame = currentAnimation.getKeyFrame(stateTime, true);
		
		
		if  (CellType.EXIT.equals(world.getCellType((int)getX(), (int)getY()))) {
			// TODO: CARREGAR PROXIMO MAPA
		}
		
		IMapEntity entity = world.getEntity((int)getX(), (int)getY());
		if (entity != null && "equipment".equals(entity.getType())) {
			world.removeEntity(entity); //Retira do mundo
			addActor((Actor) entity); //Adiciona ao player
			//TODO: onMap setter
		}
		
		if (state  == State.STANDING) {

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !CellType.WALL.equals(world.getCellType((int)getX() - 1, (int)getY()))){
				distanceMoved = 0;
				direction.set(-speed, 0);
				currentAnimation = animatedWalk[LEFT];
				lastPosX = getX(); lastPosY = getY();
				state = State.MOVING;	
				stateTime = 0;
				update(delta);
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !CellType.WALL.equals(world.getCellType((int)getX() + 1, (int)getY()))) {
				distanceMoved = 0;
				direction.set(speed, 0);
				currentAnimation = animatedWalk[RIGHT];
				lastPosX = getX(); lastPosY = getY();
				state = State.MOVING;
				stateTime = 0;
				update(delta);
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.UP) && !CellType.WALL.equals(world.getCellType((int)getX(), (int)getY() + 1))) {
				distanceMoved = 0;
				direction.set(0, speed);
				currentAnimation = animatedWalk[UP];
				lastPosX = getX(); lastPosY = getY();
				state = State.MOVING;
				stateTime = 0;
				update(delta);
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !CellType.WALL.equals(world.getCellType((int)getX(), (int)getY() - 1))) {
				distanceMoved = 0;
				direction.set(0, -speed);
				currentAnimation = animatedWalk[DOWN];
				lastPosX = getX(); lastPosY = getY();
				state = State.MOVING;
				stateTime = 0;
				update(delta);
			} else {
				if (direction.x > 0) {
					currentAnimation = animatedStanding[RIGHT];
				} 
				else if (direction.x < 0) {
					currentAnimation = animatedStanding[LEFT];
				} 
				else if (direction.y > 0) {
					currentAnimation = animatedStanding[UP];
				} 
				else {
					currentAnimation = animatedStanding[DOWN];
				} 
			}
		}
		
		else if (state == State.MOVING) {

			distanceMoved += (speed * delta);
			if (distanceMoved == 1) {

				state = State.STANDING;
				stateTime = 0;
			}
			else if (distanceMoved > 1) { 
				lastPosX += (direction.x != 0 ? direction.x/Math.abs(direction.x) : 0);
				lastPosY += (direction.y != 0 ? direction.y/Math.abs(direction.y) : 0);
				this.setPosition(lastPosX, lastPosY);
				state = State.STANDING;
				stateTime = 0;
			}
			else {
				this.moveBy(direction.x * delta, direction.y * delta);
			}
			
		}
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) { //Aqui se desenha
		batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), //Esse tanto de parametro e necessario para movimento automatico
				getWidth(), getHeight(), getScaleX(), getScaleY(), super.getRotation());
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
	public void disposeLights() {
		super.disposeLights();
	}

	@Override
	public void dispose() {
		super.dispose();
		if (this.getParent() != null || this.getStage() != null) {
			this.remove();
		}
		Loader.unload(walkTexName);
		Loader.unload(standingTexName);
		player = null;
	}
	
	public void attach(IObserver observer){
	      observers.add(observer);		
	   }
	
	private void update(float delta){
		for (IObserver observer : observers) {
			observer.update(delta);
		}
	}

	@Override
	public String getType() {
		return "player";
	}
	
	@Override
	public float getRotation() {
		if (direction.x > 0) {
			return 0;
		} 
		else if (direction.x < 0) {
			return 180;
		} 
		else if (direction.y > 0) {
			return 90;
		} 
		else {
			return 270;
		} 
	}
}