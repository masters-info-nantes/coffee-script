# coffee-script
backend coffee machine

![alt tag](https://github.com/masters-info-nantes/coffee-script/raw/master/sequenceDiagram.png)

## Contrat
### Tokens
- Le premier token *tokenId* correspond à l'identifiant(nom, prénom) du possesseur du téléphone reconnue par le service d'authentification.
Il est donc renvoyer par le service d'authentification vers la machine à café puis vers le backend.
- Le deuxième token *tokenBack* correspond au token qui circule après le retour positif du premier token : si le client a bien été identifié. 
Il sert à l'achat de café ou à la recharge d'argent pour le compte donné. Il reste actif et peu être utilisé plusieurs fois tout pendant que le téléphone est posé sur le lecteur NFC.
- Si toutefois le téléphone est retiré pendant la manipulation, le token actif à ce moment là sera tout simplement supprimer de la machine à café.

### Conception du contrat
En tant que Backend, notre contrat importe peu. Nous avons néanmoins consulté un maximum de groupes afin d'apporter une homogénéité dans l'ensemble de nos contrats, puisque nous travaillons tous finalement sur le même projet.
