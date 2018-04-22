public class Vector2D 
{
	private double x;
	private double y;
	
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2D()
	{
		this(0, 0);
	}
	
	public double getX() { return x; }
	public double getY() { return y; }
	
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	
	public String toString()
	{
		String s = "";
		s += "(" + x + ", " + y + ")";
		return s;
	}
}
