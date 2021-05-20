package com.flappybird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;


public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture lowerpipe;
	Texture upperpipe;
	int timedelay=0;
	float targettimeformakingpipe=150;//with increase in speed we have to increase the formation of pipes

	int timeIntervalForSpeed=0;//for 100ms we incresing the spped by .01

	int res=0;//for making illusion of birds flying

	float gravity=0.2f;//to make yaxis of bird i.e. moving bird downward

	float velocity=0f;
	int yaxis;
	ArrayList<Integer> xAxis=new ArrayList<>();
	ArrayList<Integer> yAxis=new ArrayList<>();

	Random random;
	int count=0;
	float speedInc=5f;//initial spped for pipes

	Circle birdshape;
	ArrayList<Rectangle> upperpipeshape=new ArrayList<>();
	ArrayList<Rectangle> lowerpipeshape=new ArrayList<>();

	int gamestate=0;
	int scoringpipe=0;
	int score=0;
	BitmapFont bitmapFont;


	public  void makepipe(){
		random=new Random();
		float number=random.nextFloat()*250;
		xAxis.add(Gdx.graphics.getWidth());
		yAxis.add((int) number);

	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		birds=new Texture[3];
		birds[0]=new Texture("downflap.png");
		birds[1]=new Texture("midflap.png");
		birds[2]=new Texture("upflap.png");
		lowerpipe=new Texture("lowerPipe.png");
		upperpipe=new Texture("upperPipe.png");
		yaxis=Gdx.graphics.getHeight()/2;


	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gamestate==0){
			if(Gdx.input.justTouched()){
				gamestate=1;
			}
		}
		else if(gamestate==1){
			if(count<targettimeformakingpipe){
				count++;
			}else {
				count = 0;
				makepipe();
			}
			lowerpipeshape.clear();
			upperpipeshape.clear();
			for (int i=0;i<xAxis.size();i++){
				batch.draw(upperpipe,xAxis.get(i),Gdx.graphics.getHeight()/2+150+yAxis.get(i));
				upperpipeshape.add(new Rectangle(xAxis.get(i),Gdx.graphics.getHeight()/2+150+yAxis.get(i),upperpipe.getWidth(),upperpipe.getHeight()));

				batch.draw(lowerpipe,xAxis.get(i),Gdx.graphics.getHeight()/2-150-900+yAxis.get(i));
				lowerpipeshape.add(new Rectangle(xAxis.get(i),Gdx.graphics.getHeight()/2-150-900+yAxis.get(i),lowerpipe.getWidth(),lowerpipe.getHeight()));

				timeIntervalForSpeed++;
				if(timeIntervalForSpeed<100){
					timeIntervalForSpeed++;
				}else{
					timeIntervalForSpeed=0;
					speedInc+=.01;
					if(speedInc>=30){
						speedInc=30;
					}
					targettimeformakingpipe-=0.01;
					if(targettimeformakingpipe<=30){
						targettimeformakingpipe=30;
					}

				}
				xAxis.set(i,(int)(xAxis.get(i)-speedInc));


			}



			if(Gdx.input.justTouched()){
				velocity=-8;
			}
			if(timedelay<8){
				timedelay++;
			}else{
				timedelay=0;
				if(res<2){
					res++;
				}
				else{
					res=0;
				}
			}

			velocity += gravity;
			yaxis -= velocity;
			if(yaxis<=0){
				yaxis=0;
			}
			if(yaxis>=Gdx.graphics.getHeight()-birds[res].getHeight()){
				yaxis=Gdx.graphics.getHeight()-birds[res].getHeight();
			}
		}
		else {
			if(Gdx.input.justTouched()){
				gamestate=1;
				timedelay=0;
				timeIntervalForSpeed=0;
				targettimeformakingpipe=150;
				res=0;
				gravity=.2f;
				velocity=0f;
				xAxis.clear();
				yAxis.clear();
				upperpipeshape.clear();
				count=0;
				lowerpipeshape.clear();
				speedInc=5f;
			}
		}
		batch.draw(birds[res],Gdx.graphics.getWidth()/2-birds[res].getWidth(),yaxis);

		birdshape=new Circle(Gdx.graphics.getWidth()/2-birds[res].getWidth(),yaxis,birds[res].getWidth()/2);

		for(int i=0;i<upperpipeshape.size();i++){
			if(Intersector.overlaps(birdshape,upperpipeshape.get(i))){
				gamestate=2;
				break;
			}
		}

		for(int i=0;i<lowerpipeshape.size();i++){
			if(Intersector.overlaps(birdshape,lowerpipeshape.get(i))){
				gamestate=2;
				break;
			}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
