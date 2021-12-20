Feature: Verify Packt UI Functionalites 

Scenario: Login to Packt Subscriptions
	Given user launches "PacktSubscription" website 
	And user submit username and password 
	Then user should be logged in 
	
Scenario: Verify UI of all elements on screen are correct 
	When user read the test data sheet "Scenario_1" from file "PacktTestdata" 
	And verify UI of all elements are correct on screen 
	
Scenario: Verify Top navigation bar and SubOptions are correct 
	When user read the test data sheet "Scenario_2" from file "PacktTestdata" 
	Then verify Top navigation bar is displayed 
	And user clicks on menu and verify user is navigated to it 
	
Scenario: Verify 'Your suggested titles' in carousel 
	When user read the test data sheet "Scenario_3" from file "PacktTestdata" 
	Then verify "Your suggested titles" is displayed 
	And user clicks on each carousel and verify user is navigated to it 
	
Scenario: Verify search bar and verify that all titles found include that search text 
	When user read the test data sheet "Scenario_4" from file "PacktTestdata" 
	#
	And user clicks on "Browse > View All Books" menu 
	And user clicks on Clear all 
	And user apply filters on Search Page 
	And user searches products and verify all titles found include that search text 
	#
	Then User closes the browser 