Feature: Asking for the current user infos returns right information if authentified 

Scenario: Asking for the current user must success if authentified 
Given I'm authentified with user appUser and password appPass on the url http://localhost:8080/appname/api/authenticate 
When I ask for the current user calling "http://localhost:8080/appname/api/users/current"
Then I get OK response
And the user detail must contains:
| login | appUser |
| firstName | user |
| email | user@ua-morocco.com |
| house | Africa |
| activated | true |
