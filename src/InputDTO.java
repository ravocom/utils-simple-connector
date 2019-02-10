import java.io.Serializable;

public class InputDTO implements Serializable {

	private static final long serialVersionUID = 8689096073667421691L;
	private String url;
	private boolean debug;
	private boolean useProxy;
	private String proxyHost;
	private Integer proxyPort;

	public InputDTO(String url, boolean debug, boolean useProxy, String proxyHost, Integer proxyPort) {
		this.url = url;
		this.useProxy = useProxy;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.debug = debug;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isUseProxy() {
		return useProxy;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public String toString() {
		return "InputDTO [url=" + url + ", debug=" + debug + ", useProxy=" + useProxy + ", proxyHost=" + proxyHost
				+ ", proxyPort=" + proxyPort + "]";
	}

}
