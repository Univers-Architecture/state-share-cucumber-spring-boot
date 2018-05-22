Feature: Exporting data returns right information if authentified 

Scenario: Asking to export data must success if authentified 
Given I'm authentified with user appUser and password appPass on the url http://localhost:8080/appname/api/authenticate 
When I request data export by calling "http://localhost:8080/appname/api/data/export"
Then the data detail must contains:
| login | appUser |
| appName | appname |
| data | examples of data |