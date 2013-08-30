package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.redhatchallenge.rhc2013.resources.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: gerald
 * Date: 8/30/13
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudyReferenceScreen extends Composite {

    interface StudyReferenceScreenUiBinder extends UiBinder<Widget, StudyReferenceScreen> {
    }

    private static StudyReferenceScreenUiBinder UiBinder = GWT.create(StudyReferenceScreenUiBinder.class);

    @UiField HTML Header;
    public StudyReferenceScreen(){
        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.main().ensureInjected();
        initWidget(UiBinder.createAndBindUi(this));


        if(LocaleInfo.getCurrentLocale().getLocaleName().equals("ch")) {
            Header.setHTML("<h1><font size='6' color='8F0203'>学习参考</font></h1>");


        }

        else if(LocaleInfo.getCurrentLocale().getLocaleName().equals("zh")) {
            Header.setHTML("<h1><font size='6' color='8F0203'>學習參考</font></h1>");
        }

        else{
            Header.setHTML("<h1><font size='6' color='8F0203'>Study Reference</font></h1>");
        }


    }


}
