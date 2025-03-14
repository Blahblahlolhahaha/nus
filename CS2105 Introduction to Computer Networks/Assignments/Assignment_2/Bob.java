import java.util.zip.CRC32;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
class Bob{
    public static int expected = 0;
    public static final CRC32 crc32 = new CRC32();
    public static void main(String[] args) throws SocketException, UnknownHostException{
        if(args.length == 0){
            System.out.println("Usage: java Alice <port number>");
        }
        int port = 0;
        try{
            port = Integer.parseInt(args[0]);
            if(port > 65536 || port < 1){
                System.out.println("Invalid port number!");
                return;
            }

        }catch(NumberFormatException e ){
            System.out.println("Invalid port number!");
            return;
        }
        InetAddress addr = InetAddress.getLocalHost();
        InetSocketAddress sockAddr = new InetSocketAddress(addr,port); 
        DatagramSocket sock = new DatagramSocket();
        byte[] buf = new byte[64];
        byte[] send = new byte[6];
        while(true){
            try{
                DatagramPacket packet = new DatagramPacket(buf,buf.length);
                sock.receive(packet);
                if(!check(buf,packet.getLength())){
                    send[0] = 0;
                    send[1] = 0;
                
                }
                else{
                    System.out.println(new String(buf,6,packet.getLength()-6));
                    send[0] = buf[0];
                    send[1] = 1;
                }
                crc32.update(send[0]);
                crc32.update(send[1]);
                long checksum = crc32.getValue();
                for(int i = 0; i< 4;i++){
                    send[2+i] = (byte)(checksum & 0xff);
                    checksum = checksum >> 8;
                }
                InetAddress srcAddr = packet.getAddress();
                int srcPort = packet.getPort();
                packet = new DatagramPacket(send,send.length,srcAddr,srcPort);
                sock.send(packet);
            }catch(IOException e){
                continue;
            }
        }

    }
            
    public static boolean check(byte[] data, int length){
        crc32.update(data[0]);
        crc32.update(data[1]);
        crc32.update(data,6,length-6);
        long checksum = crc32.getValue();
        crc32.reset();
        long test= 0;
        for(int i = 2; i < 6; i++){
            test += (long)((data[i])&0xff) << ((i-2) * 8);
        }
        return checksum == test;
    }
}

    
