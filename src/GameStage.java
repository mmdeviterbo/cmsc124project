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

	VBox paneLexeme; //lexeme table
	VBox paneSymbolTable; //Symbol Table
	
	TextArea inputUser; VBox paneUser; Button btnSelectFile; FileChooser fileChooser;
	
	Text displayLexeme,displaySymbolTable;String errorMessage;
	TextArea displayResult; ScrollPane scrollResult; VBox paneResult; 
	boolean isNewLine;
	
	Button btnExecute; //button to execute the program
	
    TableView<Lexeme> lexemeTable; TableColumn<Lexeme, String> columnLexeme; TableColumn<Lexeme, String> columnClassification;
    TableView<SymbolTable> symbolTable; TableColumn<SymbolTable, String> columnIdentifier; TableColumn<SymbolTable, String> columnValue;

	public static final int WINDOW_HEIGHT = 1070, WINDOW_WIDTH= 1890;

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
            			inputUser.setText("The files inserted is not a LOLCODE program!");
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
		this.isNewLine=true;
		this.errorMessage="";
		inputUser.setPrefWidth(GameStage.WINDOW_WIDTH/3);
		inputUser.setWrapText(true);
		
		//sample input for test only
		inputUser.setText("HAI\n");
		
		inputUser.setText(inputUser.getText() + 
				  "IT R 18\n"
				  + "WTF?\n"
				  + "OMG 1\n"
				  + "   VISIBLE \"I'm the only oneeeee\"\n"
				  + "   GTFO\nOMG 3\n"
				  + "   VISIBLE \"third time's a charm\"\n"
				  + "OMG 5\n"
				  + "   VISIBLE \"no one wants a five\"\n"
				  + "   GTFO\n"
				  + "OMGWTF\n"
				  + "  VISIBLE \"ano na\"\n"
				  + "  VISIBLE IT\n"
				  + "OIC"
				);
		
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
				if(rowData.getValue().length()==0) {
					this.errorMessage = variable + " has NOOB value, error!";
					return null;
				}
				return rowData.getValue();
			}
		}
		this.errorMessage=variable + " variable not found, error!";
		return null;
	}
	
	private String solveArithmeticOperation(ArrayList<String> stackOperation, int index) {
		if(stackOperation.size()<3) return null;
				
		String tempA = stackOperation.get(index).replace("\"",""); String tempB = stackOperation.get(index+1).replace("\"","");
		boolean regexNUMBR_A = tempA.matches(Lexeme.NUMBR); boolean regexNUMBAR_A = tempA.matches(Lexeme.NUMBAR);
		boolean regexNUMBR_B = tempB.matches(Lexeme.NUMBR); boolean regexNUMBAR_B = tempB.matches(Lexeme.NUMBAR);
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
				else if(regexQUO_OF) {
					String divisor = String.valueOf(A);
					if(divisor.matches("0+|0+.0+")) {
						this.errorMessage = "Division by zero, error!";
						return null;
					}
					ans = B/A;
				}
				else if(regexMOD_OF) ans = B%A;
				else if(regexBIGGR_OF) ans = (B>A)? B:A;
				else if(regexSMALLR_OF) ans = (A<B)? A:B;
				stackOperation.remove(index);
				
				if(isFloat==true) stackOperation.add(index,Float.toString(ans));
				else if(isFloat==false) stackOperation.add(index,Integer.toString((int)ans));
				solveArithmeticOperation(stackOperation,0);	
			} else solveArithmeticOperation(stackOperation,index+1);
			
		}else {
			if(index+3>=stackOperation.size()) {	
				return null; 
			}
			solveArithmeticOperation(stackOperation,index+1);
		}
		if(stackOperation.size()==1) return stackOperation.get(0);
		else if(stackOperation.size()>1) {
			this.errorMessage = "Arithmetic expression has invalid operands, error!";
			return null;
		}
		return null;
	}

	private String setArithmeticOperation(String[] lexList) {		
		ArrayList<String> stackOperation = new ArrayList<String>();
		String regexNum = Lexeme.NUMBR+"|"+Lexeme.NUMBAR+"|"+"\""+Lexeme.NUMBR+"\"" +"|"+ "\""+Lexeme.NUMBAR+"\"";
		
		if(lexList.length<4) { //minimum has to be 4: SUM OF 1 AN 1
			this.errorMessage = "Arithmetic expression has missing operands, error --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(lexList[lexList.length-1].matches(Lexeme.keywordsNoLitVar)) {
			this.errorMessage= "Invalid " + lexList[lexList.length-1] +" in the end statement, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(Arrays.deepToString(lexList).contains(Lexeme.MKAY)) {
			this.errorMessage= "Arithmetic expression has found MKAY keyword, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(checkInvalidNest(lexList)) return null;
		else {
			for(int i=0;i<lexList.length-1;i++) {
				if(lexList[i].matches("\\bAN\\b")) {
					if(i+1==lexList.length) {
						this.errorMessage= "Arithmetic expression has AN in the end statement, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}else if(lexList[i+1].matches("\\bAN\\b")) {
						this.errorMessage= "Arithmetic expression has invalid AN AN operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
				}else if(lexList[i].matches(regexNum+"|"+Lexeme.VARIDENT) && !lexList[i].matches(Lexeme.mathOperator+"\\bAN\\b")) {
					if(lexList[i+1].matches(regexNum+"|"+Lexeme.VARIDENT) && !lexList[i+1].matches(Lexeme.mathOperator+"\\bAN\\b")) {
						this.errorMessage= "Arithmetic expression has invalid operand, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
				}
			}
		}
		
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
				if(i+1!=lexList.length && lexList[i+1].matches(regexNum)==true) {
					this.errorMessage = "Arithmetic expression has invalid operands, error --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
			}else if(lexList[i].matches("\\bAN\\b")){
				if(i+1<lexList.length) {
					if(lexList[i+1].matches("\\bAN\\b")) {
						this.errorMessage = "Invalid AN AN operands, error --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
				}else if(i+1==lexList.length) {
					this.errorMessage = "Invalid AN in the end of arithmetic expression, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
			}else if(lexList[i].matches(Lexeme.VARIDENT) && !lexList[i].matches("\\bAN\\b") && !lexList[i].matches(Lexeme.mathOperator+Lexeme.boolOperator)) { 
				if(i+1<lexList.length && lexList[i+1].matches(Lexeme.VARIDENT) && !lexList[i+1].matches("\\bAN\\b") && !lexList[i+1].matches(Lexeme.mathOperator)) { 
					this.errorMessage = "Invalid varidents in arithmetic expression, error --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
				String valueVar = getValueVarident(lexList[i]);
				if(valueVar!=null) stackOperation.add(0,valueVar);
				else return null;
			}else {
				this.errorMessage = "Invalid operand, "+ lexList[i] + ", in arithmetic expression, error --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}
		}
		String answer = solveArithmeticOperation(stackOperation,0);
		return answer;	

	}

	private String solveBooleanOperation(ArrayList<String> stackOperation, int index) {
		if(stackOperation.size()==1) return stackOperation.get(0);
		
		String tempA,tempB;
		if(stackOperation.size()==1) return stackOperation.get(0); //final answer then return the value
		else {
			tempA = stackOperation.get(index);
			tempB = stackOperation.get(index+1); 
		}
		String ans="";
		if(stackOperation.size()==2 && (stackOperation.get(index+1)).matches(Lexeme.NOT)) { //not operator - need one operand
			if(!stackOperation.get(index).matches("\\b"+Lexeme.TROOF[0]+"\\b|\\b"+Lexeme.TROOF[1]+"\\b")) return null;
			ans = stackOperation.remove(index).matches("\\b"+Lexeme.TROOF[0]+"\\b")? Lexeme.TROOF[1] : Lexeme.TROOF[0];
			stackOperation.remove(index);
			stackOperation.add(index,ans);
		}else if(stackOperation.size()>2) { //may valid expression
			String A,B;
			if(tempB.matches(Lexeme.NOT)) {
				if(!tempA.matches("\\b"+Lexeme.TROOF[0]+"\\b|\\b"+Lexeme.TROOF[1]+"\\b")) return null;
				A = stackOperation.remove(index).matches("\\b"+Lexeme.TROOF[0]+"\\b")? Lexeme.TROOF[1]: Lexeme.TROOF[0];
				stackOperation.remove(index);
				stackOperation.add(index,A);
				solveBooleanOperation(stackOperation,0);	
			}else if(tempB.matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7))){
				boolean Atype= tempA.matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7));
				boolean Btype = tempB.matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7));
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
				}else {
					if(index+3>=stackOperation.size()) {	
						return null; 
					}
					solveBooleanOperation(stackOperation,index+1);
				
				}
			}
		}
		if(stackOperation.size()==1) return stackOperation.get(0);
		else if(stackOperation.size()>1) {
			this.errorMessage = "Boolean expression has invalid operands, error!";
			return null;
		}
		return null;
	}
	
	private String setBooleanOperationArity(String[] lexList) {	
		String literals = Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-6)+Lexeme.VARIDENT;
		
		if(checkInvalidNest(lexList)) return null;
		else if(!lexList[lexList.length-1].matches(Lexeme.MKAY)) {
			this.errorMessage = "MKAY is not found, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}
		
		//error detection
		for(int i=0;i<lexList.length;i++) {
			if(i+1<lexList.length) {
				if(lexList[i].matches(literals) && lexList[i+1].matches(literals) && !lexList[i].matches("\\bAN\\b") && !lexList[i+1].matches("\\bAN\\b")&& !lexList[i].matches(Lexeme.keywordsNoLitVar) && !lexList[i+1].matches(Lexeme.keywordsNoLitVar)) {
					this.errorMessage = "Invalid " + lexList[i] + " " + lexList[i+1]+ " operands -->" + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
					return null;
				}else if(lexList[i].matches("\\bAN\\b") && lexList[i+1].matches("\\bAN\\b")) {
					this.errorMessage = "Expression has invalid AN AN operands -->" + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
					return null;
				}				
			}
		}
		
		String[] tempArr = new String[lexList.length-1]; //without mkay at the end
		for(int i=0;i<lexList.length-1;i++) tempArr[i] = lexList[i];
		
		String ans = solveSmooshOperation(tempArr);
		if(lexList[0].matches(Lexeme.ANY_OF) && ans.contains("WIN")) return Lexeme.TROOF[0];
		else if(lexList[0].matches(Lexeme.ANY_OF) && !ans.contains("WIN")) return Lexeme.TROOF[1];
		else if(lexList[0].matches(Lexeme.ALL_OF) && !ans.contains("FAIL")) return Lexeme.TROOF[0];
		else if(lexList[0].matches(Lexeme.ALL_OF) && ans.contains("FAIL")) return Lexeme.TROOF[1];
		return null;
	}
	
	private String setBooleanOperation(String[] lexList){
		ArrayList<String> stackOperation = new ArrayList<String>();
		if(lexList.length==1) {
			this.errorMessage = lexList[0] + " has missing operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(lexList[lexList.length-1].matches(Lexeme.keywordsNoLitVar) && !lexList[lexList.length-1].matches(Lexeme.MKAY)) {
			this.errorMessage= "Invalid " + lexList[lexList.length-1] +" in the end statement, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(lexList[0].matches(Lexeme.ALL_OF+"|"+Lexeme.ANY_OF)) return setBooleanOperationArity(lexList);
		
		//boolean expression may contain any of the other expressions
		String regexLiteral= Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7);
		String[] validExpressions = (Lexeme.mathOperator+Lexeme.boolOperator).split("\\|");
		for(int i=0;i<lexList.length;i++) {
			boolean isOperator=false;
			for(int a=0;a<validExpressions.length-1;a++) {
				String actualOperator = validExpressions[a].replace("\\b","");
				if(lexList[i].matches(Lexeme.ANY_OF+"|"+Lexeme.ALL_OF+"|"+Lexeme.MKAY)) {
					if(lexList[i].matches(Lexeme.MKAY)) this.errorMessage="Invalid MKAY in non-arity expression, error!";
					else if(lexList[i].matches(Lexeme.ALL_OF)) this.errorMessage="Invalid nesting of ALL OF, error!";
					else if(lexList[i].matches(Lexeme.ANY_OF)) this.errorMessage="IInvalid nesting of ANY OF, error!";
					return null;	
				}
				if(actualOperator.matches(lexList[i])) {
					stackOperation.add(0,actualOperator);
					isOperator=true;	
					break;
				}
			}
			if(isOperator) isOperator=false;			
			else if(lexList[i].matches(regexLiteral)) {
				stackOperation.add(0,lexList[i]);
				if(i+1<lexList.length && lexList[i+1].matches(regexLiteral)) {
					this.errorMessage="No AN between literals, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
			}else if(lexList[i].matches("\\bAN\\b")) { //if 'AN AN' exists
				if(i+1<lexList.length) if(lexList[i+1].matches("\\bAN\\b")) {
					this.errorMessage="No operand between AN AN, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", ""); 
					return null;
				}
				if(i+1==lexList.length) {
					this.errorMessage="Invalid AN in the end of statement, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", ""); 
					return null;
				}
					
			}else if(lexList[i].matches(Lexeme.VARIDENT)) { //check if variable is exist in symbol table and if type is troof type
				String value = getValueVarident(lexList[i]);
				if(value!=null) stackOperation.add(0,value);
				else return null;
				
			}else {
				this.errorMessage="Syntax error!";
				return null;
			}
		}
		if(stackOperation.size()<=1) {
			this.errorMessage="Kindly check the expression, error!";
			return null;
		}
		String ans = solveBooleanOperation(stackOperation,0); 
		return ans;
	}
	
	@SuppressWarnings("unused")
	private String setComparisonOperation(String[] lexList) {		
		String regexNum = Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7);
		String regexMath = Lexeme.boolOperator + Lexeme.mathOperator.substring(0,Lexeme.mathOperator.length()-1);
		String mathQuote = "\""+Lexeme.NUMBR +"\"|\"" +Lexeme.NUMBAR_LITERAL + "\"";
		
		if(lexList.length<4) { //comparison expression must has at least 4 operands-operator e.g DIFFRINT 1 AN 1
			this.errorMessage= "Comparison expression has missing operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(lexList[lexList.length-1].matches(Lexeme.keywordsNoLitVar)) {
			this.errorMessage= "Invalid " + lexList[lexList.length-1] +" in the end statement, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}
		else if(Arrays.deepToString(lexList).contains(Lexeme.MKAY)) {
			this.errorMessage= "Comparison expression has found MKAY keyword, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(checkInvalidNest(lexList)) return null;
		else {
			for(int i=0;i<lexList.length-1;i++) {
				if(lexList[i].matches("\\bAN\\b")) {
					if(i+1==lexList.length) {
						this.errorMessage= "Comparison expression has AN in the end statement, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}else if(lexList[i+1].matches("\\bAN\\b")) {
						this.errorMessage= "Comparison expression has invalid AN AN operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
				}else if(lexList[i].matches(regexNum+"|"+mathQuote+"|"+Lexeme.VARIDENT) && !lexList[i].matches(regexMath+"|\\bAN\\b")) {
					if(lexList[i+1].matches(regexNum+"|"+mathQuote+"|"+Lexeme.VARIDENT) && !lexList[i+1].matches(regexMath+"|\\bAN\\b")) {
						this.errorMessage= "Comparison expression has invalid operand, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
				}
			}
		}
		String isRelationOp = null; //-1 means false, 1 means biggr of, 2 means smallr

		ArrayList<String> operandA = new ArrayList<String>();
		ArrayList<String> operandB = new ArrayList<String>();		
		
		int i=1;
		for(i=1;i<lexList.length;i++) { //getting operandA
			if(lexList[i].matches(regexNum+"|"+mathQuote+"|"+Lexeme.VARIDENT) && !lexList[i].matches(regexMath+"|\\bAN\\b")) {
				if(lexList[i].matches(regexNum+"|"+mathQuote)) { //literal operand
					operandA.add(lexList[i]);
				}else if(lexList[i].matches(Lexeme.VARIDENT) && !lexList[i].matches(Lexeme.keywordsNoLitVar)) { //varident operand
					String temp = getValueVarident(lexList[i]);
					if(temp==null) return null;
					lexList[i] = temp;
					operandA.add(lexList[i]);
				}
				if(i==1) {
					if(!lexList[i+1].matches("\\bAN\\b")) {
						this.errorMessage= "Comparison expression has missing AN operand, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
					i+=2;
					break;
				}else if(i+2<lexList.length && lexList[i+1].matches("\\bAN\\b") && lexList[i+2].matches(regexNum+"|"+mathQuote+"|"+Lexeme.VARIDENT)) {
					if(!lexList[i+2].matches(Lexeme.keywordsNoLitVar)) {
						operandA.add(lexList[i+1]);
						if(lexList[i+2].matches(Lexeme.NUMBAR) && lexList[i+2].matches("\\b[0-9]+[.]0+\\b")) {
							lexList[i+2] = lexList[i].split("\\.")[0];
						}else if(lexList[i+2].matches(Lexeme.VARIDENT) && !lexList[i+2].matches(regexNum+"|"+mathQuote)) {
							String temp = getValueVarident(lexList[i+2]);
							if(temp==null) return null;
							lexList[i+2] = temp;
						}
						operandA.add(lexList[i+2]);i+=4; break;
					}
				}
			}else if(lexList[i].matches(regexMath+"|\\bAN\\b")) {
				operandA.add(lexList[i]);
			}else {
				this.errorMessage= "Comparison expression has invalid,"+ lexList[i] +" operand, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}
		}
		
		for(int j=i;j<lexList.length;j++) { //getting operandB
			if(lexList[j].matches(regexNum+"|"+ mathQuote + "|"+ Lexeme.VARIDENT) && !lexList[j].matches(regexMath+"|\\bAN\\b")) {
				if(lexList[j].matches(regexNum+"|"+mathQuote)) {
					operandB.add(lexList[j]);
					if(j+1!=lexList.length && i==j) {
						System.out.println(lexList[j]);
						this.errorMessage= "Comparison expression has excess operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
				}else if(lexList[j].matches(Lexeme.VARIDENT)) {
					String temp = getValueVarident(lexList[j]);
					if(temp==null) return null;
					lexList[j] = temp;
					operandB.add(lexList[j]);
				}else if(j+2<lexList.length && lexList[j+1].matches("\\bAN\\b") && lexList[j+2].matches(regexNum+"|"+Lexeme.VARIDENT)) {
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
			}else if(lexList[j].matches(regexMath+"|\\bAN\\b")) operandB.add(lexList[j]);
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
		
		if(valueA.matches("[0-9]+[.]0+")) valueA = valueA.split("\\.")[0]; //if decimal contains 0's, then it will be then typecast to integer
		if(valueB.matches("[0-9]+[.]0+")) valueB = valueB.split("\\.")[0]; //if decimal contains 0'sthen it will be then typecast to integer
		
					
		if(lexList[0].matches(Lexeme.DIFFRINT) && !valueA.contentEquals(valueB)) return Lexeme.TROOF[0];
		else if(lexList[0].matches(Lexeme.DIFFRINT) && valueA.contentEquals(valueB)) return Lexeme.TROOF[1];
		if(lexList[0].matches(Lexeme.BOTH_SAEM) && valueA.contentEquals(valueB)) return Lexeme.TROOF[0];
		else if(lexList[0].matches(Lexeme.BOTH_SAEM) && !valueA.contentEquals(valueB)) return Lexeme.TROOF[1];			

		return null;
	}
	
	private boolean checkIfVarExist(String lex) {
		for(SymbolTable row : symbolTable.getItems()) {
			if(row.getIdentifier().equals(lex)) {
				return true;
			}
		}
		this.errorMessage=lex + " variable is not found, error!";
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
	
	private String findErrorIHAS(String[] lexList) {
		// [I HAS A, num, ITZ, 0]
		//    0       1    2   3
		if(lexList.length==1) {
			this.errorMessage = "Variable declaration contains no variable, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(lexList.length>=2) {
			if(checkIfVarExist(lexList[1])) { //if already declared
				this.errorMessage = lexList[1] + " variable already exists, error!";
				return null;
			}else this.errorMessage = "";
			if(lexList.length>2 && !lexList[2].matches(Lexeme.ITZ)) {
				this.errorMessage = "Variable declaration has no ITZ for initialization,  error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}else if(lexList.length==3 && lexList[2].matches(Lexeme.ITZ)) {
				this.errorMessage = "Variable declaration has no value after ITZ,  error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}else if(lexList.length==4 && lexList[3].matches(Lexeme.keywordsNoLitVar)) {
				this.errorMessage = "Value in variable declaration has invalid operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;	
			}
			if(lexList[1].matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7))) {
				this.errorMessage = "Variable declaration must be stored to a variable and not to a literal,  error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}else if(lexList[1].matches(Lexeme.keywordsNoLitVar)) {
				this.errorMessage = "Variable declaration must be stored to a variable and not to a keyword,  error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}
		}
	
		return "success";
	}
	
	private String setIHAS(String[] lexList) {		
		if(findErrorIHAS(lexList)==null) return null;
	
		//uninitialized variable
		if(lexList.length==2) {
			this.symbolTable.getItems().add(new SymbolTable(lexList[1],""));
			return "success";
		}
		
		
		//this is for expression eg "I HAS A var ITZ SUM OF 10 AN 5"
		ArrayList<String> operands = new ArrayList<String>();
		String ifOperations = null;
		if(lexList.length>=5) { //expression
			for(int i=3;i<lexList.length;i++) operands.add(lexList[i]);
			String[] operandsArr =  new String[operands.size()];
			for(int i=0;i<operands.size();i++) operandsArr[i] = operands.get(i);
			ifOperations = allOperations(operandsArr);
		}
		
		//====end of i has with expression=====		
		// [I HAS A, num, ITZ, 0]
		//    0       1    2   3
		
		//checks if valus is literal, value, expression
		if(lexList.length>3 && lexList[3].matches(Lexeme.ALL_LITERALS)) { //literals
			if(lexList.length!=4) {
				this.errorMessage = "Variable declaration has excess operands! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}
			lexList[3] = lexList[3].replace("\"", "");
			this.symbolTable.getItems().add(new SymbolTable(lexList[1],lexList[3]));	
			return "Success";
		}else if(ifOperations!=null) { //expression
			ifOperations = ifOperations.replace("\"", "");
			this.symbolTable.getItems().add(new SymbolTable(lexList[1],ifOperations));		
			return "Success";
		}else if(lexList.length>3 && lexList[3].matches(Lexeme.VARIDENT)) { //varident
			boolean isFound = checkIfVarExist(lexList[3]);
			if(!isFound) return null;
			else if(lexList.length!=4) {
				this.errorMessage = "Variable declaration has excess operands! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}else if(isFound) {
				String newValue = getValueVarident(lexList[3]);
				if(newValue==null) return null;
				else {
					this.symbolTable.getItems().add(new SymbolTable(lexList[1],newValue));	
					return "success";
				}
			}else return null; //if not found
		}
		return null;
	}
	
	private boolean checkInvalidNest(String[] lexList) {
		
		String[] removeOp = new String[lexList.length-1];
 		for(int i=0;i<lexList.length-1;i++) removeOp[i] = lexList[i+1];

 		String lexListStr = Arrays.toString(removeOp);
		String patternString = Lexeme.SMOOSH+"|"+Lexeme.ANY_OF+"|"+Lexeme.ALL_OF;
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(lexListStr);
		
		while(matcher.find()) {
			this.errorMessage = "Invalid nesting in expression! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return true;
		}
		return false;
	}
	
	private String solveSmooshOperation(String[] lexList) {
		if(checkInvalidNest(lexList)) return null;
		ArrayList<String> removeAN = new ArrayList<String>();
		String regexOperation = Lexeme.mathOperator+Lexeme.boolOperator.substring(0,Lexeme.boolOperator.length()-1);
		String literalsVar = Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-6)+Lexeme.VARIDENT;

		
		if(lexList[lexList.length-1].matches("\\bAN\\b")) {
			this.errorMessage = "Expression has missing operand, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}
		
		if(lexList.length<4) { //comparison expression must has at least 4 operands-operator e.g DIFFRINT 1 AN 1
			this.errorMessage= "Expression has missing operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(Arrays.deepToString(lexList).contains("MKAY")) {
			this.errorMessage= "Expression has found MKAY keyword, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(checkInvalidNest(lexList)) return null;
		else {
			for(int i=1;i<lexList.length-1;i++) {
				if(lexList[i].matches("\\bAN\\b")) {
					if(i==1) {
						this.errorMessage= "Expression has AN in start of the statement, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
					if(i+1==lexList.length) {
						this.errorMessage= "Expression has AN in the end statement, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}else if(lexList[i+1].matches("\\bAN\\b")) {
						System.out.println(78222);
						this.errorMessage= "Expression has invalid AN AN operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
				}else if(lexList[i].matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-6)+Lexeme.VARIDENT) && !lexList[i].matches(Lexeme.keywordsNoLitVar+"|\\bAN\\b")) {
					if(lexList[i+1].matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-6)+Lexeme.VARIDENT) && !lexList[i+1].matches(Lexeme.keywordsNoLitVar+"|\\bAN\\b")) {
						this.errorMessage= "Expression has invalid operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
				}
			}
		}
		
		for(int i=1;i<lexList.length;i++) { 
			if(lexList[i].matches(Lexeme.SMOOSH)) continue;
			else if(lexList[i].matches(regexOperation)) {
				int numOperation = 0; int numOperand = 0;
				while(numOperation+1!=numOperand) {
					if(lexList[i].matches(Lexeme.NOT)) {
						removeAN.add(lexList[i]);
					}else if(lexList[i].matches("\\bAN\\b")) {
						removeAN.add(lexList[i]);
					}else if(lexList[i].matches(regexOperation)) {
						removeAN.add(lexList[i]);
						numOperation++;
					}else if(lexList[i].matches(literalsVar) && !lexList[i].matches("\\bAN\\b") && !lexList[i].matches(regexOperation)) {
						removeAN.add(lexList[i]);
						numOperand++;
					}
					i++;
				}
				i--;
				if(i+1<lexList.length && lexList[i+1].matches(literalsVar) && !lexList[i+1].matches("\\bAN\\b")) {
					this.errorMessage = "AN is missing, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
			}else if(lexList[i].matches("\\bAN\\b")) {
				if(i+1<lexList.length && lexList[i+1].matches("\\bAN\\b")) {
					this.errorMessage = "No operand in between AN AN, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
				if(i==1) {
					this.errorMessage = "Invalid AN in expression --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
				continue;
			}else {
				if(i+1<lexList.length && lexList[i+1].matches(literalsVar) && !lexList[i+1].matches(regexOperation) && !lexList[i+1].matches("\\bAN\\b")) {
					this.errorMessage = "Invalid expression! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
				removeAN.add(lexList[i]);
			}
		}
		removeAN.add(0,"VISIBLE");
		String[] lexListNew = new String[removeAN.size()];
		for(int a=0;a<removeAN.size();a++) lexListNew[a] = removeAN.get(a);
		
		String ans = doVISIBLE(lexListNew);
		if(ans!=null) return ans;
		else return null;
	}
	
	private String makeRreassignment(String[] lexList) {
		//check if LHS varident is existing or not
		
		if(lexList[0].matches(Lexeme.keywordsNoLitVar)) {
			this.errorMessage = lexList[0] + " is a reserved keyword and not a variable, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(lexList[0].matches(Lexeme.VARIDENT)) {
			if(!checkIfVarExist(lexList[0])) {
				return null;
			}
		}
		if(lexList.length==2) {
			this.errorMessage = "R reassignment has missing operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}
		
		//check if RHS is expression
		ArrayList<String> operands = new ArrayList<String>();
		String ifOperations = "";
		
		//check if RHS is literals
		String newVal = null;
		
		//valid operators
		String operatorValid = Lexeme.concat+Lexeme.mathOperator+Lexeme.boolOperator.substring(0,Lexeme.boolOperator.length()-10); //remove MKAY
		
		//if literals
		if(lexList[2].matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7))) {
			if(lexList.length!=3) {
				this.errorMessage = "R reassignment has excess operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}
			newVal = lexList[2];
		}else if(lexList.length>3 && lexList[2].matches(operatorValid)) { //expression
			if(!lexList[2].matches(operatorValid)) {
				this.errorMessage = "R reassignment has invalid operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}
			for(int i=2;i<lexList.length;i++) operands.add(lexList[i]);
			String[] operandsArr =  new String[operands.size()];
			for(int i=0;i<operands.size();i++) operandsArr[i] = operands.get(i);
			if(operandsArr[0].matches(Lexeme.NOT) && operandsArr.length<2) {
				this.errorMessage = "R reassignment has missing operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}else if(!operandsArr[0].matches(Lexeme.NOT) && operandsArr.length<4) {
				this.errorMessage = "R reassignment has missing operands, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}
			ifOperations = allOperations(operandsArr);
		}else if(lexList[2].matches(Lexeme.VARIDENT) && !lexList[2].matches(Lexeme.keywordsNoLitVar)) { //check if RHS is varident and exisiting
			if(lexList.length!=3) {
				this.errorMessage = "R reassignment has excess operands,error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return null;
			}else if(!checkIfVarExist(lexList[2])) {
				return null;
			}
			newVal = getValueVarident(lexList[2]);
			if(newVal==null) {
				return null;
			}else if(lexList[2].matches("\\b"+lexList[0]+"\\b")) {
				this.errorMessage = "Warning due to self-reassignment!";
				return null;
			}
		}else {
			this.errorMessage = "Reassignment has invalid "+ lexList[2] + " operand, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null;
		}
		if(ifOperations==null) return null;
		else if(ifOperations.length()==0) {}
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
			if(operand.matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7)+"|"+Lexeme.VARIDENT) && !operand.matches(expOp)) return;
		}else if(operand.matches(Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7)+"|"+Lexeme.VARIDENT) && !operand.matches(expOp)) return;

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
					clearTables(); return null;
				}
			}catch(Exception e) {
				clearTables(); return null;
			}
		}else if(tokenArrLine[0].matches(Lexeme.boolOperator.substring(0, Lexeme.boolOperator.length()-37))) {  //both of, either of, won of, not, all of, any of, mkay
			try {
				String ans = setBooleanOperation(tokenArrLine);
				if(ans!=null) {
					return ans;
				}else {
					clearTables(); return null;
				}
			}catch(Exception e) {
				clearTables(); return null;
			}
		}else if(tokenArrLine[0].matches(Lexeme.boolOperator.substring(67,93))){ //both saem, diffrint
			try {
				String ans = setComparisonOperation(tokenArrLine);
				if(ans!=null) {
					return ans;
				}else{
						clearTables(); return null;
				}
			}catch(Exception e) {
				clearTables(); return null;				
			}
		}else if(tokenArrLine[0].matches(Lexeme.SMOOSH)) {
			try {
				String ans = solveSmooshOperation(tokenArrLine);
				if(ans!=null) {
					return ans;
				}else {
					clearTables();
//					this.errorMessage = "Invalid smoosh concatenation --> " + Arrays.deepToString(tokenArrLine).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
			}catch(Exception e) {
				clearTables();
//				this.errorMessage = "Invalid smoosh concatenation --> " + Arrays.deepToString(tokenArrLine).replaceAll("[\\[\\]\\,]", "");
				return null;
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
					return null;
				}
			}catch(Exception e) {
				clearTables();
				return null;
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
			displayResult.setEditable(false);
			this.errorMessage = "Error input, enter on the last line!";
			displayErrorMessage();
		}
	}
	
	private String doGIMMEH(String[] lexList, ArrayList<String[]> continueLine) {
		if(lexList.length==1) {
			this.errorMessage = "GIMMEH has no variable, error!";
			return null;
		}else if(lexList[1].matches(Lexeme.keywordsNoLitVar)) {
			this.errorMessage = "Use valid variable for input, error!";
			return  null;
		}else if(lexList.length>2) {
			this.errorMessage = "GIMMEH cannot store on more than one variable, error!";
			return  null;
		}
		String variable = lexList[1];
		if(!checkIfVarExist(variable)) {
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
	
	private String checkHaiKthxbye(String[] programInput) {//this function if HAI/KTHXBYE is correctly implemented
		int len = programInput.length-1;
		if(len==-1) return null;

		String HAItemp = programInput[0].replaceAll("BTW.*","");
		HAItemp = HAItemp.replaceAll("[\\s\\n]","");
		String KTHXBYEtemp = programInput[len].replaceAll("BTW.*","");
		KTHXBYEtemp = KTHXBYEtemp.replaceAll("[\\s\\\n]","");
		
		if(HAItemp.contains("HAI") && HAItemp.length()>3) {
			this.errorMessage = programInput[0] + " is not found, error!";
			return null;
		}else if(KTHXBYEtemp.contains("KTHXBYE") && KTHXBYEtemp.length()>7) {
			this.errorMessage = programInput[len] + " is not found, error!";
			return null;
		}
		
		//if hai or kthxbye has multiple occurences
		int HAILen = 0; int KTHXBYELen = 0;
		for(int i=0;i<programInput.length;i++) {
			programInput[i] = programInput[i].replaceAll("BTW.*","");
			if(programInput[i].matches(Lexeme.HAI)) HAILen++;
			else if(programInput[i].matches(Lexeme.KTHXBYE)) KTHXBYELen++;
		}
		if(HAILen!=1 || KTHXBYELen!=1) {
			if(HAILen==0) this.errorMessage="HAI is missing, error!";
			else if(HAILen>1) this.errorMessage="Found multiple HAI, error!";
			else if(KTHXBYELen==0) this.errorMessage="KTHXBYE is missinig, error!";
			else if(KTHXBYELen>1) this.errorMessage="Found multiple KTHXBYE, error!";
			return null;
		}
		this.lexemeTable.getItems().add(new Lexeme(programInput[0],Lexeme.CODE_DELIMETER));
		this.lexemeTable.getItems().add(new Lexeme(programInput[len],Lexeme.CODE_DELIMETER));
		return "success";
	}
	
	private boolean checkVISIBLEnest(String[] lexList) {
		for(int i=1;i<lexList.length;i++) {
			if(lexList[i].matches(Lexeme.VISIBLE)) {
				this.errorMessage = "VISIBLE cannot be nested together, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
				return true;
			}
		}
		return false;
	}

	private String doVISIBLE(String[] lexList) {
		if(checkVISIBLEnest(lexList)) {
			return null;
		}
		if(lexList.length==1) {
			this.errorMessage = "Error, VISIBLE has missing operand! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null; //must contain atleast one operand
		}else if(lexList[lexList.length-1].matches(Lexeme.keywordsNoLitVar)) {
			this.errorMessage = "Invalid " + lexList[lexList.length-1] +" in end of the statement! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return null; //must contain atleast one operand
		}
		String combinedOp = Lexeme.mathOperator + Lexeme.boolOperator+"\\bAN\\b";
		String combinedVal = Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7);
		
		ArrayList<String> outputPrint = new ArrayList<String>();
		
		for(int i=1;i<lexList.length;i++) { //1 because VISIBLE keyword is excluded
			//literals only
			if(lexList[i].matches(Lexeme.ALL_LITERALS) && !lexList[i].matches("\\bAN\\b")) {
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
				if(!lexList[lexList.length-1].matches(Lexeme.MKAY)) {
					this.errorMessage = "No MKAY found in expression --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
					return null;//if mkay is not the last item
				}
				while(!lexList[i].matches(Lexeme.MKAY)) {
					tempStore.add(lexList[i]);
					i++;
				}
				tempStore.add(lexList[lexList.length-1]); //insert mkay
				if(tempStore.size()>2) { 
					String[] passToOp = new String[tempStore.size()];
					for(int a=0;a<tempStore.size();a++) passToOp[a] = tempStore.get(a);
					if(checkInvalidNest(passToOp)) return null;
					String ans = allOperations(passToOp); //if fail to evaluate the expression, throw syntax error
					if(ans!=null) {
						outputPrint.add(ans);
					}
					else {
						this.errorMessage = "Invalid expression --> " + Arrays.deepToString(passToOp).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
				}else {
					this.errorMessage = "Invalid expression --> " + Arrays.deepToString(tempStore.toArray()).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
			}
			
			
			
			//math,comparison, boolean (but not any of/all of/smoosh)
			else if(lexList[i].matches(combinedOp)) {
				String regexOperation = Lexeme.mathOperator+Lexeme.boolOperator.substring(0,Lexeme.boolOperator.length()-1);
				String literalsVar = Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7)+"|"+Lexeme.VARIDENT;
				ArrayList<String> tempStore = new ArrayList<String>();
				
				int numOperation = 0; int numOperand = 0;
				while(numOperation+1!=numOperand) {
					if(i>=lexList.length) {
						this.errorMessage="Expression has wrong operands, error! --> " +  Arrays.deepToString(tempStore.toArray()).replaceAll("[\\[\\]\\,]", "");
						return null;
					}else if(lexList[i].matches(Lexeme.NOT)) {
						tempStore.add(lexList[i]);
					}else if(lexList[i].matches(literalsVar) && !lexList[i].matches("\\bAN\\b") && !lexList[i].matches(regexOperation)) {
						tempStore.add(lexList[i]);
						numOperand++;
					}else if(lexList[i].matches(Lexeme.ANY_OF+"|"+Lexeme.ALL_OF+"|"+Lexeme.MKAY)) {
						this.errorMessage="Wrong use of operator, " + lexList[i] + ", error! --> " +  Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
						return null;
					}else if(lexList[i].matches(regexOperation)) {
						tempStore.add(lexList[i]);
						numOperation++;
					}else if(lexList[i].matches("\\bAN\\b")) {
						if(i+1<lexList.length && lexList[i+1].matches("\\bAN\\b")) {
							this.errorMessage="No operand between AN AN -->" +  Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
							return null;
						}
						if(i+1==lexList.length) {
							this.errorMessage="Invalid AN in the end of statement --> " +  Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
							return null;
						}
						tempStore.add(lexList[i]);
					}
					i++;
				}
				i--;	
				if(tempStore.size()>=2) { //NOT unary has at least 2 operands
					String[] passToOp = new String[tempStore.size()];
					for(int a=0;a<tempStore.size();a++) passToOp[a] = tempStore.get(a);
					
					if(checkInvalidNest(passToOp)) return null;
					
					String ans = allOperations(passToOp);
					if(ans!=null) outputPrint.add(ans);
					else {
						this.errorMessage="Invalid expression,error! --> " +  Arrays.deepToString(passToOp).replaceAll("[\\[\\]\\,]", "");
						return null;
					}
				}else {
					this.errorMessage="Expression has missing operands, error! --> " +  Arrays.deepToString(tempStore.toArray()).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
				continue;
			} //end of expression
			
			
			//variable
			else if(lexList[i].matches(Lexeme.VARIDENT) && !lexList[i].matches("\\bAN\\b")) {
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
	
	private String checkITvalue() { //this function is for IF-ELSE if the value of IT can be cast to troof datatype
		String ITval= getValueVarident("IT");
		if(ITval!=null) {
			if(ITval.length()!=0) ITval = ITval.replaceAll("\"", "");
		}
		String validIT = "\\b"+ Lexeme.TROOF[0]+"\\b|\\b"+Lexeme.TROOF[1]+"\\b";
	
		if(ITval==null) return null; //if IT does not exist or NOOB data type (len = 0) or cannot be cast to TROOF data type
		else if(!ITval.matches(validIT)) {
			return Lexeme.TROOF[1];
		}
		return ITval; //if no error found
	}
	
	private String findErrorInIFELSE(int isO_RLY, int isYA_RLY, int isNO_WAI, int isOIC, int increment, ArrayList<Integer> mebbeIndeces, ArrayList<String[]> tokensProgram) {
		int lenMebbeIndeces = mebbeIndeces.size();
		
		if(isYA_RLY==-1 || isOIC==-1) {
			if(isYA_RLY==-1) this.errorMessage = "YA RLLY not found in conditional statement, error!";
			else this.errorMessage = "OIC not found in conditional statement, error!";
			return null;
		}
		//if there mebbee statement and NO WAI is not the last condition to be check (if it exists)
		if(isNO_WAI!=-1) {
			if(isNO_WAI<isO_RLY) {
				this.errorMessage = "NO WAI must be preceeded by O RLY or MEBBE, error!";
				return null;
			}else if(lenMebbeIndeces!=0 && mebbeIndeces.get(lenMebbeIndeces-1)>isNO_WAI) { //else statement must be the last condition statement (if it exists)
				this.errorMessage = "NO WAI is not found in the last condition statement, error!";
				return null;
			}
		}
		//checking for if codeblocks exists (codeblock is required for every conditional statements)
		if(mebbeIndeces.size()!=0) {
			int firstMebbe = mebbeIndeces.get(0);
			if(firstMebbe-isYA_RLY==1) { //between YA RLY and first mebbe
				this.errorMessage = "Codeblock in YA RLY not found, error! --> " + Arrays.deepToString(tokensProgram.get(firstMebbe)).replaceAll("[\\[\\]\\,]", "");
				return null;
			}else if(isNO_WAI!=-1) { //between last mebbe and NO WAI (if exists)
				int lastMebbe = mebbeIndeces.get(mebbeIndeces.size()-1); 
				if(isNO_WAI-lastMebbe==1) {
					this.errorMessage = "Codeblock in MEBBE not found, error! --> " + Arrays.deepToString(tokensProgram.get(lastMebbe)).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
			}else if(isNO_WAI==-1) { //between last mebbe and OIC (if NO WAI does not exist)
				int lastMebbe = mebbeIndeces.get(mebbeIndeces.size()-1); 
				if(isOIC-lastMebbe==1) {
					this.errorMessage = "Codeblock in MEBBE not found, error! --> " + Arrays.deepToString(tokensProgram.get(lastMebbe)).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
			}
			//check for all mebbee if there's exisiting codeblock, else error
			for(int a=0;a<mebbeIndeces.size()-1;a++) {
				int A = mebbeIndeces.get(a+1), B = mebbeIndeces.get(a);
				if(A-B==1) {
					this.errorMessage = "Codeblock in MEBBE not found, error! --> " + Arrays.deepToString(tokensProgram.get(B)).replaceAll("[\\[\\]\\,]", "");
					return null;
				}
			}
		}
		//if there's no mebbe
		if(isNO_WAI!=-1 && isNO_WAI-isYA_RLY==1) {
			this.errorMessage = "Codeblock in YA RLY not found, error! --> " + Arrays.deepToString(tokensProgram.get(isYA_RLY)).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(isNO_WAI!=-1 && isOIC-isNO_WAI==1) {
			this.errorMessage = "Codeblock in NO WAI not found, error! --> " + Arrays.deepToString(tokensProgram.get(isNO_WAI)).replaceAll("[\\[\\]\\,]", "");
			return null;
		}else if(isOIC-isYA_RLY==1) {
			this.errorMessage = "Codeblock in YA RLY not found, error! --> " + Arrays.deepToString(tokensProgram.get(isYA_RLY)).replaceAll("[\\[\\]\\,]", "");
			return null;
		}
		return "succes";
	}
	
	private String findMebbeWIN(ArrayList<Integer> mebbeIndeces, ArrayList<String[]> tokensProgram, int isNO_WAI, int isOIC) {
		String ij=null;
		String boolOp = Lexeme.boolOperator.substring(0,Lexeme.boolOperator.length()-10);
		String troofValues = "\\b"+Lexeme.TROOF[0]+"\\b|"+"\\b\""+Lexeme.TROOF[0]+"\"\\b";
		
		
		for(int i=0;i<mebbeIndeces.size();i++) {
			int mebbeStart = mebbeIndeces.get(i);
			int mebbeEnd;
			if(i+1<mebbeIndeces.size()) mebbeEnd = mebbeIndeces.get(i+1);
			else if(isNO_WAI!=-1) mebbeEnd = isNO_WAI;
			else mebbeEnd = isOIC;
			
			
			if(tokensProgram.get(mebbeStart).length<3) {
				this.errorMessage = "MEBBE contains invalid expression --> " + Arrays.deepToString(tokensProgram.get(mebbeStart)).replaceAll("[\\[\\]\\,]", "");
				return null;
			}else if(!tokensProgram.get(mebbeStart)[1].matches(Lexeme.mathOperator+Lexeme.boolOperator.substring(0, Lexeme.boolOperator.length()-10))) {
				this.errorMessage = "MEBBE does not contain an expression, error! --> " + Arrays.deepToString(tokensProgram.get(mebbeStart)).replaceAll("[\\[\\]\\,]", "");
				return null;	
			}
			
			String[] mebbeExpression = new String[tokensProgram.get(mebbeStart).length-1];
			for(int a=1;a<tokensProgram.get(mebbeStart).length;a++) mebbeExpression[a-1] = tokensProgram.get(mebbeStart)[a]; 
					
			String value = allOperations(mebbeExpression);
			if(value!=null) {
				if(value.matches(troofValues)) {
					ij=mebbeStart+"-"+mebbeEnd;
					break;
				}else {
					ij=Lexeme.TROOF[1];
				}
			}else if(value==null) return null; //this is only an ERROR if null, but if numbr, numbar, still accepted
		}
		if(ij==null) {
			if(isNO_WAI!=-1) ij=isNO_WAI+"-"+isOIC;
		}
		return ij;
	}
	
	private int doIFELSE(ArrayList<String[]> tokensProgram,int i) {	
		if(tokensProgram.get(i).length!=1) {
			this.errorMessage = "O RLY? contains operands, error! --> " + Arrays.deepToString(tokensProgram.get(i)).replaceAll("[\\[\\]\\,]", "");
			return -1;
		}

		//-1 means error in return, else it  holds the statement's index
		int isYA_RLY=-1, isNO_WAI=-1, isOIC=-1, increment=0;
		ArrayList<Integer> mebbeIndeces = new ArrayList<Integer>(); //list of mebee indeces (key)
		
		//finding the ORLY, YA RLY, NO WAI, OIC keywords
		for(int a=i;a<tokensProgram.size();a++) {
			if(tokensProgram.get(a)[0].matches(Lexeme.OIC)) { //finding the OIC keyword
				if(tokensProgram.get(a).length>1) {
					this.errorMessage = tokensProgram.get(a)[0].replaceAll("[\\[\\]\\,]", "") + " contains operands, error!";
					return -1;
				}
				isOIC=a;
				increment= a+1;
				break;
			}else if(tokensProgram.get(a)[0].matches(Lexeme.YA_RLY)) {
				if(!tokensProgram.get(a-1)[0].matches(Lexeme.O_RLY)) {
					this.errorMessage = tokensProgram.get(a)[0].replaceAll("[\\[\\]\\,]", "") + " must be preceded by O RLY?, error!";
					return -1;
				}else if(tokensProgram.get(a).length>1) {
					this.errorMessage = "YA RLY contains operands, error! --> " + Arrays.deepToString(tokensProgram.get(a)).replaceAll("[\\[\\]\\,]", "");
					return -1;
				}
				isYA_RLY=a;
			}else if(tokensProgram.get(a)[0].matches(Lexeme.NO_WAI)) {
				if(tokensProgram.get(a).length>1) {
					this.errorMessage = tokensProgram.get(a)[0].replaceAll("[\\[\\]\\,]", "") + " contains operands, error!";
					return -1;
				}
				isNO_WAI = a;
			}
			else if(tokensProgram.get(a)[0].matches(Lexeme.MEBBE)) mebbeIndeces.add(a);
			else if(tokensProgram.get(a)[0].matches(Lexeme.GTFO)) {
				this.errorMessage = "GTFO is not implemented in switch case statements, error!";
				return -1;
			}
		}
		
		
		//finding error in IF else construct
		String isError = findErrorInIFELSE(i, isYA_RLY, isNO_WAI, isOIC, increment, mebbeIndeces, tokensProgram);
		if(isError==null) return -1;

		//check if the IT value has troof (or can be cast) for YA RLY, NO WAIT block
		String ITval= checkITvalue();
		if(ITval==null) return -1;
		
		//finding where the condition statement falls (true)
		ArrayList<String[]> blockStatement = new ArrayList<String[]>();
		String troofValues = "\\b"+Lexeme.TROOF[0]+"\\b|"+"\\b\""+Lexeme.TROOF[0]+"\"\\b";
		int start=-1, end=-1;
		if(ITval.matches(troofValues)) {
			start=isYA_RLY+1;
			if(mebbeIndeces.size()!=0) end=mebbeIndeces.get(0); //if there's mebbe, YA RLY execute only until first mebbe
			else if(isNO_WAI!=-1) end=isNO_WAI; //if there's NO WAI, YA RLY execute until NO WAI
			else end=isOIC; //else, YA RLY will execute until OIC
		}else if(mebbeIndeces.size()!=0){ //mebbe condeblock
			
			//returns i-j if MEBBE is true, FAIL when no MEBBEE is true
			//i-j contains the index of scope of the MEBBE codeblock
			String ansIndex = findMebbeWIN(mebbeIndeces, tokensProgram, isNO_WAI, isOIC);
			if(ansIndex!=null) {
				if(ansIndex.contains("-")) {
					start = Integer.parseInt(ansIndex.split("-")[0])+1;
					end = Integer.parseInt(ansIndex.split("-")[1]);
				}else { //if NOT NULL AND NOT WIN
					if(isNO_WAI!=-1) {
						start = isNO_WAI+1;
						end = isOIC;
					}
				}
			}else return -1; //if null, then expression has error
		}else if(isNO_WAI!=-1) { //if no mebbee and YA RLLY is false, perform else codeblock
			start = isNO_WAI+1;
			end = isOIC;
		}else return increment-1;

		if(start==-1 || end==-1) return increment-1;
		
		for(int j=start;j<end;j++) blockStatement.add(tokensProgram.get(j));	
		String block = doSyntaxAnalysis(blockStatement);
		if(block==null) return -1; //-1 means error, since null value is not syntactically correct in Int data type 
		return increment-1; //-1 if fail, syntax error
	}
	
	private boolean findErrorSwtich(ArrayList<String[]> tokensProgram,int i, int isOMGWTF, int isOIC, ArrayList<Integer> OMGlist, ArrayList<Integer> GTFOlist) {
		if(i+1<tokensProgram.size() && !tokensProgram.get(i+1)[0].matches(Lexeme.OMG)) {
			this.errorMessage="WTF? is not followed by OMG, error --> " +  Arrays.deepToString(tokensProgram.get(i+1)).replaceAll("[\\[\\]\\,]", "");
			return true;
		}else if(OMGlist.size()==0) {
			this.errorMessage="Swtich case must at least contain one OMG, error!";
			return true;
		}else if(isOIC==-1) {
			this.errorMessage="OIC in switch case is not found, error!";
			return true;
		}
		for(int a=0;a<OMGlist.size();a++) {
			int A = OMGlist.get(a);
			if(a+1==OMGlist.size()) {
				if(isOMGWTF!=-1) {
					if(isOMGWTF-A==1) { //between last OMG and OMGWTF 
						this.errorMessage = "Codeblock not found after --> " + Arrays.deepToString(tokensProgram.get(A)).replaceAll("[\\[\\]\\,]", "");
						return true;
					}else if(isOIC-isOMGWTF==1) { //between OMGWTF and OIC 
						this.errorMessage = "Codeblock not found after --> " + Arrays.deepToString(tokensProgram.get(isOMGWTF)).replaceAll("[\\[\\]\\,]", "");
						return true;
					}
				}else {
					if(isOIC-A==1) { //between last OMG and OIC
						this.errorMessage = "Codeblock not found after --> " + Arrays.deepToString(tokensProgram.get(A)).replaceAll("[\\[\\]\\,]", "");
						return true;
					}
				}
			}else if(OMGlist.get(a+1)-A==1) { //between two OMG's
				this.errorMessage = "Codeblock not found after --> " + Arrays.deepToString(tokensProgram.get(A)).replaceAll("[\\[\\]\\,]", "");
				return true;
			}
		}
		//start of finding deadcode
		for(int a=0;a<GTFOlist.size()-1;a++) {
			int c = GTFOlist.get(a);
			if(!tokensProgram.get(c+1)[0].matches(Lexeme.OMG+"|"+Lexeme.OMGWTF+"|"+Lexeme.OIC)) {
				this.errorMessage = "Deadcode found after GTFO --> " + Arrays.deepToString(tokensProgram.get(c+1)).replaceAll("[\\[\\]\\,]", "");
				return true;
			}
		}
		
		return false; //1 means success
	}
	
	private int doSWITCH(ArrayList<String[]> tokensProgram,int i) {
		ArrayList<Integer> OMGlist = new ArrayList<Integer>();
		ArrayList<Integer> GTFOlist = new ArrayList<Integer>();
		String regexLiteral = Lexeme.ALL_LITERALS.substring(0,Lexeme.ALL_LITERALS.length()-7);
		int isOMGWTF=-1, isOIC=-1, increment=0;
		
		for(int a=i+1;a<tokensProgram.size();a++) {
			if(tokensProgram.get(a)[0].matches(Lexeme.OIC)) {
				if(tokensProgram.get(a).length>1) {
					this.errorMessage="OIC must contain no literal, identifier, or expression";
					return -1;
				}
				isOIC=a;
				increment= a-i+1; //for the next blockcode after this whole switch case
				break;
			}else if(tokensProgram.get(a)[0].matches(Lexeme.OMG)) {
				if(tokensProgram.get(a).length==1) {
					this.errorMessage = "OMG does not contain a literal, error! --> " + Arrays.deepToString(tokensProgram.get(a)).replaceAll("[\\[\\]\\,]", "");
					return -1;
				}else if(tokensProgram.get(a).length>2) {
					this.errorMessage = "OMG has excess operands, error! --> " + Arrays.deepToString(tokensProgram.get(a)).replaceAll("[\\[\\]\\,]", "");
					return -1;
				}else if(!tokensProgram.get(a)[1].matches(regexLiteral)) {
					if(tokensProgram.get(a)[1].matches(Lexeme.keywordsNoLitVar))
						this.errorMessage = "OMG contains a keyword and not a literal, error! --> " + Arrays.deepToString(tokensProgram.get(a)).replaceAll("[\\[\\]\\,]", "");
					else this.errorMessage = "OMG contains a variable and not a literal, error! --> " + Arrays.deepToString(tokensProgram.get(a)).replaceAll("[\\[\\]\\,]", "");
					return -1;
				}
				OMGlist.add(a);
			}else if(tokensProgram.get(a)[0].matches(Lexeme.OMGWTF)) {
				if(tokensProgram.get(a).length>1) {
					this.errorMessage="OMGWTF must contain no literal, identifier, or expression";
					return -1;
				}
				isOMGWTF = a;
			}else if(tokensProgram.get(a)[0].matches(Lexeme.WTF)) {
				this.errorMessage = "Swtich cases cannot be nested, error!";
				return -1;
			}else if(tokensProgram.get(a)[0].matches(Lexeme.GTFO)) {
					if(tokensProgram.get(a).length>1) {
						this.errorMessage = "GTFO has excess operands, error!";
						return -1;
					}
					GTFOlist.add(a);
			}
		}
		
		//finding errors after getting the OMG, OMGWTF, OIC indeces
		boolean isErrorSwitch = findErrorSwtich(tokensProgram,i, isOMGWTF, isOIC, OMGlist, GTFOlist);
		if(isErrorSwitch) return -1;
		
		
		String ITval= getValueVarident("IT");
		if(ITval==null) return -1;
		
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
			if(isOMGWTF!=-1) {
				for(int a=isOMGWTF+1;a<isOIC;a++) blockStatement.add(tokensProgram.get(a));
					String isBlockCodeError = doSyntaxAnalysis(blockStatement);
					if(isBlockCodeError==null) { //if codeblock to be executed has error, then the whole program must be suspended/stopeed
						return -1;
					}
			}
		}
		return increment-1;
	}
	
	private String doRemoveComments() { //removes all comments before it goes to syntax analyzer
		String removeComment = this.inputUser.getText();
		Pattern pattern = Pattern.compile("[\\s]*TLDR[ ]*[\\w]+"); //if TLDR has succeeding chars (excluding whitespaces)
		Matcher match = pattern.matcher(removeComment);
		if(match.find()) {
			this.errorMessage = "Comment error found at --> "+ match.group().replaceAll("\\n", "");
			return null;
		}
	
		Pattern pattern2 = Pattern.compile("[\\w]+[ ]*OBTW.*"); 
		Matcher match2 = pattern2.matcher(removeComment);
		if(match2.find()) {
			this.errorMessage="Comment error found at --> " + match2.group();
			return null;
		}
		
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
				if(!isFoundTLDR) {
					this.errorMessage = "TLDR is not found in the multiline comments!";
					return null;
				}
			}else newRemovedComments = newRemovedComments +"\n" + tempLines[i];
		}
		return newRemovedComments;
	}
	
	
	private int findLoopError(ArrayList<String[]> tokensProgram, int i, String[] loopStartArr) {
		//checking error for loop identifier
		if(loopStartArr[1].matches(Lexeme.keywordsNoLitVar)) {
			this.errorMessage = loopStartArr[1] + " loop identifier is a reserved keyword, error!" +  Arrays.deepToString(tokensProgram.get(i)).replaceAll("[\\[\\]\\,]", "");
			return -1;
		}else if(loopStartArr[1].matches(Lexeme.ALL_LITERALS)) {
			this.errorMessage = " loop identifier is a literal and not a variable, error! --> " + Arrays.deepToString(tokensProgram.get(i)).replaceAll("[\\[\\]\\,]", "");
			return -1;
		}else if(loopStartArr.length<8) {
			this.errorMessage = "Loop statement has missing operands, error! --> " + Arrays.deepToString(tokensProgram.get(i)).replaceAll("[\\[\\]\\,]", "");
			return -1; //8 is the minimum length of the array of where IM IN YR array belongs
		}else if(loopStartArr[4].matches(Lexeme.keywordsNoLitVar)) {
			this.errorMessage =loopStartArr[4] + " variable is a reserved keyword, error! --> "  + Arrays.deepToString(tokensProgram.get(i)).replaceAll("[\\[\\]\\,]", "");
			return -1;
		}else if(loopStartArr[4].matches(Lexeme.ALL_LITERALS)) {
			this.errorMessage = loopStartArr[4] + " loop identifier is a literal and not a variable, error! --> "  + Arrays.deepToString(tokensProgram.get(i)).replaceAll("[\\[\\]\\,]", "");
			return -1;
		}		
		if(checkIfVarExist(loopStartArr[4])) {
			String variable = getValueVarident(loopStartArr[4]);
			if(variable==null) return -1; //must have valid data type (numbr, numbar, troof only)
			if(variable.matches("\\b"+Lexeme.TROOF[1]+"\\b")) updateVar(loopStartArr[4],"0"); //online interpreter makes FAIL = 0, WIN = 1
			else if(variable.matches("\\b"+Lexeme.TROOF[0]+"\\b")) updateVar(loopStartArr[4],"1");
			else if(!variable.matches(Lexeme.NUMBR+"|"+Lexeme.NUMBAR)) {
				this.errorMessage = loopStartArr[4] +" variable is not troof, numbar, nor numbr data type, error!"  + Arrays.deepToString(tokensProgram.get(i)).replaceAll("[\\[\\]\\,]", "");
				return -1;
			}
		}else return -1;
		
		return 1;
	}
	
	private int doLoop(ArrayList<String[]> tokensProgram, int i) {
		
		String[] loopStartArr = tokensProgram.get(i); //array of where IM IN YR belongs
		int start = i, end = -1, updateVal = 0;
		String literals = Lexeme.NUMBR+"|"+Lexeme.NUMBAR+"|\\b"+Lexeme.TROOF[0]+"\\b|\\b"+Lexeme.TROOF[1]+"\\b";

		if(findLoopError(tokensProgram, i, loopStartArr)==-1) { //-1 means error
			return -1;
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
		if(end==-1) {
			this.errorMessage = "IM OUTA YR not found in loop, error!";
			return -1;
		}
		
		//loop identifer must be paired/must
		if(tokensProgram.get(end).length!=2) {
			this.errorMessage = "IM OUTA YR has excess operands, error! --> " + Arrays.deepToString(tokensProgram.get(end)).replaceAll("[\\[\\]\\,]", "");
			return -1;
		}else if(!loopStartArr[1].contentEquals(tokensProgram.get(end)[1])){
			this.errorMessage = "IM IN YR loop identifier is not the same with IM OUTA YR loop identifier, error! --> \n\t" + Arrays.deepToString(tokensProgram.get(start)).replaceAll("[\\[\\]\\,]", "") +"\n\t" + Arrays.deepToString(tokensProgram.get(end)).replaceAll("[\\[\\]\\,]", "");
			return -1;
		}
		
		//increment or decrement based if UPPIN/NERFIN respectively
		if(!tokensProgram.get(start)[2].matches(Lexeme.UPPIN+"|"+Lexeme.NERFIN)) {
			this.errorMessage = tokensProgram.get(start)[2] + " is invalid to increment or decrement the loop, error! --> " + Arrays.deepToString(tokensProgram.get(start)).replaceAll("[\\[\\]\\,]", "");
			return -1;
		}
		updateVal = loopStartArr[2].matches(Lexeme.UPPIN)? 1:-1;
		
		//getting the blockStatement to be executed
		ArrayList<String[]> blockStatements = new ArrayList<String[]>();
		for(int c=start+1;c<end;c++) blockStatements.add(tokensProgram.get(c));

		//getting the condition statement: outputs WIN (break loop), FAIL (continue loop)
		ArrayList<String> conditionStatement = new ArrayList<String>();
		if(tokensProgram.get(start)[6].matches(Lexeme.MKAY)) {
			this.errorMessage = "Invalid MKAY in the start of loop condition expression, error! --> " + Arrays.deepToString(tokensProgram.get(start)).replaceAll("[\\[\\]\\,]", "");
			return -1; 
		}else if(!tokensProgram.get(start)[6].matches(Lexeme.boolOperator.substring(0,Lexeme.boolOperator.length()-1))) {
			this.errorMessage = "Boolean expression can only be used for loop condition, error!";
			return -1; //BOTH SAEM because if: WIN - stop loop, FAIL = continue loop
		}
		
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
			String loopResult = doSyntaxAnalysis(blockStatements);
			if(loopResult==null){
				return -1;
			}else if(loopResult.matches("break")) {
				break;
			}
			
			//increment/decrement the variable (depends if UPPIN, NERFIN)
			String varUpdate = getValueVarident(loopStartArr[4]);
			if(varUpdate!=null && varUpdate.matches(Lexeme.NUMBAR)) {
				Float newval = Float.parseFloat(getValueVarident(loopStartArr[4]))+ updateVal;
				updateVar(loopStartArr[4],Float.toString(newval));
			}else if(varUpdate!=null && varUpdate.matches(Lexeme.NUMBR)) {
				int newval = Integer.parseInt(getValueVarident(loopStartArr[4]))+updateVal;
				updateVar(loopStartArr[4],Integer.toString(newval));
			}
			if(checkerInfiniteLoop>=500) return -2; //ideal scenario (online interpreter only allowed at most 1,000 iterations, else infinite loop error)
			checkerInfiniteLoop++;
			
		}
		return end;
	}
	
	
	private void findErrorSyntaxAnalysis(String[] lexList) { //this function also returns an syntax error, if any
		String lexeme = lexList[0];
		
		//conditional statements
		String[] conditionalKeywords = Lexeme.conditional.split("\\|"); //ifelse => 1,2,3, switch => 5,6,7
		for(int i=1;i<conditionalKeywords.length;i++) {
			if(lexeme.matches(conditionalKeywords[i])) {
				if(i<4) this.errorMessage = lexeme + " is not implemented with IF-ELSE statements, error!";
				else this.errorMessage = lexeme + " is not implemented with SWITCH-CASE statements, error!";
				return;
			}
		}
		
		//literals, ANY, MKAY, ITZ, IT, R
		String regexLiterals = Lexeme.ALL_LITERALS.substring(0, Lexeme.ALL_LITERALS.length()-7);
		
		if(lexeme.matches(regexLiterals)) {
			this.errorMessage = lexeme + " literal has unknown operation, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return;
		}else if(lexeme.matches(Lexeme.ITZ)) {
			this.errorMessage = lexeme + " must be only used with initialization, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return;
		}else if(lexeme.matches("\\bAN\\b")) {
			this.errorMessage = lexeme + " is not with any expression, error! --> "  + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return;
		}else if(lexeme.matches(Lexeme.MKAY)){
			this.errorMessage = lexeme + " keyword is not implemented with ALL OF or ANY OF expression, error! --> "  + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return;
		}else if(lexeme.matches(Lexeme.IT)) {
			this.errorMessage = lexeme + " variable has with unknown operation, error! --> "  + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return;
		}else if(lexeme.matches("\\bR\\b")) {
			this.errorMessage = lexeme + " reassignment is not stored to a variable, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return;
		}else if(lexeme.matches(Lexeme.TLDR)) {
			this.errorMessage = lexeme + " is not implemented with multiline comment statements, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return;
		}else if(lexeme.matches(Lexeme.GTFO)) {
			this.errorMessage = lexeme + " is not implemented with switch case statements, error! --> " + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return;
		}else if(lexeme.matches(Lexeme.VARIDENT) && !lexeme.matches(Lexeme.keywordsNoLitVar)) {
			this.errorMessage = lexeme + " variable has unknown operation, error! --> "  + Arrays.deepToString(lexList).replaceAll("[\\[\\]\\,]", "");
			return;
		}
	}
	
	private ArrayList<String[]> doLexicalAnalysis() { 	
		clearTables();
		this.symbolTable.getItems().add(new SymbolTable(Lexeme.IT,""));
		 displayResult.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; -fx-font-size: 1.5em;");
		this.displayResult.setText("");
		this.errorMessage="";

		
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
		
		//checks for HAI/KTHXBYE
		if(checkHaiKthxbye(programInput)==null) {
			Lexeme.HAI_KTHXBYE_ERROR = true;
			return null;
		}
		
		//finding error in lexical analysis
		Pattern regexSplit = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
		for(int i=0;i<programInput.length;i++) {
			Matcher regexMatcher = regexSplit.matcher(programInput[i]);
			while(regexMatcher.find()) {
				String lex = regexMatcher.group();				
				
				if(lex.matches(Lexeme.INVALIDdecimal)) {
					this.errorMessage = lex + " has invalid decimal value, error";					
					return null;
				}else if(lex.matches(Lexeme.INVALIDdigit)) {
					 this.errorMessage = lex + "  literal has invalid positive sign, error!";
					return null;
				}else if(lex.matches(Lexeme.INVALIDvar)) {
					if(lex.matches(Lexeme.NUMBAR+"|"+Lexeme.NUMBR)) continue;
					 this.errorMessage = lex + " is invalid variable identifier, error!";
					return null;
				}
			}
		}
		
		
		//finding lexeme
		for(int i=0;i<programInput.length;i++) {	
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
//		for(String[] arr : tokenizedOutput) {
//			System.out.print(Arrays.toString(arr)+ "\n");
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
					this.errorMessage="Lexeme not found, error!";
					return null;
				}
			}
			
		} //for loop k
		
		return tokenizedOutput;
	} //end function
	
	private void displayErrorMessage() {
		clearTables();
		 displayResult.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #ff0000; -fx-font-size: 1.5em;");
		if(this.errorMessage.length()==0) displayResult.setText("Syntax Error");
		else displayResult.setText(this.errorMessage);
	}
	
	private String doSyntaxAnalysis(ArrayList<String[]> tokensPerLine) {
		
		String errorLog = checkErrorLogs(); //errorlogs consists only checking of: HAI/KTHXBYE, deadcode, comments
		if(errorLog==null) return null;
		
		for(int i=0;i<tokensPerLine.size();i++) {
			String[] tokenArrLine = tokensPerLine.get(i);
			
			//syntax for all operations (math, boolean, comparison, relational) and its result is not stored to a variable (then it must be stored to IT)
			String storeIt = allOperations(tokenArrLine);
			if(storeIt!=null) storeIT(storeIt,tokenArrLine);
			
			//syntax for I HAS
			if(tokenArrLine[0].matches(Lexeme.HAI+"|"+Lexeme.KTHXBYE)) {continue;}
			else if(tokenArrLine[0].matches(Lexeme.I_HAS_A)) {
				try {
					String ans = setIHAS(tokenArrLine);
					if(ans!=null) {
					}else return null;
					
				}catch(Exception e) {
					return null;
				}
			}else if(tokenArrLine[0].matches(Lexeme.VARIDENT) && tokenArrLine.length>1 && tokenArrLine[1].matches(Lexeme.R)) {
				try {
					String ans = makeRreassignment(tokenArrLine);
					if(ans!=null) {}
					else return null;
					
				}catch(Exception e) {
					return null;
				}
			}else if(tokenArrLine[0].matches(Lexeme.GIMMEH)) {
				if(displayResult.getLength()!=0 && displayResult.getText().charAt(0)=='\n') displayResult.setText(displayResult.getText().substring(1)); //printing format only (nothing to do with syntax analyzer)
				try{
					ArrayList<String[]> continueAfterInput = new ArrayList<String[]>();
					for(int a=i+1;a<tokensPerLine.size();a++) continueAfterInput.add(tokensPerLine.get(a));
					String ans = doGIMMEH(tokenArrLine,continueAfterInput);
					if(ans==null) return null;						
				}catch(Exception e) {
					return null;
				}
				break;
			}else if(tokenArrLine[0].matches(Lexeme.O_RLY)) {
				try {
					int ans = doIFELSE(tokensPerLine,i);
					if(ans!=-1) i=ans;
					else return null;
				}catch(Exception e) {
					return null;
				}
			}
			else if(tokenArrLine[0].matches(Lexeme.GTFO)) {
				return "break";
			}else if(tokenArrLine[0].matches(Lexeme.WTF)) {
				try {
					int ans = doSWITCH(tokensPerLine,i);
					if(ans!=-1) i+=ans;
					else return null;
				}catch(Exception e) {
					return null;
				}
			}else if(storeIt!=null) {
				this.errorMessage="";
			}else if(tokenArrLine[0].matches(Lexeme.IM_IN_YR)) {
				try {
					int ans = doLoop(tokensPerLine,i);
					if(ans!=-1 && ans!=-2) {
						i=ans; continue;
					}else if(ans==-2) {
						this.errorMessage = "Infinite loop encountered, error!";
						return null;
					}else return null;
					
				}catch(Exception e) {
					return null;
				}
				
			}else {
				findErrorSyntaxAnalysis(tokenArrLine);
				return null;				
			}
		}
		return "success";
	}
	
	private String checkErrorLogs() {
		 if(Lexeme.HAI_KTHXBYE_ERROR) {
			 return null;
		 }else if(Lexeme.COMMENT_ERROR) {
			 return null;
		 }
		return "success";
	}
	
	private void resetAllFlags() {
    	clearTables();
    	displayResult.setText("");
    	Lexeme.resetErrorFlags();
    	errorMessage=""; 
	}
	
	private void btnExecuteHandle() { //get and process the code input by user from texarea named "inputUser"
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e)
            { 
            	resetAllFlags();
//            	try {
            		if(inputUser.getText().replaceAll("[\\s]*","").length()>0) {
                		ArrayList<String[]> tokensPerLine = doLexicalAnalysis();
                		if(Lexeme.HAI_KTHXBYE_ERROR) {
                			displayErrorMessage();
                		}else if(tokensPerLine!=null) {
                			String result = doSyntaxAnalysis(tokensPerLine);
                			if(result!=null) {
                				if(result.matches("break")) {
                					errorMessage = "GTFO is implemented without switchcase or loop statements, error!";
                					displayErrorMessage();
                				}
                			}else {
                				displayErrorMessage();
                			}
                		}else {
                			displayErrorMessage();
                		}
            		}
//            	}catch(Exception e1) {
//            		displayErrorMessage();
//            	}
            		
            }
		};
		btnExecute.setOnAction(event); 
	}
} 


//BONUSES DONE:
//	1.) a! -- suppress newline
//	2.) SMOOSH
//	3.) loop (without nesting)
//  4.) loop (with nesting)
//	5.) typecast in arithmetic operation, "124" to 124
//  6.) typecast from numbr to numbar,   2.0 to 2	(no specs related to this, we follow the rule in online interpreter) and they are equal/WIN
//  7.) deadcode in switch, after gtfo
//	8.) MEBBEE added
//  9.) NO WAI (if else) is optional


