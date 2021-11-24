import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

    public class Crack{

        public static final int LEFT = 10;
        public static final int RIGHT = 20;
        public static final int UP = 30;
        public static final int DOWN = 40;
        public final int VERTICAL = 100;
        public final int HORIZONTAL = 200;

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
            setCrackDepth(crackDepth);
            setSteps(steps);
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

            Point start = new Point();
            Point end = new Point();

            switch(direction){
                case LEFT:
                    start.setLocation(getBounds(brick).x + getBounds(brick).width, getBounds(brick).y);
                    end.setLocation(getBounds(brick).x + getBounds(brick).width, getBounds(brick).y + getBounds(brick).height);
                    drawCrack(getImpact(point),getRandomPointVertical(start,end));
                    break;
                case RIGHT:
                    start.setLocation(getBounds(brick).getLocation());
                    end.setLocation(getBounds(brick).x, getBounds(brick).y + getBounds(brick).height);
                    drawCrack(getImpact(point),getRandomPointVertical(start,end));
                    break;
                case UP:
                    start.setLocation(getBounds(brick).x, getBounds(brick).y + getBounds(brick).height);
                    end.setLocation(getBounds(brick).x + getBounds(brick).width, getBounds(brick).y + getBounds(brick).height);
                    drawCrack(getImpact(point),getRandomPointHorizontal(start,end));
                    break;
                case DOWN:
                    start.setLocation(getBounds(brick).getLocation());
                    end.setLocation(getBounds(brick).x + getBounds(brick).width, getBounds(brick).y);
                    drawCrack(getImpact(point),getRandomPointHorizontal(start,end));
                    break;
            }
        }

        private Point getImpact(Point2D point) {
            return new Point((int) point.getX(),(int) point.getY());
        }

        /**
         * this method is used to get a random point from a start point to the end point based on the direction it was given (horizontal).
         *
         * @param start the start position where the crack starts.
         * @param end
         * @return
         */
        private Point getRandomPointHorizontal(Point start, Point end){
            return makeRandomPoint(start,end,HORIZONTAL);
        }

        /**
         * this method is used to get a random point from a start point to the end point based on the direction it was given (vertical).
         *
         * @param start
         * @param end
         * @return
         */
        private Point getRandomPointVertical(Point start, Point end){
            return makeRandomPoint(start,end,VERTICAL);
        }

        /**
         * this method is used to get the brick boundaries.
         *
         * @param brick the brick object.
         * @return it returns a Rectangle object which is the boundary of the brick.
         */
        private Rectangle getBounds(Brick brick){
            return brick.getBrick().getBounds();
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

            int bound = getCrackDepth();

            double x,y;

            for(int i = 1; i < getSteps();i++){

                x = (i * getBrickWidth(start, end)) + start.x;
                y = (i * getBrickHeight(start, end)) + start.y + randomInBounds(bound);

                path.lineTo(x,y);

            }

            path.lineTo(end.x,end.y);
            getCrackPath().append(path,true);
        }

        private double getBrickHeight(Point start, Point end) {
            return (end.y - start.y) / (double)getSteps();
        }

        private double getBrickWidth(Point start, Point end) {
            return (end.x - start.x) / (double)getSteps();
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
         * this method is used to create a random point based on the direction provided.
         *
         * @param from this is the position where it begins.
         * @param to this is the position where it ends.
         * @param direction the direction using an integer constant.
         * @return it returns a random point (coordinates) on the brick.
         */
        private Point makeRandomPoint(Point from,Point to, int direction){

            Point out = new Point();
            int position;

            switch(direction){
                case HORIZONTAL:
                    position = rnd.nextInt(to.x - from.x) + from.x;
                    out.setLocation(position,to.y);
                    break;
                case VERTICAL:
                    position = rnd.nextInt(to.y - from.y) + from.y;
                    out.setLocation(to.x,position);
                    break;
            }
            return out;
        }

        public int getCrackDepth() {
            return crackDepth;
        }

        public void setCrackDepth(int crackDepth) {
            this.crackDepth = crackDepth;
        }

        public int getSteps() {
            return steps;
        }

        public void setSteps(int steps) {
            this.steps = steps;
        }
    }

