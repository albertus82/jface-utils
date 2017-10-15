package it.albertus.util.version;

import java.net.MalformedURLException;
import java.net.URL;

public interface UrlSupplier {

	URL get() throws MalformedURLException;

}
