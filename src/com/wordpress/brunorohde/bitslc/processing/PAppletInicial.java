package com.wordpress.brunorohde.bitslc.processing;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.puredata.core.PdBase;

import controlP5.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import processing.core.PApplet;
import processing.core.PGraphics;
import com.wordpress.brunorohde.bitslc.multitouch.MTListenerCallBack;
import com.wordpress.brunorohde.bitslc.multitouch.MultiTouchP;
import com.wordpress.brunorohde.bitslc.pdstuff.PdListenerCallBack;
import com.wordpress.brunorohde.bitslc.pdstuff.PureDataManager;


public class PAppletInicial extends PApplet implements PdListenerCallBack,MTListenerCallBack {
	
	private static final String TAG = "BEATSLICER";
		
	String stringVal;
	float numeroFloat;
	PureDataManager pdManager;
	MultiTouchP multiTouch;
	BufferDraw bufferDraw1;
	BufferDraw bufferDraw2;
	DrawStep stepA;
	DrawStep stepB;
	InterfaceMaster intMaster;


	ControlP5 cp5;
    
	PGraphics pg;

	
	public final int bgcolor = color(180);
    public final int bgcolor2 = 0;
	
    public float activeA = 0;
    public float activeB = 0;
    
    public float steps = 16;
    
    public float actualMasterBPM = 140;
    
    public boolean helpView = false;
    
    public String audioTrackA;
    public String audioTrackB;
    
    public float offsetA = 0;
	public float rangeA = 8;
	public float offsetB = 8;
	public float rangeB = 8;
	
	public float stepWidth;
	
	public float movInA = 0;
	public float movInB = 0;
	
	public float movOutA = 0;
	public float movOutB = 0;
	
	public float[][] touchTrackA = new float[10][2];
	public float[][] touchTrackB = new float[10][2];
	
	public int[] touchSelA = {0,0,0,0,0,0,0,0,0,0};
	public int[] touchSelB = {0,0,0,0,0,0,0,0,0,0};
	
	public String saveFilePath;
	public String saveFileName;
	
		
/////////////////////////////////////////////
    
	public void setup(){

		multiTouch = new MultiTouchP(this);
		
		textSize(50);
		noStroke();
	    noSmooth();
		
	    apagaTela();
		desenhaBG();
	    
		cp5 = new ControlP5(this);
		
		stepA = new DrawStep(this);
	    stepB = new DrawStep(this);
	    
		desenhaSteps();
		
		desenhaOnda();
	    
		
		intMaster = new InterfaceMaster(this, width/32f);
		
		
		//////////////////////////////////////// Define camada semitransparente de ajuda, renderizada por cima de tudo
		
		pg = createGraphics(width, height);
		pg.beginDraw();
		pg.background(0,180);
		pg.fill(255);
		pg.textSize(height/35);
		pg.textAlign(CENTER, CENTER);
		pg.rect((width - (width/8) + (width/32f)), (height*.75f) + (height/15f) + (height*.058f), width/16, height/14);
		pg.text("Liga A / B\nSYNC", (width/32f)+(width/32f), (height/12f)+(width/32));
		pg.text("Fatias", (width/32f)+(width/32f), (height/4f)+(width/20));
		pg.text("BPM Master", (width/32f)+(width/32f), (height/2f)+(width/42));
		pg.text("Volume\nGeral", (width/32f)+(width/32f), (height*.75f) + (height/20f));
		pg.text("Liga A", (width*.15f)+(width/32f), (height/12f)+(width/32));
		pg.text("Random A", (width*.15f)+(width/32f)+((width/16)*1.41f), (height/12f)+(width/32)); 
		pg.text("BPM A", (width*.15f)+(width/32f)+((width/16)*2.83f), (height/12f)+(width/32)); 
		pg.text("Divisão\nBPM A", (width*.15f)+((width/16)*4.75f), (height/12f)+(width/32)); 
		pg.text("Transpor\nFatia A", (width*.15f)+((width/16)*6.17f), (height/12f)+(width/32));
		pg.text("Inverte\nFatia A", (width*.15f)+((width/16)*7.59f), (height/12f)+(width/32));
		pg.text("Fade In Fatia A", (width*.15f)+((width/16)*9.83f), (height/11.3f));
		pg.text("Fade Out Fatia A", (width*.15f)+((width/16)*9.83f), (height/5.7f));
		pg.text("Escolhe\nÁudio A", (width - (width/8) + (width/16f)), (height/11.3f));
		pg.text("Escolhe\nÁudio B", (width - (width/8) + (width/16f)), (height/5.7f));
		pg.text("Mixer\nA - B", (width - (width/8) + (width/16f)), (height/2f));
		pg.text("Grava\nÁudio", (width - (width/8) + (width/16f)), (height*.822f));
		pg.pushStyle();
		pg.textSize(height/25);
		pg.text("PISTA A", (width/2), (height*0.33f));
		pg.text("PISTA B", (width/2), (height*0.66f));
		pg.popStyle();
		pg.text("- ÁREA MULTITOQUES -\n2 toques na área de cada pista determinam começo e fim da seleção (laranja claro)\n1 toque dentro da seleção permite arrastá-la para os lados\nAs seleções podem ser controladas simultaneamente (multitoque)", (width/2f), (height/2f));
		pg.text("Liga B", (width*.15f)+(width/32f), (height*.866f));
		pg.text("Random B", (width*.15f)+(width/32f)+((width/16)*1.41f), (height*.866f)); 
		pg.text("BPM B", (width*.15f)+(width/32f)+((width/16)*2.83f), (height*.866f)); 
		pg.text("Divisão\nBPM B", (width*.15f)+((width/16)*4.75f), (height*.866f)); 
		pg.text("Transpor\nFatia B", (width*.15f)+((width/16)*6.17f), (height*.866f));
		pg.text("Inverte\nFatia B", (width*.15f)+((width/16)*7.59f), (height*.866f));
		pg.text("Fade In Fatia B", (width*.15f)+((width/16)*9.83f), (height*.822f));
		pg.text("Fade Out Fatia B", (width*.15f)+((width/16)*9.83f), (height*.75f) + (height/6.3f));
		pg.endDraw();

		////////////////////////////////////////		
		
	    intMaster.cp5.setColorActive(color(241, 90, 36)).setColorBackground(color(180)).setColorForeground(color(241, 90, 36));
	    intMaster.numberSetMasterSteps.cp5.setColorActive(color(241, 90, 36)).setColorBackground(color(180)).setColorForeground(color(241, 90, 36)).setColorValueLabel(color(0));
	    intMaster.numberSetMasterBPM.cp5.setColorActive(color(241, 90, 36)).setColorBackground(color(180)).setColorForeground(color(241, 90, 36)).setColorValueLabel(color(0));
	    intMaster.intTrackA.cp5.setColorActive(color(241, 90, 36)).setColorBackground(color(0)).setColorForeground(color(241, 90, 36));
	    intMaster.intTrackA.numberSetTrackBPM.cp5.setColorActive(color(241, 90, 36)).setColorBackground(color(0)).setColorForeground(color(241, 90, 36)).setColorValueLabel(color(180));
	    intMaster.intTrackA.numberSetTrackDiv.cp5.setColorActive(color(241, 90, 36)).setColorBackground(color(0)).setColorForeground(color(241, 90, 36)).setColorValueLabel(color(180));
	    intMaster.intTrackA.numberSetTrackPBSpeed.cp5.setColorActive(color(241, 90, 36)).setColorBackground(color(0)).setColorForeground(color(241, 90, 36)).setColorValueLabel(color(180));
	    intMaster.intTrackB.cp5.setColorActive(color(241, 90, 36)).setColorBackground(color(0)).setColorForeground(color(241, 90, 36));
	    intMaster.intTrackB.numberSetTrackBPM.cp5.setColorActive(color(241, 90, 36)).setColorBackground(color(0)).setColorForeground(color(241, 90, 36)).setColorValueLabel(color(180));
	    intMaster.intTrackB.numberSetTrackDiv.cp5.setColorActive(color(241, 90, 36)).setColorBackground(color(0)).setColorForeground(color(241, 90, 36)).setColorValueLabel(color(180));
	    intMaster.intTrackB.numberSetTrackPBSpeed.cp5.setColorActive(color(241, 90, 36)).setColorBackground(color(0)).setColorForeground(color(241, 90, 36)).setColorValueLabel(color(180));
	    intMaster.recSession.setColorBackground(color(100,0,0)).setColorForeground(color(100,0,0)).setColorActive(color(255,0,0));
	    
	    intMaster.cp5.addCallback(new CallbackListener() {
	    	
	        public void controlEvent(CallbackEvent theEvent) {
	        	
	        	if(theEvent.getController().equals(intMaster.openA)){
	        	
	        		switch(theEvent.getAction()) {
	        		
	        	      case(ControlP5.ACTION_RELEASED):
	        	    	  
	        	    	  escolheArquivo(1);
	        	    	  
	        	      break;
	        	    }	
	        	}
	        		
        		if(theEvent.getController().equals(intMaster.openB)){
    	        	
	        		switch(theEvent.getAction()) {
	        		
	        	      case(ControlP5.ACTION_RELEASED): 
	        	      
	        	    	  escolheArquivo(2);
	        	    	  
	        	      break;
		        	}
        		}

        		if(theEvent.getController().equals(intMaster.recSession)){

        			switch(theEvent.getAction()) {

					case (ControlP5.ACTION_PRESSED):

						if (theEvent.getController().getValue() == 1) {
							startRec();
						}

						if (theEvent.getController().getValue() == 0) {
							stopRec();
						}

	        	      break;
		        	}
        		}
        		
        		if(theEvent.getController().equals(intMaster.helpButton)){
    	        	
	        		switch(theEvent.getAction()) {
	        		
	        	      case(ControlP5.ACTION_RELEASED): 
	        	    	  
	        	    	  if(helpView == false){
	        	    		  helpView = true;
	        	    	  }
	        	    	  else{
	        	    		  helpView = false;
	        	    	  }
	        	    	  
	        	      break;
		        	}
        		}
	        }
	    });
	    
	}

	
	public void draw() {
		
		
		if (intMaster.numberSetMasterBPM.getNumber() != actualMasterBPM) {
			
			intMaster.intTrackA.numberSetTrackBPM.setNumber(intMaster.numberSetMasterBPM.getNumber());
			intMaster.intTrackB.numberSetTrackBPM.setNumber(intMaster.numberSetMasterBPM.getNumber());
			actualMasterBPM = intMaster.numberSetMasterBPM.getNumber();

			
		}

		
		steps = intMaster.numberSetMasterSteps.getNumber();

		apagaTela();
		desenhaBG();

		desenhaSteps();

		//////////////////////////////////////// Desenha camadas de ControlP5

		intMaster.desIntMaster();
		intMaster.numberSetMasterSteps.desNumberSet();
		intMaster.numberSetMasterBPM.desNumberSet();
		intMaster.intTrackA.desIntTrack();
		intMaster.intTrackB.desIntTrack();
		intMaster.intTrackA.numberSetTrackBPM.desNumberSet();
		intMaster.intTrackA.numberSetTrackDiv.desNumberSet();
		intMaster.intTrackA.numberSetTrackPBSpeed.desNumberSet();
		intMaster.intTrackB.numberSetTrackBPM.desNumberSet();
		intMaster.intTrackB.numberSetTrackDiv.desNumberSet();
		intMaster.intTrackB.numberSetTrackPBSpeed.desNumberSet();

		////////////////////////////////////////


		if (intMaster.intTrackA.trackOn.getState() == true && activeA != 0) {

			stepA.desenhaAtivo(activeA);

		}

		if (intMaster.intTrackB.trackOn.getState() == true && activeB != 0) {

			stepB.desenhaAtivo(activeB);

		}

		bufferDraw1.bufferDraw();
		bufferDraw2.bufferDraw(true);


		if(helpView == true){
			image(pg, 0, 0);
		}

		pushStyle();
		fill(0);
		textSize(height/25);
		textAlign(CENTER, CENTER);
		text("???", (width - (width/8) + (width/32f)+(width/32f)), (height*.75f) + (height/6.3f));
		popStyle();


		PdBase.sendFloat("master_Steps", intMaster.numberSetMasterSteps.getNumber());
		PdBase.sendFloat("master_Vol", intMaster.volMaster.getValue());
		PdBase.sendFloat("master_Mixer", intMaster.mixerMaster.getValue());

		PdBase.sendFloat("track_A_On", intMaster.intTrackA.trackOn.getValue());
		PdBase.sendFloat("track_A_Mode", intMaster.intTrackA.trackMode.getValue());
		PdBase.sendFloat("track_A_BPM", intMaster.intTrackA.numberSetTrackBPM.getNumber());
		PdBase.sendFloat("track_A_Div", intMaster.intTrackA.numberSetTrackDiv.getNumber());
		PdBase.sendFloat("track_A_PBSpeed", intMaster.intTrackA.numberSetTrackPBSpeed.getNumber());
		PdBase.sendFloat("track_A_Rev", intMaster.intTrackA.trackRev.getValue());
		PdBase.sendFloat("track_A_Fadein", intMaster.intTrackA.trackFadeIn.getValue());
		PdBase.sendFloat("track_A_Fadeout", intMaster.intTrackA.trackFadeOut.getValue());
		PdBase.sendFloat("track_A_Offset", offsetA);
		PdBase.sendFloat("track_A_Range", rangeA);

		PdBase.sendFloat("track_B_On", intMaster.intTrackB.trackOn.getValue());
		PdBase.sendFloat("track_B_Mode", intMaster.intTrackB.trackMode.getValue());
		PdBase.sendFloat("track_B_BPM", intMaster.intTrackB.numberSetTrackBPM.getNumber());
		PdBase.sendFloat("track_B_Div", intMaster.intTrackB.numberSetTrackDiv.getNumber());
		PdBase.sendFloat("track_B_PBSpeed", intMaster.intTrackB.numberSetTrackPBSpeed.getNumber());
		PdBase.sendFloat("track_B_Rev", intMaster.intTrackB.trackRev.getValue());
		PdBase.sendFloat("track_B_Fadein", intMaster.intTrackB.trackFadeIn.getValue());
		PdBase.sendFloat("track_B_Fadeout", intMaster.intTrackB.trackFadeOut.getValue());
		PdBase.sendFloat("track_B_Offset", offsetB);
		PdBase.sendFloat("track_B_Range", rangeB);
		
	}


	public void desenhaSteps(){
		
	    stepA.desenhaFundo(0.15f, 0.25f, steps, false);
	    stepB.desenhaFundo(0.15f, 0.5f, steps, true);
	    
		stepA.desenhaSel(steps, offsetA, rangeA);
		stepB.desenhaSel(steps, offsetB, rangeB);
	    
	}
	
	public void desenhaOnda(){
		
		float [] newArray1 = pdManager.getArrayFromPd("1-audiol");
	    float [] newArray2 = pdManager.getArrayFromPd("2-audiol");
	    
	    bufferDraw1 = new BufferDraw(this, newArray1);
	    bufferDraw2 = new BufferDraw(this, newArray2);
		
	}
	
	
	public void apagaTela(){
		
		background(bgcolor);

	}
	
	public void desenhaBG(){
		
		fill(bgcolor2);
	    rect(0, 0, width/8, height);
	    rect((width - (width/8)), 0, width/8, height);
		
	}
	
	
	public void escolheArquivo(int seletor){
		
		startActivityForResult(new Intent("com.wordpress.brunorohde.bitslc.processing.FileChooser"), seletor);
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				audioTrackA = data.getData().toString();
				PdBase.sendSymbol("caminho_A", audioTrackA);
				desenhaOnda();
			}
		}
		
		if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				audioTrackB = data.getData().toString();
				PdBase.sendSymbol("caminho_B", audioTrackB);
				desenhaOnda();
			}
		}
		
	}
	
	
///////////////////////////////////////////// Funções para gerenciar gravação de sessão de áudio
	
	public void startRec() {
		prepareRec();
		PdBase.sendFloat("Rec", 1);
	}

	public void stopRec () {
		PdBase.sendFloat("Rec", 0);
	}

	private void prepareRec() {

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/BITSLC");
		myDir.mkdirs();
		SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmm");
		Date now = new Date();
		String fileName = formatter.format(now);
		String fname = "rec_" + fileName;
		saveFilePath = myDir.getAbsolutePath();
		saveFileName = fname + ".wav";
		PdBase.sendSymbol("recPath", myDir + "/" + fname);
	}    
	
/////////////////////////////////////////////
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		//pegamos os valores "extras" que enviamos desde o MainActivity
		Bundle extras=getIntent().getExtras(); //criamos um objeto que pega todos os extras existentes no intent
		stringVal = extras.getString("Um String"); // pegamos o valor segundo a etiqueta dada no MainActivity
		numeroFloat = extras.getFloat("Um float"); // pegamos o valor segundo a etiqueta dada no MainActivity
		pdManager = new PureDataManager(this);
		pdManager.openPatch("BEATSLICER.pd", com.wordpress.brunorohde.bitslc.R.raw.patch); 
		pdManager.setTicksPerBuffer(1);
		pdManager.setChanelIn(0);
		pdManager.addSendMessagesFromPD("stepActiveA");
		pdManager.addSendMessagesFromPD("stepActiveB");

	}
	
	@Override
	public void onResume() {
		super.onResume();
		pdManager.onResume();
	}
	@Override
	public void onPause() {
		super.onPause();
		pdManager.onPause();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		pdManager.onDestroy();
	}
	@Override
	public void finish() {
		super.finish();
		pdManager.finish();
	}

	
///////////////////////////////////////////// CALLBACKS FROM PD - As seguintes funçōes sāo chamadas quando tiver alguma mensagem nova enviada do PD
	
	
	@Override
	public void callWhenReceiveFloat(String key, float val) {
		
		String AAA = "stepActiveA";
		String BBB = "stepActiveB";
		
		if (key.matches(AAA)){
		
			activeA = val;
			
		}
		
		if (key.matches(BBB)){
			
			activeB = val;
			
		}
		
	}
	
	@Override
	public void callWhenReceiveBang(String key) {
	}
	
	@Override
	public void callWhenReceiveSymbol(String key, String symbol) {
	}
		
	@Override
	public void callWhenReceiveList(String key, Object... args) {
	}
	
	@Override
	public void callWhenReceiveMessage(String key, String symbol,
			Object... args) {
	}
	
	
///////////////////////////////////////////// TOUCH CALLBACK - Funções chamadas cada vez que tem um novo evento na tela
	
	@Override
	public void screenTouched(int id, float x, float y) {
		
		float[] selA = stepA.getSelection();
		float[] selB = stepB.getSelection();
		
		stepWidth = selA[4];

		
		//////////////////////////////////////// Localiza toques dentro de cada Track
		
		
		if (x > width*.15f && y > height*.25f && x < (width*.15f)+(width*.7) && y < height*.5){
			
			touchTrackA[id][0] = 1;
			touchTrackA[id][1] = x;
			
		}
		
		if (x > width * .15f && y > height * .5f && x < (width * .15f)+(width * .7) && y < height * .75) {

			touchTrackB[id][0] = 1;
			touchTrackB[id][1] = x;
			
		}
		
		//////////////////////////////////////// Se houver 2 toques dentro de 1 track, seta inicio e fim da seleção
		
		int howManyTrackA = 0;
		int howManyTrackB = 0;
		
		for(int z = 0; z < 10; z++){
			
			if (touchTrackA[z][0] == 1){
				howManyTrackA++;
			}
			if (touchTrackB[z][0] == 1){
				howManyTrackB++;
			}
		}
		
		
		float touchTrackA_1 = 0;
		float touchTrackA_2 = 0;
		
		float touchTrackB_1 = 0;
		float touchTrackB_2 = 0;
		
		boolean miniCountA = false;
		boolean miniCountB = false;
		
		
		if (howManyTrackA == 2){
			
			for(int z = 0; z < 10; z++){
				
				if (touchTrackA[z][0] == 1){
					
					if (miniCountA == false){
						touchTrackA_1 = touchTrackA[z][1];
						miniCountA = true;
					}
					
					else{
						touchTrackA_2 = touchTrackA[z][1];
						miniCountA = false;
					}
				
				}
			}
			
			if (touchTrackA_1 < touchTrackA_2){
				offsetA = (int)(((touchTrackA_1) - (width*.15f)) / stepWidth);
				rangeA = (int)(((touchTrackA_2 - touchTrackA_1) / stepWidth) + 1);
			}
			
			if (touchTrackA_1 > touchTrackA_2){
				offsetA = (int)(((touchTrackA_2) - (width*.15f)) / stepWidth);
				rangeA = (int)(((touchTrackA_1 - touchTrackA_2) / stepWidth) + 1);
			}
			
		}
		
		
		if (howManyTrackB == 2){
			
			for(int z = 0; z < 10; z++){
				
				if (touchTrackB[z][0] == 1){
					
					if (miniCountB == false){
						touchTrackB_1 = touchTrackB[z][1];
						miniCountB = true;
					}
					
					else{
						touchTrackB_2 = touchTrackB[z][1];
						miniCountB = false;
					}
				
				}
			}
			
			if (touchTrackB_1 < touchTrackB_2){
				offsetB = (int)(((touchTrackB_1) - (width*.15f)) / stepWidth);
				rangeB = (int)(((touchTrackB_2 - touchTrackB_1) / stepWidth) + 1);
			}
			
			if (touchTrackB_1 > touchTrackB_2){
				offsetB = (int)(((touchTrackB_2) - (width*.15f)) / stepWidth);
				rangeB = (int)(((touchTrackB_1 - touchTrackB_2) / stepWidth) + 1);
			}
			
		}
		
		
		//////////////////////////////////////// Localiza toques dentro de cada seleção atual
		
		
		if (x > selA[0] && y > selA[1] && x < selA[2] && y < selA[3]){
			
			movInA = 0;
			touchSelA[id] = 1;
			
		}
			
		if (x > selB[0] && y > selB[1] && x < selB[2] && y < selB[3]){
			
			movInB = 0;
			touchSelB[id] = 1;

		}
		
		
	}
	
	@Override
	public void screenTouchedReleased(int id) {
		
		
		for(int z = 0; z < 10; z++){
			if(touchTrackA[z][0] == 1){
				touchTrackA[z][0] = 0;
				touchTrackA[z][1] = 0;
			}
			
			if(touchTrackB[z][0] == 1){
				touchTrackB[z][0] = 0;
				touchTrackB[z][1] = 0;
			}
		}

		touchSelA[id] = 0;
		touchSelB[id] = 0;
		
	}

	@Override
	public void screenTouchedDragged(int id, float x, float y, float dist, float ang) {
		
		int howManyTrackA = 0;
		int howManyTrackB = 0;
		
		int howManySelA = 0;
		int howManySelB = 0;
		
		int movInANormal = 0;
		int movInBNormal = 0;
		
		//////////////////////////////////////// Conta toques dentro de cada Track
		
		for(int z = 0; z < 10; z++){
			if (touchTrackA[z][0] == 1){
				howManyTrackA++;
			}
		}
		
		for(int z = 0; z < 10; z++){
			if (touchTrackB[z][0] == 1){
				howManyTrackB++;
			}
		}
		
		//////////////////////////////////////// Conta toques dentro de cada seleção
		
		for(int z = 0; z < 10; z++){
			if (touchSelA[z] == 1){
				howManySelA++;
			}
		}
		
		for(int z = 0; z < 10; z++){
			if (touchSelB[z] == 1){
				howManySelB++;
			}
		}
		
		////////////////////////////////////////
		
		if (howManyTrackA == 1){
		
			if (howManySelA == 1 && touchSelA[id] == 1) {

				if (ang < 0.5 || ang > 4.95) {

					movInA += dist;
					movInANormal = (int)(movInA / stepWidth);
					touchTrackA[id][1] += dist;

				}

				if (ang > 2.5 && ang < 3.5) {

					movInA -= dist;
					movInANormal = (int)(movInA / stepWidth);
					touchTrackA[id][1] -= dist;

				}

				if (movInANormal != 0) {
					offsetA += movInANormal;
					movInA = 0;
				}
			}

		}

		////////////////////////////////////////
		
		if (howManyTrackB == 1) {

			if (howManySelB == 1 && touchSelB[id] == 1) {

				if (ang < 0.5 || ang > 4.95) {

					movInB += dist;
					movInBNormal = (int)(movInB / stepWidth);
					touchTrackB[id][1] += dist;

				}

				if (ang > 2.5 && ang < 3.5) {

					movInB -= dist;
					movInBNormal = (int)(movInB / stepWidth);
					touchTrackB[id][1] -= dist;

				}

				if (movInBNormal != 0) {
					offsetB += movInBNormal;
					movInB = 0;
				}
			}

		}
	}
	
	public boolean surfaceTouchEvent(MotionEvent me) {
		multiTouch.surfaceTouchEvent(me);
	    return super.surfaceTouchEvent(me);
	}
	
}
