package com.wordpress.brunorohde.bitslc.processing;



import processing.core.PApplet;
import controlP5.*;


public class InterfaceMaster {
	
	PApplet p5;
	ControlP5 cp5;
	
	InterfaceTrack intTrackA;
	InterfaceTrack intTrackB;
	
	Toggle onMaster;
	NumberSet numberSetMasterSteps;
	NumberSet numberSetMasterBPM;
	Slider volMaster;
	Slider mixerMaster;
	Button openA;
	Button openB;
	Toggle recSession;
	Button helpButton;
	
	
	public InterfaceMaster (PApplet p_apl, float Xpos) {
	
		p5 = p_apl;
		cp5 = new ControlP5(p5);
		
		cp5.setAutoDraw(false);
		
		float y = p5.height/12f;
		
		
		intTrackA = new InterfaceTrack(p5, p5.width*.15f, p5.height/12f);
		intTrackB = new InterfaceTrack(p5, p5.width*.15f, (p5.height*.75f) + (p5.height/15f));
		
		
		onMaster = cp5.addToggle("On_Master")
		.setPosition(Xpos, y)
		.setSize(p5.width/16, p5.width/16)
		.setLabelVisible(false)
		.plugTo(this)
		.setValue(false)
		;
				
		numberSetMasterSteps = new NumberSet(p5, Xpos, (y * 3), 1, 64, 16);
		
		numberSetMasterBPM = new NumberSet(p5, Xpos, (y * 5.5f), 1, 300, 140);
		

		volMaster = cp5.addSlider("Volume_Master")
        .setPosition(Xpos, y * 8)
        .setSize(p5.width/16, p5.width/6)
        .setRange(0, 1)
        .setDecimalPrecision(10)
        .setLabelVisible(false)
        .plugTo(this)
        .setValue(.8f)
        ;
		
		openA = cp5.addButton("Open_A")
		.setPosition((p5.width - (p5.width/8) + (p5.width/32f)), (p5.height/12f) - (p5.height*.03f))
		.setSize(p5.width/16, p5.height/14)
		.setLabelVisible(false)
        .plugTo(this)
		;
		
		openB = cp5.addButton("Open_B")
		.setPosition((p5.width - (p5.width/8) + (p5.width/32f)), (p5.height/12f) + (p5.height*.058f))
		.setSize(p5.width/16, p5.height/14)
		.setLabelVisible(false)
        .plugTo(this)
		;
    
		mixerMaster = cp5.addSlider("Mixer")
        .setPosition((p5.width - (p5.width/8) + (p5.width/32f)), p5.height/4f)
        .setSize(p5.width/16, p5.height/2)
        .setRange(0,1)
        .setSliderMode(Slider.FLEXIBLE)
        .setHandleSize(p5.height/18)
        .setLabelVisible(false)
        .plugTo(this)
        .setValue(.5f)
        ;
		
		recSession = cp5.addToggle("Rec_Session")
		.setPosition((p5.width - (p5.width/8) + (p5.width/32f)), (p5.height*.75f) + (p5.height/15f) - (p5.height*.03f))
		.setSize(p5.width/16, p5.height/14)
		.setLabelVisible(false)
		.plugTo(this)
		.setState(false)
		;
				
		helpButton = cp5.addButton("Help_Button")
		.setPosition((p5.width - (p5.width/8) + (p5.width/32f)), (p5.height*.75f) + (p5.height/15f) + (p5.height*.058f))
		.setSize(p5.width/16, p5.height/14)
		.setLabelVisible(false)
        .plugTo(this)
		;
		
		}
	
	public void On_Master(boolean theValue){
		
		intTrackA.trackOn.setState(theValue);
		intTrackB.trackOn.setState(theValue);
		
	}
	
	public void desIntMaster(){
		cp5.draw();
	}
	
}
