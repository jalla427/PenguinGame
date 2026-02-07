package tmp;

import GameObjects.Penguin;

public class LevelCollection {

    public static Level[] levels = new Level[] {
            new Level(new int[] { 3 }, new int[][] { new int[] { 650 }, new int[] { 300 } }, 1),
            new Level(new int[] { 2, 1 }, new int[][] { new int[] { 600, 400 }, new int[] { 230, 500 } }, 2),
            new Level(new int[] { 3, 2, 1 }, new int[][] { new int[] { 400, 500, 600 }, new int[] { 500, 400, 450 } }, 6)
    };

    public static void setupLevel(int levelNum) {
        for(int i = 0; i < levels[levelNum - 1].getSequence().length; i++) {
            Handler.addPenguin(new Penguin(levels[levelNum - 1].getLocations()[0][i], levels[levelNum - 1].getLocations()[1][i], levels[levelNum - 1].getSequence()[i], false));
        }
        Game.currentSequence = levels[levelNum - 1].getSequence();
        Game.sequenceTarget = 1;
    }

    public static int getLevelPar(int levelNum) {
        return levels[levelNum - 1].getPar();
    }
    public static int getLevelBest(int levelNum) {
        return levels[levelNum - 1].getBest();
    }
    public static void setLevelBest(int levelNum, int newBest) {
        levels[levelNum - 1].setBest(newBest);
    }
}
