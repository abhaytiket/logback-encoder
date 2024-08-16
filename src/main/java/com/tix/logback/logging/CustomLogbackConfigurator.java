package com.tix.logback.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.LoggerContext;

public class CustomLogbackConfigurator extends ContextAwareBase implements Configurator {
    
    @Override
    public Configurator.ExecutionStatus configure(LoggerContext lc) {
        CustomJsonEncoder jsonEncoder = new CustomJsonEncoder();
        jsonEncoder.setContext(lc);
        jsonEncoder.start();
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(lc);
        consoleAppender.setName("STDOUT");
        consoleAppender.setTarget("System.out");
        consoleAppender.setEncoder(jsonEncoder);
        consoleAppender.start();
        Logger rootLogger = lc.getLogger("ROOT");
        rootLogger.setLevel(Level.WARN);
        rootLogger.addAppender(consoleAppender);
        return ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY;
    }
}
