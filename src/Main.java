import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws JClassAlreadyExistsException
	 */
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, JClassAlreadyExistsException {
		ObjectMapper mapper = new ObjectMapper();

		// read JSON from a file
		Map<String, Object> mappedJson = mapper.readValue(new File("new_test2.json"), new TypeReference<Map<String, Object>>() {
		});

		JCodeModel codeModel = new JCodeModel();

		JDefinedClass _definedClassMain = codeModel._class("br.com.mobiplus.json2parser.model.Main");

		JsonToParser json2parser = new JsonToParser(codeModel);
		json2parser.readObject(mappedJson, null, _definedClassMain);
		json2parser.generateClasses(codeModel);
	}

}
