package com.comcast.xidio.listener;
/**
 * Interface to handle pay and pause functionality in the playing video
 * 
 * @author Dillip Lenka
 *
 */
public interface PlayPauseListener 
{

	void onPlay();
    void onPause();
    void onResume();
}
