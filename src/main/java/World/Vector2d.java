package World;

import java.util.Objects;

public record Vector2d(int x, int y) {
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean precedes(Vector2d other) {
        if (this.x <= other.x && this.y <= other.y)
            return true;
        return false;
    }

    public boolean follows(Vector2d other) {
        if (this.x >= other.x && this.y >= other.y)
            return true;
        return false;
    }

    public Vector2d upperRight(Vector2d other) {
        int temp_x;
        int temp_y;
        if (this.x < other.x)
            temp_x = other.x;
        else
            temp_x = this.x;
        if (this.y < other.y)
            temp_y = other.y;
        else
            temp_y = this.y;
        return new Vector2d(temp_x, temp_y);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int temp_x;
        int temp_y;
        if (this.x < other.x)
            temp_x = this.x;
        else
            temp_x = other.x;
        if (this.y < other.y)
            temp_y = this.y;
        else
            temp_y = other.y;
        return new Vector2d(temp_x, temp_y);
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return that.x == this.x && that.y == this.y;
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
