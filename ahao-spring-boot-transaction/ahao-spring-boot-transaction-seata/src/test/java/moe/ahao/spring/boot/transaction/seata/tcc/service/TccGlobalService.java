package moe.ahao.spring.boot.transaction.seata.tcc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.seata.spring.annotation.GlobalTransactional;
import moe.ahao.transaction.bank.transfer.dto.TransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TccGlobalService {
    @Autowired
    private OutTransferService outTransferService;
    @Autowired
    private InTransferService inTransferService;
    @Autowired
    private ObjectMapper objectMapper;

    @GlobalTransactional
    public void transfer(Long outAccountId, Long inAccountId, BigDecimal amount) throws Exception {
        TransferDTO outTransferDTO = new TransferDTO(UUID.randomUUID().toString(), outAccountId, amount);
        outTransferService.prepare(null, objectMapper.writeValueAsString(outTransferDTO));

        TransferDTO inTransferDTO = new TransferDTO(UUID.randomUUID().toString(), inAccountId, amount);
        inTransferService.prepare(null, objectMapper.writeValueAsString(inTransferDTO));
    }
}
