import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 15;
    static final int PADDLE_HEIGHT = 100;
    static final int PADDLE_WIDTH = 16;

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    // Constructor
    GamePanel(){
        newPaddle();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    // Methods to create new ball and new paddels
    public void newBall(){
        random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2), random.nextInt(GAME_HEIGHT-BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
    }
    public void newPaddle(){
        paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    // Graphics
    public void paint(Graphics g){
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }
    public void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }

    // Game physics
    public void move(){
        paddle1.move();
        paddle2.move();
        ball.move();
    }
    public void checkCollision(){
        // Ball bounces off borders
        if(ball.y <= 0){
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y >= GAME_HEIGHT-BALL_DIAMETER){
            ball.setYDirection(-ball.yVelocity);
        }
        // Ball bounces off paddles
        if(ball.intersects(paddle1)){
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // incrases ball velocity with each bounce
            if(ball.yVelocity > 0){
                ball.yVelocity++; // increase ball velocity with each bounce
            }
            else{
                ball.yVelocity--;
            }
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(ball.intersects(paddle2)){
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // incrases ball velocity with each bounce
            if(ball.yVelocity > 0){
                ball.yVelocity++; // increase ball velocity with each bounce
            }
            else{
                ball.yVelocity--;
            }
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        // Stops paddles
        if(paddle1.y<=0){
            paddle1.y=0;
        }
        if(paddle1.y>=(GAME_HEIGHT-PADDLE_HEIGHT)){
            paddle1.y=GAME_HEIGHT-PADDLE_HEIGHT;
        }
        if(paddle2.y<=0){
            paddle2.y=0;
        }
        if(paddle2.y>=(GAME_HEIGHT-PADDLE_HEIGHT)){
            paddle2.y=GAME_HEIGHT-PADDLE_HEIGHT;
        }

        // Give player a point
        if(ball.x <= 0){
            score.player2++;
            newPaddle();
            newBall();
            System.out.println("PLayer 2: " + score.player2);
        }
        if(ball.x >= GAME_WIDTH-BALL_DIAMETER){
            score.player1++;
            newPaddle();
            newBall();
            System.out.println("Player 1: " + score.player1);
        }
    }
    public void run(){
        // Game loop
        long lastTime = System.nanoTime();
        double amountofTicks = 60.0;
        double ns = 1000000000/amountofTicks;
        double delta = 0;
        while(true){
            long now = System.nanoTime();
            delta += (now - lastTime)/ns;
            lastTime = now;
            if(delta >= 1){
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    // Action listener
    public class AL extends KeyAdapter{
        public void keyPressed (KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }
        public void keyReleased (KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
