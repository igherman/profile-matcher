# Profile Matcher 
Simple spring boot service to fulfill interview test. 

The profile matcher extracts the profile from database (Mongodb) and match it against the current campaign (currently mocked at run time).

You can test the app in two modes:
## Run the app
Pull the sources from git, run __docker-compose up -d__ locally, start the spring boot app.
execute the following curl:

``` xpath
curl --location --request GET 'http://localhost:8080/get_client_config/97983be2-98b7-11e7-90cf-082e5f28d836' --head 'Authorization: Basic dGVzdHVzZXI6cGFzc3dvcmQ='
```

During the startup, the existence of player in the db will be ensured, following data being used: 
```json
{
  "player_id": "97983be2-98b7-11e7-90cf-082e5f28d836",
  "credential": "apple_credential",
  "created": "2021-01-10 13:37:17Z",
  "modified": "2021-01-23 13:37:17Z",
  "last_session": "2021-01-23 13:37:17Z",
  "total_spent": 400,
  "total_refund": 0,
  "total_transactions": 5,
  "last_purchase": "2021-01-22 13:37:17Z",
  "active_campaigns": [],
  "devices": [
    {
      "id": 1,
      "model": "apple iphone 11",
      "carrier": "vodafone",
      "firmware": "123"
    }
  ],
  "level": 3,
  "xp": 1000,
  "total_playtime": 144,
  "country": "CA",
  "language": "fr",
  "birthdate": "2000-01-10 13:37:17Z",
  "gender": "male",
  "inventory": {
    "cash": 123,
    "coins": 123,
    "item_1": 1,
    "item_34": 3,
    "item_55": 2
  },
  "clan": {
    "id": "123456",
    "name": "Hello world clan"
  },
  "_customfield": "mycustom"
}
```
The current campaign will be mocked at the runtime:
```json
{
  "game": "mygame",
  "name":"mycampaign",
  "priority": 10.5,
  "matchers": {
    "level": {
      "min": 1,
      "max": 3
    },
    "has": {
      "country": ["US","RO","CA"],
      "items": ["item_1"]
    },
    "does_not_have": {
      "items": ["item_4"]
    }
  },
  "start_date": "2022-01-25 00:00:00Z",
  "end_date": "2022-02-25 00:00:00Z",
  "enabled": true,
  "last_updated": "2021-07-13 11:46:58Z"
}
```

## Run the tests
Pull the sources from git, run the tests. 
Test are using embedded mongodb and is not depending on local instance of mongodb.    
Currently, two scenarios are tested __player match the current campaign__ and __player does not match__. 
Values for both test are mocked. 