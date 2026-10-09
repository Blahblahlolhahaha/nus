import java.util.LinkedList;

public class MazeSolver implements IMazeSolver {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

    private Maze maze;
    private boolean[][] visited;
    private LinkedList<Coord> queue;
    private Coord end;
	private Coord recentStart;
    public MazeSolver() {
        this.maze = null;    
        this.queue = new LinkedList<>();
    }

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
	    this.maze = maze;
        this.visited = new boolean[maze.getRows()][maze.getColumns()];
    }

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
        if(maze == null){
            throw new Exception("GG maze not initialized");
        }

        if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
        reset();
        queue.clear();
        recentStart = new Coord(startRow, startCol);
        queue.add(recentStart);
		return solve(endRow,endCol);
	}

    private void reset(){
        for(int i = 0; i < maze.getRows(); i++){
            for(int j = 0; j< maze.getColumns(); j++){
                maze.getRoom(i, j).onPath = false;
                visited[i][j] = false;
            }
        }
    }

    private boolean canGo(int row, int col, int dir){
        switch (dir) {
			case NORTH:
				return !maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return !maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return !maze.getRoom(row, col).hasEastWall();
			case WEST:
				return !maze.getRoom(row, col).hasWestWall();
		}

		return false;

    }

    public Integer solve(int row, int col){
        Coord coord = queue.poll();
        int steps = 0;
        while(coord != null){
            visited[coord.row][coord.col] = true;
            if(coord.row == row && coord.col == col){
                while(true){
                    maze.getRoom(coord.row,coord.col).onPath = true;
                    if(coord.parent == null){
                        return steps;
                    }
                    steps += 1;
                    coord = coord.parent;
                }
            }
            for(int i = 0;i < 4;i++){
                if(canGo(coord.row,coord.col,i)){
                    int[] delta = DELTAS[i];
                    Coord target = new Coord(coord.row + delta[0], coord.col + delta[1], coord);
                    if(!visited[target.row][target.col]){
                        queue.add(target);
                    }
                }
            }
            coord = queue.poll(); 
            
        }
        return null;
    } 

	@Override
	public Integer numReachable(int k) throws Exception {
        System.out.println(maze.getRows() * maze.getColumns());
	    int res = 0;
        queue.clear();
        reset();
        Coord coord = recentStart;
        while(coord != null){
            if(coord.steps == k && !visited[coord.row][coord.col]){
                res++;
            }
            else{
                for(int i = 0;i < 4; i++){
                    if(canGo(coord.row,coord.col,i)){
                        int[] delta = DELTAS[i];
                        Coord target = new Coord(coord.row + delta[0], coord.col + delta[1], coord);
                        if(!visited[target.row][target.col]){
                            queue.add(target);
                        }
                    }
                }
            }
            visited[coord.row][coord.col] = true;
            coord = queue.poll();

        } 
        return res;

    
    }

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = Maze.readMaze("maze-empty.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 2, 3));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    class Coord{
        public int row, col, steps;
        public Coord parent;

        public Coord(int row, int col){
            this.row = row;
            this.col = col;
            this.parent = null;
            this.steps = 0;
        }

        public Coord(int row, int col, Coord parent){
            this.row = row;
            this.col = col;
            this.parent = parent;
            this.steps = parent.steps + 1;
        }

        @Override
        public String toString(){
            return String.format("(%d,%d)", row,col);
        }

        @Override
        public boolean equals(Object o){
            if(o instanceof Coord){
                Coord coord = (Coord) o;
                return equals(coord);
            }
            return false;
        }

        public boolean equals(Coord coord){
            return this.row == coord.row && this.col == coord.col;
        }

    }
}
