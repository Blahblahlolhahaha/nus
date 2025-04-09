import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class TSPGraph implements IApproximateTSP {

    SasaLele sale;
    
    @Override
    public void MST(TSPMap map){
        ArrayList<SasaLele> points =  new ArrayList<>();
        boolean[] visited;
        int[] indexes;
        int count = map.getCount();
        visited = new boolean[count];
        indexes = new int[count];
        PriorityQueue<SasaLele> queue = new PriorityQueue<>();
        for(int i = 0; i < count; i++){
            TSPMap.Point point = map.getPoint(i);
            points.add(new SasaLele(i, Math.sqrt(point.getX() * point.getX() + point.getY() * point.getY()), point,null));
        }
        points.sort(SasaLele::compareTo);
        for(int i = 0; i < count; i++){
            indexes[points.get(i).id] = i;
        }
        SasaLele sale = points.get(0);
        this.sale = sale;
        SasaLele prev = null;
        while(sale != null){
            if(prev != null && !visited[sale.id]){
                map.setLink(sale.id, sale.parent.id,false);
                sale.parent.childs.add(sale);
                map.redraw();
            }
            visited[sale.id] = true;
            for(int i = 0; i < count; i++){
                SasaLele old = points.get(i);
                SasaLele test = new SasaLele(old.id, map.pointDistance(old.id, sale.id), old.point,sale);
                if(!visited[test.id]){
                    if(queue.containsKey(test)){
                        queue.decreaseKey(test);
                    }
                    else{
                        queue.add(test);
                        
                    }
                } 
            }   
            prev = sale;
            sale = queue.poll();
        }
       
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);
        boolean visited[] = new boolean[map.getCount()];
        Stack<SasaLele> stack = new Stack<>();
        stack.addAll(this.sale.childs);
        ArrayList<SasaLele> leaves = new ArrayList<>();
        leaves.add(this.sale);
        while(!stack.empty()){
            SasaLele sale = stack.pop();
            if(sale.childs.isEmpty()){
                leaves.add(sale);
            }
            else{
                stack.addAll(sale.childs);

            }
        }
        while(leaves.size() >= 2){
            SasaLele leaf = leaves.getFirst();
            double weight = Double.MAX_VALUE; 
            int index = -1;
            SasaLele res = null;
            for(int i = 1; i < leaves.size(); i++){
                SasaLele test = leaves.get(i);
                double x = test.point.getX() - leaf.point.getX();
                double y = test.point.getY() - leaf.point.getY();
                double testWeight = Math.sqrt(x * x + y * y);
                if(testWeight < weight){
                    weight = testWeight;
                    index = i;
                    res = test;
                }
            }
            if(index != -1 && res != null){
                map.setLink(leaf.id, res.id);
                leaves.remove(index);
                leaves.removeFirst();
                
            }
        }
        // TODO: implement the rest of this method.
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        return false;
    }

    @Override
    public double tourDistance(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        return 0;
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
                return sasa.point.equals(point);
            }
            return false;
        }
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "../fiftypoints.txt");
        TSPGraph graph = new TSPGraph();

        graph.MST(map);
        graph.TSP(map);
        // System.out.println(graph.isValidTour(map));
        // System.out.println(graph.tourDistance(map));
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
