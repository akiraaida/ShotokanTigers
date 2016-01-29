Lab 2
---
Notes on control points for each function
  - front end
    - doesn't actually validate stuff
  - back end is the master server
  syncs with master server 
  
  
 
general
---
  - enforce privlege
    - admin functions:
      - transfer
      - create
      - delete
      - disable
      - changeplan
      - paybill
   - certain functions prompt admins for cardholder names & number
    - admins SHOULD be prompted
    - standard SHOULD NOT be prompted
    - functions:
      - withdraw
      - transfer
      - pay bill
      - deposit
- Make sure user ain't staring at a blank screen
  - ENSURE there is response to each command
  - even invalid commands?
- No negative numbers
  - some of these might have invalid inputs
- valid account:
  - number & cardholder name match
  - was not created this session
  - was not deleted this session
  - was not disabled this session
- NOTE:
  - Current bank accounts file is READ-ONLY
  - we write to Bank account transaction file
  
login/logout
----
- Front end (home team):
  - second login attempt cannot occur until logout
  - not allowed to access system
    - before login
    - after logout while before login
  - do not ask for name if admin mode
    - (Kathryn say: is testable?)
    - (Akira say: test for prompt)
  - read in the file
  - ensure after logout write to/out transaction file

withdraw
---
- is bank account valid
  - for logged in user in standard mode
  - for selected user in admin mode
- check that withdrawl is sent to back end with correct amount
- reject any withdrawls that
  - are to invalid accounts (duplicate point?)
  - would exceed 500 dollars
  - would cause hte bank account balance to be less than 0
  
transfer
----
- similar to withdraw (see doc)
- daily limit is 1000 not 500 
  - only in standard mode
  - not in admin mode
  - (NOTE: 1000 in the current session)
  - (Alex say: could easily cheat with another session)
    - (is this testable? rrmrmrrmmmri dunno?)
- in standard mode, what if you transfer to yourself?
  - we have to make a decision on what the reaction will be
    
paybill
----
- similar balance/account validity concerns as above
- specify company to pay to
  - specifically EC, CQ, TV
  - don't accept others
- max amount is 2000 
  - only in standard mode tho
  - can in admin mode
  
deposit
----
- validate account
- the deposited funds are separate and on hold from the actual balance

create
----
- sets initial balance to some specific number aww yaaa MONEY TREE
  - max is this number:  $99999.99
  - note: not just integer values
  - note: 2 decimal points displayed?
- can't set 2 accounts with same number
- should it be initialized to student or non-student?
  - initialize as SP? (Well that's my guess - Kathryn)

delete
----
- must be existing account holder

disable
----
- account is set from A to D

changeplan
---
- change account from SP to NP
- 

LET'S CALL DIBS FOR WRITING TEST CASES
----
  Akira
  ----
  changeplan
  disable
  delete
  create
  
  Kathryn
  ----
  withdrawal
  paybill
  deposit
  
  Alex
  ----
  login
  logout
  transfer
