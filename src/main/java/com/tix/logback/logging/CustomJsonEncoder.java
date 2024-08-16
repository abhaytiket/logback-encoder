package com.tix.logback.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.MDC;

import com.fasterxml.jackson.databind.ObjectMapper;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.EncoderBase;

public class CustomJsonEncoder extends EncoderBase<ILoggingEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] headerBytes() {
        return null;
    }

    @Override
    public byte[] encode(ILoggingEvent event) {
        try {
            LogEvent logEvent = new LogEvent(
                event.getTimeStamp(),
                event.getLevel().toString(),
                event.getLoggerName(),
                event.getThreadName(),
                event.getFormattedMessage(),
                MDC.getCopyOfContextMap()
            );
            return (objectMapper.writeValueAsString(logEvent) + "\n").getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            addError("Error encoding log event", e);
            return null;
        }
    }

    @Override
    public byte[] footerBytes() {
        return null;
    }

    // Inner class to represent the log event structure
    public static class LogEvent {
        public String timestamp;
        public String level;
        public String source;
        public String thread;
        public String message;
        public String x_request_id;
        public Map<String, String> sdc;

        public LogEvent(long timestamp, String level, String source, String thread, String message, Map<String, String> mdc) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.timestamp = sdf.format(new java.util.Date(timestamp));
            this.level = level;
            this.source = source;
            this.thread = thread;
            this.message = message;
            if (mdc != null && !mdc.isEmpty()) {
                String requestId = mdc.get("x_request_id");
                this.x_request_id = requestId != null ? requestId : "";
                mdc.remove("x_request_id");
                this.sdc = mdc;
            } else {
                this.x_request_id = "";
                this.sdc = new HashMap<>();
            }
        }
    }
}
