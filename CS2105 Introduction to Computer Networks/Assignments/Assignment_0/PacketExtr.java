import java.io.IOException;
class PacketExtr{
    public static void main(String[] args){
       byte[] data = new byte[1024];
       int read;
       try{
           while((read = System.in.read(data)) != -1){
               if(data[0] != 'S'){
                   break;
               }
               int packetSize = 0;
               int sizeData = 0; 
               for(int i = 6;i < 1024; i++){
                  if(data[i] == 'B'){
                     sizeData = 1024 - i - 1;
                     for(int x = 0;i - x > 6 ;x++){
                         packetSize += (data[i - 1 - x]-'0') * Math.pow(10,x);
                     }
                     System.out.write(data,i + 1, 1024 - i - 1);
                     System.out.flush();
                     break;
                  }
               }
               if(packetSize < 1024){
                   continue;
               }
               byte[] rest = new byte[packetSize - sizeData];
               System.in.read(rest); 
               System.out.write(rest);
               System.out.flush();     
           }
       }catch(IOException e){
           System.out.println("Smth went wrong");
       }
    }

}
