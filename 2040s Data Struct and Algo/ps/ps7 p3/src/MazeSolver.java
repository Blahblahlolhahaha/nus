import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall
	);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 0, 1 }, // East
			{ 0, -1 }, // West
			{ 1, 0 } // South
	};

	private Maze maze;
	PriorityQueue<Coord> queue;
	HashMap<Coord, Integer> map;
	public MazeSolver() {
		this.queue =  new PriorityQueue<>();
		this.map = new HashMap<>();
		// TODO: Initialize variables.
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
	}

	public void reset(){
		queue.clear();
		map.clear();
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (maze == null) {
			return null;
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			return null;
		}

		reset();
		Coord coord = new Coord(startRow, startCol, 0);
		Coord end = new Coord(endRow, endCol, 0);
		map.put(coord, 0);
		while(coord != null){
			if(coord.equals(end)){
				return coord.fear;
			}
			for(int i = 0; i < 4; i++){
				int[] delta = DELTAS[i];
				int fear = WALL_FUNCTIONS.get(i).apply(maze.getRoom(coord.row, coord.col));
				if(fear != TRUE_WALL){
					if(fear == EMPTY_SPACE){
						fear = 1;
					}
					int row = coord.row + delta[0];
					int col = coord.col + delta[1];
					Coord target = new Coord(row, col, coord.fear + fear);
					if(map.containsKey(target)){
						int ogFear = map.get(target);
						if(ogFear <= target.fear){
							continue;
						}  
						
					}
					map.put(target, target.fear);
					queue.add(target);
				}
				// Coord target = new Coord(endRow, endCol, i)
			}
			coord = queue.poll();
		}
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		return null;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("haunted-maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);
			// for(int i = 0; i < maze.getRows(); i++){
			// 	for(int j = 0; j < maze.getColumns(); j++){
			// 		for(int k = 0; k < maze.getRows(); k++){
			// 			for(int l = 0; l < maze.getColumns(); l++){
			// 				if(solver.pathSearch(i, j, k, l) != 0){
			// 					System.out.printf("%d%d%d%d",i,j,k,l);
			// 				}
			// 			}
			// 		}
			// 	}
			// }
			System.out.println(solver.pathSearch(0, 0, 0, 4));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class Coord implements Comparable<Coord>{
		public int row, col;
		public int fear;

		public Coord(int row, int col, int fear){
			this.row = row;
			this.col = col;
			this.fear = fear;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Coord){
				Coord coord = (Coord)obj;
				return this.row == coord.row && this.col == coord.col;
			}
			return false;
		}

		@Override
		public int hashCode() {
			int seed = 13;
			return (31 * seed + row) * 31 + col;
		}

		@Override
		public int compareTo(Coord o) {
			return this.fear - o.fear;
		}
	}
}
