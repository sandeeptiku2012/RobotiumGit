package com.comcast.xideo;

import com.comcast.ui.libv1.widget.MenuLayoutEx.MenuItemEx;

public class MenuChangedEvent {
	Object source;
	MenuItemEx menuItem;

	public MenuChangedEvent(Object source, MenuItemEx menuItem) {
		this.source = source;
		this.menuItem = menuItem;
	}

	public Object getSource() {
		return source;
	}

	public MenuItemEx getMenuItem() {
		return menuItem;
	}
}

