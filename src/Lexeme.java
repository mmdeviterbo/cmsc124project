import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Lexeme{
	//hashmap for searching of LOLcode lexemes and its classification
	public static HashMap<String,ArrayList<String>> TOKENS = new HashMap<String, ArrayList<String>>();
	
	//hashmap for storing lexemes and its classification input by user
	public static HashMap<String,ArrayList<String>> userTokens = new HashMap<String, ArrayList<String>>();
		
	
	//variable
	public static final String VARIDENT = "[a-zA-Z][0-9a-zA-Z_]*";

	//literals
	public static final String NUMBR = "-?[0-9]+";
	public static final String NUMBAR = "-?[0-9]*\\.[0-9]+";
	public static final String YARN = "\"[^\"]+\""; 
	public static final String[] TROOF = {"WIN","FAIL"};
	public static final String[] TYPE = {"NUMBR","NUMBAR","YARN","TROOF"};
	public static final String[] LITERALS = {NUMBR,NUMBAR,YARN,TROOF[0],TROOF[1],TYPE[0],TYPE[1],TYPE[2],TYPE[3]};
	public static final String ALL_LITERALS = NUMBAR+"|"+NUMBR +"|"+YARN+"|"+TROOF[0]+"|"+TROOF[1]+"|"+TYPE[0]+"|"+TYPE[1]+"|"+TYPE[2]+"|"+TYPE[3] + "|AN";
	
	//file naming and formatting
	public static final String HAI =  "\\bHAI\\b";
	public static final String KTHXBYE =  "\\bKTHXBYE\\b";

	//comments	
	public static final String BTW = "\\bBTW\\b";
	public static final String OBTW = "\\bOBTW\\b";
	public static final String TLDR = "\\bTLDR\\b";

	//variable
	public static final String I_HAS_A =  "\\bI HAS A\\b";
	public static final String ITZ = "\\bITZ\\b";
	public static final String IT = "IT";
	public static final String I_HAS_CONSTRUCT =  I_HAS_A +"|";
	public static final String R = "\\bR\\b";
	
	

	//arithmetic/mathematical Operations
	public static final String SUM_OF = "\\bSUM OF\\b";
	public static final String DIFF_OF = "\\bDIFF OF\\b";
	public static final String PRODUKT_OF = "\\bPRODUKT OF\\b";
	public static final String QUOSHUNT_OF = "\\bQUOSHUNT OF\\b";
	public static final String MOD_OF = "\\bMOD OF\\b";
	public static final String BIGGR_OF = "\\bBIGGR OF\\b";
	public static final String SMALLR_OF = "\\bSMALLR OF\\b";
	
	//boolean operations
	public static final String BOTH_OF= "\\bBOTH OF\\b";
	public static final String EITHER_OF = "\\bEITHER OF\\b";
	public static final String WON_OF = "\\bWON OF\\b";
	public static final String NOT = "\\bNOT\\b";
	public static final String ANY_OF = "\\bANY OF\\b";
	public static final String ALL_OF = "\\bALL OF\\b";
	public static final String MKAY = "\\bMKAY\\b";
	
	//comparison operators
	public static final String BOTH_SAEM = "\\bBOTH SAEM\\b";
	public static final String DIFFRINT = "\\bDIFFRINT\\b";
	
	//string concatenation
	public static final String SMOOSH = "\\bSMOOSH\\b";
	
	//input-output
	public static final String VISIBLE = "\\bVISIBLE\\b";
	public static final String GIMMEH  = "\\bGIMMEH\\b";
	
	//condtional / if then statements
	public static final String O_RLY  = "O RLY\\?";
	public static final String YA_RLY  = "\\bYA RLY\\b";
	public static final String MEBBE  = "\\bMEBBE\\b";
	public static final String NO_WAI  = "\\bNO WAI\\b";

	//conditional / switch case
	public static final String WTF  = "WTF\\?";
	public static final String OMG  = "\\bOMG\\b";
	public static final String OMGWTF  =  "\\bOMGWTF\\b";
	public static final String OIC  = "\\bOIC\\b"; 
	public static final String GTFO = "\\bGTFO\\b";
	
	//single quote
	public static final String QUOTE = "\"";

	
	
	//not included in Project specification
	// public static final String MAEK = "MAEK";
	// public static final String IS_NOW_A = "IS NOW A";
	// public static final String A = "A";
	// public static final String IM_IN_YR  = "IM IN YR";
	// public static final String UPPIN  = "UPPIN";
	// public static final String NERFIN  = "NERFIN";
	// public static final String YR  = "YR";
	// public static final String TIL  = "TIL";
	// public static final String WILE  = "WILE";
	// public static final String IM_OUTTA_YR = "IM OUTTA YR";
	//========================================= END OF LEXEME ===========================================



	//identifier
	public static final String CODE_DELIMETER = "Code Delimeter";
	public static final String VARIABLE_DECLARATION = "Declaration Identifier";
	public static final String VARIABLE_ASSIGNMENT = "Assignment Identifier";
	public static final String VARIABLE_IDENTIFIER = "Variable Identifier";
	public static final String STRING_DELIMETER = "String Delimeter";
	public static final String OUTPUT_KEYWORD = "Output Keyword";
	public static final String INPUT_KEYWORD = "Input Keyword";
	public static final String COMMENT_KEYWORD  = "Comment Identifier";
	public static final String CONCATENATION_KEYWORD  = "Concatenation Identifier";
	public static final String CONDITIONAL_KEYWORD  = "Conditional Identifier";
	public static final String BREAK_IDENTIFIER = "Break Identifier";
	
	//literal
	public static final String LITERAL = "Literal";
	
	//operator
	public static final String ARITHMETIC_OPERATOR = "Arithmetic Operator";
	public static final String BOOLEAN_OPERATOR  = "Boolean Operator";
	public static final String COMPARISON_OPERATOR  = "Comparison Operator";

	//compiled regex in every catergory (input/output, expression, comments, conditional statements)
	
	public static final String startEnd = Lexeme.HAI + "|" + Lexeme.KTHXBYE + Lexeme.GTFO + "|";
	public static final String inputOutput = Lexeme.VISIBLE + "|" + Lexeme.GIMMEH + "|";
	public static final String itz = Lexeme.ITZ + "|";
	public static final String comments = Lexeme.BTW + "|" + Lexeme.OBTW + "|" + Lexeme.TLDR + "|";
	public static final String concat = Lexeme.SMOOSH + "|";
	public static final String conditional = Lexeme.O_RLY + "|" + Lexeme.YA_RLY + "|" + Lexeme.MEBBE + "|" + Lexeme.NO_WAI + "|" + Lexeme.WTF + "|" + Lexeme.OMGWTF + "|" + Lexeme.OMG + "|" + Lexeme.OIC + "|";
	public static final String mathOperator = Lexeme.SUM_OF +"|" + Lexeme.DIFF_OF + "|" + Lexeme.PRODUKT_OF + "|" + Lexeme.QUOSHUNT_OF + "|" + Lexeme.MOD_OF + "|" + Lexeme.BIGGR_OF + "|" + Lexeme.SMALLR_OF + "|";
	public static final String boolOperator = Lexeme.BOTH_OF +  "|" + Lexeme.EITHER_OF +  "|" + Lexeme.WON_OF +  "|" + Lexeme.ANY_OF +  "|" + Lexeme.ALL_OF + "|" +Lexeme.NOT + "|"+Lexeme.BOTH_SAEM + "|" + Lexeme.DIFFRINT + "|"+Lexeme.MKAY+"|";	
	public static final String combineRegex = startEnd + I_HAS_CONSTRUCT + itz + inputOutput + concat + conditional + mathOperator + boolOperator + ALL_LITERALS+"|a!|"+VARIDENT; 
	

	
	private String lexeme;
	private String classification;
	public Lexeme(String lexeme, String classification) {
		this.setLexeme(lexeme);
		this.setClassification(classification);
	}
	public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	
	
	
	
	public static void initializeLexemeDescription() {
		ArrayList<String> code_delimeter = new ArrayList<String>();
		code_delimeter.add(HAI);code_delimeter.add(KTHXBYE);
		Lexeme.TOKENS.put(CODE_DELIMETER,code_delimeter);
		
		
		ArrayList<String> variable_declaration = new ArrayList<String>();
		variable_declaration.add(I_HAS_A);
		Lexeme.TOKENS.put(VARIABLE_DECLARATION,variable_declaration);
		
		
		ArrayList<String> variable_assignment = new ArrayList<String>();
		variable_assignment.add(ITZ);variable_assignment.add(R);
		Lexeme.TOKENS.put(VARIABLE_ASSIGNMENT,variable_assignment);
		
		
		ArrayList<String> delimeter = new ArrayList<String>();
		delimeter.add("AN");delimeter.add("\\bMKAY\\b");delimeter.add("a!");
		Lexeme.TOKENS.put("Delimeter",delimeter);
		
		ArrayList<String> string_delimeter = new ArrayList<String>();
		string_delimeter.add(QUOTE);
		Lexeme.TOKENS.put(STRING_DELIMETER,string_delimeter);
		
		ArrayList<String> ouput_keyword = new ArrayList<String>();
		ouput_keyword.add(VISIBLE);
		Lexeme.TOKENS.put(OUTPUT_KEYWORD,ouput_keyword);
		
		ArrayList<String> input_keyword = new ArrayList<String>();
		input_keyword.add(GIMMEH);
		Lexeme.TOKENS.put(INPUT_KEYWORD,input_keyword);
		
		ArrayList<String> comment_keyword = new ArrayList<String>();
		comment_keyword.add(BTW);comment_keyword.add(OBTW);comment_keyword.add(TLDR);
		Lexeme.TOKENS.put(COMMENT_KEYWORD,comment_keyword);
		
		ArrayList<String> concatenation_keyword = new ArrayList<String>();
		concatenation_keyword .add(SMOOSH);
		Lexeme.TOKENS.put(CONCATENATION_KEYWORD,concatenation_keyword);
		
		ArrayList<String> switch_keyword = new ArrayList<String>();
		switch_keyword.add(WTF);switch_keyword.add(OMG);switch_keyword.add(OMGWTF);switch_keyword.add(OIC);
		switch_keyword.add(O_RLY);switch_keyword.add(YA_RLY);switch_keyword.add(MEBBE);switch_keyword.add(NO_WAI);
		Lexeme.TOKENS.put(CONDITIONAL_KEYWORD,switch_keyword);
		
		
		ArrayList<String> break_identifier = new ArrayList<String>();
		break_identifier.add(GTFO);
		Lexeme.TOKENS.put(BREAK_IDENTIFIER,break_identifier);
		
		ArrayList<String> arithmetic_operator = new ArrayList<String>();
		arithmetic_operator.add(SUM_OF);arithmetic_operator.add(DIFF_OF);arithmetic_operator.add(PRODUKT_OF);
		arithmetic_operator.add(QUOSHUNT_OF);arithmetic_operator.add(MOD_OF);arithmetic_operator.add(BIGGR_OF);
		arithmetic_operator.add(SMALLR_OF);
		Lexeme.TOKENS.put(ARITHMETIC_OPERATOR,arithmetic_operator);
		
		ArrayList<String> boolean_operator = new ArrayList<String>();
		boolean_operator.add(BOTH_OF);boolean_operator.add(EITHER_OF);boolean_operator.add(WON_OF);
		boolean_operator.add(ALL_OF);boolean_operator.add(ANY_OF);boolean_operator.add(NOT);
		Lexeme.TOKENS.put(BOOLEAN_OPERATOR,boolean_operator);
		
		
		ArrayList<String> comparison_operator = new ArrayList<String>();
		comparison_operator.add(BOTH_SAEM);comparison_operator.add(DIFFRINT);
		Lexeme.TOKENS.put(COMPARISON_OPERATOR,comparison_operator);	
		
		ArrayList<String> literalValues = new ArrayList<String>();
		literalValues.add(Lexeme.NUMBR); literalValues.add(Lexeme.NUMBAR);literalValues.add(Lexeme.TROOF[0]);literalValues.add(Lexeme.TROOF[1]);
		literalValues.add(Lexeme.TYPE[0]);literalValues.add(Lexeme.TYPE[1]);literalValues.add(Lexeme.TYPE[2]);literalValues.add(Lexeme.TYPE[3]);
		literalValues.add(Lexeme.YARN);
		Lexeme.TOKENS.put(Lexeme.LITERAL, literalValues);	
		
		ArrayList<String> variable = new ArrayList<String>();
		variable.add(Lexeme.VARIDENT);
		Lexeme.TOKENS.put(Lexeme.VARIABLE_IDENTIFIER,variable);
		
	}
	
	public static void printLexemeContent() { //prints the content of hasmap TOKENS
        for (Map.Entry<String, ArrayList<String>> item : Lexeme.TOKENS.entrySet()) {
            System.out.println(item.getKey());
            for(int i=0;i<item.getValue().size();i++) {
            	System.out.println("\t" + item.getValue().get(i));
            }
        }
	}
	public static String findLexemeType(String word) {
        for (Map.Entry<String, ArrayList<String>> item : Lexeme.TOKENS.entrySet()) {
            for(int i=0;i<item.getValue().size();i++) {
        		Pattern regex= Pattern.compile("^"+item.getValue().get(i)+"$");
    			Matcher regexMatcher = regex.matcher(word);
    			if(regexMatcher.find()) {
                	return item.getKey();
    			}
            }
        }
        return null; //if not found: either it is a variable defined by a user, or error (keyword not part of the program)
	}


	
	
	

}