/**
 * 
 */
package net.vidageek.crawler;

/**
 * @author jonasabreu
 * 
 */
public enum Status {

	OK(200, 299), NOT_FOUND(404, 404), ERROR(1, 999);

	private final int begin;
	private final int end;

	private Status(final int begin, final int end) {
		this.begin = begin;
		this.end = end;
	}

	public static Status fromHttpCode(final int code) {
		for (Status status : values()) {
			if ((status.begin <= code) && (status.end >= code)) {
				return status;
			}
		}
		return ERROR;
	}

}
