package com.flexicore.rules.service;

import com.flexicore.rules.model.Scenario;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;

public class LogHolder {

    private static final Map<String, Logger> loggers=new ConcurrentHashMap<>();
    private static final Logger logger=Logger.getLogger(LogHolder.class.getCanonicalName());

    public static Logger getLogger(Scenario scenario){
        return loggers.computeIfAbsent(scenario.getId(),f->createLogger(scenario));
    }

    public static void clearLogger(Scenario scenario){
        Logger scriptLogger=getLogger(scenario);
        synchronized (scriptLogger){
            closeAndRemoveLogger(scriptLogger);

            if(scenario.getLogFileResource()!=null){
                File file=new File(scenario.getLogFileResource().getFullPath());
                try (FileChannel outChan = new FileOutputStream(file, true).getChannel()) {
                    outChan.truncate(0);
                }
                catch (Exception e){
                    logger.log(Level.SEVERE,"failed clearing log file",e);
                }
            }
        }

    }
    public static class CustomFormatter extends SimpleFormatter {
        private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

        @Override
        public synchronized String format(LogRecord logRecord) {
            return String.format(format,
                    new Date(logRecord.getMillis()),
                    logRecord.getLevel().getLocalizedName(),
                    logRecord.getMessage()
            );
        }
    }

    private static void closeAndRemoveLogger(Logger logger){
        synchronized (logger){
            for (Handler handler : Arrays.asList(logger.getHandlers())) {
                handler.flush();
                handler.close();
                logger.removeHandler(handler);
            }
        }
        loggers.remove(logger.getName(),logger);

    }

    public static void flush(Logger logger){
        for (Handler handler : logger.getHandlers()) {
            handler.flush();
        }
    }

    private static Logger createLogger(Scenario scenario) {
        Logger scriptLogger = Logger.getLogger(scenario.getId());
        try {
            if (scenario.getLogFileResource() != null) {
                boolean hasFileHandler = false;
                for (Handler handler : scriptLogger.getHandlers()) {
                    if (handler instanceof FileHandler) {
                        hasFileHandler = true;
                        break;
                    }
                }
                if (!hasFileHandler) {
                    FileHandler handler = new FileHandler(scenario.getLogFileResource().getFullPath(), 0, 1, true);
                    handler.setFormatter(new CustomFormatter());
                    scriptLogger.addHandler(handler);
                    scriptLogger.setUseParentHandlers(false);

                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "failed getting script logger", e);
        }
        return scriptLogger;
    }
}
