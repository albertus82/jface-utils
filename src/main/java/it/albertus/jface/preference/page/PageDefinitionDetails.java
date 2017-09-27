package it.albertus.jface.preference.page;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import it.albertus.util.ISupplier;
import it.albertus.util.Localized;

public class PageDefinitionDetails {

	private String nodeId;
	private ISupplier<String> label;
	private Class<? extends BasePreferencePage> pageClass;
	private ImageDescriptor image;
	private IPageDefinition parent;

	protected PageDefinitionDetails() {}

	private PageDefinitionDetails(final PageDefinitionDetailsBuilder builder) {
		this.nodeId = builder.nodeId;
		this.label = builder.label;
		this.pageClass = builder.pageClass;
		this.image = builder.image;
		this.parent = builder.parent;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(final String nodeId) {
		this.nodeId = nodeId;
	}

	public ISupplier<String> getLabel() {
		return label;
	}

	public void setLabel(final ISupplier<String> label) {
		this.label = label;
	}

	public Class<? extends BasePreferencePage> getPageClass() {
		return pageClass;
	}

	public void setPageClass(final Class<? extends BasePreferencePage> pageClass) {
		this.pageClass = pageClass;
	}

	public ImageDescriptor getImage() {
		return image;
	}

	public void setImage(final ImageDescriptor image) {
		this.image = image;
	}

	public IPageDefinition getParent() {
		return parent;
	}

	public void setParent(IPageDefinition parent) {
		this.parent = parent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PageDefinitionDetails)) {
			return false;
		}
		PageDefinitionDetails other = (PageDefinitionDetails) obj;
		if (nodeId == null) {
			if (other.nodeId != null) {
				return false;
			}
		}
		else if (!nodeId.equals(other.nodeId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PageDefinitionDetails [nodeId=" + nodeId + ", label=" + label + ", pageClass=" + pageClass + ", image=" + image + ", parent=" + parent + "]";
	}

	public static class PageDefinitionDetailsBuilder {
		private String nodeId;
		private ISupplier<String> label;
		private Class<? extends BasePreferencePage> pageClass;
		private ImageDescriptor image;
		private IPageDefinition parent;

		public PageDefinitionDetailsBuilder nodeId(final String nodeId) {
			this.nodeId = nodeId;
			return this;
		}

		public PageDefinitionDetailsBuilder label(final ISupplier<String> label) {
			this.label = label;
			return this;
		}

		public PageDefinitionDetailsBuilder label(final String label) {
			this.label = new Localized() {
				@Override
				public String getString() {
					return label;
				}
			};
			return this;
		}

		public PageDefinitionDetailsBuilder pageClass(final Class<? extends BasePreferencePage> pageClass) {
			this.pageClass = pageClass;
			return this;
		}

		public PageDefinitionDetailsBuilder image(final ImageDescriptor imageDescriptor) {
			this.image = imageDescriptor;
			return this;
		}

		public PageDefinitionDetailsBuilder image(final Image image) {
			this.image = ImageDescriptor.createFromImage(image);
			return this;
		}

		public PageDefinitionDetailsBuilder image(final ImageData imageData) {
			this.image = ImageDescriptor.createFromImageData(imageData);
			return this;
		}

		public PageDefinitionDetailsBuilder parent(final IPageDefinition parent) {
			this.parent = parent;
			return this;
		}

		public PageDefinitionDetails build() {
			return new PageDefinitionDetails(this);
		}
	}

}
