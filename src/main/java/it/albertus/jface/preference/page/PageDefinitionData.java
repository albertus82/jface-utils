package it.albertus.jface.preference.page;

import it.albertus.util.Localized;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

public class PageDefinitionData {

	private final String nodeId;
	private final Localized label;
	private final Class<? extends BasePreferencePage> pageClass;
	private final ImageDescriptor image;
	private final PageDefinition parent;

	public String getNodeId() {
		return nodeId;
	}

	public Localized getLabel() {
		return label;
	}

	public Class<? extends BasePreferencePage> getPageClass() {
		return pageClass;
	}

	public ImageDescriptor getImage() {
		return image;
	}

	public PageDefinition getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return "PageDefinitionData [" + (nodeId != null ? "nodeId=" + nodeId + ", " : "") + (label != null ? "label=" + label + ", " : "") + (pageClass != null ? "pageClass=" + pageClass + ", " : "") + (image != null ? "image=" + image + ", " : "") + (parent != null ? "parent=" + parent : "") + "]";
	}

	public static class PageDefinitionDataBuilder {
		private String nodeId;
		private Localized label;
		private Class<? extends BasePreferencePage> pageClass;
		private ImageDescriptor image;
		private PageDefinition parent;

		public PageDefinitionDataBuilder nodeId(final String nodeId) {
			this.nodeId = nodeId;
			return this;
		}

		public PageDefinitionDataBuilder label(final Localized label) {
			this.label = label;
			return this;
		}

		public PageDefinitionDataBuilder label(final String label) {
			this.label = new Localized() {
				@Override
				public String getString() {
					return label;
				}
			};
			return this;
		}

		public PageDefinitionDataBuilder pageClass(final Class<? extends BasePreferencePage> pageClass) {
			this.pageClass = pageClass;
			return this;
		}

		public PageDefinitionDataBuilder image(final ImageDescriptor imageDescriptor) {
			this.image = imageDescriptor;
			return this;
		}

		public PageDefinitionDataBuilder image(final Image image) {
			this.image = ImageDescriptor.createFromImage(image);
			return this;
		}

		public PageDefinitionDataBuilder image(final ImageData imageData) {
			this.image = ImageDescriptor.createFromImageData(imageData);
			return this;
		}

		public PageDefinitionDataBuilder parent(final PageDefinition parent) {
			this.parent = parent;
			return this;
		}

		public PageDefinitionData build() {
			return new PageDefinitionData(this);
		}
	}

	private PageDefinitionData(final PageDefinitionDataBuilder builder) {
		this.nodeId = builder.nodeId;
		this.label = builder.label;
		this.pageClass = builder.pageClass;
		this.image = builder.image;
		this.parent = builder.parent;
	}

}
