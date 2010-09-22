package net.vidageek.crawler.config.http;

public class Cookie {

	private final String name;
	private final String value;

	public Cookie(final String name, final String value) {
		this.name = name;
		this.value = value;
	}

	public String value() {
		return value;
	}

	public String name() {
		return name;
	}

}
