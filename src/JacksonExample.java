import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;

public class JacksonExample {
	public static void main(String[] args){


		
		
		ObjectMapper mapper = new ObjectMapper();

		try {

			// read JSON from a file
			Map<String, Object> userInMap = mapper.readValue(new File("test2.json"), new TypeReference<Map<String, Object>>() {
			});

			readJson(userInMap);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void readJson(Object object) {
		if (object instanceof Map) {
			Map<String, ?> objectMap = (HashMap) object;  
			
			for (String key : objectMap.keySet()) {
				System.out.println(key);
				readJson(objectMap.get(key));
			}
			
		} else if (object instanceof List) {
			
			ArrayList<String> list = (ArrayList<String>) object;
			
			for (int i = 0; i < list.size(); i++) {
			//	System.out.println(list.get(i));
				readJson(list.get(i));
			}
			
		} else if (object instanceof String || object == null) {
			System.out.println(object);
		} else if (object instanceof Integer) {
			System.out.println(object);
		} else if (object instanceof Long) {
			System.out.println(object);
		} else if (object instanceof Boolean) {
			System.out.println(object);
		} else {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> NOT MAPPED: " + object);
		}
	}
	
	/**
	 * http://stackoverflow.com/questions/121324/a-java-api-to-generate-java-source-files
	 */
	private void generateClass()  throws JClassAlreadyExistsException, IOException {
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
	
	
	
}