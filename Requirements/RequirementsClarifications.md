Overview
===
I've matched sections in this to the "EXTREME COURSE PROJECT" *guitars* master
file.

General
----
- The front end is used to generate transaction output files that the back end
    will manage. ​*Your front end must also check for constraints and create
    output files that meet these constraints*​.
- There are lots of inconsistencies in the banking system requirements document
    (this is on purpose)
- You shouldn't be ​_literally_​ using underscores in your data
- Check for EoF to see when input file is complete.
- "I stated it. I'm the customer. I can change whatever requirements I want."
- We are assuming this bank is Canadian currency only [in terms of dollar bill amounts]
  - including $5, $10, $20, $50, $100
- This conversation:
      Santiago Bonada [4:55 PM] 
      the format of the bank account transaction file for each transaction is
      CC_AAAAAAAAAAAAAAAAAAAA_NNNNN_PPPPPPPP_QM while the format for current bank
      account is NNNNN_AAAAAAAAAAAAAAAAAAAA_S_PPPPPPPP. for quality of life it
      would be better if both followed the same order.

      Mike Miljanovic [4:59 PM] 
      I'll leave it up to @jeremy if he wants to change that
- you should test as much as possible

Current Bank Accounts File (Front End)
----
- the account number, active/disabled,and balance do not matter for the
    END_OF_FILE account
- Users should not be allowed to access the end_of_file account


Current Bank Accounts File & Master Bank Accounts File (Back End)
----
​- *Everyone can add an extra flag to the end of the current and master bank
    accounts files, e.g. NNNNN_AAAAAAAAAAAAAAAAAAAA_S_PPPPPPPP_Q, where Q is S
    for student plans and N for non-student plans.*​


The Front End
===

Informal Customer Requirements for the Front End/Transaction Code Details
----
- login
  - You can assume no two users have the exact same name.
  - A second log-in attempt will be rejected and ignored; subsequent
      transactions will be considered as part of the first account login
  - A user can login to a disabled account, but can't do any transactions.
    - Note that admins can re-enable accounts.
  - Use the transaction code 10 for login.
  - For the Miscellaneous information, use " A" for admin and " S" for standard logins.
- withdrawal
 - The front end deducts the 5-10 cent transaction fee every time a transaction
    is made. Do this first, so that if someone tries to withdraw 400$ from a
    400$ account, it is rejected since the 5-10 cents will be removed before
    the withdraw happens.
 - Can take variable amounts (due to the existence of cheques)
- transfer
  - Transfers are > $0
- paybill
  - For paying bills will the company name always be provided as the 2 letter
    initialism
    - at least, this assumption "is fine"
- deposit
 - restricted to canadian currency paper amounts (see above)
- create
  - Truncate account names if necessary
  - By default, accounts should be non-student plans.
  - It's up to you if you want the next account number to be sequential
- delete
  - An Admin can delete an account that has been disabled.
  - No need to reserve deleted account numbers.
- disable
- changeplan
  - it should allow changes in both directions
  - Always swap to the other plan.
  - When changing to student plan, subsequent transactions are charged the
    student rate
    - [and presumably vice versa?]
- logout
  - 00 (end of session) is the transaction code for logging out
    - If the program sees a transaction code of 00, it can ignore the rest. The
        other data does not matter for the end of session line
- Transaction Code 09
  enable – enable a bank account
  • should ask for the bank account holder’s name (as a text line)
  • should ask for the account number (as a text line)
  • should change the bank account from disabled (D) to active (A)
  • should save this information for the bank account transaction file
  • Constraints:
  o privileged transaction - only accepted when logged in admin
  mode
  o Account holder’s name must be the name of an existing
  account holder
  o Account number must be the number of the account holder
  specified

General Requirements for the Front End
----
- At the end of each session, when the logout transaction is processed, a bank
  account transaction file for the ​*session*​ is written
- the front end should check for transaction constraints. This includes trying
    to withdraw more money from an account than is allowed
- If you restart the front end and try again, it should still correctly fail to
    withdraw more money than allowed
- Must track sessions even after restart in same day:
  - The front end should keep internal track of balances...The front end should
      not be reading in transaction output files, or start fresh each session
      from the master accounts list.
  - Deposits should neither be available in the same login session ​*or in the
      same day*​. You cannot access the funds until the next day after the back
      end has applied the transactions.
- Can assume accounts will not complete more than 9999 transactions each day.
- On the nature of transfers:
    @patsmuk: Re: "perhaps two 02 lines where the first is implicitly the
    withdrawal and the second is implicitly the deposit" *This will be the
    universal procedure for transfer transactions*​
- Need to support both input from both command line input and text file; it
    should wait unless text file told it to terminate
    - That is, if they are piped in [through stdin], that's the behaviour you
        should expect
- Input numbers will always be in the format #####.## where # is a number from
    0-9. No dollar signs. Values that don't fit in this format aren't allowed.
- You probably want some way to cause the front end to terminate
- Withdraw, Deposit, Transfer, and Paybill transactions are the only ones that
    incur fees. They are not charged when an admin is logged in.
  - If a transaction is attempted but rejected, do not charge any transaction fees.
- Here's a conversation:

    Nicholas Gregorio [12:26 PM] 
    So invalid input would take them back to the last successful transaction?

    Mike Miljanovic [12:28 PM] 
    basically yes
Bank Account Transaction File
----
- This conversation:
      
      Patrick Smuk [1:23 PM] 
      Do 00 end of session log entries serve a functional purpose in the back-end, or are they just markers?

      [1:24] 
      and same question for the _MM extension, does that currently have a use?

      Mike Miljanovic [1:25 PM] 
      Just markers. Yes, it has a use :)

      Patrick Smuk [1:30 PM] 
      anything aside from 03 paybill?
      
      Mike Miljanovic [1:33 PM] 
      Not that i can think of at the moment, but that could certainly change
      
      [Me [??:?? ??]
      ??????????]

Permissions
----
- Admins can perform any action when they've logged into an account,
  unless the account is disabled. Reject log ins for non-matching
  name/accounts, even if they are an admin.
- Admins are allowed any amount that fits in the format.
    
The Back End
====

Informal Customer Requirements for the Back End
----

General Requirements for the Back End
----

Back End Error Recording
----

The Merged Bank Account Transaction File
----

Current & Master Bank Acounts File:
----
See above
