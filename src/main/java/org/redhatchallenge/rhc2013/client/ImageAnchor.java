package org.redhatchallenge.rhc2013.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;

/**
 * Created with IntelliJ IDEA.
 * User: Jun
 * Date: 19/8/13
 * Time: 11:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageAnchor extends Anchor {
    public ImageAnchor(){

    }

    public void setResource(ImageResource imageResource) {
        Image img = new Image(imageResource);
        img.setStyleName("socialIcons");
        DOM.insertBefore(getElement(), img.getElement(), DOM.getFirstChild(getElement()));
    }
}
