package com.vimond.errorreporting;

import com.comcast.xideo.Constants;
import com.goldengate.guice.ErrorReportSenderSharedSecret;
import com.goldengate.guice.ErrorReportSenderUrl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ErrorReportSenderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ErrorReportSender.class).to(AsyncErrorReportSenderImpl.class);
        //bind(ErrorReportSender.class).to(ErrorReportSenderImpl.class);
    }


    @Provides
    @ErrorReportSenderUrl
    public String providesErrorReportSenderUrl() {
        return Constants.ERROR_REPORT_RECEIVER_URL;
    }

    @Provides
    @ErrorReportSenderSharedSecret
    public String providesErrorReportSenderSharedSecret() {
        return Constants.ERROR_REPORT_RECEIVER_SHARED_SECRET;
    }

    @Provides
    @AsyncErrorReportSenderImpl.RealErrorReportSender
    public ErrorReportSender providesRealErrorReportSender() {
        ErrorReportSender errorReportSender = new ErrorReportSenderImpl(
                providesErrorReportSenderUrl(),
                providesErrorReportSenderSharedSecret()
        );
        return errorReportSender;
    }
}
