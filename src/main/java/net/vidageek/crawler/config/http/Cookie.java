package net.vidageek.crawler.config.http;

public class Cookie {

	private final String name;
	private final String value;
	private final String domain;
	private final String path;

	public Cookie(final String name, final String value, final String domain, final String path) {
		this.name = name;
		this.value = value;
		this.domain = domain;
		this.path = path;
	}

	public String value() {
		return value;
	}

	public String name() {
		return name;
	}

	public String domain() {
		return domain;
	}

	public String path() {
		return path;
	}

}
