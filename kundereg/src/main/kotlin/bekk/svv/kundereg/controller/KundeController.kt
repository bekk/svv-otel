package bekk.svv.kundereg.controller

import bekk.svv.kundereg.service.KundeService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = ["*"])
class KundeController(val service: KundeService) {

    @GetMapping("/hentKunder")
    fun hentKunder(withFact: Boolean): ResponseEntity<List<String>> {
        val kunder = service.hentKunder(withFact)
        return ResponseEntity.ok(kunder)
    }

    @PostMapping("/switchOnOffServer")
    fun switchOnOffServer() {
        service.switchOnOffServer()
    }
}