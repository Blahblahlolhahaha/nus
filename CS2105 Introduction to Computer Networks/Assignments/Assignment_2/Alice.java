import java.util.zip.CRC32;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.lang.Exception;
import java.lang.Thread;
import java.lang.Runnable;
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

        
        InetSocketAddress sockAddr = new InetSocketAddress("127.0.0.1",port);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Stream<String> lines = br.lines();
        WindowHandler handler = new WindowHandler(sockAddr);
        handler.loadString(lines.toList());
        handler.send();
    }

}

class WindowHandler{
    private static final int TIMEOUT = 30;
    public static final int WINDOW = 32;
    private volatile static ArrayList<Packet> buffer;
    private volatile static int acked = 0;
    private volatile static int loc = -1;
    private InetSocketAddress sockAddr; 
    public  WindowHandler(InetSocketAddress sockAddr){
        this.sockAddr = sockAddr; 
        buffer = new ArrayList<>();
    }

    public void loadString(List<String> lines){
        StringBuilder builder = new StringBuilder();
        for(String string : lines){
            string += "\n";
            if(builder.length() + string.length() > 58 && !builder.isEmpty()){
                buffer.addAll(Packet.getPackets(builder.toString()));
                builder.setLength(0);
            }
            builder.append(string);
            
        }
        if(!builder.isEmpty()){
            buffer.addAll(Packet.getPackets(builder.toString()));
        }
        for(int i = 0;i < buffer.size(); i++){
            if(buffer.get(i) == null){
            System.out.println(i);
            }
        }
    }

    public void send(){
        int i = 0;
        SendThread[] threads = new SendThread[WINDOW];
        System.out.println(buffer.size());
        boolean send = true;
        while(send){
            send = false;
            for(int x = 0;x < WINDOW; x++){
                if(i + x == buffer.size()){
                    break;
                }
                Packet packet = buffer.get(x + i);
                if((packet != null  && !packet.acked)){
                    send = true;
                    threads[x] = new SendThread(x + i);
                    threads[x].start();
                }
            }
            for(int x = 0; x < WINDOW; x++){
                if(i + x == buffer.size()){
                    break;
                }
                try{
                    Thread thread = threads[x];
                    if(thread != null){
                        threads[x].join();
                    }
                    Packet packet = buffer.get(x + i);
                    if(x + i - loc == 1 && packet != null &&packet.acked){
                        loc++;
                    }
                }catch(InterruptedException e){
                    continue;
                }
            }
            i = loc + 1;
        }
    }

    public boolean sendPacket(InetSocketAddress sockAddr, Packet packet) throws IOException{
        DatagramSocket sock = new DatagramSocket();
        byte[] data = packet.getData();
        DatagramPacket sendPacket = new DatagramPacket(data,data.length, sockAddr);
        sock.send(sendPacket);
        byte[] recv = new byte[64];
        DatagramPacket recvPacket = new DatagramPacket(recv, 64, sockAddr);
        sock.setSoTimeout(TIMEOUT);
        sock.receive(recvPacket);
        sock.close();
        byte ack = recv[0];
        byte end = recv[1];
        long checksum = 0;
        for(int i = 2; i< 6;i++){
            checksum += (long)((recv[i]) & 0xff) << ((i-2) * 8);
        }
        Packet packet2 = new Packet(ack,end,checksum);
        //System.out.println("ack:" + ack);
        //System.out.println("seq:" + data[0]);
        if(packet2.check() && data[0] == ack){
            return true;
        }
        return false;
    } 

    private class SendThread extends Thread{
        private int i;

        public SendThread(int i){
            this.i = i;
        }

        @Override
        public void run() {
            try{
                synchronized(buffer){
                    if(sendPacket(sockAddr,buffer.get(i))){
                        //System.out.println("acked!");
                        buffer.get(i).acked = true;
                    }
                }
            }catch(IOException e){
                return;
            }
        }
    
    }

}


class Packet{
    private byte seq;
    private byte end;
    private long checksum;
    private byte[] data;
    public boolean acked = false;
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

    public Packet(byte seq, byte end, long checksum){
        this.seq = seq;
        this.end = end;
        this.checksum = checksum;
    }

    public static List<Packet> getPackets(String data){
        byte[] bytes = data.getBytes();
        Packet[] packets = new Packet[(int)Math.ceil((double)bytes.length/maxData)];
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
        return Arrays.asList(packets);
    
    }

    public boolean check(){
        CHECK.update(this.seq);
        CHECK.update(this.end);
        long checksum = CHECK.getValue();
        CHECK.reset();
        return checksum == this.checksum && this.end == 1;
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

    public int getDiff(int seq){
        int diff = seq - this.seq;
        if(diff < 0 && seq < WindowHandler.WINDOW && this.seq <= 255 - WindowHandler.WINDOW + 2){
            diff += 256;
        }
        return diff;
    }

}

class PacketTimeoutException extends Exception{
    public int seq;
    public PacketTimeoutException(int seq){
        super("Packet Timeout!");
        this.seq = seq;
    }

}

