package com.tix.logback.logging;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.slf4j.MDC;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

public class CustomJsonLayout extends LayoutBase<ILoggingEvent> {
    @Override
    public String doLayout(ILoggingEvent event) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        String formattedTimestamp = Instant.ofEpochMilli(event.getTimeStamp())
                                        .atOffset(ZoneOffset.UTC)
                                        .format(formatter);
        sb.append("{\"timestamp\":\"").append(formattedTimestamp).append("\",");
        sb.append("\"level\":\"").append(event.getLevel()).append("\",");
        sb.append("\"source\":\"").append(event.getLoggerName()).append("\",");
        sb.append("\"thread\":\"").append(event.getThreadName()).append("\",");
        sb.append("\"message\":\"").append(event.getFormattedMessage()).append("\",");
        sb.append("\"x_request_id\":\"").append(event.getMDCPropertyMap().get("x_request_id")).append("\",");
        
        Map<String, String> mdcMap = MDC.getCopyOfContextMap();
        mdcMap.remove("x_request_id");
        sb.append("\"sdc\": ").append(mdcMap);
        sb.append("}\n");
        return sb.toString();
    }
}
