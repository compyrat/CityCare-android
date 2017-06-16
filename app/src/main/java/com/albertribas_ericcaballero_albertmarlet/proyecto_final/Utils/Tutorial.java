package com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;

/**
 * Created by albertribgar on 02/06/2016.
 */
public class Tutorial extends Thread{

    private static ChainTourGuide mTourGuideHandler;
    private static ChainTourGuide[] arrayTutorials;
    private static Animation mEnterAnimation, mExitAnimation;
    public static void createTutorials(Activity a, int[] gravity, boolean[] Circ_Rect, String[] texts, View[] views){
        arrayTutorials = new ChainTourGuide[views.length];

        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(300);
        mEnterAnimation.setFillAfter(true);

        mExitAnimation = new AlphaAnimation(1f, 0f);
        mExitAnimation.setDuration(0);
        mExitAnimation.setFillAfter(true);

        for (int i = 0; i<views.length; i++){
            ToolTip tt = new ToolTip();

            tt.setTitle("Tutorial")
                    .setDescription(texts[i])
                    .setBackgroundColor(Color.parseColor("#c0392b"));
            switch(gravity[i]){
                case 1:tt.setGravity(Gravity.TOP | Gravity.LEFT);
                    break;
                case 2:tt.setGravity(Gravity.TOP |Gravity.CENTER);
                    break;
                case 3:tt.setGravity(Gravity.TOP |Gravity.RIGHT);
                    break;
                case 4:tt.setGravity(Gravity.CENTER |Gravity.LEFT);
                    break;
                case 5:tt.setGravity(Gravity.CENTER);
                    break;
                case 6:tt.setGravity(Gravity.CENTER |Gravity.RIGHT);
                    break;
                case 7:tt.setGravity(Gravity.BOTTOM |Gravity.LEFT);
                    break;
                case 8:tt.setGravity(Gravity.BOTTOM |Gravity.CENTER);
                    break;
                case 9:tt.setGravity(Gravity.BOTTOM |Gravity.RIGHT);
                    break;
                default:
                    break;
            }

            Overlay ol = new Overlay();
            ol.setBackgroundColor(Color.parseColor("#662c3eBB"))
                    .setEnterAnimation(mEnterAnimation)
                    .setExitAnimation(mExitAnimation);

            if (Circ_Rect[i]){
                ol.setStyle(Overlay.Style.Circle);
            }else{
                ol.setStyle(Overlay.Style.Rectangle);
            }
            Pointer p = new Pointer().setGravity(Gravity.BOTTOM);

            
            arrayTutorials[i] = ChainTourGuide.init(a)
                    .setPointer(p)
                    .setToolTip(tt)
                    .setOverlay(ol)
                    .playLater(views[i]);
        }

        Sequence sequence = new Sequence.SequenceBuilder()
                .add(arrayTutorials)
                .setDefaultOverlay(new Overlay()
                                .setEnterAnimation(mEnterAnimation)
                                .setExitAnimation(mExitAnimation)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mTourGuideHandler.next();
                                    }
                                })
                )
                .setDefaultPointer(null)
                .setContinueMethod(Sequence.ContinueMethod.OverlayListener)
                .build();
        mTourGuideHandler = ChainTourGuide.init(a).playInSequence(sequence);
    }
}
