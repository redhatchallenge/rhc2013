package org.redhatchallenge.rhc2013.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class WatermarkedTextBox extends TextBox implements BlurHandler, FocusHandler {

    private String watermark;
    private HandlerRegistration blurHandler;
    private HandlerRegistration focusHandler;

    public WatermarkedTextBox() {
        super();
    }

    public WatermarkedTextBox(String defaultValue) {
        this();
        setText(defaultValue);
    }

    public WatermarkedTextBox(String defaultValue, String watermark) {
        this(defaultValue);
        setWatermark(watermark);
    }

    public void setWatermark(final String watermark) {
        this.watermark = watermark;

        if((watermark != null) && (!watermark.equals(""))) {
            blurHandler = addBlurHandler(this);
            focusHandler = addFocusHandler(this);
            EnableWatermark();
        }

        else {
            blurHandler.removeHandler();
            focusHandler.removeHandler();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        EnableWatermark();
    }

    @Override
    public void onFocus(FocusEvent event) {
        if(getText().equalsIgnoreCase(watermark)) {
            setText("");
            this.removeStyleName("watermarkInput");
        }
    }

    private void EnableWatermark() {
        String text = getText();
        if ((text.length() == 0) || (text.equalsIgnoreCase(watermark)))
        {
            this.addStyleName("watermarkInput");
            setText(watermark);
        }
    }
}
