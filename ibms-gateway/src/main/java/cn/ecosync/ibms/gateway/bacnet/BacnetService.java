package cn.ecosync.ibms.gateway.bacnet;

import cn.ecosync.ibms.bacnet.dto.BacnetObject;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectProperties;
import cn.ecosync.ibms.bacnet.dto.BacnetProperty;
import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.*;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyMultipleAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyMultipleRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.AbortReason;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.error.ErrorClassAndCode;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Double;
import com.serotonin.bacnet4j.type.primitive.*;
import com.serotonin.bacnet4j.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BacnetService implements ApplicationRunner, DisposableBean {
    public static final Logger log = LoggerFactory.getLogger(BacnetService.class);
    public static final String ENV_BACNET_IFACE = "BACNET_IFACE";

    private LocalDevice localDevice;
    private RemoteDeviceDiscoverer remoteDeviceDiscoverer;

    public PropertyValues execute(BacnetReadPropertyMultipleService service) throws Exception {
        if (localDevice == null || !localDevice.isInitialized()) {
            log.atError().log("本地设备未初始化");
            return null;
        }

        Integer deviceInstance = service.getDeviceInstance();
        RemoteDevice remoteDevice = localDevice.getRemoteDeviceBlocking(deviceInstance);

        PropertyReferences refs = new PropertyReferences();
        for (BacnetObjectProperties bacnetDataPoint : service.getBacnetDataPoints()) {
            BacnetObject bacnetObject = bacnetDataPoint.getBacnetObject();
            ObjectIdentifier oid = new ObjectIdentifier(bacnetObject.getObjectType().getCode(), bacnetObject.getObjectInstance());
            List<BacnetProperty> properties = bacnetDataPoint.getProperties();
            for (BacnetProperty property : properties) {
                PropertyIdentifier pid = PropertyIdentifier.forId(property.getPropertyIdentifier().getCode());
                UnsignedInteger propertyArrayIndex = property.getPropertyArrayIndex()
                        .map(UnsignedInteger::new)
                        .orElse(null);
                refs.addIndex(oid, pid, propertyArrayIndex);
            }
        }
        PropertyValues propertyValues = readProperties(localDevice, remoteDevice, refs);
        log.info("{}", propertyValues);
        return propertyValues;
    }

    public void initialize() throws Exception {
        if (localDevice != null) {
            localDevice.terminate();
            log.atInfo().log("关闭本地设备");
        }

        log.atInfo().log("开始初始化");

        String interfaceName = System.getenv(ENV_BACNET_IFACE);
        if (interfaceName == null || interfaceName.isEmpty()) {
            log.atError().addKeyValue("env", ENV_BACNET_IFACE).log("环境变量未设置");
            return;
        }

        NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
        if (networkInterface == null) {
            log.atInfo().log("networkInterface is null");
            return;
        }

        InetAddress localAddress = null;
        InetAddress broadcastAddress = null;
        for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
            InetAddress address = interfaceAddress.getAddress();
            InetAddress broadcast = interfaceAddress.getBroadcast();
            log.atInfo()
                    .addKeyValue("localAddress", address)
                    .addKeyValue("broadcastAddress", broadcast)
                    .log("");
            if (!(address instanceof Inet4Address)) {
                continue;
            }
            localAddress = address;
            broadcastAddress = broadcast;
        }

        if (localAddress == null) {
            log.atError().addKeyValue(ENV_BACNET_IFACE, interfaceName).log("无法获取网络接口的地址");
        }

        if (broadcastAddress == null) {
            log.atError().addKeyValue(ENV_BACNET_IFACE, interfaceName).log("无法获取网络接口的广播地址");
        }

        if (localAddress == null || broadcastAddress == null) {
            return;
        }

        log.atInfo()
                .addKeyValue("localAddress", localAddress)
                .addKeyValue("broadcastAddress", broadcastAddress)
                .log("绑定地址");

        // 创建网络配置
        IpNetwork network = new IpNetworkBuilder()
                .withLocalBindAddress(localAddress.getHostAddress()) // 设置本地 IP 地址
                .withBroadcast(broadcastAddress.getHostAddress(), IpNetwork.DEFAULT_PORT) // 设置广播地址
                .build();

        // 创建传输层
        Transport transport = new DefaultTransport(network);
//        transport.setTimeout(3000);
//        transport.setSegTimeout(3000);

        // 创建本地设备
        localDevice = new LocalDevice(ObjectIdentifier.UNINITIALIZED, transport);

        // 初始化本地设备
        localDevice.initialize();

        log.atInfo().log("初始化成功");
    }

    public void sendWhoIs() {
        if (localDevice == null || !localDevice.isInitialized()) {
            log.atError().log("本地设备未初始化");
            return;
        }
        if (remoteDeviceDiscoverer != null) {
            remoteDeviceDiscoverer.stop();
        }
        log.atInfo().log("发送 WhoIs 请求");
        remoteDeviceDiscoverer = localDevice.startRemoteDeviceDiscovery(in -> {
            int remoteDeviceInstance = in.getInstanceNumber();
            log.atInfo().addKeyValue("deviceInstance", remoteDeviceInstance).log("找到设备");
        });
    }

    public static Number getValueAsNumber(Encodable encodable) {
        if (encodable instanceof Null) {
            return 0;
        } else if (encodable instanceof Boolean) {
            Boolean booleanValue = (Boolean) encodable;
            return booleanValue.booleanValue() ? 1 : 0;
        } else if (encodable instanceof UnsignedInteger) {
            UnsignedInteger unsignedInteger = (UnsignedInteger) encodable;
            return unsignedInteger.longValue();
        } else if (encodable instanceof SignedInteger) {
            SignedInteger signedInteger = (SignedInteger) encodable;
            return signedInteger.intValue();
        } else if (encodable instanceof Real) {
            Real real = (Real) encodable;
            return real.floatValue();
        } else if (encodable instanceof Double) {
            Double doubleValue = (Double) encodable;
            return doubleValue.doubleValue();
        } else if (encodable instanceof Enumerated) {
            Enumerated enumerated = (Enumerated) encodable;
            return enumerated.intValue();
        } else {
            return null;
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            initialize();
            sendWhoIs();
        } catch (Exception e) {
            log.atError().setCause(e).log("");
        }
    }

    @Override
    public void destroy() {
        remoteDeviceDiscoverer.stop();
        localDevice.terminate();
    }

    private static PropertyValues readProperties(LocalDevice localDevice, RemoteDevice remoteDevice, PropertyReferences refs) throws BACnetException {
        Map<ObjectIdentifier, List<PropertyReference>> properties;
        PropertyValues propertyValues = new PropertyValues();
        ReadListenerUpdater updater = new ReadListenerUpdater(null, propertyValues, refs.size());

        // Read property multiple can be used. Determine the max references
        int maxRef = remoteDevice.getMaxReadMultipleReferences();

        // If the device supports read property multiple, send them all at once, or at least in partitions.
        List<PropertyReferences> partitions = refs.getPropertiesPartitioned(maxRef);
        int counter = 0;
        while (!partitions.isEmpty()) {
            PropertyReferences partition = partitions.get(0);
            properties = partition.getProperties();
            List<ReadAccessSpecification> specs = new ArrayList<>();
            for (ObjectIdentifier oid : properties.keySet())
                specs.add(new ReadAccessSpecification(oid, new SequenceOf<>(properties.get(oid))));

            ReadPropertyMultipleRequest request = new ReadPropertyMultipleRequest(new SequenceOf<>(specs));

            ReadPropertyMultipleAck ack;
            try {
                ack = localDevice.send(remoteDevice, request).get();
                counter++;

                List<ReadAccessResult> results = ack.getListOfReadAccessResults().getValues();
                ObjectIdentifier oid;
                for (ReadAccessResult objectResult : results) {
                    oid = objectResult.getObjectIdentifier();
                    for (ReadAccessResult.Result result : objectResult.getListOfResults().getValues()) {
                        updater.increment(remoteDevice.getInstanceNumber(), oid, result.getPropertyIdentifier(),
                                result.getPropertyArrayIndex(), result.getReadResult().getDatum());
                        if (updater.cancelled())
                            break;
                    }

                    if (updater.cancelled())
                        break;
                }

                partitions.remove(0);
            } catch (ServiceTooBigException e) {
                if (partition.size() < 2)
                    throw e;

                // Reduce the device's max references.
                remoteDevice.reduceMaxReadMultipleReferences(partition.size());

                // Create a new PropertyReferences instance from the remaining references.
                PropertyReferences remaining = new PropertyReferences(partitions);

                // Repartition the remaining requests.
                partitions = remaining.getPropertiesPartitioned(remoteDevice.getMaxReadMultipleReferences());
            } catch (AbortAPDUException e) {
                AbortReason abortReason = e.getApdu().getAbortReason();
                log.atWarn().addKeyValue("abortReason", abortReason).log("Chunked request failed");
                if (AbortReason.bufferOverflow.equals(abortReason) || AbortReason.segmentationNotSupported.equals(abortReason)) {
                    if (partition.size() < 2) {
                        throw e;
                    }

                    // Reduce the device's max references.
                    remoteDevice.reduceMaxReadMultipleReferences(partition.size());

                    // Create a new PropertyReferences instance from the remaining references.
                    PropertyReferences remaining = new PropertyReferences(partitions);

                    // Repartition the remaining requests.
                    partitions = remaining.getPropertiesPartitioned(remoteDevice.getMaxReadMultipleReferences());
                } else {
                    throw new BACnetException("Completed " + counter + " requests. Excepted on: " + request, e);
                }
            } catch (BACnetTimeoutException e) {
                if (counter == 0) {
                    // For the first request, rethrow the exception
                    throw e;
                }
                // Otherwise, populate the properties with errors.
                RequestUtils.populateWithError(remoteDevice, properties, updater, new ErrorClassAndCode(ErrorClass.device, ErrorCode.timeout));
                partitions.remove(0);
            } catch (ErrorAPDUException e) {
                log.atError().setCause(e).log("");
                RequestUtils.populateWithError(remoteDevice, properties, updater, e.getError());
                partitions.remove(0);
            } catch (BACnetException e) {
                throw new BACnetException("Completed " + counter + " requests. Excepted on: " + request, e);
            }
            if (updater.cancelled()) {
                break;
            }
        }
        return propertyValues;
    }
}
