package io.github.albertus82.jface.preference.page;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * <p>
 * This is a very simple example of enum that implements
 * {@link io.github.albertus82.jface.preference.page.IPageDefinition
 * IPageDefinition}:
 * </p>
 * 
 * <pre>
 * public enum MyPageDefinition implements IPageDefinition {
 * 
 * 	TEXT(new PageDefinitionDetailsBuilder().nodeId("text").label("Text").build()),
 * 	TEXT_NUMERIC(new PageDefinitionDetailsBuilder().nodeId("text.numeric").parent(TEXT).label("Numeric").build()),
 * 	COMBO(new PageDefinitionDetailsBuilder().nodeId("combo").label("Combo").build()),
 * 	COMBO_NUMERIC(new PageDefinitionDetailsBuilder().nodeId("combo.numeric").parent(COMBO).label("Numeric").build()),
 * 	PAGE(new PageDefinitionDetailsBuilder().nodeId("page").label("Page").build()),
 * 	VARIOUS(new PageDefinitionDetailsBuilder().nodeId("various").label("Various").build());
 * 
 * 	private PageDefinitionDetails pageDefinitionDetails;
 * 
 * 	MyPageDefinition() {
 * 		this(new PageDefinitionDetailsBuilder().build());
 * 	}
 * 
 * 	MyPageDefinition(PageDefinitionDetails pageDefinitionDetails) {
 * 		this.pageDefinitionDetails = pageDefinitionDetails;
 * 	}
 * 
 * 	&#64;Override
 * 	public String getNodeId() {
 * 		return pageDefinitionDetails.getNodeId();
 * 	}
 * 
 * 	&#64;Override
 * 	public String getLabel() {
 * 		return pageDefinitionDetails.getLabel().get();
 * 	}
 * 
 * 	&#64;Override
 * 	public Class&lt;? extends BasePreferencePage&gt; getPageClass() {
 * 		return pageDefinitionDetails.getPageClass();
 * 	}
 * 
 * 	&#64;Override
 * 	public IPageDefinition getParent() {
 * 		return pageDefinitionDetails.getParent();
 * 	}
 * 
 * 	&#64;Override
 * 	public ImageDescriptor getImage() {
 * 		return pageDefinitionDetails.getImage();
 * 	}
 * }
 * </pre>
 * 
 * <p>
 * You can surely improve this code, for example introducing localization and
 * autodetermining {@code nodeId} values using the enum names. This example
 * makes use of
 * {@link io.github.albertus82.jface.preference.page.PageDefinitionDetails
 * PageDefinitionDetails} helper class and its builder.
 * </p>
 */
public interface IPageDefinition {

	String getNodeId();

	String getLabel();

	Class<? extends BasePreferencePage> getPageClass();

	ImageDescriptor getImage();

	IPageDefinition getParent();

}
