package org.redhatchallenge.rhc2013.client;

import com.google.gwt.core.client.GWT;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class Jquery {

    public static native void bindEn(int time) /*-{

        $wnd.jQuery(function(){
            var note = $wnd.$('#note'),
                ts = new Date(2012, 0, 1),
                newYear = true;

            if((new Date()) > ts){
                // The new year is here! Count towards something else.
                // Notice the *1000 at the end - time must be in milliseconds
                ts = (new Date()).getTime() + time*1000;
                newYear = false;
            }

            $wnd.$('#countdown').countdown({
                timestamp	: ts,
                callback	: function(days, hours, minutes, seconds){

                    var message = "";

                    message += days + " day" + ( days==1 ? '':'s' ) + ", ";
                    message += hours + " hour" + ( hours==1 ? '':'s' ) + ", ";
                    message += minutes + " minute" + ( minutes==1 ? '':'s' ) + " and ";
                    message += seconds + " second" + ( seconds==1 ? '':'s' ) + " <br />";

                    if(newYear){
                        message += "Congrats!";
                    }
                    else {
                        message += "Left to Round 1 of Red Hat Challenge 2013!";
                    }

                    note.html(message);
                }
            });

        });
    }-*/;

    public static native void bindCh(int time) /*-{

        $wnd.jQuery(function(){

            var note = $wnd.$('#note'),
                ts = new Date(2012, 0, 1),
                newYear = true;

            if((new Date()) > ts){
                // The new year is here! Count towards something else.
                // Notice the *1000 at the end - time must be in milliseconds
                ts = (new Date()).getTime() + time*1000;
                newYear = false;
            }

            $wnd.$('#countdown').countdown({
                timestamp	: ts,
                callback	: function(days, hours, minutes, seconds){

                    var message = "";

                    message += days + "天" + ( days==1 ? '':'' ) + ", ";
                    message += hours + " 小时" + ( hours==1 ? '':'' ) + ", ";
                    message += minutes + " 分钟" + ( minutes==1 ? '':'' ) + ", ";
                    message += seconds + " 秒" + ( seconds==1 ? '':'' ) + " <br />";

                    if(newYear){
                        message += "红帽挑战赛2013";
                    }
                    else {
                        message += "红帽挑战赛2013第一轮比赛倒数!";
                    }

                    note.html(message);
                }
            });

        });
    }-*/;

    public static native void bindZh(int time) /*-{

        $wnd.jQuery(function(){

            var note = $wnd.$('#note'),
                ts = new Date(2012, 0, 1),
                newYear = true;

            if((new Date()) > ts){
                // The new year is here! Count towards something else.
                // Notice the *1000 at the end - time must be in milliseconds
                ts = (new Date()).getTime() + time*1000;
                newYear = false;
            }

            $wnd.$('#countdown').countdown({
                timestamp	: ts,
                callback	: function(days, hours, minutes, seconds){

                    var message = "";

                    message += days + "天" + ( days==1 ? '':'' ) + ", ";
                    message += hours + " 小時" + ( hours==1 ? '':'' ) + ", ";
                    message += minutes + " 分鐘" + ( minutes==1 ? '':'' ) + ", ";
                    message += seconds + " 秒" + ( seconds==1 ? '':'' ) + " <br />";

                    if(newYear){
                        message += "紅帽挑戰賽2013";
                    }
                    else {
                        message += "紅帽挑戰賽2013第一輪比賽倒数!";
                    }

                    note.html(message);
                }
            });

        });
    }-*/;

    public static native void countdown() /*-{
        (function($){

            // Number of seconds in every time division
            var days	= 24*60*60,
                hours	= 60*60,
                minutes	= 60;

            // Creating the plugin
            $wnd.$.fn.countdown = function(prop){

                var options = $.extend({
                    callback	: function(){},
                    timestamp	: 0
                },prop);

                var left, d, h, m, s, positions;

                // Initialize the plugin
                init(this, options);

                positions = this.find('.position');

                (function tick(){

                    // Time left
                    left = Math.floor((options.timestamp - (new Date())) / 1000);

                    if(left < 0){
                        left = 0;
                    }

                    // Number of days left
                    d = Math.floor(left / days);
                    updateDuo(0, 1, d);
                    left -= d*days;

                    // Number of hours left
                    h = Math.floor(left / hours);
                    updateDuo(2, 3, h);
                    left -= h*hours;

                    // Number of minutes left
                    m = Math.floor(left / minutes);
                    updateDuo(4, 5, m);
                    left -= m*minutes;

                    // Number of seconds left
                    s = left;
                    updateDuo(6, 7, s);

                    // Calling an optional user supplied callback
                    options.callback(d, h, m, s);

                    // Scheduling another call of this function in 1s
                    setTimeout(tick, 1000);
                })();

                // This function updates two digit positions at once
                function updateDuo(minor,major,value){
                    switchDigit(positions.eq(minor),Math.floor(value/10)%10);
                    switchDigit(positions.eq(major),value%10);
                }

                return this;
            };


            function init(elem, options){
                elem.addClass('countdownHolder');

                // Creating the markup inside the container
                $wnd.$.each(['Days','Hours','Minutes','Seconds'],function(i){
                    $wnd.$('<span class="count'+this+'">').html(
                        '<span class="position">\
                            <span class="digit static">0</span>\
                        </span>\
                        <span class="position">\
                            <span class="digit static">0</span>\
                        </span>'
                    ).appendTo(elem);

                    if(this!="Seconds"){
                        elem.append('<span class="countDiv countDiv'+i+'"></span>');
                    }
                });

            }

            // Creates an animated transition between the two numbers
            function switchDigit(position,number){

                var digit = position.find('.digit')

                if(digit.is(':animated')){
                    return false;
                }

                if(position.data('digit') == number){
                    // We are already showing this number
                    return false;
                }

                position.data('digit', number);

                var replacement = $('<span>',{
                    'class':'digit',
                    css:{
                        top:'-2.1em',
                        opacity:0
                    },
                    html:number
                });

                // The .static class is added when the animation
                // completes. This makes it run smoother.

                digit
                    .before(replacement)
                    .removeClass('static')
                    .animate({top:'2.5em',opacity:0},'fast',function(){
                        digit.remove();
                    })

                replacement
                    .delay(100)
                    .animate({top:0,opacity:1},'fast',function(){
                        replacement.addClass('static');
                    });
            }
        })($wnd.jQuery);
    }-*/;

    public static native void prettyPhoto() /*-{
        $wnd.$(document).ready(function(){
            $wnd.$("a[rel^='prettyPhoto']").prettyPhoto({
                allow_resize: true
            });
        });
    }-*/;

    public static native void facebookShareEn() /*-{
        $wnd.FB.ui({
            method: 'feed',
            link: 'redhatchallenge2013-rhc2013.rhcloud.com/?locale=en#registration',
            picture: 'https://redhatchallenge2013-rhc2013.rhcloud.com/images/redhathome_logo_thumbnail.png',
            name: 'Red Hat Challenge 2013',
            caption: 'Join Red Hat Challenge 2013 Now!!',
            message: 'Facebook%20Dialogs%20are%20so%20easy!',
            description: 'Looking for a stage to showcase your knowledge on cloud computing, operating systems and virtualization? Join the Red Hat Challenge 2013!.'
        }, function(response){});
    }-*/;

    public static native void facebookShareZh() /*-{
        $wnd.FB.ui({
            method: 'feed',
             link: 'redhatchallenge2013-rhc2013.rhcloud.com/?locale=zh#registration',
            picture: 'https://redhatchallenge2013-rhc2013.rhcloud.com/images/redhathome_logo_thumbnail.png',
            name: 'Red Hat Challenge 2013',
            caption: 'Join Red Hat Challenge 2013 Now!!',
            description: 'Looking for a stage to showcase your knowledge on cloud computing, operating systems and virtualization? Join the Red Hat Challenge 2013!.'
        }, function(response){});
    }-*/;

    public static native boolean checkIfTwitterWidgetIsLoaded() /*-{
        if(!(typeof $wnd.twtrr === "undefined") && !(null===$wnd.twtrr)) {
            return true;
        }

        return false;
    }-*/;
}
