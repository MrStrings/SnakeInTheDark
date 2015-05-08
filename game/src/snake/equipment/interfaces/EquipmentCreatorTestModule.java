package snake.equipment.interfaces;



/**                              Developed By:
 *                                  NoDarkGlasses
 *                        
 * @author bszazulla
 */

public class EquipmentCreatorTestModule 
{
	// Representa��o de como usar este componente para cria��o de equipamentos
	public static void main(String[] args) 
	{
		// instancia do objeto
		IEquipment equip = EquipmentCreator.createFactory("flashlight").create();
		
		System.out.println("Equipment name: " + equip.getName());
		System.out.println("Equipment description: " + equip.getDescription());
	}
}
