
import java.util.*;

//Glenn Findlay

//Contains methods for solving the eight-puzzle
public class EightPuzzleSolver {
	
	 public static final int boardWidth = 3;

	    //represents a xy position on the board
	    public static class Cell{
	        public int r;
	        public int c;

	        public Cell(int _r, int _c){
	            r = _r;
	            c = _c;
	        }

	        public String ToString(){
	            return new String("r: " + r + " c: " + c);
	        }
	    }

	    // represents a possible move
	    public static class MoveNode implements Comparable<MoveNode>{
	        int[][] boardState;
	        MoveNode parent;
	        boolean isRoot;
	        int currentCost;
	        Cell moveToReach;
	        int depth;

	        public MoveNode(int[][] _board, MoveNode _parent, Cell movingCell){
	            boardState = _board;
	            parent = _parent;
	            moveToReach = movingCell;

	            if(parent != null){
	                depth = _parent.depth +1;
	                currentCost = getBoardH(_board) + depth + 1;
	            }
	        }


	        @Override
	        public int compareTo(MoveNode other) {
	            return Integer.compare(this.currentCost, other.currentCost);
	        }

	        // equals and hashcode are required for hashset to work
	        @Override
	        public boolean equals(Object other) {

	            if (other == this) return true;

	            if (!(other instanceof MoveNode)) return false;

	            MoveNode otherNode = (MoveNode) other;
	            return (this.hashCode() == otherNode.hashCode());
	        }


	        // return a hash of the board state
	        public int hashCode(){
	            StringBuilder sb = new StringBuilder();
	            for(int i =0; i < boardWidth;i++)
	                for(int j = 0; j < boardWidth;j++){
	                    sb.append(boardState[i][j]);
	                }

	            int hashVal = Integer.valueOf(sb.toString());

	            return hashVal;
	        }

	    }


	    public static void printBoard(int[][] board){

	        for(int i = 0; i < boardWidth;i++){
	            for(int j = 0; j < boardWidth;j++){
	                System.out.print(board[i][j] + " ");
	            }
	            System.out.println("");
	        }

	    }

	    // check if board state is valid
	    // horizontal swaps must be even
	    public static boolean isSolvable(int[][] board){

	        int swaps = 0;
	        // get onto a single line
	        int[] oneLineArray = new int[boardWidth*boardWidth];
	        for(int i = 0; i < board.length; i++)
	            for(int j = 0; j < board.length;j++){
	                oneLineArray[i*3+j] = board[i][j];
	            }

	        // check the number of values that are higher than values that come later
	        for(int i = 0; i < oneLineArray.length;i++){
	                for(int j = i + 1; j < oneLineArray.length; j++)
	                    if(oneLineArray[j] < oneLineArray[i] && oneLineArray[i] != 0 && oneLineArray[j] != 0) swaps++;
	        }

	        return (swaps % 2 == 0);
	    }

	    // finds the location of the blank space on the board
	    public static Cell getIndex(int[][] board, int ID){

	        int zeroIndexR = 0;
	        int zeroIndexC = 0;

	        for(int i = 0; i < board.length;i++)
	            for(int j = 0; j < board.length;j++){
	                if(board[i][j]== ID){
	                    zeroIndexR = i;
	                    zeroIndexC = j;
	                }
	            }

	        return new Cell(zeroIndexR, zeroIndexC);
	    }


	    // get all possible moves that can be made
	    public static ArrayList<Cell> getValidMoves(int[][] board){

	        int zeroIndexR = 0;
	        int zeroIndexC = 0;

	        ArrayList<Cell> options = new ArrayList<Cell>();

	        // find 0 index
	        Cell zeroIndex = getIndex(board, 0);
	        zeroIndexR = zeroIndex.r;
	        zeroIndexC = zeroIndex.c;

	        // check R, L, Down, Up
	        if (zeroIndexC + 1 < board[zeroIndexR].length) options.add(new Cell(zeroIndexR, zeroIndexC + 1));
	        if (zeroIndexC - 1 >= 0) options.add(new Cell(zeroIndexR, zeroIndexC - 1));

	        if (zeroIndexR + 1 < board.length) options.add(new Cell(zeroIndexR + 1, zeroIndexC));
	        if (zeroIndexR - 1 >= 0) options.add(new Cell(zeroIndexR - 1, zeroIndexC));


	        return options;
	    }


	    // evaluate heuristic for a given board state
	    public static int getBoardH(int[][] board) {

	        int H = 0;

	        // find heuristic for each cell
	        for (int i = 0; i < board.length; i++) {
	            for (int j = 0; j < board.length; j++) {

	                int cellID = board[i][j];
	                Cell goalLoc = new Cell(cellID / 3, cellID % 3);

	             H += Math.abs(goalLoc.r - i) + Math.abs(goalLoc.c - j);

	            }
	        }

	        return H;
	    }


	    //is the board complete
	    public static boolean isComplete(int[][] board){

	        boolean isComplete = true;

	        // search for an out-of-place board piece
	        for(int i = 0; i < board.length;i++)
	            for(int j = 0; j < board.length;j++){
	                int id = board[i][j];
	                if((i != id / 3 || j != id % 3)) isComplete = false;
	            }


	        return isComplete;
	    }

	    // returns new board after move is made
	    public static int[][] getNextBoard(int[][] board, Cell move){

	        int[][] newBoard = new int[board.length][board.length];

	        // copy existing
	        for (int i = 0; i < boardWidth; i++)
	            for (int j = 0; j < boardWidth; j++) {
	                newBoard[i][j] = board[i][j];
	            }

	        //make the move
	        Cell zeroIndex = getIndex(board, 0);
	        int ID = board[move.r][move.c];
	        newBoard[zeroIndex.r][zeroIndex.c] = ID;
	        newBoard[move.r][move.c] = 0;

	        return newBoard;
	    }

	    // try to find a path to the solution
	    public static MoveNode searchIterative(MoveNode start) {

	        MoveNode current = start;
	        boolean puzzleComplete = false;

	        PriorityQueue<MoveNode> moveQ = new PriorityQueue<>();      // open set
	        HashSet<Integer> previousMoves = new HashSet<>();       // closed set (duplicates not added)

	        while (!puzzleComplete) {

	            if(!current.isRoot) previousMoves.add(current.hashCode());

	            if (isComplete(current.boardState)) {
	                puzzleComplete = true;
	            }

	            //search valid moves and add them to the move queue
	            if (!puzzleComplete) {
	                ArrayList<Cell> validMoves = getValidMoves(current.boardState);
	                ArrayList<MoveNode> validMoveNodes = new ArrayList<MoveNode>();


	                for (Cell c : validMoves) {
	                    int[][] newBoard = getNextBoard(current.boardState, c);
	                    validMoveNodes.add(new MoveNode(newBoard, current, c));
	                }

	                // remove moves that would take to board to a previously explored state
	                validMoveNodes.removeIf(node -> previousMoves.contains(node.hashCode()));

	                moveQ.addAll(validMoveNodes);

	                current = moveQ.poll();
	            }
	        }

	        return current;
	    }


	    // show the path taken to a current state
	    public static ArrayList<int[][]> showPath(MoveNode node){
	    	
	    	ArrayList<int[][]> boardStatesList = new ArrayList<int[][]>();

	        System.out.println("\nSolution Path: \n");

	        int[][][] boardstates = new int[node.depth][boardWidth][boardWidth];

	        MoveNode current = node;

	        // compile previous states into an array
	        for(int i = node.depth - 1; i >= 0; i--){

	            boardstates[i] = current.boardState;
	            boardStatesList.add(boardstates[i]);
	            current = current.parent;
	        }


	        // show all board states in order
	        for(int i = 0; i < node.depth; i++){
	              printBoard(boardstates[i]);
	              System.out.println();
	        }

	        System.out.println(node.depth + " total moves");

	       Collections.reverse(boardStatesList);
	        
	        return boardStatesList;
	        
	    }


	    public static ArrayList<int[][]> solveBoard(int[][] board) {

	        int [][] goalState = {
	                {0,1,2},
	                {3,4,5},
	                {6,7,8}};

	        System.out.print("Goal: \n");
	        printBoard(goalState);

	        System.out.println("\nStart state:");
	        printBoard(board);

	        MoveNode root = new MoveNode(board, null, null);
	        root.isRoot = true;
	        root.depth = 0;

	        MoveNode finalNode = (searchIterative(root));
	        
	        return showPath(finalNode);

	    }
	    
	  //Generate a new and solvable board
		public static int[][] getNewValidBoard(){
			
			int[][] boardTiles = generateBoard();
			
	        while(!EightPuzzleSolver.isSolvable(boardTiles) && !EightPuzzleSolver.isComplete(boardTiles)){
	        	boardTiles = generateBoard();
	        }
	        
	        return boardTiles;
		}
		
		   // randomly place pieces on the board
	    	public static int[][] generateBoard(){

	    	int[][] boardTiles = new int[boardWidth][boardWidth];
	    	
	        Random randomGenerator = new Random();

	        //a list of desired numbers
	        int[] numberArr = new int[] {0,1,2,3,4,5,6,7, 8};
	        ArrayList<Integer> numberList = new ArrayList<Integer>();
	        for(int i = 0; i < numberArr.length;i++){
	            numberList.add(numberArr[i]);
	        }

	        //distribute numbers randomly across board
	        for(int i=0; i < boardWidth; i++){
	            for(int j = 0; j < boardWidth; j++){
	                int nextIndex = randomGenerator.nextInt(numberList.size());
	                boardTiles[i][j] = numberList.get(nextIndex);
	                numberList.remove(nextIndex);
	            }
	        }
	        
	        return boardTiles;
	    }
	    
	    
	
}
