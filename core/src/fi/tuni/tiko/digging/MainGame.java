package fi.tuni.tiko.digging;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Iterator;

import static com.badlogic.gdx.Application.ApplicationType.Android;
import static fi.tuni.tiko.digging.Player.ATTACKING;
import static fi.tuni.tiko.digging.Player.LEFT;
import static fi.tuni.tiko.digging.Player.READY;
import static fi.tuni.tiko.digging.Player.RIGHT;
import static fi.tuni.tiko.digging.Player.WALKING;
import static fi.tuni.tiko.digging.PlayerControls.NOQUEU;
import static fi.tuni.tiko.digging.PlayerControls.TRYDOWN;
import static fi.tuni.tiko.digging.PlayerControls.TRYLEFT;
import static fi.tuni.tiko.digging.PlayerControls.TRYRIGHT;
import static fi.tuni.tiko.digging.PlayerControls.TRYUP;
import static fi.tuni.tiko.digging.WalkingCreature.DEAD;
import static fi.tuni.tiko.digging.WalkingCreature.FALLING;

public class MainGame extends ApplicationAdapter implements GestureDetector.GestureListener {

	//näitä finaleja sun muita tullaan ehkä myöhemmin muuttamaan, testataan nyt näillä

	//1 molemmilla puolilla
	public static int TILES_IN_ROWS_INCLUDING_EDGES = 9;
	public static int TILES_IN_ROWS_WITHOUT_EDGES = TILES_IN_ROWS_INCLUDING_EDGES-2;

	//heightin takia, jotkut kentät ehkä erikokoisia?
	int TILES_IN_COLS= 200;

	//pikseleinä? hmm nämä ei sinällään toimi tai näitä ei ehkä tarvita missään, mutta koitetaanpa pitää lukua
	//82 oli aluksi (7*82)=574 eli tuohon 576 maailman kokoon sopiva. Sitten lisäsin 0.1f marginaalit sekä oikealle
	//että vasemmalle koska pitää olla vähän edes tilaa (undiggable reunat). Nyt siis 576/7.2f (7 tileä +0.2 marginit), on 80
	//int TILE_WIDTH_PIXELS   = 80;
	int TILE_HEIGHT_PIXELS  = 80;
	//int TILE_WIDTH_PIXELS;
	//int TILE_HEIGHT_PIXEL;


	//public int getTILE_HEIGHT() {
	//	return TILE_HEIGHT;
	//}


	public static float UNDIGGABLE_MARGIN=0.1f;

	float UNDIGGABLE_MARGIN_WIDTH;

	//EARTH tarkoittaa maatilejä + marginaalia x-suunnassa, -y -koordinaateissa ollaan maan yläpuolella
	//float EARTH_WIDTH= TILE_WIDTH_PIXELS*TILES_IN_ROWS;
	//float EARTH_HEIGHT= TILE_HEIGHT_PIXELS*TILES_IN_COLS;

	int UNDIGGABLE_MARGIN_PIXELS;

	float TIDEWIDTH;
	float TIDEHEIGHT;

	GestureDetector gestureDetector;



			//myöhemmin joku oma luokkansa farmi tietysti

	private GameTexture farm;

	private SpriteBatch batch;

	private PlayerControls playerControls;

	private DirtPool dirtPool;
	private StonePool stonePool;
	private BlankPool blankPool;
	private DescendingPool descendingPool;
	private PermanentPool permanentPool;
	private FarmPool farmPool;

	private TilePools tilePools;

	private GoblinPool goblinPool;
	private SpikePool spikePool;
	private FallingTrapPool fallingTrapPool;

	private HazardPools hazardPools;

	private ResourcePool resourcePool;

	private ResourceAnimationPool resourceAnimationPool;
	private ArrayList<ResourceGainedAnimation> resourceAnimationList;

	private TileAnimationPools tileAnimationPools;

	//creating static graphics here, because adding textures in this didnt work in DirtTile classes body.
	// This could probably be replaced with asset manager if needed
	public static GameTexture[] dirtTextureTileset = new GameTexture[48];





	private int totalResourcesCollected;

	private OrthographicCamera camera;
	//private Cameraplacer cameraplacer;

	//private FitViewport fitViewport;

	Player player;
	//position player will be heading, these will change in ... walkthinkgs
	//int targetX;
	//int targetY;

	//to be added later
	//Menu menu

	//Settings settings

	//IMPORTANT TÄÄ allStages PITÄÄ POISTAA MYÖHEMMIN JA MUUTTAA STAGEN LUOMISEN LOGIIKKAA, EI OO KOKONAISIA STAGEJA JOISTA PIDETÄÄN KIRJAA, IHAN LIIKAA MUISTIA VIE, SEN SIJAAN TEEN TUON allLevelStats-listan
	private ArrayList<Stage> allStages;
	private ArrayList<LevelStats> allLevelsStats;


	Stage currentStage;

	boolean didThisAlready = false;

	@Override
	public void create () {


		//UNDIGGABLE_MARGIN_WIDTH = (Gdx.graphics.getWidth() % 7) / 2;


		//initalizing dirtTileTextureset using static GameTexture[] dirtTextureTileset array. 0-index will be left empty for convienience.
		for (int i=1; i < dirtTextureTileset.length; i++) {
			dirtTextureTileset[i]=new GameTexture(new Texture("tilesets/dirt/dirtTile-"+i+".png"));
		}


		batch = new SpriteBatch();

		farm = new GameTexture(new Texture ("farm.jpg"));


		player = new Player();

		playerControls = new PlayerControls();

		gestureDetector = new GestureDetector(this);
		Gdx.input.setInputProcessor(gestureDetector);

		//nämä voisi laittaa tulemaan jo valikkojen aikana/niitä ennen, jolloin itse pelin generoiminen ei vie niin paljoa aikaa
		dirtPool = new DirtPool(200, 500);
		stonePool = new StonePool(670, 900);
		blankPool = new BlankPool(50,500);
		descendingPool = new DescendingPool(50, 500);
		permanentPool = new PermanentPool(200, 500);
		farmPool = new FarmPool(7,16);

		resourcePool = new ResourcePool(12,48);




		tilePools=new TilePools(dirtPool, stonePool, blankPool, descendingPool, permanentPool, farmPool, resourcePool);

		resourceAnimationPool = new ResourceAnimationPool(3, 6);

		resourceAnimationList= new ArrayList<ResourceGainedAnimation>();
		tileAnimationPools = new TileAnimationPools(resourceAnimationPool, resourceAnimationList);


		goblinPool = new GoblinPool(28,100);
		spikePool = new SpikePool (28, 100);
		fallingTrapPool = new FallingTrapPool (10, 99);

		hazardPools = new HazardPools(goblinPool, spikePool, fallingTrapPool);



		allLevelsStats = new ArrayList<LevelStats>();
		allLevelsStats.add(new LevelStats(0, 3, 1000, 0.1f));

		//pitää muuttaa tätä kaikilla ei voi olla omaa tiles arrayta
		allStages = new ArrayList<Stage>();
		allStages.add(new Stage(allStages.size(), 2,3,4, tilePools, hazardPools, allLevelsStats.get(0), tileAnimationPools, totalResourcesCollected )); // id 0 farm?
		//allStages.add(new Stage(allStages.size(), 2,3,4)); // id 1? stage1

		//oikeasti myöhemmin saatetaan aloittaa 0:sta tms.
		startStage(0);

		//camera = new OrthographicCamera();
		//camera= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//camera = new OrthographicCamera(TILES_IN_ROWS+2*UNDIGGABLE_MARGIN, 7.44f);

		//camera = new OrthographicCamera((TILES_IN_ROWS-2)+2*UNDIGGABLE_MARGIN, 7.44f);
		camera = new OrthographicCamera((TILES_IN_ROWS_WITHOUT_EDGES)+2*UNDIGGABLE_MARGIN, 12.8f);




		//camera.setToOrtho(true, TILES_IN_ROWS+2*UNDIGGABLE_MARGIN, 1024/TILE_HEIGHT);

		//camera.setToOrtho(true, (TILES_IN_ROWS_WITHOUT_EDGES)+2*UNDIGGABLE_MARGIN, Gdx.graphics.getHeight()/TILE_HEIGHT_PIXELS);

		switch (Gdx.app.getType()) {
			case Android:

				float resoWidthTweaked = TILES_IN_ROWS_WITHOUT_EDGES + 2 * UNDIGGABLE_MARGIN;


				float aspectRatio = Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
				System.out.println("Resolution: " + aspectRatio);


				//camera.setToOrtho(true, (TILES_IN_ROWS_WITHOUT_EDGES)+2*UNDIGGABLE_MARGIN, 12.8f);


				camera.setToOrtho(true, (TILES_IN_ROWS_WITHOUT_EDGES) + 2 * UNDIGGABLE_MARGIN, aspectRatio * resoWidthTweaked);

				break;

			case Desktop:
				camera.setToOrtho(true, (TILES_IN_ROWS_WITHOUT_EDGES)+2*UNDIGGABLE_MARGIN, 12.8f);

				break;
		}





		//camera.rotate(90);
		//camera.setToOrtho(true, 1024/TILE_HEIGHT, TILES_AMOUNT_WIDTH);
		//fitViewport = new FitViewport(





		//   7/12.44444 is aspect ratio of 9/16
		//camera.setToOrtho(true, 7, 12.44444f);
		//camera.region.flip(false, true);
		//camera.rotate(180);


		//camera.position.x=WORLD_WIDTH/2;

		//tuo 1f pitäisi olla tilen width muuttuja
		//camera.position.x=((TILES_IN_ROWS*1f)/2 + UNDIGGABLE_MARGIN);

		//äh ei jaksa säätää just nyt tästä saattaa tulla ongelmia
		//camera.position.x=3.5f;
		camera.position.x=1.03f+(float)(TILES_IN_ROWS_WITHOUT_EDGES)/2;


		camera.position.y=player.getY()+3.0f;

		//cameraplacer=new Cameraplacer();



	}

	@Override
	public void render () {

		//System.out.println("player tile pos y: "+player.getTilePosY()+", pl tile pos x: "+player.getTilePosX());


		float delta = Gdx.graphics.getDeltaTime();



		//huom tätä ei ehkä kannata joka renderillä aina
		//cameraplacer.updateCameraPosition(camera, delta, currentStage, player);

		batch.setProjectionMatrix(camera.combined);

		clearScreen();

		checkSpecialTiles(delta);
		checkVanishingTiles(delta);

		//System.out.println(player.getX());
		//System.out.println(player.getY());

		//Not sure if this is the right class to hold checkPlayersUnwantedMovemement()-method or would it be better to be in player?


		checkDeadHazards();
		checkPlayerHazardCollision();

		checkIfHazardsGetTriggeredOrNoticed();


		checkHazardMovement(delta);

		checkPlayersUnwantedMovement();



		//System.out.println(playerControls.getQueu());


		if ( (player.getStatus() == READY) && (playerControls.getQueu() != NOQUEU) ) {
			playerControls.checkQueu(player, currentStage);
		}

		controlPlayer();





		player.updateMovement(delta, currentStage);

		tileAnimationPools.checkResourceGainedAnimations(delta);











		if (!didThisAlready) {
			//this is to test tile positions in different ways

			/*
			for (int y=0; y<3; y++) {
				for (int x=0; x<currentStage.tiles[0].length; x++) {
					GameTile t = currentStage.tiles[y][x];
					System.out.print("y: "+y+", x: "+x+" ; Width "+t.getWidth()+", Height "+t.getHeight() +", GameobjectGetY "+t.getY()+ ", GameobjectGetX "+t.getX());
					System.out.println(", getTileLocationY() "+t.getLocationY()+ ", getTileLocationX() "+t.getLocationX()+"") ;


				}

			}*/
		didThisAlready = true;
		}











		batch.begin();

		batch.draw(farm,+0.9f,-4f, 7.4f, 5f);
		currentStage.draw(batch);

		tileAnimationPools.draw(batch);



		player.draw(batch);


		batch.end();

		moveCamera();
	}

	@Override
	public boolean touchDown (float x, float y, int pointer, int button) {
		//Gdx.app.log("touchDown", "Touchdown: x: "+x+", y: "+y);
		return false;
	}

	@Override
	public boolean tap (float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress (float x, float y) {

		if (player.getStatus()==READY) {
			for (int yyy = player.getTilePosY(); yyy<player.getTilePosY()+7; yyy++) {
				currentStage.tiles[yyy][player.getTilePosX()]=new BlankTile(yyy, player.getTilePosX() );
			}
			return true;

		}
		return false;
	}

	@Override
	public boolean fling (float velocityX, float velocityY, int button) {

		if (player.getStatus()==READY) {

			if ((velocityX > Math.abs(velocityY)) ) {

				playerControls.tryRight(player, currentStage);
				return true;


			} else if ( (velocityX < 0) && Math.abs(velocityX) > Math.abs(velocityY) ) {

				playerControls.tryLeft(player, currentStage);
				return true;

			} else if ((velocityY > Math.abs(velocityX)) ) {

				playerControls.tryDown(player, currentStage);
				return true;
			} else  {
				playerControls.tryUp(player, currentStage);
				return true;
			}


		} else {
			if ((velocityX > Math.abs(velocityY)) ) {

				playerControls.setQueu(TRYRIGHT);
			} else if ( (velocityX < 0) && Math.abs(velocityX) > Math.abs(velocityY) ) {

				playerControls.setQueu(TRYLEFT);

			}  else if ((velocityY > Math.abs(velocityX)) ) {

				playerControls.setQueu(TRYDOWN);
			}  else  {
				playerControls.setQueu(TRYUP);
			}

			return true;
		}




	}

	@Override
	public boolean pan (float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop (float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom (float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop () {

	}

	@Override
	public void dispose () {
		batch.dispose();
		
	}


	private void clearScreen() {
		Gdx.gl.glClearColor(0.39f, 0.39f, 0.39f, 0.19f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	//camera will follow player's y-position
	public void moveCamera() {

		camera.position.y = player.getY()+3f;
		//camera.position.y++;

		//Gdx.app.log("Location", "Location: y: "+camera.position.y+", x: "+camera.position.x);

		camera.update();
	}

	//starts and generates a level.
	public void startStage(int idOfStage) {

		if (idOfStage > allStages.size() || idOfStage < 0)
		{
			throw new IllegalArgumentException("id you provided wasn't in range of [ from 0 to allStages.size() ]. parameter idOfStage must be between 0 and "+allStages.size());
		} else {
			currentStage = allStages.get(idOfStage);
		}

		player.setStageCurrentlyIn(idOfStage);

		currentStage.generateNewMap();
		//player.setY(1f-player.getHeight());
		//player.setX(0.5f);

		//TÄRKEÄÄ TÄYTYY LAITTAA METODI PALYERIIN
		//player.setX(player.getX()-player.getWidth()/2);

		System.out.println(player.getY());


		//player.setGravityPull(currentStage.getGravity());



	}


	public void checkIfHazardsGetTriggeredOrNoticed() {

		for (int i=0; i<currentStage.hazardList.size(); i++) {
			if (currentStage.hazardList.get(i) instanceof FallingTrap) {
				FallingTrap fallingTrap = (FallingTrap)currentStage.hazardList.get(i);
				if (fallingTrap.getStatus()==READY && player.getTilePosY() > fallingTrap.getTilePosY() && player.getTilePosX()==fallingTrap.getTilePosX()) {

					int amountOfTilesToCheck = 1;
					boolean continues = true;
					for (int yy = fallingTrap.getTilePosY() + 2; yy < currentStage.tiles.length && continues; yy++) {
						if (currentStage.tiles[yy][fallingTrap.getTilePosX()].isConcrete() == false) {
							amountOfTilesToCheck++;
						} else {
							continues = false;
						}
					}

					if (player.getTilePosY()==fallingTrap.getTilePosY()+amountOfTilesToCheck) {
						fallingTrap.startTriggering();

						System.out.println("TRIGGERED ;-(");
					}
				}

				//checks if hazard is READY and 2 tiles away, and makes player notice it in that case and vice versa
				if (fallingTrap.getStatus()==READY) {
					if (Math.abs(player.getTilePosY() - fallingTrap.getTilePosY() ) <= 3 && Math.abs(player.getTilePosX() - fallingTrap.getTilePosX() ) <= 2) {
						fallingTrap.setNoticed(true);
					} else {
						fallingTrap.setNoticed(false);
					}



				}

			}
		}


	}







	public void checkSpecialTiles(float delta) {
		Iterator<GameTile> it = currentStage.specialTileList.iterator();
		while (it.hasNext()) {
			GameTile specialTile = it.next();
			if (specialTile instanceof DescendingDirtTile) {
				if (((DescendingDirtTile) specialTile).isDescending()) {
					if (((DescendingDirtTile) specialTile).getDescendingTimeLeft() > 0) {
						((DescendingDirtTile) specialTile).continueDescending(delta);
					} else {
						if (!specialTile.isVanishing()) {
							((DescendingDirtTile) specialTile).startVanishing(currentStage);
							it.remove();
						}
					}





				} else if ( (specialTile.getLocationX()== player.getTilePosX() ) && (specialTile.getLocationY() == player.getTilePosY()+1) ) {
					((DescendingDirtTile) specialTile).startDescending(delta);}


			}

		}
	}

	public void checkDeadHazards() {

		Iterator<TileBasedObject> it = currentStage.hazardList.iterator();
		while (it.hasNext()) {
			TileBasedObject hazard = it.next();
			if (hazard instanceof HazardousWalker) {

				if (((HazardousWalker) hazard).getStatus() == DEAD) {
					it.remove();
				}


			} else if (hazard instanceof ImmobileHazard) {
				if (((ImmobileHazard)hazard).isStatusDead() ) {
					it.remove();
				}
			}
		}
	}

		/*

Iterator<User> it = list.iterator();
while (it.hasNext()) {
  User user = it.next();
  if (user.getName().equals("John Doe")) {
    it.remove();
  }
}

*/


		/*
		for (int i=0; i<currentStage.hazardList.size(); i++) {
			if (currentStage.hazardList.get(i) instanceof HazardousWalker) {
				HazardousWalker hazard = (HazardousWalker)currentStage.hazardList.get(i);
				if (hazard.getStatus()==DEAD) {

				}
			}
		}*/


	public void checkHazardMovement(float delta) {

		for (int i=0; i<currentStage.hazardList.size(); i++) {
			if (currentStage.hazardList.get(i) instanceof HazardousWalker) {
				HazardousWalker hazard = (HazardousWalker)currentStage.hazardList.get(i);
				//if (hazard.getTargetTilePosX() != hazard.getTilePosX() || hazard.getTargetTilePosY() != hazard.getTilePosY() ) {
				hazard.updateMovement(currentStage.tiles, delta);

				//}

			} else if (currentStage.hazardList.get(i) instanceof ImmobileHazard) {
				ImmobileHazard hazard = (ImmobileHazard)currentStage.hazardList.get(i);

				if (!hazard.isVanishing()) {
					hazard.checkIfConcreteTileBelow(currentStage.tiles);
					hazard.updateVanishStatus(delta);
				} else {
					hazard.updateVanishStatus(delta);
				}

			}

		}

	}
	public void checkVanishingTiles(float delta) {
		if (currentStage.vanishingTileList.size() > 0) {
			Iterator<GameTile> it = currentStage.vanishingTileList.iterator();
			while (it.hasNext()) {
				GameTile vanishingTile = it.next();
				if (vanishingTile.getVanishTimeLeft()>0) {
					vanishingTile.continueVanishing(delta);
				} else {

					it.remove();
					System.out.println("removed");
				}
			}

		}
	}


	public void checkPlayersUnwantedMovement() {
		//this could be done the other way but let's try doing it by allowing every action like breaking, walking etc to be finished
		//before they start falling etc against players will
		//In order for this to work, this method should be called before controlPlayer() -method, so player shouldnt be able to avoid falling any way

		if (player.getStatus()==READY) {
			//checks if the tile right under player isn't concrete
			if (! currentStage.getTile(player.getTilePosY()+1, player.getTilePosX() ).isConcrete() ) {

				playerControls.setQueu(NOQUEU);
				int amountOfTilesToFall=1;


				// we will check how big the fall will be so the falling goes more smooth (animation etc)
				boolean continues =true;
				for (int yy=player.getTilePosY()+2; yy < currentStage.tiles.length && continues; yy++) {
					if (! currentStage.getTile(yy, player.getTilePosX() ).isConcrete() ) {
						amountOfTilesToFall++;
					} else {
						continues=false;
					}
				}

				player.startFalling(amountOfTilesToFall);
				System.out.println("started to FALL " +amountOfTilesToFall+" tiles.");
			}

		}
	}


	public void checkPlayerHazardCollision() {

		//important: walking player set to be stronger than hazards (same as attacking)
		if (player.getStatus()!=ATTACKING && player.getStatus()!=WALKING) {

			for (int i = 0; i < currentStage.hazardList.size(); i++) {
				TileBasedObject hazard = currentStage.hazardList.get(i);
				//if (hazard.getTilePosX()==player.getTilePosX() && hazard.getTilePosY()==player.getTilePosY()) {
				if (!(hazard.isVanishing()) && (hazard.getRectangle().overlaps(player.getRectangle()))) {

					if (hazard instanceof Hazard && (hazard.isVanishing()==false)) {
						//if (((Hazard) hazard).getGetsDestroyedByFallingPlayer() && (player.getStatus()==FALLING) ) {
						if (((Hazard) hazard).getGetsDestroyedByFallingPlayer() && ( (player.getY() + player.getHeight()) < (hazard.getY() + hazard.getHeight())) ) {
							System.out.println("playerY: "+player.getY());
							System.out.println("hazardY: "+hazard.getY());
							//&& (player.getY() > hazard.getY() )) {
							((Hazard) hazard).vanish();
							System.out.println("theyShouldVanish");
						} else {
							if (hazard.isVanishing()==true)
								System.out.println("zapThing");
							zapPlayer();
						}

					}
				}
			}

		//this is the case when player is attacking
		} else {
			for (int i = 0; i < currentStage.hazardList.size(); i++) {
				TileBasedObject hazard = currentStage.hazardList.get(i);
				if ((hazard.isVanishing()==false) && (hazard.getRectangle().overlaps(player.getRectangle()))) {

					if (player.getAttackDirection()==LEFT && (hazard.getX()<player.getX()) ||
					    player.getAttackDirection()==RIGHT && (hazard.getX()>player.getX())) {
						((Hazard) hazard).vanish();
						System.out.println(i);
					} else {
						zapPlayer();
					}

				}
			}
		}


	}
	//player "dies" and starts from the beginning of the stage
	public void zapPlayer() {

		playerControls.setQueu(NOQUEU);

		System.out.println("Zap");
		player.getZapped();
		//currentStage.dispose();
		tilePools.putAllTilesIntoPools(currentStage.tiles);
		hazardPools.putAllHazardsIntoPool(currentStage.hazardList);


		currentStage.hazardList.clear();
		currentStage.specialTileList.clear();
		currentStage.vanishingTileList.clear();
		currentStage.resourceTileList.clear();

		startStage(player.getStageCurrentlyIn());
	}

	public void tryPlayerRight() {

	}

	public void controlPlayer() {





		//float delta = Gdx.graphics.getDeltaTime();


		//switch (Gdx.app.getType()) {
			//case Desktop:



				if (player.getStatus()==READY) {




					//targetX starts from the currentTilePos of player and then get
					//OKEI TÄÄ KOKO HOMMA PITÄÄ MIETTIÄ UUDESTAAN METODIEN KAUTTA MENEE KOODIN TOISTOKSI
					//AINA ERI SUUNTIEN KANSSA MUTTA KOKEILLAAN NYT NÄIN ALKUUN

					//int targetX = player.getTilePosX();
					//int targetY = player.getTilePosY();

					if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {

						playerControls.tryRight(player, currentStage);





					} else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {


						playerControls.tryLeft(player, currentStage);


					} else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {



						playerControls.tryDown(player, currentStage);



					} else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
						playerControls.cheatDown(player, currentStage);
					} else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
						playerControls.tryUp(player, currentStage);


					} else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
						player.startAttacking(LEFT);
					} else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
						player.startAttacking(RIGHT);
					}

				// updating queue in case player is already taking an action
				} else {
					if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
						playerControls.setQueu(TRYRIGHT);

					} else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
						playerControls.setQueu(TRYLEFT);
					} else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
						playerControls.setQueu(TRYDOWN);
					} else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
						playerControls.setQueu(TRYUP);
					}
				}






				/*
				if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
					player.walk(RIGHT, delta);
					System.out.println("werwer");
					//player.setX(player.getX()+0.02f);
				} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
					player.walk(LEFT, delta);

				} else {
					player.setWalking(false);
				}

				 */


				//break;

			//case Android:

				//break;
		}




				/*

				Gdx.input.setInputProcessor(detector);


				float moveAmountLR = Gdx.input.getAccelerometerY() * (SPEED/4) * delta;
				float moveAmountUD = Gdx.input.getAccelerometerX() * (SPEED/4) * delta;

				//Gdx.app.log("2334", "LR amount: "+moveAmountLR);
				//Gdx.app.log("sdfsdf", "UD amount: "+moveAmountUD);

				if ((moveAmountLR > 0))
					playerSprite.translateX(moveAmountLR);

				if ((moveAmountLR < -0))
					playerSprite.translateX(moveAmountLR);

				if ((moveAmountUD > 0))
					playerSprite.translateY(0f - moveAmountUD);

				if ((moveAmountUD < -0))
					playerSprite.translateY(0f - moveAmountUD);




*/





	}
