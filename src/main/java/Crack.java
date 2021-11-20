import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

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

        private static Random rnd = new Random();

        /**
         * this is the constructor used to create the crack object.
         *
         * @param crackDepth this is the depth of crack which is going to be initialized.
         * @param steps this is the step variable which is going to be initialized.
         */
        public Crack(int crackDepth, int steps){

            crack = new GeneralPath();
            this.crackDepth = crackDepth;
            this.steps = steps;
        }

        /**
         * this method is used to get the Crack Path properties.
         *
         * @return a GeneralPath that is the path
         */
        public GeneralPath getCrackPath(){

            return crack;
        }

        /**
         * this method is used to reset the crack path.
         */
        public void reset(){
            crack.reset();
        }

        /**
         * This method is used to calculate and determine where to draw the crack.
         *
         * @param point the point where the ball comes in contact with.
         * @param direction the direction where the ball touch the brick.
         */
        protected void makeCrack(Point2D point, int direction, Brick brick){
            Rectangle bounds = brick.getBrick().getBounds();

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
         * @param start this is the start point where the crack is going to start.
         * @param end this is the end point where the crack is going to end.
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

                // inMiddle(i,CRACK_SECTIONS,steps) should be inMiddle(i,steps,CRACK_SECTIONS) THIS NEEDS FURTHER CHECKING.
                if(inMiddle(i,CRACK_SECTIONS,steps)){
                    y += jumps(jump,JUMP_PROBABILITY);
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
         * @return it returns a boolean value depending if
         */
        // this is useless since the divisions is 35 and steps is 3. possibly the values are reversed (steps is divisions
        // and divisions are steps during usage)
        private boolean inMiddle(int i,int steps,int divisions){
            int low = (steps / divisions);
            int up = low * (divisions - 1);

            return  (i > low) && (i < up);
        }

        /**
         * this method is used to return a random number within the negative and positive bound based on a probability provided.
         *
         * @param bound the value which is used to pass into randomInBounds method.
         * @param probability the probability that it will occur.
         * @return returns a random value from randomInBounds based on a probability.
         */
        private int jumps(int bound,double probability){

            if(rnd.nextDouble() > probability)
                return randomInBounds(bound);
            return  0;

        }

        /**
         * this method is used to create a random point based on the direction provided.
         *
         * @param from this is the position where it begins.
         * @param to this is the position where it ends.
         * @param direction the direction in integer constant.
         * @return it returns a random point (coordinates) on the brick.
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

