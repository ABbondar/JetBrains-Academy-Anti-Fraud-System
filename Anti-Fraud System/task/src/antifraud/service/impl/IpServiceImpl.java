package antifraud.service.impl;

import antifraud.dto.IpDTO;
import antifraud.exception.ip.IpNotFoundException;
import antifraud.exception.ip.IpNotValidException;
import antifraud.model.Ip;
import antifraud.repository.IpRepository;
import antifraud.service.IpService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IpServiceImpl implements IpService {

    private final IpRepository ipRepository;

    @Override
    public List<IpDTO> getAll() {
        return ipRepository.findAll()
                .stream()
                .map(IpDTO::mapToIpDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Ip save(Ip ip) {
        if (validateIpAddress(ip.getIpAddress())) {
            throw new IpNotValidException();
        }

        if (ipRepository.findAll()
                .stream()
                .anyMatch(i -> ip.getIpAddress().equals(i.getIpAddress()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return ipRepository.save(ip);
    }

    @Override
    @Transactional
    public void delete(String ipAddress) {
        if (validateIpAddress(ipAddress)) {
            throw new IpNotValidException();
        }
        var ip = ipRepository.findByIpAddress(ipAddress)
                .orElseThrow(IpNotFoundException::new);

        ipRepository.deleteIpByIpAddress(ip.getIpAddress());
    }

    private static boolean validateIpAddress(String ipAddress) {
        InetAddressValidator validator = InetAddressValidator.getInstance();
        return !validator.isValidInet4Address(ipAddress);
    }
}