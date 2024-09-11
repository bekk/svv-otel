package bekk.svv.kundereg.service

import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class KundeService  {

    private val client: HttpClient = HttpClient.newBuilder().build()

    private var isServerDown = false

    fun switchOnOffServer() {
        isServerDown = !isServerDown
    }

    fun hentKunder(withFact: Boolean) : List<String> {
        val names = mutableListOf("Gunnar", "Per", "Silje", "Guro", "Kristian", "gyda")
        if (withFact) {
            val fact = getRandomFact()
            names.add(fact)
        }
        if (isServerDown) {
            throw Exception("Kunde Register Service is down.")
        }
        return names
    }

    fun getRandomFact(): String {
        val request =
            HttpRequest.newBuilder().uri(URI.create("https://catfact.ninja/fact")).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }
}