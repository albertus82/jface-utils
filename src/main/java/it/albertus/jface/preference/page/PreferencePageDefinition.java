package it.albertus.jface.preference.page;

import it.albertus.util.Localized;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

public class PreferencePageDefinition implements IPreferencePageDefinition {

	private String nodeId;
	private Localized label;
	private Class<? extends BasePreferencePage> pageClass;
	private ImageDescriptor image;
	private IPreferencePageDefinition parent;

	public PreferencePageDefinition() {}

	@Override
	public final String getNodeId() {
		return nodeId;
	}

	public final void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public final Localized getLabel() {
		return label;
	}

	public final void setLabel(Localized label) {
		this.label = label;
	}

	@Override
	public final Class<? extends BasePreferencePage> getPageClass() {
		return pageClass;
	}

	public final void setPageClass(Class<? extends BasePreferencePage> pageClass) {
		this.pageClass = pageClass;
	}

	@Override
	public final ImageDescriptor getImage() {
		return image;
	}

	public final void setImage(ImageDescriptor image) {
		this.image = image;
	}

	@Override
	public final IPreferencePageDefinition getParent() {
		return parent;
	}

	public final void setParent(IPreferencePageDefinition parent) {
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
		if (!(obj instanceof PreferencePageDefinition)) {
			return false;
		}
		PreferencePageDefinition other = (PreferencePageDefinition) obj;
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
		return "PreferencePageDefinition [" + (nodeId != null ? "nodeId=" + nodeId + ", " : "") + (label != null ? "label=" + label + ", " : "") + (pageClass != null ? "pageClass=" + pageClass + ", " : "") + (image != null ? "image=" + image + ", " : "") + (parent != null ? "parent=" + parent : "") + "]";
	}

	public static class PreferencePageDefinitionBuilder {
		private String nodeId;
		private Localized label;
		private Class<? extends BasePreferencePage> pageClass;
		private ImageDescriptor image;
		private IPreferencePageDefinition parent;

		public PreferencePageDefinitionBuilder nodeId(final String nodeId) {
			this.nodeId = nodeId;
			return this;
		}

		public PreferencePageDefinitionBuilder label(final Localized label) {
			this.label = label;
			return this;
		}

		public PreferencePageDefinitionBuilder label(final String label) {
			this.label = new Localized() {
				@Override
				public String getString() {
					return label;
				}
			};
			return this;
		}

		public PreferencePageDefinitionBuilder pageClass(final Class<? extends BasePreferencePage> pageClass) {
			this.pageClass = pageClass;
			return this;
		}

		public PreferencePageDefinitionBuilder image(final ImageDescriptor imageDescriptor) {
			this.image = imageDescriptor;
			return this;
		}

		public PreferencePageDefinitionBuilder image(final Image image) {
			this.image = ImageDescriptor.createFromImage(image);
			return this;
		}

		public PreferencePageDefinitionBuilder image(final ImageData imageData) {
			this.image = ImageDescriptor.createFromImageData(imageData);
			return this;
		}

		public PreferencePageDefinitionBuilder parent(final IPreferencePageDefinition parent) {
			this.parent = parent;
			return this;
		}

		public PreferencePageDefinition build() {
			return new PreferencePageDefinition(this);
		}
	}

	private PreferencePageDefinition(final PreferencePageDefinitionBuilder builder) {
		this.nodeId = builder.nodeId;
		this.label = builder.label;
		this.pageClass = builder.pageClass;
		this.image = builder.image;
		this.parent = builder.parent;
	}

}
