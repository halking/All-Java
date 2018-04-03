package httpcore;

import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ClientMain {

	public static void main(String[] args) throws Exception{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("https://www.baidu.com");
		CloseableHttpResponse response = client.execute(httpGet);
		try {
			System.out.println(response.getStatusLine());
			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i].getName()+"="+headers[i].getValue());
			}
			HttpEntity entity = response.getEntity();
			if (entity!=null) {
				InputStream is = entity.getContent();
				try {
					int a = is.read();
					System.out.println(a);
				} finally {
					is.close();
				}
			}
		} finally {
			response.close();
		}
	}

}
