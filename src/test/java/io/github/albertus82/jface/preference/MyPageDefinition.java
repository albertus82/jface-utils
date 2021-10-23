package io.github.albertus82.jface.preference;

import org.eclipse.jface.resource.ImageDescriptor;

import io.github.albertus82.jface.preference.page.BasePreferencePage;
import io.github.albertus82.jface.preference.page.IPageDefinition;
import io.github.albertus82.jface.preference.page.PageDefinitionDetails;
import io.github.albertus82.jface.preference.page.PageDefinitionDetails.PageDefinitionDetailsBuilder;

public enum MyPageDefinition implements IPageDefinition {

	TEXT(new PageDefinitionDetailsBuilder().nodeId("text").label("Text").build()),
	TEXT_NUMERIC(new PageDefinitionDetailsBuilder().nodeId("text.numeric").parent(TEXT).label("Numeric").build()),
	COMBO(new PageDefinitionDetailsBuilder().nodeId("combo").label("Combo").build()),
	COMBO_NUMERIC(new PageDefinitionDetailsBuilder().nodeId("combo.numeric").parent(COMBO).label("Numeric").build()),
	PAGE(new PageDefinitionDetailsBuilder().nodeId("page").label("Page").build()),
	VARIOUS(new PageDefinitionDetailsBuilder().nodeId("various").label("Various").build());

	private PageDefinitionDetails pageDefinitionDetails;

	MyPageDefinition() {
		this(new PageDefinitionDetailsBuilder().build());
	}

	MyPageDefinition(PageDefinitionDetails pageDefinitionDetails) {
		this.pageDefinitionDetails = pageDefinitionDetails;
	}

	@Override
	public String getNodeId() {
		return pageDefinitionDetails.getNodeId();
	}

	@Override
	public String getLabel() {
		return pageDefinitionDetails.getLabel().get();
	}

	@Override
	public Class<? extends BasePreferencePage> getPageClass() {
		return pageDefinitionDetails.getPageClass();
	}

	@Override
	public IPageDefinition getParent() {
		return pageDefinitionDetails.getParent();
	}

	@Override
	public ImageDescriptor getImage() {
		return pageDefinitionDetails.getImage();
	}

}
