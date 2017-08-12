package com.sf.service.interceptors;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncodingLoggingOutInterceptor extends LoggingOutInterceptor
{
	private Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public void handleMessage(Message message) throws Fault
	{
		String encoding = System.getProperty("file.encoding");
        encoding = encoding == null || encoding.equals("") ? "UTF-8" : encoding;
        
        log.debug("encoding : " + encoding);
        message.put(Message.ENCODING, encoding);
		super.handleMessage(message);
	}
	
}