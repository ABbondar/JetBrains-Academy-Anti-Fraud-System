package antifraud.controller;

import antifraud.dto.IpDTO;
import antifraud.model.Ip;
import antifraud.service.IpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/antifraud")
@RequiredArgsConstructor
public class IpController {

    private final IpService ipService;

    @GetMapping("/suspicious-ip")
    public ResponseEntity<?> getAllIpAddresses() {
        log.info("[GET] Request to read all IP-addresses");

        return ResponseEntity.ok(ipService.getAll());
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<?> saveIpAddress(@Validated @RequestBody Ip ip) {
        log.info("[POST] Request to save IP-address");

        var i = ipService.save(ip);
        return new ResponseEntity<>(IpDTO.mapToIpDTO(i), HttpStatus.OK);
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<?> deleteIpAddress(@PathVariable String ip) {
        log.info("[DELETE] Request to delete IP-address");

        ipService.delete(ip);
        return ResponseEntity.ok(Map.of(
                "status", "IP " + ip + " successfully removed!"));
    }
}