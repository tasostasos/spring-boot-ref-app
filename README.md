# Spring Boot reference app: Oauth2 ,kafka

* enabled oauth2 oidc(keycloak) login for thymeleaf pages
* Controller with keycloak authentication enabled via bearer access token
* Custom jwt converter for using realm roles .
* Controller for sending authorized calls to keycloak REST API(via interceptor,using admin access token)
* in memory db for test data
* kafka consumer/producer for exchanging json messages
* Controller for kafka producing
* docker-compose yaml for running newest version of zookeeper/kafka broker-JSON serialization/deserialization support
* junit tests
* postman collection with required endpoints


## Authorization server info(keycloak)

Pulled docker image from  **quay.io/keycloak/keycloak:22.0.5**
to start a container:
`docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:22.0.5 start-dev`
running on http://localhost:8080

to create keycloak realms/roles/users:
* login to  http://localhost:8080 with admin credentials
* Create realm SpringBootKeycloak
* select created realm
* create client with client-id:login-app,valid-redirect-uris:http://localhost:8081/*
* create realm role: user
* go to the Users page and add one:username : user1 ,first name,last name,credentials->set password  
 ,role mappings->assign role user

for realm admin creation:
* create realm role: admin and assign realm-management roles needed
* create user adminuser (username : adminuser ,first name,last name,credentials->set password ) 
and assign the new realm roles:user,admin

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
 
Added latest kafka broker by adding docker-compose.yaml that installs zookeeper and kafka in a docker cluster:
To create and start a docker container,run in same folder where **docker-compose.yaml** is:
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

All required endpoints for testing the application are in src/main/resources/postman/endpoints.json (import in postman)