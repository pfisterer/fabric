package fabric.module.typegen;

public class TypeGenFactory {
	private static TypeGenFactory instance;
	
	private TypeGen typeGen;
	
	private TypeGenFactory() {
		// Empty implementation
	}
	
	public synchronized TypeGenFactory getInstance() {
		if (null == instance)
		{
			instance = new TypeGenFactory();
		}
		
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	public TypeGen createTypeGen(String typeGenType) {
		if (null == typeGen) {
		
			try {
				Class clazz = Class.forName(typeGenType);
				typeGen = (TypeGen) clazz.newInstance();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return typeGen;
	}	
}
