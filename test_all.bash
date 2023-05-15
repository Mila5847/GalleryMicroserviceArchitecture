#!/usr/bin/env bash
#
# Sample usage:
#   ./test_all.bash start stop
#   start and stop are optional
#
#   HOST=localhost PORT=7000 ./test-em-all.bash
#
# When not in Docker
#: ${HOST=localhost}
#: ${PORT=7000}

# When in Docker
# shellcheck disable=SC2223
: ${HOST=localhost}
# shellcheck disable=SC2223
: ${PORT=8080}

#array to hold all our test data ids
allTestExhibitionsIds=()
allTestPaintingIds=()
allTestSculptureIds=()
allTestGalleryIds=()

function assertCurl() {

  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%{http_code}\""
  # shellcheck disable=SC2155
  local result=$(eval $curlCmd)
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpCode" = "$expectedHttpCode" ]
  then
    if [ "$httpCode" = "200" ]
    then
      echo "Test OK (HTTP Code: $httpCode)"
    else
      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
    fi
  else
      echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
      echo  "- Failing command: $curlCmd"
      echo  "- Response Body: $RESPONSE"
      exit 1
  fi
}

function assertEqual() {

  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    exit 1
  fi
}

#have all the microservices come up yet?
function testUrl() {
    # shellcheck disable=SC2124
    url=$@
    if curl $url -ks -f -o /dev/null
    then
          echo "Ok"
          return 0
    else
          echo -n "not yet"
          return 1
    fi;
}

#prepare the test data that will be passed in the curl commands for posts and puts
function setupTestdata() {

#CREATE SOME EXHIBITION TEST DATA - THIS WILL BE USED FOR THE POST REQUEST
#all use galleryId ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8

body=\
'{ "exhibitionName": "new exhibition",
"roomNumber" : 213, "duration" : 120,
"startDay": "Monday", "endDay": "Wednesday",
"paintings":
[ { "paintingId": "6ae9eaf7-5ec4-45da-a85d-45a317e711a2",
"title": "The Two Fridas", "yearCreated": 1939,
"painterId": "0e1482bb-67a8-4620-842b-3f7bfb7ee175",
"galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8" },
{ "paintingId": "d2d6b05f-9cfb-4a54-ba3e-57dffe0fd5c6",
"title": "Mona Lisa", "yearCreated": 1506, "painterId":
"ede04dc9-9cf9-4191-8b4e-7d91234cb49c", "galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8" }
], "sculptures": [ { "sculptureId": "2872b8a2-e691-4115-891c-bed7187392d0", "title": "Hand",
"material": "Clay", "texture": "Bumpy", "galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8" },
{"sculptureId": "29e803f5-c8aa-475c-832e-8edbb2336778", "title": "Torso", "material": "Clay",
"texture": "Rough", "galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8" } ] }'


    recreateExhibitionAggregate 1 "$body" "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8"

#CREATE SOME GALLERY TEST DATA - THIS WILL BE USED FOR THE POST REQUEST
body=\
'{
     "name": "Art Is Art",
     "openFrom": "Thursday",
     "openUntil": "Sunday",
     "streetAddress": "86 Gateway Center",
     "city": "Kargowa",
     "province": "Quebec",
     "country": "Georgia",
     "postalCode": "66-120"
 }'
    recreateGallery 1 "$body"

}

#USING EXHIBITION TEST DATA - EXECUTE POST REQUEST
function recreateExhibitionAggregate() {
    local testId=$1
    local aggregate=$2
    local galleryId=$3

    #create the exhibition aggregates and record the generated exhibitionIds
    exhibitionId=$(curl -X POST http://$HOST:$PORT/api/v1/exhibitions/galleries/$galleryId -H "Content-Type:
    application/json" --data "$aggregate" | jq '.exhibitionId')
    allTestExhibitionsIds[$testId]=$exhibitionId
    echo "Added Exhibition Aggregate with exhibitionId: ${allTestExhibitionsIds[$testId]}"
}

function recreateGallery() {
    local testId=$1
    local aggregate=$2

    #create the gallery aggregates and record the generated galleryIds
    galleryId=$(curl -X POST http://$HOST:$PORT/api/v1/galleries -H "Content-Type:
    application/json" --data "$aggregate" | jq '.galleryId')
    allTestGalleryIds[$testId]=$galleryId
    echo "Added Gallery Aggregate with galleryId: ${allTestGalleryIds[$testId]}"
}

#don't start testing until all the microservices are up and running
function waitForService() {
    # shellcheck disable=SC2124
    url=$@
    echo -n "Wait for: $url... "
    n=0
    until testUrl $url
    do
        n=$((n + 1))
        if [[ $n == 100 ]]
        then
            echo " Give up"
            exit 1
        else
            sleep 6
            echo -n ", retry #$n "
        fi
    done
}

#start of test script
set -e

echo "HOST=${HOST}"
echo "PORT=${PORT}"

# shellcheck disable=SC2199
if [[ $@ == *"start"* ]]
then
    echo "Restarting the test environment..."
    echo "$ docker-compose down"
    docker-compose down
    echo "$ docker-compose up -d"
    docker-compose up -d
fi

#try to delete an entity/aggregate that you've set up but that you don't need. This will confirm that things are working
#I've set up an inventory with no vehicles in it
waitForService curl -X DELETE http://$HOST:$PORT/api/v1/exhibitions

setupTestdata

#EXECUTE EXPLICIT TESTS AND VALIDATE RESPONSE
## Verify that a normal get by id of earlier posted gallery works
echo -e "\nTest 1: Verify that a normal get by id of earlier posted gallery works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/galleries/${allTestGalleryIds[1]} -s"
assertEqual ${allTestGalleryIds[1]} $(echo $RESPONSE | jq .galleryId)
#

## Verify that a normal post of gallery works
echo -e "\nTest 2: Verify that a normal post of gallery works"
assertCurl 201 "curl -X POST http://$HOST:$PORT/api/v1/galleries -H \"Content-Type: application/json\" --data '{ \"name\": \"Art\", \"openFrom\": \"Thursday\", \"openUntil\": \"Sunday\", \"streetAddress\": \"797 rue perroer \", \"city\": \"Brossarf\", \"province\": \"Quebec\", \"country\": \"Canada\", \"postalCode\": \"J4W1W6\" }' -s"
assertEqual "\"Art\"" $(echo $RESPONSE | jq .name)
assertEqual "\"Thursday\"" $(echo $RESPONSE | jq .openFrom)
assertEqual "\"Sunday\"" $(echo $RESPONSE | jq .openUntil)
#

## Verify that a normal get by id of earlier posted exhibition works
echo -e "\nTest 4: Verify that a normal get by id of earlier posted exhibition works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/exhibitions/${allTestExhibitionsIds[1]} -s"
assertEqual ${allTestExhibitionsIds[1]} $(echo $RESPONSE | jq .exhibitionId)
#

## Verify that a normal put of exhibition works
echo -e "\nTest 5: Verify that a normal put of exhibition works"
assertCurl 200 "curl -X PUT http://$HOST:$PORT/api/v1/exhibitions/${allTestExhibitionsIds[1]} -H \"Content-Type: application/json\" --data '{ \"exhibitionName\": \"exhibition1\",
                                                                                                                                                  \"roomNumber\" : 240, \"duration\" : 120,
                                                                                                                                                  \"startDay\": \"Monday\", \"endDay\": \"Wednesday\",
                                                                                                                                                  \"paintings\":
                                                                                                                                                  [ { \"paintingId\": \"6ae9eaf7-5ec4-45da-a85d-45a317e711a0\",
                                                                                                                                                  \"title\": \"The Two Fridas\", \"yearCreated\": 1939,
                                                                                                                                                  \"painterId\": \"0e1482bb-67a8-4620-842b-3
                                                                                                                                                  1b0a4c9f7a\" } ],
                                                                                                                                                  \"sculptures\":
                                                                                                                                                  [ { \"sculptureId\": \"29e803f5-c8aa-475c-832e-8edbb2336778\",
                                                                                                                                                  \"title\": \"Torso\", \"material\": \"Clay\",
                                                                                                                                                  \"texture\": \"Rough\", \"galleryId\": \"ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8\" } ] }' -s"
assertEqual "\"exhibition1\"" $(echo $RESPONSE | jq .exhibitionName)
assertEqual 240 $(echo $RESPONSE | jq .roomNumber)
assertEqual 120 $(echo $RESPONSE | jq .duration)

## Verify that a normal post of exhibition works
echo -e "\nTest 5: Verify that a normal post of exhibition works"
assertCurl 201 "curl -X POST http://$HOST:$PORT/api/v1/exhibitions/galleries/ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8 -H \"Content-Type: application/json\" --data '{ \"exhibitionName\": \"exhibition1\",
                                                                                                                                                  \"roomNumber\" : 240, \"duration\" : 120,
                                                                                                                                                  \"startDay\": \"Monday\", \"endDay\": \"Wednesday\",
                                                                                                                                                  \"paintings\":
                                                                                                                                                  [ { \"paintingId\": \"6ae9eaf7-5ec4-45da-a85d-45a317e711a0\",
                                                                                                                                                  \"title\": \"The Two Fridas\", \"yearCreated\": 1939,
                                                                                                                                                  \"painterId\": \"0e1482bb-67a8-4620-842b-3f7bfb7ee175\",
                                                                                                                                                  \"galleryId\": \"ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8\" },
                                                                                                                                                  { \"paintingId\": \"d2d6b05f-9cfb-4a54-ba3e-57dffe0fd5c0\",
                                                                                                                                                  \"title\": \"Mona Lisa\", \"yearCreated\": 1506, \"painterId\":
                                                                                                                                                  \"ede04dc9-9cf9-4191-8b4e-7d91234cb49c\", \"galleryId\": \"ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8\" }
                                                                                                                                                  ], \"sculptures\": [ { \"sculptureId\": \"2872b8a2-e691-4115-891c-bed7187392d1\", \"title\": \"Hand\",
                                                                                                                                                  \"material\": \"Clay\", \"texture\": \"Bumpy\", \"galleryId\": \"ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8\" },
                                                                                                                                                  {\"sculptureId\": \"29e803f5-c8aa-475c-832e-8edbb2336770\", \"title\": \"Torso\", \"material\": \"Clay\",
                                                                                                                                                  \"texture\": \"Rough\", \"galleryId\": \"ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8\" } ] }' -s"
assertEqual "\"exhibition1\"" $(echo $RESPONSE | jq .exhibitionName)
assertEqual 240 $(echo $RESPONSE | jq .roomNumber)
assertEqual 120 $(echo $RESPONSE | jq .duration)
assertEqual "\"Monday\"" $(echo $RESPONSE | jq .startDay)
assertEqual "\"Wednesday\"" $(echo $RESPONSE | jq .endDay)
#

## Verify that a normal get all exhibitions works
echo -e "\nTest 4: Verify that a normal get all exhibitions works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/exhibitions -s"
assertEqual 2 $(echo $RESPONSE | jq '. | length')
#

## Verify that a normal put of exhibition works
echo -e "\nTest 5: Verify that a normal put of exhibition works"
body=\
'{ "exhibitionName": "UpdatedExhibition",
"roomNumber" : 213, "duration" : 120,
"startDay": "Monday", "endDay": "Wednesday",
"paintings":
[ { "paintingId": "6ae9eaf7-5ec4-45da-a85d-45a317e711a2",
"title": "The Two Fridas", "yearCreated": 1939,
"painterId": "0e1482bb-67a8-4620-842b-3f7bfb7ee175",
"galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8" },
{ "paintingId": "d2d6b05f-9cfb-4a54-ba3e-57dffe0fd5c6",
"title": "Mona Lisa", "yearCreated": 1506, "painterId":
"ede04dc9-9cf9-4191-8b4e-7d91234cb49c", "galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8" }
], "sculptures": [ { "sculptureId": "2872b8a2-e691-4115-891c-bed7187392d0", "title": "Hand",
"material": "Clay", "texture": "Bumpy", "galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8" },
{"sculptureId": "29e803f5-c8aa-475c-832e-8edbb2336778", "title": "Torso", "material": "Clay",
"texture": "Rough", "galleryId": "ea85d3ba-d708-4ff3-bbbb-dd9c5c77b8e8" } ] }'
assertCurl 200 "curl -X PUT http://$HOST:$PORT/api/v1/exhibitions/${allTestExhibitionsIds[1]} -H \"Content-Type: application/json\" -d '${body}' -s"
# shellcheck disable=SC2046
assertEqual ${allTestExhibitionsIds[1]} $(echo $RESPONSE | jq .exhibitionId)
#
# shellcheck disable=SC2199
if [[ $@ == *"stop"* ]]
then
    echo "We are done, stopping the test environment..."
    echo "$ docker-compose down"
    docker-compose down
fi