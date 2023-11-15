# Spring Boot reference app: Oauth2 ,kafka

### Branches:
* master : java 17 , spring boot 3.x implementation
* java_11 : java 11 ,spring boot 2.x implementation

### Features
* Controller with keycloak authentication enabled via bearer access token
* Custom jwt converter for using realm roles .
* Controller for sending authorized calls to keycloak REST API(via interceptor,using admin access token)
* kafka consumer/producer for exchanging json messages
* Controller for kafka producing
* docker-compose yaml for running newest version of zookeeper/kafka broker-JSON serialization/deserialization support
* junit tests
* swagger
* in memory db for test data
* enabled oauth2 oidc(keycloak) login for front end endpoints(thymeleaf templates)

### Docker container prerequisites
* _keycloak server_:
pull : from  **quay.io/keycloak/keycloak:22.0.5**
start a container: `docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:22.0.5 start-dev`
* _kafka broker_:
start the docker cluster:
  run in same folder where **docker-compose.yaml** is:
  `$ docker-compose up -d`


### Keycloak set up realm,realm roles,users structure:
* login to  http://localhost:8080 with admin credentials
* Create realm SpringBootKeycloak
* select created realm
* create client with client-id:login-app,valid-redirect-uris:http://localhost:8081/*
* create realm role: user
* go to the Users page and add one:username : user1 ,first name,last name,credentials->set password  
 ,role mappings->assign role user

#### for realm admin creation:
* create realm role: admin and assign realm-management roles needed
* create user adminuser (username : adminuser ,first name,last name,credentials->set password ) 
and assign the new realm roles:user,admin

### fetch user access token from keycloak admin POST (use postman)
`curl --location 'http://localhost:8080/realms/SpringBootKeycloak/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Cookie: AUTH_SESSION_ID= ...; AUTH_SESSION_ID_LEGACY= ...; JSESSIONID=...' \
--data-urlencode 'client_id=login-app' \
--data-urlencode 'username=user1' \
--data-urlencode 'password=user1' \
--data-urlencode 'grant_type=password'`

### fetch realm admin access token from keycloak admin POST(use postman)
`curl --location 'http://localhost:8080/realms/SpringBootKeycloak/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Cookie: AUTH_SESSION_ID=...; AUTH_SESSION_ID_LEGACY=...; JSESSIONID=...' \
--data-urlencode 'client_id=login-app' \
--data-urlencode 'username=adminuser' \
--data-urlencode 'password=adminuser' \
--data-urlencode 'grant_type=password' `

### send request with retrieved token example GET(use postman):
`curl --location 'http://localhost:8081/customers' \
--header 'Authorization: Bearer  <jwt access token>\
--header 'Cookie: JSESSIONID=...'`

### authenticated requests to keycloak REST api
KeycloakRestApiController uses RestTemplate with KeycloakInterceptor that 
* gets an access token with realm admin privileges
* passes the token as request header 
* and proceeds to keycloak REST API .

### kafka consume/produce from docker cluster 
Added latest kafka broker by adding docker-compose.yaml that installs zookeeper/broker in a docker cluster.

To create and start a docker container,run in same folder where **docker-compose.yaml** is:

`$ docker-compose up -d`

You can check the verbose logs of kafka broker:

`$ docker-compose logs kafka`

You can Visualize this setup with **Offset Explorer** 

You can produce json kafka messages by running the endpoint(requires keycloak authentication):

`curl --location 'http://localhost:8081/kafka-produce' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer  <jwt access token>\
--header 'Cookie: JSESSIONID=47E9532C6E46290CE764AE7D173DB13F' \
--data '{
"key":"test2",
"value":"test2V"
}'`
 
_older kafka brokers do not support headers needed for JSON serializers/deserializers such as spotify/kafka docker image._


### swagger endpoints:
* java 17 (Open-api): http://localhost:8081/swagger-ui/index.html?continue
* java 11 (springfox): http://localhost:8081/swagger-ui/
