# Chiffrage
Pour le chiffrage

Besoins : 
  - Il faut que les ordinateurs sur lequels seront lancés les clients (deux ou trois si l'on veut un client qui ne verra pas les messages déchiffrés) soient connectés sur le même réseau que l'ordinateur avec le serveur.
  - Choisir un port (exemple 8080, Port).
  - Obtenir l'IP de l'ordinateur du serveur (IPServeur).
  - Lancer le serveur puis le nombre de clients voulus.

Lancement serveur :
  - Aller à l'emplacement : ~/Chiffrage/ReseauTextChatServeur/bin
  - Lancer la commande : java Chat Port

Lancement client :
  - Aller à l'emplacement : ~/Chiffrage/ReseauTextChatClient/bin
  - Lancer la commande : java Client Port IPServeur 

Fonctionnement :
  - Pour partager la clé publique d'un client aux autres clients, envoyer le message "/cp" une fois ce client connecté au serveur.
  - Un client ne peut garder qu'une seule clé publique d'un autre client, cette clé est remplacé à chaque réception d'une nouvelle clé publique externe.
  - Tout les clients recoivent les messages, affichent le message chiffré reçu, essaient de le déchiffrer et affichent le résultat.
  - Seul le client ayant la clé privée correspondant à la clé publique utilisé pour le chiffrage peut le déchiffrer.
