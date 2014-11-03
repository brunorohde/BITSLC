package com.wordpress.brunorohde.bitslc.processing;

import controlP5.*;
import processing.core.PApplet;
import processing.core.PFont;


public class NumberSet {
	
	PApplet p5;
	ControlP5 cp5;
	
	float numberFinal;
	
	float initStore;
	
	boolean decimal = false;
	
	Button addButton;
	Button dimButton;
	Label numberView;
	
	
	NumberSet (PApplet p_apl, float Xpos, float Ypos, float min, float max, float init) {
		
		p5 = p_apl;
		cp5 = new ControlP5(p5);
		
		cp5.setAutoDraw(false);
		
		initStore = init;
		
		int w = p5.width/16;
		int h = p5.height/30;
		int i = p5.height/70;
		
		PFont font = p5.createFont("Verdana", p5.height/30, false);
		
		addButton = cp5.addButton("add")
		.setPosition(Xpos, Ypos)
        .setSize(w, h)
        .setLabelVisible(false)
        .plugTo(this)
		;
		
		numberView = cp5.addNumberbox("number")
        .setPosition(Xpos, Ypos + h + i)
        .setSize(w, h * 2)
        .setRange(min, max)
        .setMultiplier(1)
        .setDirection(Controller.HORIZONTAL)
        .plugTo(this)
        .setValue(init)
        .setCaptionLabel("")
        .setDecimalPrecision(0)
        .getValueLabel()
        .setFont(font)
        ;
		
		dimButton = cp5.addButton("dim")
		.setPosition(Xpos, (h * 2) + (Ypos + h + i) + i)
        .setSize(w, h)
        .setLabelVisible(false)
        .plugTo(this)
		;
		
		}
	
	NumberSet (PApplet p_apl, float Xpos, float Ypos, float min, float max, float init, boolean controle) {
		
		p5 = p_apl;
		cp5 = new ControlP5(p5);
		
		cp5.setAutoDraw(false);
		
		initStore = init;
		
		decimal = true;	
		
		int w = p5.width/16;
		int h = p5.height/30;
		int i = p5.height/70;
		
		PFont font = p5.createFont("Verdana", p5.height/30, false);
		
		
		addButton = cp5.addButton("add")
		.setPosition(Xpos, Ypos)
        .setSize(w, h)
        .setLabelVisible(false)
        .plugTo(this)
		;
		
		numberView = cp5.addNumberbox("number")
        .setPosition(Xpos, Ypos + h + i)
        .setSize(w, h * 2)
        .setRange(min, max)
        .setMultiplier(.01f)
        .setDirection(Controller.HORIZONTAL)
        .plugTo(this)
        .setValue(init)
        .setCaptionLabel("")
        .setDecimalPrecision(2)
        .getValueLabel()
        .setFont(font)
        ;
		
		dimButton = cp5.addButton("dim")
		.setPosition(Xpos, (h * 2) + (Ypos + h + i) + i)
        .setSize(w, h)
        .setLabelVisible(false)
        .plugTo(this)
		;
		
		}
	
	public void add (){
		
		if (decimal == false) {
		
			float actualNumber = cp5.getController("number").getValue() + 1;
			cp5.getController("number").setValue(actualNumber);
			
		}
		
		else {
			
			float actualNumber = cp5.getController("number").getValue() + .01f;
			cp5.getController("number").setValue(actualNumber);
			
		}
		
	}
	
	public void dim (){
		
		if (decimal == false) {
			
			float actualNumber = cp5.getController("number").getValue() - 1;
			cp5.getController("number").setValue(actualNumber);
			
		}
		
		else {
			
			float actualNumber = cp5.getController("number").getValue() - .01f;
			cp5.getController("number").setValue(actualNumber);
			
		}
		
	}
	
	public void number (){
		
		numberFinal = cp5.getController("number").getValue();
			
	}
	
	public void setNumber (float theNumber){
		
		cp5.getController("number").setValue(theNumber);
		
	}
	
	public float getNumber (){
		
		float actualNumber = initStore;
		
		if (cp5.getController("number").isMousePressed() == false) {
			
			actualNumber = numberFinal;
			initStore = numberFinal;
			
		}
		
		return actualNumber;
		
	}
	
	public void desNumberSet(){
		cp5.draw();
	}
	
}
