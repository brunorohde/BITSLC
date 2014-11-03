package com.wordpress.brunorohde.bitslc.processing;

import processing.core.PApplet;


public class DrawStep {

	PApplet p5;
	
	float xInit;
	float yInit;
	float hInit;
	float deltaInit;
	
	float xSel;
	float xSelFim;

	
	
	DrawStep (PApplet p_apl) {
		p5 = p_apl;
		}
	
	public void desenhaFundo(float xpos, float ypos, float steps, boolean controleLoop){
	     
		float x = p5.width * xpos;
	    float y = p5.height * ypos;
	    float delta = p5.width * 0.7f / steps;
    	float h = p5.height * 0.25f;
    	
    	deltaInit = delta;
    	xInit = x;
    	yInit = y;
    	hInit = h;
    	
    	
	    for (float value = 0; value < steps; value++) {
	    	
	    	if (controleLoop == false){
		    	p5.fill(230);
		    	controleLoop = true;
	    	}
	    	
	    	else{
		    	p5.fill(200);
		    	controleLoop = false;
	    	}
	    	
	    	p5.rect(x, y, delta, h);
	    	x = x + delta;
	    	
	    	}    
	    }
	
	public void desenhaAtivo(float active) {
		
		float xAtual = (active - 1) * deltaInit + xInit;
		
		p5.pushMatrix();
		p5.pushStyle();
		p5.fill(p5.color(101, 133, 194));
		if (xAtual >= p5.width*.15f && xAtual < p5.width*.85f) {
			p5.rect(xAtual, yInit, deltaInit, hInit);
		}
		
		p5.popMatrix();
		p5.popStyle();
		
	}
	
	public void desenhaSel(float steps, float offset, float range){
		
		float largura = deltaInit;
		
		float xAtual =  offset * largura + xInit;
		float xFim = xAtual + (largura * range);
		float corrigeFim =  Math.abs(steps - (offset + range));
		
		xSel = xAtual;
		xSelFim = xFim;
		
		if(offset >= steps || xFim <= xInit){
			largura = 0;
		}
		
		if (xFim >= p5.width*.85f) {
			
			p5.pushMatrix();
			p5.pushStyle();
			p5.fill(p5.color(241, 90, 36, 60));
			p5.rect(xAtual, yInit, (largura * (range - corrigeFim)), hInit);
			p5.popMatrix();
			p5.popStyle();
			
		}
		
		if (xAtual < xInit) {
			
			p5.pushMatrix();
			p5.pushStyle();
			p5.fill(p5.color(241, 90, 36, 60));
			p5.rect(xInit, yInit, (largura * (range + offset)), hInit);
			p5.popMatrix();
			p5.popStyle();
			
		}
		
		if (xFim < p5.width*.85f && xAtual >= xInit) {
				
			p5.pushMatrix();
			p5.pushStyle();
			p5.fill(p5.color(241, 90, 36, 60));
			p5.rect(xAtual, yInit, (largura * range), hInit);
			p5.popMatrix();
			p5.popStyle();
			
		}
		
	}
	
	public float[] getSelection() {
		
		float[] tabela = new float[5];
		
		tabela[0] = xSel;
		tabela[1] = yInit;
		tabela[2] = xSelFim;
		tabela[3] = yInit + hInit;
		tabela[4] = deltaInit;
		
		return tabela;
		
	}
	
}
