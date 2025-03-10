package cn.ecosync.ibms.bacnet;

import cn.ecosync.ibms.bacnet.dto.BacnetObject;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectProperties;
import cn.ecosync.ibms.bacnet.dto.BacnetProperty;
import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Double;
import com.serotonin.bacnet4j.type.primitive.*;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.PropertyValues;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BacnetUtils {
    public static final Logger log = LoggerFactory.getLogger(BacnetUtils.class);
    public static final String ENV_BACNET_IFACE = "BACNET_IFACE";

    private static LocalDevice localDevice;

    public static PropertyValues execute(BacnetReadPropertyMultipleService service) throws Exception {
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

        return RequestUtils.readProperties(localDevice, remoteDevice, refs, false, null);
    }

    public static void initialize() throws Exception {
        if (localDevice != null) {
            localDevice.terminate();
            log.atInfo().log("关闭本地设备");
        }

        log.atInfo().log("开始初始化");

        // 指定网络接口名称，例如 "eth0" 或 "wlan0"
        String interfaceName = System.getenv(ENV_BACNET_IFACE);
        if (interfaceName == null || interfaceName.isEmpty()) {
            log.atError().addKeyValue("env", ENV_BACNET_IFACE).log("环境变量未设置");
            return;
        }

        // 获取网络接口的 IP 地址和广播地址
        InetAddress localAddress = getLocalAddress(interfaceName);
        InetAddress broadcastAddress = getBroadcastAddress(interfaceName);

        if (localAddress == null || broadcastAddress == null) {
            log.atError().addKeyValue(ENV_BACNET_IFACE, interfaceName).log("无法获取网络接口的地址或广播地址");
            return;
        }

        // 创建网络配置
        IpNetwork network = new IpNetworkBuilder()
                .withLocalBindAddress(localAddress.getHostAddress()) // 设置本地 IP 地址
                .withBroadcast(broadcastAddress.getHostAddress(), IpNetwork.DEFAULT_PORT) // 设置广播地址
                .build();

        // 创建传输层
        Transport transport = new DefaultTransport(network);

        // 创建本地设备
        localDevice = new LocalDevice(ObjectIdentifier.UNINITIALIZED, transport);

        // 初始化本地设备
        localDevice.initialize();

        log.atInfo().log("初始化成功");
    }

    public static void sendWhoIs() throws Exception {
        if (localDevice == null || !localDevice.isInitialized()) {
            log.atError().log("本地设备未初始化");
            return;
        }

        log.atInfo().log("发送 WhoIs 请求");
        localDevice.sendGlobalBroadcast(new WhoIsRequest());

        TimeUnit.SECONDS.sleep(5);

        localDevice.getRemoteDevices().forEach(in ->
                log.atInfo().addKeyValue("deviceInstance", in.getInstanceNumber()).log("找到设备"));

//        localDevice.startRemoteDeviceDiscovery(in -> {
//            int remoteDeviceInstance = in.getInstanceNumber();
//            log.atInfo().addKeyValue("deviceInstance", remoteDeviceInstance).log("找到设备");
//        });
    }

    /**
     * 获取指定网络接口的本地 IP 地址
     */
    private static InetAddress getLocalAddress(String interfaceName) throws SocketException {
        NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
        if (networkInterface == null) {
            return null;
        }
        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
            InetAddress address = addresses.nextElement();
            if (!address.isLoopbackAddress() && address.isSiteLocalAddress()) {
                return address;
            }
        }
        return null;
    }

    /**
     * 获取指定网络接口的广播地址
     */
    private static InetAddress getBroadcastAddress(String interfaceName) throws SocketException {
        NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
        if (networkInterface == null) {
            return null;
        }
        for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
            if (address.getAddress() != null) {
                return address.getBroadcast();
            }
        }
        return null;
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
}
