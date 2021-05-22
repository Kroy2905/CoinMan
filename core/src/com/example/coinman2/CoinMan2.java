package com.example.coinman2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;

public   class CoinMan2 extends ApplicationAdapter {
	SpriteBatch batch;

	Texture background , coin,bomb,dizzy;
	Texture[] man;
	int manState = 0,manY = 0,coinCount=0,bombCount=0;
	float gravity = 0.35f;
	float velocity = 0 ;
Rectangle manRectangle;
	int pause = 0 , score = 0 , gamestate =0;
	BitmapFont font;

  Random random;
	ArrayList<Integer> coinX = new ArrayList<>();
	ArrayList<Integer> coinY = new ArrayList<>();

	ArrayList<Integer> bombX = new ArrayList<>();
	ArrayList<Integer>  bombY = new ArrayList<>();
	ArrayList<Rectangle> coinsRectangle = new ArrayList<>();
	ArrayList<Rectangle> bombsRectangle = new ArrayList<>();

    @Override
	public void create() {
    	batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");

		manY = Gdx.graphics.getHeight() / 2;
		bomb = new Texture("bomb.png");
		coin = new Texture("coin.png");
		random = new Random();
		font = new BitmapFont();
		font.setColor(Color.RED);
		font.getData().setScale(10);
		dizzy = new Texture("dizzy-1.png");
	}


    public  void  makeCoins(){

		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinY.add((int)height);
		coinX.add(Gdx.graphics.getWidth());
	}

	public  void  makeBombs(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombY.add((int)height);
		bombX.add(Gdx.graphics.getWidth());
	}



	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gamestate == 1 ){
			// Game is on

			// COINS -------
			if(coinCount<100){
				coinCount++;
			}else{
				coinCount = 0;
				makeCoins();
			}
			coinsRectangle.clear();
			for(int i = 0;i<coinX.size();i++){
				batch.draw(coin,coinX.get(i),coinY.get(i));
				coinX.set(i,coinX.get(i)-5);
				coinsRectangle.add(new Rectangle(coinX.get(i), coinY.get(i),coin.getWidth(),coin.getHeight()));

			}
			//------------------------

			//  BOMBS -------
			if(bombCount<260){
				bombCount++;
			}else{
				bombCount = 0;
				makeBombs();
			}
			bombsRectangle.clear();
			for(int i = 0;i<bombX.size();i++){
				batch.draw(bomb,bombX.get(i),bombY.get(i));
				bombX.set(i,bombX.get(i)-9);
				bombsRectangle.add(new Rectangle(bombX.get(i), bombY.get(i),bomb.getWidth(),bomb.getHeight()));

			}
			//------------------------

			if(Gdx.input.justTouched()){
				velocity = -15;
			}
			if(pause < 6)
				pause++;
			else {
				pause= 0;
				if (manState < 3)
					manState++;
				else
					manState = 0;
			}

			velocity+= gravity;
			manY -=velocity;

			if(manY<=0)
				manY = 0;

		}
		else  if(gamestate==0){
			 if(Gdx.input.justTouched()){
			 	gamestate = 1 ;

			 }
			// waiting to start
		}
		else if(gamestate == 2 ){
			// game over

			if(Gdx.input.justTouched()){
				gamestate = 1 ;
				manY = Gdx.graphics.getHeight()/2;
				score = 0 ;
				velocity = 0;
				coinY.clear();
				coinX.clear();
				coinsRectangle.clear();
				coinCount = 0;
				bombY.clear();
				bombX.clear();
				bombsRectangle.clear();
				bombCount = 0;


			}



		}

        if(gamestate!=2)
		batch.draw(man[manState],Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2 , manY);
        else
			batch.draw(dizzy,Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2 , manY);

			batch.draw(man[manState],Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2 , manY);
		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2 , manY,man[manState].getWidth(),man[manState].getHeight());

		for (int i =0;i<coinsRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,coinsRectangle.get(i))){
				Gdx.app.log("COIN!","COllision");
				score++;

				coinsRectangle.remove(i);
				coinX.remove(i);
				coinY.remove(i);
				break;
			}
		}
		for (int i =0;i<bombsRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,bombsRectangle.get(i))){
				Gdx.app.log("BOMB!","BOOM");
               gamestate = 2;
			}
		}

         font.draw(batch,String.valueOf(score),100,200);

		batch.end();

	}
	
	@Override
	public void dispose ()
	{
		batch.dispose();

	}
}

