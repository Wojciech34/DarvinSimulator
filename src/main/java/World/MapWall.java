package World;


public class MapWall extends AbstractMap {

    public MapWall(int High, int Width, int JungleWidth, int JungleHigh, int startEnergy, int moveEnergy, int plantEnergy){
        this.MapWidth = Width;
        this.MapHigh = High;
        this.JungleLowerLeft = new Vector2d((Width - JungleWidth)/2,(High - JungleHigh)/2);
        this.JungleUpperRight = new Vector2d((Width + JungleWidth)/2, (High + JungleHigh)/2);
        this.startEnergy = startEnergy;
        this.requiredEnergyToBeParent = startEnergy/2;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
    }



    @Override
    public Vector2d howToMove(Vector2d position) {
        int x = position.getX();
        int y = position.getY();
        if (x < 0){
            x = 0;
        }
        if (x >= MapWidth){
            x = MapWidth - 1;
        }
        if (y < 0){
            y = 0;
        }
        if (y >= MapHigh){
            y = MapHigh - 1;
        }
        return new Vector2d(x, y);
    }
}
