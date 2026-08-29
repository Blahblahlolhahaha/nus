#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
int main()
{
      int result;
      result = fork();
      if(result==0) fork();
      else 
      {
          fork();
          fork();
      }
      printf("Hello %d\n", result);
      return 0;
}
