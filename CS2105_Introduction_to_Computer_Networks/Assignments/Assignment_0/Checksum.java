import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.CRC32;
class Checksum{
    public static void main(String[] args){
        try{
            if(args.length <1){
               System.out.println("No file input!");
               return; 
            }
            FileInputStream fis = new FileInputStream(args[0]);
            CRC32 crc = new CRC32();
            byte[] buffer = new byte[1024];
            int read = fis.read(buffer);
            while(read != -1){
               crc.update(buffer, 0,read);
               read = fis.read(buffer); 
            } 
            fis.close();
            System.out.println(crc.getValue());
        } catch(FileNotFoundException e){
            System.out.println("File not found!");
        } catch(IOException e){
            System.out.println("Error occured while reading file");
        }
    }
}
