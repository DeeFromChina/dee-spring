package org.dee;

import cn.hutool.json.JSONUtil;
import org.dee.logging.entity.LogEntity;
import org.dee.logging.entity.ThreadLog;
import org.dee.logging.init.ReadLogFileConfiguration;
import org.dee.logging.utils.LogFileUtil;
import org.springframework.boot.actuate.autoconfigure.liquibase.LiquibaseEndpointAutoConfiguration;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;

public class TestClass {

    public static void main2(String[] args) {
        String logFilePath = "/Users/frieda.li/Desktop/code/sunline/SunERP/Application_Server/oa-kpi/${sys:system.basedir}/logs/default.log";
        Map<String, ThreadLog> map = LogFileUtil.parseLogFileNio(logFilePath);
        StringBuffer s = new StringBuffer();
        map.get("17097813003451")
                .getLogLineJson()
                .forEach(item -> s.append(item));
        System.out.println(s.toString());
        LogEntity logEntity = JSONUtil.toBean(s.toString(), LogEntity.class);
        System.out.println(logEntity.getTraceId());
//        List<LogEntity> list = LogFileUtil.convertLogEntity(map);

//        String log = "2024-03-06 15:55:00.306  INFO 72176";
//        System.out.println(log.substring(0, 23));
    }

    public static void main3(String[] args) {
        Class<?> clazz2 = ReadLogFileConfiguration.class;
        System.out.println(clazz2.isAnnotationPresent(Component.class));

        Class<?> clazz = LiquibaseEndpointAutoConfiguration.class;
        System.out.println(clazz.isAnnotationPresent(Component.class));
//        System.out.println(StrUtil.lowerFirst(LogEntity.class.getSimpleName()));
    }

    public static String getMacAddress() {
        String os = System.getProperty("os.name").toLowerCase();
        String result = "";
        if (os.contains("win")) {
            try {
                Process process = Runtime.getRuntime().exec(System.getenv("COMSPEC") + " /c ipconfig /all");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Physical Address")) {
                        result = line.substring(line.indexOf(":") + 2).trim().replaceAll(" ", "");
                        break;
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("MAC Address: " + getMacAddress());
    }

    public static void main6(String[] args) {
        try {
            // 获取本地主机
            InetAddress inetAddress = InetAddress.getLocalHost();

            // 获取网络接口枚举
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            // 遍历网络接口
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                // 如果是活动的并且不是回环接口
                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    // 获取接口的MAC地址
                    byte[] macBytes = networkInterface.getHardwareAddress();

                    if (macBytes != null) {
                        // 转换MAC地址为字符串形式
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02X%s", b, (b < macBytes.length - 1) ? "-" : ""));
                        }
                        String macAddress = sb.toString();
                        System.out.println("Interface Name : " + networkInterface.getName());
                        System.out.println("MAC Address : " + macAddress);
                    }
                }
            }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

}
