package osotnikov.demo.instantloan.constants;

public enum TypeOfInterest {
	
	COMPOUND("COMPOUND"),
	SIMPLE("SIMPLE");
	
	private TypeOfInterest(String type) {
		this.type = type;
	}

	private String type;

	public String getType() {
		return type;
	}
	
	
}
