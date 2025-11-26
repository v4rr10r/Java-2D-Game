package main;

import Entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    public final int orginalTileSize =16;
    final int scale = 3; // bcz now a days pc have high resolution
   public final int tileSize = orginalTileSize * scale;
   public  final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;  //768 pixels
    public final int screenheight = tileSize * maxScreenRow; // 576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int WorldWidth = tileSize * maxWorldCol ;
    public final int WorldHeight = tileSize * maxWorldRow ;


    int FPS =60;
    KeyHandler Keyh= new KeyHandler();
    TileManager tileM = new TileManager(this );
    Sound se = new Sound();
    Sound music = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;

    //entity and objects
    public  Player player= new Player(this , Keyh);
    public SuperObject obj[]= new SuperObject[10];


    //Set players default position

    int playerx= 100;
    int playery =100;
    int playerspeed = 4;

    public GamePanel () {

        this.setPreferredSize(new Dimension(screenWidth,screenheight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(Keyh);
        this.setFocusable(true);
        this.requestFocusInWindow();

    }

    public void setUpGame (){

        aSetter.setObject();

        playMusic(0);
    }

    public void startGameThread (){

        gameThread = new Thread(this);
        gameThread.start();
    }

//    @Override
//    public void run() {
//        double  drawInterval = 1000000000/FPS;
//        double nextDrawTime = System.nanoTime() + drawInterval;
//        while (gameThread != null){
//
//            long currentTime = System.nanoTime();
//
//
//            update();
//            //1. update : update information such as chracter position
//
//            //2. Draw : draw the screen with update information
//            repaint();
//
//
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime/1000000;
//
//                if(remainingTime<0){
//                    remainingTime=0;
//                }
//
//                Thread.sleep((long) remainingTime);
//                nextDrawTime+=drawInterval;
//
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

//DELTA METHOD FOR GAME LOOP
    @Override
    public void run() {

        double  drawInterval = 1000000000/FPS;
        double delta =0 ;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null){

            currentTime =System.nanoTime();

            delta+=(currentTime-lastTime) / drawInterval;
            lastTime=currentTime;

            if(delta>=1){

                update();
                repaint();
                delta--;
            }

        }

    }

    public void update (){
       player.update();
    }

    public void paintComponent (Graphics g){
        super.paintComponent(g);
        Graphics2D g2= (Graphics2D) g;

        //TILE
        tileM.draw(g2);

        //OBJECT
        for (int i = 0; i < obj.length; i++) {
            if (obj[i]!= null){
                obj[i].draw(g2 ,this);
            }

        }

        //PLAYER
        player.draw(g2);

        ui.draw(g2);

        g2.dispose();

    }

    public void playMusic(int i){

        music.SetFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic (){

        music.stop();
    }

    public void playSE (int i){

        se.SetFile(i);
        se.play();
    }
}
