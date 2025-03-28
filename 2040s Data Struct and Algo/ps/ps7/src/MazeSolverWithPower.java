import java.util.LinkedList;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	public Maze maze;
	public LinkedList<Coord> queue;
// 	public boolean[][][] visited;
	public boolean[][][] tracker;
	public Coord recent;
	public int superpower;
	public MazeSolverWithPower() {
		this.maze = null;
		this.queue = new LinkedList<>();
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		
	}

	private void reset(){
		this.tracker = new boolean[maze.getRows()][maze.getColumns()][this.superpower + 2];
        for(int i = 0; i < maze.getRows(); i++){
            for(int j = 0; j< maze.getColumns(); j++){
                maze.getRoom(i, j).onPath = false;
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

	public void setPath(Coord coord){
		while(coord != null){
			maze.getRoom(coord.row, coord.col).onPath = true;
			coord = coord.parent;
		}		
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		reset();
		queue.clear();
		recent = new Coord(startRow,startCol,this.superpower);
		queue.add(recent);
		while(!queue.isEmpty()){
			Coord coord = queue.poll();
			if(coord.row == endRow && coord.col == endCol){
				setPath(coord);
				return coord.steps;
			}
			for(int i = 0; i < 4; i++){		
				int[] delta = DELTAS[i];
				Coord target = new Coord(coord.row + delta[0], coord.col + delta[1], coord.superpower,coord);	
				if((target.row >= 0 && target.row < maze.getRows() && target.col >= 0 && target.col < maze.getColumns())){
					if(canGo(coord.row, coord.col, i)){
						if(!tracker[target.row][target.col][target.superpower]){
							queue.add(target);
						}
					}
					else if(coord.superpower > 0){
						if(!tracker[target.row][target.col][target.superpower]){
							target.superpower -= 1;
							queue.add(target);
						}
					}
				}	
			}
			tracker[coord.row][coord.col][superpower] = true;
		}
		return null;
	}
	

	@Override
	public Integer numReachable(int k) throws Exception {
		if(maze == null || recent == null){
			return 0;
		}
		reset();
		queue.clear();
		int num = 0;
		queue.add(recent);
		while(!queue.isEmpty()){
			Coord coord = queue.poll();
			if(coord.steps == k && !tracker[coord.row][coord.col][this.superpower+1]){
				num++;
			}
			else{
				for(int i = 0; i < 4; i++){		
					int[] delta = DELTAS[i];
					Coord target = new Coord(coord.row + delta[0], coord.col + delta[1], coord.superpower,coord);	
					if((target.row >= 0 && target.row < maze.getRows() && target.col >= 0 && target.col < maze.getColumns())){
						if(canGo(coord.row, coord.col, i)){
							if(!tracker[target.row][target.col][target.superpower]){
								queue.add(target);
							}
						}
						else if(coord.superpower > 0){
							if(!tracker[target.row][target.col][target.superpower - 1]){
								target.superpower -= 1;
								queue.add(target);
							}
							
						}
					}	
					
				}
			}
			tracker[coord.row][coord.col][coord.superpower] = true;
		    tracker[coord.row][coord.col][this.superpower+1]= true;
        }
		return num;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpowers) throws Exception {
		this.superpower = superpowers;
		return pathSearch(startRow, startCol, endRow, endCol);
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-dense.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);
			System.out.println(solver.pathSearch(0,0, 03, 3, 6));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class Coord{
		public int row, col, superpower, steps;
		public Coord parent;

		public Coord(int row, int col, int superpower){
			this.row = row;
			this.col = col;
			this.superpower = superpower;
			this.parent = null;			
			this.steps = 0;
		}

		public Coord(int row, int col, int superpower, Coord parent){
			this.row = row;
			this.col = col;
			this.superpower = superpower;
			this.parent = parent;
			this.steps  = parent.steps + 1;
		}

	}
}
