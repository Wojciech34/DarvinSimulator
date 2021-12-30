package World;

public enum MapDirection {
    NORTH(1,0),
    NORTHEAST(1,1),
    EAST(1,0),
    SOUTHEAST(1,-1),
    SOUTH(0,-1),
    SOUTHWEST(-1,-1),
    WEST(-1,0),
    NORTHWEST(-1,1);

    private final Vector2d vector;
    MapDirection(int x, int y){
        this.vector = new Vector2d(x,y);
    }
    public Vector2d toUnitVector(){
        return this.vector;
    }
}
