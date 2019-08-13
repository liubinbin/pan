package cn.liubinbin.experiment.histogram;

/**
 * Created by bin on 2019/5/17.
 */
public class FastLongHistogramUsage {

    public static void main(String[] args) {
        FastLongHistogram fastLongHistogram = new FastLongHistogram();
        for (int i = 0; i < 1000000; i++) {
            fastLongHistogram.add(i, 1);
        }

        System.out.println("count " + fastLongHistogram.getCount());
        System.out.println("getNumAtOrBelow " + fastLongHistogram.getNumAtOrBelow(77));
        System.out.println("getMax " + fastLongHistogram.getMax());
        System.out.println("getMean " + fastLongHistogram.getMean());
        System.out.println("getMin " + fastLongHistogram.getMin());
        System.out.println("Quantiles ");
        long[] quantiles = fastLongHistogram.getQuantiles();
        for (int i = 0; i < quantiles.length; i++) {
            System.out.println("quantile: " + FastLongHistogram.DEFAULT_QUANTILES[i] + " value: " + quantiles[i]);
        }
        System.out.println("custom Quantiles ");
        double[] customQuantiles = new double[]{0.2, 0.343, 0.234};
        quantiles = fastLongHistogram.getQuantiles(customQuantiles);
        for (int i = 0; i < quantiles.length; i++) {
            System.out.println("quantile: " + customQuantiles[i] + " value: " + quantiles[i]);
        }

        System.out.println("------------------------------------");
        for (double tempQ = 0.00001; tempQ < 0.3; tempQ += 0.00001) {
            double[] tempCQs = new double[]{tempQ};
            long[] tempQs = fastLongHistogram.getQuantiles(tempCQs);
            for (int i = 0; i < tempQs.length; i++) {
                System.out.println("quantile: " + tempQ + " value: " + tempQs[i]);
            }
        }

    }
}
