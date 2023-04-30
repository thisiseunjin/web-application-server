package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        	//TODO 1-1 : InputStream을 한줄로 읽기 위해 BufferedReader을 생성한다.
        	BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));
        	
        	//TODO 1-2 :  readLine 메소드를 활용해 라인별로 HTTP 요청 정보를 읽는다.
        	String line = br.readLine();
        	
        	//TODO 1-3 : 요청 정보 천체를 출력한다.
        	log.debug("all request : {}", line);
        	
        	//TODO 1-4 : line이 null값일 경우를 대비해서 예외처리를 해준다.
        	if(line == null) return;

        	//TODO 1-5 : 헤더의 마지막을 출력한다.
        	
        	
        	//TODO 2-1 : 첫 번째 요청을 추출한다.
        	String[] tokens = line.split(" ");
        	// tokens[0] = GET, tokens[1] = /index.html
        	
        	while(!line.equals("")) {
        		line = br.readLine();
        		log.debug("header : {}", line);
        	}
        	//TODO 3-1 : 요청 url에 해당하는 파일을 webapp에서 읽어 전달
            DataOutputStream dos = new DataOutputStream(out);
            //byte[] body = "Hello World".getBytes();
            byte[] body = Files.readAllBytes(new File("./webapp"+tokens[1]).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
