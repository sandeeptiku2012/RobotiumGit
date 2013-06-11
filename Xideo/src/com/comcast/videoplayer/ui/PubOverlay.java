/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 *  Copyright 2012 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/

package com.comcast.videoplayer.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comcast.xideo.R;

public class PubOverlay {

    private final int MS_IN_SECOND = 1000;

    private final ViewGroup container;
    private final TextView txtAdRemainingTime;
    private final TextView txtRemainingAdsInBreak;

    private long currentAdBreakSize;
    private long currentAdTime;
    private long currentAdIndex;
    private long currentAdDuration;

    public PubOverlay(ViewGroup container) {
        this.container = container;
        txtAdRemainingTime = (TextView) container.findViewById(R.id.txtAdRemainingTime);
        txtRemainingAdsInBreak = (TextView) container.findViewById(R.id.txtRemainingAdsInBreak);

        hide();
    }

    private void hide() {
        this.container.setVisibility(View.INVISIBLE);
    }

    private void show() {
        this.container.setVisibility(View.VISIBLE);
    }

    private void showAdProgress() {
        txtRemainingAdsInBreak.setVisibility(View.VISIBLE);

        // TODO: Change this back to VISIBLE once the remaining time reporting is fixed
        txtAdRemainingTime.setVisibility(View.INVISIBLE);
    }

    private void hideAdProgress() {
        txtRemainingAdsInBreak.setVisibility(View.INVISIBLE);
        txtAdRemainingTime.setVisibility(View.INVISIBLE);
    }
}
