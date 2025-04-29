import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.BufferedOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.IOException;
public class WebServer{
    private static final String GET =  "GET";
    private static final String POST =  "POST";
    private static final String DELETE =  "DELETE";
    private static Store store = new Store(); 
    public static void main(String[] args) throws IOException{
        if(args.length == 0){
            System.out.println("Usage: java Main <port number>");
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
        ServerSocket serverSocket = new ServerSocket(port);
        while(true){
            Socket clientSocket = serverSocket.accept();
            BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
            InputStream in = clientSocket.getInputStream();
            processClient(in, out);    
            in.close();
            out.close();
        }
    }

    public static int findEnd(byte[] buf){
        for(int i = 0; i < buf.length-1;i++){
            if(buf[i] == 0x20 && buf[i+1] == 0x20){
                return i;
            }
        }
        return -1;
    }

    public static void processClient(InputStream in, BufferedOutputStream out)throws IOException{
        byte request[] = new byte[1024];
        int read = in.read(request ,0,1024);
        byte[] details;
        int sum = 0;
        while(read != -1){
            details = Arrays.copyOfRange(request,0,read);
            sum = read;
            int processed = 0;
            while(details.length > 0){
                while(findEnd(details) == -1){
                    int yes = in.read(request,sum,1024-sum);
                    if(yes != -1){
                        sum += yes;
                    }
                    details = Arrays.copyOfRange(request,processed,sum);
                }
                Request req = Request.parse(details,in);
                process(req, out);
                out.flush();
                processed += req.getLength();
                if(processed > sum){
                    break;
                }
                details = Arrays.copyOfRange(request, processed, sum);
            }
            read = in.read(request,0,1024);
        }
    }

    public static void process(Request req, BufferedOutputStream out){
        String method = req.getMethod();
        if(method.equals(GET)){
           String path = req.getPath();
           if(path.equals("/key")){
               getKey(req,out);
           }
           else if(path.equals("/counter")){
               getCounter(req,out);
           }
        } else if(method.equals(POST)){
            String path = req.getPath();
            if(path.equals("/key")){
               postKey(req,out);
            }
            else if(path.equals("/counter")){
               postCounter(req,out);
            }
        } else if(method.equals(DELETE)){
            String path = req.getPath();
            if(path.equals("/key")){
               deleteKey(req,out);
            }
            else if(path.equals("/counter")){
               deleteCounter(req,out);
            }
        }
    }

    public static void getKey(Request req, BufferedOutputStream out){
        byte[] value = store.getValue(req.getName());
        if(value == null){
            Response.sendResponse(out,404,new byte[0]);
            return;
        }
        Response.sendResponse(out,200,value);
    }

    public static void getCounter(Request req, BufferedOutputStream out){
        int count = store.getCounter(req.getName());
        if(count == -1){
            Response.sendResponse(out,200,"Infinity".getBytes());
            return;
        }
        if(count == 0){
            Response.sendResponse(out,404,new byte[0]);
            return;
        }
        Response.sendResponse(out,200,String.valueOf(count).getBytes());
    }

    public static void postKey(Request req, BufferedOutputStream out){
       if(store.insertKey(req.getName(),req.getBody())){
           Response.sendResponse(out,200,new byte[0]);
           return;
       } 
       Response.sendResponse(out,405,new byte[0]);
    }

    public static void postCounter(Request req, BufferedOutputStream out){
        if(store.insertCounter(req.getName(),Integer.parseInt(new String(req.getBody())))){
           Response.sendResponse(out,200,new byte[0]);
           return; 
        }
        Response.sendResponse(out,405,new byte[0]);
    }

    public static void deleteKey(Request req, BufferedOutputStream out){
        byte[] value = store.deleteKey(req.getName());
        if(value == null){
            if(Store.invalidDeletion){
                Response.sendResponse(out,405,new byte[0]);
                Store.invalidDeletion = false;
            }
            else{
                Response.sendResponse(out, 404,new byte[0]);
            }
            return;
        }
        Response.sendResponse(out,200,value);
    }

    public static void deleteCounter(Request req, BufferedOutputStream out){
        int value = store.deleteCounter(req.getName()); 
        if(value == -1){
           Response.sendResponse(out,404,new byte[0]); 
           return;
        }
        Response.sendResponse(out,200,String.valueOf(value).getBytes());
    }
}

class Request{
    private String method;
    private String path;
    private String name;
    private byte[] body;
    private HashMap<String,String> headers;

    private Request(String method, String path, String name, byte[] body, HashMap<String,String> headers){
        this.method = method;
        this.path = path;
        this.name = name;
        this.body = body;
        this.headers = headers;
    }

    public String getMethod(){
        return this.method;
    }

    public String getPath(){
        return this.path;
    }

    public String getName(){
        return this.name;
    }

    public byte[] getBody(){
        return this.body;
    }
    
    public static Request parse(byte[] request, InputStream in)throws IOException{
        String headers = "";
        for(int i = 0;i < request.length -1; i++){
            byte curr = request[i];
            byte next = request[i+1];
            if(curr == 0x20 && next == 0x20){
                headers = new String(request,0,i);
                break; 
            }
        }
        String[] stuff = headers.split(" ");
        String method = stuff[0].toUpperCase();
        String[] params = stuff[1].split("/",3);
        String path = "/" + params[1];
        String name = params[2];
        byte[] body = new byte[0];
        HashMap<String,String> head = new HashMap<>();
        for(int i = 2; i < stuff.length; i+=2){
            head.put(stuff[i].toLowerCase(),stuff[i+1]);
        }
        if(method.equals("POST")){
            int contentLen = Integer.parseInt(head.get("content-length"));
            body = new byte[contentLen];
            int count = 0;
            int read = headers.length() + 2;
            if(contentLen <= (request.length - read)){
                for(int i = 0; i < body.length; i++){
                    body[count] = request[i + read];
                    count++;
                }
            }
            else{ 
                for(int i = 0; i <request.length - read; i++){
                    body[count] = request[i + read];
                    count++;
                }
                int remaining = contentLen - count;
                while(count < contentLen){
                    if(remaining < 1024){
                        count += in.read(body,count,remaining);
                    }
                    else{
                        count += in.read(body,count,1024);
                    }
                    remaining = contentLen - count;
                }
            }
        }
        return new Request(method,path,name,body,head);
    }

    public int getLength(){
        int totalLength = method.length() + 1 + path.length() + name.length() + 1;
        for(String key: headers.keySet()){
           totalLength += key.length() + 1 + headers.get(key).length() + 1; 
        }
        return totalLength + 2 + body.length;
    }
}

class Response{
    private static final String OK = "200 OK ";
    private static final String NOTFOUND = "404 NotFound  ";
    private static final String NOTALLOWED = "405 MethodNotAllowed  ";

    public static void sendResponse(BufferedOutputStream out, int statusCode, byte[] body){
        try{
            StringBuilder buffer = new StringBuilder(); 
            switch(statusCode){
                case 200:
                    buffer.append(OK);
                    if(body.length != 0){
                        buffer.append("Content-Length " + body.length + "  ");
                        out.write(buffer.toString().getBytes(),0,buffer.length());
                        out.write(body,0,body.length);
                        return;
                    }
                    buffer.append(" ");
                    break;
                case 404:
                    buffer.append(NOTFOUND);
                    break;
                case 405:
                    buffer.append(NOTALLOWED);
                    break;
            } 
            out.write(buffer.toString().getBytes(),0,buffer.length());
            out.flush();
        }catch(IOException e){
            System.out.println("An error occured");
        }
    }
}

class Store{
    public static boolean invalidDeletion = false; 
    private HashMap<String,byte[]> valueStore;
    private HashMap<String,Integer> counterStore;

    public Store(){
        this.valueStore = new HashMap<>();
        this.counterStore = new HashMap<>();
    }

    public byte[] getValue(String key){
        byte[] value = valueStore.get(key);
        if(value == null){
            return value;
        }
        int count = getCounter(key);
        if(count == -1){
            return value;
        }
        if(count == 1){
            counterStore.remove(key);
            valueStore.remove(key);
            return value;
        }
        counterStore.put(key,count-1);
        return value;
    }

    public int getCounter(String key){
        if(!counterStore.containsKey(key)){
            if(valueStore.containsKey(key)){
                return -1;
            } else {
                return 0;
            }
        }
        return counterStore.get(key);
        
    }

    public boolean insertKey(String key, byte[] value){
        if(valueStore.containsKey(key) && getCounter(key) > 0){
            return false;
        }
        valueStore.put(key,value);
        return true;
    }

    public boolean insertCounter(String key, int count){
        if(valueStore.containsKey(key)){
            int curr = counterStore.getOrDefault(key,0);
            counterStore.put(key,count + curr);
            return true;
        }
        return false;
    }

    public byte[] deleteKey(String key){        
        if(counterStore.containsKey(key)){
            Store.invalidDeletion = true;
            return null;
        }
        return valueStore.remove(key);
    } 

    public int deleteCounter(String key){
        Integer check = counterStore.remove(key);
        if(check == null){
            return -1;
        }
        return check;
    }

}
