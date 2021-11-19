import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/**
 * this is an abstract class which is used for the rubber ball implementation.
 */
abstract public class Ball {

    private Shape ballFace;

    private Point2D center;

    Point2D up;
    Point2D down;
    Point2D left;
    Point2D right;

    private Color border;
    private Color inner;

    private int speedX;
    private int speedY;

    /**
     * this is the constructor used to create a ball object.
     *
     * @param center this is the position where the ball is to be formed.
     * @param diameterA this is the diameter of the ball based on x-axis
     * @param diameterB this is the diameter of the ball based on y-axis.
     * @param inner this is the Color for the inside of the ball.
     * @param border this is the Color for the border of the ball.
     */
    public Ball(Point2D center,int diameterA,int diameterB,Color inner,Color border){
        this.center = center;

        createDirectionalPoint2D();

        setDirectionalPoints(diameterA, diameterB);

        ballFace = makeBall(center,diameterA,diameterB);
        setColorOfBall(inner, border);
        setSpeed(0,0);
    }

    /**
     * This method is used to set the color of the ball.
     *
     * @param inner the inner color of the ball that is used to be set for the ball inner color.
     * @param border the border color of the ball that is used to be set for the ball border color.
     */
    private void setColorOfBall(Color inner,Color border){
        this.border = border;
        this.inner  = inner;
    }

    /**
     * this method is used to create the direction pointer2D objects.
     */
    private void createDirectionalPoint2D(){
        up = new Point2D.Double();
        down = new Point2D.Double();
        left = new Point2D.Double();
        right = new Point2D.Double();
    }

    protected abstract Shape makeBall(Point2D center,int diameterA,int diameterB);

    /**
     * this method is used to move the ball.
     */
    public void move(){
        RectangularShape tmp = (RectangularShape) ballFace;
        center.setLocation((center.getX() + speedX),(center.getY() + speedY));
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((center.getX() -(w / 2)),(center.getY() - (h / 2)),w,h);
        setDirectionalPoints(w,h);

        ballFace = tmp;
    }

    /**
     * this method is used to set the speed of the ball on both axis.
     *
     * @param x the speed on the x-axis.
     * @param y the speed on the y-axis.
     */
    public void setSpeed(int x,int y){
        setXSpeed(x);
        setYSpeed(y);
    }

    /**
     * this is used to set the speed of the ball on the x-axis.
     *
     * @param s speed on the x-axis.
     */
    public void setXSpeed(int s){
        this.speedX = s;
    }

    /**
     * this is used to set the speed of the ball on the y-axis.
     *
     * @param s speed on the y-axis.
     */
    public void setYSpeed(int s){
        this.speedY = s;
    }

    /**
     * this method is used to reverse the direction where the ball is going on the x-axis.
     */
    public void reverseX(){
        setXSpeed(-this.speedX);
    }

    /**
     * this method is used to reverse the direction where the ball is going on the y-axis.
     */
    public void reverseY(){
        setYSpeed(-this.speedY);
    }

    /**
     * this is to get the color of the border of the ball.
     *
     * @return it returns a color that is the color of the border of the ball.
     */
    public Color getBorderColor(){
        return this.border;
    }

    /**
     * this method is used to get the inner color of the ball.
     *
     * @return it returns a color that is the color of the in side of the ball.
     */
    public Color getInnerColor(){
        return this.inner;
    }

    /**
     * this is used to get the position of the ball.
     *
     * @return it returns a Point2D format of the position of the ball.
     */
    public Point2D getPosition(){
        return this.center;
    }

    /**
     * this method is used to get the shape of the ball.
     *
     * @return it returns the shape of the ball
     */
    public Shape getBallFace(){
        return this.ballFace;
    }

    /**
     * this method is used to move the ball to the said location.
     *
     * @param p the position in Point format where the ball is going to.
     */
    public void moveTo(Point p){
        center.setLocation(p);

        RectangularShape tmp = (RectangularShape) ballFace;
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((center.getX() -(w / 2)),(center.getY() - (h / 2)),w,h);
        ballFace = tmp;
    }

    /**
     * this method is used to set the ball direction point location.
     *
     * @param width the width of the ball.
     * @param height the height of the ball.
     */
    private void setDirectionalPoints(double width,double height){
        up.setLocation(center.getX(),center.getY()-(height / 2));
        down.setLocation(center.getX(),center.getY()+(height / 2));

        left.setLocation(center.getX()-(width / 2),center.getY());
        right.setLocation(center.getX()+(width / 2),center.getY());
    }

    /**
     * this method is used to get the speed of the ball on the x-axis.
     *
     * @return an integer of the speed of the ball on the x-axis.
     */
    public int getSpeedX(){
        return this.speedX;
    }

    /**
     * this methods is used to get the speed of the ball on the y-axis.
     *
     * @return an integer of the speed of the ball on the y-axis.
     */
    public int getSpeedY(){
        return this.speedY;
    }


}
