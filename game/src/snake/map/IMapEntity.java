package snake.map;

import snake.engine.models.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;

public interface IMapEntity {

    float getX();

    float getY();

    void dispose();

    void act(float delta);

    void draw(Batch batch, float parentAlpha);

	GameWorld getWorld();
	
	public String getType();
}
