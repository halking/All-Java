package httpcores;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;

public class HttpMain {

	public static void main(String[] args) {
			Logger logger = Logger.getLogger(HttpMain.class.getSimpleName());
			HttpRequest request = new BasicHttpRequest("GET","/",HttpVersion.HTTP_1_1);
			logger.log(Level.INFO, request.getRequestLine().getMethod());
			logger.log(Level.INFO, request.getRequestLine().getUri());
			logger.log(Level.INFO, request.getProtocolVersion().toString());
			HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
			response.addHeader("Cookie","c1=a;path=/;domain=localhost");
			response.addHeader("Cookie","c2=b;path=\"/\",c3=c;domain=\"localhost\"");
			
			HeaderIterator iterator = response.headerIterator();
			HeaderElementIterator eIterator = new BasicHeaderElementIterator(iterator);
			while (eIterator.hasNext()) {
				HeaderElement element = eIterator.nextElement();
				System.out.println("name="+element.getName()+"----value="+element.getValue());
				NameValuePair[] pair=element.getParameters();
				for (int i = 0; i < pair.length; i++) {
					System.out.println(" "+pair[i]);
				}
			}
			HttpProcessor processor = HttpProcessorBuilder.create()
					.add(new RequestContent())
					.add(new RequestConnControl())
					.add(new RequestUserAgent("MyAPP-HTTP/1.1"))
					.add(new RequestExpectContinue(true))
					.build();
			
		 HttpCoreContext context = new HttpCoreContext();
		 
		try {
			processor.process(request, context);
		} catch (HttpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

}
