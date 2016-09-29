package it.albertus.jface.preference;

import it.albertus.jface.preference.page.BasePreferencePage;
import it.albertus.jface.preference.page.IPageDefinition;
import it.albertus.jface.preference.page.PageDefinitionDetails;
import it.albertus.jface.preference.page.PageDefinitionDetails.PageDefinitionDetailsBuilder;

import org.eclipse.jface.resource.ImageDescriptor;

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
		return pageDefinitionDetails.getLabel().getString();
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
