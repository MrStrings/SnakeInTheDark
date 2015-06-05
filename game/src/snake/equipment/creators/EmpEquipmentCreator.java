package snake.equipment.creators;

import snake.equipment.IEquipment;
import snake.equipment.IEquipmentCreator;
import snake.equipment.implementations.EmpEquipment;

/**                              Developed By:
 *                                  NoDarkGlasses
 *                        
 * @author bszazulla
 */

public class EmpEquipmentCreator implements IEquipmentCreator
{
	public IEquipment create(float x, float y, boolean onMap)
	{
		return new EmpEquipment();
	}
}
