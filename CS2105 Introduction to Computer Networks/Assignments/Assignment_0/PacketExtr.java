import java.io.ByteArrayOutputStream;
class PacketExtr{
    public static void main(String[] args){
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       byte[] data = new byte[1024];
       int read;
       while((read = System.in.read(data)) != -1){
           int packetSize = 0;
           int sizeData = 0; 
           for(int i = 6;i < 1024; i++){
              if(data[i] == 'B'){
                 sizeData = i;
                 for(int x = 0;i - x > 6 ;x++){
                     packetSize += data[i - 1 - x] * Math.pow(10,x);
                 }
                 System.out.write(data,i+1, 1024 - i);
                 System.out.flush();
              }
           }
           byte[] rest = new byte[packetSize - sizeData];
           System.in.read(rest); 
           
       }
    }

}
