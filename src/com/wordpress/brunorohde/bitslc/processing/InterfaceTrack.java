package com.wordpress.brunorohde.bitslc.processing;

import processing.core.PApplet;
import processing.core.PFont;
import controlP5.*;


public class InterfaceTrack {
	
	PApplet p5;
	ControlP5 cp5;
	
	Toggle trackOn;
	Toggle trackMode;
	NumberSet numberSetTrackBPM;
	NumberSet numberSetTrackDiv;
	NumberSet numberSetTrackPBSpeed;
	Toggle trackRev; 
	Slider trackFadeIn;
	Slider trackFadeOut;
	
	public InterfaceTrack (PApplet p_apl, float Xpos, float Ypos) {
	
		p5 = p_apl;
		cp5 = new ControlP5(p5);
		
		cp5.setAutoDraw(false);

		
		int l = p5.width/16;
		int a = p5.width/16;
		
		float interv = l + p5.width*.026f;
		
		
		trackOn = cp5.addToggle("TrackOn")
		.setPosition(Xpos, Ypos)
		.setSize(l, a)
		.setLabelVisible(false)
		.plugTo(this)
		.setValue(false)
		;
		
		trackMode = cp5.addToggle("TrackMode")
		.setPosition(Xpos + interv, Ypos)
		.setSize(l, a)
		.setLabelVisible(false)
		.setMode(ControlP5.SWITCH)
		.plugTo(this)
		.setValue(false)
		;
		
		numberSetTrackBPM = new NumberSet(p5, Xpos + (interv * 2), Ypos - (p5.height*.03f), 1, 300, 140);
		
		numberSetTrackDiv = new NumberSet(p5, Xpos + (interv * 3), Ypos - (p5.height*.03f), 1, 24, 1);
		
		numberSetTrackPBSpeed = new NumberSet(p5, Xpos + (interv * 4), Ypos - (p5.height*.03f), .01f, 2, 1, true);
		
		
		trackRev = cp5.addToggle("TrackRev")
		.setPosition(Xpos + (interv * 5), Ypos)
		.setSize(l, a)
		.setLabelVisible(false)
		.plugTo(this)
		.setValue(false)
		;
		
		trackFadeIn = cp5.addSlider("TrackFadeIn")
	    .setPosition(Xpos + (interv * 6), Ypos - (p5.height*.03f))
	    .setRange(0,1)
	    .setSize(p5.width/6, p5.height/14)
	    .setLabelVisible(false)
	    .setSliderMode(Slider.FLEXIBLE)
	    .setHandleSize(l/3)
	    .plugTo(this)
		.setValue(0)
	    ;  
	    
	    trackFadeOut = cp5.addSlider("TrackFadeOut")
	    .setPosition(Xpos + (interv * 6), Ypos + (p5.height*.058f))
	    .setRange(0,1)
	    .setSize(p5.width/6, p5.height/14)
	    .setLabelVisible(false)
	    .setSliderMode(Slider.FLEXIBLE)
	    .setHandleSize(l/3)
	    .plugTo(this)
		.setValue(1)
	    ;     
		
		}

	public void desIntTrack(){
		cp5.draw();
	}
	
}
