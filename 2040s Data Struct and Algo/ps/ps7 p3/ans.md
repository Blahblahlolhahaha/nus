number of vertices = rc

For number of edges,
each column has at most (c-1) edges
each row has at most (r-1) edges
Hence max number of edges = c(r-1) + r(c-1) = 2rc - r - c

let V = rc
let E = 2rc - r - c

self implemented priority queue has the following time complexities:

bubbleUp/bubbleDown 

```java
private void bubbleUp(int curr){ //O(log(V))
	T obj = arr[curr];
	while(curr > 1){
        //each loop is O(1)
		int old = curr;
		curr /= 2; //goes up the heap log(V) times
		T parent = arr[curr];
		if(parent.compareTo(obj) > 0){
			swap(old,curr);
		}
	}
}

private void bubbleDown(int curr){ O(log(n))
	T last = arr[curr]; 
	while(last != null && (last.compareTo(arr[curr * 2]) > 0 
		|| last.compareTo(arr[curr * 2 + 1]) > 0)){
		//runs at most log(V) times, with each loop taking O(1)
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
```

poll/add/decreaseKey -> calls bubbleUp/Down while taking O(1) to set size/compare/insert into HashMap hence T(log(V)) + T(1) -> O(log(V))

containsKey queries from HashMap -> O(1)

pathSearch uses dijkstra with custom PriorityQueue:

while loop runs at most E times
each loop checks if node has been visited before/is in queue which are both O(1) 
```java
if(!visited[row][col]){ // O(1)
    if(queue.containsKey(target)){ //O(1)

    }
}
```

thereafter, if it is not visited, and not in queue, it will add to queue/decreaseKey (if node is in queue), taking log(V) time. 
Hence each loop is O(1) + O(1) + O(log(V))

thus pathSearch takes O(Elog(V))
number of vertices = V = rc

