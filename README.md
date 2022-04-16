# CMSC436FinalProject
## Application Concept  
Sending notifications/reminders to the users of their contacts that they haven’t been in touch with recently.

## Key Functionality  
* Sending Notifications
* Asks for users permission to access the contacts and phone application
* Choosing important contacts (the contacts the user wants to get reminded on)
* Tracking time since last call or last message (user selects which option)
* User sets time limit since last call to that contact (24 hrs, 1 week, 1 month, 6 months, 1 year, custom) before receiving a reminder
* Understanding appropriate times to make calls (does not notify during night hours)
* Information of the important contacts and timestamps will be stored in shared preferences
* Deleting contacts / important contacts
* Deleting from contacts would also delete from important contacts, since the app accesses contacts application
* Deleting important contacts would not delete from actual contacts application

## Rough Architecture  
* **Home Page:** The home page consists of the important contacts in ascending order of time left before the next call reminder. If the user clicks on any of the important contacts it will send them to the dial screen with their contact. For each contact, there is a gear button to the right of their name that opens the * Adjustment Page. On the bottom right there is an add button to open the Add Important Contact Page.  
* **Add Important Contact Page:** If user already gave the application access to their Contacts and Phone, then the user will choose an important contact from their contacts and set up their reminder  
* **Adjustment Page:** On the adjustment page you would be able to change the settings (time limit, custom time limit) of the important contact and you can also delete the important contact if it’s no longer required  

## System Components  
* Intents and Activities for going between Pages and Applications
* Permissions for accessing user’s contacts and the ability to make calls
* Notifications for sending notifications to user to make calls
* Toasts for alerting that settings have been saved or new contact has been added
* Shared Preferences for storing the contact and time interval information
* User Interface for listing important contacts and buttons for adding/adjusting
* BroadcastReceiver for listening for calls made to/from important contacts
