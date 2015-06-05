package snake.equipment.implementations;


import com.badlogic.gdx.graphics.g2d.Batch;

import snake.equipment.IEquipment;
import snake.map.IMapAccess;
import snake.visuals.enhanced.LightMapEntity;

/**                              Developed By:
 *                                  NoDarkGlasses
 *                                  
 * Classe abstrata diretora de todos os equipamentos concretos
 *                        
 * @author bszazulla
 */

public abstract class AbstractEquipment extends LightMapEntity implements IEquipment
{
	protected String name;
	protected String description;
	
	// Captar o nome do equipamento
	public String getName()
	{
		return name;
	}
	
	// para pegar uma pequena descri��o do equipamento
	public String getDescription()
	{
		return description;
	}
	
	// Para se ativar no mapa o efeito do equipamento
	public abstract void activateOnMap(IMapAccess map);

	// Para se desenhar na tela (ele se chama sozinho)
	public abstract void draw (Batch batch, float parentAlpha);
	
	// Para se atualizar na l�gica do jogo
	public abstract void act (float delta);
	
	// M�todos relacionados ao foco de luz (alguns equipments ter�o eles sobrescritos, outros n�o - usam esses da classe m�e mesmo -)
	// Se h� ou n�o luz funcionando (just a test)
	@Override
	public boolean hasLights()
	{
		return false;
	}
	// Cria��o da luz (nungu�m chama de fora, ele � um dos m�todos que caracterizam o objeto)
	@Override
	public void createLights()
	{
	}
	// Esp�cie de free na luz criada
	@Override
	public void disposeLights()
	{
	}
}
