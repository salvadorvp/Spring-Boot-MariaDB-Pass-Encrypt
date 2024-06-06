package com.westwebsystems.budgets.helputils;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtil {

    public static String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        } else {
            clientIp = clientIp.split(",")[0].trim();
        }

        try {
            InetAddress inetAddress = InetAddress.getByName(clientIp);
            if (inetAddress instanceof java.net.Inet6Address) {
                byte[] ipv4Bytes = extractIPv4FromIPv6(inetAddress.getAddress());
                if (ipv4Bytes != null) {
                    InetAddress ipv4Address = InetAddress.getByAddress(ipv4Bytes);
                    clientIp = ipv4Address.getHostAddress();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return clientIp;
    }

    private static byte[] extractIPv4FromIPv6(byte[] ipv6Address) {
        if (ipv6Address.length == 16) {
            for (int i = 0; i < 10; i++) {
                if (ipv6Address[i] != (byte) 0x00) {
                    return null;
                }
            }
            if (ipv6Address[10] == (byte) 0xff && ipv6Address[11] == (byte) 0xff) {
                byte[] ipv4Bytes = new byte[4];
                System.arraycopy(ipv6Address, 12, ipv4Bytes, 0, 4);
                return ipv4Bytes;
            }
        }
        return null;
    }
}
