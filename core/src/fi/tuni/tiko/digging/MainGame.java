package fi.tuni.tiko.digging;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

public class MainGame extends Game  {

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

	//GestureDetector gestureDetector;

	//dying during episodes 3-6 will affect the highscore result
	int deathsPastEpisode2 = 0;
	int tryAgainsSinceEpisode2 = 0;


	boolean farmLevel=true;
			//myöhemmin joku oma luokkansa farmi tietysti

	GameTexture backGroundTexture;

	private GameTexture farm;
	GameTexture[] farms = new GameTexture[7];

	private SpriteBatch batch;

	private PlayerControls playerControls;

	private FreeTypeFontGenerator fontGenerator;
	private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
	private BitmapFont font;

	ResourceUI resourceUI;

	boolean restartLevelAvailable;



	private DirtPool dirtPool;
	private StonePool stonePool;
	private BlankPool blankPool;
	private DescendingPool descendingPool;
	private PermanentPool permanentPool;
	private FarmPool farmPool;
	private RootPool rootPool;
	EntranceTile entranceTile;

	private TilePools tilePools;

	private GoblinPool goblinPool;
	private SpikePool spikePool;
	private FallingTrapPool fallingTrapPool;

	private HazardPools hazardPools;

	private ResourcePool resourcePool;
	private RootResourcePool rootResourcePool;

	private ResourceAnimationPool resourceAnimationPool;
	private ArrayList<ResourceGainedAnimation> resourceAnimationList;

	private TileAnimationPools tileAnimationPools;

	//creating static graphics here, because adding textures in this didnt work in DirtTile classes body.
	// This could probably be replaced with asset manager if needed
	public static GameTexture[] dirtTextureTileset = new GameTexture[48];

	//int[][] passages;


	InfoMessageBox infoMessageBox;
	SingleSlide singleSlide;


	int totalResourcesCollected = 0;

	private OrthographicCamera camera;
	//private Cameraplacer cameraplacer;

	//private FitViewport fitViewport;

	Player player;
	//position player will be heading, these will change in ... walkthinkgs
	//int targetX;
	//int targetY;

	//to be added later

	ScreenHelper screenHelper;
	MainMenu mainMenu;
	SettingsMenu settingsMenu;
	TutorialScreen tutorialScreen;
	SingleSlideScreen singleSlideScreen;
	HighScoreScreen highScoreScreen;

	boolean [][] levelsPassed;

	boolean soundsOn;
	boolean musicOn;
	boolean languageEnglish;

	Stage currentStage;

	int episode;
	int level;

	//Settings settings

	//IMPORTANT TÄÄ allStages PITÄÄ POISTAA MYÖHEMMIN JA MUUTTAA STAGEN LUOMISEN LOGIIKKAA, EI OO KOKONAISIA STAGEJA JOISTA PIDETÄÄN KIRJAA, IHAN LIIKAA MUISTIA VIE, SEN SIJAAN TEEN TUON allLevelStats-listan
	private ArrayList<Stage> allStages;
	private ArrayList<LevelStats> allLevelsStats;




	boolean didThisAlready = false;

	//these will be used in other screens:
	float resoWidthTweaked;
	float resoHeightTweaked;

	PlayScreen playScreen;
	PlayScreenHelper playScreenHelper;

	QuestionMenuScreen questionMenuScreen;

	GameTexture[] numbers = new GameTexture[12];

	Audio audio;

	@Override
	public void create () {

		soundsOn = true;
		musicOn = true;
		languageEnglish=true;

		audio=new Audio();


		for (int i=0; i<11; i++) {
			numbers[i]=new GameTexture(new Texture("numbers/"+i+".png"));

		}
		numbers[11]=new GameTexture(new Texture("numbers/line.png"));

		//UNDIGGABLE_MARGIN_WIDTH = (Gdx.graphics.getWidth() % 7) / 2;


		//initalizing dirtTileTextureset using static GameTexture[] dirtTextureTileset array. 0-index will be left empty for convienience.
		for (int i=1; i < dirtTextureTileset.length; i++) {
			dirtTextureTileset[i]=new GameTexture(new Texture("tilesets/dirt/dirtTile"+i+".png"));
		}


		for (int i=1; i < farms.length; i++) {
			farms[i] = new GameTexture(new Texture("farm"+i+".jpg"));
		}
		farms[0] = new GameTexture(new Texture("farmDungeon.png"));

		farm = farms[1];


		batch = new SpriteBatch();
		episode = 5;
		level = 4;

		//just for testing purposes, this could only be in StageRandomizers method just as well
		//passages = new int[1][];


		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/NovaCut-Regular.ttf"));
		fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 24;
		fontParameter.borderWidth = 1;
		fontParameter.borderColor = Color.BLACK;
		fontParameter.color = Color.WHITE;
		fontParameter.flip=true;
		font = fontGenerator.generateFont(fontParameter);




		entranceTile = new EntranceTile(2, 2);




		camera = new OrthographicCamera((TILES_IN_ROWS_WITHOUT_EDGES)+2*UNDIGGABLE_MARGIN, 12.8f);




		switch (Gdx.app.getType()) {
			case Android:

				resoWidthTweaked = TILES_IN_ROWS_WITHOUT_EDGES + 2 * UNDIGGABLE_MARGIN;


				float aspectRatio = Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
				System.out.println("Resolution: " + aspectRatio);




				resoHeightTweaked = aspectRatio * resoWidthTweaked;
				//camera.setToOrtho(true, (TILES_IN_ROWS_WITHOUT_EDGES)+2*UNDIGGABLE_MARGIN, 12.8f);


				camera.setToOrtho(true, resoWidthTweaked, resoHeightTweaked);



				break;

			case Desktop:

				resoHeightTweaked=12.8f;

				resoWidthTweaked = TILES_IN_ROWS_WITHOUT_EDGES + 2 * UNDIGGABLE_MARGIN;

				camera.setToOrtho(true, resoWidthTweaked , resoHeightTweaked);



				break;
		}




		player = new Player(this);

		backGroundTexture = new GameTexture(new Texture("Background.png"));



		playerControls = new PlayerControls();




		//nämä voisi laittaa tulemaan jo valikkojen aikana/niitä ennen, jolloin itse pelin generoiminen ei vie niin paljoa aikaa
		dirtPool = new DirtPool(200, 550);
		stonePool = new StonePool(670, 900);
		blankPool = new BlankPool(50,500);
		descendingPool = new DescendingPool(50, 500);
		permanentPool = new PermanentPool(200, 500);
		farmPool = new FarmPool(7,16);

		resourcePool = new ResourcePool(12,48);
		rootResourcePool = new RootResourcePool(9, 48);

		rootPool = new RootPool(100, 400);




		tilePools=new TilePools(dirtPool, stonePool, blankPool, descendingPool, permanentPool, farmPool, resourcePool, rootPool, rootResourcePool);

		resourceAnimationPool = new ResourceAnimationPool(3, 6);

		resourceAnimationList= new ArrayList<ResourceGainedAnimation>();
		tileAnimationPools = new TileAnimationPools(resourceAnimationPool, resourceAnimationList);


		goblinPool = new GoblinPool(28,100);
		spikePool = new SpikePool (28, 100);
		fallingTrapPool = new FallingTrapPool (10, 99);

		hazardPools = new HazardPools(goblinPool, spikePool, fallingTrapPool);



		allLevelsStats = new ArrayList<LevelStats>();
		allLevelsStats.add(new LevelStats(0, 3, 1000, 0.1f));

		resourceUI=new ResourceUI(totalResourcesCollected);

		levelsPassed= new boolean[6][10];


		//pitää muuttaa tätä kaikilla ei voi olla omaa tiles arrayta
		//allStages = new ArrayList<Stage>();

		//allStages.add(new Stage(allStages.size(), 2,3,4, tilePools, hazardPools, allLevelsStats.get(0), tileAnimationPools, totalResourcesCollected, backGroundTexture )); // id 0 farm?

		singleSlide = new SingleSlide(camera, numbers);


		currentStage = new Stage(this);
		//currentStage.stageSettings = new StageSettings(episode,level);
		//allStages.add(currentStage);





		//allStages.add(new Stage(allStages.size(), 2,3,4)); // id 1? stage1

		//oikeasti myöhemmin saatetaan aloittaa 0:sta tms.
		startStage();

		//camera = new OrthographicCamera();
		//camera= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//camera = new OrthographicCamera(TILES_IN_ROWS+2*UNDIGGABLE_MARGIN, 7.44f);

		//camera = new OrthographicCamera((TILES_IN_ROWS-2)+2*UNDIGGABLE_MARGIN, 7.44f);





		//camera.setToOrtho(true, TILES_IN_ROWS+2*UNDIGGABLE_MARGIN, 1024/TILE_HEIGHT);

		//camera.setToOrtho(true, (TILES_IN_ROWS_WITHOUT_EDGES)+2*UNDIGGABLE_MARGIN, Gdx.graphics.getHeight()/TILE_HEIGHT_PIXELS);







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

		infoMessageBox = new InfoMessageBox(camera);




		playScreenHelper= new PlayScreenHelper(playerControls, allStages, allLevelsStats);

		screenHelper = new ScreenHelper(camera, player, resoWidthTweaked, resoHeightTweaked);
		mainMenu = new MainMenu(this, screenHelper);
		settingsMenu = new SettingsMenu(this, screenHelper);
		playScreen = new PlayScreen(this, screenHelper, playScreenHelper);
		tutorialScreen = new TutorialScreen(this, screenHelper, infoMessageBox);
		singleSlideScreen = new SingleSlideScreen(this, screenHelper, singleSlide);
		questionMenuScreen = new QuestionMenuScreen(this, screenHelper);
		highScoreScreen=new HighScoreScreen(this, screenHelper);

		setScreen(mainMenu);




	}

	public BitmapFont getFont () {
		return font;
	}

	public PlayScreen getPlayScreen () {
		return playScreen;
	}

	public GameTexture getFarm () {
		return farm;
	}

	public MainMenu getMainMenu () {
		return mainMenu;
	}

	public SettingsMenu getSettingsMenu () {
		return settingsMenu;
	}

	public PlayerControls getPlayerControls () {
		return playerControls;
	}

	@Override
	public void render () {

		//System.out.println("player tile pos y: "+player.getTilePosY()+", pl tile pos x: "+player.getTilePosX());

		super.render();

		/*

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

		//updateHazardOccupations is too resource heavy for phone
		//updateHazardOccupations();
		checkDeadHazards();
		checkPlayerHazardCollision();
		checkHazardHazardCollision();

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




















		*/
		/*



		batch.begin();

		batch.draw(farm,+0.9f,-4f, 7.4f, 5f);
		currentStage.draw(batch);

		tileAnimationPools.draw(batch);



		player.draw(batch);


		batch.end();

		moveCamera();

		*/
	}

	public TutorialScreen getTutorialScreen () {
		return tutorialScreen;
	}

	public void setTutorialScreen (TutorialScreen tutorialScreen) {
		this.tutorialScreen = tutorialScreen;
	}

	public SpriteBatch getBatch () {
		return batch;
	}



	@Override
	public void dispose () {
		batch.dispose();
		
	}




	//camera will follow player's y-position
	//public void moveCamera() {

	//	camera.position.y = player.getY()+3f;
		//camera.position.y++;

		//Gdx.app.log("Location", "Location: y: "+camera.position.y+", x: "+camera.position.x);

	//	camera.update();
	//}

	//starts and generates a level.
	public void startStage() {



		//player.setStageCurrentlyIn(idOfStage);

		currentStage.generateNewMap();


		//player.setY(1f-player.getHeight());
		//player.setX(0.5f);

		//TÄRKEÄÄ TÄYTYY LAITTAA METODI PALYERIIN
		//player.setX(player.getX()-player.getWidth()/2);

		//System.out.println(player.getY());


		//player.setGravityPull(currentStage.getGravity());



	}
/*
	public void checkHazardHazardCollision() {
		for (int i=0; i<currentStage.hazardList.size(); i++) {
			for (int j=i+1; j<currentStage.hazardList.size(); j++) {
				TileBasedObject hazard1 = currentStage.hazardList.get(i);
				TileBasedObject hazard2 = currentStage.hazardList.get(j);

				if (!hazard1.isVanishing() && !hazard2.isVanishing()) {
					if (hazard1.getRectangle().overlaps(hazard2.getRectangle())  ) {


						if (hazard1.getHazardStrength() > hazard2.getHazardStrength() ) {
							vanishThisHazard(hazard2);
						} else if (hazard2.getHazardStrength() > hazard1.getHazardStrength() ) {
							vanishThisHazard(hazard1);
							//in case of 2 strenght 1, goblins in each other, or spike+fallingTrap or each other
						} else {

							//in case of goblins; one falling onto another, the bottom one will vanish
							if (hazard1 instanceof Goblin && hazard2 instanceof Goblin) {

								if (hazard1.getY() > hazard2.getY()) {
									vanishThisHazard(hazard2);
								} else if (hazard2.getY() > hazard1.getY()) {
									vanishThisHazard(hazard1);
								//2 goblins walking into each other (y-position same in both)
								} else {
									Goblin goblin1 = (Goblin)hazard1;
									Goblin goblin2 = (Goblin)hazard2;

									//case if one Goblin is walking on a tile where another goblin already is standing, the walking
									//one will walk back, even if it means they will fall down if the previous tile has been digged/dug?

									//POSSIBLE BUG PLACE
									//there might be a small chance of bugs, since other statuses are not counted, falling that is just ending right then? we'll see
									if (goblin1.getStatus()==WALKING && goblin2.getStatus()==READY) {
										goblin1.turnAroundAndChangeWalkingDirection(goblin2);
									} else if (goblin1.getStatus()==READY && goblin2.getStatus()==WALKING)  {
										goblin2.turnAroundAndChangeWalkingDirection(goblin1);
									}
								}



							} else {

								vanishThisHazard(hazard1);
								vanishThisHazard(hazard2);

							}

						}

				}

				}
			}
		}


	}



 */
    //IMPORTANT ABOUT PERFORMANCE
    //this will prevent goblins trying to even start to walk towards already occupied tiles, this is something
    //that might be able to take away from game completely if it becomes too heavy to perform smoothly but it
    //would cause a bit of jökeltäminen in goblin moves :D
    public void updateHazardOccupations() {
        for (int i = 0; i < currentStage.hazardList.size(); i++) {
            TileBasedObject hazard = currentStage.hazardList.get(i);
            if (hazard.isVanishing()) {
                if (hazard instanceof HazardousWalker) {
                    ((HazardousWalker) hazard).unOccupyTile(currentStage);
                } else {
                    ((ImmobileHazard) hazard).unOccupyTile(currentStage);
                }
            // hazard is not vanishing so the tile will become occupied
            } else {
                if (hazard instanceof HazardousWalker) {
                	if (((HazardousWalker) hazard).getStatus()==WALKING || ((HazardousWalker) hazard).getStatus()==FALLING ) {
						((HazardousWalker) hazard).unOccupyTile(currentStage);
					} else {
						((HazardousWalker) hazard).occupyTile(currentStage);
					}

                } else {
                    ((ImmobileHazard) hazard).occupyTile(currentStage);

                }


            }
        }
    }

	public void checkHazardHazardCollision() {
		for (int i = 0; i < currentStage.hazardList.size(); i++) {
			for (int j = i + 1; j < currentStage.hazardList.size(); j++) {
				TileBasedObject hazard1 = currentStage.hazardList.get(i);
				TileBasedObject hazard2 = currentStage.hazardList.get(j);

				if (!hazard1.isVanishing() && !hazard2.isVanishing()) {
					if (hazard1.getRectangle().overlaps(hazard2.getRectangle())) {

						//some really bad code coming up, but it should suffice for now
						if (hazard1.getHazardStrength() > hazard2.getHazardStrength()) {
							if (hazard1.getY() != hazard2.getY()) {
								vanishThisHazard(hazard2);
							} else {
								if (hazard2 instanceof Goblin) {
									Goblin goblin = (Goblin) hazard2;
									goblin.turnAroundAndChangeWalkingDirection(hazard1);
								}
							}

						} else if (hazard2.getHazardStrength() > hazard1.getHazardStrength()) {

							if (hazard1.getHeightAdjustedY() != hazard2.getHeightAdjustedY()) {
								vanishThisHazard(hazard1);
							} else {
								if (hazard1 instanceof Goblin) {
									Goblin goblin = (Goblin) hazard1;
									goblin.turnAroundAndChangeWalkingDirection(hazard2);
								}


							}
								//in case of 2 strenght 1, goblins in each other, or spike+fallingTrap or each other
							} else {

								//in case of goblins; one falling onto another, the bottom one will vanish
								if (hazard1 instanceof Goblin && hazard2 instanceof Goblin) {

									if (hazard1.getY() > hazard2.getY()) {
										vanishThisHazard(hazard1);
									} else if (hazard2.getY() > hazard1.getY()) {
										vanishThisHazard(hazard2);
										//2 goblins walking into each other (y-position same in both)
									} else {
										Goblin goblin1 = (Goblin) hazard1;
										Goblin goblin2 = (Goblin) hazard2;

										//case if one Goblin is walking on a tile where another goblin already is standing, the walking
										//one will walk back, even if it means they will fall down if the previous tile has been digged/dug?

										//POSSIBLE BUG PLACE
										//there might be a small chance of bugs, since other statuses are not counted, falling that is just ending right then? we'll see
										if (goblin1.getStatus() == WALKING && goblin2.getStatus() == READY) {
											goblin1.turnAroundAndChangeWalkingDirection(goblin2);
										} else if (goblin1.getStatus() == READY && goblin2.getStatus() == WALKING) {
											goblin2.turnAroundAndChangeWalkingDirection(goblin1);
										}
									}


								} else {

									vanishThisHazard(hazard1);
									vanishThisHazard(hazard2);

								}

							}

						}

					}
				}
			}



	}

	public void vanishThisHazard(TileBasedObject hazard) {

		if (hazard instanceof Goblin) {
			((Goblin) hazard).vanish();
		} else if (hazard instanceof Spike) {
			((Spike) hazard).vanish();
		} else if (hazard instanceof FallingTrap) {
			((FallingTrap) hazard).vanish();
		}

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
						fallingTrap.startTriggering(episode);

						//System.out.println("TRIGGERED ;-(");
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

					currentStage.tiles[hazard.getTilePosY()][hazard.getTilePosX()].setOccupied(false);
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
				hazard.updateMovement(currentStage.tiles, delta, episode, player);

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
					//System.out.println("removed");
				}
			}

		}
	}


	public void checkPlayersUnwantedMovement() {
		//this could be done the other way but let's try doing it by allowing every action like breaking, walking etc to be finished
		//before they start falling etc against players will
		//In order for this to work, this method should be called before controlPlayer() -method, so player shouldn't be able to avoid falling any way

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
				//System.out.println("started to FALL " +amountOfTilesToFall+" tiles.");
			}

		}
	}


	public void checkPlayerHazardCollision() {

		//important: walking player set to be stronger than hazards (same as attacking)


		if (player.getStatus() != ATTACKING && player.getStatus() != WALKING) {


			for (int i = 0; i < currentStage.hazardList.size(); i++) {
				TileBasedObject hazard = currentStage.hazardList.get(i);

				boolean continues = true;

				//if (hazard instanceof Goblin) {
				//	if (player.getStatus() == ATTACKING || player.getStatus() == WALKING) {
				//		continues = false;
				//	}

				//}


				if (continues) {


					//if (hazard.getTilePosX()==player.getTilePosX() && hazard.getTilePosY()==player.getTilePosY()) {
					if (!(hazard.isVanishing()) && (hazard.getRectangle().overlaps(player.getRectangle()))) {

					/*
					boolean continuesBugfix=true;

					if (hazard instanceof Spike) {

						if (player.getDirection()==RIGHT && player.getStatus()==WALKING) {

							continuesBugfix=false;
							System.out.println("walking bugfix happened");





						}

						Spike tempHazard = (Spike)hazard;
						if (tempHazard.getX() > player.getX()) {
							tempHazard.setX(tempHazard.getX()+1.6f);
							if (!tempHazard.getRectangle().overlaps(player.getRectangle())) {
								continuesBugfix=false;
								System.out.println("temphazard spike bugfix happened");
							}
							tempHazard.setX(tempHazard.getX()-1.6f);
						}

					}

					if (continuesBugfix) {

					 */

						if (hazard instanceof Hazard && (hazard.isVanishing() == false)) {

							//modifier "mod" is used to solve problem of player dying to hazards unnecessary because of their different sizes/positions that were changed later
						/*
						float mod = 0;
						if (hazard instanceof Spike) {
							mod=2f;
						} else if (hazard instanceof FallingTrap) {
							mod=2f;
						}*/

							//if (((Hazard) hazard).getGetsDestroyedByFallingPlayer() && (player.getStatus()==FALLING) ) {
							if (((Hazard) hazard).getGetsDestroyedByFallingPlayer() && ((player.getY() + player.getHeight()) < (hazard.getY() + hazard.getHeight()))

							) {
								//System.out.println("playerY: " + player.getY());
								//System.out.println("hazardY: " + hazard.getY());
								//&& (player.getY() > hazard.getY() )) {
								((Hazard) hazard).vanish();
								//System.out.println("theyShouldVanish");
							} else {
								if (hazard.isVanishing() == true) {
									//System.out.println("zapThing");
								} else {
									zapPlayer();

								}


							}

						}


					}
				}
			}



		}





				//this is the case when player is attacking
				if (player.getStatus() == ATTACKING) {

					if (player.getAttackDirection()==LEFT) {
						player.rectangle.x = player.rectangle.x-0.3f;
					}

					for (int i = 0; i < currentStage.hazardList.size(); i++) {
						TileBasedObject hazard = currentStage.hazardList.get(i);
						if ((hazard.isVanishing() == false) && (hazard.getRectangle().overlaps(player.getRectangle()))) {

							if (player.getAttackDirection() == LEFT && (hazard.getX() < player.getX()) ||
									player.getAttackDirection() == RIGHT && (hazard.getX() > player.getX())) {
								((Hazard) hazard).vanish();
								System.out.println(i);
							} else {
								zapPlayer();
							}

						}
					}

					if (player.getAttackDirection()==LEFT) {
						player.rectangle.x = player.rectangle.x+0.3f;
					}
				}

				//case when player is walking and hazard is fallingTrap.. this just had to be added because of all the bugs that were fixed and caused this new bug

		if (player.getStatus() == WALKING ) {

			for (int i = 0; i < currentStage.hazardList.size(); i++) {
				TileBasedObject hazard = currentStage.hazardList.get(i);
				if ((hazard.isVanishing() == false) && (hazard.getRectangle().overlaps(player.getRectangle()))) {

					if (hazard instanceof FallingTrap) {
						zapPlayer();

				} else if (hazard instanceof Goblin) {

						if (player.getDirection() == LEFT && (hazard.getX() < player.getX()) ||
						player.getDirection() == RIGHT && hazard.getX() > player.getX()) {
							((Hazard) hazard).vanish();
						} else {
							zapPlayer();
						}



					}
				}
			}
		}




	}

	public void putAllBackIntoPools() {
		tilePools.putAllTilesIntoPools(currentStage.tiles);
		hazardPools.putAllHazardsIntoPool(currentStage.hazardList);


		currentStage.hazardList.clear();
		currentStage.specialTileList.clear();
		currentStage.vanishingTileList.clear();
		currentStage.resourceTileList.clear();
		resourceAnimationPool.putAllBackToPool(resourceAnimationList);

		playerControls.setQueu(NOQUEU);

	}
	//player "dies" and starts from the beginning of the stage
	public void zapPlayer() {



		//System.out.println("Zap");
		player.getZapped();
		//currentStage.dispose();

		putAllBackIntoPools();


		if (level>1) {
			level--;
		}
		if (episode >= 3) {
			deathsPastEpisode2++;
		}
		farmLevel=true;
		startStage();
	}




	public void tryPlayerRight() {

	}

	public TilePools getTilePools () {
		return tilePools;
	}

	public HazardPools getHazardPools () {
		return hazardPools;
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


	public ArrayList<LevelStats> getAllLevelsStats () {
		return allLevelsStats;
	}

	public TileAnimationPools getTileAnimationPools () {
		return tileAnimationPools;
	}
}
