# ChangeMachine
allows a user to request change for a given bill
#To get the chage
Method = GET
URL = http://localhost:8080/Change?amount=10.0

amount is mandetory param in query.
allowed amount/bill are Available bills are (1, 2, 5, 10, 20, 50, 100)

Sample Response Payload are as follow
1. Sucess Response for http://localhost:8080/Change?amount=10.0
{
    "0.1": "4",
    "0.05": "5",
    "0.01": "1"
}
where key is coing value and value is total amount of that coin.
2. Failure response for http://localhost:8080/Change?amount=1.01
{
    "error": "Bad Request",
    "error_description": "Not Valid number"
}

#To Change the coin number
Method = PUT
URL = http://localhost:8080/CoinNumber?coinnumber=100

Sample Response
{
    "0.1": "100",
    "0.05": "100",
    "0.25": "100",
    "0.01": "100"
}
