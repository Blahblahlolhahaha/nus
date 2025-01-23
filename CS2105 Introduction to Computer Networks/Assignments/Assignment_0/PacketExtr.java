import java.io.IOException;
class PacketExtr{
    public static void main(String[] args){
       byte[] data = new byte[20];
       int read;
       try{
           while((read = System.in.read(data)) != -1){
               if(data[0] != 'S'){
                   break;
               }
               int packetSize = 0;
               int sizeData = 0; 
               for(int i = 6;i < 20; i++){
                  if(data[i] == 'B'){
                     sizeData = 20 - i - 1;
                     for(int x = 0;i - x > 6 ;x++){
                         packetSize += (data[i - 1 - x]-'0') * Math.pow(10,x);
                     }
                     if(packetSize < 20){
                        System.out.write(data,i + 1,packetSize); 
                     }
                     else{
                        System.out.write(data,i + 1, 20 - i - 1);
                     }
                     System.out.flush();
                     break;
                  }
               }
               if(packetSize < 20){
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
