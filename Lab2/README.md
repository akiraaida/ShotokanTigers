Lab 2
---
Notes on control points for each function
  - front end
    - doesn't actually validate stuff
  - back end is the master server
  syncs with master server 

login
----
- Front end (home team):
  - verify bank accounts file is current
    - (NOTE: seem to be many dated bank account text files)
    - (NOTE: need CURRENT file)
  - second login attempt cannot occur until logout
  - do not ask for name if admin mode
    - (Kathryn say: is testable?)
    - (Akira say: test for prompt)
- NOT front end:
  - verify session type
    - (NOTE: security things -- admin identity, any passwords?)
    - (NOTE: standard mode idenity have card holder name & number)
  - verify card holder name
