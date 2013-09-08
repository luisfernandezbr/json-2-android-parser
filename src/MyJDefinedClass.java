import com.sun.codemodel.JDefinedClass;


public class MyJDefinedClass {

	public String key;
	public JDefinedClass jDefinedClass;
	
	public MyJDefinedClass(String key, JDefinedClass jDefinedClass) {
		super();
		this.key = key;
		this.jDefinedClass = jDefinedClass;
	}
	
	@Override
	public boolean equals(Object object) {
	
		if (!(object instanceof MyJDefinedClass)) {
			return false;
		}
		
		MyJDefinedClass obj = (MyJDefinedClass) object;
		
		if (obj.key.equals(this.key)) {
			return true;
		}
		
		return false;
	}
	
}
