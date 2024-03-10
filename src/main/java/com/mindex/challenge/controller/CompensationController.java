package com.mindex.challenge.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@RestController
@RequestMapping(path = {"/compensation"})
public class CompensationController {

  private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

  private final CompensationService compensationService;

  public CompensationController(CompensationService compensationService) {
    this.compensationService = compensationService;
  }

  @PostMapping
  public Compensation create(@RequestBody Compensation compensation) {
    LOG.debug("Received compensation create request for [{}]", compensation);

    return compensationService.create(compensation);
  }

  @GetMapping("/{employeeId}")
  public List<Compensation> findCompensationHistory(@PathVariable String employeeId) {
    LOG.debug("Received find compensation history for employeeId [{}]", employeeId);

    return compensationService.findCompensationHistory(employeeId);
  }
}
