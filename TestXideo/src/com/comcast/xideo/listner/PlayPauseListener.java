package com.comcast.xideo.listner;
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
