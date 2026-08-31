// Implement a double linked-list

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "llist.h"

// Initialize the linked list by setting head to NULL
// PRE: head is the pointer variable that points to the
//      first node of the linked list
// POST: head is set to NULL
void free_node(TLinkedList* node) {
    node->prev = nullptr;
    node->next = nullptr;
    free(node);
}

void init_llist(TLinkedList **head) {
    // Set head to NULL
    *head = NULL;
}

// Create a new node
// PRE: filename = name of file to be inserted
//      filesize = size of file in blocks
//      startblock = File's starting block
// RETURNS: A new node containing the file information is created.

TLinkedList *create_node(char *filename, int filesize, int startblock) {
    TLinkedList* node = (TLinkedList*) malloc(sizeof(TLinkedList));

    strcpy(node->filename, filename);
    node->filesize = filesize;
    node->startblock = startblock;
    node->prev = nullptr;
    node->next = nullptr;

    return node;
}

// Insert node into the end of the linkedlist indicated by head
// PRE: head = Pointer variable pointing to the start of the linked list
//      node = Node created using create_node
// POST: node is inserted into the linked list.

void insert_llist(TLinkedList **head, TLinkedList *node) {
    if(*head == NULL) {
        *head = node;
        return;
    }

    TLinkedList* trav = *head;
    while(trav->next != NULL) {
        trav = trav->next;
    }
    trav->next = node;
}

// Delete node from the linkedlist
// PRE: head = Pointer variable pointing to the start of the linked list
//      node = An existing node in the linked list to be deleted.
// POST: node is deleted from the linked list

void delete_llist(TLinkedList **head, TLinkedList *node) {
    if(node->prev != NULL) {
        node->prev->next = node->next;
    }
    if(node->next != NULL) {
        node->next->prev = node->prev;
    }
    if(node->next == NULL && node->prev == NULL) {
        *head = NULL;
    } 
    free_node(node);
}


// Find node in the linkedlist
// PRE: head = Variable that points to the first node of the linked list
//      fname = Name of file to look for
// RETURNS: The node that contains fname, or NULL if not found.
TLinkedList *find_llist(TLinkedList *head, char *fname) {
    TLinkedList* trav = head;

    while(trav != NULL) {
        if(!strcmp(trav->filename, fname)) {
            return trav;
        }
        trav = trav->next;
    }
    return NULL;
}

// Traverse the entire linked list calling a function
// PRE: head = Variable pointing to the first node of the linked list
//      fn = Pointer to function to be called for each node
// POST: fn is called with every node of the linked list.

void traverse(TLinkedList **head, void (*fn)(TLinkedList *)) {
    TLinkedList* trav = *head;

    while(trav != NULL) {
        fn(trav);
        trav = trav->next;
    } 
}

