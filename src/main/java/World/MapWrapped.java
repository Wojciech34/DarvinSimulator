package World;

public class MapWrapped extends AbstractMap{

    public MapWrapped(int High, int Width, int JungleWidth, int JungleHigh, int startEnergy, int moveEnergy, int plantEnergy){
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
            x = MapWidth-1;
        }
        if (x >= MapWidth){
            x = 0;
        }
        if (y < 0){
            y = MapHigh-1;
        }
        if (y >= MapHigh){
            y = 0;
        }
        return new Vector2d(x, y);
    }
}
