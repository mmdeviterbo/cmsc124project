import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage; 

public class GameStage{ 
	Group root; Scene scene; Stage stage;
	HBox hb; //window to store INPUT window -Lexeme Window-Symbol Table Window
	VBox vb; //window MAIN layout

	VBox paneLexeme; //lexeme window
	VBox paneSymbolTable; //Symbol Table
	
	TextArea inputUser; VBox paneUser; Button btnSelectFile; FileChooser fileChooser;
	
	Text displayLexeme; Text displaySymbolTable;
	TextArea displayResult; ScrollPane scrollResult; VBox paneResult; 
	
	Button btnExecute;
	
    TableView<Lexeme> lexemeTable; TableColumn<Lexeme, String> columnLexeme; TableColumn<Lexeme, String> columnClassification;
    TableView<SymbolTable> symbolTable; TableColumn<SymbolTable, String> columnIdentifier; TableColumn<SymbolTable, String> columnValue;
	public static final int WINDOW_HEIGHT = 1070;
	public static final int WINDOW_WIDTH= 1890;
	
	public GameStage() {
		//check changes
		this.root = new Group();
		this.hb = new HBox(); this.vb = new VBox();
		this.paneLexeme = new VBox(); this.paneSymbolTable = new VBox();
		this.inputUser = new TextArea();
		this.btnExecute = new Button("Execute");
		this.displayLexeme = new Text(); this.displaySymbolTable = new Text();
		this.displayResult = new TextArea(); this.scrollResult = new ScrollPane();
		this.lexemeTable = new TableView(); this.columnLexeme = new TableColumn<>("Lexeme");
	    this.columnClassification = new TableColumn<>("Classification");
	    this.paneResult = new VBox();
		this.symbolTable = new TableView();
	    this.columnIdentifier = new TableColumn<>("Identifier"); 
	    this.columnValue = new TableColumn<>("Value");
	    this.paneUser= new VBox();
	    this.btnSelectFile = new Button("Select file");
	    this.fileChooser = new FileChooser();
		this.scene = new Scene(root,GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,Color.BLACK); 
	}
	public void setStage(Stage stage) {
		this.stage = stage;
		Lexeme.initializeLexemeDescription();
		this.initializeElements();	    
	    this.initializeWindow();
	    this.initializeTables();
	    this.btnExecuteHandle(); //if clicked, execute the code found on textarea named "inputUser"   
	    this.btnSelecFileHandle(stage);
	}
	private void btnSelecFileHandle(Stage stage){
        btnSelectFile.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
            if(selectedFile==null) {
            	inputUser.setText("No file is selected.");
            }else {
            	try(BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            		String line;
            		String load = "";
            		while((line = reader.readLine()) != null){
                        load = load + line + "\n";
            		}
//            		System.out.println(load);
            		inputUser.setText(load);
                }catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
	}
	private void initializeTables() {
	    this.columnLexeme.setCellValueFactory(new PropertyValueFactory<>("lexeme"));
	    this.columnClassification.setCellValueFactory(new PropertyValueFactory<>("classification"));
	    this.lexemeTable.getColumns().add(this.columnLexeme);
	    this.lexemeTable.getColumns().add(this.columnClassification);
		
	    this.columnIdentifier.setCellValueFactory(new PropertyValueFactory<>("identifier"));
	    this.columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
	    this.lexemeTable.setPlaceholder(new Label("Lexeme"));
	    
	    this.symbolTable.getColumns().add(this.columnIdentifier);
	    this.symbolTable.getColumns().add(this.columnValue);
	    this.symbolTable.setPlaceholder(new Label("Symbol Table"));
	    lexemeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    symbolTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}
	private void initializeWindow() { //put elements in window, make some elements scrollable (Lexeme window, Symbol Table window)
		scrollResult.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollResult.setMinHeight(GameStage.WINDOW_HEIGHT/2);
		scrollResult.setMaxHeight(GameStage.WINDOW_HEIGHT/2);

		paneResult.getChildren().add(displayResult);
		paneResult.setPrefWidth(GameStage.WINDOW_WIDTH);
		paneResult.setPrefHeight(GameStage.WINDOW_HEIGHT);
		paneResult.setStyle("-fx-background-color: #000000;");
		

		paneLexeme.setPrefWidth(GameStage.WINDOW_WIDTH/3);		
		paneSymbolTable.setPrefWidth(GameStage.WINDOW_WIDTH/3);
		paneUser.setPrefWidth(GameStage.WINDOW_WIDTH/2.95);
		
		paneSymbolTable.getChildren().add(symbolTable);
	    paneLexeme.getChildren().add(lexemeTable);
	    
	    paneUser.getChildren().add(btnSelectFile);
	    paneUser.getChildren().add(inputUser);
	    
	    displayResult.setFont(Font.font("Consolas", FontPosture.REGULAR, 19));
	    
	    
	    hb.getChildren().addAll(paneUser,paneLexeme,paneSymbolTable);
	    vb.getChildren().addAll(hb,btnExecute,paneResult);
	    vb.setSpacing(3);
	    hb.setSpacing(3);
	    root.getChildren().add(vb); 
	    stage.setMaximized(true);
	    stage.setTitle("CMSC 124 Project");
//		stage.setResizable(false); //window is not resizable, para hindi na naten problemahin muna yung responsiveness
		stage.setScene(scene); 
		stage.show(); 
	}
	private void initializeElements() {
		inputUser.setPrefWidth(GameStage.WINDOW_WIDTH/3);
		inputUser.setText("BOTH OF WIN AN EITHER OF FAIL AN WON OF WIN AN WIN");
		
		String str = "hello" + "\n" + "hello" + "\n" +"hello" + "\n" +"hello" + "\n" +"hello" + "\n" +"hello" + "\n" +"hello" + "\n" +"hello" + "\n" +"hello" + "\n" +"hello" + "\n" +"hello" + "\n" +"hello" + "\n" +"hello" + "\n" +"hello" + "\n" +"hello" + "\n";
		str = str + str + str; //sample string lang if magsscroll yung window ng "Lexeme" at "Symbol Table"
		
		
		displayLexeme.setText(str);
		displaySymbolTable.setText(str);
	    btnExecute.setPrefWidth(GameStage.WINDOW_WIDTH*1.01);
	    btnExecute.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 5;");
	    btnExecute.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 25));
	    btnExecute.setTextFill(Color.BLACK);
	    
	    btnSelectFile.setPrefWidth(GameStage.WINDOW_WIDTH/2.90);
	    btnSelectFile.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 3;-fx-border-color: black");
	    
	    inputUser.setPrefHeight(GameStage.WINDOW_HEIGHT/2.90);
	    inputUser.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; ");
	    
	    displayResult.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; -fx-font-size: 1.5em;");
	    displayResult.setPrefSize(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
	    displayResult.setEditable(false);
	    
	    lexemeTable.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #9ACD32; -fx-highlight-text-fill: #9ACD32; -fx-text-fill: #9ACD32; ");
	    symbolTable.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #9ACD32; -fx-highlight-text-fill: #9ACD32; -fx-text-fill: #9ACD32; ");
	    
	}

	private ArrayList<String[]> doLexicalAnalysis() {
		clearTables();
		this.symbolTable.getItems().add(new SymbolTable(Lexeme.IT,""));
		
		String[] programInput= this.inputUser.getText().split("\n");
		String wrongOperators = "|\\bBOTH\\b|\\bWON\\b|\\bEITHER\\b|\\bALL\\b|\\bANY\\b|\\bSUM\\b|\\bDIFF\\b|\\bPRODUKT\\b|QUOSHUNT\\b|\\bMOD\\b|\\bBIGGR\\b|\\bSMALLR\\b|\\bDIFFRINT\\b";
		Pattern regex = Pattern.compile(Lexeme.combineRegex+wrongOperators);
		Pattern regexSplit = Pattern.compile("[^\\s\"']+|\"[^\"]*\"");
		
		ArrayList<String[]> tokenizedOutput = new ArrayList<String[]>();
		
		for(int i=0;i<programInput.length;i++) { //programInput = [[sentence1],[sentence2],[sentence3],..,]
			Matcher regexMatcher = regex.matcher(programInput[i]);
			ArrayList<String> tempLine = new ArrayList<String>();
			while (regexMatcher.find()) {
				String match = regexMatcher.group();
				
				if(match.contains(Lexeme.I_HAS_A+" ")) {
					String[] I_HAS_A_ARR = {Lexeme.I_HAS_A}; 
					String[] splitMatch = match.split(Lexeme.I_HAS_A+" ");
					ArrayList<String> result = new ArrayList<String>();
					Matcher regexIHAS= regexSplit.matcher(splitMatch[1]);
					while (regexIHAS.find()) {
						result.add(regexIHAS.group());
					}
					Object[] resultArr = result.toArray();
					String[] combine = new String[I_HAS_A_ARR.length + resultArr.length];
					System.arraycopy(I_HAS_A_ARR, 0, combine, 0, I_HAS_A_ARR.length);  
					System.arraycopy(resultArr, 0, combine, I_HAS_A_ARR.length, resultArr.length);  
			        tokenizedOutput.add(combine);
				}else {
					//edit if this is a varident found in symbol table
					if(match.matches("^BOTH$|^WON$|^EITHER$|^ALL$|^ANY$|^SUM$|^DIFF$|^PRODUKT$|^QUOSHUNT$|^MOD$|^BIGGR$|^SMALLR$|^DIFFRINT$")==true) {
						System.out.println("216 Error: wrong use of operators");
						return null;
					}
					tempLine.add(match);
				}
			}
			String[] arrResult = new String[tempLine.size()];
			for(int a=0;a<tempLine.size();a++) {
				arrResult[a] = tempLine.get(a);	
				
			}
			if(arrResult.length!=0) tokenizedOutput.add(arrResult);
		}
//		for(String[] arr : tokenizedOutput) {
//			System.out.println("224: "+Arrays.toString(arr));
//		}
		
		

		
		//--------------------------this: lexemes already tokenized here in this line-------------------------
		
		for(int k=0;k<tokenizedOutput.size();k++) { //tokenizedOutput = [[word1,word2],[word1,word2],[word1,word2],..,]
			String[] arrLexeme = tokenizedOutput.get(k);
			String classification;
			if(arrLexeme[0].equals(Lexeme.I_HAS_A)) {
				if(arrLexeme.length==2) {
					classification = Lexeme.findLexemeType(arrLexeme[0]);
					if(classification!=null) {
						this.symbolTable.getItems().add(new SymbolTable(arrLexeme[1],""));
						this.lexemeTable.getItems().add(new Lexeme(arrLexeme[0],Lexeme.VARIABLE_DECLARATION));
						this.lexemeTable.getItems().add(new Lexeme(arrLexeme[1],Lexeme.VARIABLE_IDENTIFIER));
					}
				}else if(arrLexeme.length>3) {
					if(checkExpression(arrLexeme[3]).equals("LITERAL")) {
						String removeQuote = arrLexeme[3].replace("\"", "");
						this.symbolTable.getItems().add(new SymbolTable(arrLexeme[1],removeQuote));
						this.lexemeTable.getItems().add(new Lexeme(arrLexeme[0],Lexeme.VARIABLE_DECLARATION));
						this.lexemeTable.getItems().add(new Lexeme(arrLexeme[1],Lexeme.VARIABLE_IDENTIFIER));
						this.lexemeTable.getItems().add(new Lexeme(arrLexeme[2],Lexeme.VARIABLE_ASSIGNMENT));
						this.lexemeTable.getItems().add(new Lexeme(arrLexeme[3],"Literal"));
					}
				}
			}else{
				for(int a=0;a<arrLexeme.length;a++) {
					if(arrLexeme[a]==null) continue;
					classification = Lexeme.findLexemeType(arrLexeme[a]);
					if(classification!=null) {
						this.lexemeTable.getItems().add(new Lexeme(arrLexeme[a],classification));
					}	
				}
			}
		} //for loop k
//		for(String[] arr : tokenizedOutput) {
//		System.out.println(Arrays.toString(arr));
//	}
		return tokenizedOutput;
	} //end function
	
	private String checkExpression(String expr) { //determine if it is an expression, literal/what type of literal, varident
//		public static final String LITERALS = Lexeme.YARN + Lexeme.NUMBR + Lexeme.NUMBAR + Lexeme.TROOF[0] + Lexeme.TROOF[1] + Lexeme.TYPE[0] + Lexeme.TYPE[1] + Lexeme.TYPE[2] + Lexeme.TYPE[3];
		for(int i=0;i<Lexeme.LITERALS.length;i++) {
			if(Pattern.compile("^"+Lexeme.LITERALS[i]+"$").matcher(expr).find()) { 
				return "LITERAL";
			}
		}
		return null;
	}
	private String solveArithmeticOperation(ArrayList<String> stackOperation, int index) {
//		System.out.println("278: "+ Arrays.toString(stackOperation.toArray()));
		if(stackOperation.size()<3) return null;
		
		
		String tempA = stackOperation.get(index).replace("\"",""); String tempB = stackOperation.get(index+1).replace("\"","");
		boolean regexNUMBR_A = tempA.matches("^"+Lexeme.NUMBR+"$"); boolean regexNUMBAR_A = tempA.matches("^"+Lexeme.NUMBAR+"$");
		boolean regexNUMBR_B = tempB.matches("^"+Lexeme.NUMBR+"$"); boolean regexNUMBAR_B = tempB.matches("^"+Lexeme.NUMBAR+"$");
		boolean regexSUM_OF = (stackOperation.get(index+2)).matches(Lexeme.SUM_OF);
		boolean regexDIFF_OF = (stackOperation.get(index+2)).matches(Lexeme.DIFF_OF);
		boolean regexPROD_OF = (stackOperation.get(index+2)).matches(Lexeme.PRODUKT_OF);
		boolean regexQUO_OF = (stackOperation.get(index+2)).matches(Lexeme.QUOSHUNT_OF);
		boolean regexMOD_OF = (stackOperation.get(index+2)).matches(Lexeme.MOD_OF);
		boolean regexBIGGR_OF = (stackOperation.get(index+2)).matches(Lexeme.BIGGR_OF);
		boolean regexSMALLR_OF = (stackOperation.get(index+2)).matches(Lexeme.SMALLR_OF);
		boolean regexArith= (stackOperation.get(index+2)).matches(Lexeme.mathOperator.substring(0,Lexeme.mathOperator.length()-1));
		
		float A=0,B=0; float ans = 0; boolean isFloat=false;
		
		if(stackOperation.size()>2 && (regexNUMBR_A || regexNUMBAR_A) && (regexNUMBR_B || regexNUMBAR_B)) {
			if(regexArith) {
				String Astr = stackOperation.remove(index).replace("\"", "");
				String Bstr = stackOperation.remove(index).replace("\"", "");
				if(regexNUMBR_A) A = (int) Integer.parseInt(Astr);
				else if (regexNUMBAR_A) {
					A = Float.parseFloat(Astr); 
					isFloat=true;
				}
				if(regexNUMBR_B) B = (int) Integer.parseInt(Bstr);
				else if(regexNUMBAR_B) {
					B = Float.parseFloat(Bstr);
					isFloat=true;
				}
				if(isFloat==false) ans = (int) ans;
				else ans = (float) ans;
				//check
				if(regexSUM_OF) ans = A+B;
				else if(regexDIFF_OF) ans = B-A;
				else if(regexPROD_OF) ans = A*B;
				else if(regexQUO_OF) ans = B/A;
				else if(regexMOD_OF) ans = B%A;
				else if(regexBIGGR_OF) ans = (B>A)? B:A;
				else if(regexSMALLR_OF) ans = (A<B)? A:B;
				stackOperation.remove(index);
				
				if(isFloat==true) stackOperation.add(index,Float.toString(ans));
				else if(isFloat==false) stackOperation.add(index,Integer.toString((int)ans));
				solveArithmeticOperation(stackOperation,0);	
			} else solveArithmeticOperation(stackOperation,index+1);
			
		}else solveArithmeticOperation(stackOperation,index+1);
		if(stackOperation.size()==1) return stackOperation.get(0);
		else return null; //wrong construct is used
	}

	private String setArithmeticOperation(String[] lexList) {
		ArrayList<String> stackOperation = new ArrayList<String>();
		String regexNum = "^"+Lexeme.NUMBR+"$"+"|"+"^"+Lexeme.NUMBAR+"$"+"|"+"^\""+Lexeme.NUMBR+"\"$" +"|"+ "^\""+Lexeme.NUMBAR+"\"$";
		for(int i=0;i<lexList.length;i++) {
			if(lexList[i].matches("SUM OF")) {
				stackOperation.add(0,"SUM OF");
			}else if(lexList[i].matches("DIFF OF")){
				stackOperation.add(0,"DIFF OF");
			}else if(lexList[i].matches("PRODUKT OF"))  {
				stackOperation.add(0,"PRODUKT OF");
			}else if(lexList[i].equals("QUOSHUNT OF")) {
				stackOperation.add(0,"QUOSHUNT OF");
			}else if(lexList[i].equals("MOD OF")) {
				stackOperation.add(0,"MOD OF");
			}else if(lexList[i].equals("BIGGR OF")) {
				stackOperation.add(0,"BIGGR OF");
			}else if(lexList[i].equals("SMALLR OF")) {
				stackOperation.add(0,"SMALLR OF");
			}else if(lexList[i].matches(regexNum)) {
				stackOperation.add(0,lexList[i]);
				if(i+1!=lexList.length && lexList[i+1].matches(regexNum)==true) return null;
			}else if(lexList[i].matches("^AN$")){
				if(i+1!=lexList.length) {
					if(lexList[i+1].matches("AN")) return null;
					if(i!=0 && lexList[i].matches("AN") && (lexList[i-1].matches(regexNum)==false)) return null;
				}else if(i+1==lexList.length && lexList[i].matches(regexNum)==false) return null;
			}else if(lexList[i].matches(Lexeme.VARIDENT)) { //kulang pa: check if it exists in symbol table, if yes: get its value, else error/not existing
				stackOperation.add(0,lexList[i]);
			}else {
				return null;
			}
		}
		String answer = solveArithmeticOperation(stackOperation,0);
		return answer;	

	}

	private String solveBooleanOperation(ArrayList<String> stackOperation, int index, boolean isArity) {
		String tempA,tempB;
//		System.out.println(Arrays.toString(stackOperation.toArray()));
		
		//for arity - any of/all of
		boolean isAllTroof=true;
		for(String lexeme : stackOperation) {
			if(lexeme.matches("\\bBOTH OF\\b|\\bEITHER OF\\b|\\bNOT\\b|\\bWON OF\\b") && isArity) 
				isAllTroof = false;
		}
		if(isAllTroof && isArity) return Arrays.toString(stackOperation.toArray());
		
		if(stackOperation.size()==1 && !isArity) return stackOperation.get(0);
		else {
			tempA = stackOperation.get(index);
			tempB = stackOperation.get(index+1); 
		}
		String ans="";
		boolean regexTROOF_A=true,regexTROOF_B=true, regexBOTH_OF = true, regexEITHER_OF=true,regexWON_OF=true,regexNOT=true;
		if(stackOperation.size()==2 && (stackOperation.get(index+1)).matches(Lexeme.NOT)) { //not operator - need one operand
			ans = stackOperation.remove(index).matches(Lexeme.TROOF[0])? Lexeme.TROOF[1] : Lexeme.TROOF[0];
			stackOperation.remove(index);
			stackOperation.add(index,ans);
		}else if(stackOperation.size()>2) { //two or more operands
			String A,B; 
			if(tempB.matches(Lexeme.NOT)) {
				A = stackOperation.remove(index).matches(Lexeme.TROOF[0])? Lexeme.TROOF[1]: Lexeme.TROOF[0];
				stackOperation.remove(index);
				stackOperation.add(index,A);
				solveBooleanOperation(stackOperation,0,isArity);	
			}else if(tempB.matches("^"+Lexeme.TROOF[0]+"$|^"+Lexeme.TROOF[1]+"$")){
				regexTROOF_A = tempA.matches("^"+Lexeme.TROOF[0]+"$|^"+Lexeme.TROOF[1]+"$");
				regexTROOF_B = tempB.matches("^"+Lexeme.TROOF[0]+"$|^"+Lexeme.TROOF[1]+"$");
				regexBOTH_OF = (stackOperation.get(index+2)).matches(Lexeme.BOTH_OF);
				regexEITHER_OF = (stackOperation.get(index+2)).matches(Lexeme.EITHER_OF);
				regexWON_OF = (stackOperation.get(index+2)).matches(Lexeme.WON_OF);	
				
				boolean regexBool= (stackOperation.get(index+2)).matches("^"+Lexeme.BOTH_OF+"$|^"+Lexeme.EITHER_OF+"$|^"+Lexeme.WON_OF+"$");	
				if(stackOperation.size()>2 && (regexTROOF_A && regexTROOF_B) && regexBool) {
					A = stackOperation.remove(index);
					B = stackOperation.remove(index);
					boolean Abool = A.matches("^"+Lexeme.TROOF[0]+"$")? true : false;
					boolean Bbool = B.matches("^"+Lexeme.TROOF[0]+"$")? true : false;
					if(regexBOTH_OF) ans = Abool&&Bbool==true? Lexeme.TROOF[0] : Lexeme.TROOF[1];
					else if(regexEITHER_OF) ans = Abool||Bbool==true? Lexeme.TROOF[0] : Lexeme.TROOF[1];
					else if(regexWON_OF) ans = Abool==Bbool==true? Lexeme.TROOF[1] : Lexeme.TROOF[0];
					stackOperation.remove(index);
					stackOperation.add(index,ans);
					solveBooleanOperation(stackOperation,0,isArity);	
				}else solveBooleanOperation(stackOperation,index+1,isArity);
			}
		}
		if(stackOperation.size()==1 && !isArity) return stackOperation.get(0);
		else if(stackOperation.size()>=1 && isArity) return Arrays.toString(stackOperation.toArray());
		if(!isArity && stackOperation.size()>1) return null;
		else return null; //wrong construct is used
	}
	
	
	private String setBooleanOperationArity(String[] lexList) {
//		System.out.println(Arrays.toString(lexList));
		ArrayList<String> operand = new ArrayList<String>();		
		String regexBool = Lexeme.boolOperator.substring(0,Lexeme.boolOperator.length()-33);
		
		System.out.println("433: " + Arrays.toString(lexList));
		for(int i=1;i<lexList.length-1;i++) { //start at 1 since any/all of is not included, length-1 since mkay is not included: only between ANs are included
			if(lexList[i].matches(regexBool+"|\\b"+Lexeme.TROOF[0]+"\\b|\\b"+Lexeme.TROOF[1]+"\\b|\\b"+Lexeme.MKAY+"\\b")) {
				operand.add(0,lexList[i]);
				if(lexList[i].matches("\\b"+Lexeme.TROOF[0]+"\\b|\\b"+Lexeme.TROOF[1]+"\\b") && i!=0){
					if(lexList[i-1].matches("\\b"+Lexeme.TROOF[0]+"\\b|\\b"+Lexeme.TROOF[1]+"\\b")) return null;
				}
			}else if(lexList[i].matches("\\bAN\\b")) {
				if(i!=0 && lexList[i-1].matches("\\bAN\\b")) return null;
				if(i==lexList.length-2) return null;
			}else {
				return null;
			}
		}
		boolean isAllTroof = true;
		for(String bool : operand) {
			if(bool.matches("\\b"+Lexeme.TROOF[0]+"\\b|\\b"+Lexeme.TROOF[1]+"\\b")==false) {
				isAllTroof = false;
				break;
			}
		}
		String arrStr = Arrays.toString(operand.toArray()).replaceAll("\\[|\\]|,", "");
		if(isAllTroof && lexList[0].matches("\\bANY OF\\b") && arrStr.contains("WIN")) return Lexeme.TROOF[0];
		else if(isAllTroof && lexList[0].matches("\\bANY OF\\b") && !arrStr.contains("WIN")) return Lexeme.TROOF[1];
		else if(isAllTroof && lexList[0].matches("\\bALL OF\\b") && arrStr.contains("FAIL")) return Lexeme.TROOF[1];
		else if(isAllTroof && lexList[0].matches("\\bALL OF\\b") && !arrStr.contains("FAIL")) return Lexeme.TROOF[0];
		
		String answer = solveBooleanOperation(operand,0,true).replaceAll("\\[|\\]|,", "");
		if(lexList[0].matches("\\bANY OF\\b") && answer.contains("WIN")) return Lexeme.TROOF[0];
		else if(lexList[0].matches("\\bANY OF\\b") && !answer.contains("WIN")) return Lexeme.TROOF[1];
		else if(lexList[0].matches("\\bALL OF\\b") && answer.contains("FAIL")) return Lexeme.TROOF[1];
		else if(lexList[0].matches("\\bALL OF\\b") && !answer.contains("FAIL")) return Lexeme.TROOF[0];
		return null;
	}
	
	
	private String setBooleanOperation(String[] lexList){
//		System.out.println(Arrays.toString(lexList));
		ArrayList<String> stackOperation = new ArrayList<String>();
		String regexBool= "^"+Lexeme.TROOF[0]+"$"+"|"+"^"+Lexeme.TROOF[1]+"$";
		
		if(lexList[0].matches("\\bALL OF\\b") || lexList[0].matches("\\bANY OF\\b")) {	
			if(lexList[0].matches("\\bALL OF\\b")) stackOperation.add(0,"ALL OF");
			else if(lexList[0].matches("\\bANY OF\\b")) stackOperation.add(0,"ANY OF");
			if(lexList[lexList.length-1].matches("\\bMKAY\\b")==false) return null;
			else return setBooleanOperationArity(lexList);
		}
		
		for(int i=0;i<lexList.length;i++) {
			if(lexList[i].matches("BOTH OF")) {
				stackOperation.add(0,"BOTH OF");
			}else if(lexList[i].matches("EITHER OF")){
				stackOperation.add(0,"EITHER OF");
			}else if(lexList[i].matches("WON OF"))  {
				stackOperation.add(0,"WON OF");
			}else if(lexList[i].equals("NOT")) {
				stackOperation.add(0,"NOT");
			}else if(lexList[i].matches(regexBool)) {
				stackOperation.add(0,lexList[i]);
				if(i+1!=lexList.length && lexList[i+1].matches(regexBool)==true) return null;
			}else if(lexList[i].matches("^AN$")){
				if(i+1!=lexList.length) {
					if(lexList[i+1].matches("AN")) return null;
					if(i!=0 && lexList[i].matches("AN") && (lexList[i-1].matches(regexBool)==false)) return null;
				}else if(i+1==lexList.length && lexList[i].matches(regexBool)==false) return null;
			}else if(lexList[i].matches("\\bMKAY\\b")) {
				if(i+1==lexList.length) return null;
				if(lexList[i].matches("\\bANY OF\\b")==false && lexList[i].matches("\\bALL OF\\b")==false) return null;
			}else if(lexList[i].matches(Lexeme.VARIDENT)) { //check if variable is exist in symbol table and if type is troof type
				stackOperation.add(0,lexList[i]);
			}else {
				return null;
			}
		}
		if(stackOperation.size()<=1) return null;
//		System.out.println("479"+Arrays.toString(stackOperation.toArray()));
		String ans = solveBooleanOperation(stackOperation,0,false); 
		return ans;
		

	}
	private void clearTables() {
		lexemeTable.getItems().clear();
		symbolTable.getItems().clear();
	}

	
	
	private void doSyntaxAnalysis(ArrayList<String[]> tokensPerLine) {
		for(int i=0;i<tokensPerLine.size();i++) {
			String[] tokenArrLine = tokensPerLine.get(i);
			if(tokenArrLine[0].matches(Lexeme.mathOperator.substring(0, Lexeme.mathOperator.length()-1))) {
				try {
					String ans = setArithmeticOperation(tokenArrLine);
					if(ans!=null) {
						//set text to display to that says "Syntax Error"
//						//store variable IT since there is no VISIBLE keyword before
						System.out.println("Temporary print of ans: " + ans);
					}else displayResult.setText(displayResult.getText()+"\n"+"473 Syntax Error.");
				}catch(Exception e) {
					displayResult.setText(displayResult.getText()+"\n"+"475 Syntax Error.");
				}
				//continue coding here,  add return value to symbol table and update it
			}else if(tokenArrLine[0].matches(Lexeme.boolOperator.substring(0, Lexeme.boolOperator.length()-33))) { 
				try {
					String ans = setBooleanOperation(tokenArrLine);
					if(ans!=null) System.out.println("481 Correct syntax: " + ans);
					else {
						clearTables();
						displayResult.setText(displayResult.getText()+"\n"+"481 Syntax Error.");	
					}
				}catch(Exception e) {
					clearTables();
					displayResult.setText(displayResult.getText()+"\n"+"484 Syntax Error.");
				}
			}else {
				clearTables();
				displayResult.setText(displayResult.getText()+"\n"+"487 Syntax Error.");
			}
		}

	}
	
//	private void doGIMMEH() {
        //for gimmeh part
//    	displayResult.setEditable(true);
//    	displayResult.setText(displayResult.getText()+"Enter input: ");
////    	displayResult.setText("Hello world\nHello world\nHello world\nHello world\nHello world\nHello world\nHello world\nHello world\nHello world\nHello world\nHello world\nHello world\nHello world\nHello world\nHello world\nhe\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\ndasfdfsdfd\n\n\n\n\n\n\n\n\n\n\ndsfsdfds");
//    	
//    	displayResult.setOnKeyPressed(new EventHandler<KeyEvent>()
//    	 {
//    	   @Override
//    	   public void handle(KeyEvent ke)
//    	   {
//    	     if (ke.getCode().equals(KeyCode.ENTER))
//    	     {
//    	    	 //store this input to new variable
//    	       System.out.println(displayResult.getText().split("Enter input: ")[1]);
//    	       displayResult.setText(displayResult.getText()+"\n");
//    	       displayResult.setEditable(false);`
//    	     }
//    	   }
//    	 });
//	}
	
	
	private void btnExecuteHandle() { //get and process the code input by user from texarea named "inputUser"
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e)
            { 
            	try {
            		ArrayList<String[]> tokensPerLine = doLexicalAnalysis();
            		if(tokensPerLine!=null) doSyntaxAnalysis(tokensPerLine);
            		else {
            			clearTables();
            			displayResult.setText(displayResult.getText()+"\n"+" 499 Syntax Error.");
            		}
            	}catch(Exception e1) {
            		clearTables();
            		displayResult.setText(displayResult.getText()+"\n"+"501 Syntax Error.");
            	}
            	
            	
            }
		};
		btnExecute.setOnAction(event); 
		
	}
} 










