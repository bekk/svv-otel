package bekk.svv.foererkort.controller

import bekk.svv.foererkort.service.FoererkortService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = ["*"])
class FoererkortController(val service: FoererkortService) {

    @GetMapping("/harFoererkort")
    fun harFoerkort(name: String, withFact: Boolean): Boolean {
        return service.harFoererkort(name, withFact)
    }

    @PostMapping("/switchOnOffServer")
    fun switchOnOffServer() {
        service.switchOnOffServer()
    }
}