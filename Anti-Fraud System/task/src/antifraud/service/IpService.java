package antifraud.service;

import antifraud.dto.IpDTO;
import antifraud.model.Ip;

import java.util.List;

public interface IpService {

    List<IpDTO> getAll();

    Ip save(Ip ip);

    void delete(String ipAddress);
}