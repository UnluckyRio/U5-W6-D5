# Integrazione Cloudinary

Questo progetto utilizza **Cloudinary** per la gestione del caricamento delle immagini dei dipendenti.

## Configurazione

### 1. File env.properties

Le credenziali Cloudinary sono memorizzate nel file `src/main/resources/env.properties` che è **escluso dal repository
Git** per motivi di sicurezza.

Per configurare il progetto:

1. Copia il file template:
   ```bash
   cp src/main/resources/env.properties.example src/main/resources/env.properties
   ```

2. Modifica `env.properties` con le tue credenziali Cloudinary:
   ```properties
   cloudinary.cloud.name=YOUR_CLOUD_NAME
   cloudinary.api.key=YOUR_API_KEY
   cloudinary.api.secret=YOUR_API_SECRET
   ```

### 2. Dipendenze

La dipendenza Cloudinary è già configurata nel `pom.xml`:

```xml

<dependency>
    <groupId>com.cloudinary</groupId>
    <artifactId>cloudinary-http44</artifactId>
    <version>1.38.0</version>
</dependency>
```

## Utilizzo

### Upload di un'immagine profilo

**Endpoint:** `POST /api/dipendenti/{id}/uploadImage`

**Tipo di richiesta:** `multipart/form-data`

**Parametro:** `file` (immagine da caricare)

**Esempio con cURL:**

```bash
curl -X POST http://localhost:8080/api/dipendenti/1/uploadImage \
  -F "file=@/path/to/image.jpg"
```

**Esempio con Postman:**

1. Seleziona il metodo `POST`
2. URL: `http://localhost:8080/api/dipendenti/{id}/uploadImage`
3. Vai alla tab "Body"
4. Seleziona "form-data"
5. Aggiungi una chiave `file` di tipo "File"
6. Carica l'immagine

**Risposta di successo:**

```json
{
  "message": "Immagine caricata con successo su Cloudinary",
  "imageUrl": "https://res.cloudinary.com/your-cloud-name/image/upload/v1234567890/dipendenti/image.jpg"
}
```

### Funzionalità implementate

- ✅ **Upload automatico** su Cloudinary nella cartella `dipendenti`
- ✅ **Validazione del file** (solo immagini)
- ✅ **Eliminazione automatica** della vecchia immagine quando ne viene caricata una nuova
- ✅ **Eliminazione automatica** dell'immagine quando il dipendente viene eliminato
- ✅ **URL sicuro** (https) dell'immagine salvato nel database

## Struttura dei file

```
src/main/java/com/example/U5_W6_D5/
├── config/
│   └── CloudinaryConfig.java        # Configurazione bean Cloudinary
├── service/
│   ├── CloudinaryService.java       # Servizio per upload/delete su Cloudinary
│   └── DipendenteService.java       # Integrazione con gestione immagini
└── controller/
    └── DipendenteController.java    # Endpoint per upload immagini

src/main/resources/
├── env.properties                    # Credenziali Cloudinary (NON committato)
├── env.properties.example           # Template per le credenziali
└── application.properties           # Configurazione principale
```

## Sicurezza

⚠️ **IMPORTANTE:** Il file `env.properties` contiene informazioni sensibili ed è escluso dal repository Git tramite
`.gitignore`.

Mai committare credenziali nel repository!

## Note

- Le immagini vengono salvate nella cartella `dipendenti` su Cloudinary
- L'URL dell'immagine viene salvato nel campo `immagineProfiloPath` dell'entità Dipendente
- In caso di errori, verifica che le credenziali siano corrette nel file `env.properties`

