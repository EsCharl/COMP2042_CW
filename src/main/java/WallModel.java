public class WallModel {
    private static final int CLAY = 1;
    private static final int STEEL = 2;
    private static final int CEMENT = 3;
    private static final int REINFORCED_STEEL = 4;

    private static WallModel uniqueWallModel;

    public static WallModel singletonWallModel(){
        if(getUniqueWallModel() == null){
            setUniqueWallModel(new WallModel());
        }
        return getUniqueWallModel();
    }

    public static WallModel getUniqueWallModel() {
        return uniqueWallModel;
    }

    public static void setUniqueWallModel(WallModel uniqueWallModel) {
        WallModel.uniqueWallModel = uniqueWallModel;
    }

    private WallModel(){

    }

    public int getClayIntegerConstant(){
        return CLAY;
    }

    public int getSteelIntegerConstant(){
        return STEEL;
    }

    public int getCementIntegerConstant(){
        return CEMENT;
    }

    public int getReinforcedSteelIntegerConstant(){
        return REINFORCED_STEEL;
    }

}
