import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class SimpleConnector {

	// Decided to fall back to syso's
	private static final Log log = LogFactory.getLog(SimpleConnector.class);

	public static void main(String[] args) {
		InputDTO inputDTO = captureInput(args);
		// inputDTO = new InputDTO("https://aerosearchelastic.accelaero.cloud", true,true, "10.30.100.19", 8080);
		execute(inputDTO);
	}

	private static void execute(InputDTO inputDTO) {
		HttpRequestBase httpGet = null;

		if (inputDTO != null) {
			boolean success = false;
			long startTime = new Date().getTime();

			try {

				if (inputDTO.isDebug()) {
					System.setProperty("javax.net.debug", "all");
				}

				HttpClient client = getHttpClient(inputDTO.isUseProxy(), inputDTO.getProxyHost(),
						inputDTO.getProxyPort());
				httpGet = new HttpGet(inputDTO.getUrl());

				HttpResponse httpResponse = client.execute(httpGet);

				if (httpResponse != null) {
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						success = true;
					}

					logHeaders(httpResponse);
					logResponse(httpResponse);
				}

			} catch (Exception e) {
				log.error("Execution failed with error " + e);
			} finally {
				System.out.println("Execution Status>>> ");
				System.out.println("SUCCESS=" + success + ",EXECUTION_TIME(ms)=" + (new Date().getTime() - startTime));

				if (httpGet != null) {
					httpGet.releaseConnection();
				}

			}
		}

	}

	private static HttpClient getHttpClient(boolean useProxy, String proxyHost, Integer proxyPort) {
		HttpClient httpClient;
		if (useProxy) {
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			httpClient = HttpClientBuilder.create().setProxy(proxy).build();
		} else {
			httpClient = HttpClientBuilder.create().build();
		}
		return httpClient;
	}

	private static InputDTO captureInput(String[] inputs) {

		InputDTO inputDTO = null;

		if (isValid(inputs)) {

			String[] input = inputs[0].split(",");
			String url = input[0];
			boolean debug = Boolean.valueOf(input[1]);
			boolean useProxy = Boolean.valueOf(input[2]);
			String proxyHost = null;
			Integer proxyPort = -1;

			if (input.length == 5 && useProxy) {
				proxyHost = input[3];
				proxyPort = Integer.valueOf(input[4]);
			}

			inputDTO = new InputDTO(url, debug, useProxy, proxyHost, proxyPort);

			System.out.println("########################################## INPUT PARAMETERS ######################################################");
			System.out.println(inputDTO);
			System.out.println("###################################################################################################################");
		} else {

			System.err.println("#####################################################################################################################");
			System.err.println("Please enter the input in below format to do a HTTP GET request");
			System.err.println("Format: java -jar SimpleConnector.jar URL,DEBUG,USE_PROXY,PROXY_HOST,PROXY_PORT");
			System.err.println("Eg (with proxy): java -jar SimpleConnector.jar https://aerosearchelastic.accelaero.cloud,false,true,10.30.100.19,8080");
			System.err.println("Eg (without proxy): java -jar SimpleConnector.jar https://aerosearchelastic.accelaero.cloud,false,false");
			System.err.println("######################################################################################################################");
			System.err.println();
		}

		return inputDTO;
	}

	private static boolean isValid(String[] args) {
		boolean valid = false;
		if (args != null && args.length > 0) {
			int paramCount = args[0].split(",").length;
			if (paramCount == 5 || paramCount == 3) {
				valid = true;
			}
		}
		return valid;
	}

	private static void logHeaders(HttpResponse httpResponse) {
		Header[] headers = httpResponse.getAllHeaders();
		StringBuilder sb = new StringBuilder();
		if (headers != null && headers.length > 0) {
			for (Header header : headers) {
				sb.append("Key=" + header.getName() + ", Value=" + header.getValue()).append("\n");
			}
		}
		System.out.println("Response Headers>>> ");
		System.out.println(sb);
	}

	private static void logResponse(HttpResponse response) {
		String responseString = "Error in handling response";
		try {
			responseString = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			System.err.println("Parse Exception" + e);
		} catch (IOException e) {
			System.err.println("IOException" + e);
		}
		System.out.println("Response String>>> ");
		System.out.println(responseString);
	}

}
