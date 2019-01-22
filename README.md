# runsignup-api-client
Command line API client for RunSignUp - for downloading participants.  Makes for easier automated, scripted downloads.

Build with `./gradlew build`

Built app will be found in `build/distributions/runsignup-api-client-VERSION.zip`

Run `runsignup-api-client -h` for all options

Sample download from race id 1234 would be `./runsignup-api-client -r=1234 -k=API_KEY -s=API_SECRET -f=Database.csv`
