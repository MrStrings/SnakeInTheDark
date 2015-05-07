package snake.equipmentComponent;

import snake.interfacesAndAbstract.IEquipment;


/**                              Developed By:
 *                                  NoDarkGlasses
 *                        
 * @author bszazulla
 */

public class EquipmentCreatorTestModule 
{
	// Representa��o do que a engine ter� que fazer para criar equipamentos
	public static void main(String[] args) 
	{
		// instancia do objeto
		IEquipment equip = EquipmentCreator.createFactory("flashlight").create();
		
		System.out.println("Equipment name: " + equip.getName());
		System.out.println("Equipment description: " + equip.getDescription());
	}
}
