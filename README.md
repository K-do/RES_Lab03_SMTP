# Laboratoire 3 - SMTP

Auteurs: Alexandra Cerottini & Miguel Do Vale Lopes

Date: 29.04.2021



## Description du projet

Ce laboratoire a été réalisé dans le cadre d'un cours de réseaux (RES) suivi à la HEIG-VD, une Haute-École de Suisse.

Le but de ce laboratoire est de développer une application client (TCP) en Java qui va utiliser une Socket API pour communiquer avec un serveur SMTP.

Cette application consiste en l'envoi de pranks par email à différents groupes contenant une liste de victimes.



## Instructions pour la configuration du serveur SMTP fictif avec Docker

Dans ce laboratoire, il nous a été recommandé d'utiliser MockMock server sur GitHub. Il disponible sur le lien suivant: https://github.com/tweakers/MockMock.  C'est un serveur SMTP multi-plateforme permettant de tester si des emails sont envoyés sans les envoyer réellement. Il nous fournit une interface web qui ressemble à une boîte mail nous permettant de consulter ce qu'on aurait envoyé si nous dirigions nos emails vers de vrais clients. Cette interface web ne nous permet pas d'envoyer des emails.

Pour installer MockMock server, il faut cloner le repos se trouvant sur le lien mentionné plus haut avec la commande `git clone` dans un terminal sur notre machine. Ensuite, il faut mettre à jour le pom.xml car une dépendance n'est plus à jour. Pour se faire, il suffit d'ouvrir le pom.xml et de remplacer le plugin contenant le groupId ***org.dstovall*** par ***com.jolira***. 

![image-20210429101509133](/home/alexandra/.config/Typora/typora-user-images/image-20210429101509133.png)

Il faut donc remplacer ce qui est en rouge sur l'image par ce qui est en vert.

De plus, il faut supprimer les différentes lignes en rouge ci-dessous:

![image-20210429101557393](/home/alexandra/.config/Typora/typora-user-images/image-20210429101557393.png)

Pour démarrer le serveur, il faut ouvrir un terminal à l'emplacement des fichiers du serveur MockMock et faire un `mvn clean install`. Ensuite il faut aller dans le fichier *target* et lancer `java -jar MockMock-1.4.0.one-jar.jar -p 2525`.  2525 est le port de notre choix. Par défaut, le serveur MockMock écoute sur le port 25 mais celui-ci peut entraîner quelques soucis car il faut des droits administrateur sur certains systèmes pour y permettre l'écoute. Si le port souhaite être changé, il faut également le modifier dans le fichier `config.properties` (voir prochaine section).

Lorsque le serveur est démarré, la boîte mail du serveur MockMock peut être consultée dans un navigateur en tapant `localhost:8282`.  Si un autre port que le 8282 souhaite être utilisé, il faut relancer le fichier *.jar* avec la commande `java -jar MockMock-1.4.0.one-jar.jar -h <port>` en remplaçant *<port>* par le port souhaité.

![image-20210429170003485](../../../../../.config/Typora/typora-user-images/image-20210429170003485.png)

Voici à quoi ressemble l'interface web lorsqu'elle est lancée. Celle-ci peut directement être utilisée avec notre programme.

Si l'on souhaite implémenter le serveur MockMock avec Docker...........



##### TODO:  Comment installer mockmock + docker



## Configuration du projet

Pour envoyer un prank à une liste de victimes, il suffit de modifier trois fichiers dans le dossier *config* principal.

### config.properties

Ce fichier contient la configuration principale du programme.

![image-20210429172627885](../../../../../.config/Typora/typora-user-images/image-20210429172627885.png)

**smtpServerAddress**: permet de spécifier l'adresse du serveur SMTP. Si vous souhaitez utiliser le serveur MockMock, spécifier `localhost`.  

:warning: Ce champ est obligatoire. 

**smtpServerPort**: permet de spécifier le numéro de port pour l'écoute du serveur SMTP.

 

### messages.utf8

lalala

### victims.json

lalala



## Description de notre implémentation



