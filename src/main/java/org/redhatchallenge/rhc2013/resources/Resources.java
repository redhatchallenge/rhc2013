package org.redhatchallenge.rhc2013.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author: Terry Chia (Ayrx)
 */
public interface Resources extends ClientBundle {

    public static final Resources INSTANCE = GWT.create(Resources.class);

    @Source("css/960.css")
    Grid grid();

    @Source("css/buttons.css")
    Buttons buttons();

    @Source("css/global.css")
    Global global();

    @Source("css/main.css")
    Main main();

    @Source("css/prettyPhoto.css")
    PrettyPhoto prettyPhoto();

    @Source("css/webfonts.css")
    WebFonts webFonts();

    @Source("assets/css/styles.css")
    AssetStyles assetStyles();

    @Source("image/login_button.png")
    ImageResource loginButton();

    @Source("image/login_button_grey.png")
    ImageResource loginButtonGrey();

    @Source("image/reg_button.png")
    ImageResource registrationIcon();

    @Source("image/submit_button.png")
    ImageResource submitButton();

    @Source("image/submit_button_grey.png")
    ImageResource submitButtonGrey();

    @Source("image/saveprofile_button.png")
    ImageResource saveButton();

    @Source("image/saveprofile_button_grey.png")
    ImageResource saveButtonGrey();

    @Source("image/save_password_btn.png")
    ImageResource changePwdButton();

    @Source("image/save_password_btn_grey.png")
    ImageResource changePwdButtonGrey();

    @Source("image/socialbutton1.png")
    ImageResource socialButton1();

    @Source("image/socialButton2.png")
    ImageResource socialButton2();

    @Source("image/socialbutton11.png")
    ImageResource socialButton11();

    @Source("image/begin_button_el.png")
    ImageResource beginButtonEn();

    @Source("image/time_slot_closed.png")
    ImageResource timeslotClosed();

    @Source("image/bronze_trophy.png")
    ImageResource bronzeTrophy();

    @Source("image/silver_trophy.png")
    ImageResource silverTrophy();

    @Source("image/gold_trophy.png")
    ImageResource goldTrophy();

    public interface Grid extends CssResource {

        String grid_7();

        String push_9();

        String grid_8();

        String grid_5();

        String grid_6();

        String push_5();

        String grid_3();

        String push_6();

        String grid_4();

        String grid_1();

        String push_7();

        String push_8();

        String grid_2();

        String grid_10();

        String grid_11();

        String grid_12();

        String pull_10();

        String pull_12();

        String pull_11();

        String push_2();

        String push_1();

        String push_4();

        String push_3();

        String container_12();

        String suffix_7();

        String suffix_6();

        String suffix_9();

        String suffix_8();

        String suffix_3();

        String suffix_11();

        String suffix_2();

        String suffix_10();

        String suffix_5();

        String suffix_4();

        String omega();

        String suffix_1();

        String showborder();

        String clear();

        String pull_4();

        String pull_5();

        String pull_2();

        String pull_3();

        String pull_8();

        String pull_9();

        String pull_6();

        String pull_7();

        String alpha();

        String prefix_10();

        String pull_1();

        String prefix_11();

        String prefix_8();

        String push_12();

        String grid_9();

        String prefix_7();

        String prefix_6();

        String push_10();

        String prefix_5();

        String push_11();

        String prefix_9();

        String prefix_4();

        String prefix_3();

        String clearfix();

        String prefix_2();

        String prefix_1();
    }

    public interface Buttons extends CssResource {

        String orange();

        String red();

        String magenta();

        String awesome();

        String blue();

        String green();

        String yellow();

        String small();

        String medium();

        String large();
    }

    public interface Global extends CssResource {

    }

    public interface Main extends CssResource{

        String look();

        String myButton();

        String submit();

        String smallest();

        String larger();

        String rightcol();

        String leftcol();

        String block();

        String myButton2();

        String myButton3();

        String gray();

        String panel01();

        String smaller();

        String panel02();

        String largest();

        String red();

        String hide();

        @ClassName("key-bullets")
        String keyBullets();

        String goright();

        @ClassName("bg-grid")
        String bgGrid();

        String nowrap();

        String required();

        String goleft();

        @ClassName("social-icons")
        String socialIcons();

        String rowform();

        @ClassName("footer-nav-list")
        String footerNavList();

        @ClassName("sf-menu")
        String sfMenu();

        String sfHover();

        String first();

        @ClassName("nav-link")
        String navLink();

        @ClassName("nav-first")
        String navFirst();

        @ClassName("nav-band")
        String navBand();

        @ClassName("primary-nav")
        String primaryNav();

        String link();

        String textbox();

        @ClassName("gwt-Label")
        String gwtLabel();
    }

    public interface PrettyPhoto extends CssResource{

        String pp_nav();

        String pp_close();

        String pp_middle();

        String pp_next();

        String pp_details();

        String pp_default();

        String pp_description();

        String pp_content_container();

        String pp_gallery();

        String pp_top();

        String pp_hoverContainer();

        String pp_loaderIcon();

        String twitter();

        String selected();

        String light_rounded();

        String pp_pause();

        String pp_pic_holder();

        String facebook();

        String pp_overlay();

        String pp_arrow_next();

        String dark_rounded();

        String pp_content();

        String pp_social();

        String pp_expand();

        String light_square();

        String pp_previous();

        String ppt();

        String pp_inline();

        String pp_play();

        String pp_arrow_previous();

        String pp_left();

        @ClassName("default")
        String classDefault();

        String pp_fade();

        String pp_bottom();

        String pp_contract();

        String dark_square();

        String currentTextHolder();

        String disabled();

        String pp_right();
    }

    public interface WebFonts extends CssResource {

    }

    public interface AssetStyles extends CssResource {

        String note();
    }
}
