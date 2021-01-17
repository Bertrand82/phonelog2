Ptit'crm

Cette application fait un lien entre les appels et sms entrants et sortants d'un téléphone android et un agenda disponible sur le telephone.
Dans la terminologie google, un agenda est un "calendar".

Un appel entrant ou sortant a un cycle de vie : Chaque appel est géré par le telephone puis inscrit dans une base de donnée embarquée sur le téléphone.
Ptit'crm a un "Observer" sur cette base de donnée , récupère les paramètres de l'appel, les enrichit éventuellement avec des données issus des reférentiel des "contacts", demande à l'utilisateur de rajouter éventuellement l'appel, et l'inscrit comme un évènement dans un agenda sélectionné.
Cet agenda est réparti, consultable partout, et sécurisé.

Des visuels permettent de gérer ces calendars.

Lors d'un appel entrant , l'historique des appels est présenté avant le décrochage par l'utilisateur, qui peut avoir l'historique des commentaires qu'il a rajouté.

Lors de la reception d'un appel, une notification est envoyé à un serveur, ce qui permet à ne application de présenter l'historique sur un ecran d'ordinateur plus détaillé, et eventuellement de le consolider avec d'autres données.

Architecture :
Il y a 2 activités: Une MainActivity , qui a la responsabilité de gérer les autorisations et de lancer la deuxième activités qui affiche le fragment demandé.