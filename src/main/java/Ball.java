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

    private Color borderBallColor;
    private Color innerBallColor;

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

        setBallFace(makeBall(center,diameterA,diameterB));
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
        setBorderBallColor(border);
        setInnerBallColor(inner);
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
        center.setLocation((center.getX() + getSpeedX()),(center.getY() + getSpeedY()));

        setDisplayBallFace();

        setDirectionalPoints(getRectangularShape().getWidth(),getRectangularShape().getHeight());
    }

    /**
     * this method is used to set the ball face graphically.
     */
    private void setDisplayBallFace(){
        getRectangularShape().setFrame((center.getX() -(getRectangularShape().getWidth() / 2)),(center.getY() - (getRectangularShape().getHeight() / 2)),getRectangularShape().getWidth(),getRectangularShape().getHeight());

        setBallFace(getRectangularShape());
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
        setXSpeed(-this.getSpeedX());
    }

    /**
     * this method is used to reverse the direction where the ball is going on the y-axis.
     */
    public void reverseY(){
        setYSpeed(-this.getSpeedY());
    }

    /**
     * this is to get the color of the border of the ball.
     *
     * @return it returns a color that is the color of the border of the ball.
     */
    public Color getBorderBallColor(){
        return this.borderBallColor;
    }

    /**
     * this method is used to get the inner color of the ball.
     *
     * @return it returns a color that is the color of the in side of the ball.
     */
    public Color getInnerBallColor(){
        return this.innerBallColor;
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

        setDisplayBallFace();
    }

    /**
     * this gets the Rectangle with the shape of the ball.
     *
     * @return returns a rectangle with the shape of the ball.
     */
    private RectangularShape getRectangularShape(){
        return (RectangularShape) getBallFace();
    }

    /**
     * this method is used to set the ball direction point location.
     *
     * @param width the width of the ball.
     * @param height the height of the ball.
     */
    private void setDirectionalPoints(double width,double height){
        getUp().setLocation(center.getX(),center.getY()-(height / 2));
        getDown().setLocation(center.getX(),center.getY()+(height / 2));

        getLeft().setLocation(center.getX()-(width / 2),center.getY());
        getRight().setLocation(center.getX()+(width / 2),center.getY());
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

    /**
     * this is used to set the shape of the ball.
     *
     * @param ballFace this is the shape used to set the ball shape.
     */
    public void setBallFace(Shape ballFace) {
        this.ballFace = ballFace;
    }

    /**
     * this method is used to set the border color of the ball.
     *
     * @param border this is the color used to set the border of the ball.
     */
    public void setBorderBallColor(Color border) {
        this.borderBallColor = border;
    }

    /**
     * this method is used to set the inner color of the ball.
     *
     * @param inner this is the color used to set the inside of the ball.
     */
    public void setInnerBallColor(Color inner) {
        this.innerBallColor = inner;
    }

    /**
     * this method is used to get the ball upwards position in point 2D.
     *
     * @return this returns a point2D object for the upside of the ball.
     */
    public Point2D getUp() {
        return up;
    }

    /**
     * this method is used to get the ball downwards position in point 2D.
     *
     * @return this returns a point2D object for the downside of the ball.
     */
    public Point2D getDown() {
        return down;
    }

    /**
     * this method is used to get the ball left position in point 2D.
     *
     * @return this returns a point2D object for the left side of the ball.
     */
    public Point2D getLeft() {
        return left;
    }

    /**
     * this method is used to get the ball right position in point 2D.
     *
     * @return this returns a point2D object for the right side of the ball.
     */
    public Point2D getRight() {
        return right;
    }
}
