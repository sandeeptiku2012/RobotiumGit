package com.vimond.errorreporting;

public interface ErrorReportSender {

    public static ErrorReportSender singleton = null;

    public void send(String errorMsg, Throwable e);
}
