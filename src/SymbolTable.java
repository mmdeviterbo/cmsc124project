
public class SymbolTable {

		 
		private String identifier;  //if na-detect sa classification ng lexeme sa 
		private String value; 		//Lexeme.java as "variable identifier", add to this class in identifier attribute
		public SymbolTable(String identifier, String value) {
			this.setIdentifier(identifier);
			this.setValue(value);
		}
		public String getIdentifier() {
			return identifier;
		}
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	
		
}
