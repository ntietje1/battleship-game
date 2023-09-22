- added command line handling to Driver
  - runs human vs. ai with no arguments
  - runs local ai vs. server ai when host and port are provided
  - we made this change to be able to play on the server
- updated takeShots in BotPlayer 
  - added fields to BotPlayer
  - created helper methods
  - we made this change to improve the performance of our take shots method
- updated setup in AbstractPlayer
  - we made this change to improve the performance of our setup method
- fixed field of field issues
  - we made this change because it was a problem in our code

all other changes were new files (records for Jsons and ProxyController)