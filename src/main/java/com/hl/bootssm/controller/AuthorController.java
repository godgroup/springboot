package com.hl.bootssm.controller;

import com.hl.bootssm.domain.AuthorInfo;
import com.hl.bootssm.service.inters.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author static
 */
@RestController
@RequestMapping("/author")
public class AuthorController {
    @Autowired
    private IAuthorService service;

    @GetMapping("/one")
    public AuthorInfo getOne(@RequestParam("id") Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return service.getOne(id);
    }
}