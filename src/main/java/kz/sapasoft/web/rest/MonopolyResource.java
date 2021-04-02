package kz.sapasoft.web.rest;

import java.util.List;
import kz.sapasoft.dto.ListItem;
import kz.sapasoft.service.MonopolistService;
import kz.sapasoft.web.rest.errors.MonopolyException;
import kz.sapasoft.web.rest.vm.MonopolistVM;
import kz.sapasoft.web.rest.vm.TransactionVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/monopoly")
public class MonopolyResource {

    private final MonopolistService monopolistService;

    public MonopolyResource(MonopolistService monopolistService) {
        this.monopolistService = monopolistService;
    }

    @GetMapping("/drop")
    public void dropToDefault() {
        monopolistService.dropToDefault();
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginToAccount(@RequestBody MonopolistVM monopolist) {
        try {
            return ResponseEntity.ok(monopolistService.login(monopolist));
        } catch (MonopolyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorKey());
        }
    }

    @GetMapping("/get-info/{id}")
    public ResponseEntity<?> getMonopolistInfo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(monopolistService.getInfo(id));
        } catch (MonopolyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorKey());
        }
    }

    @PostMapping("/send-money")
    public ResponseEntity<Void> sendMoney(@RequestBody TransactionVM transaction) {
        monopolistService.sendMoney(transaction);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-list/{except}")
    public ResponseEntity<List<ListItem>> getListOfMonopolist(@PathVariable Long except) {
        return ResponseEntity.ok(monopolistService.getListOfMonopolist(except));
    }
}
