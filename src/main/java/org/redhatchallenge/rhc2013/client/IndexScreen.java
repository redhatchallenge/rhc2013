package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.redhatchallenge.rhc2013.resources.Resources;
import org.redhatchallenge.rhc2013.shared.Student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author  Terry Chia (terrycwk1994@gmail.com)
 */
public class IndexScreen extends Composite {
    interface IndexScreenUiBinder extends UiBinder<Widget, IndexScreen> {
    }

    private static IndexScreenUiBinder UiBinder = GWT.create(IndexScreenUiBinder.class);
    private MessageMessages messages = GWT.create(MessageMessages.class);
    @UiField Image registerImage;
    @UiField HTML challengeLink;
    @UiField Anchor socialButton1;
    @UiField Anchor socialButton2;
    @UiField HTML videoOne;
    @UiField HTML videoTwo;
    @UiField HTML videoThree;

    @UiField HTML countChina500 ;
    private GetAllStudentServiceAsync getAllStudentService = null;
    private  List<Student> allStudent = new ArrayList<Student>();
    private  List<Student> chinaStudents = new ArrayList<Student>();

    public IndexScreen() {

        Resources.INSTANCE.main().ensureInjected();
        Resources.INSTANCE.grid().ensureInjected();
        Resources.INSTANCE.buttons().ensureInjected();
        Resources.INSTANCE.assetStyles().ensureInjected();

        initWidget(UiBinder.createAndBindUi(this));

        // Sets cursor to indicate the image is clickable
        registerImage.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        challengeLink.setHTML("<h1><font color=\"#CC0000\">" + messages.takeChallenge() + "!</font></h1>");
        challengeLink.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        if(LocaleInfo.getCurrentLocale().getLocaleName().equals("ch")) {
            socialButton1.setVisible(false);
            socialButton2.setTarget("_blank");
            socialButton2.setHref("http://e.weibo.com/redhatchina");
            videoOne.setHTML("<a href=\"http://player.youku.com/player.php/sid/XNTk5Mzk0OTgw/;iframe=true\" rel=\"prettyPhoto\"><img src=\"images/home_video_21_ch.jpg\" /></a>");
            videoTwo.setHTML("<a href=\"http://player.youku.com/player.php/sid/XMzQyMzc5MDYw/;iframe=true\" rel=\"prettyPhoto\"><img src=\"images/home_video_22_ch.jpg\" /></a>");
            videoThree.setHTML("<a href=\"http://you.video.sina.com.cn/api/sinawebApi/outplayrefer.php/vid=93232834_2298930370_P0q1RnNqDDXK+l1lHz2stqlF+6xCpv2xhGi2slqtJg5aUgyYJMXNb9QE4irUCMxG5yoUEJU2dvwl1RUubA;iframe=true\" rel=\"prettyPhoto\"><img src=\"images/home_video_23_ch.jpg\" /></a>");

            getAllStudentService = GetAllStudentService.Util.getInstance();

            getAllStudentService.getListOfStudents(new AsyncCallback<List<Student>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onSuccess(List< Student > students) {
                    allStudent = students;
                    for (Student s : allStudent){
                        if (s.getCountry().contains("China")){
                            chinaStudents.add(s);
                        }
                    }
                    if (chinaStudents.size() >= 2500){
                        int remainingSlot = 3000 - chinaStudents.size();
                        countChina500.setHTML("<FONT SIZE='5' COLOR='8f0203'>剩余报名数量: " + remainingSlot + "</FONT>");
                    }
                }
            });

        }

        else {
            socialButton1.setTarget("_blank");
            socialButton1.setHref("https://www.facebook.com/redhatinc?fref=ts");
            socialButton2.setTarget("_blank");
            socialButton2.setHref("https://twitter.com/red_hat_apac");

            videoOne.setHTML("<a href=\"http://www.youtube.com/watch?v=35ZKmL4OfeI\" rel=\"prettyPhoto\" title=\"\" id=\"vid2012\"><img src=\"images/home_video_21.jpg\" /></a>");
            videoTwo.setHTML("<a href=\"https://www.facebook.com/video/embed?video_id=10150591836889283&amp;iframe=true\" rel=\"prettyPhoto\"><img src=\"images/home_video_22.jpg\" /></a>");
            videoThree.setHTML("<a href=\"https://www.facebook.com/video/embed?video_id=10151393170889283&amp;iframe=true\" rel=\"prettyPhoto\"><img src=\"images/home_video_23.jpg\" /></a>");
        }

}

    @UiHandler({"registerImage", "challengeLink"})
    public void handleClick(ClickEvent event) {
        History.newItem("registration", true);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Jquery.countdown();
        if(LocaleInfo.getCurrentLocale().getLocaleName().equals("en")) {
            Jquery.bindEn(1382490000 - safeLongToInt(new Date().getTime()/1000));
        }

        else if(LocaleInfo.getCurrentLocale().getLocaleName().equals("ch")) {
            Jquery.bindCh(1382490000 - safeLongToInt(new Date().getTime()/1000));
        }
        else if(LocaleInfo.getCurrentLocale().getLocaleName().equals("zh")) {
            Jquery.bindCh(1382490000 - safeLongToInt(new Date().getTime()/1000));
        }
        Jquery.prettyPhoto();
    }

    private static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}