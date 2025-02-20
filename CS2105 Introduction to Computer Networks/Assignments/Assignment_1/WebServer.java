import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.ArrayList;

public class WebServer{
    private static final String GET =  "GET";
    private static final String POST =  "POST";
    private static final String DELETE =  "DELETE";
    
    public static void main(String[] args){
        Store store = new Store();

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
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(clientSocket.getInputStream());
            char buffer[] = new char[1024];
            int read = in.read(buffer,0,1024);
            int processed = 0;
            while(request != ""){
                String request = String.valueOf(buffer);
                Request req = Request.parse(request);
                process(req, out);
                request = request.subString(req.getLength());
            }

        }
    }

    public static void process(Request req, PrintWriter out){
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
        }
    }

    public static void getKey(Request req, PrintWriter out){
        String value = store.getValue(req.name);
        if(value == null){
            Response.sendResponse(out,404,"");
            return;
        }
        Response.sendResponse(out,200,value);
    }

    public static void getCounter(Request req, PrintWriter out){
        int count = store.getCounter(req.getName());
        if(value == -1){
            Response.sendResponse(out,200,"Infinity");
            return;
        }
        if(value == 0){
            Response.sendResponse(out,404,"");
            return;
        }
        Response.sendResponse(out,200,String.valueOf(count));
    }

    public static void postKey(Request req, PrintWriter out){
       if(store.insertKey(req.getName(),req.getBody())){
           Response.sendResponse(out,200,"");
           return;
       } 
       Response.sendResponse(out,405,"");
    }

    public static void postCounter(Request req, PrintWriter out){
        if(store.insertCounter(req.getName(),Integer.parseValue())){
           Reponse.sendResponse(out,200,"");
           return; 
        }
        Response.sendResponse(out,405,"");
    }
}

class Request{
    private String method;
    private String path;
    private String name;
    private String body;

    private Request(String method, String path, String name, String body){
        this.method = method;
        this.path = path;
        this.name = name;
        this.body = body;
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

    public String getBody(){
        return this.body;
    }
    
    public static Request parse(String request){
        int read = 0;
        int length = request.length();
        String[] stuff = request.split(" ");
        String method = stuff[0];
        String[] params = stuff[1].split("/");
        String path = "/" + params[1];
        String name = params[2];
        String body = "";
        if(method == "POST"){
            int contentLen = Integer.parseInt(stuff[3]);
            body = stuff[5].subString(0,contentLen);
        }
        return new Request(method,path,name,body);
    }

    public int getLength(){
        int bodyLen = 0;
        if(body != ""){
            bodyLen = 15 + body.length() + String.valueOf(body.length()).length() + 1;
        }
        return method.length() + 1 + path.length() + name.length() + 3 + bodyLen;
    }
}

class Response{
    private static final String OK = "200 OK ";
    private static final String NOTFOUND = "404 NotFound  ";
    private static final String NOTALLOWED = "405 MethodNotAllowed  ";

    public static void sendResponse(PrintWriter out, int statusCode, String body){
        StringBuilder buffer = new StringBuilder(); 
        switch(statusCode){
            case 200:
                buffer.append(OK);
                if(body != ""){
                    buffer.append("Content-Length " + body.length() + "  " + body);
                    out.print(buffer);
                    return;
                }
                buffer.append(" ");
                break;
           case 404:
                buffer.append(NOTFOUND);
                break;
           case 405:
                buffer.append(NOTALLOWED);
                break
        }
        out.print(buffer);
    }
}

class Store{
    HashMap<String,String> valueStore;
    HashMap<String,String> counterStore;

    public Store(){
        this.valueStore = new HashMap<>();
        this.counterStore = new HashMap<>();
    }

    public String getValue(String key){
        String value = valueStore.get(key);
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

    public boolean insertKey(String key, String value){
        if(valueStore.containsKey(key)){
            return false;
        }
        valueStore.add(key,value);
        return true;
    }

    public boolean insertCounter(String key, int count){
        if(counterStore.containsKey(key)){
            int curr = counterStore.getOrDefault(key,0);
            counterStore.put(key,count + curr);
            return true;
        }
        return false;
    }
}
