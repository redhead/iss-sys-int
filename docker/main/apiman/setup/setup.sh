#!/bin/bash

FILES=./*.json

regex="(POST|PUT)"

for f in $FILES
do
    [[ $f =~ $regex ]]
    curl -H "Content-Type: application/json" -H "Authorization: Basic YWRtaW46YWRtaW4xMjMh" -X ${BASH_REMATCH[1]} -d @$f http://localhost:8080/apiman/organizations
done

