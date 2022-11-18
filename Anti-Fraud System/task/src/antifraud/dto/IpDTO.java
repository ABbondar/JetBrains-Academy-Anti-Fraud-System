package antifraud.dto;

import antifraud.model.Ip;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IpDTO {
    private long id;
    private String ip;

    public static IpDTO mapToIpDTO(Ip ip) {
        return new IpDTO(
                ip.getId(),
                ip.getIpAddress());
    }
}