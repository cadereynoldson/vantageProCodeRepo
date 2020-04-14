package analytics;

public class WindChill {
    
    /**
     * Calculates the wind chill given a temperature (degrees farenheit) and a windspeed (miles per hour). Note, this  
     * @param temperature the temperature in farenheit. 
     * @param windspeed the windspeed in miles per hour.
     * @return the calculated wind chill.
     */
    public static double calculateWindChill(double temperature, double windspeed) {
        if (windspeed < 3)
            throw new IllegalArgumentException("Windspeed cannot be negative.");
        else
            return 35.74 + (0.6215 * temperature) - (35.75 * Math.pow(windspeed, 0.16)) + (0.4275 * temperature * Math.pow(windspeed, 0.16));
    }
    
    public static void main(String[] args) {
        double w1 = calculateWindChill(50, 10);
        System.out.println("Wind chill for 50 degrees and 10 mph wind (Should be ~46): " + w1);
    }
}
