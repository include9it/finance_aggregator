Installation and running
---
<h4>Tools</h4>
- JDK 17.0.7 <br/>
- Gradle 7.6.2 <br/>

Task
---
Write a simple financing application aggregator backend service
that exposes API for a web-application and allows a customer to 
fill in application form and submit data to your service. 
Your service should send customer application data to 
two financing institutions, that will provide an 
offer for this customer. Financing institution offers 
should be returned to customer as they become available 
so that one can choose the most appropriate.

---
Open API specification of FastBank API: https://shop.stage.klix.app/api/FastBank

Open API specification of SolidBank API: https://shop.stage.klix.app/api/SolidBank

---
Request example
---
SolidBank application creation example:
curl --location --request POST 'https://shop.stage.klix.app/api/SolidBank/applications' \
--header 'Content-Type: application/json' \
--data-raw '{
"phone": "+37126000000",
"email": "john.doe@klix.app",
"monthlyIncome": 150.0,
"monthlyExpenses": 10.0,
"maritalStatus": "MARRIED",
"agreeToBeScored": true,
"amount": 150.0
}'
SolidBank application retrieval example:
curl --location --request GET 'https://shop.stage.klix.app/api/SolidBank/applications/0f1bda7f-e8ee-4815-b110-41fb239eee48'