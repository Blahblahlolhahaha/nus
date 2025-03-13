import java.util.zip.CRC32;
import java.util.stream.Stream;
import java.util.Arrays;
import java.lang.Exception;
import java.lang.Thread;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Alice{
    public static void main(String[] args) throws SocketException,UnknownHostException{
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
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Stream<String> lines = br.lines();
        WindowHandler handler = new WindowHandler(sockAddr);
        handler.loadString(lines);
        handler.send();
    }

}

class WindowHandler{
    private static final int TIMEOUT = 50;
    private volatile Packet[] buffer;
    private InetSocketAddress sockAddr;
    
    public  WindowHandler(InetSocketAddress sockAddr){
        this.sockAddr = sockAddr; 
    }

    public void loadString(Stream<String> lines){
        buffer = lines.flatMap(x -> Packet.getPackets(x)).toArray(Packet[]::new);
    }

    public void send(){
        Thread[] threads = new Thread[5];
        threads[0] = new Thread(()->threader(0));
        threads[1] = new Thread(()->threader(1));
        threads[2] = new Thread(()->threader(2));
        threads[3] = new Thread(()->threader(3));
        threads[4] = new Thread(()->threader(4));
        for(int i = 0; i< 5; i++){
            threads[i].start();
        }
        for(int i = 0; i < 5; i++){
            try{
                threads[i].join();
            }catch(InterruptedException e){
               System.exit(0); 
            }
        }

    }

    public static void sendPacket(InetSocketAddress sockAddr, Packet packet){
        try{
            DatagramSocket sock = new DatagramSocket();
            byte[] data = packet.getData();
            byte packSeq = data[0];
            DatagramPacket sendPacket = new DatagramPacket(data,data.length, sockAddr);
            sock.send(sendPacket);
            byte[] recv = new byte[64];
            DatagramPacket recvPacket = new DatagramPacket(recv, 64, sockAddr);
            sock.setSoTimeout(TIMEOUT);
            sock.receive(recvPacket);
            sock.close();
            byte ack = recv[0];
            int checksum = 0;
            for(int i = 1; i< 5;i++){
                checksum += recv[i] << ((i-1) * 8);
            }
            Packet packet2 = new Packet(ack,checksum);
            if(!packet2.check() && ack != packSeq){
                throw new IOException();
            }
            else{
                packet.acked = true;
            }
        } catch(IOException e){
            //WindowHandler.sendPacket(sockAddr,packet);
            packet.acked = true;
            return;
        }
    } 

    public void threader(int i){
        while(i < buffer.length){
            sendPacket(sockAddr,buffer[i]);
            while(true){
                try{ 
                    Thread.sleep(25);
                    if(i == 0 || buffer[i-1].acked){
                        i += 5;
                        break;
                    }
                }catch(InterruptedException e){
                    System.exit(0);
                }
            }
        }
    }


}


class Packet{
    private byte seq;
    private byte end;
    private long checksum;
    private byte[] data;
    boolean acked = false;
    private static final CRC32 CHECK = new CRC32();
    private static final int maxData = 58;
    private static byte currSeq = 0;
    private Packet(byte seq, byte end,byte[] data){
        this.seq = seq;
        this.end = end;
        this.data = data;
        CHECK.update(seq);
        CHECK.update(end);
        CHECK.update(data);
        this.checksum = CHECK.getValue();
        CHECK.reset();
    }

    public Packet(byte seq, int checksum){
        this.seq = seq;
        this.checksum = checksum;
    }

    public static Stream<Packet> getPackets(String data){
        byte[] bytes = data.getBytes();
        Packet[] packets = new Packet[bytes.length/maxData + 1];
        int remaining = bytes.length;
        int index = 0;
        while(remaining > 0){
            byte[] seg;
            if(remaining <= maxData){
                seg = Arrays.copyOfRange(bytes,index * maxData,bytes.length);
                byte end = 1;
                packets[index] = new Packet(currSeq, end, seg);
            }
            else{
                seg = Arrays.copyOfRange(bytes,index * maxData,(index+1)*maxData);
                byte end = 0;
                packets[index] = new Packet(currSeq, end, seg);
            }
            remaining -= maxData;
            currSeq = (byte)((currSeq + (byte)1) % 256);
            index++;
        }
        return Arrays.asList(packets).stream();
    
    }

    public boolean check(){
        CHECK.update(this.seq);
        long checksum = CHECK.getValue();
        CHECK.reset();
        return checksum == this.checksum;
    }



    public byte[] getData(){
        long check = checksum;
        byte[] sad = new byte[1 + 1 + 4 + data.length];
        sad[0] = seq;
        sad[1] = end;
        for(int i = 0;i < 4; i++){
            sad[2 + i] = (byte)(check & 0xff);
            check= check >> 8;
        }
        for(int i = 0; i < data.length; i++){
            sad[6 + i] = data[i];
        }

        check = 0;
        for(int i = 2; i < 6; i++){
            check += (long)((sad[i])&0xff) << ((i-2)*8);
        }
        return sad;
    }

}

class PacketTimeoutException extends Exception{
    public int seq;
    public PacketTimeoutException(int seq){
        super("Packet Timeout!");
        this.seq = seq;
    }

}

