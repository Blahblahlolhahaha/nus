import java.util.zip.CRC32;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
class Bob{
    public static final CRC32 crc32 = new CRC32();
    public static int corrupted = 0;
    public static int exp = 0;
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
        Ring ring = new Ring();
        InetSocketAddress sockAddr = new InetSocketAddress("127.0.0.1",port); 
        DatagramSocket sock = new DatagramSocket(sockAddr);
        byte[] send = new byte[6];
        while(true){
            try{
                byte[] buf = new byte[64];
                DatagramPacket packet = new DatagramPacket(buf,buf.length); 
                sock.receive(packet);
                if(!check(buf,packet.getLength())){
                    send[0] = 0;
                    send[1] = 0;
                }
                else{
                    if(ring.insertBuf(buf,packet.getLength())){
                        send[0] = buf[0];
                        send[1] = 1;
                    } else{
                        send[0] = buf[0];
                        send[1] = 1;
                    }
                }
                crc32.update(send[0]);
                crc32.update(send[1]);
                long checksum = crc32.getValue();
                crc32.reset();
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

class Ring{
    public static int WINDOW = 32;
    private byte[][] ring = new byte[WINDOW][];
    private int expSeq = 0;
    private int head = 0;

    public boolean insertBuf(byte[] buf, int length){
        buf = Arrays.copyOf(buf,length);
        int seq = (buf[0] & 0xff);
        if(seq <  WINDOW - 2 && expSeq >= 256 - WINDOW + 1){
            seq += 256;
        }
        int end = expSeq + WINDOW;
        if(seq < expSeq || seq >= end){
            return true;
        }
        int insert = (head + (seq - expSeq)) % WINDOW;
        ring[insert] = buf;
        if(expSeq == seq){
            loopRing();
        } 
        return true;
    }

    private void loopRing(){
        for(int i = 0; i < WINDOW; i++){
            byte[] buf = ring[head];
            if(buf == null){
                break;
            }
            printBuf(buf);
            ring[head] = null;
            expSeq = (expSeq + 1) % 256;
            Bob.exp = expSeq;
            head = (head + 1) % WINDOW;
        }
    }

    public void printBuf(byte[] buf){
        String data = new String(buf,6,buf.length - 6);
        System.out.printf("%s",data);
    }
}

    
