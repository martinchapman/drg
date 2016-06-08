package buildx.utils;

public class Utils {

	private Utils() {}
	
	public static boolean debug;
	
	public static void debug( String output ) {
		
		if ( debug ) {
			
			System.out.println( output );
			
		}
		
	}
}
