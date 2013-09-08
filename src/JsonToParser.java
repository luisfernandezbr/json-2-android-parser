import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

public class JsonToParser {

	String basePackage = "br.com.mobiplus.json2parser.model";
	Stack<MyJDefinedClass> stack = new Stack<MyJDefinedClass>();
	JCodeModel codeModel;
	
	

	public JsonToParser (JCodeModel codeModel) {
		super();
		this.codeModel = codeModel;
	}

	public void readObject(Object item, String lastKey, JDefinedClass lastDefinedClass) throws JClassAlreadyExistsException {
		if (isKey(item)) {
			System.out.println("### Nova chave encontrada: " + item);
			this.handleObjectKeys(item, lastKey, lastDefinedClass);

		} else {

			System.out.println(">>> Novo valor encontrado: " + item);
			this.handleObjectValues(item, lastKey, lastDefinedClass);
		}
	}

	public void handleObjectKeys(Object item, String lastKey, JDefinedClass lastDefinedClass) throws JClassAlreadyExistsException {
		JDefinedClass newDefinedClass = null;

		if (lastKey != null) {
			// TODO Tratando duplicacao das classes dentro da lista

			try {
				newDefinedClass = this.generateClass(codeModel, basePackage, lastKey);

				if (lastDefinedClass != null && !lastDefinedClass.fields().containsKey(lastKey)) {
					lastDefinedClass.field(JMod.PUBLIC, newDefinedClass, newDefinedClass.name().toLowerCase());
				}
			} catch (JClassAlreadyExistsException e) {
				System.out.println(e.getMessage());
			}
			

		}

		@SuppressWarnings("unchecked")
		Map<String, ?> objectMap = (HashMap<String, ?>) item;

		for (String key : objectMap.keySet()) {
			if (newDefinedClass != null) {
				this.readObject(objectMap.get(key), key, newDefinedClass);
			} else {
				this.readObject(objectMap.get(key), key, lastDefinedClass);
			}
		}
	}

	public void handleObjectValues(Object item, String lastKey, JDefinedClass lastDefinedClass) throws JClassAlreadyExistsException {
		if (item instanceof List) {

			@SuppressWarnings("unchecked")
			ArrayList<String> list = (ArrayList<String>) item;

			for (int i = 0; i < list.size(); i++) {
				try {
					// if (!lastDefinedClass.name().equals(lastKey)) {
					addField(lastDefinedClass, lastKey, List.class);
					// }

					this.readObject(list.get(i), lastKey, lastDefinedClass);
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage() + " > cause " + e.getCause());
				}
			}

		} else if (item instanceof String || item == null) {
			this.addField(lastDefinedClass, lastKey, String.class);

		} else if (item instanceof Integer) {
			this.addField(lastDefinedClass, lastKey, int.class);

		} else if (item instanceof Long) {
			this.addField(lastDefinedClass, lastKey, long.class);

		} else if (item instanceof Double) {
			this.addField(lastDefinedClass, lastKey, double.class);

		} else if (item instanceof Boolean) {
			this.addField(lastDefinedClass, lastKey, boolean.class);

		} else {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> NOT MAPPED: " + item);
		}
	}

	public void addField(JDefinedClass definedClass, String lastKey, Class<?> clazz) {
		try {
			
			if (clazz.getName().contains("List")) {
				JType jtype = codeModel.ref("ArrayList").narrow(codeModel.ref(getClassName(lastKey)));
				definedClass.field(JMod.PUBLIC, jtype, lastKey);
			} else {
				definedClass.field(JMod.PUBLIC, clazz, lastKey);	
			}
			
			
			System.out.println("    Atributo adicionado: " + lastKey);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean isKey(Object object) {
		if (object instanceof Map) {
			return true;
		}
		return false;
	}

	public JDefinedClass generateClass(JCodeModel codeModel, String basePackage, String className) throws JClassAlreadyExistsException {
		return codeModel._class(basePackage + "." + getClassName(className));
	}

	
	
	public void generateClasses(JCodeModel codeModel) throws JClassAlreadyExistsException, IOException {
		File file = new File("target/classes");
		file.mkdirs();
		codeModel.build(file);
	}
	
	public String getClassName(String name) {
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
}
