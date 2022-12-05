package testCodeForDNQDec;


import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;


public class multimatrix {
    public static void main(String[] args) {
        int size = 2000;
        double[][] arr1 = new double[size][size];
        double[][] arr2 = new double[size][size];
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr1[i].length; j++) {
                arr1[i][j] = Math.random();
                arr2[i][j] = Math.random();
            }
        }

        long time = System.currentTimeMillis();
        parallelMultiplyMatrix(arr1, arr2);
        int miliSeconds = (int) (System.currentTimeMillis() - time);
        int Seconds = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - time);
        int min = (int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - time);
        System.out.println("\ntime for the multiplication: parallel");
        System.out.println( miliSeconds + " in milliseconds\n" +
                            Seconds + " in seconds\n" +
                            min + " in minutes\n");

        time = System.currentTimeMillis();
        sequentialMultiply(arr1, arr2);
        miliSeconds = (int) (System.currentTimeMillis() - time);
        Seconds = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - time);
        min = (int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - time);
        System.out.println("time for the multiplication: sequential");
        System.out.println( miliSeconds + " in milliseconds\n"+
                            Seconds + " in seconds\n" +
                            min + " in minutes");
    }

    public static double[][] sequentialMultiply(double[][] arr1, double[][] arr2) {
        double[][] result = new double[arr1.length][arr1[0].length];

        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr1[i].length; j++) {
                result[i][j] = arr1[i][0] * arr2[0][j];
                for (int k = 1; k < arr1.length; k++) {
                    result[i][j] += arr1[i][k] * arr2[k][j];
                }
            }
        }
        return result;
    }

    public static double[][] parallelMultiplyMatrix(double[][] arr1, double[][] arr2) {
        double[][] result = new double[arr1.length][arr1.length];
        RecursiveAction task = new parallelMultiply(arr1, arr2, result, 0, arr1.length, 0, arr1.length);

        ForkJoinPool fork = new ForkJoinPool();
        fork.invoke(task);
        return result;
    }

    private static class parallelMultiply extends RecursiveAction {
        private final static int THRESHOLD = 1000;
        private double[][] arr1;
        private double[][] arr2;
        private double[][] result;
        private int x1;
        private int x2;
        private int y1;
        private int y2;

        public parallelMultiply(double[][] arr1, double[][] arr2, double[][] result, int positionX1, int positionX2, int positionY1, int positionY2) {
            this.arr1 = arr1;
            this.arr2 = arr2;
            this.result = result;
            this.x1 = positionX1;
            this.x2 = positionX2;
            this.y1 = positionY1;
            this.y2 = positionY2;
        }

        @Override
        protected void compute() {
            if (((x2 - x1) < THRESHOLD) || ((y2 - y1) < THRESHOLD)) {
                for (int i = x1; i < x2; i++) {
                    for (int j = y1; j < y2; j++) {
                        result[i][j] = arr1[i][0] * arr2[0][j];

                        for (int k = 1; k < arr1.length; k++) {
                            result[i][j] += arr1[i][k] * arr2[k][j];
                        }
                    }
                }
            }
            else {
                int midX = (x1 + x2) / 2;
                int midY = (y1 + y2) / 2;

                invokeAll(
                        new parallelMultiply(arr1, arr2, result, x1, midX, y1, midY),
                        new parallelMultiply(arr1, arr2, result, midX, x2, y1, midY),

                        new parallelMultiply(arr1, arr2, result, x1, midX, midY, y2),
                        new parallelMultiply(arr1, arr2, result, midX, x2, midY, y2));
            }
        }
    }
}
