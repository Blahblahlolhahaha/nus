#include <stdio.h>
#include "stack.h"
int main() {

    int i;
    for(i=1; i<=9; i++)
    {
        printf("Enqueing %d\n", 2*i);
        push(2 * i);
    }

    printf("\nCalling map_addition.\n");
    map_addition(15);
    printf("Stack contents after map_addition:\n");
    print_stack();
/* Uncomment the following statements to test flex_reduce */

    
    int(*fptr) (int, int);
    int v1 = 1;
    int v2 = 3;
    int v3 = 10;
    
    printf("Calling flex map.\n");

    fptr = add;
    flex_map(fptr, v1);
    printf("Stack contents after flex_map\n");
    print_stack();

    fptr = multiply;
    flex_map(fptr, v2);
    printf("Stack contents after flex_map\n");
    print_stack();

    fptr = modulo;
    flex_map(fptr, v3);
    printf("Stack contents after flex_map\n");
    print_stack();

    printf("The address of fptr now is %p\n", fptr);
    
    return 0;
}

