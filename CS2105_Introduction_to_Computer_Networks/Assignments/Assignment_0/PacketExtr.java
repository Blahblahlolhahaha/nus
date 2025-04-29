import java.io.IOException;
class PacketExtr{
    public static void main(String[] args){
       int read;
       String sizeStr = "";
       try{
           while((read = System.in.read()) != -1){
               int packetSize = 0;
               if(read == 'B'){
                  char number[] = sizeStr.split(": ")[1].toCharArray();
                  for(int i = number.length - 1;i >= 0; i--){
                      packetSize += (number[i] - '0') * Math.pow(10,(number.length - 1) - i);
                  }
                  byte[] rest = new byte[packetSize];
                  System.in.read(rest);
                  sizeStr = ""; 
                  System.out.write(rest);
                  System.out.flush();  
               }
              else{
                 sizeStr += (char)read;
              } 
           }
       }catch(IOException e){
           System.out.println("Smth went wrong");
       }
    }

}
