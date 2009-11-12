package net.vidageek.crawler;

/**
 * @author jonasabreu
 * 
 */
final public class Url {

    private final String link;
    private final int depth;

    public Url(final String link, final int depth) {
        this.link = link;
        this.depth = depth;
    }

    public int depth() {
        return depth;
    }

    public String link() {
        return link;
    }

    @Override
    public String toString() {
        return "[" + link + "] at depth " + depth;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Url other = (Url) obj;
        if (link == null) {
            if (other.link != null) {
                return false;
            }
        } else if (!link.equals(other.link)) {
            return false;
        }
        return true;
    }

}
