#include "stack.h"
#include <stdio.h>

static int _stack[MAX_SIZE];
static int _top = 0;

void push(int data) {
	if (_top == MAX_SIZE){
		printf("Error: Stack is full. Item value (%d) is not added.\n", data);
	}   
	else {
		_stack[_top] = data;
		_top++;
	}
}

int pop() {
	int val = -1;
	if (_top == 0){
		printf("Error: Stack is empty. Nothing to return\n");
	}
	else {
		_top--;
		val = _stack[_top];
	}

	return val;
}

void push_multiple(int data, int times) {
    for (int i = 0; i < times; i++) {
        push(data);
    }
}

int size() {
    return _top;
}

/* This section is for the function pointers exercise */
int add(int val, int input) {
    return val + input;
}

int multiply(int val, int input) {
    return val * input;
}

int modulo(int val, int input) {
    if (input == 0) {
        return val;
    }
    return val % input;
}

void print_stack() {
    for (int i = 0; i < _top; i++) {
        printf("Element %d is %d\n", i+1, _stack[i]);
    }
}

void map_addition(int input) {
    int i;
    for (i = 0; i < _top; i++) {
        _stack[i] = add(_stack[i], input);
    }
}

// For you to implement.
// 
// PRE: op = pointer to the function to apply
//      value = an integer given as input 
// POST: op is called with each element of the stack as first argument and value as second argument for each 
//       and the result is written back to the same value
void flex_map(int (*op)(int, int), int value) {
    for(int i = 0; i < _top; i++) {
        _stack[i] = op(_stack[i], value);
    }
}
