package Entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler Keyh;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0;
    public int standCounter=0 ;
    boolean moving = false;
    int pixelCounter=0;

    public Player (GamePanel gp , KeyHandler Keyh) {
    this.gp=gp;
    this.Keyh=Keyh;

    screenX = gp.screenWidth/2 -(gp.tileSize/2);
    screenY = gp.screenheight/2 -(gp.tileSize/2);

    solidArea = new Rectangle(1,1,46,46);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    setDefaultValues();
    getPlayerImage();
    }

    public void setDefaultValues ( ){
    worldX=gp.tileSize*23;
    worldY=gp.tileSize*21;
    speed=4;
    direction="down";
    }


    public void getPlayerImage (){

    try{

        down1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_down_1.png"));
        down2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_down_2.png"));
        up1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_up_1.png"));
        up2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_up_2.png"));
        left1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_left_1.png"));
        left2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_left_2.png"));
        right1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_right_1.png"));
        right2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_right_2.png"));

    }catch (IOException e){
        e.printStackTrace();
    }

    }

    public void update (){

        if(moving==false) {

            if (Keyh.downPressed == true || Keyh.upPressed == true
                    || Keyh.leftPressed == true || Keyh.rightPressed == true) {

                if (Keyh.upPressed == true) {
                    direction = "up";
                }

                if (Keyh.downPressed == true) {
                    direction = "down";
                }

                if (Keyh.leftPressed == true) {
                    direction = "left";
                }

                if (Keyh.rightPressed == true) {
                    direction = "right";
                }
                moving=true;

                //CHECK TILE COLLSION
                collisionOn = false;
                gp.cChecker.checkTile(this);

                //CHECK OBJECT COLLISION
                int objIndex = gp.cChecker.checkObject(this, true);
                pickUpObject(objIndex);


            }

        }
//            FOR SPRITE TO STAND STILL
            else {
                standCounter++;

                if (standCounter==20){
                    spriteNum =1;
                    standCounter=0;
                }

            }

            if(moving==true){

                //IF COLLSION IS FALSE PLAYER CAN MOVE

                if (collisionOn == false ){

                    switch (direction){

                        case "up": worldY -= speed; break;
                        case "down": worldY += speed; break;
                        case "left": worldX -= speed; break;
                        case "right": worldX += speed; break;
                    }
                }
                spriteCounter++;
                if (spriteCounter > 10) {
                    if (spriteNum == 1) {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 1;
                    }
                    spriteCounter = 0;
                }
                pixelCounter+=speed;
                if(pixelCounter == gp.tileSize){

                    moving=false;
                    pixelCounter=0;
                }

            }

    }





    public void pickUpObject (int i){

        if(i != 999){

            String objectName = gp.obj[i].name;

            switch (objectName){

                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i]=null;
                    gp.ui.showMessage("you got a key!");
                    break;

                case "Door":
                    if (hasKey>0){
                        gp.obj[i]=null;
                        gp.playSE(3);
                        hasKey--;
                        gp.ui.showMessage("you opened the door");
                    }
                    else {
                        gp.ui.showMessage("you need a key");
                    }
                    break;

                case "Boots":
                    gp.playSE(2);
                    speed+=2;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Speed up!");
                    break;

                case "Chest":
                    gp.ui.gameFinished=true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;

            }
        }

    }


    public void draw (Graphics2D g2){
//        g2.setColor(Color.white);
//        g2.fillRect(x , y , gp.tileSize , gp.tileSize );

        BufferedImage image=null;

        switch (direction) {

            case "up":
                if(spriteNum ==1) image=up1;
                if (spriteNum==2) image=up2;
            break;

            case "down":
                if(spriteNum ==1) image=down1;
                if (spriteNum==2) image=down2;
            break;

            case "left":
                if(spriteNum ==1) image=left1;
                if (spriteNum==2) image=left2;
            break;

            case "right":
                if(spriteNum ==1) image=right1;
                if (spriteNum==2) image=right2;
            break;
        }

//        // Draw solidArea Hitbox
//        g2.setColor(Color.RED);
//        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        g2.drawImage(image , screenX , screenY, gp.tileSize , gp.tileSize , null);

    }
}
