package net.vidageek.crawler.component;

/**
 * Code copied from HtmlUnit
 * (src/main/java/com/gargoylesoftware/htmlunit/util/UrlUtils.java)
 * (https://htmlunit.svn.sourceforge.net/svnroot/htmlunit/trunk/htmlunit -
 * commit 5556)
 * 
 * Modifications made by jonasabreu (jonas at vidageek dot net)
 */
public final class UrlUtils {

	public static String resolveUrl(final String baseUrl, final String relativeUrl) {
		if (baseUrl == null) {
			throw new IllegalArgumentException("Base URL must not be null");
		}
		if (relativeUrl == null) {
			throw new IllegalArgumentException("Relative URL must not be null");
		}
		final Url url = resolveUrl(parseUrl(baseUrl.trim()), relativeUrl.trim());

		return url.toString();
	}

	private static Url parseUrl(final String spec) {
		final Url url = new Url();
		int startIndex = 0;
		int endIndex = spec.length();

		// Section 2.4.1: Parsing the Fragment Identifier
		//
		// If the parse string contains a crosshatch "#" character, then the
		// substring after the first (left-most) crosshatch "#" and up to the
		// end of the parse string is the <fragment> identifier. If the
		// crosshatch is the last character, or no crosshatch is present, then
		// the fragment identifier is empty. The matched substring, including
		// the crosshatch character, is removed from the parse string before
		// continuing.
		//
		// Note that the fragment identifier is not considered part of the URL.
		// However, since it is often attached to the URL, parsers must be able
		// to recognize and set aside fragment identifiers as part of the
		// process.
		final int crosshatchIndex = indexOf(spec, '#', startIndex, endIndex);

		if (crosshatchIndex >= 0) {
			url.fragment_ = spec.substring(crosshatchIndex + 1, endIndex);
			endIndex = crosshatchIndex;
		}
		// Section 2.4.2: Parsing the Scheme
		//
		// If the parse string contains a colon ":" after the first character
		// and before any characters not allowed as part of a scheme name (i.e.,
		// any not an alphanumeric, plus "+", period ".", or hyphen "-"), the
		// <scheme> of the URL is the substring of characters up to but not
		// including the first colon. These characters and the colon are then
		// removed from the parse string before continuing.
		final int colonIndex = indexOf(spec, ':', startIndex, endIndex);

		if (colonIndex > 0) {
			final String scheme = spec.substring(startIndex, colonIndex);
			if (isValidScheme(scheme)) {
				url.scheme_ = scheme;
				startIndex = colonIndex + 1;
			}
		}
		// Section 2.4.3: Parsing the Network Location/Login
		//
		// If the parse string begins with a double-slash "//", then the
		// substring of characters after the double-slash and up to, but not
		// including, the next slash "/" character is the network location/login
		// (<net_loc>) of the URL. If no trailing slash "/" is present, the
		// entire remaining parse string is assigned to <net_loc>. The double-
		// slash and <net_loc> are removed from the parse string before
		// continuing.
		//
		// Note: We also accept a question mark "?" or a semicolon ";" character
		// as
		// delimiters for the network location/login (<net_loc>) of the URL.
		final int locationStartIndex;
		int locationEndIndex;

		if (spec.startsWith("//", startIndex)) {
			locationStartIndex = startIndex + 2;
			locationEndIndex = indexOf(spec, '/', locationStartIndex, endIndex);
			if (locationEndIndex >= 0) {
				startIndex = locationEndIndex;
			}
		} else {
			locationStartIndex = -1;
			locationEndIndex = -1;
		}
		// Section 2.4.4: Parsing the Query Information
		//
		// If the parse string contains a question mark "?" character, then the
		// substring after the first (left-most) question mark "?" and up to the
		// end of the parse string is the <query> information. If the question
		// mark is the last character, or no question mark is present, then the
		// query information is empty. The matched substring, including the
		// question mark character, is removed from the parse string before
		// continuing.
		final int questionMarkIndex = indexOf(spec, '?', startIndex, endIndex);

		if (questionMarkIndex >= 0) {
			if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
				// The substring of characters after the double-slash and up to,
				// but not
				// including, the question mark "?" character is the network
				// location/login
				// (<net_loc>) of the URL.
				locationEndIndex = questionMarkIndex;
				startIndex = questionMarkIndex;
			}
			url.query_ = spec.substring(questionMarkIndex + 1, endIndex);
			endIndex = questionMarkIndex;
		}
		// Section 2.4.5: Parsing the Parameters
		//
		// If the parse string contains a semicolon ";" character, then the
		// substring after the first (left-most) semicolon ";" and up to the end
		// of the parse string is the parameters (<params>). If the semicolon
		// is the last character, or no semicolon is present, then <params> is
		// empty. The matched substring, including the semicolon character, is
		// removed from the parse string before continuing.
		final int semicolonIndex = indexOf(spec, ';', startIndex, endIndex);

		if (semicolonIndex >= 0) {
			if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
				// The substring of characters after the double-slash and up to,
				// but not
				// including, the semicolon ";" character is the network
				// location/login
				// (<net_loc>) of the URL.
				locationEndIndex = semicolonIndex;
				startIndex = semicolonIndex;
			}
			url.parameters_ = spec.substring(semicolonIndex + 1, endIndex);
			endIndex = semicolonIndex;
		}
		// Section 2.4.6: Parsing the Path
		//
		// After the above steps, all that is left of the parse string is the
		// URL <path> and the slash "/" that may precede it. Even though the
		// initial slash is not part of the URL path, the parser must remember
		// whether or not it was present so that later processes can
		// differentiate between relative and absolute paths. Often this is
		// done by simply storing the preceding slash along with the path.
		if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
			// The entire remaining parse string is assigned to the network
			// location/login (<net_loc>) of the URL.
			locationEndIndex = endIndex;
		} else if (startIndex < endIndex) {
			url.path_ = spec.substring(startIndex, endIndex);
		}
		// Set the network location/login (<net_loc>) of the URL.
		if ((locationStartIndex >= 0) && (locationEndIndex >= 0)) {
			url.location_ = spec.substring(locationStartIndex, locationEndIndex);
		}
		return url;
	}

	/*
	 * Returns true if specified string is a valid scheme name.
	 */
	private static boolean isValidScheme(final String scheme) {
		final int length = scheme.length();
		if (length < 1) {
			return false;
		}
		char c = scheme.charAt(0);
		if (!Character.isLetter(c)) {
			return false;
		}
		for (int i = 1; i < length; i++) {
			c = scheme.charAt(i);
			if (!Character.isLetterOrDigit(c) && (c != '.') && (c != '+') && (c != '-')) {
				return false;
			}
		}
		return true;
	}

	private static Url resolveUrl(final Url baseUrl, final String relativeUrl) {
		final Url url = parseUrl(relativeUrl);
		// Step 1: The base URL is established according to the rules of
		// Section 3. If the base URL is the empty string (unknown),
		// the embedded URL is interpreted as an absolute URL and
		// we are done.
		if (baseUrl == null) {
			return url;
		}
		// Step 2: Both the base and embedded URLs are parsed into their
		// component parts as described in Section 2.4.
		// a) If the embedded URL is entirely empty, it inherits the
		// entire base URL (i.e., is set equal to the base URL)
		// and we are done.
		if (relativeUrl.length() == 0) {
			return new Url(baseUrl);
		}
		// b) If the embedded URL starts with a scheme name, it is
		// interpreted as an absolute URL and we are done.
		if (url.scheme_ != null) {
			return url;
		}
		// c) Otherwise, the embedded URL inherits the scheme of
		// the base URL.
		url.scheme_ = baseUrl.scheme_;
		// Step 3: If the embedded URL's <net_loc> is non-empty, we skip to
		// Step 7. Otherwise, the embedded URL inherits the <net_loc>
		// (if any) of the base URL.
		if (url.location_ != null) {
			return url;
		}
		url.location_ = baseUrl.location_;
		// Step 4: If the embedded URL path is preceded by a slash "/", the
		// path is not relative and we skip to Step 7.
		if ((url.path_ != null) && url.path_.startsWith("/")) {
			url.path_ = removeLeadingSlashPoints(url.path_);
			return url;
		}
		// Step 5: If the embedded URL path is empty (and not preceded by a
		// slash), then the embedded URL inherits the base URL path,
		// and
		if (url.path_ == null) {
			url.path_ = baseUrl.path_;
			// a) if the embedded URL's <params> is non-empty, we skip to
			// step 7; otherwise, it inherits the <params> of the base
			// URL (if any) and
			if (url.parameters_ != null) {
				return url;
			}
			url.parameters_ = baseUrl.parameters_;
			// b) if the embedded URL's <query> is non-empty, we skip to
			// step 7; otherwise, it inherits the <query> of the base
			// URL (if any) and we skip to step 7.
			if (url.query_ != null) {
				return url;
			}
			url.query_ = baseUrl.query_;
			return url;
		}
		// Step 6: The last segment of the base URL's path (anything
		// following the rightmost slash "/", or the entire path if no
		// slash is present) is removed and the embedded URL's path is
		// appended in its place. The following operations are
		// then applied, in order, to the new path:
		final String basePath = baseUrl.path_;
		String path = new String();

		if (basePath != null) {
			final int lastSlashIndex = basePath.lastIndexOf('/');

			if (lastSlashIndex >= 0) {
				path = basePath.substring(0, lastSlashIndex + 1);
			}
		} else {
			path = "/";
		}
		path = path.concat(url.path_);
		// a) All occurrences of "./", where "." is a complete path
		// segment, are removed.
		int pathSegmentIndex;

		while ((pathSegmentIndex = path.indexOf("/./")) >= 0) {
			path = path.substring(0, pathSegmentIndex + 1).concat(path.substring(pathSegmentIndex + 3));
		}
		// b) If the path ends with "." as a complete path segment,
		// that "." is removed.
		if (path.endsWith("/.")) {
			path = path.substring(0, path.length() - 1);
		}
		// c) All occurrences of "<segment>/../", where <segment> is a
		// complete path segment not equal to "..", are removed.
		// Removal of these path segments is performed iteratively,
		// removing the leftmost matching pattern on each iteration,
		// until no matching pattern remains.
		while ((pathSegmentIndex = path.indexOf("/../")) > 0) {
			final String pathSegment = path.substring(0, pathSegmentIndex);
			final int slashIndex = pathSegment.lastIndexOf('/');

			if (slashIndex < 0) {
				continue;
			}
			if (!pathSegment.substring(slashIndex).equals("..")) {
				path = path.substring(0, slashIndex + 1).concat(path.substring(pathSegmentIndex + 4));
			}
		}
		// d) If the path ends with "<segment>/..", where <segment> is a
		// complete path segment not equal to "..", that
		// "<segment>/.." is removed.
		if (path.endsWith("/..")) {
			final String pathSegment = path.substring(0, path.length() - 3);
			final int slashIndex = pathSegment.lastIndexOf('/');

			if (slashIndex >= 0) {
				path = path.substring(0, slashIndex + 1);
			}
		}

		path = removeLeadingSlashPoints(path);

		url.path_ = path;
		// Step 7: The resulting URL components, including any inherited from
		// the base URL, are recombined to give the absolute form of
		// the embedded URL.
		return url;
	}

	private static String removeLeadingSlashPoints(String path) {
		while (path.startsWith("/..")) {
			path = path.substring(3);
		}

		return path;
	}

	/**
	 * Code copied from HtmlUnit
	 * (src/main/java/com/gargoylesoftware/htmlunit/TextUtil.java)
	 * (https://htmlunit.svn.sourceforge.net/svnroot/htmlunit/trunk/htmlunit -
	 * commit 5556)
	 */
	public static boolean startsWithIgnoreCase(final String stringToCheck, final String prefix) {

		if (prefix.length() == 0) {
			throw new IllegalArgumentException("Prefix may not be empty");
		}

		final int prefixLength = prefix.length();
		if (stringToCheck.length() < prefixLength) {
			return false;
		}
		return stringToCheck.substring(0, prefixLength).toLowerCase().equals(prefix.toLowerCase());
	}

	/**
	 * Code copied from HtmlUnit
	 * (src/main/java/com/gargoylesoftware/htmlunit/StringUtils.java)
	 * (https://htmlunit.svn.sourceforge.net/svnroot/htmlunit/trunk/htmlunit -
	 * commit 5556)
	 */
	public static int indexOf(final String s, final char searchChar, final int beginIndex, final int endIndex) {
		for (int i = beginIndex; i < endIndex; i++) {
			if (s.charAt(i) == searchChar) {
				return i;
			}
		}
		return -1;
	}

	private static class Url {

		private String scheme_;
		private String location_;
		private String path_;
		private String parameters_;
		private String query_;
		private String fragment_;

		public Url(final Url url) {
			scheme_ = url.scheme_;
			location_ = url.location_;
			path_ = url.path_;
			parameters_ = url.parameters_;
			query_ = url.query_;
			fragment_ = url.fragment_;
		}

		public Url() {
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();

			if (scheme_ != null) {
				sb.append(scheme_);
				sb.append(':');
			}
			if (location_ != null) {
				sb.append("//");
				sb.append(location_);
			}
			if (path_ != null) {
				sb.append(path_);
			}
			if (parameters_ != null) {
				sb.append(';');
				sb.append(parameters_);
			}
			if (query_ != null) {
				sb.append('?');
				sb.append(query_);
			}
			if (fragment_ != null) {
				sb.append('#');
				sb.append(fragment_);
			}
			return sb.toString();
		}
	}

}
