import java.awt.Color;

public class ColorPalate {
	
	public static Color GREEN = new Color(39, 174, 96);
	public static Color RED = new Color(231, 76, 60);
	public static Color ORANGE = new Color(243, 156, 18);
	public static Color BLUE = new Color(52, 152, 219);
	public static Color DARK_GRAY = new Color(33,33,33);
	public static Color GRAY = new Color(44,44,44);
	public static Color LIGHT_GRAY = new Color(66,66,66);

	public static Color randomColor() {
		Color[] choices =  {GREEN, RED, ORANGE, BLUE};
		return choices[(int) (Math.random() * 4)];
	}
}
