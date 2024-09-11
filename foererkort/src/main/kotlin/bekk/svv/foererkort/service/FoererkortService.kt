package bekk.svv.foererkort.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration

@Service
class FoererkortService  {

    private val client: HttpClient = HttpClient.newBuilder().build()
    private var isServerDown = false

    private val foerkortMap: Map<String, Boolean> = mapOf(
        "Gunnar" to true, "Silje" to false, "Guro" to true, "Per" to false
    )

    fun harFoererkort(name: String, withFact: Boolean): Boolean {
        val kunder = getKunder(withFact)
        val kunde = kunder.find { it == name }

        if (isServerDown) {
            throw Exception("Foererkort Service is down.")
        }

        return foerkortMap.getOrDefault(kunde, false)
    }

    fun getKunder(withFact: Boolean): List<String> {
        val request = HttpRequest.newBuilder().uri(URI.create("http://kundereg:8080/api/hentKunder?withFact=$withFact"))
            .timeout(Duration.ofSeconds(20)).build()
        val response = client.send(request, BodyHandlers.ofString())
        val mapper = jacksonObjectMapper()
        return mapper.readValue(response.body(), Array<String>::class.java).toList()
    }

    fun switchOnOffServer() {
        isServerDown = !isServerDown
    }
}