package httpcores;

import java.io.IOException;
import java.net.Socket;

import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.jdbc.Null;

public class BlockingIOMain {
	
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("115.182.201.6", 80);
    	DefaultBHttpClientConnection coon = new DefaultBHttpClientConnection(1024* 1024);
		coon.bind(socket);
		HttpRequest request = new BasicHttpRequest("GET", "/mine");
		coon.sendRequestHeader(request);
		HttpResponse response = coon.receiveResponseHeader();
		coon.receiveResponseEntity(response);
		HttpEntity entity = response.getEntity();
		if (entity!=null) {
			System.out.println(entity.getContent());
		}
		HttpConnectionMetrics metrics = coon.getMetrics();
		System.out.println(coon.isOpen());
		System.out.println(metrics.getRequestCount());
		System.out.println(metrics.getResponseCount());
		HttpProcessor responsePro = HttpProcessorBuilder.create().
				add(new ResponseDate())
				.add(new ResponseContent())
				.add(new ResponseServer("My-HTTP/1.1"))
				.add(new ResponseConnControl())
				.build();
		HttpService service = new HttpService(responsePro,null);
		
	    HttpRequestHandler handler = new HttpRequestHandler() {
			
			@Override
			public void handle(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
					throws HttpException, IOException {
				// TODO Auto-generated method stub
				
			}
		};
		HttpServerConnection serverConnection = new DefaultBHttpServerConnection(1024*1024);
//		HttpContext context = serverConnection.r
	}
	
}
