# BddExample3
This bdd example shows more complicated problem where domains interact with each.

##start system
###start rabbitmq
docker run -d --hostname rabbitmq --name rabbitmq -p 15671:15671 -p 567:5671 -p 5672:5672 rabbitmq:3-management


