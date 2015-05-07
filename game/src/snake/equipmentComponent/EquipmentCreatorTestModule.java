package snake.equipmentComponent;

import snake.interfacesAndAbstract.EquipmentCreator;
import snake.interfacesAndAbstract.IEquipment;


/*                               Developed By:
 *                                  NoDarkGlasses
 *                        
 * Module: bszazulla && --------------- (modifiable according to need)
 */


public class EquipmentCreatorTestModule 
{
	// Representa��o do que a engine ter� que fazer para criar equipamentos
	public static void main(String[] args) 
	{
		// inst�ncia da f�brica do objeto 
		EquipmentCreator equipFactory = new EquipmentCreator();
		
		// instancia do objeto
		IEquipment equip = equipFactory.createFactory("flashlight").create();
		
		System.out.println("Equipment name: " + equip.getName());
		System.out.println("Equipment description: " + equip.getDescription());
	}

}
