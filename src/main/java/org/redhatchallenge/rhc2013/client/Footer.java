package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.resources.Resources;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class Footer extends Composite {
    interface FooterUiBinder extends UiBinder<Widget, Footer> {
    }

    private static FooterUiBinder UiBinder = GWT.create(FooterUiBinder.class);

    public Footer() {
        Resources.INSTANCE.grid().ensureInjected();
        initWidget(UiBinder.createAndBindUi(this));

    }
}