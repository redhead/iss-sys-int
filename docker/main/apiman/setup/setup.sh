#!/bin/sh

status="0"
until [ "$status" == "200" ]; do
	status=`curl -L -s -o /dev/null -w "%{http_code}" -X GET http://localhost:8080/apiman`
	echo $status
	sleep 2
done

sleep 20

i=-1

urls[$((i=i+1))]='organizations'
urls[$((i=i+1))]='organizations/int/plans'
urls[$((i=i+1))]='organizations/int/plans'
urls[$((i=i+1))]='organizations/int/plans'
urls[$((i=i+1))]='organizations/int/plans/high-volume/versions/1.0/policies'
urls[$((i=i+1))]='organizations/int/plans/high-volume/versions/1.0/policies'
urls[$((i=i+1))]='organizations/int/plans/high-volume/versions/1.0/policies'
urls[$((i=i+1))]='organizations/int/plans/low-volume/versions/1.0/policies'
urls[$((i=i+1))]='organizations/int/plans/low-volume/versions/1.0/policies'
urls[$((i=i+1))]='organizations/int/plans/low-volume/versions/1.0/policies'
urls[$((i=i+1))]='organizations/int/plans/premium/versions/1.0/policies'
urls[$((i=i+1))]='organizations/int/plans/premium/versions/1.0/policies'
urls[$((i=i+1))]='actions'
urls[$((i=i+1))]='actions'
urls[$((i=i+1))]='actions'
urls[$((i=i+1))]='organizations/int/services'
urls[$((i=i+1))]='organizations/int/services/order/versions/1.0'
urls[$((i=i+1))]='actions'
urls[$((i=i+1))]='organizations/int/services'
urls[$((i=i+1))]='organizations/int/services/orderService/versions/1.0'
urls[$((i=i+1))]='actions'
urls[$((i=i+1))]='organizations/int/applications'
urls[$((i=i+1))]='organizations/int/applications/app/versions/1.0/contracts'
urls[$((i=i+1))]='organizations/int/applications/app/versions/1.0/contracts'
urls[$((i=i+1))]='actions'

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

regex="(POST|PUT)"
FILES=$DIR/*.json
i=0
for f in $FILES
do
    [[ $f =~ $regex ]]

    echo $f '->' 'http://localhost:8080/apiman/'${urls[i]}
    
	status=`curl -s -o /dev/null -w "%{http_code}" -H "Content-Type: application/json" -H "Authorization: Basic YWRtaW46YWRtaW4xMjMh" -X ${BASH_REMATCH[1]} -d @$f http://localhost:8080/apiman/${urls[i]}`	
	if [ "$status" -ge "400" ]
	then
		echo $status
		break
	fi

    i=$(($i + 1))
done

java -jar $DIR/h2.jar &
java -cp $DIR/h2.jar org.h2.tools.RunScript -url jdbc:h2:tcp://localhost/~/test -user sa -password sa -script $DIR/init-users.sql -showResults
