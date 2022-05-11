import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

//Glenn Findlay

public class EightPuzzle implements ActionListener {
	
	//window dimensions & animation speed settings
	int windowWidth = 325;
	int windowHeight = 600;
	int animateInterval = 1000; // time between frames in ms
	int animateIntervalSlow = 1500;
	int animateIntervalFast = 500;
	Timer timer = new Timer(animateInterval, this);
	
	//Frame and button fields
	int boardSize = 3;
	JFrame frame;
	JTextField textFieldSpeed;
	JTextField textFieldMove;
	int moveNum;
	JButton[][] tiles = new JButton[boardSize][boardSize];
	JButton[] functionButtons = new JButton[10];
	JButton resetButton, shuffleButton, 
	slowButton, mediumButton, fastButton,
	nextButton, stopButton, backButton;
	JPanel panel;
	
	int[][] initialBoardState = new int[boardSize][boardSize];
	
	Font myFont = new Font("Arial", Font.BOLD, 15);			
	
	ArrayList<int[][]> boardStatesList = new ArrayList<int[][]>(); 	// list of solution moves, or rewinded moves
	ArrayList<int[][]> boardStatesPast = new ArrayList<int[][]>();	// previous moves
	
	EightPuzzle(){
		
		//frame setup
		frame = new JFrame("Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(windowWidth, windowHeight);
		frame.setLayout(null);
		
		//speed field
		textFieldSpeed = new JTextField();
		textFieldSpeed.setBounds(10, 40, 150, 40);
		textFieldSpeed.setFont(myFont);
		textFieldSpeed.setEditable(false);
		textFieldSpeed.setBackground(Color.gray);
		textFieldSpeed.setBorder(null);
		textFieldSpeed.setForeground(Color.white);
		
		//move counter
		textFieldMove = new JTextField();
		textFieldMove.setBounds(205, 40, 100, 40);
		textFieldMove.setFont(myFont);
		textFieldMove.setEditable(false);
		textFieldMove.setBackground(Color.gray);
		textFieldMove.setBorder(null);
		textFieldMove.setForeground(Color.white);
		
		//control buttons
		resetButton = new JButton("Reset");
		resetButton.setBounds(25, 505, 125, 50);
		
		shuffleButton = new JButton("Shuffle");
		shuffleButton.setBounds(160, 505, 125, 50);
		
		frame.add(resetButton);
		frame.add(shuffleButton);
		
		
		//speed buttons
		slowButton = new JButton("Slow");
		slowButton.setBounds(5, 80, 100, 50);
		
		mediumButton = new JButton("Medium");
		mediumButton.setBounds(105, 80, 100, 50);
		
		fastButton = new JButton("Fast");
		fastButton.setBounds(205, 80, 100, 50);
		
		frame.add(slowButton);
		frame.add(mediumButton);
		frame.add(fastButton);
		
		//step buttons
		backButton = new JButton("<");
		backButton.setBounds(5, 145, 100, 50);
		
		stopButton = new JButton("||");
		stopButton.setBounds(105, 145, 100, 50);
		
		nextButton = new JButton(">");
		nextButton.setBounds(205, 145, 100, 50);
		
		frame.add(backButton);
		frame.add(stopButton);
		frame.add(nextButton);
		
		frame.add(textFieldSpeed);	
		frame.add(textFieldMove);
		
		//function button setup	
		functionButtons[0] = resetButton;
		functionButtons[1] = shuffleButton;
		
		functionButtons[2] = slowButton;
		functionButtons[3] = mediumButton;
		functionButtons[4] = fastButton;
		
		functionButtons[5] = backButton;
		functionButtons[6] = stopButton;
		functionButtons[7] = nextButton;
		
		for(int i = 0; i < 8;i++ ) {
			functionButtons[i].addActionListener(this);
			functionButtons[i].setFont(myFont);
			functionButtons[i].setFocusable(false); // removes on-click outline			
		}
			
		// tile button setup
		int[][] boardTiles = EightPuzzleSolver.getNewValidBoard();
		for(int i = 0; i < boardSize;i++ ) {
			for(int j = 0; j <boardSize;j++) {
			tiles[i][j] = new JButton(String.valueOf(boardTiles[i][j]));
			tiles[i][j].addActionListener(this);
			tiles[i][j].setFocusable(false);
			tiles[i][j].setFont(myFont);
			
			if(tiles[i][j].getText().equals("0")) {
				tiles[i][j].setVisible(false);
			}
			
			initialBoardState[i][j] = boardTiles[i][j];
					
			}
		}
		
		
		// panel setup
		panel = new JPanel();		
		panel.setBounds(5, 200, 300, 300);
		panel.setLayout(new GridLayout(3, 3, 0, 0));	// rows, cols, padding, padding
		panel.setBackground(Color.DARK_GRAY);
		
		// fill panel with buttons & tiles
		for(int i = 0; i < boardSize;i++ ) {
			for(int j = 0; j <boardSize;j++) {
			panel.add(tiles[i][j]);
			}
		}
		
		frame.add(panel);		
		frame.setLocationRelativeTo(null); // center on screen	
		frame.getContentPane().setBackground(Color.gray);
		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
				
		//respond to a tile click
		for(int i = 0; i < boardSize;i++ ) {
			for(int j = 0; j <boardSize;j++) {
				JButton button = tiles[i][j];
			if(e.getSource() == button) {
				if(manageTileClick(i, j)) moveNum++;
			}
			}
		}
		
		//respond to a control button click
		if(e.getSource() == resetButton) {
			timer.stop();
			
			//reset fields
			textFieldSpeed.setText("");
			moveNum = 0;		
			boardStatesList = new ArrayList<int[][]>();
			boardStatesPast = new ArrayList<int[][]>();
			
			resetBoard();
		}
		else if(e.getSource() == shuffleButton) {
			timer.stop();
			
			//reset fields
			textFieldSpeed.setText("");
			moveNum = 0;		
			boardStatesList = new ArrayList<int[][]>();
			boardStatesPast = new ArrayList<int[][]>();
			
			shuffleBoard();
		}
		
		//speed buttons
		else if(e.getSource() == slowButton) {
			boardStatesList = EightPuzzleSolver.solveBoard(getBoardState());
			
			//update timer
			textFieldSpeed.setText("Speed: Slow");
			timer.stop();	
			timer = new Timer(animateIntervalSlow, this);
			timer.start();
		}
		else if(e.getSource() == mediumButton) {
			boardStatesList = EightPuzzleSolver.solveBoard(getBoardState());
					
			//update timer
			textFieldSpeed.setText("Speed: Medium");
			timer.stop();			
			timer = new Timer(animateInterval, this);
			timer.start();
		}
		else if(e.getSource() == fastButton) {
			boardStatesList = EightPuzzleSolver.solveBoard(getBoardState());	
			
			//update timer
			textFieldSpeed.setText("Speed: Fast");
			timer.stop();		
			timer = new Timer(animateIntervalFast, this);
			timer.start();
		}
		
		//step buttons
		else if(e.getSource() == backButton && boardStatesPast.size() > 0) {
			
			//record current state
			moveNum--; 			
			if(boardStatesList.size() > 0) {
			for(int i = boardStatesList.size() - 1; i > 0; i--) {
				boardStatesList.set(i, boardStatesList.get(i-1));
			}
			boardStatesList.set(0, getBoardState());
			}
			
			//rewind board
			animationUpdate(boardStatesPast.get(boardStatesPast.size() - 1));
			boardStatesPast.remove(boardStatesPast.size() - 1);
			
		}
		else if(e.getSource() == stopButton) {
			timer.stop();
		}
		else if(e.getSource() == nextButton && !EightPuzzleSolver.isComplete(getBoardState())) {
			
			if(boardStatesList.size() > 0) {
				animationUpdate();		
			}
			else {
				boardStatesList = EightPuzzleSolver.solveBoard(getBoardState());
				animationUpdate();
			}
			moveNum++;
		}				
		
		// if this is an animation update
		else if( boardStatesList.size() > 0 && timer.isRunning()) {

			moveNum++;
			
			animationUpdate();
				
		}
		//stop if puzzle is done
		else if( boardStatesList.size() == 0) {
			timer.stop();
		}
		
		//text update
		textFieldMove.setText("Move: "+ String.valueOf(moveNum));
		if(EightPuzzleSolver.isComplete(getBoardState())) textFieldSpeed.setText("Puzzle Complete!");
				
	}
	
	//Update board to a new state
	private void animationUpdate(int[][] targetState) {
				
		for(int i = 0; i < boardSize;i++ ) {
			for(int j = 0; j <boardSize;j++) {
			tiles[i][j].setText(String.valueOf(targetState[i][j]));			
			if(tiles[i][j].getText().equals("0")) {
				tiles[i][j].setVisible(false);
			}
			else tiles[i][j].setVisible(true);
			
			}
		}
	}
	
	private void animationUpdate() {
		
		boardStatesPast.add(getBoardState());
		
		int[][] nextState = boardStatesList.get(0);
		boardStatesList.remove(0);
				
		animationUpdate(nextState);
	}
	
	
	//Move a tile after it has been clicked
	//Returns true if a tile was moved
	private boolean manageTileClick(int tileRow, int tileCol) {
		
		boolean moved = false;		
		int[][] start = getBoardState();
				
		if(tileRow - 1 >= 0 && (tiles[tileRow - 1][tileCol]).getText().equals("0")) {
			swapTiles(tileRow, tileCol, tileRow-1, tileCol);
			moved = true;
		}
		else if(tileRow + 1 <= boardSize - 1 && (tiles[tileRow + 1][tileCol]).getText().equals("0")) {
			swapTiles(tileRow, tileCol, tileRow+1, tileCol);
			moved = true;
		}
		else if(tileCol - 1 >= 0 && (tiles[tileRow][tileCol - 1]).getText().equals("0")) {
			swapTiles(tileRow, tileCol, tileRow, tileCol-1);
			moved = true;
		}
		else if(tileCol + 1 <= boardSize - 1 && (tiles[tileRow][tileCol + 1]).getText().equals("0")) {
			swapTiles(tileRow, tileCol, tileRow, tileCol + 1);
			moved = true;
		}		
		if(moved) {
			boardStatesPast.add(start);
		}
		
		return moved;
	}
		
	// revert puzzle to start state
	public void resetBoard() {
		for(int i = 0; i < boardSize;i++ ) {
			for(int j = 0; j <boardSize;j++) {
			tiles[i][j].setText(String.valueOf(initialBoardState[i][j]));			
			if(tiles[i][j].getText().equals("0")) {
				tiles[i][j].setVisible(false);
			}
			else tiles[i][j].setVisible(true);
		}
		}
		
	}
	
	// create a new puzzle arrangement
	public void shuffleBoard() {
		
		// generate new board
		int[][] boardTiles = EightPuzzleSolver.getNewValidBoard();
		
		// tile button setup
		for(int i = 0; i < boardSize;i++ ) {
			for(int j = 0; j <boardSize;j++) {
			tiles[i][j].setText(String.valueOf(boardTiles[i][j]));			
			if(tiles[i][j].getText().equals("0")) {
				tiles[i][j].setVisible(false);
			}
			else tiles[i][j].setVisible(true);
			
			initialBoardState[i][j] = boardTiles[i][j];
			
			}
		}
						
		
	}
	
	// Swaps 2 tiles given their coords
	private void swapTiles(int realX, int realY, int blankX, int blankY) {
		
		JButton realTile = tiles[realX][realY];
		JButton blankTile = tiles[blankX][blankY];
		
		blankTile.setText(realTile.getText());
		blankTile.setVisible(true);

		realTile.setText("0");
		realTile.setVisible(false);
			
	}
	
	//Returns board as 2d array
	private int[][] getBoardState() {
		
		int[][] boardState = new int[boardSize][boardSize];
		
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				boardState[i][j] = Integer.parseInt(tiles[i][j].getText());	
			}
		}
		
		return boardState;
	}
	
	public static void main(String[] args) {
		EightPuzzle puzzle = new EightPuzzle();		
	}
	
}
