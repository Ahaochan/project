package moe.ahao.tend.consistency.core.utils;


import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 获取本机ip
 **/
@Slf4j
public class NetUtils {
    /**
     * 获取当前节点的地址信息
     *
     * @return 地址信息
     */
    public static String getCurrentPeerAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address
                        // loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                        && !ip.isLoopbackAddress()
                        && !ip.getHostAddress().contains(":")) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取本机ip地址时，发生异常", e);
        }
        return null;
    }

}
