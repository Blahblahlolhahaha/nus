#include <stdio.h>
#include "stack.h"

int main() {

	int times = 3;
	int val = 42;
	printf("Adding %d times the value %d\n", times, val);
	push_multiple(val, times);

	for (int i = 0; i < MAX_SIZE; i++) {
		printf("Adding %d\n", i);
		push(i);
	}

	//This will lead to an error.
	//extern int _top;
	//printf("The size of the stack currently is %d\n", _top);

	for(int i = 0; i <= MAX_SIZE; i++) {
		int v = pop();
		printf("Element %d is %d\n", i, v);
	}
}
