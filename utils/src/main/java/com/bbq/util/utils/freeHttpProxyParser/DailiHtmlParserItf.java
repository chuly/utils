package com.bbq.util.utils.freeHttpProxyParser;

import java.util.List;

import com.bbq.util.selenium.bean.HttpProxyBean;

public interface DailiHtmlParserItf {
	
	List<HttpProxyBean> parseList(String html);

}
