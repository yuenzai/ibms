//package cn.ecosync.ibms.bacnet.model;
//
//import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
//import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
//import cn.ecosync.ibms.util.CollectionUtils;
//import io.prometheus.metrics.model.registry.MultiCollector;
//import lombok.ToString;
//import org.springframework.util.Assert;
//
//import java.util.List;
//import java.util.function.BiConsumer;
//import java.util.stream.Collectors;
//
//@ToString(callSuper = true)
//public class BacnetDataAcquisition extends DeviceDataAcquisition {
//    private List<BacnetDataPoint> dataPoints;
//
//    protected BacnetDataAcquisition() {
//    }
//
//    public BacnetDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId, Long scrapeInterval, SynchronizationStateEnum synchronizationState, List<BacnetDataPoint> dataPoints) {
//        super(dataAcquisitionId, scrapeInterval, synchronizationState);
//        this.dataPoints = dataPoints;
//    }
//
//    @Override
//    public List<BacnetDataPoint> getDataPoints() {
//        return CollectionUtils.nullSafeOf(dataPoints);
//    }
//
//    @Override
//    public void newInstruments(BiConsumer<String, MultiCollector> consumer) {
//        getDataPoints().stream()
//                .collect(Collectors.groupingBy(in -> in.getDataPointId().getDeviceCode()))
//                .forEach((deviceCode, dataPoints) -> consumer.accept(deviceCode, new BacnetInstrumentation(dataPoints)));
//    }
//
//    @Override
//    public BacnetDataAcquisitionBuilder builder() {
//        return new BacnetDataAcquisitionBuilder(this);
//    }
//
//    public static class BacnetDataAcquisitionBuilder extends DeviceDataAcquisitionBuilder {
//        private List<BacnetDataPoint> dataPoints;
//
//        public BacnetDataAcquisitionBuilder(BacnetDataAcquisition dataAcquisition) {
//            this(dataAcquisition.getDataAcquisitionId(), dataAcquisition.getScrapeInterval(), dataAcquisition.getSynchronizationState(), dataAcquisition.getDataPoints());
//        }
//
//        public BacnetDataAcquisitionBuilder(DeviceDataAcquisitionId dataAcquisitionId, Long scrapeInterval, SynchronizationStateEnum synchronizationState, List<BacnetDataPoint> dataPoints) {
//            super(dataAcquisitionId, scrapeInterval, synchronizationState);
//            Assert.notNull(dataPoints, "dataPoints must not be null");
//            this.dataPoints = dataPoints;
//        }
//
//        public BacnetDataAcquisitionBuilder with(List<BacnetDataPoint> dataPoints) {
//            if (dataPoints != null) {
//                this.dataPoints = dataPoints;
//            }
//            return this;
//        }
//
//        @Override
//        public BacnetDataAcquisition build() {
//            return new BacnetDataAcquisition(dataAcquisitionId, scrapeInterval, synchronizationState, dataPoints);
//        }
//    }
//}
