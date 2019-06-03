package ais.dx.Congest;

public class Density {

    public static double density(double param1, double param2, int[] shipsLength){
        double coef = 4 / (Math.PI * param1 * param2);

        double numeratorItem = 0.0;
        double denominator = 0.0;
        for (int i = 0; i < shipsLength.length; ++ i) {
            if (shipsLength[i] == 0) {
                continue;
            }
            numeratorItem += TransTab.translator(shipsLength[i]) / Math.pow(shipsLength[i], 2);
            denominator += TransTab.translator(shipsLength[i]);
        }

        return coef * numeratorItem / denominator;
    }
}
