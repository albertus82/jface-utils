package it.albertus.jface;

import org.junit.Assert;
import org.junit.Test;

public class EventsTest {

	@Test
	public void testEventNames() {
		Assert.assertEquals("None", Events.getName(0));
		Assert.assertEquals("KeyDown", Events.getName(1));
		Assert.assertEquals("KeyUp", Events.getName(2));
		Assert.assertEquals("MouseDown", Events.getName(3));
		Assert.assertEquals("MouseUp", Events.getName(4));
		Assert.assertEquals("MouseMove", Events.getName(5));
		Assert.assertEquals("MouseEnter", Events.getName(6));
		Assert.assertEquals("MouseExit", Events.getName(7));
		Assert.assertEquals("MouseDoubleClick", Events.getName(8));
		Assert.assertEquals("Paint", Events.getName(9));
		Assert.assertEquals("Move", Events.getName(10));
		Assert.assertEquals("Resize", Events.getName(11));
		Assert.assertEquals("Dispose", Events.getName(12));
		Assert.assertEquals("Selection", Events.getName(13));
		Assert.assertEquals("DefaultSelection", Events.getName(14));
		Assert.assertEquals("FocusIn", Events.getName(15));
		Assert.assertEquals("FocusOut", Events.getName(16));
		Assert.assertEquals("Expand", Events.getName(17));
		Assert.assertEquals("Collapse", Events.getName(18));
		Assert.assertEquals("Iconify", Events.getName(19));
		Assert.assertEquals("Deiconify", Events.getName(20));
		Assert.assertEquals("Close", Events.getName(21));
		Assert.assertEquals("Show", Events.getName(22));
		Assert.assertEquals("Hide", Events.getName(23));
		Assert.assertEquals("Modify", Events.getName(24));
		Assert.assertEquals("Verify", Events.getName(25));
		Assert.assertEquals("Activate", Events.getName(26));
		Assert.assertEquals("Deactivate", Events.getName(27));
		Assert.assertEquals("Help", Events.getName(28));
		Assert.assertEquals("DragDetect", Events.getName(29));
		Assert.assertEquals("Arm", Events.getName(30));
		Assert.assertEquals("Traverse", Events.getName(31));
		Assert.assertEquals("MouseHover", Events.getName(32));
		Assert.assertEquals("HardKeyDown", Events.getName(33));
		Assert.assertEquals("HardKeyUp", Events.getName(34));
		Assert.assertEquals("MenuDetect", Events.getName(35));
		Assert.assertEquals("SetData", Events.getName(36));
		Assert.assertEquals("MouseVerticalWheel", Events.getName(37));
		Assert.assertEquals("MouseHorizontalWheel", Events.getName(38));
		Assert.assertEquals("Settings", Events.getName(39));
		Assert.assertEquals("EraseItem", Events.getName(40));
		Assert.assertEquals("MeasureItem", Events.getName(41));
		Assert.assertEquals("PaintItem", Events.getName(42));
		Assert.assertEquals("ImeComposition", Events.getName(43));
		Assert.assertEquals("OrientationChange", Events.getName(44));
		Assert.assertEquals("Skin", Events.getName(45));
		Assert.assertEquals("OpenDocument", Events.getName(46));
		Assert.assertEquals("Touch", Events.getName(47));
		Assert.assertEquals("Gesture", Events.getName(48));
		Assert.assertEquals("Segments", Events.getName(49));
	}

}
