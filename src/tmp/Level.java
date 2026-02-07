package tmp;

public class Level {

    int[] sequence;
    int[][] locations;
    int par;
    int best = 9999;

    public Level(int[] sequence, int[][] locations, int par) {
        this.sequence = sequence;
        this.locations = locations;
        this.par = par;
    }

    public int[] getSequence() {
        return sequence;
    }
    public int[][] getLocations() {
        return locations;
    }
    public int getPar() {
        return par;
    }
    public int getBest() {
        return best;
    }
    public void setBest(int best) {
        this.best = best;
    }

}
