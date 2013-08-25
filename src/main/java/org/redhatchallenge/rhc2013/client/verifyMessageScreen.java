package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.resources.Resources;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class verifyMessageScreen extends Composite {
    interface MessageScreenUiBinder extends UiBinder<Widget, verifyMessageScreen> {
    }

    private static MessageScreenUiBinder UiBinder = GWT.create(MessageScreenUiBinder.class);
    private static MessageMessages messages = GWT.create(MessageMessages.class);

    @UiField HTML messageLabel;
    @UiField Image socialButton1;
    @UiField HTML socialButton2;
    @UiField HTML socialButton3;

    public verifyMessageScreen(String message) {
        initWidget(UiBinder.createAndBindUi(this));
        messageLabel.setHTML(message);

        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.main().ensureInjected();

        socialButton1.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        socialButton2.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        socialButton3.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        injectTwitterScript();

        if(LocaleInfo.getCurrentLocale().getLocaleName().equals("en")) {
            ClickHandler handlerEn = new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Jquery.facebookShareEn();
                }
            };

            socialButton1.setResource(Resources.INSTANCE.socialButton11());
            socialButton1.addClickHandler(handlerEn);

            socialButton2.setHTML("<a href=\"https://twitter.com/share\" class=\"twitter-share-button\" data-count=\"none\" data-text=\"Join Red Hat Challenge 2013 now!\" data-lang=\"en\">Tweet</a>");
        }

        else if(LocaleInfo.getCurrentLocale().getLocaleName().equals("ch")) {
            ClickHandler handlerChWeibo = new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Window.open("http://service.weibo.com/share/share.php?url=https://redhatchallenge2013-rhc2013.rhcloud.com&title=我报名参加了红帽挑战赛2013. 你也一起参加吧， 我们可能可以一起到北京参加大决赛喔。快报名！\n https://redhatchallenge2013-rhc2013.rhcloud.com/?locale=ch#registration&pic=&language=zh_cn", "weibo-share-dialog", "width=626,height=436");
                }
            };

            socialButton3.setHTML("<img src=images/socialButton2_ch.png>");
            socialButton3.addClickHandler(handlerChWeibo);
        }

        else if(LocaleInfo.getCurrentLocale().getLocaleName().equals("zh")) {
            ClickHandler handlerZh = new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Jquery.facebookShareZh();
                }
            };

            socialButton1.setResource(Resources.INSTANCE.socialButton11());
            socialButton1.addClickHandler(handlerZh);

            socialButton2.setHTML("<a href=\"https://twitter.com/share\" class=\"twitter-share-button\" data-count=\"none\" data-text=\"Join Red Hat Challenge 2013 now!\" data-lang=\"en\">Tweet</a>");
        }
    }

    private void injectTwitterScript() {
        Document doc = Document.get();
        ScriptElement script = doc.createScriptElement();
        script.setSrc("https://platform.twitter.com/widgets.js");
        script.setType("text/javascript");
        script.setLang("javascript");
        doc.getBody().appendChild(script);
    }
}