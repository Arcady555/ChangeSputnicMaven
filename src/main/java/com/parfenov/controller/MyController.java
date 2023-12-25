package com.parfenov.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.parfenov.model.ExchangeModel;
import com.parfenov.service.MyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
public class MyController {
    private final MyService service;

    @GetMapping("/allCurrencies/{code}")
    public String findAll(Model model, @PathVariable("code") String code) throws JsonProcessingException {
        model.addAttribute("list", service.findAllRegardingOne(code));
        return "all";
    }

    @GetMapping("/pair_exchange/{base}/{target}")
    public String pairExchange(Model model,
                               @PathVariable("base") String codeBase,
                               @PathVariable("target") String codeTarget) throws JsonProcessingException {
        ExchangeModel exchangeModel = service.pairConversion(codeBase, codeTarget);
        model.addAttribute("pairExchange", exchangeModel);
        return "pair";
    }

    @PostMapping("/exchange")
    public String exchange(@RequestParam("codeBase") String codeBase,
                           @RequestParam("codeTarget") String codeTarget) {
        return "redirect:/pair_exchange/" + codeBase + "/" + codeTarget;
    }
}