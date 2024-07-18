package org.dee.gateway.health;

import lombok.extern.slf4j.Slf4j;
import org.dee.gateway.entity.BeatInfo;
import org.dee.gateway.service.InstanceService;
import org.dee.utils.LocalDateTimeUtil;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class GatewayRegisterServerMonitor implements Closeable {

    private final ScheduledExecutorService executorService;

    private InstanceService instanceService;

    public GatewayRegisterServerMonitor(InstanceService instanceService, int threadCount) {
        this.instanceService = instanceService;
        this.executorService = new ScheduledThreadPoolExecutor(threadCount, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("org.dee.gateway.server.monitor");
                return thread;
            }
        });
    }

    @Override
    public void close() throws IOException {
        String className = this.getClass().getName();
        log.info("{} do shutdown begin", className);
        shutdownThreadPool(this.executorService, log);
        log.info("{} do shutdown stop", className);
    }

    public static void shutdownThreadPool(ExecutorService executor, Logger logger) {
        executor.shutdown();
        int retry = 3;

        while(retry > 0) {
            --retry;

            try {
                if (executor.awaitTermination(1L, TimeUnit.SECONDS)) {
                    return;
                }
            } catch (InterruptedException var4) {
                executor.shutdownNow();
                Thread.interrupted();
            } catch (Throwable var5) {
                if (logger != null) {
                    logger.error("ThreadPoolManager shutdown executor has error : {}", var5);
                }
            }
        }

        executor.shutdownNow();
    }

    public void startMonitor() {
        log.debug("======start-monitor========");
        startTask();
    }

    public void startTask() {
        this.executorService.schedule(new MonitorTask(), 5, TimeUnit.SECONDS);
    }

    class MonitorTask implements Runnable {

        public void run() {
            long currentTimeStamp = LocalDateTimeUtil.getLocalDateTimeStamp(LocalDateTime.now());
            Map<String, BeatInfo> instanceStateMap = instanceService.getInstanceStateMap();

            List<String> removeServerNames = new ArrayList<>();
            instanceStateMap.forEach((k,v) -> {
                if(currentTimeStamp > v.getLastTimeStamp()) {
                    removeServerNames.add(k);
                }
            });
            for(String serverName : removeServerNames){
                instanceStateMap.remove(serverName);
            }
            GatewayRegisterServerMonitor.this.startTask();
        }

    }

}
