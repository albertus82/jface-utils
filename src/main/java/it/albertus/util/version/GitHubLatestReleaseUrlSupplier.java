package it.albertus.util.version;

import java.net.MalformedURLException;
import java.net.URL;

public class GitHubLatestReleaseUrlSupplier implements UrlSupplier {

	private String template = "https://api.github.com/repos/%s/%s/releases";

	private String user;
	private String repo;

	public GitHubLatestReleaseUrlSupplier(final String user, final String repo) {
		this.user = user;
		this.repo = repo;
	}

	@Override
	public URL get() throws MalformedURLException {
		return new URL(String.format(template, user, repo));
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public String getRepo() {
		return repo;
	}

	public void setRepo(final String repo) {
		this.repo = repo;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
