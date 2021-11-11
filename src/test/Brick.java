package test;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Created by filippo on 04/09/16.
 *
 */

/**
 * This class is an abstract class which is going to be used for implementation. (CementBrick, ClayBrick, SteelBrick)
 */
abstract public class Brick  {

    public static final int DEF_CRACK_DEPTH = 1;
    public static final int DEF_STEPS = 35;


    public static final int UP_IMPACT = 100;
    public static final int DOWN_IMPACT = 200;
    public static final int LEFT_IMPACT = 300;
    public static final int RIGHT_IMPACT = 400;

    private int ploy = 0;


    /**
     * This class is used for the bricks on the level to display the crack if they are not destroyed.
     */
    public class Crack{

        private static final int CRACK_SECTIONS = 3;
        private static final double JUMP_PROBABILITY = 0.7;

        public static final int LEFT = 10;
        public static final int RIGHT = 20;
        public static final int UP = 30;
        public static final int DOWN = 40;
        public static final int VERTICAL = 100;
        public static final int HORIZONTAL = 200;



        private GeneralPath crack;

        private int crackDepth;
        private int steps;


        public Crack(int crackDepth, int steps){

            crack = new GeneralPath();
            this.crackDepth = crackDepth;
            this.steps = steps;

        }



        public GeneralPath draw(){

            return crack;
        }

        public void reset(){
            crack.reset();
        }

        /**
         * This method is used to calculate and determine where to draw the crack.
         *
         * @param point the point where the ball comes in contact with.
         * @param direction the direction where the ball touch the brick.
         */
        protected void makeCrack(Point2D point, int direction){
            Rectangle bounds = Brick.this.brickFace.getBounds();

            Point impact = new Point((int)point.getX(),(int)point.getY());
            Point start = new Point();
            Point end = new Point();


            switch(direction){
                case LEFT:
                    start.setLocation(bounds.x + bounds.width, bounds.y);
                    end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height);
                    Point tmp = makeRandomPoint(start,end,VERTICAL);
                    drawCrack(impact,tmp);

                    break;
                case RIGHT:
                    start.setLocation(bounds.getLocation());
                    end.setLocation(bounds.x, bounds.y + bounds.height);
                    tmp = makeRandomPoint(start,end,VERTICAL);
                    drawCrack(impact,tmp);

                    break;
                case UP:
                    start.setLocation(bounds.x, bounds.y + bounds.height);
                    end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height);
                    tmp = makeRandomPoint(start,end,HORIZONTAL);
                    drawCrack(impact,tmp);
                    break;
                case DOWN:
                    start.setLocation(bounds.getLocation());
                    end.setLocation(bounds.x + bounds.width, bounds.y);
                    tmp = makeRandomPoint(start,end,HORIZONTAL);
                    drawCrack(impact,tmp);

                    break;

            }
        }

        /**
         * this method is used to draw the crack.
         *
         * @param start
         * @param end
         */
        //method name change
        protected void drawCrack(Point start, Point end){

            GeneralPath path = new GeneralPath();


            path.moveTo(start.x,start.y);

            double w = (end.x - start.x) / (double)steps;
            double h = (end.y - start.y) / (double)steps;

            int bound = crackDepth;
            int jump  = bound * 5;

            double x,y;

            for(int i = 1; i < steps;i++){

                x = (i * w) + start.x;
                y = (i * h) + start.y + randomInBounds(bound);

                // inMiddle(i,CRACK_SECTIONS,steps) should be inMiddle(i,steps,CRACK_SECTIONS)
                if(inMiddle(i,steps,CRACK_SECTIONS)){
                    y += jumps(jump,JUMP_PROBABILITY);
                    System.out.println(ploy++);
                }

                path.lineTo(x,y);

            }

            path.lineTo(end.x,end.y);
            crack.append(path,true);
        }

        /**
         * this method is used to give a random value between negative bound and bound.
         *
         * @param bound the bound (maximum) that it can go.
         * @return it returns a value between negative bound and bound.
         */
        private int randomInBounds(int bound){
            int n = (bound * 2) + 1;
            return rnd.nextInt(n) - bound;
        }

        /**
         * this method is used to determine if variable i will be within the value of (steps/divisions) and (steps/divisions) * (divisions - 1). if it is it will return true.
         *
         * @param i
         * @param steps
         * @param divisions
         * @return
         */
        // this is useless since the divisions is 35 and steps is 3. possibly the values are reversed (steps is divisions and divisions are steps)
        private boolean inMiddle(int i,int steps,int divisions){
            int low = (steps / divisions);
            int up = low * (divisions - 1);

            return  (i > low) && (i < up);
        }

        /**
         * this method is used to return a random number within the negative and positive bound based on a probability provided.
         *
         * @param bound
         * @param probability
         * @return
         */
        private int jumps(int bound,double probability){

            if(rnd.nextDouble() > probability)
                return randomInBounds(bound);
            return  0;

        }

        /**
         * this method is used to create a random point based on the direction provided.
         *
         * @param from
         * @param to
         * @param direction
         * @return
         */
        private Point makeRandomPoint(Point from,Point to, int direction){

            Point out = new Point();
            int pos;

            switch(direction){
                case HORIZONTAL:
                    pos = rnd.nextInt(to.x - from.x) + from.x;
                    out.setLocation(pos,to.y);
                    break;
                case VERTICAL:
                    pos = rnd.nextInt(to.y - from.y) + from.y;
                    out.setLocation(to.x,pos);
                    break;
            }
            return out;
        }

    }

    private static Random rnd;

    private String name;
    Shape brickFace;

    private Color border;
    private Color inner;

    private int fullStrength;
    private int strength;

    private boolean broken;


    /**
     * this method is used to create a brick object.
     *
     * @param name the name of the brick
     * @param pos the position of the brick
     * @param size the size of the break
     * @param border the color of the brick border
     * @param inner the inside color of brick
     * @param strength the strength of the brick. (how many hits can it take before it break)
     */
    public Brick(String name, Point pos,Dimension size,Color border,Color inner,int strength){
        rnd = new Random();
        broken = false;
        this.name = name;
        brickFace = makeBrickFace(pos,size);
        this.border = border;
        this.inner = inner;
        this.fullStrength = this.strength = strength;

    }

    protected abstract Shape makeBrickFace(Point pos,Dimension size);

    /**
     * this method is used to determine if the brick is broken.
     *
     * @param point the point where the ball comes in contact to
     * @param dir the direction where the ball comes in contact with the object.
     * @return returns a boolean value negative if the brick is broken, true if it is not.
     */
    public  boolean setImpact(Point2D point , int dir){
        if(broken)
            return false;
        impact();
        return  broken;
    }


    public abstract Shape getBrick();


    /**
     * this method is used to get the border color of the brick.
     *
     * @return returns the Color of the border of the brick.
     */
    public Color getBorderColor(){
        return  border;
    }

    /**
     * this method is used to get the internal color of he brick.
     *
     * @return returns the Color of the internal of the brick.
     */
    public Color getInnerColor(){
        return inner;
    }

    /**
     * this method is used to get the direction of impact where the ball comes in contact with the brick,
     *
     * @param b the ball object.
     * @return an Integer where the impact direction is decided.
     */
    public final int findImpact(Ball b){
        if(broken)
            return 0;
        int out  = 0;
        if(brickFace.contains(b.right))
            out = LEFT_IMPACT;
        else if(brickFace.contains(b.left))
            out = RIGHT_IMPACT;
        else if(brickFace.contains(b.up))
            out = DOWN_IMPACT;
        else if(brickFace.contains(b.down))
            out = UP_IMPACT;
        return out;
    }

    /**
     * this method is used to return the brick if it is broken or not.
     *
     * @return
     */
    public final boolean isBroken(){
        return broken;
    }

    /**
     * this method is used to repair the brick to full strength.
     */
    public void repair() {
        broken = false;
        strength = fullStrength;
    }

    /**
     * this method is used to decrement the strength of the brick and update the broken variable if it is broken or not.
     */
    public void impact(){
        strength--;
        broken = (strength == 0);
    }



}





