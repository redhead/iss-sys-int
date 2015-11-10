#!/bin/sh

status="0"
until [ "$status" == "200" ]; do
	status=`curl -L -s -o /dev/null -w "%{http_code}" -X GET http://localhost:8080/apiman`
	echo $status
	sleep 2
done

sleep 20

urls[0]='organizations'
urls[1]='organizations/int/plans'
urls[2]='organizations/int/plans'
urls[3]='organizations/int/plans'
urls[4]='organizations/int/plans/high-volume/versions/1.0/policies'
urls[5]='organizations/int/plans/low-volume/versions/1.0/policies'
urls[6]='actions'
urls[7]='actions'
urls[8]='actions'
urls[9]='organizations/int/services'
urls[10]='organizations/int/services/order/versions/1.0'
urls[11]='actions'
urls[12]='organizations/int/services'
urls[13]='organizations/int/services/orderService/versions/1.0'
urls[14]='actions'
urls[15]='organizations/int/applications'
urls[16]='organizations/int/applications/app/versions/1.0/contracts'
urls[17]='organizations/int/applications/app/versions/1.0/contracts'
urls[18]='actions'

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

