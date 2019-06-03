package ais.dx.Congest;

public class TransTab {

    /**
     * @param shipLength
     * 将船舶长度转船成为标准船舶
     * */
    public static double translator(int shipLength) {
        if (shipLength < 30) {
            return 0.3;
        } else if (shipLength < 50) {
            return 0.5;
        } else if (shipLength < 90) {
            return 1.0;
        } else if (shipLength < 180) {
            return 2.1;
        } else {
            return 3.5;
        }
    }
}
