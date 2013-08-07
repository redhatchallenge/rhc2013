package org.ayrx.rhchallenge.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.ayrx.rhchallenge.resources.Resources;

/**
 * @author  Terry Chia (terrycwk1994@gmail.com)
 */
public class IndexScreen extends Composite {
    interface IndexScreenUiBinder extends UiBinder<Widget, IndexScreen> {
    }

    private static IndexScreenUiBinder UiBinder = GWT.create(IndexScreenUiBinder.class);

    @UiField Image registerImage;
    @UiField HTML challengeLink;

    public IndexScreen() {

        ScriptInjector.fromUrl("js/jquery-1.7.1.min.js").inject();

        Resources.INSTANCE.main().ensureInjected();
        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.buttons().ensureInjected();
        Resources.INSTANCE.assetStyles().ensureInjected();

        initWidget(UiBinder.createAndBindUi(this));

        // Sets cursor to indicate the image is clickable
        registerImage.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        challengeLink.setHTML("<h1><font color=\"#CC0000\">Take the Challenge Now!</font></h1>");
        challengeLink.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    }

    @UiHandler({"registerImage", "challengeLink"})
    public void handleClick(ClickEvent event) {
        ContentContainer.INSTANCE.setContent(new RegisterScreen());
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Jquery.countdown();
        Jquery.bind();
    }


}