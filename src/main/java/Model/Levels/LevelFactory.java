package Model.Levels;

public class LevelFactory {

        public WallLevelTemplates getLevel(String levelType){
            if (levelType.equalsIgnoreCase("CHAINLEVEL")) {
                return new ChainWallLevel();
            } else if (levelType.equalsIgnoreCase("TWOLINESLEVEL")) {
                return new TwoLinesWallLevel();
            } else if (levelType.equalsIgnoreCase("RANDOMLEVEL")) {
                return new RandomWallLevel();
            }
            return null;
        }


}
