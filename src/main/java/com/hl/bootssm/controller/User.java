package com.hl.bootssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hl.bootssm.domain.UserInfo;
import com.hl.bootssm.domain.resultmodel.CommonResult;
import com.hl.bootssm.service.inters.IUserService;

@RestController
@RequestMapping("/user")
public class User {
	@Autowired
	private IUserService service;
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public Object addUser(UserInfo user) {
		boolean result = false;
		int code = 200;
		try {
			result = service.addUser(user);
		} catch (Exception e) {
			code = 202;
		}
		return new CommonResult(code, result);
	}
}
