# POC Tjenestemonitorering med OpenTelemetry
Dette er et repo for å eksperimentere med monitorering av tjenester
ved hjelp av OpenTelemetry. OpenTelemetry brukes til å observere
applikasjoner og henter ut data som for eksempel logger, metrikker
og traces. Formålet med dette repoet er hovedsakelig for å
eksperimentere med tracing og hvordan koble tracing data opp mot 
Grafana for visualisering. 

## Oppsett
Målet med oppsettet er å bruke det enkleste setup´et som mulig for
å få visualisert traces i Grafana. 
Derfor er applikasjonene blitt instrumentert med en OpenTelemetry 
agent hver som gjør alt arbeidet for oss. Det eneste man
evt trenger å gjøre er config settings, som gjøres i 
`config.properties`. Videre er Tempo satt opp som en service i 
`docker-compose` filen, hvor den bruker et ferdig lagd Tempo image. 
Grafana må også settes opp som en service, den er også blitt
definert med et ferdig lagd image. Dette er i utgangspunktet 
det man trenger for å visualisere traces i Grafana. 

I tillegg kan man visualisere service graphs i Grafana. Da trenger
man også å sette opp en Prometheus service. 

De forskjellige servicene er forklart nærmere under.

### SpringBoot Applikasjoner
To enkle SpringBoot applikasjoner er satt opp for å simulere 
en veldig enkel versjon av svv sine tjenester.
Disse applikasjonene er:
1. Foererkort
2. Kundereg

Begge disse applikasjonene har et par enkle endepunkter som 
bruker hverandres endepunkter slik at man kan se _tracingen_ gjennom 
denne pipelinen mellom applikasjonene. 

### Grafana Tempo
Grafana Tempo er en backend for å lagre tracing data. OpenTelemetry
sender den observerte dataen inn til Tempo. Dette gjøres via en grpc 
connection. Grpc er et rammeverk som gjør at applikasjoner kan 
kommunisere med hverandre enkelt. 

### Prometheus
Prometheus er en backend for å lagre metrikkel data. OpenTelemetry 
henter ut masse metrikker fra applikasjonene som blir lagret
i Prometheus.

### Grafana
Grafana er en plattform for å visualisere data, da spesielt data
som logger, metrikker og tracing. Grafana kobler seg til Tempo
og henter ut tracing dataen og visualiser den og viser evt 
error meldinger. 

### Traffic
Traffic applikasjonen i dette repoet er en enkel Flask applikasjon
som skal simulere trafikk i de aktuelle SpringBoot applikasjonene.
Den fungerer slik at den sender masse request gjennom de 
forskjellige endepunktene for å simulere hvordan svv sine 
tjenester blir brukt til daglig, bare i en veldig liten skala.

## Hvordan komme igang
Kjører run.sh scriptet i svv-otel for å starte alle applikasjonene.
```
bash run.sh
```

Dette scriptet går gjennom alle mappene og bygger SpringBoot
applikasjonene med Gradle. I tillegg bygger den containere til
de forskjellige applikasjonene/servicene som har en Dockerfile.

### Visualisere i Grafana
Gå til `http://localhost:3000`. Dette er startsiden til Grafana
plattformen.
Naviger deg til `Data Source` og legg til Prometheus og Tempo
som data sources. Til URL connection feltet skal Prometheus ha 
`http://prometheus:9090`, og Tempo skal ha `http://tempo:3200`.
Når man legger til Tempos om en Data source, kan man i tillegg 
gå til _Advanced settings_ og legge til Prometheus som en Data source
under _Service graph_ feltet. Dette er for å kunne visualisere 
en service graph mellom applikasjonene også. 

Når dette er gjort kan du navigere deg til `Explore` og se på
tracingen som blir registrert. Altså når du endepunktene i de
forskjellige SpringBoot Applikasjonene blir brukt, skal dette
vises som en _Trace_ i Grafana. 

### Traffic Simulering
For å kjøre masse requests og simulere bruken av svv sine tjenester
kan du bruke traffic simuleringen. Den sender x antall requests til
endepunktene i x antall minutter. For å sette igang traffic
simuleringen kan du kalle på endepunktet 
`http://localhost:5000/start` via postman for eksempel. Eller så kan
du gjøre det direkt fra terminalen:
```
curl http://localhost:5000/start
```
