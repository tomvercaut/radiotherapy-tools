package tv.radiotherapy.tools.dicom.xml;

/**
 * An interface defining if an algorithm is using a parallelised algorithm using multiple threads to handle the workload.
 */
public interface ParallelAlgorithm {
    /**
     * Define if an algorithm uses a parallel approach to process the workload (faster).
     *
     * @param enableParallelAlgorithm true if a parallel algorithm should be used, false otherwise.
     */
    void enableParallel(boolean enableParallelAlgorithm);

    /**
     * Check if the algorithm uses a parallel approach to process the workload.
     *
     * @return True if the algorithm uses mutliple threads / cores.
     */
    boolean isParallel();
}
