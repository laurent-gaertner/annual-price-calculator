# annual-price-calculator
GET https://annual-price-calculator.herokuapp.com/annual-value

example payload:
{"services":[{"name":"service1","period":"weekly","dayOfWeek":"friday", "price": 100},
             {"name":"service2","period":"Monthly","dayOfMonth":2,"price":1},
             {"name":"service3","period":"Monthly","dayOfMonth":"last","price":1}]}


When the period is weekly, only the dayOfWeek key will be provided.
when the period is monthly, only the dayOfMonth key will be provided.
dayOfMonth will either be an integer in the range of 1 to 27, or the string "last".
A service's price is given in cents.
Price can't be negative
If the request body does not conform to this schema the service must reply with an HTTP Bad Request status.
