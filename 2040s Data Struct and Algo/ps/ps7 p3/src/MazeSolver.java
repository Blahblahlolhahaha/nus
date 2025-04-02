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
	boolean[][] visited;
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
		this.visited = new boolean[maze.getRows()][maze.getColumns()];
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		reset();
		Coord coord = new Coord(startRow, startCol, 0);
		Coord end = new Coord(endRow, endCol, 0);
		map.put(coord, 0);
		while(coord != null){
			visited[coord.row][coord.col] = true;
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
					if(!visited[row][col]){
						Coord target = new Coord(row, col, coord.fear + fear);
						if(queue.containsKey(target)){
							queue.decreaseKey(target, target.fear);
						}
						else{
							queue.add(target);
						}
					}
				}
			}
			coord = queue.poll();
		}
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		reset();
		Coord coord = new Coord(startRow, startCol, 0);
		Coord end = new Coord(endRow, endCol, 0);
		map.put(coord, 0);
		Integer min = null;
		while(coord != null){
			visited[coord.row][coord.col] = true;
			if(coord.equals(end)){
				return coord.fear;
			}
			for(int i = 0; i < 4; i++){
				int[] delta = DELTAS[i];
				int fear = WALL_FUNCTIONS.get(i).apply(maze.getRoom(coord.row, coord.col));
				if(fear != TRUE_WALL){
					boolean empty = false;
					if(fear == EMPTY_SPACE){
						empty = true;
					}
					int row = coord.row + delta[0];
					int col = coord.col + delta[1];
					if(!visited[row][col] || (row == endRow && col == endCol)){
						if(coord.fear > fear){
							fear = coord.fear;
						}
						Coord target = new Coord(row, col, fear + (empty? 1 : 0), coord);
						if(queue.containsKey(target)){
							queue.decreaseKey(target, target.fear);
						}
						else{
							queue.add(target);
						}
					}
				}
			}
			coord = queue.poll();
		}
		return min;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		return null;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-empty.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);
			for(int i = 0; i < maze.getRows(); i++){
				for(int j = 0; j < maze.getColumns(); j++){
					for(int k = 0; k < maze.getRows(); k++){
						for(int l = 0; l < maze.getColumns(); l++){
							if(solver.bonusSearch(i, j, k, l) != Math.abs(i - k) + Math.abs(j - l)){
								System.out.printf("%d%d%d%d\n",i,j,k,l);
							}
						}
					}
				}
			}
			System.out.println(solver.bonusSearch(0, 0, 0, 3));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class Coord implements Comparable<Coord>{
		public int row, col;
		public Coord parent;
		public int fear;

		public Coord(int row, int col, int fear){
			this.row = row;
			this.col = col;
			this.fear = fear;
		}

		public Coord(int row, int col, int fear, Coord parent){
			this.row = row;
			this.col = col;
			this.fear = fear;
			this.parent = parent;
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
			if(o != null){
				return this.fear - o.fear;
			}
			return -1;
		}
	}

	public class PriorityQueue<T extends Comparable<T>>{
		T[] arr;
		HashMap<T,Integer> indexMap;
		public int size;
		
		public PriorityQueue(){
			clear();
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void clear(){
			Comparable[] obj = new Comparable[8];
			arr = (T[]) obj;
			indexMap = new HashMap<>();
			size = 0;
		}

		public boolean containsKey(T obj){
			return indexMap.containsKey(obj);
		}

		public void add(T obj){
			size += 1;
			arr[size] = obj;
			int curr = size;
			bubbleUp(curr);
			if(size >= arr.length / 2){
				resize(arr.length * 2);
			}
		}

		public T poll(){
			if(size >= 1){
				T obj = arr[1];
				T last = arr[size];
				arr[1] = last;
				indexMap.put(last, 1);
				int curr = 1;
				bubbleDown(curr);
				indexMap.remove(obj);
				arr[size] = null;
				size -= 1;
				return obj;
			}
			if(size <= arr.length/4){
				resize(arr.length/2);
			}
			return null;
		}

		public void decreaseKey(T obj, int key){
			int curr = indexMap.get(obj);
			T og = arr[curr];
			if(og.compareTo(obj) > 0){
				arr[curr] = obj;
				bubbleUp(curr);
			}
		}

		private void bubbleUp(int curr){
			T obj = arr[curr];
			while(curr > 1){
				int old = curr;
				curr /= 2;
				T parent = arr[curr];
				if(parent.compareTo(obj) > 0){
					swap(old,curr);
				}
			}
		}

		private void bubbleDown(int curr){
			T last = arr[curr]; 
			while(last != null && (last.compareTo(arr[curr * 2]) > 0 
				|| last.compareTo(arr[curr * 2 + 1]) > 0)){
					if(arr[curr * 2].compareTo(arr[curr * 2 + 1]) < 0){
						swap(curr,curr * 2);
						curr = curr * 2;
					}
					else{
						swap(curr,curr * 2 + 1);
						curr = curr * 2 + 1;
					}
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void resize(int length){
			Comparable[] obj = new Comparable[length];
			for(int i = 1; i <= size; i++){
				obj[i] = arr[i];
			}
			arr = (T[]) obj;
		}

		private void swap(int old, int curr){
			T swapped = arr[curr];
			T swapped2 = arr[old];
			arr[old] = swapped;
			arr[curr] = swapped2;
			indexMap.put(swapped2, curr);
			indexMap.put(swapped, old);
		}

	}
}
