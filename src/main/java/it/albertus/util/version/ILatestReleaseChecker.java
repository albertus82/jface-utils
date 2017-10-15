package it.albertus.util.version;

import java.io.IOException;
import java.net.Proxy;

public interface ILatestReleaseChecker {

	String check(UrlSupplier urlSupplier, Proxy proxy, LatestReleaseCallback callback) throws IOException;

}
