import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	boolean isNewLine, isDeadCode;
	
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
            }else {
            	try(BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            		if(selectedFile.toString().matches(".+\\.lol$")==false) {
            			inputUser.setText("Please insert lolcode file!");
            			return;
            		}
            		
            		String line;
            		String load = "";
            		while((line = reader.readLine()) != null){
                        load = load + line + "\n";
            		}
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
	    
	    
	    symbolTable.setEditable(true);
	    lexemeTable.setEditable(true);
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
		isNewLine=true;
		isDeadCode=false;
		
		inputUser.setPrefWidth(GameStage.WINDOW_WIDTH/3);
		inputUser.setWrapText(true);
		
		//sample input for test only
		inputUser.setText("HAI\n");
//		inputUser.setText(inputUser.getText()+"\n\nI HAS A VAR ITZ 0\nI HAS A VAR2 ITZ 0\nI HAS A VAR3 ITZ 0\nIM IN YR LOOPY UPPIN YR VAR TIL BOTH SAEM VAR AN 10"
//				+ "\n\tVISIBLE SUM OF VAR AN 1\n\tIM IN YR LOOPY2 UPPIN YR VAR2 TIL BOTH SAEM VAR2 AN 3\n\t\tVISIBLE \"this is inner-1\"\n\tIM OUTTA YR LOOPY2\n\tVAR2 R 0"
//				+ "\n\n\tIM IN YR LOOPY2 UPPIN YR VAR3 TIL BOTH SAEM VAR3 AN 3\n\t\tVISIBLE \"this is inner-2\"\n\tIM OUTTA YR LOOPY2\n\tVAR3 R 0\n"
//				+ "\tVISIBLE \"is deadcode\"\n"
//				+ "IM OUTTA YR LOOPY\n");
		 inputUser.setText(inputUser.getText() + "VISIBLE BOTH OF WIN AN WIN");
//		inputUser.setText(inputUser.getText() + "SUM OF 10 AN 10\nWTF?\nOMG 20\n\tVISIBLE \"first choice\"\nOMG 30\n\tVISIBLE \"2nd choice\"\nOMG 40\n\tVISIBLE \"3rd choice\"\nOMGWTF\n\tVISIBLE \"default choice\" \nOIC");
//		inputUser.setText(inputUser.getText() + "\n\n\tOBTW dsadsda\ndsdasdasadas\nsadasdsdasd\n\tTLDR");
		inputUser.setText(inputUser.getText() + "\nKTHXBYE");		
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
	    inputUser.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; -fx-font-size: 1.2em;");
	    
	    displayResult.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; -fx-font-size: 1.5em;");
	    displayResult.setPrefSize(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT/2);
	    displayResult.setEditable(false);
	    
	    
	    
	    lexemeTable.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #9ACD32; -fx-highlight-text-fill: #9ACD32; -fx-text-fill: #9ACD32; ");
	    symbolTable.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #9ACD32; -fx-highlight-text-fill: #9ACD32; -fx-text-fill: #9ACD32; ");
	    
	}

	private String getValueVarident(String variable) {
		for(SymbolTable rowData : symbolTable.getItems()) {
			if(variable.equals(rowData.getIdentifier())) {
				return rowData.getValue();
			}
		}
		return null;
	}
	
	private String solveArithmeticOperation(ArrayList<String> stackOperation, int index) {
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
			if(lexList[i].matches(Lexeme.SUM_OF)) stackOperation.add(0,"SUM OF");
			else if(lexList[i].matches(Lexeme.DIFF_OF)) stackOperation.add(0,"DIFF OF");
			else if(lexList[i].matches(Lexeme.PRODUKT_OF)) stackOperation.add(0,"PRODUKT OF");
			else if(lexList[i].matches(Lexeme.QUOSHUNT_OF)) stackOperation.add(0,"QUOSHUNT OF");
			else if(lexList[i].matches(Lexeme.MOD_OF)) stackOperation.add(0,"MOD OF");
			else if(lexList[i].matches(Lexeme.BIGGR_OF)) stackOperation.add(0,"BIGGR OF");
			else if(lexList[i].matches(Lexeme.SMALLR_OF)) stackOperation.add(0,"SMALLR OF");
			else if(lexList[i].matches(regexNum)) {
				stackOperation.add(0,lexList[i]);
				if(i+1!=lexList.length && lexList[i+1].matches(regexNum)==true) return null;
			}else if(lexList[i].matches("\\bAN\\b")){
				if(i+1!=lexList.length) {
					if(lexList[i+1].matches("\\bAN\\b")) return null;
				}else if(i+1==lexList.length && lexList[i].matches(regexNum)==false) return null;
			}else if(lexList[i].matches(Lexeme.VARIDENT) && !lexList[i].matches("\\bAN\\b")) { //kulang pa: check if it exists in symbol table, if yes: get its value, else error/not existing
				String valueVar = getValueVarident(lexList[i]);
				if(valueVar!=null) stackOperation.add(0,valueVar);
				else return null;
			}else return null;
		}
		String answer = solveArithmeticOperation(stackOperation,0);
		return answer;	

	}

	private String solveBooleanOperation(ArrayList<String> stackOperation, int index) {
		System.out.println(Arrays.deepToString(stackOperation.toArray()));
		if(stackOperation.size()==1) return stackOperation.get(0);
		
		String tempA,tempB;
		if(stackOperation.size()==1) return stackOperation.get(0); //final answer then return the value
		else {
			tempA = stackOperation.get(index);
			tempB = stackOperation.get(index+1); 
		}
		String ans="";
		if(stackOperation.size()==2 && (stackOperation.get(index+1)).matches(Lexeme.NOT)) { //not operator - need one operand
			if(!stackOperation.get(index).matches(Lexeme.TROOF[0]+"|"+Lexeme.TROOF[1])) return null;
			ans = stackOperation.remove(index).matches(Lexeme.TROOF[0])? Lexeme.TROOF[1] : Lexeme.TROOF[0];
			stackOperation.remove(index);
			stackOperation.add(index,ans);
		}else if(stackOperation.size()>2) { //may valid expression
			String A,B;
			if(tempB.matches(Lexeme.NOT)) {
				if(!tempA.matches(Lexeme.TROOF[0]+"|"+Lexeme.TROOF[1])) return null;
				A = stackOperation.remove(index).matches(Lexeme.TROOF[0])? Lexeme.TROOF[1]: Lexeme.TROOF[0];
				stackOperation.remove(index);
				stackOperation.add(index,A);
				solveBooleanOperation(stackOperation,0);	
			}else if(tempB.matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-3))){
				boolean Atype= tempA.matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-3));
				boolean Btype = tempB.matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-3));
				boolean regexBool= (stackOperation.get(index+2)).matches(Lexeme.mathOperator+Lexeme.boolOperator);
				if(stackOperation.size()>2 && Atype && Btype && regexBool && !(stackOperation.get(index+2)).matches(Lexeme.NOT)) {				
					A = stackOperation.remove(index); B = stackOperation.remove(index);
					boolean Abool = A.matches("\\b"+Lexeme.TROOF[0]+"\\b")? true : false;
					boolean Bbool = B.matches("\\b"+Lexeme.TROOF[0]+"\\b")? true : false;

					String[] validExpressions = (Lexeme.mathOperator+Lexeme.boolOperator).split("\\|");
					for(int a=0;a<validExpressions.length-1;a++) {
						if(stackOperation.get(index).matches(validExpressions[a])){ //based on array: 0-6 indeces are math, 7-12 boolean, 13-14 comparison
							String actualOperator = validExpressions[a].replace("\\b","");
							String lexList[] = {actualOperator,B,"AN",A};
							if(a>=0 && a<=6) { //then it is math expression
								ans = setArithmeticOperation(lexList);
							}else if(a>=7 && a<=12) { //then it is relational op
								if(actualOperator.matches(Lexeme.BOTH_OF)) ans = Abool&&Bbool==true? Lexeme.TROOF[0] : Lexeme.TROOF[1];
								else if(actualOperator.matches(Lexeme.EITHER_OF)) ans = Abool||Bbool==true? Lexeme.TROOF[0] : Lexeme.TROOF[1];
								else if(actualOperator.matches(Lexeme.WON_OF)) ans = Abool==Bbool==true? Lexeme.TROOF[1] : Lexeme.TROOF[0];
							}else if(a>=13 && a<=14) { //then it is comparison
								ans = setComparisonOperation(lexList);
							}
							break;
						}
					}
					stackOperation.remove(index); //remove the operation used
					stackOperation.add(index,ans); //push the answer from recent expression
					solveBooleanOperation(stackOperation,0);
				}else solveBooleanOperation(stackOperation,index+1);
			}
		}
		if(stackOperation.size()==1) return stackOperation.get(0);
		if(stackOperation.size()>1) return null;
		return null;
	}
	
	private String setBooleanOperationArity(String[] lexList) {
		ArrayList<String> operand = new ArrayList<String>();		
		String regexOperation = Lexeme.mathOperator+Lexeme.boolOperator;
		String allLiterals = Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-3);
		
		for(int i=1;i<lexList.length-1;i++) { //start at 1 since any/all of is not included, length-1 since mkay is not included: only between ANs are included
			if(lexList[i].matches(regexOperation+allLiterals)) {
				operand.add(0,lexList[i]);
				if(lexList[i].matches(Lexeme.ALL_LITERALS) && i!=0){
					if(lexList[i-1].matches(Lexeme.ALL_LITERALS) && !lexList[i-1].matches("\\bAN\\b")) {
						return null;
					}
				}
			}else if(lexList[i].matches("\\bAN\\b")) {
				if(i!=0 && lexList[i-1].matches("\\bAN\\b")) return null;
				if(i==lexList.length-2) return null;
			}else if(lexList[i].matches(Lexeme.VARIDENT)) {
				String value = getValueVarident(lexList[i]);
				if(value!=null) operand.add(0,value); //if variable is not found on symboltable (or not decalred)
				else return null;
			}else return null;
			
		}
		boolean isAllTroof = true;
		for(String bool : operand) {
			if(bool.matches("\\b"+Lexeme.TROOF[0]+"\\b|\\b"+Lexeme.TROOF[1]+"\\b")==false) {
				isAllTroof = false;
				break;
			}
		}
		
		//if all literals/varident only
		String arrStr = Arrays.toString(operand.toArray()).replaceAll("\\[|\\]|,", "");
		if(isAllTroof && lexList[0].matches(Lexeme.ANY_OF) && arrStr.contains("WIN")) return Lexeme.TROOF[0];
		else if(isAllTroof && lexList[0].matches(Lexeme.ANY_OF) && !arrStr.contains("WIN")) return Lexeme.TROOF[1];
		else if(isAllTroof && lexList[0].matches(Lexeme.ALL_OF) && arrStr.contains("FAIL")) return Lexeme.TROOF[1];
		else if(isAllTroof && lexList[0].matches(Lexeme.ALL_OF) && !arrStr.contains("FAIL")) return Lexeme.TROOF[0];
		
		//if there's nested expressions
		String answer = solveBooleanOperation(operand,0).replaceAll("\\[|\\]|,", "");
		if(lexList[0].matches(Lexeme.ANY_OF) && answer.contains("WIN")) return Lexeme.TROOF[0];
		else if(lexList[0].matches(Lexeme.ANY_OF) && !answer.contains("WIN")) return Lexeme.TROOF[1];
		else if(lexList[0].matches(Lexeme.ALL_OF) && answer.contains("FAIL")) return Lexeme.TROOF[1];
		else if(lexList[0].matches(Lexeme.ALL_OF) && !answer.contains("FAIL")) return Lexeme.TROOF[0];
		return null;
	}
	
	private String setBooleanOperation(String[] lexList){
		ArrayList<String> stackOperation = new ArrayList<String>();
		if(lexList[0].matches(Lexeme.ALL_OF+"|"+Lexeme.ANY_OF)) {
			if(checkInvalidNest(lexList)) return null;
			if(!lexList[lexList.length-1].matches(Lexeme.MKAY)) return null;
			else return setBooleanOperationArity(lexList);
		}
		
		//boolean expression may contain any of the other expressions
		String regexLiteral= Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-3);
		for(int i=0;i<lexList.length;i++) {
			String[] validExpressions = (Lexeme.mathOperator+Lexeme.boolOperator).split("\\|");
			boolean isOperator=false;
			for(int a=0;a<validExpressions.length-1;a++) {
				String actualOperator = validExpressions[a].replace("\\b","");
				if(actualOperator.matches(Lexeme.ANY_OF+"|"+Lexeme.ALL_OF)) continue;
				if(actualOperator.matches(lexList[i])) {
					stackOperation.add(0,actualOperator);
					isOperator=true;	
				}
			}
			if(isOperator) isOperator=false;			
			else if(lexList[i].matches(regexLiteral)) {
				stackOperation.add(0,lexList[i]);
				if(i+1<lexList.length && lexList[i+1].matches(regexLiteral)) return null;
			}else if(lexList[i].matches("\\bAN\\b")) {
				if(i+1<lexList.length) if(lexList[i+1].matches("\\bAN\\b")) return null;
			}
			else if(lexList[i].matches(Lexeme.MKAY)) {
				if(i+1!=lexList.length) return null;
				if(!lexList[0].matches(Lexeme.ANY_OF) || !lexList[0].matches(Lexeme.ALL_OF)) return null;
			}else if(lexList[i].matches(Lexeme.VARIDENT)) { //check if variable is exist in symbol table and if type is troof type
				String value = getValueVarident(lexList[i]);
				if(value!=null) stackOperation.add(0,value);
				else return null;
			}else return null;
		}
		if(stackOperation.size()<=1) return null;
		String ans = solveBooleanOperation(stackOperation,0); 
		return ans;
	}
	
	@SuppressWarnings("unused")
	private String setComparisonOperation(String[] lexList) {
		
//		System.out.println("Given: "+Arrays.deepToString(lexList));
		
		String regexNum = Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-3);
		String regexMath = Lexeme.boolOperator + Lexeme.mathOperator.substring(0,Lexeme.mathOperator.length()-1);
		String isRelationOp = null; //-1 means false, 1 means biggr of, 2 means smallr

		ArrayList<String> operandA = new ArrayList<String>();
		ArrayList<String> operandB = new ArrayList<String>();
		
		if(lexList[0].matches(Lexeme.BOTH_SAEM) || lexList[0].matches(Lexeme.DIFFRINT)) {			
			int i=1;
			for(i=1;i<lexList.length;i++) { //getting operandA
				if(lexList[i].matches(regexNum+"|"+Lexeme.VARIDENT) && !lexList[i].matches(regexMath+"|AN")) {
					if(lexList[i].matches(regexNum)) { //literal operand
						operandA.add(lexList[i]);
					}else if(lexList[i].matches(Lexeme.VARIDENT)) { //varident operand
						String temp = getValueVarident(lexList[i]);
						if(temp==null) return null;
						lexList[i] = temp;
						operandA.add(lexList[i]);
					}
					if(i==1) {
						if(!lexList[i+1].matches("AN")) return null;
						i+=2;
						break;
					}else if(i+2<lexList.length && lexList[i+1].matches("AN") && lexList[i+2].matches(regexNum+"|"+Lexeme.VARIDENT)) {
						operandA.add(lexList[i+1]);
						if(lexList[i+2].matches(Lexeme.NUMBAR) && lexList[i+2].matches("[0-9]+[.]0+")) {
							lexList[i+2] = lexList[i].split("\\.")[0];
						}else if(lexList[i+2].matches(Lexeme.VARIDENT)) {
							String temp = getValueVarident(lexList[i+2]);
							if(temp==null) return null;
							lexList[i+2] = temp;
						}
						operandA.add(lexList[i+2]);i+=4; break;

					}
				}else if(lexList[i].matches(regexMath+"|AN")) {
					operandA.add(lexList[i]);
				}else return null;
			}
			
			for(int j=i;j<lexList.length;j++) { //getting operandB
				if(lexList[j].matches(regexNum+"|"+Lexeme.VARIDENT) && !lexList[j].matches(regexMath+"|AN")) {
					if(lexList[j].matches(regexNum)) {
						operandB.add(lexList[j]);
					}
					else if(lexList[j].matches(Lexeme.VARIDENT)) {
						String temp = getValueVarident(lexList[j]);
						if(temp==null) return null;
						lexList[j] = temp;
						operandB.add(lexList[j]);
					}else if(j+2<lexList.length && lexList[j+1].matches("AN") && lexList[j+2].matches(regexNum+"|"+Lexeme.VARIDENT)) {
						operandB.add(lexList[j+1]);
						if(lexList[j+2].matches(Lexeme.NUMBAR) && lexList[j+2].matches("[0-9]+[.]0+")) {
							lexList[j+2] = lexList[j+2].split("\\.")[0];
						}else if(lexList[j+2].matches(Lexeme.VARIDENT)) {
							String temp = getValueVarident(lexList[j+2]);
							if(temp==null) return null;
							lexList[j+2] = temp;
						}
						operandB.add(lexList[j+2]);
					}
				}else if(lexList[j].matches(regexMath+"|AN")) operandB.add(lexList[j]);
				else return null;

			}
			
			if(operandA.size()==0 || operandB.size()==0) return null;
		
			String[] operandAtemp = new String[operandA.size()];
			for(int a=0;a<operandA.size();a++) operandAtemp[a] = operandA.get(a);
			String[] operandBtemp = new String[operandB.size()];
			for(int b=0;b<operandB.size();b++) operandBtemp[b] = operandB.get(b);
			
			//if list has more than one size, then it has expression (to be solved), else it has only literal/variables
			String valueA = operandA.size()==1? operandA.get(0) : allOperations(operandAtemp);
			String valueB = operandB.size()==1? operandB.get(0) : allOperations(operandBtemp);
			
			if(valueA==null || valueB==null) return null;
			
			
			//if operand has literal/varident but has TROOF values, it must result FAIL if BOTH SAEM, Error if diffrint
			if(!valueA.matches(Lexeme.NUMBAR+"|"+Lexeme.NUMBR)) return Lexeme.TROOF[1];
			if(!valueB.matches(Lexeme.NUMBAR+"|"+Lexeme.NUMBR)) return Lexeme.TROOF[1];
			
			if(valueA.matches("[0-9]+[.]0+")) valueA = valueA.split("\\.")[0];
			if(valueB.matches("[0-9]+[.]0+")) valueB = valueB.split("\\.")[0];
			
						
			if(lexList[0].matches(Lexeme.DIFFRINT) && !valueA.contentEquals(valueB)) return Lexeme.TROOF[0];
			else if(lexList[0].matches(Lexeme.DIFFRINT) && valueA.contentEquals(valueB)) return Lexeme.TROOF[1];
			if(lexList[0].matches(Lexeme.BOTH_SAEM) && valueA.contentEquals(valueB)) return Lexeme.TROOF[0];
			else if(lexList[0].matches(Lexeme.BOTH_SAEM) && !valueA.contentEquals(valueB)) return Lexeme.TROOF[1];			
		}
		return null;
	}
	
	private boolean checkIfVarExist(String lex) {
		for(SymbolTable row : symbolTable.getItems()) if(row.getIdentifier().equals(lex)) return true;
		return false;
	}
	
	private boolean checkIfLexemeExist(String lex) {
		for(Lexeme row : lexemeTable.getItems()) if(row.getLexeme().equals(lex)) return true;
		return false;
	}

	private void updateVar(String var, String newVal) {
		for(SymbolTable row : symbolTable.getItems()) {
			if(row.getIdentifier().equals(var)) {
				row.setValue(newVal);
			}
			
		}
	}
	
	private String checkGFTO(ArrayList<String[]> tokensPerLine) {
		//check if there's unreachable code after GTFO keyword
		int index=0;
		for(String[] row : tokensPerLine) {
			if(row[0].matches(Lexeme.GTFO)) {
				if(index+1<tokensPerLine.size()) {
					if(!tokensPerLine.get(index+1)[0].matches(Lexeme.OMG+"|"+Lexeme.OMGWTF+"|"+Lexeme.OIC+"|"+Lexeme.IM_OUTTA_YR)) {
						isDeadCode=true;
						return null;
					}
				}
			}
			index++;
		}
		return "succes";
	}
	
	private String setIHAS(String[] lexList) {		
		if(lexList.length==1 || lexList.length==3 || (lexList.length>3 && !lexList[2].matches(Lexeme.ITZ))) {
			displayResult.setText("Variable assignment error!");
			return null;
		}else if(lexList.length>=2) {
			if(checkIfVarExist(lexList[1])) {
				displayResult.setText("Variable already exists, error!");
				return null;
			}else if(lexList[1].matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-3))) {
				displayResult.setText("Variable is invalid, error!");
				return null;
			}
			else if(lexList.length==2) {
				this.symbolTable.getItems().add(new SymbolTable(lexList[1],""));
				return "success";
			}
		}
		
		//this is for operations eg "I HAS A var ITZ SUM OF 10 AN 5"
		ArrayList<String> operands = new ArrayList<String>();
		String ifOperations = null;
		if(lexList.length>=5) { //expression
			for(int i=3;i<lexList.length;i++) operands.add(lexList[i]);
			String[] operandsArr =  new String[operands.size()];
			for(int i=0;i<operands.size();i++) operandsArr[i] = operands.get(i);
			ifOperations = allOperations(operandsArr);
		}
		//====end of i has with operations=====		
		
		
		if(lexList[3].matches(Lexeme.ALL_LITERALS)) {
			if(lexList.length!=4) {
				displayResult.setText("Variable assignment error!");
				return null;
			}
			lexList[3] = lexList[3].replace("\"", "");
			this.symbolTable.getItems().add(new SymbolTable(lexList[1],lexList[3]));	
			return "Success";
		}else if(ifOperations!=null) {
			ifOperations = ifOperations.replace("\"", "");
			this.symbolTable.getItems().add(new SymbolTable(lexList[1],ifOperations));		
			return "Success";
		}else if(lexList[3].matches(Lexeme.VARIDENT)) { //varident
			boolean isFound = false;
			for(SymbolTable row : symbolTable.getItems()) {
				if(lexList[3].contentEquals(row.getIdentifier())) {
					this.symbolTable.getItems().add(new SymbolTable(lexList[1],row.getValue()));			
					isFound = true;
					break;
				}
			}
			if(!isFound) {
				displayResult.setText(lexList[3]+ " variable not found, error!");
				return null;
			}else return "Success";
		}
		displayResult.setText("I HAS statement error!");
		return null;
	}
	
	private boolean checkInvalidNest(String[] lexList) {
		String[] removeOp = new String[lexList.length-1];
 		for(int i=0;i<lexList.length-1;i++) removeOp[i] = lexList[i+1];

 		String lexListStr = Arrays.toString(removeOp);
		String patternString = "SMOOSH|ALL OF|ANY OF";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(lexListStr);

//		System.out.println(lexListStr);
		while(matcher.find()) return true;
		return false;
	}
	
	private String solveSmooshOperation(String[] lexList) {		
		if(checkInvalidNest(lexList)) return null;
		ArrayList<String> removeAN = new ArrayList<String>();
		String regexOperation = Lexeme.mathOperator+Lexeme.boolOperator.substring(0,Lexeme.boolOperator.length()-1);
		String literalsVar = Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-2)+Lexeme.VARIDENT;

		if(lexList[lexList.length-1].matches("AN")) return null;
		
		for(int i=1;i<lexList.length;i++) { 
			if(lexList[i].matches(Lexeme.SMOOSH)) {
				continue;
			}
			else if(lexList[i].matches(regexOperation)) {
				int numOperation = 0; int numOperand = 0;
				while(numOperation+1!=numOperand) {
					if(lexList[i].matches(Lexeme.NOT)) {
						removeAN.add(lexList[i]);
					}else if(lexList[i].matches(literalsVar) && !lexList[i].matches("AN") && !lexList[i].matches(regexOperation)) {
						removeAN.add(lexList[i]);
						numOperand++;
					}else if(lexList[i].matches(regexOperation)) {
						removeAN.add(lexList[i]);
						numOperation++;
					}else if(lexList[i].matches("AN")) {
						if(i+1<lexList.length && lexList[i+1].matches("AN")) return null;
						removeAN.add(lexList[i]);
					}
					i++;
				}
				i--;
				if(i+1<lexList.length && lexList[i+1].matches(literalsVar) && !lexList[i+1].matches("AN")) return null;
			}else if(lexList[i].matches("\\bAN\\b")) {
				if(i+1<lexList.length && lexList[i+1].matches("AN")) return null;
				if(i==1) return null;
				continue;
			}else {
				if(i+1<lexList.length && lexList[i+1].matches(literalsVar) && !lexList[i+1].matches(regexOperation) && !lexList[i+1].matches("AN")) {
					return null;
				}
				removeAN.add(lexList[i]);
			}
		}
		removeAN.add(0,"VISIBLE");
		String[] lexListNew = new String[removeAN.size()];
		for(int a=0;a<removeAN.size();a++) {
			lexListNew[a] = removeAN.get(a);
		}
		
		String ans = doVISIBLE(lexListNew);
		if(ans!=null) return ans;
		else return null;
	}
	
	private String makeRreassignment(String[] lexList) {
		//check if LHS varident is existing or not		
		if(lexList[0].matches(Lexeme.VARIDENT)) {
			if(getValueVarident(lexList[0])==null) {
				displayResult.setText(lexList[0]+" is not found, error!");
				return null;
			}
		}		
		if(lexList.length<=2) {
			displayResult.setText("Reassignment syntax error!");
			return null;
		}
		
		//check if RHS is expression
		ArrayList<String> operands = new ArrayList<String>();
		String ifOperations = "";
		
		//check if RHS is literals
		String newVal = null;
		
		
		//if literals
		if(lexList[2].matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-3))) {
			if(lexList.length!=3) {
				displayResult.setText("Reassignment error!");
				return null;
			}
			newVal = lexList[2];
		}
		else if(lexList.length>3) { //math,
			for(int i=2;i<lexList.length;i++) operands.add(lexList[i]);
			String[] operandsArr =  new String[operands.size()];
			for(int i=0;i<operands.size();i++) operandsArr[i] = operands.get(i);
			if(operandsArr[0].matches(Lexeme.NOT) && operandsArr.length<2) {
				displayResult.setText("761: Reassignment of expression error!");
				return null;
			}else if(!operandsArr[0].matches(Lexeme.NOT) && operandsArr.length<4) {
				displayResult.setText("Reassignment of expression error!");
				return null;
			}
			ifOperations = allOperations(operandsArr);
		}else if(lexList[2].matches(Lexeme.VARIDENT)) { //check if RHS is varident and exisiting
			if(lexList.length!=3) {
				displayResult.setText("Reassignment error!");
				return null;
			}
			newVal = getValueVarident(lexList[2]);
			if(newVal==null) {
				displayResult.setText(lexList[2] +" is not found, error!");
				return null;
			}else if(lexList[2].contentEquals(lexList[0])) {
				displayResult.setText("Warning due to self-reassignment!");
				return null;
			}
		}
		if(ifOperations==null) {
			displayResult.setText("Reassignment of expression error!");
			return null;
		}else if(ifOperations.length()==0) {}
		else newVal = ifOperations;
		
		//reassigning after checking/solving if literals/varident/expression
		newVal = newVal.replaceAll("\"", "");
		updateVar(lexList[0], newVal);
		return "success";
	}
	
	private void clearTables() {
		lexemeTable.getItems().clear();
		symbolTable.getItems().clear();
	}

	private void storeIT(String answer, String[] operands) {
		String operand = operands[0];
		
		String expOp = Lexeme.mathOperator+Lexeme.boolOperator+Lexeme.concat.substring(0,Lexeme.concat.length()-1);
		if(operand.matches(Lexeme.VISIBLE)) {
			operand = operands[1];
			if(operand.matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-3)+"|"+Lexeme.VARIDENT) && !operand.matches(expOp)) return;
		}else if(operand.matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-3)+"|"+Lexeme.VARIDENT) && !operand.matches(expOp)) return;

		for(SymbolTable row : symbolTable.getItems()) {
			row.setValue(answer);		
			break;
		}
	}
	
	private String allOperations(String[] tokenArrLine) {
		if(tokenArrLine[0].matches(Lexeme.mathOperator.substring(0, Lexeme.mathOperator.length()-1))) {
			try {
				String ans = setArithmeticOperation(tokenArrLine);
				if(ans!=null) { 
					return ans;
				}else {
					clearTables();
					displayResult.setText("473 Syntax Error."); return null;
				}
			}catch(Exception e) {
				clearTables();
				displayResult.setText("475 Syntax Error."); return null;
			}
		}else if(tokenArrLine[0].matches(Lexeme.boolOperator.substring(0, Lexeme.boolOperator.length()-37))) {  //both of, either of, won of, not, all of, any of, mkay
			try {
				String ans = setBooleanOperation(tokenArrLine);
				if(ans!=null) {
					System.out.println("481 Correct syntax: " + ans); 
					return ans;
				}
				else {
					clearTables();
					displayResult.setText("481 Syntax Error."); return null;
				}
			}catch(Exception e) {
				clearTables();
				displayResult.setText("484 Syntax Error."); return null;
			}
		}else if(tokenArrLine[0].matches(Lexeme.boolOperator.substring(67,93))){ //both saem, diffrint
			try {
			String ans = setComparisonOperation(tokenArrLine);
			if(ans!=null) {
				System.out.println("Comparison - Correct syntax: " + ans); 
				return ans;
			}
			else{
					clearTables();
					displayResult.setText("624 Syntax Error."); return null;
			}
			}catch(Exception e) {
				clearTables();
				displayResult.setText("Catch Syntax Error.");	return null;				
			}
		}else if(tokenArrLine[0].matches(Lexeme.SMOOSH)) {
			try {
				String ans = solveSmooshOperation(tokenArrLine);
				if(ans!=null) {
					System.out.println("Smoosh - Correct syntax: ");
					return ans;
				}else {
					clearTables();
					displayResult.setText("Smoosh - Syntax Error."); return null;
				}
			}catch(Exception e) {
				displayResult.setText("Smoosh - Catch Syntax Error."); return null;
			}
		}else if(tokenArrLine[0].matches(Lexeme.VISIBLE)) {
			try {
				if(tokenArrLine[tokenArrLine.length-1].contentEquals("a!")) isNewLine=false;
				String ans = doVISIBLE(tokenArrLine);	
				if(ans!=null) {
					printFormatVisible(ans);					
					return ans;
				}else {
					clearTables();
					displayResult.setText("VISIBLE - Syntax Error."); return null;
				}
			}catch(Exception e) {
				clearTables();
				displayResult.setText("VISIBLE Catch - Syntax Error."); return null;
			}
		
		}
		return null;
	}
	
	private void printFormatVisible(String temp){
		//bonus#1
		String ans = temp.replace("a!","");
		if(isNewLine) displayResult.setText(displayResult.getText()+ans+"\n");
		else displayResult.setText(displayResult.getText()+ans);
		this.isNewLine=true;
	}
	
	private void storeGIMMEH(String variable, String newStr, String oldStr, ArrayList<String[]> continueLine) {
		boolean isSuccessInput=false;
		if(newStr.contains(oldStr)) {
			for(SymbolTable row : symbolTable.getItems()) {
				if(row.getIdentifier().equals(variable)) {
					newStr = newStr.replace(oldStr,"").replaceAll("[\"\n]",""); 
					row.setValue(newStr);
					symbolTable.refresh(); //update instantly the symboltable (tableview on the right)
					isSuccessInput=true;
					displayResult.setEditable(false);
					displayResult.setText(displayResult.getText()+"\n");
					if(displayResult.getText().charAt(0)=='\n') displayResult.setText(displayResult.getText().substring(1)); //printing format only
					doSyntaxAnalysis(continueLine);
					return;
				}
			}
		}
		if(!isSuccessInput) {
			clearTables();
			displayResult.setText("Error input, enter on last line!");
			displayResult.setEditable(false);
		}
	}
	
	private String doGIMMEH(String[] lexList, ArrayList<String[]> continueLine) {
		if(lexList.length==1) {
			displayResult.setText("GIMMEH has no variable, error!");
			return null;
		}else if(lexList.length>2) {
			displayResult.setText("GIMMEH cannot store on more than one variable, error!");
			return  null;
		}
		String variable = lexList[1];
		if(!checkIfVarExist(variable)) {
			displayResult.setText(variable +" does not exist!");
			return null; //lexList[1] contains the varident/variable that user want to store, if not existing then return null (error)
		}
		
		if(displayResult.getLength()!=0 && displayResult.getText().charAt(0)=='\n') displayResult.setText(displayResult.getText().substring(1)); //printing format only
		
    	displayResult.setEditable(true);
    	
    	//old string input from terminal
    	String oldStr = displayResult.getText();    	
    	
    	displayResult.setOnKeyPressed(new EventHandler<KeyEvent>(){
    	   @Override
    	   public void handle(KeyEvent ke){
    	     if(ke.getCode().equals(KeyCode.ENTER)){
    	    	 String newStr = displayResult.getText();
    	    	 storeGIMMEH(variable,newStr,oldStr,continueLine);
    	     }
    	   }
    	});
    	return "success";
	}
	
	private String checkHaiKthxbye(String[] programInput) {
		int len = programInput.length-1;
		if(len==-1) return null;
		programInput[0] = programInput[0].replaceAll("BTW.*","");
		programInput[0] = programInput[0].replaceAll("[\\s\\n]","");
		programInput[len] = programInput[len].replaceAll("BTW.*","");
		programInput[len] = programInput[len].replaceAll("[\\s\\\n]","");
		if(programInput[0].matches("HAI") && programInput[0].length()>3) return null;
		if(programInput[len].matches("KTHXBYE|\\n") && programInput[len].length()>7) return null;
		if(!programInput[0].matches(Lexeme.HAI) || !programInput[len].matches(Lexeme.KTHXBYE)) return null;
		
		//if hai or kthxbye has multiple occurences
		int HAILen = 0; int KTHXBYELen = 0;
		for(int i=0;i<programInput.length;i++) {
			programInput[i] = programInput[i].replaceAll("BTW.*","");
			if(programInput[i].matches(Lexeme.HAI)) HAILen++;
			if(programInput[i].matches(Lexeme.KTHXBYE)) KTHXBYELen++;
		}
		if(HAILen!=1 || KTHXBYELen!=1) return null;
		this.lexemeTable.getItems().add(new Lexeme(programInput[0],Lexeme.CODE_DELIMETER));
		this.lexemeTable.getItems().add(new Lexeme(programInput[len],Lexeme.CODE_DELIMETER));
		return "success";
	}
	
	private String doVISIBLE(String[] lexList) {
		
		if(lexList.length==1) return null; //must contain atleast one operand
		String combinedOp = Lexeme.mathOperator + Lexeme.boolOperator+"\\bAN\\b";
		String combinedVal = Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-3);
		
		ArrayList<String> outputPrint = new ArrayList<String>();
		
		for(int i=1;i<lexList.length;i++) { //1 because VISIBLE keyword is excluded
			//literals only
			if(lexList[i].matches(Lexeme.ALL_LITERALS) && !lexList[i].matches("AN")) {
				outputPrint.add(lexList[i]); 
				continue; //outputPrint list will collect all operands (arity) before it prints/displays to the textarea
			}
			
			//smoosh operation
			else if(lexList[i].matches(Lexeme.SMOOSH)) {
				String smooshArr[] = new String[lexList.length-1];
				for(int a=1;a<lexList.length;a++) smooshArr[a-1] = lexList[a];
				return solveSmooshOperation(smooshArr);
			}
			
			//any of, all of, mkay
			else if(lexList[1].matches(Lexeme.ANY_OF+"|"+Lexeme.ALL_OF)) {
				ArrayList<String> tempStore = new ArrayList<String>();
				if(!lexList[lexList.length-1].matches(Lexeme.MKAY)) return null;//if mkay is not the last item
				while(!lexList[i].matches(Lexeme.MKAY)) {
					tempStore.add(lexList[i]);
					i++;
				}
				tempStore.add(lexList[lexList.length-1]); //insert mkay
				if(tempStore.size()>2) { 
					String[] passToOp = new String[tempStore.size()];
					for(int a=0;a<tempStore.size();a++) passToOp[a] = tempStore.get(a);
					String ans = allOperations(passToOp); //if fail to evaluate the expression, throw syntax error
					if(ans!=null) {
						outputPrint.add(ans);
					}
					else return null;
				}else return null;
			}
			
			
			
			//math,comparison, boolean
			else if(lexList[i].matches(combinedOp)) {
				String regexOperation = Lexeme.mathOperator+Lexeme.boolOperator.substring(0,Lexeme.boolOperator.length()-1);
				String literalsVar = Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-2)+"|"+Lexeme.VARIDENT;
				ArrayList<String> tempStore = new ArrayList<String>();
				
				int numOperation = 0; int numOperand = 0;
				while(numOperation+1!=numOperand) {
					if(lexList[i].matches(Lexeme.NOT)) {
						tempStore.add(lexList[i]);
					}else if(lexList[i].matches(literalsVar) && !lexList[i].matches("\\bAN\\b") && !lexList[i].matches(regexOperation)) {
						tempStore.add(lexList[i]);
						numOperand++;
					}else if(lexList[i].matches(regexOperation)) {
						tempStore.add(lexList[i]);
						numOperation++;
					}else if(lexList[i].matches("\\bAN\\b")) {
						if(i+1<lexList.length && lexList[i+1].matches("AN")) return null;
						tempStore.add(lexList[i]);
					}
					i++;
				}
				i--;	
				if(tempStore.size()>1) { //NOT unary has at least 2 operands
					String[] passToOp = new String[tempStore.size()];
					for(int a=0;a<tempStore.size();a++) passToOp[a] = tempStore.get(a);
					String ans = allOperations(passToOp);
					if(ans!=null) outputPrint.add(ans);
					else return null;
				}else return null;
				continue;
			} //end of expression
			
			
			//variable
			else if(lexList[i].matches(Lexeme.VARIDENT) && !lexList[i].matches("AN")) {
				 if(checkIfVarExist(lexList[i])) {
					 String val = getValueVarident(lexList[i]);
					 if(val.length()>0) {
						 outputPrint.add(val); 
					 }else return null; //if variable has NOOB value (no value assigned)
					 continue;
				 }else return null;
			}
		}
		 
		String finalOutput="";
		if(outputPrint.size()!=0) {
			for(String output : outputPrint) {
				finalOutput = finalOutput + output.replace("\"","");		
			}
			return finalOutput;
		}
		return null;
	}
	
	private int doIFELSE(ArrayList<String[]> tokensProgram,int i) {	
		int isYA_RLY=-1;
		int isNO_WAI=-1;
		int isOIC=-1;
		int increment=0;
		for(int a=i;a<tokensProgram.size();a++) {
			if(tokensProgram.get(a)[0].matches(Lexeme.OIC)) {
				isOIC=a;
				increment= a-i+1;
				if(increment < 6) return -1;
				break;
			}else if(tokensProgram.get(a)[0].matches(Lexeme.YA_RLY)) {
				if(!tokensProgram.get(a-1)[0].matches(Lexeme.O_RLY)) return -1;
				isYA_RLY = a;
			}else if(tokensProgram.get(a)[0].matches(Lexeme.NO_WAI)) isNO_WAI = a;
		}
		if(isYA_RLY==-1 || isNO_WAI==-1 || isOIC==-1) return -1;
		
		
		String ITval= getValueVarident("IT");
		ArrayList<String[]> blockStatement = new ArrayList<String[]>();
		int start=0;
		int end=0;
		if(ITval.matches(Lexeme.TROOF[0]+"|"+"\""+Lexeme.TROOF[0]+"\"")) {
			start=isYA_RLY+1;
			end=isNO_WAI;
		}else { //FAIL, or cannot be typecast to WIN
			start=isNO_WAI+1;
			end=isOIC;
		}
		
		for(int j=start;j<end;j++) blockStatement.add(tokensProgram.get(j));		
		doSyntaxAnalysis(blockStatement);
		
		return increment-1; //-1 if fail, syntax error
	}
	
	private int doSWITCH(ArrayList<String[]> tokensProgram,int i) {
		ArrayList<Integer> OMGlist = new ArrayList<Integer>();
		int isOMGWTF=-1;
		int isOIC=-1;
		int increment=0;

		if(i+1<tokensProgram.size() && !tokensProgram.get(i+1)[0].matches(Lexeme.OMG)) return -1;
		
		for(int a=i;a<tokensProgram.size();a++) {
			if(tokensProgram.get(a)[0].matches(Lexeme.OIC)) {
				isOIC=a;
				increment= a-i+1;
				if(increment < 6) return -1;
				break;
			}else if(tokensProgram.get(a)[0].matches(Lexeme.OMG)) {
				if(tokensProgram.get(a).length==1 || !tokensProgram.get(a)[1].matches(Lexeme.ALL_LITERALS)) return -1;
				if(a!=0 && tokensProgram.get(a-1)[0].matches(Lexeme.OMG)) return -1;
				OMGlist.add(a);
			}else if(tokensProgram.get(a)[0].matches(Lexeme.OMGWTF)) {
				if(tokensProgram.get(a).length>1) return -1;
				if(a!=0 && tokensProgram.get(a-1)[0].matches(Lexeme.OMG)) return -1;
				isOMGWTF = a;
			}
		}
		
		if(isOMGWTF==-1 || OMGlist.size()==0 || isOIC==-1) return -1;
		
		String ITval= getValueVarident("IT");
		ITval = ITval.replace("\"", "");
		if(ITval.length()==0) return -1;
		
		//find on where OMG it satisfies, matches, or equal
		boolean isOMGsatisfied = false;
		ArrayList<String[]> blockStatement = new ArrayList<String[]>();
		
		for(Integer index : OMGlist) {
			String omgVal = tokensProgram.get(index)[1].replace("\"", "");
			if(omgVal.equals(ITval) && !isOMGsatisfied) isOMGsatisfied = true;
			if(isOMGsatisfied) {
				for(int a=index;a<isOIC;a++) {
					if(!tokensProgram.get(a)[0].matches(Lexeme.OMG+"|"+Lexeme.OMGWTF)) {
						blockStatement.add(tokensProgram.get(a));
					}
					if(tokensProgram.get(a)[0].matches(Lexeme.GTFO)) {
						break;
					}
				}
				doSyntaxAnalysis(blockStatement);
				break;
			}
		}
		
		//if there is no OMG satisfied, matches, or equal
		if(!isOMGsatisfied) {
			for(int a=isOMGWTF+1;a<isOIC;a++) blockStatement.add(tokensProgram.get(a));
			doSyntaxAnalysis(blockStatement);
		}
		
		return increment-1;
	}
	
	private String doRemoveComments() { //removes all comments before it goes to syntax analyzer
		String removeComment = this.inputUser.getText();
		Pattern pattern = Pattern.compile("[\\s]*TLDR[ ]*[\\w]+"); //if TLDR has succeeding chars (excluding whitespaces)
		Matcher match = pattern.matcher(removeComment);
		if(match.find()) return null;
	
		Pattern pattern2 = Pattern.compile("[\\w]+[ ]*OBTW.*"); 
		Matcher match2 = pattern2.matcher(removeComment);
		if(match2.find()) return null;
		
		String newRemovedComments="";
		String[] tempLines = removeComment.split("\n");
		for(int i=0;i<tempLines.length;i++) {
			if(tempLines[i].matches("[\\s]*OBTW[\\s\\w]*")) {
				boolean isFoundTLDR = false;
				while(i<tempLines.length) {
					if(tempLines[i].matches("[\\s\\w]*TLDR")) {
						isFoundTLDR = true;
						break;
					}
					i++;
				}
				if(!isFoundTLDR) return null;
			}else newRemovedComments = newRemovedComments +"\n" + tempLines[i];
		}
		return newRemovedComments;
	}
	
	private int doLoop(ArrayList<String[]> tokensProgram, int i) {
		
		String[] loopStartArr = tokensProgram.get(i); //array of where IM IN YR belongs
		int start = i, end = -1, updateVal = 0;
		String literals = Lexeme.NUMBR+"|"+Lexeme.NUMBAR+"|"+Lexeme.TROOF[0]+"|"+Lexeme.TROOF[1];

		if(!loopStartArr[1].matches(Lexeme.VARIDENT)) return -1;
		if(loopStartArr.length<8) return -1; //8 is the minimum length of the array of where IM IN YR array belongs
				
		//check if valid data type to be incremented/decremented (check if var has data type of numbar, numbr, or troof, else error)
		if(checkIfVarExist(loopStartArr[4])) {
			String variable = getValueVarident(loopStartArr[4]);
			if(variable.length()==0 || !variable.matches(literals)) return -1; //must have valid data type (numbr, numbar, troof only)
			if(variable.matches(Lexeme.TROOF[1])) updateVar(loopStartArr[4],"0");
			else if(variable.matches(Lexeme.TROOF[0])) updateVar(loopStartArr[4],"1");
		}
		
		//find where IM OUTTA YR
		int numLoop = 1; //this is for nested loop 
		for(int a=i+1;i<tokensProgram.size();a++) {
			if(tokensProgram.get(a)[0].matches(Lexeme.IM_OUTTA_YR)) numLoop--;
			else if(tokensProgram.get(a)[0].matches(Lexeme.IM_IN_YR)) numLoop++;
			if(numLoop==0) {
				end = a;
				break;
			}
		}
		if(end==-1) return -1;
		
		//loop identifer must be paired/must
		if(tokensProgram.get(end).length!=2 || !loopStartArr[1].contentEquals(tokensProgram.get(end)[1])) return -1;
		
		//increment or decrement based if UPPIN/NERFIN respectively
		if(!tokensProgram.get(start)[2].matches(Lexeme.UPPIN+"|"+Lexeme.NERFIN)) return -1;
		updateVal = loopStartArr[2].matches(Lexeme.UPPIN)? 1:-1;
		
		//getting the blockStatement to be executed
		ArrayList<String[]> blockStatements = new ArrayList<String[]>();
		for(int c=start+1;c<end;c++) blockStatements.add(tokensProgram.get(c));

		

		//getting the condition statement: outputs WIN breakk, FAIL continue
		ArrayList<String> conditionStatement = new ArrayList<String>();
		if(!tokensProgram.get(start)[6].matches(Lexeme.boolOperator.substring(0,Lexeme.boolOperator.length()-1))) return -1; //BOTH SAEM because if: WIN - stop loop, FAIL = continue loop
		for(int b=6;b<loopStartArr.length;b++) { //6 because 6 is the constant value of index in an array of where the condition checking begins
			conditionStatement.add(tokensProgram.get(start)[b]);
		}
	
		int checkerInfiniteLoop = 0; //this stops if it exceeds 1,000 iterations assuming 1,000 is the maximum statck size (ideal) -- to avoid infinite loop
		
		while(true) {	
			//gettinig the expression resulting to: WIN/FAIL
			String[] conditionStatementArr = new String[conditionStatement.size()];
			for(int b=0;b<conditionStatement.size();b++) conditionStatementArr[b] = conditionStatement.get(b);
			String isContinueLoop = allOperations(conditionStatementArr);	
			if(isContinueLoop!=null && isContinueLoop.equals(Lexeme.TROOF[0])) break; //if WINL : break the loop
			
			//if FAIL, execute
			doSyntaxAnalysis(blockStatements);
			
			String varUpdate = getValueVarident(loopStartArr[4]);
			if(varUpdate!=null && varUpdate.matches(Lexeme.NUMBAR)) {
				Float newval = Float.parseFloat(getValueVarident(loopStartArr[4]))+ updateVal;
				updateVar(loopStartArr[4],Float.toString(newval));
			}else if(varUpdate!=null && varUpdate.matches(Lexeme.NUMBR)) {
				int newval = Integer.parseInt(getValueVarident(loopStartArr[4]))+updateVal;
				updateVar(loopStartArr[4],Integer.toString(newval));
			}
			if(checkerInfiniteLoop>=1000) return -2; //ideal scenario (online interpreter only allowed at most 1,000 iterations, else infinite loop error)
			checkerInfiniteLoop++;
		}
		return end;
	}
	
	private ArrayList<String[]> doLexicalAnalysis() { 
		clearTables();
		this.symbolTable.getItems().add(new SymbolTable(Lexeme.IT,""));
		this.displayResult.setText("");

		String removeComment = doRemoveComments();
		if(removeComment==null) return null;
		
		String[] programInputwComments = removeComment.split("\n");
		Pattern regex = Pattern.compile(Lexeme.combineRegex);
		ArrayList<String[]> tokenizedOutput = new ArrayList<String[]>();
		
		
		//removes all comments
		ArrayList<String> programInputList = new ArrayList<String>();
		for(int i=0;i<programInputwComments.length;i++) { //programInput = [[line1],[line2],[line3],..,]	
			if(programInputwComments[i].contains("BTW") ) {
				 if(programInputwComments[i].substring(0,3).equals("BTW")){
					 continue;
				 }else if(programInputwComments[i].length()>3) {
					 programInputList.add(programInputwComments[i].replaceAll("BTW.*",""));
				 }
			}
			else {
				if(!programInputwComments[i].matches("^[\\s]+$") && programInputwComments[i].length()>0) programInputList.add(programInputwComments[i]);
			}
		}
//		System.out.println(Arrays.toString(programInputList.toArray()));
		
		String[] programInput = new String[programInputList.size()];
		for(int a=0;a<programInputList.size();a++) programInput[a] = programInputList.get(a);
//		System.out.println("894: " + Arrays.toString(programInput));
		
		if(checkHaiKthxbye(programInput)==null) {
			displayResult.setText("HAI/KTHXBYE has error!");
			return null;
		}
		
		for(int i=0;i<programInput.length;i++) {
			//check if HAI/KTHXBYE are valid		
			Matcher regexMatcher = regex.matcher(programInput[i]);	
			ArrayList<String> tokenizedLine = new ArrayList<String>();
			while (regexMatcher.find()) {
				String match = regexMatcher.group();
				tokenizedLine.add(match);
			}
			String[] arrResult = new String[tokenizedLine.size()];
			for(int a=0;a<tokenizedLine.size();a++) {
				arrResult[a] = tokenizedLine.get(a);	
			}
			if(arrResult.length!=0) tokenizedOutput.add(arrResult);
		}
		if(checkGFTO(tokenizedOutput)==null) return null; 
		
		
//		for(String[] arr : tokenizedOutput) {
//			System.out.print(Arrays.toString(arr)+ " ");
//		}	

		//--------------------------this: lexemes already tokenized here in this line-------------------------
		
		for(int k=0;k<tokenizedOutput.size();k++) { //tokenizedOutput = [[word1,word2],[word1,word2],[word1,word2],..,]
			String[] arrLexeme = tokenizedOutput.get(k);
			String classification;
			
			for(int a=0;a<arrLexeme.length;a++) {
				if(arrLexeme[a]==null) continue;
				classification = Lexeme.findLexemeType(arrLexeme[a]);
				if(classification!=null) {
					if(arrLexeme[a].matches(Lexeme.YARN) && !checkIfLexemeExist(arrLexeme[a])) {
						String removeQuote = arrLexeme[a].replace(Lexeme.QUOTE, "");
						if(!checkIfLexemeExist(Lexeme.QUOTE)) this.lexemeTable.getItems().add(new Lexeme(Lexeme.QUOTE,Lexeme.STRING_DELIMETER));
						this.lexemeTable.getItems().add(new Lexeme(removeQuote,classification));
					}
					else {
						if(!checkIfLexemeExist(arrLexeme[a])) this.lexemeTable.getItems().add(new Lexeme(arrLexeme[a],classification));
					}
				}else {
					System.out.println("615 Lexeme not found");
					return null;
				}
			}
			
		} //for loop k
		
		return tokenizedOutput;
	} //end function
	
	private void doSyntaxAnalysis(ArrayList<String[]> tokensPerLine) {
		
		for(int i=0;i<tokensPerLine.size();i++) {
			String[] tokenArrLine = tokensPerLine.get(i);
			
			//syntax for all operations (math, boolean, comparison, relational) and its result is not stored to a variable (then it must be stored to IT)
			String storeIt = allOperations(tokenArrLine);
			if(storeIt!=null) storeIT(storeIt,tokenArrLine);
			
			//syntax for I HAS
			if(tokenArrLine[0].matches(Lexeme.HAI+"|"+Lexeme.KTHXBYE)) {continue;}
			else if(Arrays.toString(tokenArrLine).contains("I HAS A")) {
				try {
					String ans = setIHAS(tokenArrLine);
					if(ans!=null) {
					}else {
						clearTables();
						return;
					}
				}catch(Exception e) {
					clearTables();
					displayResult.setText("I HAS-Syntax Error."); return;
				}
				
			}else if(tokenArrLine[0].matches(Lexeme.VARIDENT) && tokenArrLine.length>1 && tokenArrLine[1].matches(Lexeme.R)) {
				try {
					String ans = makeRreassignment(tokenArrLine);
					if(ans!=null) {}
					else {
						clearTables();
						return;
					}
				}catch(Exception e) {
					clearTables();
					displayResult.setText("Reassignment error."); return;
				}
			}else if(tokenArrLine[0].matches(Lexeme.GIMMEH)) {
				if(displayResult.getLength()!=0 && displayResult.getText().charAt(0)=='\n') displayResult.setText(displayResult.getText().substring(1)); //printing format only (nothing to do with syntax analyzer)
				try{
					ArrayList<String[]> continueAfterInput = new ArrayList<String[]>();
					for(int a=i+1;a<tokensPerLine.size();a++) continueAfterInput.add(tokensPerLine.get(a));
					
					String ans = doGIMMEH(tokenArrLine,continueAfterInput);
					if(ans==null) {
						clearTables();
						return;						
					}
				}catch(Exception e) {
					clearTables();
					displayResult.setText("GIMMEH Syntax Error."); return;
				}
				break;
			}else if(tokenArrLine[0].matches(Lexeme.O_RLY)) {
				try {
					int ans = doIFELSE(tokensPerLine,i);
					if(ans!=-1) {
						i+=ans;
					}
					else {
						clearTables();
						displayResult.setText("IF-ELSE Syntax Error"); return;
					}
				}catch(Exception e) {
					clearTables();
					displayResult.setText("IF ELSE Syntax Error."); return;	
				}
			}else if(tokenArrLine[0].matches(Lexeme.GTFO)) {}
			else if(tokenArrLine[0].matches(Lexeme.WTF)) {
				try {
					int ans = doSWITCH(tokensPerLine,i);
					if(ans!=-1) {
						i+=ans;
					}else {
						clearTables();
						displayResult.setText("Switch Case Syntax Error"); return;
					}
				}catch(Exception e) {
					clearTables();
					displayResult.setText("Switch Case Syntax Error."); return;
				}
			}else if(storeIt!=null) {	
			}else if(tokenArrLine[0].matches(Lexeme.IM_IN_YR)) {
				try {
					int ans = doLoop(tokensPerLine,i);
					if(ans!=-1 && ans!=-2) {
						i=ans;continue;
					}else if(ans==-2) {
						displayResult.setText("Infinite loop encountered, error!");
						clearTables();
						return;
					}else {
						clearTables();
						displayResult.setText("Loop Syntax Error."); return;
					}
				}catch(Exception e) {
					clearTables();
					displayResult.setText("Loop Syntax Error"); return;
				}
			}else {
				clearTables();
				displayResult.setText("487 Syntax Error.");
				return;				
			}
		}

	}

	private void btnExecuteHandle() { //get and process the code input by user from texarea named "inputUser"
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e)
            { 
//            	try {
            		if(inputUser.getText().replaceAll("[\\s]*","").length()>0) {
                		ArrayList<String[]> tokensPerLine = doLexicalAnalysis();
                		if(tokensPerLine!=null) doSyntaxAnalysis(tokensPerLine);
                		else if(tokensPerLine==null && isDeadCode) {
                			clearTables();
                			displayResult.setText("Dead code found after GTFO, error!");
                			isDeadCode=false;
                		}
                		else {
                			clearTables();
                			displayResult.setText(displayResult.getText()+"\n"+"499 Syntax Error.");
                		}
            		}
//            	}catch(Exception e1) {
//            		clearTables();
//            		displayResult.setText(displayResult.getText()+"\n"+"501 Syntax Error.");
//            	}
            	
            }
		};
		btnExecute.setOnAction(event); 
		
	}
} 

//all testcases passed


//BONUS DONE:
//	1.) a!
//	2.) SMOOSH
//	3.) loop (without nesting)
//  4.) loop (with nesting)
//	5.) typecast in arithmetic operation, "124" to 124
//  6.) typecast from numbr to numba,   2.0 to 2	(no specs related to this, we follow the rule in online interpreter) and they are equal/WIN

// 7.) deadcode in switch, after gtfo



