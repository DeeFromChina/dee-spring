package org.dee.gateway.register.beat;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dee.gateway.proxy.GatewayProxy;
import org.dee.utils.LocalDateTimeUtil;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class BeatReactor implements Closeable {

    private final ScheduledExecutorService executorService;

    private GatewayProxy serverProxy;

    private Map<String, BeatInfo> dom2Beat = new HashMap<>();

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

    public BeatReactor(GatewayProxy serverProxy, int threadCount) {
        this.serverProxy = serverProxy;
        this.executorService = new ScheduledThreadPoolExecutor(threadCount, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("org.dee.gateway.server.beat.sender");
                return thread;
            }
        });
    }

    public void addBeatInfo(BeatInfo beatInfo) {
        log.info("[BEAT] adding beat: {} to beat map.", beatInfo);
        startTask(beatInfo, beatInfo.getPeriod());
    }

    public void removeBeatInfo(BeatInfo beatInfo) {
        log.info("[BEAT] removing beat: {}:{}:{} from beat map.", new Object[]{beatInfo.getServiceName(), beatInfo.getServerAddr()});
        BeatInfo info = this.dom2Beat.remove(beatInfo.getId());
        if (info != null) {
            info.setStopped(true);
        }
    }

    public void startTask(BeatInfo beatInfo, long nextTime) {
        LocalDateTime lastDateTime = LocalDateTimeUtil.offset(LocalDateTime.now(), nextTime + 11, ChronoUnit.SECONDS);
        beatInfo.setLastTimeStamp(LocalDateTimeUtil.getLocalDateTimeStamp(lastDateTime));
        this.executorService.schedule(new BeatTask(beatInfo), nextTime, TimeUnit.SECONDS);
    }

    class BeatTask implements Runnable {
        BeatInfo beatInfo;

        public BeatTask(BeatInfo beatInfo) {
            this.beatInfo = beatInfo;
        }

        public void run() {
            if (!this.beatInfo.isStopped()) {
                long nextTime = this.beatInfo.getPeriod();
                try {
                    //发送心跳请求
                    serverProxy.sendBeat(beatInfo);
                    if(!dom2Beat.containsKey(beatInfo.getId())){
                        dom2Beat.put(beatInfo.getId(), beatInfo);
                    }
                } catch (Exception e) {
                    log.error("[CLIENT-BEAT] failed to send beat: {}, unknown exception msg: {}", new Object[]{JSONUtil.toJsonStr(this.beatInfo), e.getMessage(), e});
                } finally {
                    BeatReactor.this.startTask(beatInfo, nextTime);
                }
            }
        }
    }

}
