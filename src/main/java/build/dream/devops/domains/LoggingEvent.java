package build.dream.devops.domains;

import java.util.Date;

public class LoggingEvent {
    private Date timestmp;
    private String formattedMessage;
    private String loggerName;
    private String levelString;
    private String threadName;
    private String referenceFlag;
    private String arg0;
    private String arg1;
    private String arg2;
    private String arg3;
    private String callerFilename;
    private String callerClass;
    private String callerMethod;
    private String callerLine;
    private Long eventId;

    public Date getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Date timestmp) {
        this.timestmp = timestmp;
    }

    public String getFormattedMessage() {
        return formattedMessage;
    }

    public void setFormattedMessage(String formattedMessage) {
        this.formattedMessage = formattedMessage;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getLevelString() {
        return levelString;
    }

    public void setLevelString(String levelString) {
        this.levelString = levelString;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getReferenceFlag() {
        return referenceFlag;
    }

    public void setReferenceFlag(String referenceFlag) {
        this.referenceFlag = referenceFlag;
    }

    public String getArg0() {
        return arg0;
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getArg3() {
        return arg3;
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }

    public String getCallerFilename() {
        return callerFilename;
    }

    public void setCallerFilename(String callerFilename) {
        this.callerFilename = callerFilename;
    }

    public String getCallerClass() {
        return callerClass;
    }

    public void setCallerClass(String callerClass) {
        this.callerClass = callerClass;
    }

    public String getCallerMethod() {
        return callerMethod;
    }

    public void setCallerMethod(String callerMethod) {
        this.callerMethod = callerMethod;
    }

    public String getCallerLine() {
        return callerLine;
    }

    public void setCallerLine(String callerLine) {
        this.callerLine = callerLine;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public static class ColumnName {
        public static final String TIMESTMP = "timestmp";
        public static final  String FORMATTED_MESSAGE = "formatted_message";
        public static final  String LOGGER_NAME = "logger_name";
        public static final  String LEVEL_STRING = "level_string";
        public static final  String THREAD_NAME = "thread_name";
        public static final  String REFERENCE_FLAG = "reference_flag";
        public static final  String ARG0 = "arg0";
        public static final  String ARG1 = "arg1";
        public static final  String ARG2 = "arg2";
        public static final  String ARG3 = "arg3";
        public static final  String CALLER_FILENAME = "caller_filename";
        public static final  String CALLER_CLASS = "caller_class";
        public static final  String CALLER_METHOD = "caller_method";
        public static final  String CALLER_LINE = "caller_line";
        public static final  String EVENT_ID = "event_id";
    }
}
