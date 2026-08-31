#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "dir.h"

typedef struct fstr {
    char filename[MAX_FNAME_LEN];
    int filesize;
    int startblock;
} TFileData;

// Number of entries in hash table
#define INIT_TABLE_LEN       2

// Number of files we are inserting
#define NUM_FNAMES      6

// Test missing file
#define MISSING_FILE        "bork.jpg"
#define MISSING_LEN         15
#define MISSING_BLKNO       6

// Our hash function
int hash(char *filename, int len) {
    // Simple modulo hash function
    unsigned int sum = 0;
    int namelen = strlen(filename);

    for(int i=0; i<namelen; i++) {
        sum += (int) filename[i];
    }

    return (sum % len);
}

int main() {
    // Same set of files as testlist
    TFileData fnames[NUM_FNAMES] = {{"test.txt", 32 , 0}, 
        {"hello.txt", 172, 93}, {"a.exe", 2384, 381}, {"c.exe", 8475, 123}, 
        {"d.tmp", 8374, 274}, {"e.bin", 283, 8472}};

    int table_len = INIT_TABLE_LEN;

    // Create the hashtable
    //Recall that an array is simply a pointer to the first element
    //So we this pointer points to an array which holds the pointers to the head of our linked list.
    TLinkedList **hashtable = (TLinkedList **) malloc(sizeof(TLinkedList *) * table_len);

    // Initialize the hash table
    init_hashtable(hashtable, table_len);

    int i;

    printf("\nAdding files.\n");

    for(i=0; i<NUM_FNAMES; i++) {
        add_file(fnames[i].filename, fnames[i].filesize, fnames[i].startblock,
            hash, hashtable, table_len);
    }

    printf("\nListing files.\n");
    listdir(hashtable, table_len);

    // Search for an existing file
    printf("\nSearching for existing file %s\n", fnames[2].filename);
    TLinkedList *filedata = find_file(fnames[2].filename, hash, hashtable, table_len);
    if(!filedata) {
        printf("ERROR: Cannot find file!\n");
    }
    else
    {
        printf("OK!\n");
        printf("Filename: %s\tFile size: %d\tStarting Block: %d\n", filedata->filename,
            filedata->filesize, filedata->startblock);
    }

    printf("\nSearching for non-existing file\n");
    filedata = find_file(MISSING_FILE, hash, hashtable, table_len);
    if(!filedata)
        printf("OK. File not found.\n");
    else
        printf("ERROR: File should not be found\n");

    // Add in the file and try again
    printf("\nAdding the missing file\n");
    add_file(MISSING_FILE, MISSING_LEN, MISSING_BLKNO, hash, hashtable, table_len);

    // Now search again
    filedata = find_file(MISSING_FILE, hash, hashtable, table_len);

    if(!filedata)
        printf("ERROR: File should now be found\n");
    else
        printf("Ok, file found.\n");
        printf("Filename: %s\tFile size: %d\tStarting Block: %d\n", filedata->filename,
            filedata->filesize, filedata->startblock);
    
    //Test resizing the hashtable
    printf("\nResizing hashtable to double its size.\n");
    
    //Allocate new hashtable
    hashtable = resize_hashtable(hashtable, table_len, table_len * 2, hash);
    
    table_len = table_len * 2;

    printf("Ok. Hashtable resized.\n");
    
    // Search again for existing file
    printf("\nSearching for existing file %s\n", fnames[2].filename);
    filedata = find_file(fnames[2].filename, hash, hashtable, table_len);
    if(!filedata) {
        printf("ERROR: Cannot find file!\n");
    }
    else
    {
        printf("OK!\n");
        printf("Filename: %s\tFile size: %d\tStarting Block: %d\n", filedata->filename,
            filedata->filesize, filedata->startblock);
    }

    printf("\nListing files.\n");
    listdir(hashtable, table_len);
    
    // Delete file
    printf("\nDeleting file %s\n", fnames[1].filename);

    delete_file(fnames[1].filename, hash, hashtable, table_len);

    filedata = find_file(fnames[1].filename, hash, hashtable, table_len);

    if(!filedata) 
        printf("OK! File no longer found!\n");
    else
        printf("ERROR: File still exists!\n");

    cleanup_dir(hashtable, table_len);
    free(hashtable);
    hashtable = NULL;

    return 0;
}
