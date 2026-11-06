# Utilisation de l'IA et problèmes rencontrés (TP 1)

## Utilisation de l'IA

J'ai utilisé l'assistant Claude comme aide ponctuelle, surtout pour comprendre des messages d'erreur et vérifier que mon code respectait l'énoncé. Le code de `JsonAdapterPourGemini` est celui du prof, je ne l'ai pas modifié.

## Problème 1 : le serveur ne se relançait plus (port 4848)

**Ce qui s'est passé :** après des modifications de `index.xhtml`, rien ne changeait dans le navigateur. En voulant relancer l'application, IntelliJ affichait :

> Error running 'tp1'. Address localhost:4848 is already in use

**Cause :** un ancien serveur Payara tournait encore en arrière-plan et occupait le port 4848. Le navigateur parlait donc à l'ancienne instance, qui n'avait jamais rechargé mes nouveaux fichiers. Cela explique pourquoi mes corrections semblaient ne servir à rien.

**Ce que j'ai fait :** j'ai ouvert le terminal d'IntelliJ et cherché quel processus utilisait le port :

```
netstat -ano | findstr :4848
```

Le résultat montrait `LISTENING 8908` : 8908 est le PID du processus. Je me suis d'abord trompée en tapant un PID d'exemple (`12345`), ce qui a donné « processus introuvable ». J'ai ensuite utilisé le vrai numéro :

```
taskkill /PID 8908 /F
```

J'ai revérifié avec `netstat` : plus rien n'écoutait sur les ports 4848 et 8080. Après avoir relancé avec **Run**, le serveur a démarré avec ma dernière version de la page.

**Ce que j'ai retenu :** quand une modification n'a aucun effet, il faut vérifier que le serveur a bien été redéployé. Avant de relancer, je dois arrêter proprement l'ancien Payara.

## Problème 2 : alignement de la page avec le mode debug (fausse alerte)

**Ce qui s'est passé :** sans le mode debug, la page était bien alignée. Dès que les deux zones JSON (requête et réponse) s'affichaient, la colonne « Conversation » ne se trouvait plus en face de « Question ». J'ai cru que mon code avait un problème.

**Ce que j'ai compris :** j'ai demandé à l'IA de m'expliquer ce que je voyais. Ce n'était pas une erreur dans mon code : j'avais mal interprété le résultat.

**Ce que j'ai fait :** aucune modification du code n'a été nécessaire.

**Ce que j'ai retenu :** avant de modifier du code, il faut vérifier que l'affichage est vraiment un bug, et pas un comportement normal que j'ai mal interprété.

## Problème 3 : le JSON ne s'affichait pas

**Cause :** dans la version du TP, le panel debug dépend de `bb.debug`, qui n'est mis à jour qu'à l'envoi du formulaire. Si j'activais le switch **après** avoir envoyé la question, rien n'apparaissait.

**Solution :** activer le switch en premier, puis cliquer sur « Envoyer la question ». Les deux zones JSON apparaissent alors sous le switch.

## Autres problèmes plus petits

| Problème | Cause | Solution |
|---|---|---|
| `class LlmInteraction is public, should be declared in a file named LlmInteraction.java` | Le record était dans un fichier au mauvais nom | Créer un fichier `LlmInteraction.java` |
| `cannot find symbol class JsonAdapterPourGemini` dans `Bb` | Import manquant | Ajouter l'import dans `Bb` |
| Erreur « La valeur est incorrecte » sur le rôle de l'API | J'avais retiré `editable="true"` | Le remettre, comme dans le TP |
| Erreur 503 « high demand » | Modèle Gemini surchargé, pas un bug de mon code | Attendre et renvoyer la question |
| `thoughtSignature` dans le JSON de la requête | Comportement du modèle Gemini : la réponse est recopiée telle quelle dans l'historique | Aucun changement, c'est lié au modèle |

## Vérifications avant le push

- Ma clé secrète n'est pas dans le code : elle est lue par `System.getenv("GEMINI_KEY")`, et `git grep -n "AIza"` ne renvoie rien.
- Le dossier `target` n'est pas dans le dépôt (`.gitignore`).
