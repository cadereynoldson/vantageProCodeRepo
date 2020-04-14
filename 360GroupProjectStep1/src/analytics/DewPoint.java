package analytics;

public class DewPoint {
    /**
     * Calculates the dew point given an air temperature
     * @param airTemperature
     * @param relativeHumidity
     * @return
     */
    public static double calculateDewPoint(double airTemperature, double relativeHumidity) {
        double lnHumidity = Math.log(relativeHumidity / 100);
        double tMult = 17.27 * airTemperature;
        double tPlus = 237.3 + airTemperature;
        return (237.3 * (lnHumidity + (tMult / tPlus))) / (17.27 - (lnHumidity + (tMult / tPlus))); 
    }
    
    public static void main(String[] args) {
        double d1 = calculateDewPoint(20, 70);
        System.out.println("Dew point of 68 degrees and 70% humidity (should be ~62): " + d1);
    }
}
