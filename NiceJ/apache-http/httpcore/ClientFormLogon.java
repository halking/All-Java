package httpcore;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ClientFormLogon {

	public static void main(String[] args) throws Exception{
			BasicCookieStore cookieStore = new BasicCookieStore();
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			AuthScope scope = new AuthScope("localhost", 8080);
			UsernamePasswordCredentials passwordCredentials = new UsernamePasswordCredentials("huang", "huangalin");
			credentialsProvider.setCredentials(scope,passwordCredentials);
			CloseableHttpClient client = HttpClients.custom().
					setDefaultCookieStore(cookieStore)
					.setDefaultCredentialsProvider(credentialsProvider)
					.build();
			try {
				HttpGet httpGet = new HttpGet("http://localhost:8080/");
				CloseableHttpResponse response = client.execute(httpGet);
				try {
					HttpEntity entity = response.getEntity();
					System.out.println("user login status : "+response.getStatusLine());
					EntityUtils.consume(entity);
					System.out.println("Initial set of cookies ");
					List<Cookie>  cookies = cookieStore.getCookies();
					if (cookies.isEmpty() || cookies.size()<0) {
						System.out.println("cookies is null");
					} else {
						for (int i = 0; i < cookies.size(); i++) {
							System.out.println("cookie :"+cookies.get(i).toString());
						}
					}
				} finally {
					response.close();
				}
				HttpUriRequest request = RequestBuilder.post()
						.setUri("http://localhost:8080/")
						.addParameter("TOKEN-A", "USERNAME")
						.addParameter("TOKEN-B", "PASSWORD")
						.build();
				CloseableHttpResponse response2 = client.execute(request);
				try {
					HttpEntity entity = response2.getEntity();
					System.out.println("user login status : "+response.getStatusLine());
					EntityUtils.consume(entity);
					System.out.println("Initial set of cookies ");
					List<Cookie>  cookies = cookieStore.getCookies();
					if (cookies.isEmpty() || cookies.size()<0) {
						System.out.println("cookies is null");
					} else {
						for (int i = 0; i < cookies.size(); i++) {
							System.out.println("cookie :"+cookies.get(i).toString());
						}
					}
				} finally {
					response2.close();
				}
			} finally {
				client.close();
			}
	}

}
