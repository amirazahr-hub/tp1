package zahr.amira.tp1.llm;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;

/**
 * Gère l'accès REST à l'API de Gemini.
 * La clé secrète est lue dans la variable d'environnement GEMINI_KEY.
 */
@ApplicationScoped
public class LlmClientPourGemini implements Serializable {

    private static final String MODELE = "gemini-3.5-flash-lite";
    private static final String URL =
            "https://generativelanguage.googleapis.com/v1beta/models/" + MODELE + ":generateContent";

    // La clé secrète est récupérée dans le constructeur.
    private final String key;

    public LlmClientPourGemini() {
        this.key = System.getenv("GEMINI_KEY");
    }

    /**
     * Envoie la requête POST à l'API de Gemini.
     */
    public Response envoyerRequete(Entity<?> entity) {
        Client clientRest = ClientBuilder.newClient();
        WebTarget target = clientRest.target(URL).queryParam("key", this.key);
        Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
        return request.post(entity);
    }
}
