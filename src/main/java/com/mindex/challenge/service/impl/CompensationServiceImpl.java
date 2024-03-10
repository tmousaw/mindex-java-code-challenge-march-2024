package com.mindex.challenge.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationServiceImpl implements CompensationService {

  private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

  private final CompensationRepository compensationRepository;

  public CompensationServiceImpl(CompensationRepository compensationRepository) {
    this.compensationRepository = compensationRepository;
  }

  @Override
  public Compensation create(Compensation compensation) {
    LOG.debug("Creating compensation [{}]", compensation);

    return compensationRepository.insert(compensation);
  }

  @Override
  public List<Compensation> findCompensationHistory(String employeeId) {
    LOG.debug("Reading compensation history for employeeId [{}]", employeeId);

    return compensationRepository.findByEmployeeId(employeeId);
  }
}
