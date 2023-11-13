# Spring Boot reference app: Oauth2 ,kafka

* oauth2 oidc(keycloak) login
* keycloak authentication via bearer token requests
* custom jwt converter for fetching realm roles
* user info/claims from token
* in memory db for testing
* kafka exchanging json messages
* docker-compose yaml for running newer version of zookeeper/kafka broker for JSON serialization support


## Authorization server info(keycloak)

local docker image container from  **quay.io/keycloak/keycloak:22.0.5**
running on http://localhost:8080
to start a container:
`docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:22.0.5 start-dev`
to create keycloak realms/roles/users:
* login to  http://localhost:8080 with admin credentials
* Create realm SpringBootKeycloak
* select created realm
* create client with client-id:login-app,valid-redirect-uris:http://localhost:8081/*
* create realm role: user
* go to the Users page and add one:username : user1 ,first name,last name,credentials->set password  
 ,role mappings->assign role user

## fetch token from keycloak admin POST

`curl --location 'http://localhost:8080/realms/SpringBootKeycloak/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Cookie: AUTH_SESSION_ID=a04034ca-a9e1-4875-8ebb-50a569276ced; AUTH_SESSION_ID_LEGACY=a04034ca-a9e1-4875-8ebb-50a569276ced; JSESSIONID=D84539D9C12AD9BC48F9D1297751365D' \
--data-urlencode 'client_id=login-app' \
--data-urlencode 'username=user1' \
--data-urlencode 'password=user1' \
--data-urlencode 'grant_type=password'`

## send request with retrieved token example GET:

`curl --location 'http://localhost:8081/customers' \
--header 'Authorization: Bearer  <jwt access token>\
--header 'Cookie: JSESSIONID=D84539D9C12AD9BC48F9D1297751365D'`

## authenticated requests to keycloak REST api
KeycloakRestApiController uses RestTemplate with KeycloakInterceptor that gets an access token for admin-cli client,
authenticates via bearer token to keycloak admin and fetches the REST api info

## kafka consume/produce from docker cluster
 
Added latest kafka broker by adding docker-compose.yaml that installs zookeper and kafka in a docker cluster:
in same folder where **docker-compose.yaml** is:
`$ docker-compose up -d`
Additionally, we can also check the verbose logs while the containers are starting up and verify that the Kafka server is up:
`$ docker-compose logs kafka | grep -i started`
use the Kafka Tool GUI utility to establish a connection with our newly created Kafka server, and later, we’ll visualize this setup:
**Offset Explorer**
We must note that we need to use the Bootstrap servers property to connect to the Kafka server listening at port 29092 for the host machine.

Finally, we should be able to visualize the connection on the left sidebar:
You can produce json kafka messages by running the endpoint:
`curl --location 'http://localhost:8081/kafka-produce' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer  <jwt access token>\
--header 'Cookie: JSESSIONID=47E9532C6E46290CE764AE7D173DB13F' \
--data '{
"key":"test2",
"value":"test2V"
}'`
Have in mind that the endpoint requires keycloak authentication
##older kafka brokers do not support headers needed for JSON serializers/deserializers such as spotify/kafka docker image.
(Json Serializers pass headers such as _TypeId_ ,older kafka brokers throw 'Magic v1 do not support headers')