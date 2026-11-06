package zahr.amira.tp1.llm;

import java.io.Serializable;

public record LlmInteraction(String questionJson, String reponseJson, String reponseExtraite)
        implements Serializable {
}