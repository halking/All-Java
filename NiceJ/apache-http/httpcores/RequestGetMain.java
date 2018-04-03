package httpcores;

import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Future;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.pool.BasicConnFactory;
import org.apache.http.impl.pool.BasicConnPool;
import org.apache.http.impl.pool.BasicPoolEntry;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

public class RequestGetMain {
	public static void main(String[] args) throws Exception {
//		testGet();
//		testPool();
		Date estimatedDate = DateUtils.getDate("Asia/Singapore");
		Calendar estimatedCalendar = Calendar.getInstance();
		estimatedCalendar.setTime(estimatedDate);
		Long currentTime = DateUtils.getHourAndMinute(estimatedDate);
		System.out.println(currentTime.intValue());
	}

	public static void testPool() throws Exception {
		// Http request processor
		HttpProcessor processor = HttpProcessorBuilder.create()
				.add(new RequestContent()).add(new RequestTargetHost())
				.add(new RequestConnControl())
				.add(new RequestUserAgent("HTTP/1.1"))
				.add(new RequestExpectContinue(true))
				.build();
		// Http request exrcutor
		HttpRequestExecutor executor = new HttpRequestExecutor();
		// http connection pool
		final BasicConnPool pool = new BasicConnPool(new BasicConnFactory());

		HttpHost[] targets = { new HttpHost("www.baidu.com", 80), new HttpHost("www.yahoo.com", 80) };
		class WorkThread extends Thread {
			private final HttpHost host;

			public WorkThread(final HttpHost host) {
				super();
				this.host = host;
			}

			@Override
			public void run() {
				ConnectionReuseStrategy strategy = DefaultConnectionReuseStrategy.INSTANCE;
				try {
					Future<BasicPoolEntry> future = pool.lease(this.host, null);
					boolean reusable = false;
					BasicPoolEntry entry = future.get();
					try {
						HttpClientConnection conn = entry.getConnection();
						HttpCoreContext context = HttpCoreContext.create();
						context.setTargetHost(host);
						HttpRequest request = new BasicHttpRequest("GET","/");
						request.addHeader("Username", "huang");
						request.addHeader("Password", "huang");
						System.out.println(">>>>Request URI :"+request.getRequestLine().getUri());
						executor.preProcess(request, processor, context);
						HttpResponse response = executor.execute(request, conn, context);
						executor.postProcess(response, processor, context);
						System.out.println("\n Starting ==============\n");
						System.out.println(response.getStatusLine());
						System.out.println(response.getEntity());
						System.out.println("\nEnding ==============\n");
						reusable = strategy.keepAlive(response, context);
					} catch (IOException e) {
						throw e;
					} catch (HttpException he) {
						throw he;
					} finally {
						if (reusable) {
							System.out.println("Connection kept alive....");
						}
						pool.release(entry, reusable);
					}
				} catch (Exception ex) {
					System.out.println("request to " + this.host + " failed : " + ex.getMessage());
				}
			}
		};
		
		WorkThread[] thread = new WorkThread[targets.length];
		for (int i = 0; i < thread.length; i++) {
			thread[i] = new WorkThread(targets[i]);
		}
		for (int i = 0; i < thread.length; i++) {
			thread[i].start();
		}
		for (int i = 0; i < thread.length; i++) {
			thread[i].join();
		}
	}

	public static void testGet() throws Exception {
		// Http request processor
		HttpProcessor processor = HttpProcessorBuilder.create()
				.add(new RequestContent()).add(new RequestTargetHost())
				.add(new RequestConnControl())
				.add(new RequestUserAgent("HTTP/1.1"))
				.add(new RequestExpectContinue(true))
				.build();
		// Http request exrcutor
		HttpRequestExecutor executor = new HttpRequestExecutor();

		HttpCoreContext context = HttpCoreContext.create();
		HttpHost host = new HttpHost("www.douban.com", 8080);
		context.setTargetHost(host);
		DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
		ConnectionReuseStrategy strategy = DefaultConnectionReuseStrategy.INSTANCE;
		try {
			String[] path = { "/", "/group/guide", "/gallery" };
			for (int i = 0; i < path.length; i++) {
				if (!conn.isOpen()) {
					Socket socket = new Socket(host.getHostName(), host.getPort());
					conn.bind(socket);
				} else {
					HttpRequest request = new BasicHttpRequest("GET", path[i]);
					System.out.println(">>>>Request URI :"+request.getRequestLine().getUri());
					request.addHeader("Username", "huang");
					request.addHeader("Password", "huang");
					executor.preProcess(request, processor, context);
					HttpResponse response = executor.execute(request, conn, context);
					executor.postProcess(response, processor, context);
					System.out.println("\n Starting ==============\n");
					System.out.println(response.getStatusLine());
					System.out.println(EntityUtils.toString(response.getEntity()));
					System.out.println("\nEnding ==============\n");
					if (!strategy.keepAlive(response, context)) {
						conn.close();
					} else {
						System.out.println("Connection kept alive....");
					}
				}
			}
		} finally {
			conn.close();
		}
	}
}
