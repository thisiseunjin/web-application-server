package webserver;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;

public class RequestLine {
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

	private String method;
	private String path;
	private Map<String, String> headers = new HashMap<>();
	private Map<String, String> params = new HashMap<>();

	public RequestLine(String requestLine) {
		log.debug("request line : {}", requestLine);
		String[] tokens = requestLine.split(" ");
		if (tokens.length != 3) {
			throw new IllegalArgumentException(requestLine + "이 형식에 맞지 않습니다.");
		}
		method = tokens[0];
		if ("POST".equals(method)) {
			path = tokens[1];
			return;
		}

		int index = tokens[1].indexOf("?");
		if (index == -1) {
			path = tokens[1];
		} else {
			path = tokens[1].substring(0, index);
			params = HttpRequestUtils.parseQueryString(tokens[1].substring(index + 1));
		}
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public String getHeader(String name) {
		return headers.get(name);
	}

	public String getParameter(String name) {
		return params.get(name);
	}
}
