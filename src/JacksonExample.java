import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class JacksonExample {
	static String basePackage = "br.com.mobiplus.json2parser.model";
	//static Map<String, JDefinedClass> classesMap = new HashMap<String, JDefinedClass>();
	static Stack<MyJDefinedClass> stack = new Stack<MyJDefinedClass>();
	
	
	public static void main(String[] args) throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		// read JSON from a file
		Map<String, Object> mappedJson = mapper.readValue(new File("test2.json"), new TypeReference<Map<String, Object>>() {
		});

		
		
		JCodeModel codeModel = new JCodeModel();
//		
//		JDefinedClass _definedClassMenuItem = codeModel._class("br.com.mobiplus.json2parser.MenuItem");
//		_definedClassMenuItem.field(JMod.PUBLIC, int.class, "id");
//		_definedClassMenuItem.field(JMod.PUBLIC, String.class, "name");
//		
//		JClass _classMenuItem = codeModel.directClass("br.com.mobiplus.json2parser.MenuItem");
//		for (String key : _definedClassMenuItem.fields().keySet()) {
//			JFieldVar field = _definedClassMenuItem.fields().get(key);
//			_classMenuItem.staticRef(field);
//		}
//		
//		JDefinedClass _definedClassMenu = codeModel._class("br.com.mobiplus.json2parser.Menu");
//		_definedClassMenu.field(JMod.PUBLIC, _definedClassMenuItem, "menuItem");
//		
//		File file = new File("target/classes");
//		file.mkdirs();
//		codeModel.build(file);
		
		
		
		readJson(codeModel, mappedJson, null);

		for (int i = 0; i < stack.size(); i++) {
			stack.pop();
		}
		
		generateClass(codeModel);
	}

	static JDefinedClass lastDefinedClass;
	static boolean isFirst = true;

	private static void readJson(JCodeModel codeModel, Object item, String lastKey) throws JClassAlreadyExistsException {
		//int size = -1;

//		if (lastDefinedClass != null) {
//			size = lastDefinedClass.fields().size();
//		}

		if (item instanceof Map) {
			if (lastKey != null) {
				// TODO Tratando duplicacao das classes dentro da lista
				if (!stack.contains(new MyJDefinedClass(lastKey, null))) {
					JDefinedClass definedClass = generateClass(codeModel, basePackage, lastKey); 
					
//					if (lastDefinedClass != null) {
//						lastDefinedClass.field(JMod.PUBLIC, definedClass, definedClass.name());	
//					}
					
					
					lastDefinedClass = definedClass;
					stack.add(new MyJDefinedClass(lastKey, lastDefinedClass));	
				}
				
				//classesMap.put(lastKey, lastDefinedClass);
			}

			@SuppressWarnings("unchecked")
			Map<String, ?> objectMap = (HashMap<String, ?>) item;

			for (String key : objectMap.keySet()) {
				System.out.println(key);
				readJson(codeModel, objectMap.get(key), key);
			}

		} else if (item instanceof List) {

			@SuppressWarnings("unchecked")
			ArrayList<String> list = (ArrayList<String>) item;
			
			for (int i = 0; i < list.size(); i++) {
				try {
					if (!lastDefinedClass.name().equals(lastKey)) {
						addField(lastDefinedClass, lastKey, List.class);
					}

					readJson(codeModel, list.get(i), lastKey);	
				} catch (IllegalArgumentException e) {
					
				}
			}

		} else if (item instanceof String || item == null) {
			addField(lastDefinedClass, lastKey, String.class);	

		} else if (item instanceof Integer) {
			addField(lastDefinedClass, lastKey, int.class);

		} else if (item instanceof Long) {
			addField(lastDefinedClass, lastKey, long.class);

		} else if (item instanceof Double) {
			addField(lastDefinedClass, lastKey, double.class);

		} else if (item instanceof Boolean) {
			addField(lastDefinedClass, lastKey, boolean.class);

		} else {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> NOT MAPPED: " + item);
		}
	}

	private static void addField(JDefinedClass definedClass, String lastKey, Class<?> clazz) {
		try {
			definedClass.field(JMod.PUBLIC, clazz, lastKey);
			System.out.println("Atributo adicionado: " + lastKey );
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	private static boolean isKey(Object object) {
		if (object instanceof Map) {
			return true;
		}
		return false;
	}

	private static JDefinedClass generateClass(JCodeModel codeModel, String basePackage, String className) throws JClassAlreadyExistsException {
		return codeModel._class(basePackage + "." + className);
	}

	private static void generateClass(JCodeModel codeModel) throws JClassAlreadyExistsException, IOException {
		File file = new File("target/classes");
		file.mkdirs();
		codeModel.build(file);
	}
	
	/**
	 * http://stackoverflow.com/questions/121324/a-java-api-to-generate-java-source-files
	 */
	private static void generateClass() throws JClassAlreadyExistsException, IOException {
		JCodeModel codeModel = new JCodeModel();
		JDefinedClass cmClass = codeModel._class("foo.Bar");

		cmClass.field(0, long.class, "ids");
		cmClass.field(1, boolean.class, "enable");
		cmClass.field(2, String.class, "name");
		cmClass.field(3, JacksonExample.class, "jacksonExample");
		cmClass.field(4, JacksonExample.class, "jacksonsExample");
		cmClass.field(2, List.class, "list");

		JMethod cmMethod = cmClass.method(0, int.class, "foo");
		cmMethod.body()._return(JExpr.lit(5));

		File file = new File("target/classes");
		file.mkdirs();
		codeModel.build(file);
	}

	private static void generateClass(int i) throws JClassAlreadyExistsException, IOException {
		JCodeModel codeModel = new JCodeModel();
		JDefinedClass cmClass = codeModel._class("foo.Bar");

		cmClass.field(0, long.class, "ids");
		cmClass.field(1, boolean.class, "enable");
		cmClass.field(2, String.class, "name");
		cmClass.field(3, JacksonExample.class, "jacksonExample");
		cmClass.field(4, JacksonExample.class, "jacksonsExample");
		cmClass.field(2, List.class, "list");

		JDefinedClass cmClass2 = codeModel._class("foo.Test");

		cmClass.field(0, long.class, "ids");
		

		File file = new File("target/classes");
		file.mkdirs();
		codeModel.build(file);
	}
	
}