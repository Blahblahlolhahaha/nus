import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class TSPGraph implements IApproximateTSP {

    SasaLele sale;
    ArrayList<SasaLele> points;
    @Override
    public void MST(TSPMap map){
        
        boolean[] visited;
        int count = map.getCount();
        visited = new boolean[count];
        points =  new ArrayList<>(count);
        PriorityQueue<SasaLele> queue = new PriorityQueue<>();
        for(int i = 0; i < count; i++){
            TSPMap.Point point = map.getPoint(i);
            points.add(i, (new SasaLele(i, Math.sqrt(point.getX() * point.getX() + point.getY() * point.getY()), point,null)));
        }
        // points.sort(SasaLele::compareTo);
        SasaLele sale = points.get(0);
        this.sale = sale;
        SasaLele prev = null;
        int nodes = 0;
        while(sale != null && nodes < count){
            if(prev != null && !visited[sale.id]){
                points.set(sale.id, sale);
                map.setLink(sale.id, sale.parent.id ,false);
                sale.parent.childs.add(sale);
                map.redraw();
                visited[sale.id] = true;
                nodes += 1;
            }
            
            for(int i = 1; i < count; i++){
                if(i == sale.id || visited[i]  == true){
                    continue;
                }
                TSPMap.Point point= map.getPoint(i);
                SasaLele test = new SasaLele(i, map.pointDistance(i, sale.id), point,sale);
                if(queue.containsKey(test)){
                    queue.decreaseKey(test);
                }
                else{
                    queue.add(test);
                    
                }
            }   
            prev = sale;
            sale = queue.poll();
        }
       
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);
        ArrayList<SasaLele> leaves = new ArrayList<>();
        ArrayList<SasaLele> orphaned = new ArrayList<>();
        // leaves.add(this.sale);
        for(int i = 0; i < points.size(); i++){
            SasaLele sale = points.get(i);
            if(sale.childs.size() == 0){
                leaves.add(sale);
            }
        }
        for(int i = 0; i < leaves.size(); i++){
            Stack<SasaLele> stack = new Stack<>();
            SasaLele leaf = leaves.get(i);
            stack.add(points.get(leaves.get(i).parent.id));
            boolean link = false;
            SasaLele prev =  null;
            while(!stack.isEmpty()){
                SasaLele node = stack.pop();
                if(node.childs.size() >= 2){
                    double dist = Double.MAX_VALUE;
                    SasaLele theAns = null;
                    for(int x = 0; x < node.childs.size(); x++){
                        SasaLele point = node.childs.get(x);
                        double test =  map.pointDistance(point.id, leaf.id);
                        if(!point.equals(leaf) && (prev == null || !prev.equals(point)) && test < dist){
                            test = dist;
                            theAns = point;
                        }
                    }
                    if(theAns != null){
                        link = true;
                        map.eraseLink(theAns.id);
                        node.childs.remove(theAns);
                        theAns.parent = leaf;
                        map.setLink(theAns.id, leaf.id);
                        leaf.childs.add(theAns);
                    }
                    break;
                }
                else{
                    SasaLele add = node.childs.get(0);
                    if((prev == null || !add.equals(prev)) && !add.equals(leaf)){
                        stack.add(add);
                    }
                    if(node.parent == null){
                        map.setLink(node.id, leaf.id);
                        break;
                    }
                    else{
                        add = points.get(node.parent.id);
                        if(prev == null || !add.equals(prev) && !add.equals(leaf) && !add.equals(node)){
                            stack.add(add);
                        }
                    }
                }
                prev = node;
            }
        }
        // TODO: implement the rest of this method.
    }

    public double victoryLap = 0.0;
    @Override
    public boolean isValidTour(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        boolean visited[] = new boolean[map.getCount()];
        TSPMap.Point start = map.getPoint(0);
        TSPMap.Point node = start;
        int id = 0;
        victoryLap = 0.0;
        for(int i = 0; i < visited.length; i++){
           if(visited[id]){
               System.out.println("sad");
              return false;
           }
           visited[id] = true;
           id = node.getLink();
           if(id == -1){
               return false;
           }
           TSPMap.Point next = map.getPoint(id);
           double x = next.getX() - node.getX();
           double y = next.getY() - node.getY();
           victoryLap += Math.sqrt(x * x + y * y);
           node = next;
        }
        if(id != 0){
            return false;
        }
        return true;
    }

    @Override
    public double tourDistance(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        if(isValidTour(map)){
            return this.victoryLap;
        }
        return -1;
    }

    public class SasaLele implements Comparable<SasaLele>{
        TSPMap.Point point;
        SasaLele parent;
        double weight;
        int id;
        ArrayList<SasaLele> childs;
        public SasaLele(int id, double weight, TSPMap.Point point, SasaLele parent){
            this.id = id;
            this.weight = weight;
            this.point = point;
            this.parent = parent;
            childs = new ArrayList<>();
        }

        @Override
        public int compareTo(TSPGraph.SasaLele o) {
            if(o == null){
                return -1;
            }
            if(weight > o.weight){
                return 1;
            }
            else if(weight < o.weight){
                return -1;
            }
            return 0;
        }

        @Override
        public int hashCode() {
            int seed = 13;
			return (31 * seed + Double.hashCode(point.getX())) * 31 +  Double.hashCode(point.getY());
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof SasaLele sasa){
                return sasa.id == this.id;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "../hundredpoints.txt");
        TSPGraph graph = new TSPGraph();

        // graph.MST(map);
        graph.TSP(map);
        System.out.println(graph.isValidTour(map));
        System.out.println(graph.tourDistance(map));
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

		public void decreaseKey(T obj){
            if(containsKey(obj)){
                int curr = indexMap.get(obj);
                T og = arr[curr];
                if(og.compareTo(obj) > 0){
                    arr[curr] = obj;
                    bubbleUp(curr);
                }
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
