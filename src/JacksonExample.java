import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonExample {
	public static void main(String[] args) {

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
	
	
	
}