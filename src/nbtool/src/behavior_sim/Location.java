package behavior_sim;

public class Location
{
    public float x, y;
    
    public Location()
    {
        x = 0.0f;
        y = 0.0f;
    }
    
    public Location(float xCoord, float yCoord)
    {
        x = xCoord;
        y = yCoord;
    }

    public void rotate(float rad)
    {
            x = x * (float)Math.cos(rad) - 
                            y * (float)Math.sin(rad);

            y = y * (float)Math.cos(rad) + 
                            x * (float)Math.sin(rad);
    }
} 