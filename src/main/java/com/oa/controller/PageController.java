package com.oa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/* 
* @author 朱树广 
* @date 2019年8月5日 上午10:01:59 
* @version 1.0 
*/
@Controller(value="pageController")
public class PageController {
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}

	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}

	@RequestMapping("/rest/page/{page}")
	public String showEditPage(@PathVariable String page) {
		return page;
	}
}

